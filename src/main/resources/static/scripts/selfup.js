"use strict";
$(function () {
    var flag = false;
        $(function () {
            var curr = new Date().getFullYear();
            var opt = {};
            opt.date = {preset: 'date'};
            opt.datetime = {
                preset: 'datetime',
                minDate: new Date(2012, 3, 10, 9, 22),
                maxDate: new Date(2014, 7, 30, 15, 44),
                stepMinute: 5
            };
            opt.time = {preset: 'time'};
            opt.tree_list = {preset: 'list', labels: ['Region', 'Country', 'City']};
            opt.image_text = {preset: 'list', labels: ['Cars']};
            opt.select = {preset: 'select'};
            <!--Script-->

            $('select.changes').bind('change', function () {
                var demo = $('#demo').val();
                $(".demos").hide();
                if (!($("#demo_" + demo).length))
                    demo = 'default';

                $("#demo_" + demo).show();
                $('#test_' + demo).val('').scroller('destroy').scroller($.extend(opt[$('#demo').val()],
                    {
                        theme: 'android-ics light',
                        mode: $('#mode').val(),
                        display: $('#display').val(),
                        dateFormat: 'yy-mm-dd',
                        setText: '确定', //确认按钮名称
                        cancelText: '清空'//取消按钮名籍我
                    }));
            });

            $('#demo').trigger('change');
        });

        //建立一個可存取到該file的url
        function getObjectURL(file) {
            var url = null;
            if (window.createObjectURL != undefined) { // basic
                url = window.createObjectURL(file);
            } else if (window.URL != undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL != undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(file);
            }
            return url;
        }

        $(function () {
            $("#fileUpload").change(function () {
                var objUrl = getObjectURL(this.files[0]);
                console.log("objUrl = " + objUrl);
                if (objUrl) {
                    $("#imgUpload").parent().append($("<img>").attr("src", objUrl));
                    flag = true;
                }
            });
            $("#imgUpload").click(function () {
                $("#fileUpload").trigger("click");
            });
        });

        //弹框事件
        function showMsg(msg) {
            //提示框弹出信息停留3秒消失
            $('#textShow').text(msg).fadeIn(1000).delay(2000).fadeOut(1000);
        }

        //数据校验
        function checkInput() {
            var goodsTitle=$("#goodsTitle").val();
            var oldPrice = $("#oldPrice").val();
            var newPrice = $("#newPrice").val();
            var upTime = $("#test_default").val();
            var z = /^[0-9]*$/;
            if((goodsTitle !== null) && (goodsTitle !== "")&&(oldPrice !== null) && (oldPrice !== "")
                &&(newPrice !== null) && (newPrice !== "")&&(upTime !== null) && (upTime !== "")){
                if (z.test(oldPrice) && z.test(newPrice)) {
                    if (flag) {
                        $(".transfer-form").submit();
                    } else {
                        showMsg("请上传图片");
                    }
                } else {
                    showMsg("价格栏必须是数字");
                }
            }else {
                showMsg("请完善宝物信息");
            }
        }

        //
        function init() {
            $("#transferButton").click(function () {
                checkInput();
            });
        }

        init();
    }
);
