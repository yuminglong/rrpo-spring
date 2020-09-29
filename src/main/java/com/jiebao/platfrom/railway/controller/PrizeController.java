package com.jiebao.platfrom.railway.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.common.utils.EqualsMonth;
import com.jiebao.platfrom.demo.test.WorderToNewWordUtils;
import com.jiebao.platfrom.railway.dao.*;
import com.jiebao.platfrom.railway.domain.*;
import com.jiebao.platfrom.railway.service.*;
import com.jiebao.platfrom.system.dao.DeptMapper;
import com.jiebao.platfrom.system.dao.FileMapper;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.dao.UserRoleMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.Role;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.domain.UserRole;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.RoleService;
import com.jiebao.platfrom.system.service.UserRoleService;
import com.jiebao.platfrom.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.ContinueNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 一事一奖内容表
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
@Slf4j
@RestController
@RequestMapping("/prize")
@Api(tags = "railWay-一事一奖")   //swagger2 api文档说明示例
public class PrizeController extends BaseController {


    //private final String HOST = "http://192.168.10.104:9527";
    private final String HOST = "http://114.115.147.159:9527";


    private String message;

    @Autowired
    private PrizeUserService prizeUserService;

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private PrizeOrderService prizeOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PrizeLimitMapper prizeLimitMapper;

    @Autowired
    private PrizeUserMapper prizeUserMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private PrizeOpinionMapper prizeOpinionMapper;

    @Autowired
    private PrizeOpinionService prizeOpinionService;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private PrizeRejectOpinionService prizeRejectOpinionService;

    @Autowired
    private PrizeRejectOpinionMapper prizeRejectOpinionMapper;

    /**
     * 创建一条一事一奖
     */


    @PostMapping("/save")
    @ApiOperation(value = "创建一条一事一奖", notes = "创建一条一事一奖", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse creatPrize(@Valid Prize prize, String[] fileIds) throws JiebaoException {
        try {
            String newNumber =null;
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            prize.setCreatUser(username);
            //获得number最大值
            //生成编号
            Integer maxNumber = prizeMapper.findMaxNumber();
            String maxNumberString = (maxNumber + 1) + "";
            for (int i = 0; i < 4; i++) {
                if (maxNumberString.length() == 4) {
                    break;
                } else {
                    maxNumberString = "0" + maxNumberString;
                }
            }
            String place = prize.getPlace();
            if (place.contains("衡阳市")) {
                 newNumber = "01" + maxNumberString;
            }
            else if (place.contains("岳阳市")){
                newNumber = "02" + maxNumberString;
            }
            else if (place.contains("怀化市")){
                newNumber = "03" + maxNumberString;
            }
            else if (place.contains("娄底市")){
                newNumber = "04" + maxNumberString;
            }


            prize.setNewNumber(newNumber);
            boolean save = prizeService.save(prize);
            Arrays.stream(fileIds).forEach(fileId -> {
                fileMapper.updateByFileId(fileId, prize.getId());
            });
            Dept dept = deptService.getById(byName.getDeptId());
            if (dept.getRank() == 2) {
                //获取该区县组织机构，则getParentId就是获取它上级组织机构ID
                Dept byId = deptService.getById(byName.getDeptId());
                //把要发送的deptId保存到数据库
                prizeUserService.saveByDept(prize.getId(), byId.getParentId());
                //市级发送自身组织机构id
            } else if (dept.getRank() == 1) {
                Dept byId = deptService.getById(byName.getDeptId());
                //把要发送的deptId保存到数据库
                prizeUserService.saveByDept(prize.getId(), byId.getDeptId());
            } else {
                return new JiebaoResponse().message("无权限");
            }
            return new JiebaoResponse().message("创建一条一事一奖成功");

        } catch (Exception e) {
            message = "创建一事一奖失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    /**
     * 批量发布一事一奖
     */
    @GetMapping("/{prizeIds}")
    @ApiOperation(value = "批量发布一事一奖", notes = "批量发布一事一奖", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse creatPrize(@PathVariable String[] prizeIds) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            PrizeLimit prizeLimit = prizeLimitMapper.selectOne(new LambdaQueryWrapper<PrizeLimit>().eq(PrizeLimit::getDeptId, byName.getDeptId()));
            Integer limitNumber;
            if (prizeLimit == null) {
                //如果无记录，则设置默认限制上报次数如果为10,则要10-1即应为9
                limitNumber = 219;
            } else {
                limitNumber = prizeLimit.getLimitNumber();
            }
            //获取当前年月，该组织机构已经发布条数 ，然后+1，set进releaseNumber
            Integer countByDept = prizeOrderService.getCountByDept(byName.getDeptId());
            if (countByDept <= limitNumber) {
                Arrays.stream(prizeIds).forEach(prizeId -> {
                    Prize prize = prizeService.getById(prizeId);
                    if (prize.getStatus() == 1) {
                        PrizeOrder prizeOrderSet = new PrizeOrder();
                        prizeOrderSet.setDeptId(byName.getDeptId());
                        prizeOrderSet.setCreatTime(prize.getReleaseTime());
                        prizeOrderSet.setPrizeId(prizeId);
                        prizeOrderSet.setTitle(prize.getContent());
                        prizeOrderSet.setReleaseNumber(countByDept + 1);
                        prizeOrderService.save(prizeOrderSet);
                        //把status改为3,并创建发布时间
                        prizeMapper.release(prizeId);
                        prizeUserMapper.setCreatTime(prizeId);
                    }
                });
            } else {
                return new JiebaoResponse().message("超出发布次数限制，本月限制发布条数为" + (limitNumber + 1) + "，已超出！");
            }
            return new JiebaoResponse().message("发布一事一奖成功");
        } catch (Exception e) {
            throw new JiebaoException("发布一事一奖失败");
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "prizeIds", value = "一事一奖的id")
    })
    @DeleteMapping("/{prizeIds}")
    @ApiOperation(value = "删除一事一奖（包括未发布的，已发布的，已发布的并不会真正删除，但是查询会显示不出）", notes = "删除一事一奖（包括未发布的，已发布的，已发布的并不会真正删除，但是查询会显示不出）", response = JiebaoResponse.class, httpMethod = "DELETE")
    public JiebaoResponse deletePrize(@PathVariable String[] prizeIds) throws JiebaoException {

        try {

            System.out.println(prizeIds);
            Arrays.stream(prizeIds).forEach(prizeId -> {
                //不能直接删掉文件（暂时未做文件），不能删除接收人，不能删除该信息本体，直接改状态 status为4
                Prize byId = prizeService.getById(prizeId);
                //1为未发送状态
                if (byId.getStatus() == 1) {
                    //删除接收的组织机构
                    prizeUserService.deleteByPrizeId(prizeId);
                    //删除内容本体（文件还没加哦）
                    prizeService.removeById(prizeId);
                    //3为已发送状态，只需改状态为4
                    message = "删除成功";
                } else if (byId.getStatus() == 3) {
                    message = "删除失败";
                } else if (byId.getStatus() == 2) {
                    //prizeUserService.ByPrizeId(prizeId);
                    message = "删除失败";
                }
            });
            return new JiebaoResponse().message(message);
        } catch (Exception e) {
            message = "删除一事一奖失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @PutMapping("/update")
    @ApiOperation(value = "修改未发布的一事一奖（已发布的不能修改）", notes = "修改发布的一事一奖（已发布的不能修改）", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse updatePrize(@Valid Prize prize) throws JiebaoException {
        try {
            this.prizeService.updateById(prize);
            return new JiebaoResponse().message("修改未发送的一事一奖成功");
        } catch (Exception e) {
            message = "修改一事一奖失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @GetMapping
    @ApiOperation(value = "分页查询发件箱（未发送的和已发送的）", notes = "分页查询发件箱（未发送的和已发送的）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getPrizeList(QueryRequest request, Prize prize, String startTime, String endTime) {
        IPage<Prize> prizeList = prizeService.getPrizeList(request, prize, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(prizeList));
    }


    @GetMapping("/inbox")
    @ApiOperation(value = "分页查询收件箱", notes = "分页查询收件箱", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getPrizeInboxList(QueryRequest request, Prize prize, String startTime, String endTime) {
        IPage<Prize> prizeList = prizeService.getPrizeInboxList(request, prize, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(prizeList));
    }

    @PostMapping("/info")
    @ApiOperation(value = "详情", notes = "详情", response = JiebaoResponse.class, httpMethod = "POST")
    public Prize getPrizeInfo(String prizeId) {
        Prize byId = prizeService.getById(prizeId);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("prize_id", prizeId);
        List<PrizeOpinion> prizeOpinions = prizeOpinionMapper.selectByMap(hashMap);
        byId.setOpinions(prizeOpinions);
        return byId;
    }

    @PostMapping("/report")
    @ApiOperation(value = "审批并上报", notes = "审批并上报", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse PrizeReport(String auditOpinion, String moneys, String[] prizeIds, String sendDeptId) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            Dept byId = deptService.getById(byName.getDeptId());
            List<Dept> depts = new ArrayList<>();
            if (byId.getRank() == 1) {
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("dept_id", sendDeptId);
                depts = deptMapper.selectByMap(columnMap);

            } else if (byId.getRank() == 0) {
                depts = null;
            } else {
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("dept_id", byId.getParentId());
                depts = deptMapper.selectByMap(columnMap);
            }
            List<Dept> finalDepts = depts;
            System.out.println(prizeIds + "**********************");
            Arrays.stream(prizeIds).forEach(prizeId -> {
                //查询该级组织机构是否有审批内容
                Integer result = prizeOpinionMapper.selectOpinion(byId.getRank(), prizeId);
                if (result == 0) {
                    //如果不为省级，就上报
                    if (byId.getRank() != 0 && finalDepts != null) {
                        for (Dept dept : finalDepts
                        ) {
                            //把要发送的deptId保存到数据库
                            prizeUserService.saveByDept(prizeId, dept.getDeptId());
                        }
                    }
                    // 0是省
                    if (byId.getRank() == 0) {
                        prizeMapper.updateStatusForPro(prizeId);
                    }
                    //4为铁护办
                    if (byId.getRank() == 4) {
                        prizeMapper.updateStatusForIron(prizeId);
                    }
                    //1为市
                    if (byId.getRank() == 1) {
                        prizeMapper.updateStatusForCity(prizeId);
                    }

                    PrizeOpinion prizeOpinion = new PrizeOpinion();
                    prizeOpinion.setRank(byId.getRank());
                    //id必须传来
                    prizeOpinion.setPrizeId(prizeId);
                    prizeOpinion.setAuditOpinion(auditOpinion);
                    // prizeOpinion.setMoney(prizeOpinion.getMoney());
                    prizeOpinionService.saveOrUpdate(prizeOpinion);
                }
            });
            //解析json数组
            JSONArray jsonArray = JSON.parseArray(moneys);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String prizeId = jsonObject.getString("prizeId");
                String opinionMoney = jsonObject.getString("opinionMoney");
                System.out.println(jsonObject.getString("prizeId") + ":" + jsonObject.getInteger("opinionMoney"));
                prizeOpinionService.saveByPrizeId(prizeId, opinionMoney, byId.getRank());
            }


            return new JiebaoResponse().message("审批或上报成功").put("status", "200");
        } catch (Exception e) {
            message = "审批或上报失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @GetMapping("/reject/list")
    @ApiOperation(value = "驳回", notes = "驳回", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse prizeReject(String[] prizeIds, String rejectAuditOpinion) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            Arrays.stream(prizeIds).forEach(prizeId -> {
                this.prizeMapper.reject(prizeId);
                PrizeRejectOpinion prj = new PrizeRejectOpinion();
                prj.setAuditOpinion(rejectAuditOpinion);
                prj.setId(prizeId);
                prj.setUserId(byName.getUserId());
                prizeRejectOpinionService.saveOrUpdate(prj);
            });
            return new JiebaoResponse().message("驳回成功").put("status", "200");
        } catch (Exception e) {
            message = "驳回失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }

    }


    @GetMapping("/findRejectOpinion")
    @ApiOperation(value = "驳回意见查询", notes = "驳回意见查询", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse findRejectOpinion(String prizeId) {
        PrizeRejectOpinion prizeRejectOpinion = prizeRejectOpinionMapper.selectOne(new LambdaQueryWrapper<PrizeRejectOpinion>().eq(PrizeRejectOpinion::getId, prizeId));
        if (prizeRejectOpinion != null) {
            User byId = userService.getById(prizeRejectOpinion.getUserId());
            Dept dept = deptService.getById(byId.getDeptId());
            prizeRejectOpinion.setRank(dept.getRank());
            prizeRejectOpinion.setUserName(byId.getUsername());
            return new JiebaoResponse().data(prizeRejectOpinion).put("status", "200");
        } else {
            return new JiebaoResponse().data("").put("status", "200");
        }
    }


    @GetMapping("/findOpinion")
    @ApiOperation(value = "意见查询", notes = "意见查询", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse findOpinion(String prizeId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("prize_id", prizeId);
        List<PrizeOpinion> prizeOpinions = prizeOpinionMapper.selectByMap(map);
        return new JiebaoResponse().data(prizeOpinions).put("status", "200");
    }

   /* @GetMapping("/briefing")
    @ApiOperation(value = "简报", notes = "简报", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse briefing(String startTime, String endTime) throws JiebaoException {
        try {
            List<Prize> prizes = prizeMapper.selectByBriefing(startTime, endTime);
            for (Prize p : prizes
            ) {
                PrizeOpinion prizeOpinion = prizeOpinionMapper.selectOne(new LambdaQueryWrapper<PrizeOpinion>().eq(PrizeOpinion::getPrizeId, p.getId()).eq(PrizeOpinion::getRank, 0));
                if (prizeOpinion != null) {
                    String money = prizeOpinion.getMoney();
                    p.setBriefingMoney(money);
                }
            }
            return new JiebaoResponse().data(prizes).put("status", "200");
        } catch (Exception e) {
            message = "生成简报失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }*/

    @GetMapping("/briefing")
    @ApiOperation(value = "简报info", notes = "简报info", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse briefing(QueryRequest request, Prize prize, String startTime, String endTime) {
        IPage<Prize> prizeList = prizeService.getBriefing(request, prize, startTime, endTime);
        List<Prize> records = prizeList.getRecords();
        for (Prize p : records
        ) {
            PrizeOpinion prizeOpinion = prizeOpinionMapper.selectOne(new LambdaQueryWrapper<PrizeOpinion>().eq(PrizeOpinion::getPrizeId, p.getId()).eq(PrizeOpinion::getRank, 0));
            if (prizeOpinion != null) {
                String money = prizeOpinion.getMoney();
                p.setBriefingMoney(money);
            }
        }
        return new JiebaoResponse().data(this.getDataTable(prizeList));
    }


    @PostMapping("/briefingWord")
    @ApiOperation(value = "生成简报（不带金额和带金额）", notes = "生成简报（不带金额和带金额）", response = JiebaoResponse.class, httpMethod = "POST")
    public void briefingWord(Integer moneyType, QueryRequest request, Prize prize, String startTime, String endTime, String period, String year, String month, String day) throws FileNotFoundException {

        System.out.println(moneyType + "---------------------");
        IPage<Prize> prizeList = prizeService.getBriefing(request, prize, startTime, endTime);
        List<Prize> records = prizeList.getRecords();

        Map<String, String> map = new HashMap<>();
        System.out.println(period + year + month + day + "-------------------------------");
        map.put("period", period);
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);


        List<String[]> testList = new ArrayList<>();
        if (moneyType == 0) {
            for (Prize p :
                    records) {
                testList.add(new String[]{p.getNumber(), p.getPlace(), p.getContent()});
            }
            //模板文件地址
            //String inputUrl = GetResource.class.getClassLoader().getResource("tempDoc.docx").getPath();
            String inputUrl = "/usr/local/rrpo/word/tempDoc.docx";


            System.out.println("-------------" + inputUrl + "---------------------");
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String oldName = "湖南铁路护路联防简报" + year + "年第" + period + "期-不带金额" + "(" + df.format(date) + ")" + ".docx";
            //新生产的模板文件
            String newName = UUID.randomUUID().toString();


            //String outputUrl = "D:/upload/words/" + newName;
            String outputUrl = "/usr/local/rrpo/upload/" + newName;
            String outPath = outputUrl + ".docx";
            WorderToNewWordUtils.changWord(inputUrl, outPath, map, testList);
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            this.saveFile("2", "10", byName.getUserId(), oldName, newName, true);
            //附带金额
        } else if (moneyType == 1) {
            for (Prize p :
                    records) {
                PrizeOpinion prizeOpinion = prizeOpinionMapper.selectOne(new LambdaQueryWrapper<PrizeOpinion>().eq(PrizeOpinion::getPrizeId, p.getId()).eq(PrizeOpinion::getRank, 0));
                if (prizeOpinion != null) {
                    testList.add(new String[]{p.getNumber(), p.getPlace(), p.getContent(), (prizeOpinion.getMoney() + "元")});
                } else {
                    testList.add(new String[]{p.getNumber(), p.getPlace(), p.getContent(), " "});
                }

            }
            //模板文件地址
            //String inputUrl = GetResource.class.getClassLoader().getResource("tempDoc_amount.docx").getPath();
            String inputUrl = "/usr/local/rrpo/word/tempDoc_amount.docx";


            System.out.println("-------------" + inputUrl + "---------------------");
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String oldName = "湖南铁路护路联防简报" + year + "年第" + period + "期-附带金额" + "(" + df.format(date) + ")" + ".docx";
            //新生产的模板文件
            String newName = UUID.randomUUID().toString();


            //String outputUrl = "D:/upload/words/" + newName;
            String outputUrl = "/usr/local/rrpo/upload/" + newName;
            String outPath = outputUrl + ".docx";
            WorderToNewWordUtils.changWord(inputUrl, outPath, map, testList);
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            this.saveFile("2", "10", byName.getUserId(), oldName, newName, true);
        }


    }


    private JiebaoResponse saveFile(String fileType, String refType, String userId, String oldName, String newName, boolean status) {
        String path = "";   //上传地址
        String accessPath = ""; //文件访问虚拟地址


        //path = "D:/upload/words/";
        path = "/usr/local/rrpo/upload/";
        accessPath = "/jbx/cdn/file/";
        String currentTimeFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
        java.io.File currentFile = new java.io.File(path + newName + ".docx");
        accessPath = HOST + accessPath + newName + ".docx";   //上传后完整文件虚拟访问地址
        //创建文件信息对象保存至数据库
        com.jiebao.platfrom.system.domain.File file = new com.jiebao.platfrom.system.domain.File();
        file.setFileType(fileType);
        file.setOldName(oldName);
        file.setNewName(newName + ".docx");
        file.setFileUrl(path);
        file.setAccessUrl(accessPath);
        file.setRefType(refType);
        file.setFileSuffix(".docx");
        file.setStatus(status);
        file.setTime(new Date());
        file.setUserId(userId);

        if (file.insert()) {
            return new JiebaoResponse().put("status", true).put("file", file);
        } else {
            currentFile.delete();
            return new JiebaoResponse().put("status", false).message("上传服务异常，上传失败，请重试！");
        }
    }


    /**
     * 文件记录删除
     *
     * @param fileId
     * @return
     */
    @ApiOperation("文件记录删除")
    @PostMapping("/deleteFile")
    public JiebaoResponse deleteFile(String fileId) {
        int i = fileMapper.deleteById(fileId);
        return new JiebaoResponse().data(i);
    }





}
