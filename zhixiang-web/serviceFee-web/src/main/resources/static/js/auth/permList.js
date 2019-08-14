var tes = 0;
var isShow = true;

var limCount = 15;

var maxLen = 0;

$(function(){
    //modelandview 返回的参数只能在html用[[${}}]]格式获取
    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    var leftMenuStorage = localStorage.getItem("leftMenuStorage");

    $("#permUl").html(leftMenuStorage);
    //添加样式
    addActClass($("#liParent1").children(":eq(0)"));
    $("#liParent1").children(":eq(1)").children(":eq(0)").find("a").css("color","#FFD600");

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
        url: "/auth/setPerm", //默认是form的action， 如果申明，则会覆盖
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

    $("#perType").append("<input class='tgl tgl-flip' id='cb9' type='checkbox'><label class='tgl-btn' data-tg-off='菜单' data-tg-on='功能' for='cb9' style='margin:0 auto;' onclick='changePerType()'></label>");

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

    $.post("/auth/getPermList",function(data){

        if(data.code==1000){

            $("#userListUl").html("");

            $.each(data.data,function(ind,val){

                if( val.istype==0) {

                    if (val.pid == 0) {

                        $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                            "<td data-label='权限名称' style='text-align: left'>" +
                            "<span class='handle'>"+val.name+"</span>" +
                            "</td>" +
                            "<td data-label='权限分类'>" +
                            "<small id='"+val.id+"-nmspan' class='label label-danger' style='font-size: 90%'></small>" +
                            "</td>" +
                            "<td data-label='权限路径'>" +
                            "<small id='"+val.id+"-unspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.page+"</small>" +
                            "</td>" +
                            "<td data-label='权限CODE'>" +
                            "<small id='"+val.id+"-atspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-bookmark-o'></i>&nbsp;"+val.code+"</small>" +
                            "</td>" +
                            "<td data-label='优先级'>" +
                            "<small id='"+val.id+"-atspan2' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.zindex+"</small>" +
                            "</td>" +
                            "<td data-label='操作'>" +
                            "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                            "<i class='fa fa-plus' data-toggle='tooltip' title='' data-original-title='添加子节点' style='cursor: pointer' onclick='addSun("+val.id+",1"+")'></i>&nbsp;" +
                            "<i class='fa fa-edit' data-toggle='tooltip' title='' data-original-title='编辑' style='cursor: pointer' onclick='editPerm("+val.id+",0"+")'></i>&nbsp;" +
                            "<i class='fa fa-trash-o' data-toggle='tooltip' title='' data-original-title='删除' style='cursor: pointer' onclick='delPerm("+val.id+","+"\""+val.name+"\")'></i>" +
                            "</div>" +
                            "</td>" +
                            "</tr>");

                        if(val.istype==0){
                            //菜单
                            $("#"+val.id+"-nmspan").html("<i class='fa fa-bars'></i>&nbsp;菜单");
                        }else if(val.istype==1){
                            //功能
                            $("#"+val.id+"-nmspan").removeClass("label-danger").addClass("label-success");
                            $("#"+val.id+"-nmspan").html("<i class='fa fa-gears'></i>&nbsp;功能");
                        }

                        var sonArray = getParentArry(val.id,data.data);

                        if(sonArray.length>0){

                            //给这个tr加一个图标
                            $("#"+val.id+"-myStyleLi").find("td:eq(0) span").before("<i class='fa fa-caret-down' data-widget='collapse' style='cursor: pointer' onclick='showOrHideSun("+val.id+")'></i>&nbsp;");
                            $.each(sonArray,function (ind2,val2) {
                                $("#"+val.id+"-myStyleLi").after("<tr id='"+val2.id+"-myStyleLi' class='box-body"+val.id+"' onmouseover='showED("+val2.id+")' onmouseleave='hideED("+val2.id+")'>" +
                                    "<td data-label='权限名称' style='text-align: left'>" +
                                    "<span class='handle' style='padding-left: 15%;'>"+val2.name+"</span>" +
                                    "</td>" +
                                    "<td data-label='权限分类'>" +
                                    "<small id='"+val2.id+"-nmspan' class='label label-danger' style='font-size: 90%'></small>" +
                                    "</td>" +
                                    "<td data-label='权限路径'>" +
                                    "<small id='"+val2.id+"-unspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val2.page+"</small>" +
                                    "</td>" +
                                    "<td data-label='权限CODE'>" +
                                    "<small id='"+val2.id+"-atspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-bookmark-o'></i>&nbsp;"+val2.code+"</small>" +
                                    "</td>" +
                                    "<td data-label='优先级'>" +
                                    "<small id='"+val2.id+"-atspan2' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val2.zindex+"</small>" +
                                    "</td>" +
                                    "<td data-label='操作'>" +
                                    "<div class='tools' data-label='"+val2.id+"' style='color: #f56954;display: none'>" +
                                    "<i class='fa fa-plus' data-toggle='tooltip' title='' data-original-title='添加子节点' style='cursor: pointer' onclick='addSun("+val2.id+",1"+")'></i>&nbsp;" +
                                    "<i class='fa fa-edit' data-toggle='tooltip' title='' data-original-title='编辑' style='cursor: pointer' onclick='editPerm("+val2.id+",0"+")'></i>&nbsp;" +
                                    "<i class='fa fa-trash-o' data-toggle='tooltip' title='' data-original-title='删除' style='cursor: pointer' onclick='delPerm("+val2.id+","+"\""+val2.name+"\")'></i>" +
                                    "</div>" +
                                    "</td>" +
                                    "</tr>");


                                if(val2.istype==0){
                                    //菜单
                                    $("#"+val2.id+"-nmspan").html("<i class='fa fa-bars'></i>&nbsp;菜单");
                                }else if(val2.istype==1){
                                    //功能
                                    $("#"+val2.id+"-nmspan").removeClass("label-danger").addClass("label-success");
                                    $("#"+val2.id+"-nmspan").html("<i class='fa fa-gears'></i>&nbsp;功能");
                                }

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
                                    "<td data-label='权限分类'>" +
                                    "<small id='"+val2.id+"-nmspan' class='label label-danger' style='font-size: 90%'></small>" +
                                    "</td>" +
                                    "<td data-label='权限路径'>" +
                                    "<small id='"+val2.id+"-unspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val2.page+"</small>" +
                                    "</td>" +
                                    "<td data-label='权限CODE'>" +
                                    "<small id='"+val2.id+"-atspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-bookmark-o'></i>&nbsp;"+val2.code+"</small>" +
                                    "</td>" +
                                    "<td data-label='优先级'>" +
                                    "<small id='"+val2.id+"-atspan2' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val2.zindex+"</small>" +
                                    "</td>" +
                                    "<td data-label='操作'>" +
                                    "<div class='tools' data-label='"+val2.id+"' style='color: #f56954;display: none'>" +
                                    "<i class='fa fa-plus' data-toggle='tooltip' title='' data-original-title='添加子节点' style='cursor: pointer' onclick='addSun("+val2.id+",1"+")'></i>&nbsp;" +
                                    "<i class='fa fa-edit' data-toggle='tooltip' title='' data-original-title='编辑' style='cursor: pointer' onclick='editPerm("+val2.id+",0"+")'></i>&nbsp;" +
                                    "<i class='fa fa-trash-o' data-toggle='tooltip' title='' data-original-title='删除' style='cursor: pointer' onclick='delPerm("+val2.id+","+"\""+val2.name+"\")'></i>" +
                                    "</div>" +
                                    "</td>" +
                                    "</tr>");

                                if(val2.istype==0){
                                    //菜单
                                    $("#"+val2.id+"-nmspan").html("<i class='fa fa-bars'></i>&nbsp;菜单");
                                }else if(val2.istype==1){
                                    //功能
                                    $("#"+val2.id+"-nmspan").removeClass("label-danger").addClass("label-success");
                                    $("#"+val2.id+"-nmspan").html("<i class='fa fa-gears'></i>&nbsp;功能");
                                }

                            });
                        }

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

    myResetForm();

    if(null!=pid){
        //flag[0:开通权限；1：新增子节点权限]
        //type[0:编辑；1：新增]
        var msg = '';
        if(flag==0){
            $("#type").val(1);
            $("#pid").val(0);
            msg = '新增权限';
        }else{
            //设置父id
            $("#type").val(1);
            $("#pid").val(pid);
            msg = "新增子权限";
        }

        //赋值类型
        if($("#cb9").prop('checked')){
            $("#istype").val(1);
        }else{
            $("#istype").val(0);
        }

        $("#chgUpdTitle").text(msg);

        $("#compose-modal").modal("show");
    }

}

function editPerm(id,type) {

    $("#type").val("");
    $("#id").val("");
    $("#pid").val("");

    myResetForm();

    if(null!=id){
        $("#type").val(type);
        $("#id").val(id);
        $.post("/auth/getPerm",{"id":id},function(data) {
            // console.log(data);
            if(null!=data){
                $("input[name='name']").val(data.name);
                $("input[name='code']").val(data.code);
                $("input[name='page']").val(data.page);
                $("input[name='zindex']").val(data.zindex);
                $("textarea[name='descpt']").val(data.descpt);

                $("#pid").val(data.pid);

                data.istype==0?$("#cb9").prop('checked',false):$("#cb9").prop('checked',true);
                //选中是功能  0是菜单，1是功能 即选中是1
                $("#chgUpdTitle").text("更新权限");

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
            content: '您确定要删除'+name+'权限吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {

                        $.post("/auth/del",{"id":id},function(data){
                            if(!isKickOut(data.code)){
                                //没有被踢出
                                if(data=="ok"){

                                    //刷新数据
                                    loadUserList(1,limCount);

                                    $.confirm({
                                        icon: '#icon-jinlingyingcaitubiao14',
                                        theme: 'modern',
                                        title: '提示',
                                        content: '删除角色成功！',
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

                                }else if(data=="删除失败，请您先删除该权限的子节点"){

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
                                        content: '删除角色失败！',
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



function changePerType(){
    if($("#cb9").prop('checked')){
        $("#istype").val(0);
    }else{
        $("#istype").val(1);
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