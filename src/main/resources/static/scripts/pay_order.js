"use strict";
$(function () {
    $("#ZhiFuBao").click(function () {
        $("#ZhiFuBaoActive").addClass("checked-active");
        $("#WeiXinActive").removeClass("checked-active");
    });

    $("#WeiXin").click(function () {
        $("#ZhiFuBaoActive").removeClass("checked-active");
        $("#WeiXinActive").addClass("checked-active");
    });
});