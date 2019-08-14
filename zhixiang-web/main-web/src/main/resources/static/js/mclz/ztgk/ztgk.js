var isShow = true;

var initPageplugins = true;

var fruits;
var map;
//var myChart;

var dataViewBottom;
var oldDateBottom = [];

var myChart;

var myChart2;

var msurementChart;
var cleanRecordChart;
var allDayCur;
var disinfectionChart;
var slipperyAlertChart;
var gasaramrChart;
var ratplateAlertChart;
var fromwallAlertChart;
var gasSwitchAlertChart;
var pepoleTempChart;
var lechengCheckRecordChart;
var lechengAptureRecordChart;

var defaultUrl;

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
    addActClass($("#liParent122").children(":eq(0)"));
    $("#liParent122").children(":eq(1)").children(":eq(0)").find("a").css("color","#FFD600");

    $("#topShowLp").css("display","none");

    defaultUrl = $("#responseDate").text().trim();
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
    // 开始初始化直播源地址
    var player1 = new EZUIPlayer('myPlayer1');
    // 开始初始化直播源地址
    var player2 = new EZUIPlayer('myPlayer2');
    // 开始初始化直播源地址
    var player3 = new EZUIPlayer('myPlayer3');
    // 开始初始化直播源地址
    var player4 = new EZUIPlayer('myPlayer4');
    // 开始初始化直播源地址
    var player4 = new EZUIPlayer('myPlayer5');
    // 开始初始化直播源地址
    var player4 = new EZUIPlayer('myPlayer6');
    // 播放
    //player.play();
    // 结束
    //player.stop();

    $('#a1').popover({
        trigger: 'manual',
        html: 'true',
        // 本来这里是打算用funciton提供已生成二维码的DIV，结果获取不到生成的二维码
        content: '<div id="qrcode1" style="width: 100%;"></div>',
        animation: false,
        placement: 'top'
    }).on("mouseenter", function () {
        var self = this;
        $(this).popover("show");
        //设置样式顶部居中
        var a = $("#qrcode1").parent().parent().css("top");
        var c = $("#qrcode1").parent().parent().css("left");
        a = a.split("px")[0];
        c = c.split("px")[0];
        var b = accSub(a,144.6);
        var d = accSub(c,5);
        $("#qrcode1").parent().parent().css("top",b+"px");
        $("#qrcode1").parent().parent().css("left",d+"px");
        // 只有show之后，popover的content才出现在dom中，此时才能生成二维码

        createCode('qrcode1',defaultUrl+'/brightkitchen/getHFivePage','myLogo');
        $(this).siblings(".popover").on("mouseleave", function () {
            $(self).popover('hide');
        });
    }).on("mouseleave", function () {
        var self = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(self).popover("hide")
            }
        }, 100);
    });

    $('#a2').popover({
        trigger: 'manual',
        html: 'true',
        // 本来这里是打算用funciton提供已生成二维码的DIV，结果获取不到生成的二维码
        content: '<div id="qrcode2" style="width: 100%;"></div>',
        animation: false,
        placement: 'top'
    }).on("mouseenter", function () {
        var self = this;
        $(this).popover("show");
        //设置样式顶部居中
        var a = $("#qrcode2").parent().parent().css("top");
        var c = $("#qrcode2").parent().parent().css("left");
        a = a.split("px")[0];
        c = c.split("px")[0];
        var b = accSub(a,144.6);
        var d = accSub(c,5);
        $("#qrcode2").parent().parent().css("top",b+"px");
        $("#qrcode2").parent().parent().css("left",d+"px");
        // 只有show之后，popover的content才出现在dom中，此时才能生成二维码

        createCode('qrcode2',defaultUrl+'/brightkitchen/getHFivePage','myLogo');
        $(this).siblings(".popover").on("mouseleave", function () {
            $(self).popover('hide');
        });
    }).on("mouseleave", function () {
        var self = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(self).popover("hide")
            }
        }, 100);
    });

    $('#a3').popover({
        trigger: 'manual',
        html: 'true',
        // 本来这里是打算用funciton提供已生成二维码的DIV，结果获取不到生成的二维码
        content: '<div id="qrcode3" style="width: 100%;"></div>',
        animation: false,
        placement: 'top'
    }).on("mouseenter", function () {
        var self = this;
        $(this).popover("show");
        //设置样式顶部居中
        var a = $("#qrcode3").parent().parent().css("top");
        var c = $("#qrcode3").parent().parent().css("left");
        a = a.split("px")[0];
        c = c.split("px")[0];
        var b = accSub(a,144.6);
        var d = accSub(c,5);
        $("#qrcode3").parent().parent().css("top",b+"px");
        $("#qrcode3").parent().parent().css("left",d+"px");
        // 只有show之后，popover的content才出现在dom中，此时才能生成二维码

        createCode('qrcode3',defaultUrl+'/brightkitchen/getHFivePage','myLogo');
        $(this).siblings(".popover").on("mouseleave", function () {
            $(self).popover('hide');
        });
    }).on("mouseleave", function () {
        var self = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(self).popover("hide")
            }
        }, 100);
    });

    $('#a4').popover({
        trigger: 'manual',
        html: 'true',
        // 本来这里是打算用funciton提供已生成二维码的DIV，结果获取不到生成的二维码
        content: '<div id="qrcode4" style="width: 100%;"></div>',
        animation: false,
        placement: 'top'
    }).on("mouseenter", function () {
        var self = this;
        $(this).popover("show");
        //设置样式顶部居中
        var a = $("#qrcode4").parent().parent().css("top");
        var c = $("#qrcode4").parent().parent().css("left");
        a = a.split("px")[0];
        c = c.split("px")[0];
        var b = accSub(a,144.6);
        var d = accSub(c,5);
        $("#qrcode4").parent().parent().css("top",b+"px");
        $("#qrcode4").parent().parent().css("left",d+"px");
        // 只有show之后，popover的content才出现在dom中，此时才能生成二维码

        createCode('qrcode4',defaultUrl+'/brightkitchen/getHFivePage','myLogo');
        $(this).siblings(".popover").on("mouseleave", function () {
            $(self).popover('hide');
        });
    }).on("mouseleave", function () {
        var self = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(self).popover("hide")
            }
        }, 100);
    });

    $('#a5').popover({
        trigger: 'manual',
        html: 'true',
        // 本来这里是打算用funciton提供已生成二维码的DIV，结果获取不到生成的二维码
        content: '<div id="qrcode5" style="width: 100%;"></div>',
        animation: false,
        placement: 'top'
    }).on("mouseenter", function () {
        var self = this;
        $(this).popover("show");
        //设置样式顶部居中
        var a = $("#qrcode5").parent().parent().css("top");
        var c = $("#qrcode5").parent().parent().css("left");
        a = a.split("px")[0];
        c = c.split("px")[0];
        var b = accSub(a,144.6);
        var d = accSub(c,5);
        $("#qrcode5").parent().parent().css("top",b+"px");
        $("#qrcode5").parent().parent().css("left",d+"px");
        // 只有show之后，popover的content才出现在dom中，此时才能生成二维码

        createCode('qrcode5',defaultUrl+'/brightkitchen/getHFivePage','myLogo');
        $(this).siblings(".popover").on("mouseleave", function () {
            $(self).popover('hide');
        });
    }).on("mouseleave", function () {
        var self = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(self).popover("hide")
            }
        }, 100);
    });

    $('#a6').popover({
        trigger: 'manual',
        html: 'true',
        // 本来这里是打算用funciton提供已生成二维码的DIV，结果获取不到生成的二维码
        content: '<div id="qrcode6" style="width: 100%;"></div>',
        animation: false,
        placement: 'top'
    }).on("mouseenter", function () {
        var self = this;
        $(this).popover("show");
        //设置样式顶部居中
        var a = $("#qrcode6").parent().parent().css("top");
        var c = $("#qrcode6").parent().parent().css("left");
        a = a.split("px")[0];
        c = c.split("px")[0];
        var b = accSub(a,144.6);
        var d = accSub(c,5);
        $("#qrcode6").parent().parent().css("top",b+"px");
        $("#qrcode6").parent().parent().css("left",d+"px");
        // 只有show之后，popover的content才出现在dom中，此时才能生成二维码
        createCode('qrcode6',defaultUrl+'/brightkitchen/getHFivePage','myLogo');
        $(this).siblings(".popover").on("mouseleave", function () {
            $(self).popover('hide');
        });
    }).on("mouseleave", function () {
        var self = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(self).popover("hide")
            }
        }, 100);
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

    myChart.setOption({
        tooltip: {//提示框，鼠标悬浮交互时的信息提示
            trigger: 'axis',//值为axis显示该列下所有坐标轴对应数据，值为item时只显示该点数据
            textStyle:{
                align:'left'
            }
        },
        legend: { //图例，每个图表最多仅有一个图例
            icon: 'rect', //设置类别样式
            textStyle:{//图例文字的样式
                color:'#fff'
            },
            data: [ '食材快检','清洗浸泡','餐具消毒','地面积水','瓦斯','挡鼠板','三离','煤气开关','体感检测','不戴口罩','不明人员']
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

    msurementChart = dataViewBottom;
    cleanRecordChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    disinfectionChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    slipperyAlertChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    gasaramrChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    ratplateAlertChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    fromwallAlertChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    gasSwitchAlertChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    pepoleTempChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    lechengCheckRecordChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    lechengAptureRecordChart = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    allDayCur = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    var msurementCount = 0;
    var cleanRecordCount = 0;
    var disinfectionCount = 0;
    var slipperyAlertCount = 0;
    var gasaramrCount = 0;
    var ratplateAlertCount = 0;
    var fromwallAlertCount = 0;
    var gasSwitchAlertCount = 0;
    var pepoleTempCount = 0;
    var lechengCheckRecordCount = 0;
    var lechengAptureRecordCount = 0;

    $.post("/earlyWarning/getEWMonthBySdId",{"sdIds":rloadSdIds},function(data){
        if(data.code==1000){
            //刷新数据
            //食材快检
            $.each(data.msurementList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                //由于这里未检测数据，暂定随机数（1-100）
                msurementChart.splice($.inArray(val.createTime,dataViewBottom),1,val.dataCount);
                msurementCount = accAdd(msurementCount,val.dataCount);
            });
            //清洗浸泡
            $.each(data.cleanRecordList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                cleanRecordChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                cleanRecordCount = accAdd(cleanRecordCount,val.dataCount);
            });
            //消毒
            $.each(data.disinfectionList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                disinfectionChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                disinfectionCount = accAdd(disinfectionCount,val.dataCount);
            });
            //地面积水
            $.each(data.slipperyAlertList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                slipperyAlertChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                slipperyAlertCount = accAdd(slipperyAlertCount,val.dataCount);
            });
            //瓦斯
            $.each(data.gasaramrList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                gasaramrChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                gasaramrCount = accAdd(gasaramrCount,val.dataCount);
            });
            //挡鼠板
            $.each(data.ratplateAlertList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                ratplateAlertChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                ratplateAlertCount = accAdd(ratplateAlertCount,val.dataCount);
            });
            //三离
            $.each(data.fromwallAlertList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                fromwallAlertChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                fromwallAlertCount = accAdd(fromwallAlertCount,val.dataCount);
            });
            //煤气开关
            $.each(data.gasSwitchAlertList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                gasSwitchAlertChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                gasSwitchAlertCount = accAdd(gasSwitchAlertCount,val.dataCount);
            });
            //体感检测
            $.each(data.pepoleTempList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                pepoleTempChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                pepoleTempCount = accAdd(pepoleTempCount,val.dataCount);
            });
            //不戴口罩
            $.each(data.lechengCheckRecordList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                lechengCheckRecordChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                lechengCheckRecordCount = accAdd(lechengCheckRecordCount,val.dataCount);
            });
            //不明人员
            $.each(data.lechengAptureRecordList,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                lechengAptureRecordChart.splice($.inArray(val.createTime,allDayCur),1,val.dataCount);
                lechengAptureRecordCount = accAdd(lechengAptureRecordCount,val.dataCount);
            });

            //补充无数据的天数为0
            for(var i=0;i<msurementChart.length;i++){
                //正式环境
                if(msurementChart[i].toString().indexOf("-")>0){
                    msurementChart.splice(i,1,0);
                }
                //正式环境
                if(cleanRecordChart[i].toString().indexOf("-")>0){
                    cleanRecordChart.splice(i,1,0);
                }
                //正式环境
                if(disinfectionChart[i].toString().indexOf("-")>0){
                    disinfectionChart.splice(i,1,0);
                }
                //正式环境
                if(slipperyAlertChart[i].toString().indexOf("-")>0){
                    slipperyAlertChart.splice(i,1,0);
                }
                //正式环境
                if(gasaramrChart[i].toString().indexOf("-")>0){
                    gasaramrChart.splice(i,1,0);
                }
                //正式环境
                if(ratplateAlertChart[i].toString().indexOf("-")>0){
                    ratplateAlertChart.splice(i,1,0);
                }
                //正式环境
                if(fromwallAlertChart[i].toString().indexOf("-")>0){
                    fromwallAlertChart.splice(i,1,0);
                }
                //正式环境
                if(gasSwitchAlertChart[i].toString().indexOf("-")>0){
                    gasSwitchAlertChart.splice(i,1,0);
                }
                //正式环境
                if(pepoleTempChart[i].toString().indexOf("-")>0){
                    pepoleTempChart.splice(i,1,0);
                }
                //正式环境
                if(lechengCheckRecordChart[i].toString().indexOf("-")>0){
                    lechengCheckRecordChart.splice(i,1,0);
                }
                //正式环境
                if(lechengAptureRecordChart[i].toString().indexOf("-")>0){
                    lechengAptureRecordChart.splice(i,1,0);
                }
                //随机假数据，正式后改为上边
                //myNewDateBM.splice(i,1,myRandomData(0,400));
            }
            //不明人员
            var option = {
                series: [{//
                        name: '食材快检',
                        type: 'line',
                        data: msurementChart,
                        itemStyle: {
                            normal: {
                                color:'#c23531'
                            }
                        },
                        markPoint: {  //系列中的数据标注内容
                            data: [{
                                type: 'max',
                                name: '最大值'
                            },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                        }/*,
                        markLine: {//系列中的数据标线内容
                            data: [{
                                type: 'average',
                                name: '平均值'
                            }]
                        }*/
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
                    name: '清洗浸泡',
                    type: 'line',
                    data: cleanRecordChart,
                    itemStyle: {
                        normal: {
                            color:'gold'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '餐具消毒',
                    type: 'line',
                    data: disinfectionChart,
                    itemStyle: {
                        normal: {
                            color:'#5b9097'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '地面积水',
                    type: 'line',
                    data: slipperyAlertChart,
                    itemStyle: {
                        normal: {
                            color:'#d48265'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '瓦斯',
                    type: 'line',
                    data: gasaramrChart,
                    itemStyle: {
                        normal: {
                            color:'#91c7ae'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '挡鼠板',
                    type: 'line',
                    data: ratplateAlertChart,
                    itemStyle: {
                        normal: {
                            color:'#749f83'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '三离',
                    type: 'line',
                    data: fromwallAlertChart,
                    itemStyle: {
                        normal: {
                            color:'#ca8622'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '煤气开关',
                    type: 'line',
                    data: gasSwitchAlertChart,
                    itemStyle: {
                        normal: {
                            color:'#bda29a'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '体感检测',
                    type: 'line',
                    data: pepoleTempChart,
                    itemStyle: {
                        normal: {
                            color:'#6e7074'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '不戴口罩',
                    type: 'line',
                    data: lechengCheckRecordChart,
                    itemStyle: {
                        normal: {
                            color:'#546570'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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
                    name: '不明人员',
                    type: 'line',
                    data: lechengAptureRecordChart,
                    itemStyle: {
                        normal: {
                            color:'#c4ccd3'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    }/*,
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }*/
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

    var curIndex = $.inArray(param.name,allDayCur);
    var msurement = msurementChart[curIndex];
    var cleanRecord = cleanRecordChart[curIndex];
    var disinfection = disinfectionChart[curIndex];
    var slipperyAlert = slipperyAlertChart[curIndex];
    var gasaramr = gasaramrChart[curIndex];
    var ratplateAlert = ratplateAlertChart[curIndex];
    var fromwallAlert = fromwallAlertChart[curIndex];
    var gasSwitchAlert = gasSwitchAlertChart[curIndex];
    var pepoleTemp = pepoleTempChart[curIndex];
    var lechengCheckRecord = lechengCheckRecordChart[curIndex];
    var lechengAptureRecord = lechengAptureRecordChart[curIndex];

    if(param.seriesId!=null){
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
        /*!//有票据数
                resultBean.setData(billTotal);
                //有卫生证数
                resultBean.setTotal(healthCertificateTotal);
                //有合格证数
                resultBean.setTotalPage(certificateTotal);*/
        var scaleData2 = [
            {
                'name': '食材快检',
                'value': msurement,
                'borderColor': '#c23531',//不使用随机色
                'shadowColor': '#c23531'
            },
            {
                'name': '清洗浸泡',
                'value': cleanRecord,
                'borderColor': 'gold',//不使用随机色
                'shadowColor': 'gold'
            },
            {
                'name': '餐具消毒',
                'value': disinfection,
                'borderColor': '#5b9097',//不使用随机色
                'shadowColor': '#5b9097'
            },
            {
                'name': '地面积水',
                'value': slipperyAlert,
                'borderColor': '#d48265',//不使用随机色
                'shadowColor': '#d48265'
            },
            {
                'name': '瓦斯',
                'value': gasaramr,
                'borderColor': '#91c7ae',//不使用随机色
                'shadowColor': '#91c7ae'
            },
            {
                'name': '挡鼠板',
                'value': ratplateAlert,
                'borderColor': '#749f83',//不使用随机色
                'shadowColor': '#749f83'
            },
            {
                'name': '三离',
                'value': fromwallAlert,
                'borderColor': '#ca8622',//不使用随机色
                'shadowColor': '#ca8622'
            },
            {
                'name': '煤气开关',
                'value': gasSwitchAlert,
                'borderColor': '#bda29a',//不使用随机色
                'shadowColor': '#bda29a'
            },
            {
                'name': '体感检测',
                'value': pepoleTemp,
                'borderColor': '#6e7074',//不使用随机色
                'shadowColor': '#6e7074'
            },
            {
                'name': '不戴口罩',
                'value': lechengCheckRecord,
                'borderColor': '#546570',//不使用随机色
                'shadowColor': '#546570'
            },
            {
                'name': '不明人员',
                'value': lechengAptureRecord,
                'borderColor': '#c4ccd3',//不使用随机色
                'shadowColor': '#c4ccd3'
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
                text:param.name+'-模块报警占比',
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

        //由于这里已有数据，故不从数据库查询
        /*$.post("/cableTicketCard/getCTCByDateSdId",{"sdIds":choseSelVal,"exitDate":param.name},function(data2){
            if(!isKickOut(data2.code)){
                //没有被踢出
                if(data2.code==1000){
                }else{
                    showJqueryConfirmWindow("#icon-shangxin1",data2.msg);
                }
            }
        });*/

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