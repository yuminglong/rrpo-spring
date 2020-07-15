package com.jiebao.platfrom.railway.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.FileMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.domain.Files;
import com.jiebao.platfrom.railway.service.FileService;
import com.jiebao.platfrom.system.domain.Dept;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "/file")
@Api(tags = "文件共享")   //swagger2 api文档说明示例
public class FileController extends BaseController {


    private String message;

    @Autowired
    private FileService fileService;

    /**
     * 使用Mapper操作数据库
     *
     * @return JiebaoResponse 标准返回数据类型
     */
    @GetMapping
    @ApiOperation(value = "查询数据List", notes = "查询数据List列表", response = JiebaoResponse.class, httpMethod = "GET")
    public Map<String, Object> getFileList(QueryRequest request, Dept dept) {

        return null;
    }

    /**
     * 分页查询
     *
     * @param - 分页参数
     * @return
     * @Parameters sortField  according to order by Field
     * @Parameters sortOrder  JiebaoConstant.ORDER_ASC or JiebaoConstant.ORDER_DESC
     */
  /*  @PostMapping("/getFileList")
    @ApiOperation(value = "分页查询", notes = "查询分页数据", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse getFileList(QueryRequest request) {
        IPage<Files> fileList = fileService.getFileList(request);
        return new JiebaoResponse().data(this.getDataTable(fileList));
    }*/
    @DeleteMapping("/{ids}")
    @Log("删除文件")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse delete(@PathVariable String[] ids) throws JiebaoException {

        try {
            Arrays.stream(ids).forEach(id -> {
                Files byId = fileService.getById(id);
                File file = new File(byId.getUrl());
                if (file.exists()) {
                    file.delete();
                }
                fileService.removeById(id);
            });
        } catch (Exception e) {
            throw new JiebaoException("删除失败");
        }
        return new JiebaoResponse().message("删除成功");
    }

    @PutMapping
    @Log("修改")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse updateFile(@Valid Files files) throws JiebaoException {
        try {
            fileService.updateByKey(files);
            return new JiebaoResponse().message("成功");
        } catch (Exception e) {
            message = "修改通讯录失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }


    }


    @PostMapping("/upload")
    @ApiOperation(value = "文件上传", notes = "文件上传", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse upload(@RequestParam(value = "file", required = false) MultipartFile file, Files files) {
        String path = null;
        if (file != null) {
            path = "D:/rrpo/download/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + file.getOriginalFilename();
        }
        files.setUrl(path);
        files.setTitle(file.getOriginalFilename());
        //files.setCreateUser();
        fileService.save(files);
        try {
            File fileSave = new File(path);
            if (!fileSave.getParentFile().exists()) {
                fileSave.getParentFile().mkdirs();
            }
            file.transferTo(fileSave);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JiebaoResponse().message("上传成功");
    }


    @GetMapping("/downLoad/{id}")
    @ApiOperation(value = "文件下载", notes = "文件下载", response = JiebaoResponse.class, httpMethod = "GET")
    public void downLoad(HttpServletResponse response, @PathVariable String id) {
        Files byId = fileService.getById(id);
        String downloadFilePath = byId.getUrl();
        String fileName = byId.getTitle();
        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开  
            try {
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return;
        }
        return;
    }

    @GetMapping("/{parentsId}")
    @Log("根据部门查看文件")
    @ApiOperation(value = "根据部门查看文件", notes = "根据部门查看文件", httpMethod = "GET")
    @Transactional(rollbackFor = Exception.class)
    public List<Files> findById(@PathVariable String parentsId) {
        return fileService.getByParentsId(parentsId);
    }
}
