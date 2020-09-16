package com.jiebao.platfrom.wx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.File;
import com.jiebao.platfrom.system.service.FileService;
import com.jiebao.platfrom.wx.domain.Month;
import com.jiebao.platfrom.wx.service.IMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-08-22
 */
@RestController
@RequestMapping("/wx/month")
@Api(tags = "wx_月度评选")
public class MonthController {
    @Autowired
    IMonthService monthService;
    @Autowired
    FileService fileService;

    @PostMapping("saveorUpdate")
    @ApiOperation("添加或者修改")
    public JiebaoResponse saveOrUpdate(Month month, String[] fileIds) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = monthService.saveOrUpdate(month) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Delete("delete")
    @ApiOperation("删除  ")
    public JiebaoResponse saveOrUpdate(String[] ids) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = monthService.removeByIds(Arrays.asList(ids)) ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
        return jiebaoResponse;
    }

    @GetMapping("list")
    @ApiOperation("查询集合")
    public JiebaoResponse pageList(QueryRequest queryRequest, String month, Integer look, Integer status) {
        return monthService.pageList(queryRequest, month, look, status);
    }

    @GetMapping("appear")
    @ApiOperation("上报")
    public JiebaoResponse appear(String monthId, Integer status) {
        return monthService.appear(monthId, status);
    }

    @GetMapping("getById")
    @ApiOperation("查看具体信息")
    public JiebaoResponse getById(String monthId) {
        return new JiebaoResponse().data(monthService.getById(monthId)).okMessage("查询成功");
    }

    @PostMapping("downDocx")
    @ApiOperation("下载文档")
    public JiebaoResponse downDocx(HttpServletResponse response, String month) {
        return monthService.monthDocx(response, month);
    }

    @PostMapping("koran")
    @ApiOperation("可入不可入按钮")
    public JiebaoResponse koran(String month, Integer status) {  //month  为id
        return monthService.koran(month, status);
    }

    @GetMapping("monthDocxText")
    @ApiOperation("导出表直观样式")
    public JiebaoResponse monthDocxText(QueryRequest queryRequest, String month) {
        return monthService.monthDocxText(queryRequest, month);
    }

    @PostMapping("downDocxGood")
    @ApiOperation("月度评选优秀记录word导出")
    public JiebaoResponse downDocxGood(HttpServletResponse response, String month, String number, String content) {
        return monthService.downDocxGood(response, month, number, content);
    }

//    public static void main(String[] args) {
//        String text="老粮仓镇、金洲镇、宁乡城郊镇、流沙河镇。湘湖街、荷花\n" +
//                "园街、五里牌街、北山镇；大托铺街；暮云街；南托街；文\n" +
//                "源街；新开铺街。黄兴镇、榔梨镇、湘龙街、安沙镇、黑石\n" +
//                "铺街。东山街、跳马镇、同升街、洞井街、雨花亭街、黎托\n" +
//                "街、高桥街、柏加镇、官桥镇、镇头镇。青山桥镇、双江口\n" +
//                "镇、大屯营镇、菁华铺镇。砂子塘街、圭塘街；丁字街道、\n" +
//                "桥驿镇、横市镇。道林镇。月亮岛街、大泽湖街、白沙洲街、\n" +
//                "乌山街、左家塘街道；观沙岭街道天顶街道望岳街道\n" +
//                "银盆岭街道。先锋街道（20/1）";
//        char[] chars = text.toCharArray();
//       List<String> list = new ArrayList<>();
//       String text1="";
//        for (char c:chars
//             ) {
//            System.out.println(c);
//        }
//
//
//    }


}
