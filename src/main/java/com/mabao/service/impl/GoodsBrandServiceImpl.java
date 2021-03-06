package com.mabao.service.impl;

import com.mabao.dao.domain.GoodsBrand;
import com.mabao.dao.repositories.GoodsBrandRepository;
import com.mabao.service.GoodsBrandService;
import com.mabao.util.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsBrandServiceImpl implements GoodsBrandService {
    @Autowired
    private GoodsBrandRepository goodsBrandRepository;

    /**
     * 获取启用的品牌
     * @param status            是否启用
     * @return                  品牌list
     */
    @Override
    public List<GoodsBrand> findByStatus(Boolean status) {
        return this.goodsBrandRepository.findByStatus(status);
    }
    /**
     * 获取品牌下拉菜单
     * @return                  Selector
     */
    @Override
    public List<Selector> findBrandForSelector() {
        List<Selector> brandSelector = new ArrayList<>();
        List<GoodsBrand> brandList = this.goodsBrandRepository.findByStatus(Boolean.TRUE);
        for (GoodsBrand b : brandList){
            Selector selector = new Selector(b.getId().toString(),b.getBrandName());
            brandSelector.add(selector);
        }
        return brandSelector;
    }
    /**
     * ID获取品牌
     * @param brandId           品牌ID
     * @return                  品牌
     */
    @Override
    public GoodsBrand get(Long brandId) {
        return this.goodsBrandRepository.findOne(brandId);
    }
}
