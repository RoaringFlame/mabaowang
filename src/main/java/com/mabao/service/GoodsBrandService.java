package com.mabao.service;

import com.mabao.dao.domain.GoodsBrand;
import com.mabao.util.Selector;
import java.util.List;

public interface GoodsBrandService {
    /**
     * 获取启用的品牌
     * @param status            是否启用
     * @return                  品牌list
     */
    List<GoodsBrand> findByStatus(Boolean status);

    /**
     * 获取品牌下拉菜单
     * @return                  Selector
     */
    List<Selector> findBrandForSelector();

    /**
     * ID获取品牌
     * @param brandId           品牌ID
     * @return                  品牌
     */
    GoodsBrand get(Long brandId);
}
