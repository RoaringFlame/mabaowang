package com.mabao.controller;

import com.mabao.controller.vo.ExpressVO;
import com.mabao.controller.vo.GoodsVO;
import com.mabao.controller.vo.OrderVO;
import com.mabao.dao.domain.Order;
import com.mabao.dao.domain.OrderDetail;
import com.mabao.service.CartService;
import com.mabao.service.OrderService;
import com.mabao.util.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    /**
     * 订单确认页付款
     * @param cartIds                   购物车IDs
     * @param addressId                 收货地址
     * @param message                   留言
     * @return                          支付
     */
    @RequestMapping(value = "/payConfirm",method = GET)
    public String orderConfirm(@RequestParam String cartIds,
                               @RequestParam Long addressId,
                               @RequestParam(required = false) String message, RedirectAttributes attr){
        //生成订单
        Order order = this.orderService.addOrder(cartIds,addressId,message);
        //删除购物车
        this.cartService.deleteCartGoodsList(cartIds);
        //跳转到支付页面
        attr.addAttribute("orderId", order.getId());
        return  "redirect:/payOrder";
    }

    @RequestMapping(value = "/paySuccess/{orderId}",method = GET)
    public String orderPay(@PathVariable("orderId") Long orderId,RedirectAttributes attr){
        this.orderService.paySuccess(orderId);
        attr.addAttribute("state",1);
        attr.addAttribute("page",0);
        attr.addAttribute("pageSize",100);
        return  "redirect:/order/search";
    }

    /**
     * 买家查询订单页面
     * @param state                     订单状态：0待支付，1待发货，2待确认收货，3全部
     * @param page                      页面
     * @param pageSize                  分页大小
     */
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String searchOrders(Integer state,int page, int pageSize, Model model){
        Page<Order> orderPage = this.orderService.findBuyerOrders(state,page,pageSize);
        PageVO<OrderVO> voPage = new PageVO<>();
        voPage.toPage(orderPage);
        List<OrderVO> volist = new ArrayList<>();
        for(Order order : orderPage.getContent()){
            List<OrderDetail> list = this.orderService.findOrderDetailListByOrderId(order.getId());
            volist.add(OrderVO.generateBy(order, GoodsVO.generateByOrderDetail(list)));
        }
        //为分页做准备
        voPage.setItems(volist);
        Map<String, Object> map = new HashMap<>();
        map.put("orderList",voPage.getItems());
        model.addAllAttributes(map);
        if(0 == state){
            return "unpaid_order";
        }else if(1 == state){
            return "nopackaged_order";
        }else if(2 == state){
            return "ckeck_order";
        }else{
            return "purchase_order";
        }
    }

    @RequestMapping(value = "/expressSuccess/{orderId}/{expressNum}",method = GET)
    public String orderExpress(@PathVariable("orderId") Long orderId,
                               @PathVariable("expressNum") Long expressNum,
                               RedirectAttributes attr){
        this.orderService.orderExpress(orderId,expressNum);
        attr.addAttribute("state",2);
        attr.addAttribute("page",0);
        attr.addAttribute("pageSize",100);
        return  "redirect:/order/search";
    }

    /**
     * 查询订单物流信息
     * @param orderId 订单号
     */
    @RequestMapping(value = "/searchExpress/{orderId}",method = RequestMethod.GET)
    public String searchExpress(@PathVariable("orderId") Long orderId,
                                Model model) throws IOException {
        ExpressVO expressVO = orderService.findPackInfoByOrder(orderId);
        model.addAttribute("expressVO",expressVO);
        return "transport";
    }

    @RequestMapping(value = "/confirmExpress/{orderId}",method = GET)
    public String orderExpress(@PathVariable("orderId") Long orderId,
                               RedirectAttributes attr){
        this.orderService.confirmExpress(orderId);
        attr.addAttribute("state",3);
        attr.addAttribute("page",0);
        attr.addAttribute("pageSize",100);
        return  "redirect:/order/search";
    }
}
