package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.domain.Tree;
import com.jiebao.platfrom.common.utils.SortUtil;
import com.jiebao.platfrom.common.utils.TreeUtil;
import com.jiebao.platfrom.railway.dao.PrivateFileMapper;
import com.jiebao.platfrom.railway.dao.PublicFileMapper;
import com.jiebao.platfrom.railway.domain.PrivateFile;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.railway.service.PrivateFileService;
import com.jiebao.platfrom.railway.service.PublicFileService;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yf
 */
@Slf4j
@Service("PrivateFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PrivateFileServiceImpl extends ServiceImpl<PrivateFileMapper, PrivateFile> implements PrivateFileService {


    @Autowired
    UserService userService;

    @Override
    public Map<String, Object> findPrivateFileList(QueryRequest request, PrivateFile privateFile) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<PrivateFile> privateFiles = findPrivateFiles(privateFile, request);
            List<Tree<PrivateFile>> trees = new ArrayList<>();
            buildTrees(trees, privateFiles);
            Tree<PrivateFile> privateFileTree = TreeUtil.build(trees);
            result.put("rows", privateFileTree);
            result.put("total", privateFiles.size());
        } catch (Exception e) {
            log.error("获取组织机构列表失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }


    private void buildTrees(List<Tree<PrivateFile>> trees, List<PrivateFile> privateFiles) {
        privateFiles.forEach(privateFile -> {
            Tree<PrivateFile> tree = new Tree<>();
            tree.setId(privateFile.getId());
            tree.setKey(tree.getId());
            tree.setParentId(privateFile.getParentId());
            tree.setText(privateFile.getName());
            tree.setMark(privateFile.getMark());
            trees.add(tree);
        });
    }


    @Override
    public List<PrivateFile> findPrivateFiles(PrivateFile privateFile, QueryRequest request) {
        QueryWrapper<PrivateFile> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User byName = userService.findByName(username);
        queryWrapper.lambda().eq(PrivateFile::getUserId,byName.getUserId());
        if (StringUtils.isNotBlank(privateFile.getName())){
            queryWrapper.lambda().eq(PrivateFile::getName, privateFile.getName());
        }
        SortUtil.handleWrapperSort(request, queryWrapper, "creatTime", JiebaoConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }



    @Override
    @Transactional
    public void createPrivateFile(PrivateFile privateFile) {
        String parentId = privateFile.getParentId();
        if (StringUtils.isNotBlank(parentId)){
            privateFile.setParentId("0");
        }
        this.save(privateFile);
    }

    @Override
    public List<PrivateFile> findChilderPrivateFile(String id) {
        LambdaQueryWrapper<PrivateFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PrivateFile::getParentId, id);
        return this.baseMapper.selectList(lambdaQueryWrapper);
    }
}
