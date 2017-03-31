"use strict";
//全局变量
var count = 120; //信息存储时间
var countdown;   //倒计时时间
$(function () {
    //绑定发送验证码按钮事件
    $("#sendSms").click(function () {
        sendCode($("#sendSms"));
    });
    //判断倒计时是否存在
    countdown = getCookieValue("secondsremained") ?
        getCookieValue("secondsremained") : 0;//获取cookie值
    if (countdown > 0) {
        countdown++;
        editCookie("secondsremained", countdown, countdown + 1);
        settime($("#sendSms"));//开始倒计时
    }

    //绑定提交验证码按钮事件
    $("#submit").click(function () {
        submitCode();
    });
});

//发送验证码时添加cookie
function addCookie(name, value, expiresHours) {
    var cookieString = name + "=" + value + ";path=/";
    //判断是否设置过期时间,0代表关闭浏览器时失效
    if (expiresHours > 0) {
        var date = new Date();
        date.setTime(date.getTime() + expiresHours * 1000);
        cookieString = cookieString + ";expires=" + date.toUTCString();
    }
    document.cookie = cookieString;
}

//修改cookie的值
function editCookie(name, value, expiresHours) {
    var cookieString = name + "=" + value + ";path=/";
    if (expiresHours > 0) {
        var date = new Date();
        date.setTime(date.getTime() + expiresHours * 1000); //单位是毫秒
        cookieString = cookieString + ";expires=" + date.toGMTString();
    }
    document.cookie = cookieString;
}

//根据名字获取cookie的值
function getCookieValue(name) {
    var strCookie = document.cookie;
    var arrCookie = strCookie.split("; ");
    for (var i = 0; i < arrCookie.length; i++) {
        var arr = arrCookie[i].split("=");
        if (arr[0] == name) {
            return arr[1];
            break;
        }
    }
}

function settime(obj) {
    countdown = getCookieValue("secondsremained");
    if (countdown == undefined) {
        obj.removeAttr("disabled");
        obj.text("获取验证码");
        return;
    } else {
        obj.attr("disabled", true);
        obj.text("重新发送(" + countdown + ")");
        countdown--;
        editCookie("secondsremained", countdown, countdown + 1);
    }
    setTimeout(function () {
        settime(obj)
    }, 1000); //每1000毫秒执行一次
}

//发送验证码
function sendCode(obj) {
    var result = isPhoneNum();
    if (result) {
        var phoneNum = $("#telNum").val();
        //将手机利用ajax提交到后台的发短信接口
        doPostBack('person/sendMes', sendBack, {"state": 1, "phoneNum": phoneNum});
        addCookie("secondsremained", count, count); //添加cookie记录,测试为5秒
        addCookie("phoneNum", phoneNum, count);
        settime(obj);//开始倒计时
        $('#warning').text('');
    }
}

//校验手机号是否合法
function isPhoneNum() {
    var phonenum = $("#telNum").val();
    var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if (!myreg.test(phonenum)) {
        $('#warning').text('请输入有效的手机号码！');
        return false;
    } else {
        return true;
    }
}

//发送请求并进行回调
function doPostBack(url, backFunc, queryParam) {
    $.ajax({
        cache: false,
        type: 'POST',
        url: url,// 请求的action路径
        data: queryParam,
        error: function () {// 请求失败处理函数
        },
        success: backFunc
    });
}

//提交验证码
function submitCode() {
    if (countdown > 0) {
        if ($("#code").val() != "") {
            var code = $("#code").val();
            doPostBack('person/submitCode', submitBack, {"state": 1, "code": code});
        }
        else {
            $('#warning').text('请输入验证码！');
        }
    } else {
        $('#warning').text('验证码已失效，请重新获取获取！');
        if ($("#telNum").val() == "") {
            $('#warning').text('请输入手机号点击获取验证码！');
        }
    }
}

//用于发送验证码回调
function sendBack(data) {
    $('#warning').text(data.message);
}

//发送验证绑定手机回调
function submitBack(data) {
    $('#warning').text(data.message);
    if (data.status == "success") {
        window.location = "user/bindphone"
    }
}

//测试回调1(用于发送验证码)
function backFunc1(data) {
    if (data.status != "success") {
        $('#warning').text(data.message);
    } else {//返回验证码
        alert("模拟验证码:" + data.message);
        $("#code").val(data.message);
    }
}

//测试回调2(用于绑定手机号)
function backFunc2(data) {
    if (data.status != "success") {
        $('#warning').text(data.message);
    } else {//返回验证码
        alert("回调验证码:" + data.message);
    }
}