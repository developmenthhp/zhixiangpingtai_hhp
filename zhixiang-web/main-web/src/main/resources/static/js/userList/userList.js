var tes = 0;
var isShow = true;
var allPage;

var limCount = 15;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var rolesArray = [];

var siteRolesArray = [];

var messagesJson;

var sdId;

var isZx;

var ajaxSCJCXXSearchFormOption = {
    //target: '#output', //把服务器返回的内容放入id为output的元素中
    beforeSubmit: showSCJCXXSearchRequest, //提交前的回调函数
    success: showSCJCXXSearchResponse, //提交后的回调函数
    url: "/user/getUsers", //默认是form的action， 如果申明，则会覆盖
    type: "post", //默认是form的method（get or post），如果申明，则会覆盖
    data:{page:curPag,limit:limCount},
    dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
    //clearForm: true,   //成功提交后，清除所有表单元素的值
    //resetForm: true,  //成功提交后，重置所有表单元素的值
    timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
};

$(function(){

    sdId = $("#currentUserSdId").text();
    isZx = $("#currentUserIsZX").text();

    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    var leftMenuStorage = localStorage.getItem("leftMenuStorage");

    $("#permUl").html(leftMenuStorage);
    addActClass($("#liParent1").children(":eq(0)"));
    $("#liParent1").children(":eq(1)").children(":eq(2)").find("a").css("color","#FFD600");

    loadUserList();

    $("#topShowLp").css("display","none");

    var fixHelperModified = function(e, tr) {
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
    }).disableSelection();

    //不需要submit按钮，可以是任何元素的click事件
    $("#searchFormI").click(function () {
        $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
        return false;
    });

    //弹幕
    function BarrageManager (options) {

        this.opts = {
            url       : '/message/getMessages',
            loadDelay : 10000 ,  // 轮询时间间隔
        }

        $.extend( this.opts , options);
        this.bc = new BarrageCollection();
    }

    BarrageManager.prototype.load = function () {
        var self = this ;

        if(messagesJson==undefined||messagesJson==''||messagesJson=='undefined'){
            $.post(self.opts.url, {sdId:12}, function(data){

                if(data.code==1000){
                    if(data.data.length>0){

                        messagesJson = JSON.stringify(data);

                        $.each(data.data,function(ind,val){
                            self.bc.add(new Barrage({
                                id:val.id,
                                name:val.fromUserName,
                                text:val.content,
                                icon:val.fromUserIcon ? val.fromUserIcon : '../images/logo1.png'
                            }));
                        });
                        self.loop();
                    }
                }
            });
        }else{

            var jsonDataMessage = JSON.parse(messagesJson);

            $.each(jsonDataMessage.data,function(ind,val){
                self.bc.add(new Barrage({
                    id:val.id,
                    name:val.fromUserName,
                    text:val.content,
                    icon:val.fromUserIcon ? val.fromUserIcon : '../images/logo1.png'
                }));
            });
            self.loop();

        }


        /*$.getJSON(self.opts.url , function (data) {
            if(data.data.length > 0) {
                for(var i = 0 ; i < data.data.length ; i++) {
                    var item = data.data[i] ;
                    self.bc.add(new Barrage({
                        id:item.id,
                        name:item.fromUserName,
                        text:item.content,
                        icon:item.fromUserIcon ? item.fromUserIcon : '../images/logo1.png'
                    }));
                }
                self.loop();
            }
        });*/
    }

    BarrageManager.prototype.loop = function () {
        var len = this.bc.mq.length , self = this  ;
        while (len--) {
            this.bc.mq[len].start(this.bc , len);
        }

        setTimeout(function () {
            self.load();
        } , this.opts.loadDelay);

    }

    function BarrageCollection () {
        this.mq = [] ;
    }

    BarrageCollection.prototype.add = function (barrage) {
        this.mq.push(barrage);
    }

    BarrageCollection.prototype.remove = function (barrage) {
        var index = this.mq.findIndex(function (item) {
            return barrage.opts.id == item.opts.id ;
        });
        if(index != -1) {
            this.mq.splice(index , 1);
        }
        barrage.opts.$el.remove();
    }

    function Barrage (options) {
        this.opts = {
            $el         : null ,
            left        : 0 ,
            bgColor     : [Math.floor(Math.random()*255),Math.floor(Math.random()*255),Math.floor(Math.random()*255)] ,
            offset      : 50 ,      // 使弹幕完全移出屏幕外
            duration    : 10000 ,   // 弹幕从右往左移动的时间
            delayTime   : 1000 ,    // 弹幕延迟移动时间
        };
        $.extend( this.opts , options);
        this.init();
    }

    Barrage.prototype.init = function () {

        this.opts.$el = $("<span><img src="+this.opts.icon+"><em>"+this.opts.name+":</em>"+this.opts.text+"</span>");

        var top = Math.ceil(Math.random() * 10 );
        this.opts.$el.css({
            top:top * 40 +'px',
            backgroundColor:"rgb("+this.opts.bgColor.join(",")+")"
        });

        var delay = Math.ceil(Math.random()*10);
        this.opts.delayTime *= Math.abs(delay - 5);

        var dur = Math.ceil(Math.random() * 10);
        this.opts.duration += dur * 1000 ;

        $('#barrage-wrapper').append(this.opts.$el);
        this.opts.left = -this.opts.$el.width() - this.opts.offset ;
        //改版
        //this.opts.left = -this.parent().width() - this.opts.offset ;
    }

    Barrage.prototype.start = function (bc , index) {
        var self = this ;
        bc.mq.splice(index , 1);
        setTimeout(function () {
            self.move(bc);
        }, this.opts.delayTime);
    }

    Barrage.prototype.move = function (bc) {
        var self = this ;
        this.opts.$el.animate({
            left:this.opts.left+'px'
        } , this.opts.duration ,"linear" ,  function () {
            bc.remove(self);
        });
    }

    new BarrageManager().load();
    //弹幕完

    var ajaxFormOption = {
        //target: '#output', //把服务器返回的内容放入id为output的元素中
        beforeSubmit: showRequest, //提交前的回调函数
        success: showResponse, //提交后的回调函数
        url: "/user/setUser", //默认是form的action， 如果申明，则会覆盖
        type: "post", //默认是form的method（get or post），如果申明，则会覆盖
        //data:
        dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
        //clearForm: true,   //成功提交后，清除所有表单元素的值
        //resetForm: true,  //成功提交后，重置所有表单元素的值
        timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
    }


    //不需要submit按钮，可以是任何元素的click事件
    $("#myFormButton").click(function () {

        $("#roleIds").val(rolesArray.join(","));

        $("#siteRoleIds").val(siteRolesArray.join(","));


        $("#userForm").ajaxSubmit(ajaxFormOption);
        return false;
    });

    /*$("#userForm").submit(function(){
        alert("sdlfjasldfjasdf");
        $(this).ajaxSubmit(ajaxFormOption);
        return false;   //阻止表单默认提交
    });*/

    //监听窗口，显示隐藏move top
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
});

function showSCJCXXSearchRequest(formData, jqForm, options){
    return true; //只要不返回false，表单都会提交,在这里可以对表单元素进行验证
};
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}

/*
     * 定义分页回掉函数
     * @param  number:跳转页
     * */
function pageChange(i) {
    curPag = accAdd(i,1);
    loadUserList();
    Pagination.Page($(".ht-page"), i, allDataTol, limCount);
}

/**
 * 加载数据
 * */
function loadUserList(){
    $("input[name='foodNameDto']").val($("#foodNameDtoShow").val());
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}

function reloadMyDateTable() {
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}

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
}
function showResponse(data, status){

    //responseText 返回的数据
    //statusText 返回标识

    if(data.code==1000){
        loadUserList(1,limCount);
        myResetForm();
        //关闭模态框
        $("#compose-modal").modal("hide");
        showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
    }else if(data.code==1001){
        //系统接口错误
        showJqueryConfirmWindow("#icon-shangxin1",data.msg);
    }else{
        showJqueryConfirmWindow("#icon-tishi1",data.msg);
    }

}


function initDateTableData(data){

    if(!isKickOut(data.code)){
        //没有被踢出
        if(data.rows!=null){
            $("#userListUl").html("");
            $.each(data.rows,function (ind,val) {
                //小品40% checkbook myStyleLi
                $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                    /*"<td data-label='移动' class='comMove'>" +
                    "<span class='handle'><i class='fa fa-ellipsis-v'></i><i class='fa fa-ellipsis-v'></i></span>" +
                "</td>" +*/
                    "<td data-label='是否在职' class='comMove'>" +
                    "<input class='tgl tgl-flip' id='"+val.id+"' type='checkbox'>" +
                    "<label id='"+val.id+"-lab' class='ullabStyle tgl-btn' data-tg-off='离职' data-tg-on='在职' for='"+val.id+"' onclick='changeJobStatus("+val.job+","+val.id+","+val.version+","+val.del+")'></label>" +
                    "</td>" +
                    "<td data-label='用户名' class='comMove'>" +
                    "<span id='"+val.id+"-unspan' class='text'>"+val.username+"</span>" +
                    "</td>" +
                    "<td data-label='手机号' class='comMove'>" +
                    /*"<span id='"+val.id+"-mbspan' class='text'>"+val.mobile+"</span>" +*/
                    "<small id='"+val.id+"-mbspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-mobile-phone'></i>&nbsp;"+val.mobile+"</small>" +
                    "</td>" +
                    "<td data-label='邮箱' class='comMove'>" +
                    /*"<span id='"+val.id+"-emspan' class='text'>"+val.email+"</span>" +*/
                    "<small id='"+val.id+"-emspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-envelope-o'></i>&nbsp;"+val.email+"</small>" +
                    "</td>" +
                    "<td data-label='角色名称' class='comMove'>" +
                    "<small id='"+val.id+"-rlspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-user'></i>&nbsp;"+val.roleNames+"</small>" +
                    "</td>" +
                    "<td data-label='添加时间' class='comMove'>" +
                    "<small id='"+val.id+"-atspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-clock-o'></i>&nbsp;"+val.insertTime+"</small>" +
                    "</td>" +
                    "<td data-label='操作'>" +
                    "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                    "<i class='fa fa-edit' style='cursor: pointer' onclick='getUserAndRoles("+JSON.stringify(val)+","+val.id+")'></i>&nbsp;" +
                    "<i class='fa fa-trash-o' style='cursor: pointer' onclick='delUser("+val.version+","+val.id+","+"\""+val.username+"\")'></i>" +
                    "</div>" +
                    "</td>" +
                    "</tr>");

                if(!val.job){

                    isShow = false;

                    //如果在职，显示在职
                    //反转

                    /*$("#"+val.id+"-lab").trigger("click");*/

                    //改为添加或移除class  checkbox 点击前先将class和check去除
                    $("#"+val.id).prop("checked",true);
                    $("#"+val.id+"-lab").addClass("cll1");

                    $("#"+val.id+"-unspan").removeClass("doneText");
                    $("#"+val.id+"-mbspan").removeClass("doneSmal");
                    $("#"+val.id+"-emspan").removeClass("doneSmal");
                    $("#"+val.id+"-rlspan").removeClass("doneSmal");
                    $("#"+val.id+"-atspan").removeClass("doneSmal");
                }else{
                    $("#"+val.id+"-unspan").addClass("doneText");
                    $("#"+val.id+"-mbspan").addClass("doneSmal");
                    $("#"+val.id+"-emspan").addClass("doneSmal");
                    $("#"+val.id+"-rlspan").addClass("doneSmal");
                    $("#"+val.id+"-atspan").addClass("doneSmal");
                }
            });

            allDataTol = data.total;

            if(initPageplugins){
                /*
                 * 初始化插件
                 * @param  object:翻页容器对象
                 * @param  function:回调函数
                 * */
                Pagination.init($(".ht-page"), pageChange);

                /*
                * 首次调用
                * @param  object:翻页容器对象
                * @param  number:当前页
                * @param  number:总页数
                * @param  number:每页数据条数
                * */
                Pagination.Page($(".ht-page"), 0, allDataTol, limCount);
                initPageplugins = false;
            }else{
                Pagination.Page($(".ht-page"), curPag-1, allDataTol, limCount);
            }


            /*$("#userListUl").viewer('update');
            $('#userListUl').viewer({
                url: 'data-original',
            });*/


        }else{
            //加一个字体图标无数据
        }

    }
}

/**
 * 加载数据
 * */
/*function loadUserList(curPag,lim){

    $.post("/user/getUsers",{page:curPag,limit:limCount},function(data){

        //$("#userList").nextAll().remove(); //该方法会删除当前节点下的所有子节点，请注意当前节点会 被删除

        if(data!=null){

            $("#userListUl").html("");

            $.each(data.list,function(ind,val){

                //小品40% checkbook myStyleLi
                $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                    /!*"<td data-label='移动' class='comMove'>" +
                    "<span class='handle'><i class='fa fa-ellipsis-v'></i><i class='fa fa-ellipsis-v'></i></span>" +
                "</td>" +*!/
                "<td data-label='是否在职' class='comMove'>" +
                "<input class='tgl tgl-flip' id='"+val.id+"' type='checkbox'>" +
                "<label id='"+val.id+"-lab' class='ullabStyle tgl-btn' data-tg-off='离职' data-tg-on='在职' for='"+val.id+"' onclick='changeJobStatus("+val.job+","+val.id+","+val.version+","+val.del+")'></label>" +
                "</td>" +
                "<td data-label='用户名' class='comMove'>" +
                "<span id='"+val.id+"-unspan' class='text'>"+val.username+"</span>" +
                "</td>" +
                "<td data-label='手机号' class='comMove'>" +
                /!*"<span id='"+val.id+"-mbspan' class='text'>"+val.mobile+"</span>" +*!/
                "<small id='"+val.id+"-mbspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-mobile-phone'></i>&nbsp;"+val.mobile+"</small>" +
                "</td>" +
                "<td data-label='邮箱' class='comMove'>" +
                /!*"<span id='"+val.id+"-emspan' class='text'>"+val.email+"</span>" +*!/
                "<small id='"+val.id+"-emspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-envelope-o'></i>&nbsp;"+val.email+"</small>" +
                "</td>" +
                "<td data-label='角色名称' class='comMove'>" +
                "<small id='"+val.id+"-rlspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-user'></i>&nbsp;"+val.roleNames+"</small>" +
                "</td>" +
                "<td data-label='添加时间' class='comMove'>" +
                "<small id='"+val.id+"-atspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-clock-o'></i>&nbsp;"+val.insertTime+"</small>" +
                "</td>" +
                "<td data-label='操作'>" +
                "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                "<i class='fa fa-edit' style='cursor: pointer' onclick='getUserAndRoles("+JSON.stringify(val)+","+val.id+")'></i>&nbsp;" +
                "<i class='fa fa-trash-o' style='cursor: pointer' onclick='delUser("+val.version+","+val.id+","+"\""+val.username+"\")'></i>" +
                "</div>" +
                "</td>" +
                "</tr>");

                if(!val.job){

                    isShow = false;

                    //如果在职，显示在职
                    //反转

                    /!*$("#"+val.id+"-lab").trigger("click");*!/

                    //改为添加或移除class  checkbox 点击前先将class和check去除
                   $("#"+val.id).prop("checked",true);
                    $("#"+val.id+"-lab").addClass("cll1");

                    $("#"+val.id+"-unspan").removeClass("doneText");
                    $("#"+val.id+"-mbspan").removeClass("doneSmal");
                    $("#"+val.id+"-emspan").removeClass("doneSmal");
                    $("#"+val.id+"-rlspan").removeClass("doneSmal");
                    $("#"+val.id+"-atspan").removeClass("doneSmal");

                    /!**
                     * -webkit-transform: rotateY(-180deg);
                     -ms-transform: rotateY(-180deg);
                     transform: rotateY(-180deg);

                     -webkit-transition: all .4s ease;
                     transition: all .4s ease;
                     * *!/
                }else{

                    /!*var tt = "#"+val.id;

                    $(tt).prop("checked",false);*!/


                    //如果在职，显示在职
                    //反转

                    /!**
                     * /!* -webkit-transform: rotateY(0);
                     -ms-transform: rotateY(0);
                     transform: rotateY(0);
                     left: 0;
                     background: #7FC6A6;*!/
                    /!*$(".tgl-btn").css("-webkit-transform","rotateY(360deg)");
                    $(".tgl-btn").css("-ms-transform","rotateY(360deg)");
                    $(".tgl-btn").css("transform","rotateY(360deg)");
                    $("#89889").css("background-color","#7FC6A6");

                    $(".tgl-btn").css("-webkit-transition","all .4s ease");
                    $(".tgl-btn").css("transition","all .4s ease");*!/

                    /!*$("#89889").addClass("testss");*!/


                    /!*$(".tgl-btn").animate({
                        aa:"360" //目的就是取一个属性值360
                        },{
                        step:function(now,fx){
                            //console.log(now);
                            $(".tgl-btn").css({"transform":"rotate("+now+"deg)"}) }, duration:1000 });*!/


                    $("#"+val.id+"-unspan").addClass("doneText");
                    $("#"+val.id+"-mbspan").addClass("doneSmal");
                    $("#"+val.id+"-emspan").addClass("doneSmal");
                    $("#"+val.id+"-rlspan").addClass("doneSmal");
                    $("#"+val.id+"-atspan").addClass("doneSmal");

             }

            });

            allDataTol = data.totals;

            if(initPageplugins){

                /!*
                 * 初始化插件
                 * @param  object:翻页容器对象
                 * @param  function:回调函数
                 * *!/
                Pagination.init($(".ht-page"), pageChange);

                /!*
                * 首次调用
                * @param  object:翻页容器对象
                * @param  number:当前页
                * @param  number:总页数
                * @param  number:每页数据条数
                * *!/
                Pagination.Page($(".ht-page"), 0, allDataTol, 15);

                initPageplugins = false;

            }

        }else{

        }
        isShow = true;
    });

}*/

function showED(cur) {
    $("div[data-label='"+cur+"']").css("display","block");
}

function hideED(cur) {
    $("div[data-label='"+cur+"']").css("display","none");
}

function getUserAndRoles(obj,id) {

    $("#roleIds").val("");
    $("#id").val("");
    $("#version").val("");
    $("#isZx").val("");
    myResetForm();

    //如果已经离职，提醒不可编辑和删除
    if(obj.job){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: "该用户已经<span style='font-weight:bold;color:red;'>离职</span>，不可进行编辑; 如需编辑，请设置为<span style='font-weight:bold;color:green;'>在职</span>状态。",
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
    }else if(obj.del){

        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '该用户已经删除，不可进行编辑;  如需编辑，请先<span style="font-weight:bold;" color="blue">恢复</span>用户状态。',
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
    }else{
        //回显数据
        $.post("/user/getUserAndRoles",{"id":id},function(data){

            if(!isKickOut(data.code)){
                if(data.msg=="ok" && data.user!=null){
                    //将该用户的权限放入字符串
                    var existRole='';
                    if(data.user.userRoles !=null ){
                        $.each(data.user.userRoles, function (index, item) {
                            existRole+=item.roleId+',';
                        });
                    }

                    //将该用户的站点权限放入字符串
                    var existSiteRole='';
                    if(data.user.userSiteRoleKeys !=null ){
                        $.each(data.user.userSiteRoleKeys, function (index, item) {
                            existSiteRole+=item.siteRoleId+',';
                        });
                    }
                    $("#id").val(data.user.id==null?'':data.user.id);
                    $("#version").val(data.user.version==null?'':data.user.version);
                    $("#username").val(data.user.username==null?'':data.user.username);
                    $("#mobile").val(data.user.mobile==null?'':data.user.mobile);
                    $("#email").val(data.user.email==null?'':data.user.email);
                    $("#sdId").val(data.user.sdid==null?'':data.user.sdid);

                    //显示角色数据
                    $("#rolesDivs").empty();

                    $.each(data.roles, function (index, item) {

                        $("#rolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                            "<a id='raleaCli"+item.id+"' href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+item.id+"' data-original-title='"+item.roleName+"' onclick='chooseRoles("+item.id+",this)'>"+item.roleName+"</a></div>");

                        if(existRole!='' && existRole.indexOf(item.id)>=0){
                            $("#raleaCli"+item.id).click();
                        }

                    });

                    //显示角色数据
                    $("#siteRolesDivs").empty();

                    $.each(data.siteRoles, function (index, item) {

                        $("#siteRolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                            "<a id='raleaSiteCli"+item.id+"' href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+item.id+"' data-original-title='"+item.roleName+"' onclick='chooseSiteRoles("+item.id+",this)'>"+item.roleName+"</a></div>");

                        if(existSiteRole!='' && existSiteRole.indexOf(item.id)>=0){
                            $("#raleaSiteCli"+item.id).click();
                        }

                    });

                    $("#chgUpdTitle").text("更新用户");

                    if(sdId=="22"&&isZx=="false"){
                        $("#zx_type").html("<input class='tglZX tgl-flipZX' id='cb9' type='checkbox'><label class='tgl-btnZX' data-tg-off='所属智飨:否' data-tg-on='所属智飨:是' for='cb9' style='margin:0 auto;width: 20%;' onclick='changePerType()'></label>");
                        if(!data.user.zx){
                            $("#cb9").click();
                        }
                        $("#form-group-sdId").css("display","block");

                    }else{
                        $("#zx_type").html("");
                        $("#form-group-sdId").css("display","none");
                    }

                    $("#compose-modal").modal("show");

                    //重新渲染下form表单中的复选框 否则复选框选中效果无效
                    // layui.form.render();
                    //layui.form.render('checkbox');
                }else{
                    //弹出错误提示
                    /*layer.alert(data.msg,function () {
                        layer.closeAll();
                    });*/
                }
            }

        });
    }


}


function delUser(version,id,name) {

    var currentUser=$("#currentUser").html();

    if(null!=id&&id!=""){
        if(currentUser==id){

            $.confirm({
                icon: '#icon-tishi1',
                theme: 'modern',
                title: '提示',
                content: '对不起，您不能执行删除自己的操作！',
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

        }else{

            $.confirm({
                icon: '#icon-tishi1',
                theme: 'modern',
                title: '提示',
                content: '您确定要删除'+name+'用户吗？',
                animation: 'news',//动画
                closeAnimation: 'news',//关闭动画
                /*autoClose: 'showMyMsg2|3000',*/
                buttons: {
                    showMyMsg2: {
                        text: '确定',
                        action: function () {

                            $.post("/user/delUser",{"id":id,"version":version},function(data){
                                if(!isKickOut(data.code)){
                                    //没有被踢出
                                    if(data=="ok"){

                                        $.confirm({
                                            icon: '#icon-jinlingyingcaitubiao14',
                                            theme: 'modern',
                                            title: '提示',
                                            content: '删除成功！',
                                            animation: 'news',//动画
                                            closeAnimation: 'news',//关闭动画
                                            autoClose: 'showMyMsg3|3000',
                                            buttons: {
                                                showMyMsg3: {
                                                    text: '关闭',
                                                    action: function () {
                                                        //刷新数据
                                                        loadUserList(1,limCount);
                                                    }
                                                }/*,
                                                    cancel: function () {

                                                    }*/
                                            }
                                        });

                                    }else{
                                        $.confirm({
                                            icon: '#icon-tishi1',
                                            theme: 'modern',
                                            title: '提示',
                                            content: '删除失败！',
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

}

function changeJobStatus(curJob,curDId,curVersion,curDel){
//可以试试定义两个click事件，一个加css 一个click
    $("#"+curDId).prop("checked",false);
    var isJob;

    var cfText;

    if(curJob){
        //该员工已离职
        cfText = '是否确认该员工在职';
        isJob = 0;
    }else{
        //该员工在职
        cfText = '是否确认该员工离职';
        isJob = 1;
    }

    if(isShow){

        if(curDel){

            $.confirm({
                /*icon: 'glyphicon glyphicon-heart',*/
                /*icon: 'fa fa-bug',*/
                icon: '#icon-tishi1',
                theme: 'modern',
                title: '提示',
                content: '该用户已删除，请先恢复',
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

        }else{

            $.confirm({
                icon: '#icon-tishi1',//fa fa-question
                theme: 'modern',
                title: '是否离职?',
                content: cfText,
                animation: 'news',//动画
                closeAnimation: 'news',//关闭动画
                /*autoClose: 'logoutUser|8',*/
                buttons: {
                    logoutUser: {
                        text: '确认',
                        action: function () {

                            $.post("/user/setJobUser",{id:curDId,job:isJob,version:curVersion},function(data){

                                if(!isKickOut(data.code)){
                                    //没有被踢出

                                    if(data!=""&&data!=null&&(data.indexOf("ok")!=-1)){

                                        $("#18-lab").animate({},function(){
                                            //第一个花括号里面是动画内容，可以为空，但不能省去中括号

                                            //在回调函数里面改变css属性来实现transform中的动画变换
                                            //$("#18-lab").css({'-webkit-transform':'rotateY(180deg)','-ms-transform':'rotateY(180deg)','transform':'rotateY(180deg)','-webkit-transform':'rotateY(0)','-ms-transform':'rotateY(0)','transform':'rotateY(0)'});

                                        });

                                        loadUserList(curPag,limCount);

                                        $.confirm({
                                            icon: '#icon-jinlingyingcaitubiao14',
                                            theme: 'modern',
                                            title: '操作结果',
                                            content: data,
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

                                    }else{

                                        $.confirm({
                                            icon: '#icon-shangxin1',
                                            theme: 'modern',
                                            title: '操作结果',
                                            content: data,
                                            animation: 'news',//动画
                                            closeAnimation: 'news',//关闭动画
                                            autoClose: 'showMyMsg|3000',
                                            buttons: {
                                                showMyMsg: {
                                                    text: '关闭',
                                                    action: function () {

                                                    }
                                                },
                                                cancel: function () {

                                                }
                                            }
                                        });

                                    }

                                }

                            });

                        }
                    },
                    cancel: function () {
                        //退出
                        //window.location.href="/logout";
                    }
                }
            });

        }

    }

    isShow = true;

}

function chooseRoles(id,curA){

    var curAClass = $(curA).attr("class");

    if(curAClass!=undefined&&curAClass!="undefined"){
        if(curAClass.indexOf("menu-w3lsgrids-click")!=-1){
            //当前已选中，设置不选中，删除数组里的权限id
            // 删除起始下标为1，长度为1的一个值(可以理解为从这个数开始删除几个)，len设置的1，如果为0，则数组不变
            rolesArray.splice($.inArray(id,rolesArray),1);
            $(curA).removeClass("menu-w3lsgrids-click");
        }else{
            rolesArray.push(id);
            //当前未选中，加入class
            $(curA).addClass("menu-w3lsgrids-click");
        }
    }else{
        rolesArray.push(id);
        //当前未选中，加入class
        $(curA).addClass("menu-w3lsgrids-click");
    }

}

function chooseSiteRoles(id,curA){

    var curAClass = $(curA).attr("class");

    if(curAClass!=undefined&&curAClass!="undefined"){
        if(curAClass.indexOf("menu-w3lsgrids-click")!=-1){
            //当前已选中，设置不选中，删除数组里的权限id
            // 删除起始下标为1，长度为1的一个值(可以理解为从这个数开始删除几个)，len设置的1，如果为0，则数组不变
            siteRolesArray.splice($.inArray(id,siteRolesArray),1);
            $(curA).removeClass("menu-w3lsgrids-click");
        }else{
            siteRolesArray.push(id);
            //当前未选中，加入class
            $(curA).addClass("menu-w3lsgrids-click");
        }
    }else{
        siteRolesArray.push(id);
        //当前未选中，加入class
        $(curA).addClass("menu-w3lsgrids-click");
    }

}

/*新增用户*/
function createUser() {

    $("#roleIds").val("");
    $("#id").val("");
    $("#version").val("");
    $("#isZx").val("");

    myResetForm();

    $("#rolesDivs").empty();
    $("#siteRolesDivs").empty();

    $.post("/auth/getRoles",function(data){

        if(!isKickOut(data.code)){

            $("#chgUpdTitle").text("开通用户");

            if(data.roles.length>0){
                $.each(data.roles,function(ind,val){
                    $("#rolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                        "<a href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+val.id+"' data-original-title='"+val.roleName+"' onclick='chooseRoles("+val.id+",this)'>"+val.roleName+"</a></div>");
                });
            }

            if(data.siteRoles.length>0){
                $.each(data.siteRoles,function(ind,val){
                    $("#siteRolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                        "<a href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+val.id+"' data-original-title='"+val.roleName+"' onclick='chooseSiteRoles("+val.id+",this)'>"+val.roleName+"</a></div>");
                });
            }

            if(sdId=="22"&&isZx=="false"){
                $("#zx_type").html("<input class='tglZX tgl-flipZX' id='cb9' type='checkbox'><label class='tgl-btnZX' data-tg-off='所属智飨:否' data-tg-on='所属智飨:是' for='cb9' style='margin:0 auto;width: 20%;' onclick='changePerType()'></label>");
                $("#form-group-sdId").css("display","block");
            }else{
                $("#zx_type").html("");
                $("#form-group-sdId").css("display","none");
            }

            $("#compose-modal").modal("show");

        };


    });


}

function changePerType(){

    if($("#cb9").prop('checked')){
        $("#isZx").val(1);
    }else{
        $("#isZx").val(0);
    }

}

function myResetForm(){
    rolesArray = [];
    siteRolesArray = [];
    $('#userForm').resetForm();
    $('#userForm').clearForm();
    $("#rolesDivs").find(".menu-w3lsgrids-click").each(function(){
        $(this).removeClass("menu-w3lsgrids-click");
    });
}