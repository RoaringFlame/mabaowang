package com.mabao.service.impl;

import com.mabao.dao.domain.GoodsType;
import com.mabao.dao.repositories.GoodsTypeRepository;
import com.mabao.dao.repositories.GoodsRepository;
import com.mabao.service.GoodsTypeService;
import com.mabao.util.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;

    /**
     * 查询下拉框所用的商品类型列表
     * @return Selector集合
     */
    public List<Selector> getAllGoodsTypeForSelector() {
        List<Selector> list = new ArrayList<>();
        List<GoodsType> goodsTypes= this.goodsTypeRepository.findAll();
        for (GoodsType g :goodsTypes){
            Selector s = new Selector(g.getId().toString(),g.getTypeName());
            list.add(s);
        }
        return list;
    }
    /**
     * 获取商品类型
     * @param typeId            id
     * @return                  商品类型
     */
    @Override
    public GoodsType get(Long typeId) {
        return this.goodsTypeRepository.findOne(typeId);
    }

}
