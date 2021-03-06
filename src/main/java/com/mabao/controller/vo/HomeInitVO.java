package com.mabao.controller.vo;

import com.mabao.util.Selector;

import java.util.List;

public class HomeInitVO {
    private List<Selector> goodsTypeList;               //商品类别
    private List<Selector> gender;                      //猜你喜欢，宝宝性别
    private BabyVO baby;                                 //宝宝信息
    private List<BannerVO> smallBanner;                  //轮播图片列表3张

    public List<Selector> getGoodsTypeList() {
        return goodsTypeList;
    }

    public void setGoodsTypeList(List<Selector> goodsTypeList) {
        this.goodsTypeList = goodsTypeList;
    }

    public List<Selector> getGender() {
        return gender;
    }

    public void setGender(List<Selector> gender) {
        this.gender = gender;
    }

    public List<BannerVO> getSmallBanner() {
        return smallBanner;
    }

    public void setSmallBanner(List<BannerVO> smallBanner) {
        this.smallBanner = smallBanner;
    }

    public BabyVO getBaby() {
        return baby;
    }

    public void setBaby(BabyVO baby) {
        this.baby = baby;
    }
}
