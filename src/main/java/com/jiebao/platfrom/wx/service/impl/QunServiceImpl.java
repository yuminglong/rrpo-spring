package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.CheckExcelUtil;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.UserService;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.dao.QunMapper;
import com.jiebao.platfrom.wx.domain.QunExcel;
import com.jiebao.platfrom.wx.service.IQunService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-20
 */
@Service
public class QunServiceImpl extends ServiceImpl<QunMapper, Qun> implements IQunService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DeptService deptService;

    @Override
    public JiebaoResponse addOrUpdate(Qun entity) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        if (entity.getWxId() == null) {
            if (!judge(dept.getDeptId())) {
                return jiebaoResponse.failMessage("此单位已建立群");
            }
            entity.setDate(new Date());
            entity.setCjDeptId(dept.getDeptId());
            entity.setShDeptId(dept.getDeptId());
            entity.setShStatus(0);

        } else {
            if (!entity.getCjDeptId().equals(dept.getDeptId())) {
                if (!judge(dept.getDeptId())) {
                    return jiebaoResponse.failMessage("此单位已建立群");
                }
            }
        }
        jiebaoResponse = super.saveOrUpdate(entity) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse importQun(String content) {  //合格群导入
        if (content == null)
            content = "老粮仓镇、金洲镇、宁乡城郊镇、流沙河镇。湘湖街、荷花" +
                    "园街、五里牌街、北山镇；大托铺街；暮云街；南托街；文" +
                    "源街；新开铺街。黄兴镇、榔梨镇、湘龙街、安沙镇、黑石" +
                    "铺街。东山街、跳马镇、同升街、洞井街、雨花亭街、黎托" +
                    "街、高桥街、柏加镇、官桥镇、镇头镇。青山桥镇、双江口" +
                    "镇、大屯营镇、菁华铺镇。砂子塘街、圭塘街；丁字街道、" +
                    "桥驿镇、横市镇。道林镇。月亮岛街、大泽湖街、白沙洲街、" +
                    "乌山街、左家塘街道；观沙岭街道,天顶街道,望岳街道," +
                    "银盆岭街道。先锋街道,昭山镇、鹤岭镇、五里堆街道。泉塘镇、先锋街道；东郊乡、" +
                    "壶天镇、潭市镇、姜畲镇；双马街、板塘街、白石镇、茶恩" +
                    "寺镇、龙洞镇、荷塘街；清溪镇,韶山乡,响水乡,石潭镇," +
                    "毛田镇,棋梓镇,望春门街道,育塅乡、" +
                    "月山镇,万楼街道,云塘街道。湘潭市：云湖桥镇、银田镇、" +
                    "谭家山镇、中路铺镇、新乡街道、长城乡," +
                    "南庄坪镇。苗市镇。零阳镇、溪口镇、岩泊渡镇；西溪" +
                    "坪街、官黎坪街、后坪镇;南山坪乡;" +
                    "安和街。北湖街、下湄桥街、郴江街、华塘镇、石盖塘街、" +
                    "增福街、人民路街、坳上镇、白鹿洞街道、卜里坪街道、良" +
                    "田镇、苏仙岭街道、五里牌镇、许家洞镇；白露塘、飞天山" +
                    "镇、南塔街道、栖凤渡镇、高亭司镇、马田镇、五岭镇、杨" +
                    "梅山镇、唐洞街道、白石渡街道；东江街道、洋塘乡、" +
                    "洋市镇、永乐江镇," +
                    "芦洪市镇；冷水滩工业园；杨家桥镇、下马渡镇；文富市镇、" +
                    "大盛镇、高溪市镇。肖家园街。蔡市镇、黄阳司镇、菱角山" +
                    "街、马坪开发区、仁湾街、珊瑚街、上岭桥镇；黎家坪镇、" +
                    "大村甸镇、龙山街道、长虹街道；新圩江镇、紫溪市镇" +
                    "、富塘街道、梅花镇、寿雁镇、万家庄街" +
                    "道、祥霖铺镇、营江街道。" +
                    "解放岩。峒河街、马颈坳镇、小溪镇。木江坪镇。双塘街;古" +
                    "阳镇、默戎镇；芙蓉镇、永茂镇、乾州街道、石家冲街道：" +
                    "镇溪街道。" +
                    "枫坪镇、金石镇、蓝田镇、三甲乡、石马山镇、水洞底镇、" +
                    "杨市镇。白马镇。布溪街、禾青" +
                    "镇、金竹山镇、花山街、石井镇、" +
                    "万宝镇、炉观镇、石冲口镇、孟" +
                    "公镇、西河镇。上梅镇、琅塘镇;" +
                    "槎溪镇、沙塘湾街、维山乡、洋" +
                    "溪镇、甘棠镇。茅塘镇、洪山殿" +
                    "镇、三塘镇、枫林街道、科头乡。" +
                    "坪上镇。学院街、塘渡口镇。界岭镇、宋家塘镇、爱莲" +
                    "街、鸭田镇、。廉桥镇、谭溪镇、雨溪街、金石桥镇、" +
                    "牛马司镇、兴隆街；大禾塘街、白仓镇、九公桥镇、渡" +
                    "头桥镇、高崇山镇、火车站乡、石桥街道；" +
                    "花门街道、南岳庙镇、滩头镇、桃花坪街" +
                    "道、周旺镇、横板桥镇、火厂坪镇、水东江镇、杨桥镇、周" +
                    "官桥乡、茶元头街道、岩口铺镇、长阳铺镇、岩山镇、月溪" +
                    "镇、长塘乡、石江镇、竹市镇、江口镇、雪峰街道、水东镇、" +
                    "市经开区昭阳片区。" +
                    "南洲镇；群丰镇、雷打石镇、栗雨街。淦田镇。渌口镇；" +
                    "清水塘街。龙泉街；枫溪街、学林街、井龙街、响石街、" +
                    "铜塘湾街、茨菇塘街、月塘街；贺家土街道、庆云街道、" +
                    "建宁街道街道、建设街道、合泰管委会、马家河街道、" +
                    "三门镇、田心街道、古岳峰镇。东富镇,长庆示范区朱" +
                    "亭镇。枫林镇、王仙镇、沩山镇。" +
                    "蔡家岗镇、宝峰街、楚江街、二都街、夹山镇、易家渡" +
                    "镇、永兴街、盘塘街。高新区、太子庙镇。" +
                    "新安镇。樟木桥镇、南坪镇；安福镇。东江街道。石板" +
                    "滩镇、夹山管理处、刻木乡、太浮镇、太浮镇、新铺乡、" +
                    "谢家铺镇、灌溪镇、芷兰街。金罗镇,王家厂镇,佘市" +
                    "桥镇,七里桥街道,芦荻山乡：崔家" +
                    "桥镇、石门桥镇、大堰垱镇。" +
                    "朝阳街；衡龙桥镇；渠江镇、烟溪镇。灰山港镇、桃花" +
                    "江镇。沧水铺镇、龙光桥街、平口镇、谢林港镇、修山" +
                    "镇、鱼形山街；龙岭工业区、泥江口镇：柘溪林场。" +
                    "灶市镇、哲桥镇、大浦镇、白鹤街、洪桥街、永昌街。开" +
                    "云镇。廖田镇、雨母山镇。衡州路街、鸡笼镇、茶山坳镇、" +
                    "东阳渡镇、和平乡、泉湖镇、南岳镇、公平镇；店门镇、长" +
                    "江镇、苗圃街、向阳镇、新塘镇、金龙坪街、大和圩乡、霞" +
                    "流镇、粤汉街；联合街道、广东街道、三梓镇、石湾街道、" +
                    "灵官镇、余庆街道、白地市镇、双桥镇、咸塘镇、萱州镇、" +
                    "冶金街道、岳屏镇、云集镇；宝盖镇,风石堰镇,石滩乡东" +
                    "风街道、酃湖乡,樟木乡,花桥镇,五里牌街道、小水镇," +
                    "三塘镇,谭子山镇,铁丝塘镇、雁峰街道," +
                    "板市乡、黄家湾镇、角山镇、金兰镇、金源街道、库宗桥镇、" +
                    "栏垅乡、杉桥镇、台源镇、" +
                    "西渡镇、演陂镇。" + "县溪镇、牙屯堡镇。连山乡、中方镇、渠阳镇、黔城镇。" +
                    "公平镇、土桥镇、罗旧镇；小龙门乡、林城镇、马鞍、" +
                    "坪村镇、板栗村乡、高村镇、郭公坪镇、江口镇、锦和" +
                    "镇、波洲镇、晃州镇、鱼市镇、泸阳镇、新建镇、城州" +
                    "街、河西镇、凉亭坳乡、江市场镇、甘棠镇、太阳坪乡、" +
                    "北斗溪乡、低庄镇；黄金坳镇、坨院街道、盈口乡、小" +
                    "横垅镇；舒家村乡、和平溪乡、观音阁镇、卢峰镇、双" +
                    "井镇、大江口镇、思蒙镇、沿溪乡、新店坪镇、芷江镇。" +
                    "火马冲镇、堡子镇、大桥江乡、花桥镇" +
                    ",安江镇、岔头乡、大崇乡、塘湾镇、" +
                    "铁山乡、雪峰镇、蒿吉坪乡、桐木镇、铜湾镇；" +
                    "荣家湾镇、黄沙街镇。湖滨街、坦渡镇、" +
                    "羊楼司镇、五里牌街、康王乡、弼" +
                    "时神鼎山镇、新市镇、洛王街道。" +
                    "云溪镇、长安街道、吕仙亭街道、" +
                    "归义汩罗镇、屈子祠镇、桃林镇、" +
                    "川山坪镇、弼时镇；站前街道,古" +
                    "培镇,白水镇,罗江镇。路口镇" +
                    "、安定镇、岑川镇、" +
                    "梅仙镇、三市镇、三阳乡" +
                    "、余坪镇、洞庭" +
                    "街道、枫桥湖街道,";
        char[] chars = content.toCharArray();
        List<String> list = new ArrayList<>();
        String text1 = "";
        for (char c : chars
        ) {
            if (!String.valueOf(c).matches("[\u4e00-\u9fa5]")) {
                list.add(text1);
                text1 = new String();
            } else {
                text1 += c;
            }
        }
        for (String str : list
        ) {  //具体创建对象
            List<Dept> depts = deptService.getDeptByName(str);
            if (depts.size() == 0)
                continue;
            Dept dept = depts.get(0);
            if (judge(dept.getDeptId())) {//数据库还不存在此街道 群
                Qun qun = new Qun();
                qun.setDate(new Date());
                qun.setWxName(str);
                qun.setCjDeptId(dept.getDeptId());
                qun.setShDeptId("0");
                qun.setShStatus(3);
                save(qun);
            }
        }
        return new JiebaoResponse().okMessage("成功");
    }

    @Override
    public void exPort(HttpServletResponse response, String[] deptId, String workName) {
        Dept deptLogin = deptService.getDept(); // 登陆人id
        Map<String, List<?>> map = new HashMap<>();
        List<Dept> childrenList = new ArrayList<>();
        if (deptId == null) {
            childrenList = deptService.getChildrenList(deptLogin.getDeptId());//省级下面所有的大子集
        } else {   //查询对应 市州级别
            childrenList.addAll(deptService.listByIds(Arrays.asList(deptId)));
        }
        for (Dept dept : childrenList
        ) {
            List<String> deptAllS = new ArrayList<>(); //存储市州级   下递归所有组织  机构的id
            deptAllS.add(dept.getDeptId());
            List<String> deptIds = new ArrayList<>();
            deptIds.add(dept.getDeptId());
            deptService.getAllIds(deptIds, deptAllS);
            QueryWrapper<Qun> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("cj_dept_id", deptAllS);
            List<QunExcel> qunExcels = this.baseMapper.listExcel(dept.getDeptName(), queryWrapper);
            if (qunExcels.isEmpty())
                continue;//无内容结束
            map.put(dept.getDeptName(), qunExcels);
        }
        CheckExcelUtil.exportMap(response, map, QunExcel.class, workName);
    }

    private boolean judge(String deptId) {
        return this.baseMapper.judge(deptId) == null ? true : false;
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String name, String userName) {
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        QueryWrapper<Qun> queryWrapper = new QueryWrapper<>();
        List<String> list = new ArrayList<>();  //储存id
        List<String> listPrentId = new ArrayList<>();  //储存id
        listPrentId.add(dept.getDeptId());
        list.add(dept.getDeptId());
        deptService.getAllIds(listPrentId, list);
        if (list.size() == 0) {
            queryWrapper.and(qunQueryWrapper -> qunQueryWrapper.eq("cj_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId()));
        } else {
            queryWrapper.and(qunQueryWrapper -> qunQueryWrapper.eq("cj_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId())
                    .or().in("cj_dept_id", list).eq("sh_status", 3));
        }
        if (name != null) {
            queryWrapper.like("wx_name", name);
        }
        if (userName != null) {
            queryWrapper.like("wx_user_name", userName);
        }
        queryWrapper.orderByDesc("date");
        Page<Qun> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.list(page, queryWrapper)).okMessage("查询成功");
    }


}
