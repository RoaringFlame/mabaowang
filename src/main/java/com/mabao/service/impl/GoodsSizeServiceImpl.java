package com.mabao.service.impl;

import com.mabao.dao.domain.GoodsSize;
import com.mabao.dao.repositories.GoodsSizeRepository;
import com.mabao.service.GoodsSizeService;
import com.mabao.util.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsSizeServiceImpl implements GoodsSizeService{
    @Autowired
    private GoodsSizeRepository goodsSizeRepository;
    /**
     * 宝物尺码下拉菜单
     * 依据需求确认尺码是否根据类型联动
     */
    /*public List<Selector> findGoodsSizeForSelector(Long goodsTypeId){
        List<Selector> selectors = new ArrayList<>();
        List<GoodsSize> goodsSizeList = this.goodsSizeRepository.findByGoodsTypeId(goodsTypeId);
        for (GoodsSize gs : goodsSizeList){
            Selector selector = new Selector(gs.getId().toString(),gs.getName());
            selectors.add(selector);
        }
        return selectors;
    }*/
    /**
     * 宝物尺码下拉菜单
     */
    @Override
    public List<Selector> findGoodsSizeForSelector() {
        List<Selector> selectors = new ArrayList<>();
        List<GoodsSize> goodsSizeList = this.goodsSizeRepository.findAll();
        for (GoodsSize gs : goodsSizeList){
            Selector selector = new Selector(gs.getId().toString(),gs.getName(),gs.getGoodsType().getId().toString());
            selectors.add(selector);
        }
        return selectors;
    }
    /**
     * ID获取商品
     * @param id            尺寸ID
     * @return              尺寸对象
     */
    @Override
    public GoodsSize get(Long id) {
        return this.goodsSizeRepository.findOne(id);
    }
}
