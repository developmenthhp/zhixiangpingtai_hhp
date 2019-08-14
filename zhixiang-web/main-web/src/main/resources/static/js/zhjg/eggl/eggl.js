var isShow = true;

var initPageplugins = true;

var fruits;
var map;
//var myChart;

var dataViewBottom;
var oldDateBottom = [];

var myChart;

var myChart2;

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
    $("#liParent116").children(":eq(1)").children(":eq(0)").children(":eq(0)").trigger("click");
    $("#liParent116").children(":eq(1)").children(":eq(0)").children(":eq(1)").children(":eq(2)").find("a").css("color","#FFD600");

    $("#topShowLp").css("display","none");
    //writeTopLibPur();

    //myChart = echarts.init(document.getElementById('bar-main'));

    //添加点击事件（单击），还有其他鼠标事件和键盘事件等等
    /*myChart.on("click", function (param){
        reloadDetailChar(param)
    });*/

    $(".mySpecialUlUnit").removeAttr("left");

    /*实例化编辑器 */
    /*var um = UE.getEditor('myEditor');*/

    myChart = echarts.init(document.getElementById('bar-main'));

    myChart2 = echarts.init(document.getElementById('pie-main'));

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
};
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}

function openMap() {
    map = showMap(map,'compose-modal');
}

function loadMyLChar(rloadSdIds) {

    myChart.setOption({
        tooltip: {//提示框，鼠标悬浮交互时的信息提示
            trigger: 'axis'//值为axis显示该列下所有坐标轴对应数据，值为item时只显示该点数据
        },
        legend: { //图例，每个图表最多仅有一个图例
            icon: 'rect', //设置类别样式
            textStyle:{//图例文字的样式
                color:'#fff'
            },
            data: [ '违规二更','合格二更']
        },
        toolbox: {
            show : true,
            y: 'bottom',
            feature : {
                mark : {
                    show: true
                },
                dataView : {//数据视图
                    show: true,
                    readOnly: false
                },
                magicType : {//切换图表
                    show: true,
                    type: ['line', 'bar', 'stack', 'tiled']
                },
                restore : {//还原原始图表
                    show: true
                },
                saveAsImage : {//保存图片
                    show: true
                }
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
                magicType: {//切换图表
                    show: true,
                    type: ['line', 'bar', 'stack', 'tiled'],//图表
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
        calculable: true,//是否启用拖拽重计算特性
        xAxis: [{
            type: 'category',  //坐标轴类型，横轴默认为类目型'category'
            boundaryGap: false,
            axisLabel: {
                show: true,
                textStyle: {
                    color: '#fff'   //这里用参数代替了
                }
            },
            data: dataViewBottom//数据项
        }],
        yAxis: [{
            type: 'value', //坐标轴类型，纵轴默认为数值型'value'
            axisLabel: {
                //formatter: '{value} °C', //加上单位
                formatter: '{value}', //加上单位
                show: true,
                textStyle: {
                    color: '#fff'   //这里用参数代替了
                }
            }
        }],
        dataZoom: [
            {
                id: 'dataZoomX',
                type: 'slider',
                xAxisIndex: [0],
                filterMode: 'filter'
            }/*,
            {
                id: 'dataZoomY',
                type: 'slider',
                yAxisIndex: [0],
                filterMode: 'empty'
            }*/
        ]
    });

    var myNewDateBM = dataViewBottom;
    var myNewDateBDK = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    var allDayCur = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    var bmCount = 0;

    var bdkCount = 0;
    var somtempStart = $("#somtempStart").val();
    if(somtempStart==null||somtempStart==''){
        somtempStart = 37.2;
    }

    $.post("/secondWear/getSWearBySdId",{"sdIds":rloadSdIds,"somtempStart":somtempStart},function(data){
        if(data.code==1000){
            //刷新数据
            //二更不合格
            $.each(data.rows,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）

                //myNewDateBM.splice($.inArray(val.createTime,dataViewBottom),1,val.dataCount);
                var curRamDom = myRandomData(1,5);//由于二更未接入，先使用后台加随机数
                myNewDateBM.splice($.inArray(val.createTime,dataViewBottom),1,accAdd(curRamDom,val.dataCount));
                //bdkCount = accAdd(bdkCount,val.dataCount);
                bdkCount = accAdd(curRamDom,accAdd(bdkCount,val.dataCount));
            });
            //二更合格
            $.each(data.obj,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）

                myNewDateBDK.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                var curRamDom = myRandomData(1.5);//由于二更未接入，先使用后台加随机数
                //myNewDateBDK.splice($.inArray(val.createTime,allDayCur),accAdd(curRamDom,1,val.dataCount));
                //bmCount = accAdd(bmCount,val.dataCount);
                bmCount = accAdd(curRamDom,accAdd(bmCount,val.dataCount));
            });


            //补充无数据的天数为0
            for(var i=0;i<myNewDateBM.length;i++){
                if(myNewDateBM[i].toString().indexOf("-")>0){
                    myNewDateBM.splice(i,1,0);
                }
                if(myNewDateBDK[i].toString().indexOf("-")>0){
                    myNewDateBDK.splice(i,1,0);
                }

            }

            //不明人员
            var option = {
                series: [{//
                        name: '合格二更',
                        type: 'line',
                        data: myNewDateBDK,
                        markPoint: {  //系列中的数据标注内容
                            data: [{
                                type: 'max',
                                name: '最大值'
                            },
                                {
                                    type: 'min',
                                    name: '最小值'
                                }]
                        },
                        markLine: {//系列中的数据标线内容
                            data: [{
                                type: 'average',
                                name: '平均值'
                            }]
                        }
                        /*markPoint: {
                            data: [{
                                name: '周最低',
                                value: -2,
                                xAxis: 1,
                                yAxis: -1.5
                            }]
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值'
                            }]
                        }*/
                    },{
                    name: '违规二更',
                    type: 'line',
                    data: myNewDateBM,
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    },
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }
                    /*markPoint: {
                        data: [{
                            name: '周最低',
                            value: -2,
                            xAxis: 1,
                            yAxis: -1.5
                        }]
                    },
                    markLine: {
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);

            var scaleData2 = [
                {
                    'name': '二更合格',
                    'value': data.data,
                    'borderColor': 'green',//不使用随机色
                    'shadowColor': 'green'
                },
                {
                    'name': '违规二更',
                    'value': data.total,
                    'borderColor': 'red',//不使用随机色
                    'shadowColor': 'red'
                },
                {
                    'name': '未二更',
                    'value': accSub(data.totalPage,accAdd(data.data,data.total)),
                    'borderColor': 'yellow',//不使用随机色
                    'shadowColor': 'yellow'
                }
            ];
            // 随机颜色
            var rich2 = {
                white: {
                    color: '#ddd',
                    align: 'center',
                    padding: [3, 0]
                }
            };
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
                            /*borderColor: color[i],//不适用随即色
                            shadowColor: color[i]//不适用随即色*/
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
                                percent = accMul(accDiv(params.value,data.totalPage),100).toFixed(2);
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
                    text:'二更日分析图',
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
                        return '{total|' + data.totalPage + '}';
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