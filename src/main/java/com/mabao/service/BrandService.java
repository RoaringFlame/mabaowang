package com.mabao.service;

import com.mabao.dao.domain.Brand;
import com.mabao.util.Selector;

import java.util.List;

public interface BrandService {
    /**
     * 获取启用的品牌
     * @param status            是否启用
     * @return                  品牌list
     */
    List<Brand> findByStatus(Boolean status);

    /**
     * 获取品牌下拉菜单
     * @return                  Selector
     */
    List<Selector> findBrandForSelector();
}
