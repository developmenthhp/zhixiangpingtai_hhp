var isShow = true;

var initPageplugins = true;

var fruits;
var map;

var myChart2;
var myChartDet;

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

    /*实例化编辑器 */
    /*var um = UE.getEditor('myEditor');*/

    myChart2 = echarts.init(document.getElementById('pie-main'));

    myChart2.on("click", function (param){
        reloadDetailChar(param);
    });

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

    //获取当月月份天数数组数据
    dataViewBottom = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    oldDateBottom = dataViewBottom;

    loadMyLChar('');

    //监听模态框关闭时间 监听开启事件shown.bs.modal
    $("#compose-modal").on('hide.bs.modal', function (){
        dataViewBottom = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
        var choseSelVal = $('#select1').val();
        if(choseSelVal!=undefined&&choseSelVal!=null&&choseSelVal!=''){
            loadMyLChar(choseSelVal.toString());
        }else{
            loadMyLChar('');
        }

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
}
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}

/* 地图形式显示站点 */
function openMap() {
    map = showMap(map,'compose-modal');
}

function loadMyLChar(rloadSdIds) {

    //点击的是正常bar,不包含点击最小值，最大值，平均值
    var rich2 = {
        white: {
            color: '#ddd',
            align: 'center',
            padding: [3, 0]
        }
    };

    // 健康证总数  param.value
    myChart2 = echarts.init(document.getElementById('pie-main'));

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

    $.post("/selfCheck/getTotalBySdId",{"sdIds":rloadSdIds},function(data){
        if(data.code==1000){
            var allTotal = data.data;
            var scaleData2 = [
                {
                    'name': '货品入库',
                    'value': allTotal[0],
                    'borderColor': '#c23531',//不使用随机色
                    'shadowColor': '#c23531'
                },
                {
                    'name': '货品领料',
                    'value': allTotal[1],
                    'borderColor': 'gold',//不使用随机色
                    'shadowColor': 'gold'
                },
                {
                    'name': '货品库存存量',
                    'value': allTotal[2],
                    'borderColor': '#5b9097',//不使用随机色
                    'shadowColor': '#5b9097'
                },
                {
                    'name': '货品库存盘点',
                    'value': allTotal[3],
                    'borderColor': '#d48265',//不使用随机色
                    'shadowColor': '#d48265'
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
                            shadowBlur: 85,//阴影到校
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
                                var total = 0;
                                for (var i = 0; i < scaleData2.length; i++) {
                                    total += scaleData2[i].value;
                                }
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
                    text:'占比',
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
                        var total = 0;
                        for (var i = 0; i < scaleData2.length; i++) {
                            total += scaleData2[i].value;
                        }
                        return '{total|' + total + '}';
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
    });
}

function reloadDetailChar(param) {

    var choseSelVal = $('#select1').val();
    if(choseSelVal==undefined){
        choseSelVal = '';
    }else{
        choseSelVal = choseSelVal.toString();
    }
    //点击的是正常bar,不包含点击最小值，最大值，平均值
    var rich2 = {
        white: {
            color: '#ddd',
            align: 'center',
            padding: [3, 0]
        }
    };

    // 健康证总数  param.value
    myChartDet = echarts.init(document.getElementById('detail-pie'));

    var url = '';

    if(param.name=='货品领料'){
        url = '/iwareOut/getTotalBySdIdStatus';
    }else if(param.name=='货品入库'){
        url = '/iwareReport/getTotalBySdIdStatus';
    }else if(param.name=='货品库存盘点'){
        url = '/iwarekCPD/getTotalBySdIdStatus';
    }else if(param.name=='货品库存存量'){
        url = '/iwarekCBB/getTotalBySdIdStatus';
    }

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

    $.post(url,{"sdIds":choseSelVal},function(data){
        if(data.code==1000){
            var scaleData2 = [
                {
                    'name': '审签中',
                    'value': data.data,
                    'borderColor': '#c23531',//不使用随机色
                    'shadowColor': '#c23531'
                },
                {
                    'name': '审签完成',
                    'value': data.total,
                    'borderColor': 'gold',//不使用随机色
                    'shadowColor': 'gold'
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
                            shadowBlur: 85,//阴影到校
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
                                var total = 0;
                                for (var i = 0; i < scaleData2.length; i++) {
                                    total += scaleData2[i].value;
                                }
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
                    text:'审签状态占比',
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
                        var total = 0;
                        for (var i = 0; i < scaleData2.length; i++) {
                            total += scaleData2[i].value;
                        }
                        return '{total|' + total + '}';
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

            myChartDet.setOption(option2);
        }
    });
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