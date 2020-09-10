package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Notice;
import com.jiebao.platfrom.wx.dao.NoticeMapper;
import com.jiebao.platfrom.wx.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-09-09
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Override
    public JiebaoResponse addOrUpdate(Notice notice) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (notice.getNoticeId() == null) {
            notice.setDate(new Date());
        }
        jiebaoResponse = super.saveOrUpdate(notice) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse delete(String[] ids) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = removeByIds(Arrays.asList(ids)) ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse list(String noticeId, Date StartDate, Date endStart, QueryRequest queryRequest) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        if (noticeId != null)
            queryWrapper.eq("notice_id", noticeId);
        if (StartDate != null)
            queryWrapper.ge("date", StartDate);
        if (endStart != null)
            queryWrapper.le("date", endStart);
        Page<Notice> page = new Page<>(queryRequest.getPageSize(), queryRequest.getPageNum());
        return new JiebaoResponse().data(page(page,queryWrapper));
    }

}
