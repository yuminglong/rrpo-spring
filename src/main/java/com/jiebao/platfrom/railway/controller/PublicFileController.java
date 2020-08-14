/*

package com.jiebao.platfrom.railway.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.domain.Tree;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.railway.service.PublicFileService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/publicFile")
@Api(tags = "railWay-公共信息公共文件")   //swagger2 api文档说明示例
public class PublicFileController extends BaseController {

    private String message;

    @Autowired
    private PublicFileService publicFileService;


    @GetMapping
    public Map<String, Object> publicFileList(QueryRequest request, PublicFile publicFile) {
        return this.publicFileService.findpublicFileList(request, publicFile);
    }

    @Log("新增文件夹")
    @PostMapping
    public void addPublicFile(@Valid PublicFile publicFile) throws JiebaoException {
        try {
            this.publicFileService.createPublicFile(publicFile);
        } catch (Exception e) {
            message = "新增文件夹失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @Log("删除文件夹")
    @DeleteMapping("/{publicFileIds}")
    public void deletePublicFiles(@NotBlank(message = "{required}") @PathVariable String publicFileIds) throws JiebaoException {
        try {
            String[] ids = publicFileIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id -> {
                List<PublicFile> list = this.publicFileService.findChilderPublicFile(id);
                if (list.size() > 0) {
                    list.forEach(publicFile -> {
                        publicFileService.removeById(publicFile.getId());
                    });
                }
                publicFileService.removeById(id);
            });
        } catch (Exception e) {
            message = "删除文件夹失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @Log("修改文件夹")
    @PutMapping
    public void updatePublicFile(@Valid PublicFile publicFile) throws JiebaoException {
        try {
            this.publicFileService.updatePublicFile(publicFile);
        } catch (Exception e) {
            message = "修改文件夹失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }




 @GetMapping("/publicFile")
    public Tree<PublicFile> publicFileList(QueryRequest request, PublicFile publicFile) {
        return this.publicFileService.findPublicFile(request, publicFile);
    }

}

*/
