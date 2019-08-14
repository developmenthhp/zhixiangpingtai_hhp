var isShow = true;

var initPageplugins = true;

var fruits;
var map;
var myChart;
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
    $("#liParent111").children(":eq(1)").children(":eq(1)").find("a").css("color","#FFD600");

    $("#topShowLp").css("display","none");
    //writeTopLibPur();

    myChart = echarts.init(document.getElementById('bar-main'));

    //添加点击事件（单击），还有其他鼠标事件和键盘事件等等
    myChart.on("click", function (param){
        reloadDetailChar(param)
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

    loadMyLChar('');

    //监听模态框关闭时间 监听开启事件shown.bs.modal
    $("#compose-modal").on('hide.bs.modal', function (){
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
};
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}

function openMap() {
    map = showMap(map,'compose-modal');
}

function loadMyLChar(rloadSdIds) {
    $.post("/health/getHealthCharBySdId",{"sdIds":rloadSdIds},function(data){
        if(!isKickOut(data.code)){
            //没有被踢出
            if(data.code==1000){

                var scaleData = [
                    {
                        'name': '已有健康证',
                        'value': data.data,
                        'borderColor': 'green',//不使用随机色
                        'shadowColor': 'green'
                    },
                    {
                        'name': '未传健康证',
                        'value': accSub(data.total,data.data),
                        'borderColor': 'red',//不使用随机色
                        'shadowColor': 'red'
                    }
                ];
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

                var dataArray = [];
                for (var i = 0; i < scaleData.length; i++) {
                    dataArray.push({
                        value: scaleData[i].value,
                        name: scaleData[i].name,
                        itemStyle: {
                            normal: {
                                borderWidth: 60,//模块大小
                                shadowBlur: 85,//阴影到校
                                borderColor: scaleData[i].borderColor,
                                shadowColor: scaleData[i].shadowColor
                                /*borderColor: color[i],//不适用随即色
                                shadowColor: color[i]//不适用随即色*/
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
                                    percent = accMul(accDiv(params.value,data.total),100).toFixed(2);
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
                    data: dataArray
                }];
                var option = {
                    title: {
                        text:'从业人员',
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
                        data: [scaleData[0].name],
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
                    series: seriesObj
                }

                myChart.setOption(option);

            }else{
                showJqueryConfirmWindow("#icon-shangxin1",data.msg);
            }
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
    var rich2 = {
        white: {
            color: '#ddd',
            align: 'center',
            padding: [3, 0]
        }
    };
    // 健康证总数  param.value
    var url = '';
    if(param.name=="已有健康证"){
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

        if(param.value==0){
            var scaleData2 = [
                {
                    'name': '正常健康证',
                    'value': 0,
                    'borderColor': 'green',//不使用随机色
                    'shadowColor': 'green'
                },
                {
                    'name': '临期健康证',
                    'value': 0,
                    'borderColor': 'yellow',//不使用随机色
                    'shadowColor': 'yellow'
                },
                {
                    'name': '过期健康证',
                    'value': 0,
                    'borderColor': 'red',//不使用随机色
                    'shadowColor': 'red'
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
                                percent = accMul(accDiv(params.value,1),100).toFixed(2);
                                if (params.name !== '') {
                                    return params.name + '\n{white|' + '占比' + percent + '%}';
                                } else {
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
                    text:'已有健康证',
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
                        return '{total|' + param.value + '}';
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
        }else{
            url = "/health/getHealthTypeCharBySdId";

            $.post(url,{"sdIds":choseSelVal},function(data2){
                if(!isKickOut(data2.code)){
                    //没有被踢出
                    if(data2.code==1000){
                        var scaleData2 = [
                            {
                                'name': '正常健康证',
                                'value': data2.data,
                                'borderColor': 'green',//不使用随机色
                                'shadowColor': 'green'
                            },
                            {
                                'name': '临期健康证',
                                'value': accSub(param.value,accAdd(data2.data,data2.total)),
                                'borderColor': 'yellow',//不使用随机色
                                'shadowColor': 'yellow'
                            },
                            {
                                'name': '过期健康证',
                                'value': data2.total,
                                'borderColor': 'red',//不使用随机色
                                'shadowColor': 'red'
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
                                            percent = accMul(accDiv(params.value,param.value),100).toFixed(2);
                                            if (params.name !== '') {
                                                return params.name + '\n{white|' + '占比' + percent + '%}';
                                            } else {
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
                                text:'已有健康证',
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
                                    return '{total|' + param.value + '}';
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

                    }else{
                        showJqueryConfirmWindow("#icon-shangxin1",data2.msg);
                    }
                }
            });
        }



    }else if(param.name=="未传健康证"){
        //各站点占比，站点可能过多不好看，故只显示未传健康证
        //url = "/health/getHealthCharBySdId";
        var myChart2 = echarts.init(document.getElementById('pie-main'));
        //var giftImageUrl = "data:image/png;base64,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";
        var option5 = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)",

            },
            /*graphic: {
                elements: [{
                    type: 'image',
                    style: {
                        image: giftImageUrl,
                        width: 100,
                        height: 100
                    },
                    left: 'center',
                    top: 'center'
                }]
            },*/
            title: {
                text: param.name+":"+param.value,
                left: 'center',
                top: '43%',
                padding: [24, 0],
                textStyle: {
                    color: '#fff',
                    fontSize: 12,
                    align: 'center'
                }
            },
            legend: {
                selectedMode:false,
                formatter: function(name) {
                    return '{total|' + param.value + '}';
                },
                data: param.name,
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
                                var percent = 0;
                                percent = accMul(accDiv(param.value,param.value),100).toFixed(2);
                                if (params.name !== '') {
                                    return params.name + '\n{white|' + '占比' + percent + '%}';
                                } else {
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
                data: [{
                    value: param.value,
                    name: '未传健康证',
                    itemStyle: {
                        normal: {
                            borderWidth: 20,//模块大小
                            shadowBlur: 85,//阴影到校
                            borderColor: 'red',
                            shadowColor: 'red'
                        }
                    }
                }
                ]
            }]
        };

        myChart2.setOption(option5);
    }
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