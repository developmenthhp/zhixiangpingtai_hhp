var isShow = true;

var limCount = 12;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var loadSelect2Flag = false;

var map;

var myChart;

var myChart2;

var ajaxSCJCXXSearchFormOption = {
    //target: '#output', //把服务器返回的内容放入id为output的元素中
    beforeSubmit: showSCJCXXSearchRequest, //提交前的回调函数
    success: showSCJCXXSearchResponse, //提交后的回调函数
    url: "/disinfection/getPaginationData", //默认是form的action， 如果申明，则会覆盖
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
    addActClass($("#liParent116").children(":eq(0)"));
    $("#liParent116").children(":eq(1)").children(":eq(4)").children(":eq(0)").trigger("click");
    $("#liParent116").children(":eq(1)").children(":eq(4)").children(":eq(1)").children(":eq(0)").find("a").css("color","#FFD600");

    $("#topShowLp").css("display","none");

    myChart2 = echarts.init(document.getElementById('pie-main'));

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

    $('.imgSelect').selectator({
        labels: {
            search: ''
        },
        showAllOptionsOnFocus: true//初始化复选
    });

    $('.imgSelect').next().css("width","100%");
    $("#disinReason").next().find(".selectator_option_subtitle").css("color","#cccccc");
    $("#disinReason").next().find(".selectator_option_right").css("background-color","transparent");

    loadUserList();

    //监听模态框关闭时间 监听开启事件shown.bs.modal
    $("#compose-modal").on('hide.bs.modal', function (){
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
    reloadDetailChar(data);
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
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}

function initDateTableData(data){
    if(!isKickOut(data.code)){
        //没有被踢出
        /*var choseSelVal = $('#select1').val();
        if(choseSelVal==null||choseSelVal==''){
            var allSistes = getUserSites();
            $.each(allSistes.rows,function (ind,val) {
                if(ind==0){
                    choseSelVal = val.sdId;
                }else{
                    choseSelVal = choseSelVal+","+val.sdId;
                }
            });
        }*/
        if(data.rows!=null){

            $("#userListUl").html("");
            $.each(data.rows,function (ind,val) {
                var disReason = '';
                var checkStatusColor = '';
                var jcIcon = '';
                var transColor = '';
                switch (val.disReason) {
                    case '1':{
                        disReason = "按规完成";
                        checkStatusColor = 'green';
                        jcIcon = 'iconzuoyexuanzhongzhuangtai';
                        transColor = '#7fc6a6';
                    }
                        break;
                    case '2':{
                        disReason = "违规预警";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                }

                var disTime = '';
                if(val.disTime){
                    disTime = val.disTime;
                }

                var disPersion = '';
                if(val.disPersion){
                    disPersion = val.disPersion;
                }

                var disTyp = '';
                if(val.disTyp){
                    disTyp = val.disTyp;
                }

                var disActualTime = '';
                if(val.disActualTime){
                    disActualTime = val.disActualTime;
                }

                var disRegulation = '';
                if(val.disRegulation){
                    disRegulation = val.disRegulation;
                }

                var disPersionImg = '';
                if(val.disPersionImg){
                    disPersionImg = val.disPersionImg;
                }

                var disName = '';
                if(val.disName){
                    disName = val.disName;
                }

                var disinfectionTemperature='';
                if(val.disinfectionTemperature){
                    disinfectionTemperature = val.disinfectionTemperature;
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
                    "<td data-label='消毒时间' class='comMove'>" +
                    /*"<small id='"+val.id+"-foodNameTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.ingredientName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconbianma' style='color:#7ac6eb;'></i>&nbsp;"+disTime+"</span>" +
                    "</td>" +
                    "<td data-label='消毒项目' class='comMove'>" +
                    /*"<small id='"+val.id+"-foodNameTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.ingredientName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconshicai-' style='color:#f44e42;'></i>&nbsp;"+disName+"</span>" +
                    "</td>" +
                    "<td data-label='操作人' class='comMove'>" +
                    /*"<small id='"+val.id+"-dwTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconproduct_unit' style='color:#7ac6eb;'></i>&nbsp;"+disPersion+"</>" +
                    "</td>" +
                    "<td data-label='状态' class='comMove'>" +
                    /*"<small id='"+val.id+"-priceTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;￥"+val.basePrice+"</small>" +*/
                    "<span class='text'><i class='iconfont iconjiage1' style='color:#7ac6eb;'></i>&nbsp;"+disReason+"</span>" +
                    "</td>" +
                    "<td data-label='实时温度' class='comMove'>" +
                    /*"<small id='"+val.id+"-dateTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.ratedTerm+""+val.ratedTermDW+"</small>" +*/
                    "<span class='text'><i class='iconfont iconriqi1' style='color:#7ac6eb;'></i>&nbsp;"+disTyp+"°C</span>" +
                    "</td>" +
                    "<td data-label='规定温度' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconalert' style='color:#7ac6eb;'></i>&nbsp;"+disinfectionTemperature+"°C</span>" +
                    "</td>" +
                    "<td data-label='消费耗时' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconalert' style='color:#7ac6eb;'></i>&nbsp;"+disActualTime+"</span>" +
                    "</td>" +
                    "<td data-label='规定耗时' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconalert' style='color:#7ac6eb;'></i>&nbsp;"+disRegulation+"</span>" +
                    "</td>" +
                    "<td data-label='抓拍图片' class='comMove'>" +
                    /*"<small id='"+val.id+"-ccjjTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.inventoryLimit+""+val.meteringName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+disPersionImg+"' src='"+disPersionImg+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
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

        }else{
            //加一个字体图标无数据
        }

    }

}


function reloadDetailChar(data) {

    var abnormal = data.data;
    var normal = accSub(data.total,data.data);

    //点击的是正常bar,不包含点击最小值，最大值，平均值
    var rich2 = {
        white: {
            color: '#ddd',
            align: 'center',
            padding: [3, 0]
        }
    };
    // 健康证总数  param.value
    var myChart2 = echarts.init(document.getElementById('pie-main'));
    var placeHolderStyle2 = {
        normal: {
            label: {
                show: false
            },
            labelLine: {
                show: false
            },
            color: 'rgba(0, 0, 0, 0)',
            borderColor: 'rgba(0, 0, 0, 0)',
            borderWidth: 0
        }
    };

    var scaleData2 = [
        {
            'name': '消毒违规',
            'value': abnormal,
            'borderColor': 'red',//不使用随机色
            'shadowColor': 'red'
        },
        {
            'name': '按规完成',
            'value': normal,
            'borderColor': 'green',//不使用随机色
            'shadowColor': 'green'
        }
    ];


    var dataArray2 = [];
    for (var i = 0; i < scaleData2.length; i++) {
        dataArray2.push({
            value: scaleData2[i].value,
            name: scaleData2[i].name,
            itemStyle: {
                normal: {
                    borderWidth: 60,//模块大小
                    shadowBlur: 20,//阴影到校
                    borderColor: scaleData2[i].borderColor,
                    shadowColor: scaleData2[i].shadowColor
                }
            }
        }, {
            value: 50,
            name: '',
            itemStyle: placeHolderStyle2
        });
    }
    var seriesObj2 = [{
        name: '',
        type: 'pie',
        clockWise: false,
        radius: [90, 110],//模块到中心的距离
        hoverAnimation: false,
        itemStyle: {
            normal: {
                label: {
                    show: true,
                    position: 'outside',
                    color: '#ddd',
                    formatter: function(params) {
                        var percent = 0;
                        var total = data.total;
                        if(total == 0){
                            total = 1;
                        }
                        percent = accMul(accDiv(params.value,total),100).toFixed(0);
                        if(params.name !== '') {
                            return params.name + '\n{white|' + '占比' + percent + '%}';
                        }else {
                            return '';
                        }
                    },
                    rich: rich2
                },
                labelLine: {
                    length: 10,
                    length2: 40,//标记线的长度（模块说明）
                    show: true,
                    color: 'blue'
                }
            }
        },
        data: dataArray2
    }];
    var option2 = {
        title: {
            text:'消毒占比分析',
            left:'center',
            top:'41%',
            padding:[24,0],
            textStyle:{
                color:'#fff',
                fontSize:12,
                align:'center'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c}",

        },
        legend: {
            selectedMode:false,
            formatter: function(name) {
                return '{total|' + data.total + '}';
            },
            data: [scaleData2[0].name],
            left: 'center',
            top: '51%',
            icon: 'none',
            align:'center',
            textStyle: {
                color: "#fff",
                fontSize: 12,
                rich: rich2
            },
        },
        toolbox: {//工具栏
            show: true,
            feature: {
                mark: {
                    show: true,
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                },
                dataView: { //数据视图
                    show: true,
                    readOnly: false,//是否只读
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                },
                restore: {//还原原始图表
                    show: true,
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                },
                saveAsImage: {
                    show: true,
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                }
            }
        },
        series: seriesObj2
    }

    myChart2.setOption(option2);
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