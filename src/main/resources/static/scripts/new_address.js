"use strict";
$(function () {
    var provinceId;                //省份id
    var cityId;                    //城市id
    var areaId;                    //地区id
    var dropDownList = $(".edit-add-box").find("label:eq(2)");      //下拉框
    var provinces = dropDownList.find("select:eq(0)");                //省下拉框
    var cities = dropDownList.find("select:eq(1)");                 //市下拉框
    var countries = dropDownList.find("select:eq(2)");              //区下拉框
    var addressForm = $("#frmAddress");                              //获取表单
    var submit = $(".edit-add-input input");                        //提交按钮
    //初始化第一个下拉框
    function initDropDownList() {
        $.get(MB.getContextPath() + "/provinces", {}, function (data) {
            $(data).each(function (index, province) {
                provinces.append(                   //为第一个下拉框添加省份节点
                    $("<option></option>")
                        .val(province.key)
                        .text(province.value)
                );
            });
            fistDropDownListChange();
        }, "json");
    }

    //第一个下拉框改变事件
    function fistDropDownListChange() {
        provinceId = provinces.val();               //获取第二个下拉框城市对应的provinceId
        $.get(MB.getContextPath() + "/province/" + provinceId + "/allCity", {}, function (data) {
            $(data).each(function (index, city) {                           //为第二个下拉框添加对应城市信息
                if (city.key !== cityId) {
                    cities.append($("<option></option>")
                        .val(city.key)
                        .text(city.value)
                    );
                }
            });
            secondDropDownListChange();
        }, "json");
    }

    //第二个下拉框改变事件
    function secondDropDownListChange() {
        cityId = cities.val();              //获取第二个下拉框城市对应的cartId
        $.get(MB.getContextPath() + "/city/" + cityId + "/allCounty", {}, function (data) {
            //countries.empty();                                             //移除第三个下拉框的城区节点
            $(data).each(function (index, country) {
                if (country.key !== areaId) {
                    countries.append($("<option></option>")                  //为第三个下拉框添加对应城市的区信息
                        .val(country.key)
                        .text(country.value)
                    );
                }
            });
        }, "json");
    }

    //弹框事件
    function showMsg(msg) {
        //提示框弹出信息停留3秒消失
        $('#textShow').text(msg).fadeIn(1000).delay(2000).fadeOut(1000);
    }

    function init() {
        $("#textShow").hide();                   //提示框初始化为隐藏
        submit.addClass("default");              //提交按钮样式
        //初始化第一个下拉框
        initDropDownList();
        //第一个下拉框值改变事件
        provinces.change(function () {
            cities.empty();                                    //移除第二个下拉框节点
            countries.empty();                                 //移除第三个下拉框节点
            fistDropDownListChange();
        });
        //第二个下拉框值改变事件
        cities.change(function () {
            countries.empty();                                         //移除第三个下拉框的城区节点
            secondDropDownListChange();
        });
        //第三个下拉框值改变事件
        countries.change(function () {
            areaId = $(this).val();
            $(this).next("input[name='areaId']").val(areaId);
        });


        submit.click(function () {
            var recipients = $(".edit-add-box input:eq(0)").val();           //获取收件人输入框值
            var tel = $(".edit-add-box input:eq(1)").val();                  //获取电话输入框值
            areaId = countries.val();                                        //获取区下拉框的值
            $("input[name='areaId']").val(areaId);                           //为areaId隐藏输入框复制
            areaId = $(".edit-add-box input:eq(2)").val();                   //获取areaId的值
            var location = $(".edit-add-box input:eq(3)").val();             //获取所在地输入框的值
            if (recipients == "" || tel == "" || areaId == "" || location == "" || areaId == "请选择") {
                showMsg("请完善地址信息！");                                 //如果信息不完整弹出提示框
            } else if (!(/^1[3|4|5|7|8]\d{9}$/.test(tel))) {
                showMsg("您的电话格式不对！");                               //如果电话号码格式不对弹出提示框
            } else {
                addressForm.attr("method", "post");                                                //以post方式提交表单
                addressForm.attr("action", MB.getContextPath() + "/user/address/addAddress");
                addressForm.submit();                                    //如果信息完整则提交表单
            }

        });
    }

    init();
});










