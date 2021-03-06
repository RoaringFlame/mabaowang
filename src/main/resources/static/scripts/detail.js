"use strict";
$(function () {
    //定义全局的商品id函数
    var goodsId = $("#goodsId").text();
    //详情页面弹框效果的实现
    function showMsg(msg) {
        //提示框弹出信息停留3秒消失
        $('#textShow').text(msg).fadeIn(1000).delay(2000).fadeOut(1000);
    }

    //点击加入购物车的实现
    function addToCarts(){
        $("#textShow").hide();                                      //提示框的隐藏
        $("#addToCarts").click(function () {                       //点击添加到购物车按钮判断是否成功加入购物车
            $.get(MB.getContextPath()+"/cart/cartAddGoods", {goodsId: goodsId}, function (data) {
                console.log(data);
                console.log("详情页面弹框效果的实现");
                if(data.status == "success"){
                    showMsg(data.message);
                }
                else if(data.status == "failure"){
                    window.location = MB.getContextPath()+"/user";
                }
            });
        });
    }

    //点击立即购买的实现
    function buyNow(){
        $("#textShow").hide();                          //提示框的隐藏
        $("#buyNow").click(function () {
            $.get(MB.getContextPath()+"/cart/cartAddGoods", {goodsId: goodsId}, function (data) {
                console.log(data);
                console.log("详情页面弹框效果的实现");
                if (data.status == "success") {
                    window.location = MB.getContextPath()+"/cart/index";
                } else {
                    window.location = MB.getContextPath()+"/user";
                }
            });
        });
    }

    //函数的初始化
    function init() {
        addToCarts();
        buyNow();
    }
    init();
});
