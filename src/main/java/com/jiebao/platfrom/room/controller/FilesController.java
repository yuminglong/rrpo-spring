package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.room.service.IFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/room/file")
@Api(tags = "附件上传")
public class FilesController {
    @Resource
   IFilesService filesService;

    @ApiOperation("文件上传")
    @Log(value = "文件上传")
    @PostMapping("upload")
    public List<String> fileImgSave(@RequestParam("filename") MultipartFile[] files, HttpServletRequest request) {
        System.out.println(files);
        List<String> upload =null; //uploadUtil.upload(files, request);
        return upload;
    }

    @DeleteMapping(value = "delete/{id}")
    @ApiOperation("附件删除")
    @Log(value = "附件删除")
    private JiebaoResponse deleteById(String id) {
        return new JiebaoResponse().message(filesService.removeById(id) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "getFile")
    @ApiOperation("查询指定附件")
    @Log(value = "查询指定附件")
    private JiebaoResponse getFile(String id) {
        return new JiebaoResponse().data(filesService.getById(id)).message("查询成功");
    }


}
