var tes = 0;
var isShow = true;

var limCount = 15;

var maxLen = 0;

var imgPrefix;

$(function(){
    //modelandview 返回的参数只能在html用[[${}}]]格式获取
    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    var leftMenuStorage = localStorage.getItem("leftMenuStorage");

    $("#permUl").html(leftMenuStorage);
    addActClass($("#liParent1").children(":eq(0)"));
    $("#liParent1").children(":eq(1)").children(":eq(3)").find("a").css("color","#FFD600");

    loadUserList();

    $("#topShowLp").css("display","none");

    /*var fixHelperModified = function(e, tr) {
            //children() 方法返回返回被选元素的所有直接子元素
            var $originals = tr.children();
            //clone() 方法生成被选元素的副本，包含子节点、文本和属性
            var $helper = tr.clone();
            //each() 方法规定为每个匹配元素规定运行的函数
            $helper.children().each(function(index) {
                //width() 方法返回或设置匹配元素的宽度
                //eq() 方法将匹配元素集缩减值指定 index 上的一个
                $(this).width($originals.eq(index).width())
            });
            return $helper;
        },
        updateIndex = function(e, ui) {
            //ui.item - 表示当前拖拽的元素
            //parent() 获得当前匹配元素集合中每个元素的父元素，使用选择器进行筛选是可选的
            $('td.index', ui.item.parent()).each(function(i) {
                //html() 方法返回或设置被选元素的内容 (inner HTML)
                $(this).html(i + 1);
            });
        };
    $("#sort tbody").sortable({
        //设置是否在拖拽元素时，显示一个辅助的元素。可选值：'original', 'clone'
        helper: fixHelperModified,
        //当排序动作结束时触发此事件。
        stop: updateIndex
    }).disableSelection();*/

    $("i").tooltip();


    var ajaxFormOption = {
        //target: '#output', //把服务器返回的内容放入id为output的元素中
        beforeSubmit: showRequest, //提交前的回调函数
        success: showResponse, //提交后的回调函数
        url: "/site/setSite", //默认是form的action， 如果申明，则会覆盖
        type: "post", //默认是form的method（get or post），如果申明，则会覆盖
        //data:
        dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
        //clearForm: true,   //成功提交后，清除所有表单元素的值
        //resetForm: true,  //成功提交后，重置所有表单元素的值
        timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
    }


    //不需要submit按钮，可以是任何元素的click事件
    $("#myFormButton").click(function () {
        $("#permForm").ajaxSubmit(ajaxFormOption);
        return false;
    });

    $("#file").on("change",function () {
        uploadIcon();
    });

    //监听窗口，显示隐藏move top
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
});

function showRequest(formData, jqForm, options){
    //alert("-----------------------------errr");
    //formData: 数组对象，提交表单时，Form插件会以Ajax方式自动提交这些数据，
    // 格式如：[{name:user,value:val },{name:pwd,value:pwd}]
    // jqForm:   jQuery对象，封装了表单的元素
    // options:  options对象
    //var queryString = $.param(formData); //name=1&address=2
    //var formElement = jqForm[0]; //将jqForm转换为DOM对象
    //var address = formElement.address.value; //访问jqForm的DOM元素
    return true; //只要不返回false，表单都会提交,在这里可以对表单元素进行验证
};
function showResponse(data, status){

    //responseText 返回的数据
    //statusText 返回标识

    if(data.code==1000){

        //刷新数据
        loadUserList(1,limCount);

        myResetForm();

        //关闭模态框
        $("#compose-modal").modal("hide");

        //成功
        /**
         * jquery.confirm.js 使用阿里彩色图标
         * 修改源码，搜索<i  找到找到第二个，
         * i标签替换成<svg class="icon" aria-hidden="true"><use xlink:href="'+this.icon+'"></use></svg>即可，
         * */
        $.confirm({
            /*icon: 'glyphicon glyphicon-heart',*/
            /*icon: 'fa fa-bug',*/
            icon: '#icon-jinlingyingcaitubiao14',
            theme: 'modern',
            title: '提示',
            content: data.msg,
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            autoClose: 'showMyMsg|3000',
            buttons: {
                showMyMsg: {
                    text: '关闭',
                    action: function () {

                    }
                }
            }
        });
    }else if(data.code==1001){
        //系统接口错误
        /**
         * jquery.confirm.js 使用阿里彩色图标
         * 修改源码，搜索<i  找到找到第二个，
         * i标签替换成<svg class="icon" aria-hidden="true"><use xlink:href="'+this.icon+'"></use></svg>即可，
         * */
        $.confirm({
            /*icon: 'glyphicon glyphicon-heart',*/
            /*icon: 'fa fa-bug',*/
            icon: '#icon-shangxin1',
            theme: 'modern',
            title: '提示',
            content: data.msg,
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            autoClose: 'showMyMsg|3000',
            buttons: {
                showMyMsg: {
                    text: '关闭',
                    action: function () {

                    }
                }
            }
        });

    }else{
        /**
         * jquery.confirm.js 使用阿里彩色图标
         * 修改源码，搜索<i  找到找到第二个，
         * i标签替换成<svg class="icon" aria-hidden="true"><use xlink:href="'+this.icon+'"></use></svg>即可，
         * */
        $.confirm({
            /*icon: 'glyphicon glyphicon-heart',*/
            /*icon: 'fa fa-bug',*/
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: data.msg,
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            autoClose: 'showMyMsg|3000',
            buttons: {
                showMyMsg: {
                    text: '关闭',
                    action: function () {


                    }
                }/*,
                                cancel: function () {

                                }*/
            }
        });
    }

}

/**
 * 加载数据
 * */
function loadUserList(){

    $.post("/site/getSiteList",function(data){

        if(data.code==1000){

            $("#userListUl").html("");
            imgPrefix = data.obj;
            $.each(data.data,function(ind,val){

                if (val.pid == 0) {

                    $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                        "<td data-label='站点名称' style='text-align: left'>" +
                        "<span class='handle'>"+val.name+"</span>" +
                        "</td>" +
                        "<td data-label='站点图标'>" +
                        "<div class='my-table-imgDiv'><img data-original='"+data.obj+val.photo+"' alt='站点图标' src='"+data.obj+val.photo+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                        "</td>" +
                        "<td data-label='站点地址描述'>" +
                        "<small id='"+val.id+"-descript' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.address+"</small>" +
                        "</td>" +
                        "<td data-label='优先级'>" +
                        "<small id='"+val.id+"-zindex' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.zindex+"</small>" +
                        "</td>" +
                        "<td data-label='操作'>" +
                        "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                        "<i class='fa fa-plus' data-toggle='tooltip' title='' data-original-title='添加子站点' style='cursor: pointer' onclick='addSun("+val.id+",1"+")'></i>&nbsp;" +
                        "<i class='fa fa-edit' data-toggle='tooltip' title='' data-original-title='编辑' style='cursor: pointer' onclick='editPerm("+val.id+",0"+")'></i>&nbsp;" +
                        "<i class='fa fa-trash-o' data-toggle='tooltip' title='' data-original-title='删除' style='cursor: pointer' onclick='delPerm("+val.id+","+"\""+val.name+"\")'></i>" +
                        "</div>" +
                        "</td>" +
                        "</tr>");

                    var sonArray = getParentArry(val.id,data.data);

                    if(sonArray.length>0){

                        //给这个tr加一个图标
                        $("#"+val.id+"-myStyleLi").find("td:eq(0) span").before("<i class='fa fa-caret-down' data-widget='collapse' style='cursor: pointer' onclick='showOrHideSun("+val.id+")'></i>&nbsp;");
                        $.each(sonArray,function (ind2,val2) {
                            $("#"+val.id+"-myStyleLi").after("<tr id='"+val2.id+"-myStyleLi' class='box-body"+val.id+"' onmouseover='showED("+val2.id+")' onmouseleave='hideED("+val2.id+")'>" +
                                "<td data-label='站点名称' style='text-align: left'>" +
                                "<span class='handle' style='padding-left: 15%;'>"+val2.name+"</span>" +
                                "</td>" +
                                "<td data-label='站点图标'>" +
                                "<div class='my-table-imgDiv'><img data-original='"+data.obj+val2.photo+"' alt='站点图标' src='"+data.obj+val2.photo+"' class='online' style='width:100%'/></div>" +
                                "</td>" +
                                "<td data-label='站点地址描述'>" +
                                "<small id='"+val2.id+"-descript' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val2.address+"</small>" +
                                "</td>" +
                                "<td data-label='优先级'>" +
                                "<small id='"+val2.id+"-zindex' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val2.zindex+"</small>" +
                                "</td>" +
                                "<td data-label='操作'>" +
                                "<div class='tools' data-label='"+val2.id+"' style='color: #f56954;display: none'>" +
                                "<i class='fa fa-plus' data-toggle='tooltip' title='' data-original-title='添加子站点' style='cursor: pointer' onclick='addSun("+val2.id+",1"+")'></i>&nbsp;" +
                                "<i class='fa fa-edit' data-toggle='tooltip' title='' data-original-title='编辑' style='cursor: pointer' onclick='editPerm("+val2.id+",0"+")'></i>&nbsp;" +
                                "<i class='fa fa-trash-o' data-toggle='tooltip' title='' data-original-title='删除' style='cursor: pointer' onclick='delPerm("+val2.id+","+"\""+val2.name+"\")'></i>" +
                                "</div>" +
                                "</td>" +
                                "</tr>");

                        });
                    }

                }else{

                    var sonArray = getParentArry(val.id,data.data);

                    if(sonArray.length>0){
                        var gParent = $("#"+val.id+"-myStyleLi").attr('class');

                        $("#"+val.id+"-myStyleLi").find("td:eq(0) span").css("padding-left","0");
                        //给这个tr加一个图标
                        $("#"+val.id+"-myStyleLi").find("td:eq(0) span").before("<i class='fa fa-caret-down' style='cursor: pointer;padding-left: 16%;' onclick='showOrHideSun("+val.id+")'></i>&nbsp;");
                        $.each(sonArray,function (ind2,val2) {

                            $("#"+val.id+"-myStyleLi").after("<tr id='"+val2.id+"-myStyleLi' class='"+gParent+" box-body"+val.id+"' onmouseover='showED("+val2.id+")' onmouseleave='hideED("+val2.id+")'>" +
                                "<td data-label='权限名称' style='text-align: left'>" +
                                "<span class='handle' style='padding-left: 23%'>"+val2.name+"</span>" +
                                "</td>" +
                                "<td data-label='站点图标'>" +
                                "<div class='my-table-imgDiv'><img data-original='"+data.obj+val2.photo+"' alt='站点图标' src='"+data.obj+val2.photo+"' class='online' style='width:100%'/></div>" +
                                "</td>" +
                                "<td data-label='站点地址描述'>" +
                                "<small id='"+val2.id+"-descript' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val2.address+"</small>" +
                                "</td>" +
                                "<td data-label='优先级'>" +
                                "<small id='"+val2.id+"-zindex' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val2.zindex+"</small>" +
                                "</td>" +
                                "<td data-label='操作'>" +
                                "<div class='tools' data-label='"+val2.id+"' style='color: #f56954;display: none'>" +
                                "<i class='fa fa-plus' data-toggle='tooltip' title='' data-original-title='添加子站点' style='cursor: pointer' onclick='addSun("+val2.id+",1"+")'></i>&nbsp;" +
                                "<i class='fa fa-edit' data-toggle='tooltip' title='' data-original-title='编辑' style='cursor: pointer' onclick='editPerm("+val2.id+",0"+")'></i>&nbsp;" +
                                "<i class='fa fa-trash-o' data-toggle='tooltip' title='' data-original-title='删除' style='cursor: pointer' onclick='delPerm("+val2.id+","+"\""+val2.name+"\")'></i>" +
                                "</div>" +
                                "</td>" +
                                "</tr>");

                        });
                    }

                }

            });

        }else if(data.code==1001){
            //系统接口错误
            $.confirm({
                icon: '#icon-shangxin1',
                theme: 'modern',
                title: '提示',
                content: '获取权限数据失败',
                animation: 'news',//动画
                closeAnimation: 'news',//关闭动画
                autoClose: 'showMyMsg|3000',
                buttons: {
                    showMyMsg: {
                        text: '关闭',
                        action: function () {

                        }
                    }
                }
            });

        }

        $('#userListUl').viewer({
            url: 'data-original',
        });

    });
}

function showED(cur) {
    $("div[data-label='"+cur+"']").css("display","block");
}

function hideED(cur) {
    $("div[data-label='"+cur+"']").css("display","none");
}

function showOrHideSun(did) {
    /**
     * $("#test1").prev();  // 上一个兄弟节点
     $("#test1").prevAll(); // 之前所有兄弟节点
     $("#test1").next(); // 下一个兄弟节点
     $("#test1").nextAll(); // 之后所有兄弟节点
     $("#test1").siblings(); // 所有兄弟节点
     * */
        //Find the box parent
    var box = $("#"+did+"-myStyleLi");
    //console.log(box);
    //Find the body and the footer
    //var bf = box.find(".box-body, .box-footer");
    var bf = box.siblings(".box-body"+did);
    if (!box.hasClass("collapsed-box")) {
        box.addClass("collapsed-box");
        bf.slideUp();
    } else {
        box.removeClass("collapsed-box");
        bf.slideDown();
    }

}

function addSun(pid,flag){

    $("#type").val("");
    $("#id").val("");
    $("#pid").val("");
    $("#iconImg").val("");
    //赋值经纬度
    $("#lng").val("");
    $("#lat").val("");
    $("#iconImgDiv").remove();

    myResetForm();

    if(null!=pid){
        //flag[0:开通权限；1：新增子节点权限]
        //type[0:编辑；1：新增]
        var msg = '';
        if(flag==0){
            $("#type").val(1);
            $("#pid").val(0);
            msg = '新增站点';
        }else{
            //设置父id
            $("#type").val(1);
            $("#pid").val(pid);
            msg = "新增子站点";
        }

        //赋值类型
        if($("#cb9").prop('checked')){
            $("#istype").val(1);
        }else{
            $("#istype").val(0);
        }

        $("#chgUpdTitle").text(msg);

        /*//创建地图
        var map = new AMap.Map("my-map", {
            resizeEnable: true
        });

        //输入提示
        var auto = new AMap.Autocomplete({
            input: "tipinput"
        });*/


        /*//工厂方法
        var locaMap = Loca.create('my-map', {
            pitch: 50,
            center: [116.473168, 39.993015],
            zoom: 15,
            mapStyle: 'amap://styles/twilight',
            // Loca 自 1.2.0 起 viewMode 模式默认为 3D，如需 2D 模式，请显示配置。
            // viewMode: '3D'
        });

        // 图层创建
        var vl = Loca.visualLayer({
            eventSupport: true,
            type: 'point',
            shape: 'image',
            container: locaMap
        });

        vl.on('click', function (ev) {
            var rawData = ev.rawData;
            openInfoWin(locaMap.getMap(), ev.originalEvent, {
                '名称': rawData.name,
                '位置': rawData.address,
                '电话': rawData.tel
            });
        });*/

        // 百度地图API功能
        var map = new BMap.Map("my-map2");
        map.centerAndZoom(new BMap.Point(116.4035,39.915),8);
        /*setTimeout(function(){
            map.setZoom(14);
        }, 2000);  //2秒后放大到14级*/
        map.enableScrollWheelZoom(true);

        // 覆盖区域图层测试
        map.addTileLayer(new BMap.PanoramaCoverageLayer());

        var stCtrl = new BMap.PanoramaControl(); //构造全景控件
        stCtrl.setOffset(new BMap.Size(20, 20));
        map.addControl(stCtrl);//添加全景控件

        // 添加带有定位的导航控件
        var navigationControl = new BMap.NavigationControl({
            // 靠左上角位置
            anchor: BMAP_ANCHOR_TOP_LEFT,
            // LARGE类型
            type: BMAP_NAVIGATION_CONTROL_LARGE,
            // 启用显示定位
            enableGeolocation: true
        });
        map.addControl(navigationControl);


        // 添加定位控件
        var geolocationControl = new BMap.GeolocationControl();
        geolocationControl.addEventListener("locationSuccess", function(e){
            // 定位成功事件
            var address = '';
            address += e.addressComponent.province;
            address += e.addressComponent.city;
            address += e.addressComponent.district;
            address += e.addressComponent.street;
            address += e.addressComponent.streetNumber;

        });
        geolocationControl.addEventListener("locationError",function(e){
            // 定位失败事件
            alert(e.message);
        });
        map.addControl(geolocationControl);


        var cr = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_TOP_RIGHT});   //设置版权控件位置
        map.addControl(cr); //添加版权控件

        var bs = map.getBounds();   //返回地图可视区域
        cr.addCopyright({id: 1, content: "Copyright © 2019.Company name All rights reserved.More Templates 智飨云 - Collect from 智飨云<a href='#'>智飨科技</a>", bounds: bs});
        //Copyright(id,content,bounds)类作为CopyrightControl.addCopyright()方法


        var geoc = new BMap.Geocoder();

        //单击获取点击的经纬度
        map.addEventListener("click",function(e){

            /*geoc.getLocation(e.point, function(rs){
                var addComp = rs.addressComponents;
                alert(rs.address);
                alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
            });*/
            //赋值经纬度
            $("#lng").val(e.point.lng);
            $("#lat").val(e.point.lat);

            map.clearOverlays();
            var iconUrl = imgPrefix+$("input[name='photo']").val();
            if(iconUrl!=undefined&&iconUrl!=null&&iconUrl!=""){
                //创建自己的图标
                var pt = new BMap.Point(e.point.lng, e.point.lat);
                //var myIcon = new BMap.Icon(iconUrl, new BMap.Size(120,80));
                var myIcon = new BMap.Icon(
                    iconUrl, // 上传的站点图标
                    new BMap.Size(80,90), // 视窗大小
                    {
                        imageSize: new BMap.Size(144,92), // 引用图片实际大小
                        imageOffset:new BMap.Size(-30,0)  // 图片相对视窗的偏移
                    }
                );
                var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
                map.addOverlay(marker2);              // 将标注添加到地图中
                marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            }else{
                //创建百度地图默认小狐狸图标
                var pt = new BMap.Point(e.point.lng, e.point.lat);
                var myIcon = new BMap.Icon("http://lbsyun.baidu.com/jsdemo/img/fox.gif", new BMap.Size(300,157));
                var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
                map.addOverlay(marker2);              // 将标注添加到地图中
                marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            }


        });

        var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
            {"input" : "suggestId"
                ,"location" : map
            });

        ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
            var str = "";
            var _value = e.fromitem.value;
            var value = "";
            if (e.fromitem.index > -1) {
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            }
            str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

            value = "";
            if (e.toitem.index > -1) {
                _value = e.toitem.value;
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            }
            str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
            G("searchResultPanel").innerHTML = str;
        });

        var myValue;
        ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
            var _value = e.item.value;
            myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

            setPlace(map,myValue);
        });


        $("#compose-modal").modal("show");
    }

}

function editPerm(id,type) {

    $("#type").val("");
    $("#id").val("");
    $("#pid").val("");
    $("#iconImg").val("");
    //赋值经纬度
    $("#lng").val("");
    $("#lat").val("");
    $("#iconImgDiv").remove();
    myResetForm();

    if(null!=id){
        $("#type").val(type);
        $("#id").val(id);
        $.post("/site/getSite",{"id":id},function(data) {
            // console.log(data);
            if(null!=data){
                $("input[name='name']").val(data.name);
                $("input[name='zindex']").val(data.zindex);
                $("input[name='photo']").val(data.photo);
                $("input[name='sdId']").val(data.sdId);
                $("textarea[name='address']").val(data.address);
                //赋值经纬度
                $("#lng").val(data.lng);
                $("#lat").val(data.lat);


                $("#pid").val(data.pid);

                $("#zd_img").after("<div id='iconImgDiv' class='form-group'><div style='width: 10em;margin:0 auto;'><img alt='站点图标' src='"+data.photo+"' class='online' style='width:100%'/></div></div>");
                //选中是功能  0是菜单，1是功能 即选中是1
                $("#chgUpdTitle").text("更新站点");


                // 百度地图API功能
                var map = new BMap.Map("my-map2");
                if(data.lng!=null&&data.lat!=null){
                    map.centerAndZoom(new BMap.Point(data.lng,data.lat),18);
                    /*setTimeout(function(){
                        map.setZoom(14);
                    }, 2000);  //2秒后放大到14级*/
                    setTimeout(function () {
                        map.setCenter(new BMap.Point(data.lng,data.lat));
                    } ,1000 * 0.5);
                }else{
                    map.centerAndZoom(new BMap.Point(120.578308,30.60934),18);
                    /*setTimeout(function(){
                        map.setZoom(14);
                    }, 2000);  //2秒后放大到14级*/
                    setTimeout(function () {
                        map.setCenter(new BMap.Point(120.578308,30.60934));
                    } ,1000 * 0.5);
                }

                map.enableScrollWheelZoom(true);

                map.clearOverlays();

                if(data.lng!=null&&data.lat!=null){
                    var iconUrl = imgPrefix+$("input[name='photo']").val();
                    if(iconUrl!=undefined&&iconUrl!=null&&iconUrl!=""){
                        //创建自己的图标
                        var pp = new BMap.Point(data.lng, data.lat);
                        //var myIcon = new BMap.Icon(iconUrl, new BMap.Size(120,80));
                        var myIcon = new BMap.Icon(
                            iconUrl, // 上传的站点图标
                            new BMap.Size(80,90), // 视窗大小
                            {
                                imageSize: new BMap.Size(144,92), // 引用图片实际大小
                                imageOffset:new BMap.Size(-30,0)  // 图片相对视窗的偏移
                            }
                        );
                        var marker2 = new BMap.Marker(pp,{icon:myIcon});  // 创建标注
                        map.addOverlay(marker2);              // 将标注添加到地图中
                        marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                    }else{
                        //创建百度地图默认小狐狸图标
                        var pp = new BMap.Point(data.lng, data.lat);
                        var myIcon = new BMap.Icon("http://lbsyun.baidu.com/jsdemo/img/fox.gif", new BMap.Size(300,157));
                        var marker2 = new BMap.Marker(pp,{icon:myIcon});  // 创建标注
                        map.addOverlay(marker2);              // 将标注添加到地图中
                        marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                    }
                }

                // 覆盖区域图层测试
                map.addTileLayer(new BMap.PanoramaCoverageLayer());

                var stCtrl = new BMap.PanoramaControl(); //构造全景控件
                stCtrl.setOffset(new BMap.Size(20, 20));
                map.addControl(stCtrl);//添加全景控件

                // 添加带有定位的导航控件
                var navigationControl = new BMap.NavigationControl({
                    // 靠左上角位置
                    anchor: BMAP_ANCHOR_TOP_LEFT,
                    // LARGE类型
                    type: BMAP_NAVIGATION_CONTROL_LARGE,
                    // 启用显示定位
                    enableGeolocation: true
                });
                map.addControl(navigationControl);


                // 添加定位控件
                var geolocationControl = new BMap.GeolocationControl();
                geolocationControl.addEventListener("locationSuccess", function(e){
                    // 定位成功事件
                    var address = '';
                    address += e.addressComponent.province;
                    address += e.addressComponent.city;
                    address += e.addressComponent.district;
                    address += e.addressComponent.street;
                    address += e.addressComponent.streetNumber;

                });
                geolocationControl.addEventListener("locationError",function(e){
                    // 定位失败事件
                    alert(e.message);
                });
                map.addControl(geolocationControl);


                var cr = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_TOP_RIGHT});   //设置版权控件位置
                map.addControl(cr); //添加版权控件

                var bs = map.getBounds();   //返回地图可视区域
                cr.addCopyright({id: 1, content: "Copyright © 2019.Company name All rights reserved.More Templates 智飨云 - Collect from 智飨云<a href='#'>智飨科技</a>", bounds: bs});
                //Copyright(id,content,bounds)类作为CopyrightControl.addCopyright()方法


                var geoc = new BMap.Geocoder();

                //单击获取点击的经纬度
                map.addEventListener("click",function(e){

                    /*geoc.getLocation(e.point, function(rs){
                        var addComp = rs.addressComponents;
                        alert(rs.address);
                        alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
                    });*/
                    //赋值经纬度
                    $("#lng").val(e.point.lng);
                    $("#lat").val(e.point.lat);

                    map.clearOverlays();
                    var iconUrl = imgPrefix+$("input[name='photo']").val();
                    if(iconUrl!=undefined&&iconUrl!=null&&iconUrl!=""){
                        //创建自己的图标
                        var pt = new BMap.Point(e.point.lng, e.point.lat);
                        //var myIcon = new BMap.Icon(iconUrl, new BMap.Size(120,80));
                        var myIcon = new BMap.Icon(
                            iconUrl, // 上传的站点图标
                            new BMap.Size(80,90), // 视窗大小
                            {
                                imageSize: new BMap.Size(144,92), // 引用图片实际大小
                                imageOffset:new BMap.Size(-30,0)  // 图片相对视窗的偏移
                            }
                        );
                        var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
                        map.addOverlay(marker2);              // 将标注添加到地图中
                        marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                    }else{
                        //创建百度地图默认小狐狸图标
                        var pt = new BMap.Point(e.point.lng, e.point.lat);
                        var myIcon = new BMap.Icon("http://lbsyun.baidu.com/jsdemo/img/fox.gif", new BMap.Size(300,157));
                        var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
                        map.addOverlay(marker2);              // 将标注添加到地图中
                        marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                    }


                });

                var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
                    {"input" : "suggestId"
                        ,"location" : map
                    });

                ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
                    var str = "";
                    var _value = e.fromitem.value;
                    var value = "";
                    if (e.fromitem.index > -1) {
                        value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                    }
                    str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

                    value = "";
                    if (e.toitem.index > -1) {
                        _value = e.toitem.value;
                        value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                    }
                    str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
                    G("searchResultPanel").innerHTML = str;
                });

                var myValue;
                ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
                    var _value = e.item.value;
                    myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                    G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

                    setPlace(map,myValue);
                });



                $("#compose-modal").modal("show");

            }else{
                $.confirm({
                    icon: '#icon-shangxin1',
                    theme: 'modern',
                    title: '提示',
                    content: '获取权限数据出错，请您稍后再试',
                    animation: 'news',//动画
                    closeAnimation: 'news',//关闭动画
                    autoClose: 'showMyMsg4|3000',
                    buttons: {
                        showMyMsg4: {
                            text: '关闭',
                            action: function () {


                            }
                        }/*,
                                                cancel: function () {
                                                    text: '关闭'
                                                }*/
                    }
                });
            }
        });
    }

}

function delPerm(id,name){

    if(null!=id){

        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'站点吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {

                        $.post("/site/del",{"id":id},function(data){
                            if(!isKickOut(data.code)){
                                //没有被踢出
                                if(data=="ok"){

                                    //刷新数据
                                    loadUserList(1,limCount);

                                    $.confirm({
                                        icon: '#icon-jinlingyingcaitubiao14',
                                        theme: 'modern',
                                        title: '提示',
                                        content: '删除站点成功！',
                                        animation: 'news',//动画
                                        closeAnimation: 'news',//关闭动画
                                        autoClose: 'showMyMsg3|3000',
                                        buttons: {
                                            showMyMsg3: {
                                                text: '关闭',
                                                action: function () {

                                                }
                                            }/*,
                                                cancel: function () {

                                                }*/
                                        }
                                    });

                                }else if(data=="删除失败，请您先删除该站点的子站点"){

                                    $.confirm({
                                        icon: '#icon-tishi1',
                                        theme: 'modern',
                                        title: '提示',
                                        content: data,
                                        animation: 'news',//动画
                                        closeAnimation: 'news',//关闭动画
                                        autoClose: 'showMyMsg4|3000',
                                        buttons: {
                                            showMyMsg4: {
                                                text: '关闭',
                                                action: function () {


                                                }
                                            }/*,
                                                cancel: function () {
                                                    text: '关闭'
                                                }*/
                                        }
                                    });

                                }else{
                                    $.confirm({
                                        icon: '#icon-shangxin1',
                                        theme: 'modern',
                                        title: '提示',
                                        content: '删除站点失败！',
                                        animation: 'news',//动画
                                        closeAnimation: 'news',//关闭动画
                                        autoClose: 'showMyMsg4|3000',
                                        buttons: {
                                            showMyMsg4: {
                                                text: '关闭',
                                                action: function () {


                                                }
                                            }/*,
                                                cancel: function () {
                                                    text: '关闭'
                                                }*/
                                        }
                                    });
                                }
                            }
                        });

                    }
                },
                cancel: {
                    text: "取消"
                }

            }
        });

    }

}

//根据菜单主键id获取下级菜单
//id：菜单主键id
//arry：菜单数组信息
function getParentArry(id, arry) {

    var newArry = new Array();

    for (var x in arry) {

        if (arry[x].pid == id){
            newArry.push(arry[x]);
        }

    }

    return newArry;
}


function myResetForm(){

    $('#permForm').resetForm();
    $('#permForm').clearForm();

}

function uploadIcon() {
    var form = new FormData();
    form.append("photo", document.getElementById("file").files[0]);
    $.ajax({
        url: "/file/fileup",//后台url
        data: form,
        cache: false,
        async: false,
        type: "POST", //类型，POST或者GET
        dataType: 'json', //数据返回类型，可以是xml、json等
        processData: false,
        contentType: false,
        success: function (data) {
            //成功，回调函数
            if(data.code==1000){
                $("#iconImg").val(data.data);
                $("#iconImgDiv").remove();
                $("#zd_img").after("<div id='iconImgDiv' class='form-group'><div style='width: 10em;margin:0 auto;'><img alt='站点图标' src='"+data.obj+data.data+"' class='online' style='width:100%'/></div></div>");
            }else{
                $.confirm({
                    icon: '#icon-shangxin1',
                    theme: 'modern',
                    title: '提示',
                    content: '上传站点图标失败！',
                    animation: 'news',//动画
                    closeAnimation: 'news',//关闭动画
                    autoClose: 'showMyMsg4|3000',
                    buttons: {
                        showMyMsg4: {
                            text: '关闭',
                            action: function () {


                            }
                        }
                    }
                });
            }

        }, error: function (er) {
           // 失败，回调函数
            $.confirm({
                icon: '#icon-shangxin1',
                theme: 'modern',
                title: '提示',
                content: '上传站点图标失败！',
                animation: 'news',//动画
                closeAnimation: 'news',//关闭动画
                autoClose: 'showMyMsg4|3000',
                buttons: {
                    showMyMsg4: {
                        text: '关闭',
                        action: function () {


                        }
                    }
                }
            });
        }
    });


}

/* 地图形式显示站点 */
function showMap(){
    // 百度地图API功能
    var map = new BMap.Map("my-map2");
    map.centerAndZoom(new BMap.Point(116.4035,39.915),8);
    setTimeout(function(){
        map.setZoom(14);
    }, 2000);  //2秒后放大到14级
    map.enableScrollWheelZoom(true);

    $("#compose-modal").modal("show");
}


// 百度地图API功能
function G(id) {
    return document.getElementById(id);
}

function setPlace(map,myValue){
    map.clearOverlays();    //清除地图上所有覆盖物
    function myFun(){
        var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
        map.centerAndZoom(pp, 18);
        //map.addOverlay(new BMap.Marker(pp));    //添加标注

        //赋值经纬度
        $("#lng").val(pp.lng);
        $("#lat").val(pp.lat);

        map.clearOverlays();
        var iconUrl = imgPrefix+$("input[name='photo']").val();
        if(iconUrl!=undefined&&iconUrl!=null&&iconUrl!=""){
            //创建自己的图标
            /*var pt = new BMap.Point(e.point.lng, e.point.lat);*/
            //var myIcon = new BMap.Icon(iconUrl, new BMap.Size(120,80));
            var myIcon = new BMap.Icon(
                iconUrl, // 上传的站点图标
                new BMap.Size(80,90), // 视窗大小
                {
                    imageSize: new BMap.Size(144,92), // 引用图片实际大小
                    imageOffset:new BMap.Size(-30,0)  // 图片相对视窗的偏移
                }
            );
            var marker2 = new BMap.Marker(pp,{icon:myIcon});  // 创建标注
            map.addOverlay(marker2);              // 将标注添加到地图中
            marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
        }else{
            //创建百度地图默认小狐狸图标
            /*var pt = new BMap.Point(e.point.lng, e.point.lat);*/
            var myIcon = new BMap.Icon("http://lbsyun.baidu.com/jsdemo/img/fox.gif", new BMap.Size(300,157));
            var marker2 = new BMap.Marker(pp,{icon:myIcon});  // 创建标注
            map.addOverlay(marker2);              // 将标注添加到地图中
            marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
        }

    }
    var local = new BMap.LocalSearch(map, { //智能搜索
        onSearchComplete: myFun
    });
    local.search(myValue);
}