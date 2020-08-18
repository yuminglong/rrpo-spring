package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.domain.Tree;
import com.jiebao.platfrom.common.utils.SortUtil;
import com.jiebao.platfrom.common.utils.TreeUtil;
import com.jiebao.platfrom.railway.dao.PrizeTypeMapper;
import com.jiebao.platfrom.railway.dao.PublicFileMapper;
import com.jiebao.platfrom.railway.domain.PrizeType;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.railway.service.PrizeTypeService;
import com.jiebao.platfrom.railway.service.PublicFileService;
import com.jiebao.platfrom.system.domain.File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author yf
 */
@Slf4j
@Service("PublicFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PublicFileServiceImpl extends ServiceImpl<PublicFileMapper, PublicFile> implements PublicFileService {


    @Autowired
    PublicFileMapper publicFileMapper;

    @Autowired
    PublicFileService publicFileService;

    @Override
    public Map<String, Object> findpublicFileList(QueryRequest request, PublicFile publicFile) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<PublicFile> publicFiles = findPublicFiles(publicFile, request);
            List<Tree<PublicFile>> trees = new ArrayList<>();
            buildTrees(trees, publicFiles);
            Tree<PublicFile> publicFileTree = TreeUtil.build(trees);
            result.put("rows", publicFileTree);
            result.put("total", publicFiles.size());
        } catch (Exception e) {
            log.error("获取组织机构列表失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }


    private void buildTrees(List<Tree<PublicFile>> trees, List<PublicFile> publicFiles) {
        publicFiles.forEach(publicFile -> {
            Tree<PublicFile> tree = new Tree<>();
            tree.setId(publicFile.getId());
            tree.setKey(tree.getId());
            tree.setParentId(publicFile.getParentId());
            tree.setName(publicFile.getName());
            tree.setMark(publicFile.getMark());
            trees.add(tree);
        });
    }


    @Override
    public List<PublicFile> findPublicFiles(PublicFile publicFile, QueryRequest request) {
        QueryWrapper<PublicFile> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(publicFile.getName())) {
            queryWrapper.lambda().eq(PublicFile::getName, publicFile.getName());
        }
        SortUtil.handleWrapperSort(request, queryWrapper, "creatTime", JiebaoConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }


    @Override
    @Transactional
    public void createPublicFile(PublicFile publicFile) {
        String parentId = publicFile.getParentId();
        if (parentId == null) {
            publicFile.setParentId("0");
        }
        this.save(publicFile);
    }

    @Override
    public List<PublicFile> findChilderPublicFile(String id) {
        LambdaQueryWrapper<PublicFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PublicFile::getParentId, id);
        return this.baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<PublicFile> getPublicFileListById(String publicFileId) {
        Map<String, Object> map = new HashMap<>();
        map.put("parent_id", publicFileId);
        List<PublicFile> publicFiles = publicFileMapper.selectByMap(map);
        for (PublicFile p : publicFiles
        ) {
            if (publicFiles == null) {
                p.setHasChildren(false);
            }
            List<File> files = publicFileMapper.selectFiles(publicFileId);
            p.setFiles(files);
        }
        return publicFiles;
    }
}
