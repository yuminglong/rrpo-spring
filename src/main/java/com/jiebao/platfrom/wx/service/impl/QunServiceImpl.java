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

import static io.lettuce.core.ScanArgs.Builder.matches;

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

    private boolean importQunN(String content) {  //导入所有的群
        if (content == null) {
            content = "北山镇、黄兴镇、安沙镇、湘龙街道、榔梨街道、" +
                    "金洲镇、大屯营镇、横市镇、菁华铺乡、道林镇、流沙河镇、青山桥镇、老粮仓镇、" +
                    "城郊街道、双江口镇、" +
                    "大托铺街道、南托街道、暮云街道、文源街道、新开铺街道、黑石铺街道、先锋街道、" +
                    "社港镇、官桥镇、古港镇、镇头镇、淳口镇、张坊镇、龙伏镇、官渡镇、达浒镇、沿溪镇、柏加镇、关口镇、" +
                    "大泽湖街道、月亮岛街道、桥驿镇、乌山街道、金山桥街道、白沙洲街道、丁字湾街道、" +
                    "荷花园街道、五里牌街道、湘湖街道、" +
                    "同升湖街道、洞井街道、东山街道、、雨花亭街道、跳马镇、高桥街道、圭塘街道、左家塘街道、黎托街道、高桥街道、" +
                    "望岳街道、银盆岭街道、观沙岭街道、天顶街道、" +
                    "东方红街道、" +
                    "伍家岭街道、秀峰街道、青竹湖街道、东风路街道、浏阳河街道、" +
                    "白水镇、屈子祠镇、神鼎山镇、弼时镇、罗江镇、新市镇、川山坪镇、桃林寺镇、" +
                    "归义（汨罗）镇、古培镇、" +
                    "岳阳楼区：洛王街道、梅溪（洞庭）街道、站前路街道、吕仙亭街道、枫桥湖街道、" +
                    "康王乡、" +
                    "羊楼司镇、坦渡镇、长安街道、临湘市五里牌街道、桃林（长塘）镇、" +
                    "华容县章华镇、三封寺镇、万庾镇、" +
                    "荣家湾镇、黄沙街镇、筻口镇、步仙镇、柏祥镇、杨林街道、" +
                    "云溪街道、路口镇、" +
                    "柳林洲街道、广兴洲镇、许市镇、钱粮湖镇、" +
                    "湖滨街道、" +
                    "余坪镇、芩川镇、三市镇、三阳乡、梅仙镇、安定镇、" +
                    "衡州路街道、茶山坳镇、冶金街道、东风路街道、和平乡、广东路街道、粤汉街道、东阳渡街道、苗圃街道、酃湖乡、" +
                    "开云镇、萱洲镇、长江镇、店门镇、" +
                    "鸡笼镇、咸塘镇、向阳桥街道、三塘镇、铁丝塘镇、泉湖镇、谭子山镇、衡南县花桥镇、云集镇、宝盖镇、" +
                    "三樟镇、霞流镇、大浦镇、新塘镇、石湾镇、石滩乡、" +
                    "余庆街道、灶市街道、哲桥镇、耒阳市五里牌街道、公平圩镇、大和圩乡、小水镇、" +
                    "金龙坪街道、岳屏镇、雁峰街道、" +
                    "台源镇、杉桥镇、栏垅乡、樟木乡、库宗桥镇、西渡镇、金兰镇、演坡镇、板市乡" +
                    "风石堰镇、灵官镇、双桥镇、白鹤街道、永昌街道、洪桥街道、白地市镇、" +
                    "黄沙湾街道、角山镇、金源街道、" +
                    "联合街道、雨母山镇、" +
                    "南岳镇、" +
                    "凉亭坳乡、河西街道、盈口乡、黄金坳镇、城南街道、坨院街道、" +
                    "芷江镇、公平镇、罗旧镇、土桥镇、新店坪镇、" +
                    "牙屯堡镇、县溪镇、" +
                    "板栗树乡、和平溪乡、大桥江乡、江口墟镇、舒家村乡、锦和镇、高村镇、郭公坪镇、" +
                    "新建镇、花桥镇、中方镇、铜湾镇、泸阳镇、蒿吉坪乡、桐木镇、" +
                    "渠阳镇、太阳坪乡、甘棠镇、" +
                    "林城镇、连山乡、坪村镇、堡子镇、马鞍镇、" +
                    "沿溪乡、卢峰镇、低庄镇、小横垅乡、大江口镇、北斗溪镇、观音阁镇、思蒙镇、双井镇、" +
                    "鱼市镇、波洲镇、晃州镇、" +
                    "雪峰镇、铁山乡、塘湾镇、黔城镇、江市镇、安江镇、大崇乡、岔头乡、" +
                    "小龙门乡、火马冲镇、" +
                    "龙泉街道、董家段街道、贺家土街道、庆云街道、建设街道、白关镇、枫溪街道、建宁街道、" +
                    "茨菇塘（桂花）街道、月塘（合泰）街道、" +
                    "雷打石镇、三门镇、马家河街道、栗雨街道、群丰镇、" +
                    "长庆示范区、泗汾镇、来龙门街道、王仙镇、东富镇、醴陵市枫林镇、阳三石街道、仙岳山街道、国瓷街道、板杉镇、船湾镇、孙家湾镇、沩山镇、\n" +
                    "铜湾街道、井龙街道、响石岭街道、田心街道、清水塘街道、" +
                    "渌口镇、南洲镇、淦田镇、朱亭镇、古岳峰镇、" +
                    "皇图岭镇、网岭镇、联星街道、谭桥街道、菜花坪镇、渌田镇、宁家坪镇、攸县新市镇、" +
                    "湖口镇、下东（利民）镇、马江镇、虎踞镇、思聪镇、" +
                    "沔渡镇、霞阳镇、" +
                    "学林街道、" +
                    "姜畲镇、万楼街道、鹤岭镇、先锋街道、长城乡、云塘街道、" +
                    "茶恩寺镇、中路铺镇、云湖桥镇、石潭镇、谭家山镇、白石镇、" +
                    "五里堆（宝塔）街道、岳塘区荷塘街道、" +
                    "毛田镇、壶天镇、潭市镇、新湘路街道、育塅乡、棋梓镇、泉塘镇、龙洞镇、望春门街道、东郊乡、月山镇、" +
                    "清溪镇、韶山乡、银田镇、" +
                    "双马街道、板塘街道、" +
                    "响水乡、" +
                    "昭山镇、" +
                    "万宝镇、石井镇、花山街道、水洞底镇、" +
                    "石马山镇、杨市（荷塘）镇、枫坪镇、白马镇、茅塘镇、金石镇、" +
                    "蓝田街道、三甲乡、" +
                    "维山乡、西河镇、琅塘镇、科头乡、洋溪镇、新化枫林街道、石冲口镇、槎溪镇、炉观镇、孟公镇、上梅街道、" +
                    "甘棠镇、洪山殿镇、三塘铺镇" +
                    "沙塘湾街道、布溪街道、禾青镇、金竹山镇、" +
                    "坪上镇、" +
                    "大埠桥街道、" +
                    "卜里坪街道、苏仙岭街道、坳上镇、白露塘镇、栖凤渡镇、良田镇、飞天山镇、苏仙区五里牌镇、许家洞镇、南塔街道、白鹿洞街道、" +
                    "洋塘乡、马田镇、高亭司镇、" +
                    "人民路街道、华塘镇、下湄桥街道、北湖街道、安和街道、增福街道、郴江街道、石盖塘街道、" +
                    "三都镇、唐洞街道、东江街道、" +
                    "五岭镇、白石渡镇、杨梅山镇、" +
                    "洋市镇、" +
                    "永乐江镇、" +
                    "鱼形山街道、衡龙桥镇、泥江口镇、龙光桥街道、龙岭经开区、沧水铺镇、" +
                    "桃花江镇、灰山港镇、修山镇、" +
                    "朝阳街道、谢林港镇、" +
                    "平口镇、烟溪镇、渠江镇、柘溪林场、" +
                    "谢家铺镇、蔡家岗镇、灌溪镇、石板滩镇、" +
                    "芷兰街道、芦荻山乡、河洑镇、东江街道、南坪街道," +
                    "漳江街道、佘家坪乡、泥窝潭乡、龙潭镇、牛车河镇、枫树乡、陬市镇、盘塘镇、观音寺镇、青林回维乡、" +
                    "夹山镇、宝峰街道、楚江街道、新铺镇、夹山管理处、永兴街道、易家渡镇、" +
                    "二都街道、" +
                    "樟木桥街道（德山镇）、石门桥镇、" +
                    "高新区、崔家桥镇、毛家滩乡、太子庙镇、" +
                    "新安镇、刻木山乡、佘市桥镇、安福街道、太浮镇、" +
                    "大堰垱镇、王家厂镇、金罗镇、" +
                    "七里桥街道、" +
                    "苗市镇、零阳镇、岩泊渡镇、溪口镇、南山坪乡、金岩乡、洞溪乡、" +
                    "西溪坪街道、后坪街道、南庄坪街道、教字垭镇、沙堤街道、官黎坪街道、桥头乡、崇文街道、王家坪镇、阳湖坪街道、" +
                    "利福塔镇、瑞塔铺镇、澧源镇、上洞街道、" +
                    "学院路街道、雨溪街道、" +
                    "石桥街道、高崇山镇、兴隆街道、爱莲街道、火车站乡、" +
                    "金石桥镇、桃花坪街道、鸭田镇、滩头镇、周旺镇、南岳庙镇、横板桥镇、" +
                    "花门街道、" +
                    "塘渡口镇、白仓镇、九公桥镇、岩口铺镇、长阳铺镇、" +
                    "牛马司镇、宋家塘街道、砂石镇、廉桥镇、界岭镇、大禾塘街道、火厂坪镇、周官桥乡、杨桥镇、水东江镇、" +
                    "洞水东镇、石江镇、岩山镇、雪峰街道、江口镇、长塘乡、竹市镇、月溪镇、" +
                    "茶元头街道、" +
                    "昭阳片区、" +
                    "潭溪镇、" +
                    "杨家桥街道、珊瑚街道、菱角山街道、马坪经开区、肖家园街道、上岭桥镇、黄阳司镇、高溪市镇、蔡市镇、工业园街道、仁湾街道、赵家冲岗营、" +
                    "紫溪市镇、白牙市镇、端桥铺镇、新圩江镇、大盛镇、井头圩镇、芦洪市镇、" +
                    "下马渡镇、龙山街道、长虹街道、文富市镇、大村甸镇、黎家坪镇、" +
                    "五里牌镇、泷泊镇、理家坪乡、江村镇、" +
                    "祥林铺镇、万家庄街道、营江街道、梅花镇、寿雁镇、富塘街道、" +
                    "白芒营镇、大路铺镇、沱江镇、" +
                    "石山脚街道、富家桥镇、朝阳街道、" +
                    "上江圩镇、" +
                    "镇溪街道、乾州街道、峒河街道、双塘街道、马颈坳镇、石家冲街道、" +
                    "石羔街道、茅坪乡、茨岩塘镇、兴隆街道、红岩溪镇、水田坝镇、" +
                    "芙蓉镇、永茂镇、万民乡、小溪镇、" +
                    "默戎镇、古阳镇、" +
                    "木江坪镇、" +
                    "解放岩乡、";
        }
        List<String> list = new ArrayList<>();  //储存 群名字
        char[] chars = content.toCharArray();//转换为字符
        String text1 = "";
        for (char c : chars
        ) {
            String s = String.valueOf(c);
            if (s.equals("（") || s.equals("）")) {
                text1 += c;
            } else if (!s.matches("[\u4e00-\u9fa5]")) {
                list.add(text1);
                text1 = new String();
            } else {
                text1 += c;
            }
        }
        for (String str : list
        ) {  //具体创建对象
            String str2=null;
            if (str.contains("（")) {  //有特殊括号
                char[] chars1 = str.toCharArray();
                for (char c : chars1
                ) {
                 str2+=c;
                }
            }
            List<Dept> depts = deptService.getDeptByName(str2==null?str:str2);
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
        return true;
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

    @Override
    public JiebaoResponse ListByDeptId(String deptId) {  // 通过部门选择 群
        List<String> list = new ArrayList<>();  //储存id
        List<String> listPrentId = new ArrayList<>();  //储存id
        listPrentId.add(deptId);
        list.add(deptId);
        deptService.getAllIds(listPrentId, list); //所有的组织机构
        QueryWrapper<Qun> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("cj_dept_id", list);
        return new JiebaoResponse().data(list(queryWrapper)).okMessage("查询成功");
    }

    private boolean judge(String deptId) {  //是否存在群
        return this.baseMapper.judge(deptId) == null ? true : false;
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String name, String userName, Integer status) {//status  //分状态展示
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        QueryWrapper<Qun> queryWrapper = new QueryWrapper<>();
        List<String> list = new ArrayList<>();  //储存id
        List<String> listPrentId = new ArrayList<>();  //储存id
        listPrentId.add(dept.getDeptId());
        list.add(dept.getDeptId());
        deptService.getAllIds(listPrentId, list);
        queryWrapper.in("cj_dept_id", list);
        if (name != null) {
            queryWrapper.like("wx_name", name);
        }
        if (userName != null) {
            queryWrapper.like("wx_user_name", userName);
        }
        if (status != null) {
            if (status == 2) {
                queryWrapper.eq("sh_dept_id", dept.getDeptId());
                queryWrapper.ne("sh_status", 3);
            } //属于下级  但不需要自己审核   正在 创建的额
            if (status == 3 || status == 1)//已经成功的
                queryWrapper.eq("sh_status", status);
            if (status == 4) //未上报的{
            {
                queryWrapper.eq("cj_dept_id", dept.getDeptId());
                queryWrapper.isNull("status");
            }
        }
        queryWrapper.orderByDesc("date");
        Page<Qun> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.list(page, queryWrapper)).okMessage("查询成功");
    }

}
