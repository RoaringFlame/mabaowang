"use strict";
$(function () {
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
            if (objUrl) {
//						$("#imgUpload").attr("src", objUrl);
                var path = MB.getRootPath();
                path += "/" + "person/headerPic";
                var filename = $("#fileUpload").val();
                var formData = new FormData();
                formData.append('headerPic', $('#fileUpload')[0].files[0]);
                $.ajax({
                    url: path,
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false
                }).done(function (res) {
                    window.location = "user";
                }).fail(function (res) {
                    window.location = "user";
                });
            }
        });
        $("#imgUpload").click(function () {
            $("#fileUpload").trigger("click");
        });
    });

});
