var isShow = true;

var initPageplugins = true;

var fruits;
var map;
//var myChart;

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
    $("#liParent116").children(":eq(1)").children(":eq(2)").children(":eq(0)").trigger("click");
    $("#liParent116").children(":eq(1)").children(":eq(2)").children(":eq(1)").children(":eq(1)").find("a").css("color","#FFD600");

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
        dataViewBottom = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
        var choseSelVal = $('#select1').val();
        if(choseSelVal!=undefined&&choseSelVal!=null&&choseSelVal!=''){
            loadMyLChar(choseSelVal.toString());
        }else{
            loadMyLChar('');
        }

        $('#select1').selectator('destroy');
    });
    var socket =  io.connect('http://121.43.35.181:4001', {
        transports:['websocket']
    });

    socket.on('connect', function() {
        console.log('http://121.43.35.181:4001');
        console.log("连接成功！");
        socket.emit('ratAlarm', "ratAlarm");
        console.log('<span class="connect-msg">Client has connected to the server!</span>');
    });

    socket.on('disconnect', function() {
        console.log('<span class="disconnect-msg">The client has disconnected!</span>');
    });

    socket.on('ratAlarm', function(data) {
        if(data!=null||data!=""){
            $("#lightGif").css("display","block");
            $("#alertTitle").css("display","block");
            $("#myAlertText").text(data);
        }else{
            $("#lightGif").css("display","none");
            $("#alertTitle").css("display","none");
            $("#myAlertText").text("");
        }
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
    $.post("/ratGuard/getRGAlertPieBySdId",{"sdIds":rloadSdIds},function(data){
        if(data.code==1000){
            //刷新数据
            //站点挡鼠板报警次数排行
            $("#rankingList").html("");
            if(data.rows){
                var newData=data.rows;
                //newData[0].warningTotal = 122;
                //根据价格（price）排序
                function sortprice(a,b){
                    return a.warningTotal-b.warningTotal
                }
                //利用js中的sort方法
                newData.sort(sortprice);
                //打印排序后的数据到控制台
                //console.log(newdata);

                $.each(newData,function (ind,val) {
                    //进行站点排行
                    $("#rankingList").append("<div class='' style='border-left:0;'>" +
                        /*"<i class='fa fa-warning'></i>" +*/
                        "<a href='javascript:void(0);' class='aui-flex'>" +
                        "<div class='aui-goods-img'><img src='"+val.photo+"' alt=''>" +
                        "<span class='aui-goods-top aui-goods-top-img'>" +
                        /*"<img src='../../images/gaojing.gif' alt=''>" +*/
                        /*"<i class='fa fa-warning'></i>" +*/
                        "</span>" +
                        "</div>" +
                        "<div class='aui-flex-box'>" +
                        "<h4>"+val.name+"</h4>" +
                        /*"<p style='font-size: 0.8em;color: #b8b8b8;'>-当前排行由智飨科技提供-</p>" +*/
                        "<span>" +
                        "<em><i class='aui-hot-tag'></i>预警总计:"+val.warningTotal+"次</em>" +
                        /*"<em>6.2折 | 劵</em>" +
                        "</span>" +
                        "<div class='aui-flex aui-flex-clear'><em class='aui-flex-box'>" +
                        "<i class='aui-goods-price'>￥399</i><i>￥599</i></em><em>100%好评</em>" +*/

                        "</span>" +
                        "<div class='aui-flex aui-flex-clear'><em class='aui-flex-box'>" +
                        "<i class='aui-goods-price' style='font-size: 0.8em;color: #b8b8b8;'>-当前排行由智飨科技提供-</i></em><em>排名："+accAdd(ind,1)+"</em>" +
                        "</div></div></a></div>");
                });
            }

            var scaleData2 = [
                {
                    'name': '挡鼠板报警数',
                    'value': data.data,
                    'borderColor': 'green',//不使用随机色
                    'shadowColor': 'green'
                },
                {
                    'name': '挡鼠板正常数',
                    'value': data.totalPage,
                    'borderColor': 'red',//不使用随机色
                    'shadowColor': 'red'
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
                                var total;
                                if(data.total==0){
                                    total = 1;
                                }else{
                                    total = data.total;
                                }
                                percent = accMul(accDiv(params.value,total),100).toFixed(2);
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
                    text:'已装站点数',
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