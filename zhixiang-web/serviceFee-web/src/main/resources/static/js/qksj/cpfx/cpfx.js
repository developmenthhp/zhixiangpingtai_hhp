var isShow = true;

var limCount = 12;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var loadSelect2Flag = false;

var map;

var ajaxSCJCXXSearchFormOption = {
    //target: '#output', //把服务器返回的内容放入id为output的元素中
    beforeSubmit: showSCJCXXSearchRequest, //提交前的回调函数
    success: showSCJCXXSearchResponse, //提交后的回调函数
    url: "/foodBase/getAllFoodBBySdId", //默认是form的action， 如果申明，则会覆盖
    type: "post", //默认是form的method（get or post），如果申明，则会覆盖
    data:{page:curPag,limit:limCount},
    dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
    //clearForm: true,   //成功提交后，清除所有表单元素的值
    //resetForm: true,  //成功提交后，重置所有表单元素的值
    timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
};

$(function(){
//modelandview 返回的参数只能在html用[[${}}]]格式获取
    var admName = $("#teShiro").html();

    $("#showLAdm").html("欢迎, "+admName);
    $("#headAdm").html(admName+'<i class="caret"></i>');
    $("#headAdm2").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    var leftMenuStorage = localStorage.getItem("leftMenuStorage");

    $("#permUl").html(leftMenuStorage);
    addActClass($("#liParent136").children(":eq(0)"));
    $("#liParent136").children(":eq(1)").children(":eq(2)").find("a").css("color","#FFD600");

    $("#topShowLp").css("display","none");
    //writeTopLibPur();

    //myChart = echarts.init(document.getElementById('bar-main'));

    //添加点击事件（单击），还有其他鼠标事件和键盘事件等等
    /*myChart.on("click", function (param){
        reloadDetailChar(param)
    });*/

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

    $(".mySpecialUlUnit").removeAttr("left");

    //不需要submit按钮，可以是任何元素的click事件
    $("#searchFormI").click(function () {
        $("#alertSdId").val($("#select1").val());
        $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
        return false;
    });

    //<select multiple class="selectator" data-selectator-keep-open="true"> 持续打开
    $("#select1").on("change",function (e) {
        var curMapSelect = $(this).val();
        if(curMapSelect!=undefined&&curMapSelect!=null&&curMapSelect!=""){
            var curVal = curMapSelect[curMapSelect.length-1];
            var lng = $("#select1 option[value="+curVal+"]").attr("data-lng");
            var lat = $("#select1 option[value="+curVal+"]").attr("data-lat");
            map.clearOverlays();    //清除地图上所有覆盖物
            map.centerAndZoom(new BMap.Point(lng,lat), 18);
        }
        //重新加载charts
    });

    loadUserList();

    //监听模态框关闭时间 监听开启事件shown.bs.modal
    $("#compose-modal").on('hide.bs.modal', function (){
        /*var choseSelVal = $('#select1').val();
        if(choseSelVal!=undefined&&choseSelVal!=null&&choseSelVal!=''){
            loadMyLChar(choseSelVal.toString());
        }else{
            loadMyLChar('');
        }*/

        $('#select1').selectator('destroy');
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

function showSCJCXXSearchRequest(formData, jqForm, options){
    return true; //只要不返回false，表单都会提交,在这里可以对表单元素进行验证
};
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}

//声明一个函数
function validform() {
    return $("#permForm").validate({
        rules: {
            ingredientName: {
                required: true
            }
        },
        messages: {
            ingredientName: {
                required: "请输入用户名"
            }
        },
        errorClass: "error",
        success: 'valid',
        unhighlight: function (element, errorClass, validClass) { //验证通过
            $(element).tooltip('destroy').removeClass(errorClass);
        },
        //highlight: function (element, errorClass, validClass) { //未通过验证
        //    // TODO
        //}
        //,
        errorPlacement: function (error, element) {
            if ($(element).next("div").hasClass("tooltip")) {
                $(element).attr("data-original-title", $(error).text()).tooltip("show");
            } else {
                $(element).attr("title",
                    $(error).text()).tooltip("show");
            }
        },
        submitHandler: function (form) {

        }
    });
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
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}

function reloadMyDateTable() {
    $("input[name='foodNameDto']").val($("#foodNameDtoShow").val());
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}

function initDateTableData(data){
    if(!isKickOut(data.code)){
        //没有被踢出
        if(data.rows!=null){

            $("#userListUl").html("");
            $.each(data.rows,function (ind,val) {

                var foodName = '';
                if(val.foodName){
                    foodName = val.foodName;
                }
                var siteName = '';
                if(val.siteName){
                    siteName = val.siteName;
                }
                var curId = 'QRCode'+ind;
                $("#userListUl").append("<li>" +
                    "<div class='agileinfo_gallery_grid'>" +
                    "<figure class='imghvr-fold-up' style='background: transparent;text-align: center;left: 2.4em;'>" +
                    /*"<img alt='' class='img-responsive' style='width: 265px;' id='"+curId+"'/>" +*/
                    "<div class='img-responsive' style='width: 265px;' id='"+curId+"'></div>" +
                    "<span style='color:#fff'>"+foodName+"("+siteName+")</span>" +
                    "<figcaption onclick='showUseLoginStep("+val.id+","+val.sdId+")'>" +
                    "<h5 style='top:6em;'>"+foodName+"</h5>" +
                    "<ul class='myFoodUl' style='top:6em;'>" +
                    "<li style='width: 100%;top:20em;font-size: 0.8em;color: #b8b8b8;float: none;text-align: center;'>本数据由智飨科技提供</li></ul></figcaption></figure></div></li>");
                createCode(curId,data.obj+'/foodBase/getCompositionBySdIdId?id='+val.id+'&sdIds='+val.sdId,'myLogo');
                $("#"+curId).find("canvas").css("margin-top","2em");
                $("#"+curId).find("canvas").css("width"," width: 336.4px;");

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

    /*createCode('QRCode','http://121.43.35.181:8077/','myLogo');
    $("#QRCode").find("canvas").css("margin-top","2em");
    $("#QRCode").find("canvas").css("width"," width: 336.4px;");*/
}

/* 地图形式显示站点 */
function openMap() {
    map = showMap(map,'compose-modal');
}

function showUseLoginStep(id,sdId) {
    window.location.href='/foodBase/getComLFBySdIdId?id='+id+'&sdIds='+sdId;
}

function cleanComposition(){
    //清除select2选中值
    cleanSelect2Val(["choseStatus","choseReason"]);
    //清除隐藏域值
    /*var hiddenInputIds = new Array("id");
    cleanHiddenInputIds(hiddenInputIds);*/
    //清除提交form
    commonResetForm("composition-Form");
    //隐藏
    $("#reasonDiv").css("display","none");
}