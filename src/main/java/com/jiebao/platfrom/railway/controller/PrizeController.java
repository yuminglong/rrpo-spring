package com.jiebao.platfrom.railway.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.common.utils.EqualsMonth;
import com.jiebao.platfrom.railway.dao.PrizeLimitMapper;
import com.jiebao.platfrom.railway.dao.PrizeMapper;
import com.jiebao.platfrom.railway.dao.PrizeOpinionMapper;
import com.jiebao.platfrom.railway.dao.PrizeUserMapper;
import com.jiebao.platfrom.railway.domain.*;
import com.jiebao.platfrom.railway.service.*;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PrizeOpinionMapper prizeOpinionMapper;

    @Autowired
    private PrizeOpinionService prizeOpinionService;

    @Autowired
    private FileMapper fileMapper;

    /**
     * 创建一条一事一奖
     */


    @PostMapping("/save")
    @ApiOperation(value = "创建一条一事一奖", notes = "创建一条一事一奖", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse creatPrize(@Valid Prize prize, String[] fileIds) throws JiebaoException {
        try {
            System.out.println("-------------------------"+fileIds+"-------------------------------");
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            prize.setCreatUser(username);
            boolean save = prizeService.save(prize);
            Arrays.stream(fileIds).forEach(fileId -> {
                fileMapper.updateByFileId(fileId, prize.getId());
            });
            UserRole userRole = userRoleMapper.getByUserId(byName.getUserId());
            //模拟7c8f277c3adb22397b8c38f15ca03ea8是区级
            if ("7c8f277c3adb22397b8c38f15ca03ea8".equals(userRole.getRoleId())) {
                //获取该区县组织机构，则getParentId就是获取它上级组织机构ID
                Dept byId = deptService.getById(byName.getDeptId());
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("dept_id", byId.getParentId());
                List<User> users = userMapper.selectByMap(columnMap);
                if (save) {
                    for (User user : users
                    ) {
                        //把要发送的用户ID保存到数据库
                        prizeUserService.saveByUser(prize.getId(), user.getUserId());
                    }
                }
                prize.setReviewStatus(0);
                //模拟2是市级
            } else if ("1ef79e30d65cf948db46a7e408f46085".equals(userRole.getRoleId())) {
                Dept byId = deptService.getById(byName.getDeptId());
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("dept_id", byId.getParentId());
                List<User> users = userMapper.selectByMap(columnMap);
                if (save) {
                    for (User user : users
                    ) {
                        //把要发送的用户ID保存到数据库
                        prizeUserService.saveByUser(prize.getId(), user.getUserId());
                    }
                }
                Map<String, Object> map = new HashMap<>();
                map.put("dept_id", byId.getDeptId());
                List<User> OwnUsers = userMapper.selectByMap(map);
                if (save) {
                    for (User u : OwnUsers
                    ) {
                        //把要发送的用户ID保存到数据库
                        prizeUserService.saveByUser(prize.getId(), u.getUserId());
                    }
                }
                prize.setReviewStatus(0);
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
                    Prize byId = prizeService.getById(prizeId);
                    PrizeOrder prizeOrderSet = new PrizeOrder();
                    prizeOrderSet.setDeptId(byName.getDeptId());
                    prizeOrderSet.setCreatTime(byId.getReleaseTime());
                    prizeOrderSet.setPrizeId(prizeId);
                    prizeOrderSet.setReleaseNumber(countByDept + 1);
                    prizeOrderService.save(prizeOrderSet);
                    //把status改为3,并创建发布时间
                    prizeMapper.release(prizeId);
                    prizeUserMapper.setCreatTime(prizeId);
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
                if ("1".equals(byId.getStatus())) {
                    //删除接收的组织机构
                    prizeUserService.deleteByPrizeId(prizeId);
                    //删除内容本体（文件还没加哦）
                    prizeService.removeById(prizeId);
                    //3为已发送状态，只需改状态为4
                } else if ("3".equals(byId.getStatus())) {
                    prizeUserService.ByPrizeId(prizeId);
                }
            });
            return new JiebaoResponse().message("删除一事一奖成功");
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

    @GetMapping("/report")
    @ApiOperation(value = "审批并上报", notes = "审批并上报", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse PrizeReport(@Valid PrizeOpinion prizeOpinion , String [] prizeIds) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            Dept byId = deptService.getById(byName.getDeptId());
            List<User> users = new ArrayList<>();
            if (byId.getRank() == 1){
               if ("1143" .equals(byId.getDeptId())||"1".equals(byId.getDeptId())){
                   Map<String, Object> columnMap = new HashMap<>();
                   columnMap.put("dept_id", 3000);
                   users = userMapper.selectByMap(columnMap);
               }
            }
            else{
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("dept_id", byId.getParentId());
                users = userMapper.selectByMap(columnMap);
            }
            List<User> finalUsers = users;
            Arrays.stream(prizeIds).forEach(prizeId->{
                //查询该级组织机构是否有审批内容
                Integer result = prizeOpinionMapper.selectOpinion(byId.getRank(), prizeId);
                if (result == 0){
                    //如果不为省级，就不上报
                    if (byId.getRank() != 0){
                        for (User user : finalUsers
                        ) {
                            //把要发送的用户ID保存到数据库
                            prizeUserService.saveByUser(prizeId, user.getUserId());
                        }
                    }
                    // 0是省
                    if(byId.getRank() == 0){
                        prizeMapper.updateStatusForPro(prizeId);
                    }
                    //4为铁护办
                    if(byId.getRank() == 4){
                        prizeMapper.updateStatusForIron(prizeId);
                    }
                    //1为市
                    if(byId.getRank() == 1){
                        prizeMapper.updateStatusForCity(prizeId);
                    }

                    prizeOpinion.setRank(byId.getRank());
                    //id必须传来
                    prizeOpinion.setPrizeId(prizeId);
                    prizeOpinion.setAuditOpinion(prizeOpinion.getAuditOpinion());
                    prizeOpinion.setMoney(prizeOpinion.getMoney());
                    prizeOpinionService.save(prizeOpinion);
                }
            });
            return new JiebaoResponse().message("审批或上报成功").put("status", "200");
        } catch (Exception e) {
            message = "审批或上报失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @GetMapping("/reject/list")
    @ApiOperation(value = "驳回", notes = "驳回", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse prizeReject(String [] prizeIds) throws JiebaoException {
        try {
            Arrays.stream(prizeIds).forEach(prizeId ->{
                this.prizeMapper.reject(prizeId);
            });
            return new JiebaoResponse().message("驳回成功").put("status","200");
        } catch (Exception e) {
            message = "驳回失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }

    }


    @GetMapping("/findOpinion")
    @ApiOperation(value = "意见查询", notes = "意见查询", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse findOpinion(String prizeId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("prize_id",prizeId);
        List<PrizeOpinion> prizeOpinions = prizeOpinionMapper.selectByMap(map);
        return new JiebaoResponse().data(prizeOpinions).put("status","200");
    }

    @GetMapping("/briefing")
    @ApiOperation(value = "简报", notes = "简报", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse briefing(String startTime, String endTime) throws JiebaoException {
        try {
            List<Prize> prizes = prizeMapper.selectByBriefing(startTime,endTime);
            for (Prize p: prizes
            ) {
                PrizeOpinion prizeOpinion = prizeOpinionMapper.selectOne(new LambdaQueryWrapper<PrizeOpinion>().eq(PrizeOpinion::getPrizeId, p.getId()).eq(PrizeOpinion::getRank, 0));
                String money = prizeOpinion.getMoney();
                p.setBriefingMoney(money);
            }
            return new JiebaoResponse().data(prizes).put("status","200");
        }
        catch (Exception e) {
            message = "生成简报失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }
}
