package com.jiebao.platfrom.railway.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.NoticeMapper;
import com.jiebao.platfrom.railway.domain.Notice;
import com.jiebao.platfrom.railway.service.NoticeService;
import com.jiebao.platfrom.system.dao.FileMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "/notice")
@Api(tags = "railWay-通知公告(改)")   //swagger2 api文档说明示例
public class NoticeController extends BaseController {

    private String message;
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeService noticeService;


    @Autowired
    private FileMapper fileMapper;
    @Autowired
    DeptService deptService;


    /**
     * 使用Mapper操作数据库
     *
     * @return JiebaoResponse 标准返回数据类型
     */
    @PostMapping(value = "/getNoticeListByMapper")
    @ApiOperation(value = "查询数据List", notes = "查询数据List列表", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse getNoticeListByMapper() {
        List<Notice> list = noticeService.list();
        for (Notice i : list
        ) {
            i.setKey(i.getId());
        }
        return new JiebaoResponse().data(list).message("查询成功").put("remake", "其他数据返回");
    }

    /**
     * 分页查询
     *
     * @param request - 分页参数
     * @return
     * @Parameters sortField  according to order by Field
     * @Parameters sortOrder  JiebaoConstant.ORDER_ASC or JiebaoConstant.ORDER_DESC
     */
    @GetMapping
    @ApiOperation(value = "分页查询(发件箱)", notes = "查询分页数据（发件箱）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getNoticeList(QueryRequest request, Notice notice, String startTime, String endTime) {
        IPage<Notice> noticeList = noticeService.getNoticeList(request, notice, startTime, endTime);
        List<Notice> records = noticeList.getRecords();
        for (Notice i : records
        ) {
            i.setKey(i.getId());
        }
        return new JiebaoResponse().data(this.getDataTable(noticeList));
    }


    @GetMapping("/inbox")
    @ApiOperation(value = "分页查询(收件箱)", notes = "查询分页数据（收件箱）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getNoticeInboxList(QueryRequest request, Notice notice, String startTime, String endTime) {
        IPage<Notice> noticeList = noticeService.getNoticeInboxList(request, notice, startTime, endTime);
        List<Notice> records = noticeList.getRecords();
        for (Notice i : records
        ) {
            i.setKey(i.getId());
        }
        return new JiebaoResponse().data(this.getDataTable(noticeList));
    }

    @DeleteMapping("/{ids}")
    @Log("删除通知公告")
    @ApiOperation(value = "批量删除", notes = "批量删除", response = JiebaoResponse.class, httpMethod = "DELETE")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse delete(@PathVariable String[] ids) throws JiebaoException {
        try {
            Arrays.stream(ids).forEach(id -> {
                Notice byId = noticeService.getById(id);
                //未发布时才能删掉本体信息,否则只改状态为4
                if ("1".equals(byId.getStatus()) || "2".equals(byId.getStatus())) {
                    noticeService.removeById(id);
                } else {
                    noticeMapper.updateStatus(id);
                }
            });
        } catch (Exception e) {
            throw new JiebaoException("删除失败");
        }
        return new JiebaoResponse().message("删除成功");
    }

    @PostMapping
    @ApiOperation(value = "新增通知公告", notes = "新增通知公告", response = JiebaoResponse.class, httpMethod = "POST")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse addNotice(@Valid Notice notice, String[] fileIds) {
        notice.setStatus("1");
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        notice.setCreateUser(username);
        noticeService.save(notice);
        if (fileIds != null) {
            Arrays.stream(fileIds).forEach(fileId -> {
                fileMapper.updateNoticeByFileId(fileId, notice.getId());
            });
        }
        return new JiebaoResponse().message("成功");
    }

    @PutMapping
    @Log("修改通知公告")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "修改", notes = "修改", response = JiebaoResponse.class, httpMethod = "PUT")
    public void updateNotice(@Valid Notice notice) throws JiebaoException {
        try {
            this.noticeService.updateByKey(notice);
        } catch (Exception e) {
            message = "修改通讯录失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @GetMapping("/revoke/{noticeIds}")
    @Log("撤销通知公告")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "撤销通知公告", notes = "撤销通知公告", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse revoke(@PathVariable String[] noticeIds) throws JiebaoException {
        try {
            if (noticeIds != null) {
                Arrays.stream(noticeIds).forEach(noticeId -> {
                    //状态status改为2
                    noticeService.revokeNotice(noticeId);
                });
                return new JiebaoResponse().message("撤销通知公告成功");
            }
        } catch (Exception e) {
            message = "撤销通知公告失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
        return new JiebaoResponse().message("通知公告撤销失败");

    }


    @GetMapping("/release/{noticeIds}")
    @Log("发布通知公告")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "发布通知公告", notes = "发布通知公告", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse release(@PathVariable String[] noticeIds) throws JiebaoException {
        try {
            if (noticeIds != null) {
                Arrays.stream(noticeIds).forEach(noticeId -> {
                    //状态status改为3
                    noticeService.release(noticeId);
                    Notice byId = noticeService.getById(noticeId);
                    if (byId.getSynchronizeWeb() == 1) {
                    /*    String url = "http://192.168.20.105:123/push";
                      //  String url ="http://192.168.20.105:123/push?channelId=4&title=123&html=123";
                        HttpClient client = HttpClients.createDefault();
                        //post
                        HttpPost post = new HttpPost(url);
                        //拼接参数
                        JSONObject json = new JSONObject();
                        json.put("channelId",4);
                        json.put("title", byId.getTitle());
                        json.put("content",byId.getContent());
                        json.put("createTime", byId.getCreateTime());
                        try {
                            post.addHeader("Content-type", "application/json; charset=utf-8");
                            post.setHeader("Accept", "application/json");
                            post.setEntity(new StringEntity(json.toString(), Charset.forName("utf-8")));
                            client.execute(post);
                            System.out.println("+++++++++++++传参数了++++++++++++++++++");
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                        //1.创建HttpClient对象
                        CloseableHttpClient httpClient= HttpClients.createDefault();
                        //2.创建HttpPost对象，设置URL地址
                        HttpPost httpPost=new HttpPost("http://192.168.20.105:123/push");;

                        //声明list集合，用来分装表单中的参数
                        //要求：设置请求的地址是：http://192.168.20.105:123/push?channelId=4&title=xxx&html=xxx
                        List<NameValuePair> params=new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("channelId","4"));
                        params.add(new BasicNameValuePair("title",byId.getTitle()));
                        params.add(new BasicNameValuePair("html",byId.getContent()));
                        // 创建表单的Entity对象,第一个参数是封装好的表单数据，第二个参数就是编码方式
                        UrlEncodedFormEntity formEntity= null;
                        try {
                            formEntity = new UrlEncodedFormEntity(params,"utf8");
                        } catch (UnsupportedEncodingException e) {

                            e.printStackTrace();
                        }
                        //设置表单的Entity对象到Post请求中
                        httpPost.setEntity(formEntity);


                        //使用httpClient发起响应获取repsonse
                        CloseableHttpResponse response=null;
                        try {
                            response=httpClient.execute(httpPost);
                            //4.解析响应，获取数据
                            //判断状态码是否是200
                            if(response.getStatusLine().getStatusCode()==200){
                                HttpEntity httpEntity=response.getEntity();
                                String content= EntityUtils.toString(httpEntity,"utf8");
                                System.out.println(content.length());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                response.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                httpClient.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                return new JiebaoResponse().message("发布通知公告成功");
            }
        } catch (Exception e) {
            message = "发布通知公告失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
        return new JiebaoResponse().message("发布通知公告失败");
    }

    @GetMapping(value = "/getList")
    @ApiOperation(value = "List", notes = "List列表", response = JiebaoResponse.class, httpMethod = "GET")
    public List<Notice> getList(Notice notice, QueryRequest request) {
        return noticeService.getNoticeLists(notice, request);
    }


}
