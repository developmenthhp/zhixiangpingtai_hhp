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
    url: "/bioFood/getAllIngBBySdId", //默认是form的action， 如果申明，则会覆盖
    type: "post", //默认是form的method（get or post），如果申明，则会覆盖
    data:{page:curPag,limit:limCount},
    dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
    //clearForm: true,   //成功提交后，清除所有表单元素的值
    //resetForm: true,  //成功提交后，重置所有表单元素的值
    timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
};

$(function(){
    var textResultBean = $("#responseDate").text();
    /*var reg = new RegExp("=","g");//g,表示全部替换。
    textResultBean = textResultBean.replace(reg,"}");*/
    var stepData = JSON.parse(textResultBean);
    if(stepData){
        if(stepData[0]){
            $("#whdWeight").text(stepData[0].foodWeight+"公斤");
        }
        if(stepData[1]){
            $("#loWeight").text(stepData[1].foodWeight+"公斤");
        }
        if(stepData[2]){
            $("#cleanWeight").text(stepData[2].foodWeight+"公斤");
        }
        if(stepData[3]){
            $("#adWeight").text(stepData[3].foodWeight+"公斤");
        }
        if(stepData[4]){
            $("#cutWeight").text(stepData[4].foodWeight+"公斤");
        }
        if(stepData[5]){
            $("#cookWeight").text(stepData[5].foodWeight+"公斤");
        }

    }

    //不需要submit按钮，可以是任何元素的click事件
    $("#searchFormI").click(function () {
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

    //loadUserList();

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
        var choseSelVal = $('#select1').val();
        if(data.rows!=null){

            $("#userListUl").html("");
            $.each(data.rows,function (ind,val) {

                var foodName = '';
                if(val.ingredientname){
                    foodName = val.ingredientname;
                }
                var curId = 'QRCode'+ind;
                $("#userListUl").append("<li>" +
                    "<div class='agileinfo_gallery_grid'>" +
                    "<figure class='imghvr-fold-up' style='background: transparent;text-align: center;left: 2.4em;'>" +
                    /*"<img alt='' class='img-responsive' style='width: 265px;' id='"+curId+"'/>" +*/
                    "<div class='img-responsive' style='width: 265px;' id='"+curId+"'></div>" +
                    "<span style='color:#fff'>"+foodName+"</span>" +
                    "<figcaption>" +
                    "<h5 style='top:6em;'>"+foodName+"</h5>" +
                    "<ul class='myFoodUl' style='top:6em;'>" +
                    "<li style='width: 100%;top:20em;font-size: 0.8em;color: #b8b8b8;float: none;text-align: center;'>本数据由智飨科技提供</li></ul></figcaption></figure></div></li>");
                createCode(curId,data.obj+'/bioFood/getIngBUseBySdIdName?ingredientName='+foodName+'&sdIdStr='+choseSelVal,'myLogo');
                $("#"+curId).find("canvas").css("margin-top","2em");
                $("#"+curId).find("canvas").css("width"," width: 336.4px;");

            });

        }else{
            //加一个字体图标无数据
        }

    }
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