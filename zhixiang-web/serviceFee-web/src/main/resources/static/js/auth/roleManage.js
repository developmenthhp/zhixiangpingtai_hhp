var tes = 0;
var isShow = true;
var allPage;

var limCount = 10;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var rolesArray = [];

var sdId;

var isZx;

var ajaxSCJCXXSearchFormOption = {
    //target: '#output', //把服务器返回的内容放入id为output的元素中
    beforeSubmit: showSCJCXXSearchRequest, //提交前的回调函数
    success: showSCJCXXSearchResponse, //提交后的回调函数
    url: "/auth/getRoleList", //默认是form的action， 如果申明，则会覆盖
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

    /*var tt = localStorage.getItem("admNameDate").split(",");

    $("#showLAdm").html("欢迎, "+tt[0]);
    $("#headAdm").html(tt[0]+'<i class="caret"></i>');
    $("#headAdm2").html(tt[0]);
    $("#headAdmDate").html(tt[1]);*/

    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    var leftMenuStorage = localStorage.getItem("leftMenuStorage");

    $("#permUl").html(leftMenuStorage);
    addActClass($("#liParent1").children(":eq(0)"));
    $("#liParent1").children(":eq(1)").children(":eq(1)").find("a").css("color","#FFD600");

    loadUserList();

    createRoleDiv();

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

    $("i").tooltip();

    //不需要submit按钮，可以是任何元素的click事件
    $("#searchFormI").click(function () {
        $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
        return false;
    });

    $("#myFormButton").on("click",showUpdMod);
    $("#myFormButton").on("click",showAddMod);

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

function showUpdMod() {

    var ajaxFormOptionUPD = {
        //target: '#output', //把服务器返回的内容放入id为output的元素中
        beforeSubmit: showRequest, //提交前的回调函数
        success: showResponse, //提交后的回调函数
        url: "/auth/setRole", //默认是form的action， 如果申明，则会覆盖
        type: "post", //默认是form的method（get or post），如果申明，则会覆盖
        //data:
        dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
        //clearForm: true,   //成功提交后，清除所有表单元素的值
        //resetForm: true,  //成功提交后，重置所有表单元素的值
        timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
    }

    $("#rolePermIds").val(rolesArray.join(","));

    $("#roleForm").ajaxSubmit(ajaxFormOptionUPD);

    return false;
}

function showAddMod(){
    //添加的
    var ajaxFormOptionADD = {
        //target: '#output', //把服务器返回的内容放入id为output的元素中
        beforeSubmit: showRequest, //提交前的回调函数
        success: showResponse, //提交后的回调函数
        url: "/auth/addRole", //默认是form的action， 如果申明，则会覆盖
        type: "post", //默认是form的method（get or post），如果申明，则会覆盖
        //data:
        dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
        //clearForm: true,   //成功提交后，清除所有表单元素的值
        //resetForm: true,  //成功提交后，重置所有表单元素的值
        timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
    }

    $("#rolePermIds").val(rolesArray.join(","));

    $("#roleForm").ajaxSubmit(ajaxFormOptionADD);

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
};
function showResponse(data, status){
    //responseText 返回的数据
    //statusText 返回标识
    if(data.code==1000){
        //刷新数据
        //loadUserList(1,limCount);
        loadUserList();
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
                    "<td data-label='角色编号' class='comMove'>" +
                    "<span class='handle'>"+val.code+"</span>" +
                    "</td>" +
                    "<td data-label='角色名称' class='comMove'>" +
                    "<small id='"+val.id+"-nmspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-user'></i>&nbsp;"+val.roleName+"</small>" +
                    "</td>" +
                    "<td data-label='角色描述' class='comMove'>" +
                    "<span id='"+val.id+"-unspan' class='text'>"+val.descpt+"</span>" +
                    "</td>" +
                    "<td data-label='添加时间' class='comMove'>" +
                    "<small id='"+val.id+"-atspan' class='label label-danger'><i class='fa fa-clock-o'></i>&nbsp;"+val.insertTime+"</small>" +
                    "</td>" +
                    "<td data-label='操作'>" +
                    "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                    "<i class='fa fa-edit' style='cursor: pointer' onclick='updateRole("+val.id+")'></i>&nbsp;" +
                    "<i class='fa fa-trash-o' style='cursor: pointer' onclick='delRole("+val.id+","+"\""+val.roleName+"\")'></i>" +
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
                    $("#"+val.id+"-mbspan").removeClass("doneText");
                    $("#"+val.id+"-emspan").removeClass("doneText");
                    $("#"+val.id+"-rlspan").removeClass("doneSmal");
                    $("#"+val.id+"-atspan").removeClass("doneSmal");

                }else{

                    $("#"+val.id+"-unspan").addClass("doneText");
                    $("#"+val.id+"-mbspan").addClass("doneText");
                    $("#"+val.id+"-emspan").addClass("doneText");
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

    $.post("/auth/getRoleList",{page:curPag,limit:limCount},function(data){

        //$("#userList").nextAll().remove(); //该方法会删除当前节点下的所有子节点，请注意当前节点会 被删除

        if(data!=null){

            $("#userListUl").html("");

            $.each(data,function(ind,val){

                //小品40% checkbook myStyleLi
                $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                    "<td data-label='角色编号' class='comMove'>" +
                    "<span class='handle'>"+val.code+"</span>" +
                "</td>" +
                "<td data-label='角色名称' class='comMove'>" +
                    "<small id='"+val.id+"-nmspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-user'></i>&nbsp;"+val.roleName+"</small>" +
                "</td>" +
                "<td data-label='角色描述' class='comMove'>" +
                    "<span id='"+val.id+"-unspan' class='text'>"+val.descpt+"</span>" +
                "</td>" +
                "<td data-label='添加时间' class='comMove'>" +
                    "<small id='"+val.id+"-atspan' class='label label-danger'><i class='fa fa-clock-o'></i>&nbsp;"+val.insertTime+"</small>" +
                "</td>" +
                "<td data-label='操作'>" +
                    "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                        "<i class='fa fa-edit' style='cursor: pointer' onclick='updateRole("+val.id+")'></i>&nbsp;" +
                        "<i class='fa fa-trash-o' style='cursor: pointer' onclick='delRole("+val.id+","+"\""+val.roleName+"\")'></i>" +
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
                    $("#"+val.id+"-mbspan").removeClass("doneText");
                    $("#"+val.id+"-emspan").removeClass("doneText");
                    $("#"+val.id+"-rlspan").removeClass("doneSmal");
                    $("#"+val.id+"-atspan").removeClass("doneSmal");

                }else{

                    $("#"+val.id+"-unspan").addClass("doneText");
                    $("#"+val.id+"-mbspan").addClass("doneText");
                    $("#"+val.id+"-emspan").addClass("doneText");
                    $("#"+val.id+"-rlspan").addClass("doneSmal");
                    $("#"+val.id+"-atspan").addClass("doneSmal");

             }

            });

            allDataTol = data.totals;

            /!*if(initPageplugins){

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

            }*!/

        }else{

            /!*$.confirm({
                icon: 'fa fa-question',
                theme: 'modern',
                title: '权限不足?',
                content: '权限不足，请联系管理员',
                animation: 'news',
                closeAnimation: 'news',
                autoClose: 'logoutUser|8',
                buttons: {
                    logoutUser: {
                        text: '重新登录',
                        action: function () {
                            //退出
                            window.location.href="/logout";
                        }
                    }/!*,
                    cancel: function () {
                        //退出
                        window.location.href="/logout";
                    }*!/
                }
            });*!/

        }
        isShow = true;
    });

}*/

//将所有权限查出来
function createRoleDiv(){
    $.post("/auth/findPerms",function(data){

        if (data !=null&&data.length>0) {
            $.each(data,function(ind,val){
                $("#rolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                    "<a id='firstARole"+val.id+"' href='javascript:void(0)' data-toggle='tooltip' title='' data-original-title='"+val.name+"' onclick='chooseRoles("+val.id+",this)'>"+val.name+"</a></div>");
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

function ts(did) {
    alert(did+"sdlfkjsoidfj");
}


function befShowAddMod(){

    $("#myFormButton").off("click",showAddMod);
    $("#myFormButton").off("click",showUpdMod);
    $("#myFormButton").on("click",showAddMod);

    //清空隐藏域id
    $("#id").val("");

    //重置清空form
    myResetForm();

    if(sdId=="22"&&isZx=="false"){
        $("#form-group-sdId").css("display","block");
    }else{
        $("#form-group-sdId").css("display","none");
    }

    $("#compose-modal").modal("show");

}

function updateRole(id) {

    $("#myFormButton").off("click",showUpdMod);
    $("#myFormButton").off("click",showAddMod);
    $("#myFormButton").on("click",showUpdMod);

    //清空隐藏域id
    $("#id").val("");

    //重置清空form
    myResetForm();

    //isNaN是数字返回false
    if(id!=null && !isNaN(id)){

        $.post("/auth/getUpdRoles",{"id":id},function(data){
            if(!isKickOut(data.code)){
                //没有被踢出
                if(data.code==1000){

                    $("#roleName").val(data.data.roleName);
                    $("#descpt").val(data.data.descpt);
                    $("#code").val(data.data.code);

                    $("#id").val(data.data.id);
                    $("#sdId").val(data.data.sdId);
                    
                    $.each(data.data.rolePerms,function (ind,val) {
                       $("#firstARole"+val.permitId).click();
                    });

                    $("#chgUpdTitle").text("更新角色");

                    if(sdId=="22"&&isZx=="false"){
                        $("#form-group-sdId").css("display","block");
                    }else{
                        $("#form-group-sdId").css("display","none");
                    }

                    $("#compose-modal").modal("show");

                }else{
                    showJqueryConfirmWindow("#icon-shangxin1",data.msg);
                }
            }
        });

    }else{
        showJqueryConfirmWindow("#icon-tishi1",data.msg);
    }
}

function delRole(id,name) {

    if(null!=id&&id!=""){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'角色吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {

                        $.post("/auth/delRole",{"id":id},function(data){
                            if(!isKickOut(data.code)){
                                //没有被踢出
                                if(data=="ok"){
                                    //刷新数据
                                    loadUserList();
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除角色成功！');
                                }else{
                                    showJqueryConfirmWindow("#icon-shangxin1",'删除角色失败！');
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

function myResetForm(){
    //清空权限数组
    rolesArray = [];
    $("#rolePermIds").val("");
    $('#roleForm').resetForm();
    $('#roleForm').clearForm();
    $("#rolesDivs").find(".menu-w3lsgrids-click").each(function(){
        $(this).removeClass("menu-w3lsgrids-click");
    });
}