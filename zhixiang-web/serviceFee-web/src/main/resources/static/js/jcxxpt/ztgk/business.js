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
    url: "/notarizedCertificate/getBusInfoBySdId", //默认是form的action， 如果申明，则会覆盖
    type: "post", //默认是form的method（get or post），如果申明，则会覆盖
    data:{},
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
    //添加样式
    addActClass($("#liParent111").children(":eq(0)"));
    $("#liParent111").children(":eq(1)").children(":eq(0)").find("a").css("color","#FFD600");

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

    loadUserList();

    //监听模态框关闭时间 监听开启事件shown.bs.modal
    $("#compose-modal").on('hide.bs.modal', function (){
        $("#alertSdId").val($("#select1").val());
        loadUserList();
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
        if(data.rows!=null){
            $("#userListUl").html("");
            $.each(data.rows,function (ind,val) {
                var status = '';
                var checkStatusColor2 = '';
                var jcIcon2 = '';
                var transColor2 = '';
                if(val.dataCount>0){
                    status = "已上传";
                    checkStatusColor2 = 'green';
                    jcIcon2 = 'iconzuoyexuanzhongzhuangtai';
                    transColor2 = '#7fc6a6';
                }else{
                    status = "未上传";
                    checkStatusColor2 = 'red';
                    jcIcon2 = 'iconzuoyeweixuanzhong';
                    transColor2 = '#d9534f';
                }

                var imgPath = '';
                if(val.imgPath){
                    imgPath = val.imgPath;
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
                    "<td data-label='状态' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconalert' style='color:"+checkStatusColor2+";'></i>&nbsp;"+status+"</span>" +
                    "</td>" +
                    "<td data-label='营业执照' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+imgPath+"' src='"+imgPath+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "</tr>");
            });

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