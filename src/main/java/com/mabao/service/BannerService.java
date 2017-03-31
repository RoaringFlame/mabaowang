package com.mabao.service;

import com.mabao.dao.domain.Banner;

import java.util.List;

public interface BannerService {
    /**
     * sort倒序查找启用的广告
     * @param status            是否启用
     * @return                  广告对象list
     */
    List<Banner> findByStatusOrderBySortDesc(boolean status);
}
