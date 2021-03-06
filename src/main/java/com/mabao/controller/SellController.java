package com.mabao.controller;

import com.mabao.controller.vo.AddressVO;
import com.mabao.controller.vo.GoodsDetailVO;
import com.mabao.controller.vo.GoodsVO;
import com.mabao.dao.enums.Gender;
import com.mabao.dao.enums.OrderStatus;
import com.mabao.dao.enums.Quality;
import com.mabao.dao.domain.Address;
import com.mabao.dao.domain.Goods;
import com.mabao.dao.domain.OrderDetail;
import com.mabao.service.*;
import com.mabao.util.PageVO;
import com.mabao.util.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/sell")
public class SellController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private GoodsBrandService goodsBrandService;
    @Autowired
    private GoodsSizeService goodsSizeService;
    @Autowired
    private OrderService orderService;

    /**
     * 寄售
     * (添加售货地址)
     * @param addressVO               地址对象，需包含用户ID
     * @return                      添加结果页面
     */
    @RequestMapping(value = "/receiptPlaceAdd",method = POST)
    public String addReceiptPlace(AddressVO addressVO){
        Address address = this.addressService.addAddress(addressVO);
        return address != null ? "consignment_success" : "consignment_failure";
    }


    /**
     * 自助发布宝物页下拉菜单
     * @param model             map
     * @return                  自助发布宝物页
     */
    @RequestMapping(value = "/releaseSelector",method = GET)
    public String releaseGoodsSelector(Model model){
        Map<String,Object> map = new HashMap<>();
        //商品类型
        List<Selector> goodsTypeList = this.goodsTypeService.getAllGoodsTypeForSelector();
        map.put("goodsType",goodsTypeList);
        //品牌
        List<Selector> brand = this.goodsBrandService.findBrandForSelector();
        map.put("brand",brand);
        //宝宝性别
        List<Selector> gender = Gender.toList();
        map.put("gender",gender);
        //新旧程度
        List<Selector> newDegree = Quality.toList();
        map.put("newDegree",newDegree);
        //尺码
        List<Selector> goodsSize = this.goodsSizeService.findGoodsSizeForSelector();
        map.put("goodsSize",goodsSize);
        model.addAllAttributes(map);
        return "selfup";
    }



    /**
     * 自助发布宝物
     * 添加商品
     * @param newGoods          商品对象
     * @return                  寄售成功页
     */
    @RequestMapping(value = "/release",method = RequestMethod.POST)
    public String releaseGoods(GoodsDetailVO newGoods,
                               @RequestParam(required = false) MultipartFile[] goodsPic,
                               HttpServletRequest request) throws Exception {
        Goods result= this.goodsService.releaseGoods(newGoods,goodsPic,request);
        if (result != null){
            return "publish_success";
        }else {
            return "error";
        }
    }

    /**
     * 卖家查询商品寄售状态
     * @param goodsState           商品状态：1已发布，2待发布，3已出售，4所有
     * @param page                 页码
     * @param pageSize             页面大小
     * @return                     查询内容
     */
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String searchSell(Integer goodsState,int page, int pageSize, Model model){
        Page<Goods> goodsPage = this.goodsService.findSellerGoods(goodsState,page,pageSize);
        PageVO<GoodsVO> voPage = new PageVO<>();
        voPage.toPage(goodsPage);
        voPage.setItems(GoodsVO.generateBy(goodsPage.getContent()));//将goods转换成goodsvo
        List<GoodsVO> list = voPage.getItems();
        if(goodsState >= 3){
            for(GoodsVO goodsVo:list){
                if("已售罄".equals(goodsVo.getState())) {
                    List<OrderDetail> orderDetailList = this.orderService.findOrderDetail(goodsVo.getId());
                    if(orderDetailList!=null&&(orderDetailList.size()!=0)){
                        OrderStatus orderStatus = orderDetailList.get(0).getOrder().getState();
                        if(orderStatus!=OrderStatus.Completed){
                            goodsVo.setState("交易中");
                        }else{
                            goodsVo.setState("交易完成");
                        }
                    }
                }
            }
        }
        //分页接口准备
        voPage.setItems(list);
        Map<String, Object> map = new HashMap<>();
        map.put("goodsList",voPage.getItems());
        model.addAllAttributes(map);
        if(1 == goodsState){
            return "published_order";
        }else if(2 == goodsState){
            return "unpublished_order";
        }else if(3 == goodsState){
            return "finish_order";
        }else{
            return "consignment_order";
        }
    }
}

