package com.mabao.dao.domain;

import javax.persistence.*;

@Entity
@Table(name = "t_goods_size_table")
public class GoodsSize {
    private Long id;                         //编号
    private GoodsType goodsType;             //商品类别
    private String name;                     //名称

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "goods_type_id")
    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
