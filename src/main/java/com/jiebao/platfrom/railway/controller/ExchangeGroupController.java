package com.jiebao.platfrom.railway.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.ExchangeGroupMapper;
import com.jiebao.platfrom.railway.domain.ExchangeGroup;
import com.jiebao.platfrom.railway.domain.ExchangeType;
import com.jiebao.platfrom.railway.service.ExchangeGroupService;
import com.jiebao.platfrom.railway.service.ExchangeTypeService;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "/exchangeGroup")
@Api(tags = "railWay-信息互递自定义分组")   //swagger2 api文档说明示例
public class ExchangeGroupController extends BaseController {


    private String message;

    @Autowired
    private ExchangeGroupService exchangeGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeGroupMapper exchangeGroupMapper;

    @PostMapping
    @Log("新增分组")
    @ApiOperation(value = "新增分组", notes = "新增分组", response = JiebaoResponse.class, httpMethod = "POST")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse addExchangeGroup(@Valid ExchangeGroup exchangeGroup) {
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User byName = userService.findByName(username);
        exchangeGroup.setUserId(byName.getUserId());
        exchangeGroupService.save(exchangeGroup);
        return new JiebaoResponse().message("成功");
    }

    @DeleteMapping("/{ids}")
    @Log("删除分组")
    @ApiOperation(value = "批量删除分组", notes = "批量删除分组", response = JiebaoResponse.class, httpMethod = "DELETE")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse delete(@PathVariable String[] ids) throws JiebaoException {
        try {
            Arrays.stream(ids).forEach(id -> {
                exchangeGroupService.removeById(id);
            });
        } catch (Exception e) {
            throw new JiebaoException("删除失败");
        }
        return new JiebaoResponse().message("删除成功");
    }

    @PutMapping
    @Log("修改分组")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "修改分组", notes = "修改分组", response = JiebaoResponse.class, httpMethod = "PUT")
    public void updateExchangeGroup(@Valid ExchangeGroup exchangeGroup) throws JiebaoException {
        try {
            this.exchangeGroupService.updateById(exchangeGroup);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    /**
     * 使用Mapper操作数据库
     *
     * @return JiebaoResponse 标准返回数据类型
     */
    @GetMapping(value = "/getInformListByMapper")
    @ApiOperation(value = "查询数据List", notes = "查询数据List列表", response = JiebaoResponse.class, httpMethod = "GET")
    public List<ExchangeGroup> getExchangeGroupListByMapper() {
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User byName = userService.findByName(username);
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",byName.getUserId());
        List<ExchangeGroup> list = exchangeGroupMapper.selectByMap(map);
        return list;
    }
}
