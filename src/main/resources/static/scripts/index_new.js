"use strict";
$(function () {
    //设置全局参数
    var totalPageNew = 1;                               //新品总页数
    var totalPageLike = 1;                              //猜你喜欢总页数
    var currentPageNew = 0;                             //新品展示页当前页面
    var currentPageLike = 0;                            //猜你喜欢当前页面
    var pageSize = 6;                                   //每页展示的宝物数量
    var goodsTypeId = "";                               //商品类型id
    var babyIF = null;                                  //宝宝对象
    var isNew = true;                                   //是否当前展示的是新品
    var GoodsBox = $("#hideGoods");                     //查找到新品列表下的li标签
    var GoodsListBox = $("#GoodsList");                 //新品展示
    var typeSidebar = $("#sidebar");                    //侧边栏
    var GoodsShow = $("#GoodsShow");                     //商品展示
    var likeForm = $("#likeForm");                      //猜你喜欢表单
    var loadMore = $("#loadMore");                      //加载更多按钮
    var noMore = $("#noMore");                          //无更多按钮

    function showMsg(msg) {
        //提示框弹出信息停留2秒消失
        $('#textShow').text(msg).fadeIn(500).delay(1000).fadeOut(500);
    }

    //轮播初始化
    function initCarousel(smallBanner) {                        //获取轮播图片集
        var myCarousel = $("#myCarousel");                      //找到页面对应id为myCarousel的项
        $(".carousel-indicators").empty();                            //删除静态数据
        $(".carousel-inner").empty();
        $(function () {                                         //设定轮播时间2秒
            $('.carousel').carousel({
                interval: 2000
            })
        });
        //遍历获取到的轮播图片集,index为索引（从0开始），banner自己定义的名称用来取smallBanner中的值
        $(smallBanner).each(function (index, banner) {
            var li = $("<li></li>")                             //添加li标签并为其添加属性值
                .attr("data-target", "#myCarousel")
                .attr("data-slide-to", index);
            var img = $("<div></div>").addClass("item")        //在div中添加一些属性
                .append($("<img>")
                    .attr("src", MB.getRootPath() + "/upload/" + banner.picture)           //添加图片
                    .attr("alt", banner.alt)                                                //添加提示信息
                    .click(function () {                                                    //点击图片跳转到商品详情
                        window.location = "goods/goodsDetail?goodsId=" + banner.goodsId;
                    })
                );
            if (index == 0) {
                //索引为0的轮播图设0为主轮播图
                li.addClass("active");
                img.addClass("active");
            }
            //将li标签信息添加到轮播导航中
            myCarousel.find(".carousel-indicators").append(li);
            //将img标签信息添加到论波栏目中
            myCarousel.find(".carousel-inner").append(img);
        });
    }

    //填充商品信息到列表
    function writeGoodsInfo(data) {
        var goodsList = data.items;
        $(goodsList).each(function (index, goods) {                                      //对新品进行遍历
            var newGoods = GoodsBox.clone();                                             //克隆信息
            newGoods.find("img").attr("src", MB.getRootPath() + "/upload/" + goods.picture)
                .click(function () {                                                     //点击图片跳转到商品详情
                    window.location = "goods/goodsDetail?goodsId=" + goods.id;
                });
            newGoods.find("div>p:eq(0)>span:eq(0)").text("￥" + goods.price);
            newGoods.find("div>p:eq(0)>span:eq(1)").text(goods.newDegree);
            newGoods.find("div>p:eq(1)").text(goods.brandName);
            newGoods.find("div>p:eq(2)").text(goods.title);
            GoodsListBox.append(newGoods);
        });
        if (data.items.length < pageSize) {
            infoNoMore();
        } else {
            showLoadMoreButton();
        }
    }

    //提示无更多商品
    function infoNoMore() {
        loadMore.hide();
        noMore.show();
    }

    //显示加载更多商品按钮
    function showLoadMoreButton() {
        loadMore.show();
        noMore.hide();
    }

    //按过滤条件查询新品
    function searchNewGoods() {
        isNew = true;
        typeSidebar.addClass("hide");                 //侧边栏添加隐藏样式
        var params = {
            page: currentPageNew,                           //新品当前页面数
            pageSize: pageSize,                                //新品每页展示的数据信息
            searchKey: $("#txtSearch").val(),                //搜索关键字
            goodsTypeId: goodsTypeId                          //宝物对应的id
        };
        if (currentPageNew <= totalPageNew) {                   //判断当前页面是否小于等于总页数
            MB.sendAjax("get", "home/goodsSearch", params, function (data) {
                totalPageNew = data.totalPage;                                                    //获取总页数
                currentPageNew = data.currentPage;
                writeGoodsInfo(data);
            });
        } else {
            infoNoMore();
        }
    }

    //初始化新品
    function initNewGoodsBox() {
        GoodsListBox.empty();
        currentPageNew = 0;
        searchNewGoods();
    }


    //新品点击下划线
    function newGoodsButtonClicked() {
        GoodsShow.find("div.scroll-menu ul li:eq(0)").addClass("focus");                                  //点击新品时给新品加红色下划线
        GoodsShow.find("div.scroll-menu ul li:eq(1)").removeClass("focus");                               //猜你喜欢下无红色下划线
        GoodsListBox.show();                                                                              //点击新品时新品列表的显示
        likeForm.hide();                                                                                  //猜你喜欢列表的隐藏
    }

    //猜你喜欢下划线（与新品相反）
    function guessButtonClicked() {
        GoodsShow.find("div.scroll-menu ul li:eq(1)").addClass("focus");
        GoodsShow.find("div.scroll-menu ul li:eq(0)").removeClass("focus");
        GoodsListBox.hide();
        likeForm.show();
    }

    //按宝宝信息查询猜你喜欢
    function searchGoodsLike() {
        isNew = false;
        typeSidebar.addClass("hide");               //侧边栏添加隐藏样式（假设侧边栏被点开时）
        var params = {
            name: babyIF.name,                        //宝宝姓名
            birthday: babyIF.birthday,                //宝宝生日
            gender: babyIF.gender,                    //宝宝性别
            hobby: babyIF.hobby,                      //宝宝爱好
            page: currentPageLike,                   //猜你喜欢当前页面
            pageSize: pageSize                      //猜你喜欢每页展示的物品数量
        };
        //控制猜你喜欢的加载
        if (currentPageLike <= totalPageLike) {       //判断当前页面是否小于等于总页数
            MB.sendAjax("get", "home/goodsGuess", params, function (data) {
                totalPageLike = data.totalPage;       //获取猜你喜欢总页数
                writeGoodsInfo(data);
            });
        } else {
            infoNoMore();
        }
    }

    //初始化猜你喜欢的商品
    function initLikeBox() {
        if (babyIF != "" && babyIF != null && typeof(babyIF) != "undefined") {                                 //如果宝宝id是存在加载猜你喜欢物品列表页
            GoodsListBox.empty();
            GoodsListBox.show();
            likeForm.hide();
            currentPageLike = 0;
            searchGoodsLike();
        }
        else {                                           //宝宝信息不存在点击猜你喜欢时显示表单页面
            likeForm.removeClass("hide");         //显示猜你喜欢表单
        }
    }

    //初始化新品和猜你喜欢的切换
    function initGoodsShiftAction() {
        //进去就加载新品
        GoodsListBox.show();                                                          //新品页初始化为显示
        likeForm.hide();                                                         //猜你喜欢页初始化为隐藏
        GoodsShow.find("div.scroll-menu ul li:eq(0)").click(function () {              //找到div对应的scroll-menu下的ul得第一个li
            if (!isNew) {
                isNew = true;                                                       //是否为新品 设为真
                newGoodsButtonClicked();                                             //新品点击下划线
                typeSidebar.addClass("hide");                                        //侧边栏添加隐藏样式
                initNewGoodsBox();                                                   //加载新品
            }
        });
        GoodsShow.find("div.scroll-menu ul li:eq(1)").click(function () {
            if (isNew) {
                isNew = false;                                                      //是否为新品 设为假
                guessButtonClicked();
                noMore.hide();
                loadMore.hide();
                typeSidebar.addClass("hide");                                        //侧边栏添加隐藏样式
                initLikeBox();
            }
        });
    }

    //点击类别或者搜索新品执行搜索
    function searchGoods() {
        if (isNew == true) {
            initNewGoodsBox();
        } else if (isNew == false) {
            isNew = true;                                    //新品页面展示搜索物品列表，isNew设置为true
            newGoodsButtonClicked();                         //新品下划线
            initNewGoodsBox();
        }
    }

    //侧边栏初始化
    function initGoodsType(typeList) {
        $("#searchBox").find("div.column").click(function () {
            typeSidebar.toggleClass("hide");                  //侧边栏的隐藏与显示，toggleClass()函数，当属性存在则移除，当属性不存在则添加属性
        });
        typeList.unshift({key: "", value: "所有"});          //添加点击跳转所有的选项，unshift()方法可向数组的开头添加一个或更多元素，并返回新的长度
        $(typeList).each(function (index, goodsType) {
            var li = $("<li></li>")
                .text(goodsType.value)
                .click(function () {
                    if (index > 0) {                           //去掉下标为0的值
                        goodsTypeId = goodsType.key;           //获取商品类型id
                    }
                    else {
                        goodsTypeId = null;                    //设置商品id为空
                    }
                    searchGoods();
                });
            typeSidebar.find("ul").append(li);
        });
    }

    //搜索框初始化
    function initSearchAction() {
        var txtSearch = $("#txtSearch");
        txtSearch.click(function () {
            typeSidebar.addClass("hide");                //侧边栏添加隐藏样式
        });
        txtSearch.change(function () {                   //手机上点击完成实现搜索
            searchGoods();
        });
        $("#searchButton").click(function () {
            searchGoods();
        });
    }

    //初始化猜你喜欢宝宝相关
    function initBaby(baby, gender) {
        //初始化宝宝
        if (baby != "" && baby != null && typeof(baby) != "undefined") {
            console.log(babyIF);
            babyIF = baby;
        }
        //初始化宝宝性别
        var genderList = gender;
        var genderSelector = $("#sex");
        genderSelector.empty();
        $(genderList).each(function (index, gender) {
            var option = $("<option></option>").val(gender.key).text(gender.value);
            if (index == 0) {
                option.attr("checked", true);
            }
            genderSelector.append(option);
        });
    }

    //猜你喜欢表单提交按钮操作
    function initFormAction() {
        $("#btnLikeSubmit").click(function () {
            var likeForm = $("#likeForm");
            var babyName = likeForm.find("input[name='babyName']").val();              //宝宝姓名的获取
            var babyBirthday = likeForm.find("input[name='babyBirthday']").val();     //宝宝生日的获取
            var gender = likeForm.find("select[name='sex']").val();                    //宝宝性别的获取
            var hobby = likeForm.find("input[name='hobby']").val();                    //宝宝爱好的获取
            var params = {
                name: babyName,                       //宝宝姓名
                birthday: babyBirthday,              //宝宝生日
                gender: gender,                      //宝宝性别
                hobby: hobby                         //宝宝爱好
            };
            if (babyName !== "" && babyBirthday !== "" && gender !== "" && hobby !== "") {
                MB.sendAjax("get", "home/babySubmit", params, function (data) {
                    data.birthday =babyBirthday;
                    babyIF = data;
                    initLikeBox();
                });
            } else {
                showMsg("请填写完整宝宝信息!");
            }
        });
    }

    //加载更多按钮事件初始化
    function initLoadMoreAction(){
        loadMore.click(function(){
            if(isNew){
                currentPageNew++;
                searchNewGoods();
            }else {
                currentPageLike++;
                searchGoodsLike();
            }
        });
    }

    //首页信息初始化
    function initIndexPage() {
        $("#textShow").hide();                                      //提示框的隐藏
        MB.sendAjax("get", "home", {}, function (data) {
            //侧边栏的初始化
            initGoodsType(data.goodsTypeList);
            //轮播的初始化
            initCarousel(data.smallBanner);
            //猜你喜欢初始化宝宝相关信息
            initBaby(data.baby, data.gender);
            //新品初始化，只初始化一次
            initNewGoodsBox();
        });
    }

    //首页事件控制初始化
    function initEvent() {
        //搜索框事件
        initSearchAction();
        //控制新品和猜你喜欢的切换
        initGoodsShiftAction();
        //猜你喜欢表单提交按钮控制
        initFormAction();
        //加载更多按钮
        initLoadMoreAction();
    }

    //初始化函数
    function init() {
        //首页信息初始化
        initIndexPage();
        //事件控制
        initEvent();
    }

    //调用初始化函数
    init();
});