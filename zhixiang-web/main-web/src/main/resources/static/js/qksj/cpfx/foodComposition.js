var isShow = true;

var initPageplugins = true;

var fruits;
var map;

var myChart2;

var ajaxSCJCXXSearchFormOption = {
    //target: '#output', //把服务器返回的内容放入id为output的元素中
    beforeSubmit: showSCJCXXSearchRequest, //提交前的回调函数
    success: showSCJCXXSearchResponse, //提交后的回调函数
    url: "/bioFood/getAllIngBBySdId", //默认是form的action， 如果申明，则会覆盖
    type: "post", //默认是form的method（get or post），如果申明，则会覆盖
    data:{},
    dataType: "json",//html(默认), xml, script, json...接受服务端返回的类型
    //clearForm: true,   //成功提交后，清除所有表单元素的值
    //resetForm: true,  //成功提交后，重置所有表单元素的值
    timeout: 3000 //限制请求的时间，当请求大于3秒后，跳出请求
};

$(function(){

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
        /*if(choseSelVal!=undefined&&choseSelVal!=null&&choseSelVal!=''){
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

    var scaleData2 = [];
    var textResultBean = $("#responseDate").text();

    var stepData = JSON.parse(textResultBean);
    /*stepData.push({
        'ingredientName': "酸菜",
        'metering': 22.5,
        'unitPrice': 22.7
    },{
        'ingredientName': "葱",
        'metering': 12.7,
        'unitPrice': 22.7
    },{
        'ingredientName': "姜",
        'metering': 10.3,
        'unitPrice': 22.7
    },{
        'ingredientName': "蒜",
        'metering': 7.4,
        'unitPrice': 22.7
    },{
        'ingredientName': "牛油果  ",
        'metering': 3.2,
        'unitPrice': 22.7
    });*/
    var color = rancolors(7);
    if(stepData){
        $.each(stepData,function (ind,val) {
            scaleData2.push({
                'name': val.ingredientName,
                'value': val.metering,
                'borderColor':color[ind],
                'shadowColor':color[ind]
            });
        });
    }

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
            text:'BOM用量占比',
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