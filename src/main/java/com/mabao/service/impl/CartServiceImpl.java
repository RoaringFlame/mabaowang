package com.mabao.service.impl;

import com.mabao.controller.vo.JsonResultVO;
import com.mabao.dao.domain.Cart;
import com.mabao.dao.domain.Goods;
import com.mabao.dao.domain.User;
import com.mabao.dao.repositories.CartRepository;
import com.mabao.dao.repositories.GoodsRepository;
import com.mabao.service.CartService;
import com.mabao.service.GoodsService;
import com.mabao.util.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsService goodService;

    /**
     * 删除购物车内商品
     *
     * @param cartId 购物车ID
     * @return 结果VO
     */
    @Override
    public JsonResultVO deleteCartGoods(Long cartId) {
        try {
            this.cartRepository.delete(cartId);
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, e.getMessage());
        }
        return new JsonResultVO(JsonResultVO.SUCCESS, "成功删除！");
    }

    /**
     * 用户购物车中商品列表
     *
     * @return 商品list
     */
    //注意当库存量小于购物车该物品量时应显示库存量，且库存量为0时商品在购物车内应给予删除
    @Override
    public List<Cart> findAllGoodsByUser(Long userId) {
        List<Cart> cartListNow = new ArrayList<>();
        List<Cart> cartList = this.cartRepository.findByUserId(userId);
        for (Cart cart:cartList){
            Goods goods = goodsRepository.findOne(cart.getGoods().getId());
            if(goods.getStockNumber() == 0) {//如果库存量为0，则删除
                this.cartRepository.delete(cart);
            }else if(goods.getStockNumber()<cart.getQuantity()){
                cart.setQuantity(goods.getStockNumber());
                this.cartRepository.saveAndFlush(cart);
                cartListNow.add(cart);
            }else {
                cartListNow.add(cart);
            }
        }
        return cartListNow;
    }

    /**
     * get一条购物车信息
     *
     * @param cartId 购物车ID
     * @return 购物车对象
     */
    @Override
    public Cart get(Long cartId) {
        return this.cartRepository.findOne(cartId);
    }

    /**
     * 商品ID查购物车信息
     *
     * @param goodsId 商品ID
     * @return 购物车
     */
    @Override
    public Cart findUserCartByGoodsId(Long goodsId, Long userId) {
        return this.cartRepository.findByGoodsIdAndUserId(goodsId, userId);
    }

    /**
     * 更新购物车信息
     *
     * @param cart 购物车
     * @return 更新的购物车
     */
    @Override
    public Cart updateCart(Cart cart) {
        return this.cartRepository.saveAndFlush(cart);
    }

    /**
     * 购物车添加商品
     *
     * @param goodsId 商品ID
     * @return 结果String
     */
    @Override
    public JsonResultVO addCartGoods(Long goodsId) {
        User user = UserManager.getUser();
        if (user != null) {
            //判断是否已经添加
            Cart cart = this.findUserCartByGoodsId(goodsId, user.getId());
            if (cart != null) {
                cart.setQuantity(cart.getQuantity() + 1);
                this.updateCart(cart);
                return new JsonResultVO(JsonResultVO.SUCCESS, "添加成功");
            } else {
                try {
                    //新添加
                    Cart newCart = new Cart();
                    newCart.setGoods(this.goodService.get(goodsId));
                    newCart.setCreateTime(new Date());
                    newCart.setQuantity(1);
                    newCart.setUser(user);
                    this.cartRepository.save(newCart);
                    return new JsonResultVO(JsonResultVO.SUCCESS, "添加成功");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return new JsonResultVO(JsonResultVO.FAILURE, "添加失败");
                }
            }
        } else {
            return new JsonResultVO(JsonResultVO.FAILURE, "请先登录！");
        }
    }

    /**
     * 修改购物车内某商品数量
     *
     * @param cartId 购物车ID
     * @param opt    操作：1加2减
     * @param num    数量
     * @return JsonResultVO
     */
    @Override
    public JsonResultVO changeCartGoodsNum(Long cartId, Integer opt, Integer num) {
        Cart cart = this.cartRepository.findOne(cartId);
        if (opt == 1) {
            Goods goods = cart.getGoods();
            if (goods.getStockNumber() <= cart.getQuantity()) {
                return new JsonResultVO(JsonResultVO.FAILURE, "商品库存不足！");
            } else {
                cart.setQuantity(cart.getQuantity() + num);
                this.cartRepository.saveAndFlush(cart);
                return new JsonResultVO(JsonResultVO.SUCCESS, "成功");
            }
        } else if (opt == 2) {
            if (cart.getQuantity() <= num) {
                return new JsonResultVO(JsonResultVO.FAILURE, "只剩一个啦！");
            } else {
                cart.setQuantity(cart.getQuantity() - num);
                this.cartRepository.saveAndFlush(cart);
                return new JsonResultVO(JsonResultVO.SUCCESS, "成功");
            }
        } else {
            return new JsonResultVO(JsonResultVO.FAILURE, "参数错误");
        }
    }

    /**
     * 生成订单后，购物车商品删除
     *
     * @param cartIds 购物车ids
     */
    @Override
    public void deleteCartGoodsList(String cartIds) {
        for (String cartId : cartIds.split(",")) {
            this.cartRepository.delete(Long.valueOf(cartId));
        }
    }
}
