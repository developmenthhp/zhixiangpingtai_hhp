var isShow = true;

var initPageplugins = true;

var fruits;
var map;
//var myChart;

var dataViewBottom;
var oldDateBottom = [];

var myChart;
var myChart2;
var myNewDateBM;
var allDayCur;

var ajaxSCJCXXSearchFormOption = {
    //target: '#output', //把服务器返回的内容放入id为output的元素中
    beforeSubmit: showSCJCXXSearchRequest, //提交前的回调函数
    success: showSCJCXXSearchResponse, //提交后的回调函数
    url: "/standardWear/getSWCharBySdId", //默认是form的action， 如果申明，则会覆盖
    type: "post", //默认是form的method（get or post），如果申明，则会覆盖
    data:{},
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
    $("#liParent116").children(":eq(1)").children(":eq(0)").children(":eq(0)").trigger("click");
    $("#liParent116").children(":eq(1)").children(":eq(0)").children(":eq(1)").children(":eq(0)").find("a").css("color","#FFD600");

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

    myChart.on("click", function (param){
        reloadDetailChar(param)
    });

    myChart2 = echarts.init(document.getElementById('pie-main'));

    //不需要submit按钮，可以是任何元素的click事件
    $("#searchFormI").click(function () {
        var selectYear = $("#selectYear").val();
        var selectMonth = $("#selectMonth").val();
        if((selectYear==null||selectYear=='')&&(selectMonth==null||selectMonth=='')){
            myNewDateBM = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
            allDayCur = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
            dataViewBottom = allDayCur;
            //为空，未选择时间，默认当前年当前月
            //ajaxSCJCXXSearchFormOption.data={yearMonth:'',sdIds:choseSdIds};
            $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
            return false;
        }else if(selectYear!=null&&selectYear!=''&&selectMonth!=null&&selectMonth!=''){
            myNewDateBM = get_day(selectYear+'-'+selectMonth,'2');
            allDayCur = get_day(selectYear+'-'+selectMonth,'2');
            dataViewBottom = allDayCur;
            //ajaxSCJCXXSearchFormOption.data={yearMonth:selectYear+'-'+selectMonth,sdIds:choseSdIds};
            $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
            return false;
        }else{
            //提示需要选择时间
            showJqueryConfirmWindow('#icon-tishi1',"请选择某一年下的月份");
        }
        return false;
    });

    //<select multiple class="selectator" data-selectator-keep-open="true"> 持续打开
    $("#select1").on("change",function (e) {
        var curMapSelect = $(this).val();
        //设置选择的站点
        $("#alertSdId").val(curMapSelect);
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

    myChart.setOption({
        tooltip: {//提示框，鼠标悬浮交互时的信息提示
            trigger: 'axis'//值为axis显示该列下所有坐标轴对应数据，值为item时只显示该点数据
        },
        legend: { //图例，每个图表最多仅有一个图例
            icon: 'rect', //设置类别样式
            textStyle:{//图例文字的样式
                color:'#fff'
            },
            data: [ '不戴口罩']
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

    myNewDateBM = dataViewBottom;
    allDayCur = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    loadUserList();
    //监听模态框关闭时间 监听开启事件shown.bs.modal
    $("#compose-modal").on('hide.bs.modal', function (){
        //dataViewBottom = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
        /*var choseSelVal = $('#select1').val();
        if(choseSelVal!=undefined&&choseSelVal!=null&&choseSelVal!=''){
            loadMyLChar(choseSelVal.toString());
        }else{
            loadMyLChar('');
        }*/
        var selectYear = $("#selectYear").val();
        var selectMonth = $("#selectMonth").val();
        if((selectYear==null||selectYear=='')&&(selectMonth==null||selectMonth=='')){
            myNewDateBM = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
            allDayCur = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
            dataViewBottom = allDayCur;
            //为空，未选择时间，默认当前年当前月
            //ajaxSCJCXXSearchFormOption.data={yearMonth:'',sdIds:choseSdIds};
            $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
        }else if(selectYear!=null&&selectYear!=''&&selectMonth!=null&&selectMonth!=''){
            myNewDateBM = get_day(selectYear+'-'+selectMonth,'2');
            allDayCur = get_day(selectYear+'-'+selectMonth,'2');
            dataViewBottom = allDayCur;
            //ajaxSCJCXXSearchFormOption.data={yearMonth:selectYear+'-'+selectMonth,sdIds:choseSdIds};
            $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
        }else{
            //提示需要选择时间
            showJqueryConfirmWindow('#icon-tishi1',"请选择某一年下的月份");
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
    //初始化年份选择
    setYearChosen("selectYear",new Date().getFullYear());
    //初始化月份下拉插件
    $("#selectMonth").selectator({
        labels: {
            search: '请输入月份'
        },
        showAllOptionsOnFocus: true//初始化复选
    });
    $("#selectMonth").next().css("width","100%");
});

function showSCJCXXSearchRequest(formData, jqForm, options){
    var option = {
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
        }]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    return true; //只要不返回false，表单都会提交,在这里可以对表单元素进行验证
};
function showSCJCXXSearchResponse(data, status){
    if(!isKickOut(data.code)){
        var bdkCount = 0;
        if(data.code==1000){
            //刷新数据
            //不戴口罩
            $.each(data.rows,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                myNewDateBM.splice($.inArray(val.createTime,dataViewBottom),1,val.dataCount);
                bdkCount = accAdd(bdkCount,val.dataCount);
            });

            for(var i=0;i<myNewDateBM.length;i++){
                if(myNewDateBM[i].toString().indexOf("-")>0){
                    myNewDateBM.splice(i,1,0);
                }
            }
            //不明人员
            var option = {
                series: [{
                    name: '不戴口罩',
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

            // 这里合格及不合格，由于抓拍未分人员，故不能进行合格及不合格分析
            /*var rich2 = {
                white: {
                    color: '#ddd',
                    align: 'center',
                    padding: [3, 0]
                }
            };
            //饼图加载

            var option5 = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)",

                },
                /!*graphic: {
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
                },*!/
                title: {
                    text: "标准穿戴管理"+":"+bdkCount,
                    left: 'center',
                    top: '43%',
                    padding: [24, 0],
                    textStyle: {
                        color: '#fff',
                        fontSize: 12,
                        align: 'center'
                    }
                },
                /!*legend: {
                    selectedMode:false,
                    formatter: function(name) {
                        return '{total|' + bdkCount + '}';
                    },
                    data: ['不合格','合格'],
                    left: 'center',
                    top: '51%',
                    icon: 'none',
                    align:'center',
                    textStyle: {
                        color: "#fff",
                        fontSize: 12,
                        rich: rich2
                    },
                },*!/
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
                                    if(bdkCount==0){
                                        percent = accMul(accDiv(params.value,1),100).toFixed(2);
                                    }else{
                                        percent = accMul(accDiv(params.value,bdkCount),100).toFixed(2);
                                    }

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
                        value: bdkCount,
                        name: '不合格',
                        itemStyle: {
                            normal: {
                                borderWidth: 20,//模块大小
                                shadowBlur: 85,//阴影到校
                                borderColor: 'red',
                                shadowColor: 'red'
                            }
                        }
                    },
                        {
                            value: 0,
                            name: '合格',
                            itemStyle: {
                                normal: {
                                    borderWidth: 20,//模块大小
                                    shadowBlur: 85,//阴影到校
                                    borderColor: 'red',
                                    shadowColor: 'red'
                                }
                            }
                        }]
                }]
            };

            myChart2.setOption(option5);*/
        }
    }
}

function openMap() {
    map = showMap(map,'compose-modal');
}

function loadUserList(){
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}

function reloadDetailChar(param) {

    var choseSelVal = $('#select1').val();
    if(choseSelVal==undefined){
        choseSelVal = '';
    }else{
        choseSelVal = choseSelVal.toString();
    }
    var curIndex = $.inArray(param.name,allDayCur);
    var normal = myNewDateBM[curIndex];
    if(param.seriesId!=null){
        // 这里合格及不合格，由于抓拍未分人员，故不能进行合格及不合格分析
        var rich2 = {
            white: {
                color: '#ddd',
                align: 'center',
                padding: [3, 0]
            }
        };
        //饼图加载
        var option5 = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)",

            },
            title: {
                text: "不戴口罩"+":"+normal,
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
                                var percent = 0;
                                if(normal==0){
                                    percent = accMul(accDiv(params.value,1),100).toFixed(2);
                                }else{
                                    percent = accMul(accDiv(params.value,normal),100).toFixed(2);
                                }

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
                    value: normal,
                    name: '不戴口罩',
                    itemStyle: {
                        normal: {
                            borderWidth: 20,//模块大小
                            shadowBlur: 85,//阴影到校
                            borderColor: 'red',
                            shadowColor: 'red'
                        }
                    }
                }]
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