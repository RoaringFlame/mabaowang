package com.mabao.service;

import com.mabao.dao.domain.GoodsSize;
import com.mabao.util.Selector;

import java.util.List;

public interface GoodsSizeService {
    /**
     * 宝物尺码下拉菜单
     */
    List<Selector> findGoodsSizeForSelector();

    /**
     * ID获取商品
     * @param id            尺寸ID
     * @return              尺寸对象
     */
    GoodsSize get(Long id);
}
