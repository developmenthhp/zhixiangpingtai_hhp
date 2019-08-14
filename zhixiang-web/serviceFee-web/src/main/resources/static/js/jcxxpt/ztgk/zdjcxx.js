var isShow = true;

var initPageplugins = true;

var fruits;
var map;
var myChart;
var myChart2;
var myChart3;
var myChart4;
var myChart6;
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
    //添加样式
    addActClass($("#liParent111").children(":eq(0)"));
    $("#liParent111").children(":eq(1)").children(":eq(0)").find("a").css("color","#FFD600");

    $("#topShowLp").css("display","none");
    //writeTopLibPur();

    map = showMap(map,'');

    myChart2 = echarts.init(document.getElementById('pie-main'));
    myChart3 = echarts.init(document.getElementById('pie2-main'));
    myChart4 = echarts.init(document.getElementById('pie3-main'));
    myChart6 = echarts.init(document.getElementById('pie4-main'));
    //添加点击事件（单击），访问营业执照页面
    myChart3.on("click", function (param){
        window.location.href="/notarizedCertificate/getBusPage";
    });
    //添加点击事件（单击），访问许可证页面
    myChart4.on("click", function (param){
        window.location.href="/notarizedCertificate/getLicPage";
    });
    //添加点击事件（单击），访问流通证页面
    myChart6.on("click", function (param){
        window.location.href="/notarizedCertificate/getCirPage";
    });
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

    //不需要submit按钮，可以是任何元素的click事件
    $("#searchFormI").click(function () {
        $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
        return false;
    });

    $("#choseStatus").val("");
    $("#choseStatus").trigger("chosen:updated");

    $("#choseReason").val("");
    $("#choseReason").trigger("chosen:updated");

    $("#choseStatus").on("change",function(){
        if($(this).val()=="2"){
            $("#reasonDiv").css("display","block");
        }else{
            $("#choseReason").val("");
             $("#choseReason").trigger("chosen:updated");
            $("#reasonDiv").css("display","none");
        }
    });

    $("#select1").on("change",function (e) {
        var curMapSelect = $(this).val();
        if(curMapSelect!=undefined&&curMapSelect!=null&&curMapSelect!=""){
            var lng = $("#select1 option[value="+curMapSelect+"]").attr("data-lng");
            var lat = $("#select1 option[value="+curMapSelect+"]").attr("data-lat");
            map.clearOverlays();    //清除地图上所有覆盖物
            map.centerAndZoom(new BMap.Point(lng,lat), 18);
        }

    });

    loadMyLChar();

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

function loadMyLChar() {
    //隐藏式搜索 start
    //var myChart = echarts.init(document.getElementById('bar-main'));
    /*var scaleData = [
        {
            'name': '证件临期',
            'value': 3,
            'borderColor': 'yellow',//不使用随机色
            'shadowColor': 'yellow'
        },
        {
            'name': '持证正常',
            'value': 4,
            'borderColor': 'green',//不使用随机色
            'shadowColor': 'green'
        },
        {
            'name': '证件过期',
            'value': 1,
            'borderColor': 'red',//不使用随机色
            'shadowColor': 'red'
        },
    ];*/
    // 随机颜色
    var rich = {
        white: {
            color: '#ddd',
            align: 'center',
            padding: [3, 0]
        }
    };
    var placeHolderStyle = {
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

    /*var data = [];
    for (var i = 0; i < scaleData.length; i++) {
        data.push({
            value: scaleData[i].value,
            name: scaleData[i].name,
            itemStyle: {
                normal: {
                    borderWidth: 25,//模块大小
                    shadowBlur: 50,//阴影到校
                    borderColor: scaleData[i].borderColor,
                    shadowColor: scaleData[i].shadowColor
                    /!*borderColor: color[i],//不适用随即色
                    shadowColor: color[i]//不适用随即色*!/
                }
            }
        }, {
            value: 50,
            name: '',
            itemStyle: placeHolderStyle
        });
    }
    var seriesObj = [{
        name: '',
        type: 'pie',
        clockWise: false,
        radius: [50, 80],//模块到中心的距离
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
                        for (var i = 0; i < scaleData.length; i++) {
                            total += scaleData[i].value;
                        }
                        percent = ((params.value / total) * 100).toFixed(0);
                        if (params.name !== '') {
                            return params.name + '\n{white|' + '占比' + percent + '%}';
                        } else {
                            return '';
                        }
                    },
                    rich: rich
                },
                labelLine: {
                    length: 10,
                    length2: 15,//标记线的长度（模块说明）
                    show: true,
                    color: '#00ffff'
                }
            }
        },
        data: data
    }];
    var option = {
        title: {
            text:'持证经营',
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
            show: false
        },
        legend: {
            selectedMode:false,
            formatter: function(name) {
                var total = 0; //各科正确率总和
                scaleData.forEach(function(value, index, array) {
                    total += value.value;
                });
                return '{total|' + total + '}';
            },
            data: [scaleData[0].name],
            // data: ['高等教育学'],
            // itemGap: 50,
            left: 'center',
            top: '51%',
            icon: 'none',
            align:'center',
            textStyle: {
                color: "#fff",
                fontSize: 12,
                rich: rich
            },
        },
        toolbox: {
            show: false
        },
        series: seriesObj
    }

    myChart.setOption(option);*/
    //隐藏式搜索 start

    //"sdIds":choseSelVal
    $.post("/notarizedCertificate/getNotCerBySdId",{},function(data2){
        if(!isKickOut(data2.code)){
            //没有被踢出
            if(data2.code==1000){
                //所有站点数
                var curTotalCount = data2.data;
                //在线数量饼图 start
                var option5 = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)",
                    },
                    title: {
                        text: "在线"+":"+curTotalCount,
                        left: 'center',
                        top: '43%',
                        padding: [24, 0],
                        textStyle: {
                            color: '#fff',
                            fontSize: 12,
                            align: 'center'
                        }
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
                    series: [{
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
                                        var total = 1;
                                        if(data2.obj!=0){
                                            total = data2.obj;
                                        }
                                        var percent = 0;
                                        percent = accMul(accDiv(params.value,total),100).toFixed(2);

                                        if (params.name !== '') {
                                            return params.name + '\n{white|' + '占比' + percent + '%}';
                                        } else {
                                            return '';
                                        }
                                    },
                                    rich: rich
                                },
                                labelLine: {
                                    length: 10,
                                    length2: 0,//标记线的长度（模块说明）
                                    show: true,
                                    color: 'blue'
                                }
                            }
                        },
                        data: [{
                            value: 6,
                            name: '',
                            itemStyle: {
                                normal: {
                                    borderWidth: 20,//模块大小
                                    shadowBlur: 30,//阴影到校
                                    borderColor: 'green',
                                    shadowColor: 'green'
                                }
                            }
                        }]
                    }]
                };
                myChart2.setOption(option5);
                //在线数量饼图 end

                //营业执照
                var scaleData3 = [
                    {
                        'name': '应传',
                        'value': curTotalCount,
                        'borderColor': 'yellow',//不使用随机色
                        'shadowColor': 'yellow'
                    },
                    {
                        'name': '已传',
                        'value': data2.total,
                        'borderColor': 'green',//不使用随机色
                        'shadowColor': 'green'
                    },
                    {
                        'name': '未传',
                        'value': accSub(curTotalCount,data2.total),
                        'borderColor': 'red',//不使用随机色
                        'shadowColor': 'red'
                    }
                ];
                //许可证
                var scaleData4 = [
                    {
                        'name': '应传',
                        'value': curTotalCount,
                        'borderColor': 'yellow',//不使用随机色
                        'shadowColor': 'yellow'
                    },
                    {
                        'name': '已传',
                        'value': data2.totalPage,
                        'borderColor': 'green',//不使用随机色
                        'shadowColor': 'green'
                    },
                    {
                        'name': '未传',
                        'value': accSub(curTotalCount,data2.totalPage),
                        'borderColor': 'red',//不使用随机色
                        'shadowColor': 'red'
                    }
                ];
                //流通证
                var scaleData6 = [
                    {
                        'name': '应传',
                        'value': curTotalCount,
                        'borderColor': 'yellow',//不使用随机色
                        'shadowColor': 'yellow'
                    },
                    {
                        'name': '已传',
                        'value': data2.obj,
                        'borderColor': 'green',//不使用随机色
                        'shadowColor': 'green'
                    },
                    {
                        'name': '未传',
                        'value': accSub(curTotalCount,data2.obj),
                        'borderColor': 'red',//不使用随机色
                        'shadowColor': 'red'
                    }
                ];

                //营业执照
                var dataArray3 = [];
                for (var i = 0; i < scaleData3.length; i++) {
                    dataArray3.push({
                        value: scaleData3[i].value,
                        name: scaleData3[i].name,
                        itemStyle: {
                            normal: {
                                borderWidth: 60,//模块大小
                                shadowBlur: 20,//阴影到校
                                borderColor: scaleData3[i].borderColor,
                                shadowColor: scaleData3[i].shadowColor
                            }
                        }
                    }, {
                        value: 50,
                        name: '',
                        itemStyle: placeHolderStyle
                    });
                }
                //许可证
                var dataArray4 = [];
                for (var i = 0; i < scaleData4.length; i++) {
                    dataArray4.push({
                        value: scaleData4[i].value,
                        name: scaleData4[i].name,
                        itemStyle: {
                            normal: {
                                borderWidth: 60,//模块大小
                                shadowBlur: 20,//阴影到校
                                borderColor: scaleData4[i].borderColor,
                                shadowColor: scaleData4[i].shadowColor
                            }
                        }
                    }, {
                        value: 50,
                        name: '',
                        itemStyle: placeHolderStyle
                    });
                }
                //流通证
                var dataArray6 = [];
                for (var i = 0; i < scaleData6.length; i++) {
                    dataArray6.push({
                        value: scaleData6[i].value,
                        name: scaleData6[i].name,
                        itemStyle: {
                            normal: {
                                borderWidth: 60,//模块大小
                                shadowBlur: 20,//阴影到校
                                borderColor: scaleData6[i].borderColor,
                                shadowColor: scaleData6[i].shadowColor
                            }
                        }
                    }, {
                        value: 50,
                        name: '',
                        itemStyle: placeHolderStyle
                    });
                }

                //营业执照
                var seriesObj3 = [{
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
                                    var total = 1;
                                    if(curTotalCount!=0){
                                        total = curTotalCount;
                                    }
                                    percent = accMul(accDiv(params.value,total),100).toFixed(2);
                                    if (params.name !== '') {
                                        return params.name + '\n{white|' + '占比' + percent + '%}';
                                    } else {
                                        return '';
                                    }
                                },
                                rich: rich
                            },
                            labelLine: {
                                length: 10,
                                length2: 40,//标记线的长度（模块说明）
                                show: true,
                                color: 'blue'
                            }
                        }
                    },
                    data: dataArray3
                }];
                //许可证
                var seriesObj4 = [{
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
                                    var total = 1;
                                    if(curTotalCount!=0){
                                        total = curTotalCount;
                                    }
                                    percent = accMul(accDiv(params.value,total),100).toFixed(2);
                                    if (params.name !== '') {
                                        return params.name + '\n{white|' + '占比' + percent + '%}';
                                    } else {
                                        return '';
                                    }
                                },
                                rich: rich
                            },
                            labelLine: {
                                length: 10,
                                length2: 40,//标记线的长度（模块说明）
                                show: true,
                                color: 'blue'
                            }
                        }
                    },
                    data: dataArray4
                }];
                //流通证
                var seriesObj6 = [{
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
                                    var total = 1;
                                    if(curTotalCount!=0){
                                        total = curTotalCount;
                                    }
                                    percent = accMul(accDiv(params.value,total),100).toFixed(2);
                                    if (params.name !== '') {
                                        return params.name + '\n{white|' + '占比' + percent + '%}';
                                    } else {
                                        return '';
                                    }
                                },
                                rich: rich
                            },
                            labelLine: {
                                length: 10,
                                length2: 40,//标记线的长度（模块说明）
                                show: true,
                                color: 'blue'
                            }
                        }
                    },
                    data: dataArray6
                }];

                //营业执照
                var option3 = {
                    title: {
                        text:'营业执照',
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
                            return '{total|' + data2.total + '}';
                        },
                        data: [scaleData3[0].name],
                        left: 'center',
                        top: '51%',
                        icon: 'none',
                        align:'center',
                        textStyle: {
                            color: "#fff",
                            fontSize: 12,
                            rich: rich
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
                    series: seriesObj3
                }
                //许可证
                var option4 = {
                    title: {
                        text:'餐饮许可证',
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
                            return '{total|' + data2.totalPage + '}';
                        },
                        data: [scaleData4[0].name],
                        left: 'center',
                        top: '51%',
                        icon: 'none',
                        align:'center',
                        textStyle: {
                            color: "#fff",
                            fontSize: 12,
                            rich: rich
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
                    series: seriesObj4
                }
                //流通证
                var option6 = {
                    title: {
                        text:'流通证',
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
                            return '{total|' + data2.obj + '}';
                        },
                        data: [scaleData6[0].name],
                        left: 'center',
                        top: '51%',
                        icon: 'none',
                        align:'center',
                        textStyle: {
                            color: "#fff",
                            fontSize: 12,
                            rich: rich
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
                    series: seriesObj6
                }

                myChart3.setOption(option3);
                myChart4.setOption(option4);
                myChart6.setOption(option6);

            }else{
                showJqueryConfirmWindow("#icon-shangxin1",data2.msg);
            }
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