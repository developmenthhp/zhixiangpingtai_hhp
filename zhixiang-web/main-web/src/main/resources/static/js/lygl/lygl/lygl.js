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
    url: "/keepSample/getPaginationData", //默认是form的action， 如果申明，则会覆盖
    type: "post", //默认是form的method（get or post），如果申明，则会覆盖
    data:{page:curPag,limit:limCount},
    dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
    //clearForm: true,   //成功提交后，清除所有表单元素的值
    //resetForm: true,  //成功提交后，重置所有表单元素的值
    timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
};

$(function(){

    var admName = $("#teShiro").html();

    $("#showLAdm").html("欢迎, "+admName);
    $("#headAdm").html(admName+'<i class="caret"></i>');
    $("#headAdm2").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    var leftMenuStorage = localStorage.getItem("leftMenuStorage");

    $("#permUl").html(leftMenuStorage);
    addActClass($("#liParent116").children(":eq(0)"));
    $("#liParent116").children(":eq(1)").children(":eq(5)").children(":eq(0)").css("color","#FFD600");

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

    //初始化日期插件
    $('#timeQuantum').daterangepicker(null, function(start, end, label) {
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
                var menuOrder = '';
                var checkStatusColor2 = '';
                var jcIcon2 = '';
                var transColor2 = '';
                switch (val.menuOrder) {
                    //;1 制样;2 存样;3 出样;4 作废此次
                    case '1':{
                        menuOrder = "早餐";
                        checkStatusColor2 = 'green';
                        jcIcon2 = 'iconzuoyexuanzhongzhuangtai';
                        transColor2 = '#7fc6a6';
                    }
                        break;
                    case '2':{
                        menuOrder = "午餐";
                        checkStatusColor2 = 'red';
                        jcIcon2 = 'iconzuoyeweixuanzhong';
                        transColor2 = '#d9534f';

                    }
                        break;
                    case '3':{
                        menuOrder = "晚餐";
                        checkStatusColor2 = 'red';
                        jcIcon2 = 'iconzuoyeweixuanzhong';
                        transColor2 = '#d9534f';

                    }
                        break;
                    case '4':{
                        menuOrder = "夜宵";
                        checkStatusColor2 = 'red';
                        jcIcon2 = 'iconzuoyeweixuanzhong';
                        transColor2 = '#d9534f';

                    }
                        break;
                }

                var status = '';
                var checkStatusColor = '';
                var jcIcon = '';
                var transColor = '';
                switch (val.status) {
                    //;1 制样;2 存样;3 出样;4 作废此次
                    case '1':{
                        status = "制样成功";
                        checkStatusColor = 'green';
                        jcIcon = 'iconzuoyexuanzhongzhuangtai';
                        transColor = '#7fc6a6';
                    }
                        break;
                    case '2':{
                        status = "存样成功";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                    case '3':{
                        status = "出样成功";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                    case '4':{
                        status = "此次作废";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                }

                var depositTime = '';
                if(val.depositTime){
                    depositTime = val.depositTime;
                }

                var cyTime = '';
                if(val.cyTime){
                    cyTime = val.cyTime;
                }

                var depositOperator = '';
                if(val.depositOperator){
                    depositOperator = val.depositOperator;
                }

                var depositImg = '';
                if(val.depositImg){
                    depositImg = val.depositImg;
                }

                var cyOperator = '';
                if(val.cyOperator){
                    cyOperator = val.cyOperator;
                }

                var cyOperatorImg = '';
                if(val.cyOperatorImg){
                    cyOperatorImg = val.cyOperatorImg;
                }

                var sdId='';
                if(val.sdId){
                    sdId = val.sdId;
                }

                var foodCount = 0;
                if(val.foodCount){
                    foodCount = val.foodCount;
                }

                var siteName = '';
                if(val.siteName){
                    siteName = val.siteName;
                }

                var sitePhoto='';
                if(val.sitePhoto){
                    sitePhoto = val.sitePhoto;
                }

                $("#userListUl").append("<tr class='myTableTooltip' title='"+siteName+":所属'>" +
                    "<td data-label='站点'>" +
                    "<div class='my-table-imgDiv'><img  data-original='"+sitePhoto+"' src='"+sitePhoto+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "<td data-label='样品管理日期' class='comMove'>" +
                    /*"<small id='"+val.id+"-foodNameTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.ingredientName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconbianma' style='color:#7ac6eb;'></i>&nbsp;"+depositTime+"</span>" +
                    "</td>" +
                    "<td data-label='餐次' class='comMove'>" +
                    /*"<small id='"+val.id+"-foodNameTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.ingredientName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconshicai-' style='color:#f44e42;'></i>&nbsp;"+menuOrder+"</span>" +
                    "</td>" +
                    "<td data-label='状态' class='comMove'>" +
                    /*"<small id='"+val.id+"-dwTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconproduct_unit' style='color:#7ac6eb;'></i>&nbsp;"+status+"</>" +
                    "</td>" +
                    "<td data-label='消样时间' class='comMove'>" +
                    /*"<small id='"+val.id+"-priceTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;￥"+val.basePrice+"</small>" +*/
                    "<span class='text'><i class='iconfont iconjiage1' style='color:#7ac6eb;'></i>&nbsp;"+cyTime+"</span>" +
                    "</td>" +
                    "<td data-label='样品' style='cursor: pointer' onclick='showKeepSampleDetails("+val.id+","+val.sdId+")'>" +
                    /*"<small id='"+val.id+"-dateTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.ratedTerm+""+val.ratedTermDW+"</small>" +*/
                    "<span class='text' style='color:blue;'><i class='iconfont iconriqi1' style='color:#7ac6eb;'></i>&nbsp;"+foodCount+"份</span>" +
                    "</td>" +
                    "<td data-label='留样人员' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconalert' style='color:#7ac6eb;'></i>&nbsp;"+depositOperator+"</span>" +
                    "</td>" +
                    "<td data-label='留样人员图' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+depositImg+"' src='"+depositImg+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "<td data-label='出样人员' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconalert' style='color:#7ac6eb;'></i>&nbsp;"+cyOperator+"</span>" +
                    "</td>" +
                    "<td data-label='出样人员图' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+cyOperatorImg+"' src='"+cyOperatorImg+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "</tr>");

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


            $("#userListUl").viewer('update');
            $('#userListUl').viewer({
                url: 'data-original',
            });

            $(".myTableTooltip").css('font-weight', 'normal');
            $(".myTableTooltip").tooltip({
                effect:'toggle',
                cancelDefault: true
            });

        }else{
            //加一个字体图标无数据
        }

    }
}

function showKeepSampleDetails(ksId,ksdId) {
    $.post("/keepSampleDetail/getDetailByIdSdId", {lyMainId:ksId,sdId:ksdId}, function(data){
        if (data.code==1000) {
            $("#composition-Ul").html("");
            $.each(data.rows,function (indI,valI) {

                var status = '';
                var checkStatusColor = '';
                var jcIcon = '';
                var transColor = '';
                switch (valI.lyTypes) {
                    //;1 制样;2 存样;3 出样;4 作废此次
                    case '1':{
                        status = "未存样品";
                        checkStatusColor = 'green';
                        jcIcon = 'iconzuoyexuanzhongzhuangtai';
                        transColor = '#7fc6a6';
                    }
                        break;
                    case '2':{
                        status = "识别存样";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                    case '3':{
                        status = "应急存样";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                }

                var statusCY = '';
                var checkStatusColorCY = '';
                var jcIconCY = '';
                var transColorCY = '';
                switch (valI.cyTypes) {
                    //;1 制样;2 存样;3 出样;4 作废此次
                    case '1':{
                        statusCY = "未出样品";
                        checkStatusColorCY = 'green';
                        jcIconCY = 'iconzuoyexuanzhongzhuangtai';
                        transColorCY = '#7fc6a6';
                    }
                        break;
                    case '2':{
                        statusCY = "识别出样";
                        checkStatusColorCY = 'red';
                        jcIconCY = 'iconzuoyeweixuanzhong';
                        transColorCY = '#d9534f';

                    }
                        break;
                    case '3':{
                        statusCY = "应急出样";
                        checkStatusColorCY = 'red';
                        jcIconCY = 'iconzuoyeweixuanzhong';
                        transColorCY = '#d9534f';

                    }
                        break;
                }

                var lyFoodName = "";
                if(valI.lyFoodName){
                    lyFoodName = valI.lyFoodName;
                }
                var lyItemLabel = "";
                if(valI.lyItemLabel){
                    lyItemLabel = valI.lyItemLabel;
                }
                var lyTime = '';
                if(valI.lyTime){
                    lyTime = valI.lyTime;
                }
                var lyLabelImg = "";
                if(valI.lyLabelImg){
                    lyLabelImg = valI.lyLabelImg;
                }
                var cyTime = 0;
                if(valI.cyTime){
                    cyTime = valI.cyTime;
                }
                var cyLabelImg = 0;
                if(valI.cyLabelImg){
                    cyLabelImg = valI.cyLabelImg;
                }

                $("#composition-Ul").append("<tr>" +
                    "<td data-label='菜品'>" +
                    "<span class='text'><i class='iconfont iconshicai-' style='color:#f44e42;'></i>&nbsp;"+lyFoodName+"</span>" +
                    "</td>" +
                    "<td data-label='ocr识码'>" +
                    /*"<small id='"+val.id+"-foodNameTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.ingredientName+"</small>" +*/
                    "<span class='text'><i class='iconfont icongouwuche' style='color:gold;'></i>&nbsp;"+lyItemLabel+"</span>" +
                    "</td>" +
                    "<td data-label='存入状态'>" +
                    /*"<small id='"+val.id+"-dwTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconbaobiaomiaoshu' style='color:#f44e42;'></i>&nbsp;"+status+"</>" +
                    "</td>" +
                    "<td data-label='存入时间'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<span class='text'><i class='iconfont icongouwuche' style='color:green;'></i>&nbsp;"+lyTime+"</>" +
                    "</td>" +
                    "<td data-label='存入现场图'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+lyLabelImg+"' src='"+lyLabelImg+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "<td data-label='出样状态'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconjiage1' style='color:green;'></i>&nbsp;"+statusCY+"</>" +
                    "</td>" +
                    "<td data-label='出样时间'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconjiage1' style='color:gold;'></i>&nbsp;"+cyTime+"</>" +
                    "</td>" +
                    "<td data-label='出样作业人员图'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+cyLabelImg+"' src='"+cyLabelImg+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "</tr>");
            });

            $("#composition-modal").viewer('update');
            $('#composition-modal').viewer({
                url: 'data-original',
            });
        }else{
            //系统接口错误
            showJqueryConfirmWindow("#icon-shangxin1",data.msg);
        }
    });

    $("#composition-modal").modal("show");
}

/* 地图形式显示站点 */
function openMap() {
    map = showMap(map,'compose-modal');
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