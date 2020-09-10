package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-09-09
 */
public interface INoticeService extends IService<Notice> {
    JiebaoResponse addOrUpdate(Notice notice);

    JiebaoResponse delete(String[] ids);

    JiebaoResponse list(String noticeId, Date StartDate, Date endStart, QueryRequest queryRequest);
}
