var isShow = true;

var limCount = 10;
var allDataTol;

var initPageplugins = true;

var fruits;
var map;

var dataViewBottom;
var oldDateBottom = [];

var myChart;

var myChart2;

var myNewDateBM;
var myNewDateBDK;
var allDayCur;

var searchHelper;

var droploader = null;

var curPag = 0;

var allPage = 0;

var pm = 0;

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
    addActClass($("#liParent136").children(":eq(0)"));
    $("#liParent136").children(":eq(1)").children(":eq(1)").find("a").css("color","#FFD600");

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
            $('div.index', ui.item.parent()).each(function(i) {
                //html() 方法返回或设置被选元素的内容 (inner HTML)
                $(this).html(i + 1);
            });
        };
    $("#rankingList").sortable({
        //设置是否在拖拽元素时，显示一个辅助的元素。可选值：'original', 'clone'
        helper: fixHelperModified,
        //当排序动作结束时触发此事件。
        stop: updateIndex
    }).disableSelection();

    $("i").tooltip();

    $(".mySpecialUlUnit").removeAttr("left");

    $("#select1").on("change",function (e) {
        var curMapSelect = $(this).val();
        //赋值搜索框sdId
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

    //监听模态框关闭时间 监听开启事件shown.bs.modal
    $("#compose-modal").on('hide.bs.modal', function (){
        $('#select1').selectator('destroy');
    });

    //初始化日期插件
    $('#timeQuantum').daterangepicker(null, function(start, end, label) {
    });

    searchHelper = {
        droploader: null,
        params: {
            page: 1,
            limit: 10,
            sdIds : $("input[name='sdIds']").val(),
            type : $("input[name='type']").val(),
            timeQuantum : $("input[name='timeQuantum']").val()
        },
        init: function() {
            var me = searchHelper;
            me.droploader = $('.dataArea').dropload({
                scrollArea : window,
                domUp : {
                    domClass   : 'dropload-up',
                    // 滑动到底部显示内容
                    domRefresh : '<div class="dropload-refresh">智飨平台提示:↑下拉刷新</div>',
                    // 内容加载过程中显示内容
                    domLoad    : '<div class="dropload-load"><span class="loading"></span>智飨平台提示:○营养分析数据刷新中...</div>',
                    // 没有更多内容-显示提示
                    domUpdate  : '<div class="dropload-noData">智飨平台提示:↑释放更新</div>'
                },
                domDown : {
                    domClass   : 'dropload-down',
                    // 滑动到底部显示内容
                    domRefresh : '<div class="dropload-refresh">智飨平台提示:↑上拉加载更多</div>',
                    // 内容加载过程中显示内容
                    domLoad    : '<div class="dropload-load"><span class="loading"></span>智飨平台提示:○营养分析数据加载中...</div>',
                    // 没有更多内容-显示提示
                    domNoData  : '<div class="dropload-noData">智飨平台提示:营养分析数据已到底</div>'
                },
                loadUpFn: function(wo) {
                    me.refreshFunc(wo);
                },
                loadDownFn: function(wo) {
                    me.loadFunc(wo);
                }
            });
        },
        refreshFunc: function(droploader) {
            var me = searchHelper;
            me.params.pageNo = 1;
            $.ajax({
                type: 'POST',
                url: '/nuting/getRankingListBySdId',
                dataType: 'json',
                data: me.params,
                success: function(data) {
                    //重置
                    droploader.resetload();
                    if(!isKickOut(data.code)){
                        if(data.rows!=null){
                            if(data.rows.length > 0) {
                                if((data.total%limCount)>0){
                                    allPage = accAdd(parseInt(accDiv(data.total,limCount)),1);
                                }else{
                                    allPage = parseInt(accDiv(data.total,limCount));
                                }

                                me.resetRenderFunc();
                                me.renderFunc(data.data);
                                //有数据
                                droploader.noData(false);
                                me.params.page++;
                            } else {
                                //无数据
                                droploader.noData();
                            }
                        }else{
                            //无数据
                            droploader.noData();
                        }
                    }
                    //重置
                    droploader.unlock();
                },
                error: function(xhr, type) {
                    console.log('加载数据错误-' + type);
                    droploader.resetload();
                }
            });
        },
        loadFunc: function(droploader) {
            var me = searchHelper;
            if(allPage==0){
                $.ajax({
                    type: 'POST',
                    url: '/nuting/getRankingListBySdId',
                    dataType: 'json',
                    data: me.params,
                    success: function(data) {
                        if(!isKickOut(data.code)){
                            if(data.rows!=null){
                                if(data.rows.length > 0) {
                                    if((data.total%limCount)>0){
                                        allPage = accAdd(parseInt(accDiv(data.total,limCount)),1);
                                    }else{
                                        allPage = parseInt(accDiv(data.total,limCount));
                                    }

                                    //me.resetRenderFunc();
                                    me.renderFunc(data);
                                } else {
                                    //锁定
                                    droploader.lock();
                                    //无数据
                                    droploader.noData();
                                }
                            }else{
                                //锁定
                                droploader.lock();
                                //无数据
                                droploader.noData();
                            }
                        }
                        //重置
                        droploader.resetload();
                        me.params.page++;
                    },
                    error: function(xhr, type) {
                        console.log('加载数据错误-' + type);
                        droploader.resetload();
                    }
                });
            }else if(allPage!=0){
                if(me.params.page>allPage){
                    //锁定
                    droploader.lock();
                    //无数据
                    droploader.noData();
                    droploader.resetload();
                }else{
                    $.ajax({
                        type: 'POST',
                        url: '/nuting/getRankingListBySdId',
                        dataType: 'json',
                        data: me.params,
                        success: function(data) {
                            if(!isKickOut(data.code)){
                                if(data.rows!=null){
                                    if(data.rows.length > 0) {
                                        if((data.total%limCount)>0){
                                            allPage = accAdd(parseInt(accDiv(data.total,limCount)),1);
                                        }else{
                                            allPage = parseInt(accDiv(data.total,limCount));
                                        }

                                        //me.resetRenderFunc();
                                        me.renderFunc(data);
                                    } else {
                                        //锁定
                                        droploader.lock();
                                        //无数据
                                        droploader.noData();
                                    }
                                }else{
                                    //锁定
                                    droploader.lock();
                                    //无数据
                                    droploader.noData();
                                }
                            }
                            //重置
                            droploader.resetload();
                            me.params.page++;
                        },
                        error: function(xhr, type) {
                            console.log('加载数据错误-' + type);
                            droploader.resetload();
                        }
                    });
                }
            }
        },

        renderFunc: function(data) {
            initDateTableData2(data);
            paramChangeFlag = false;

            $("#rankingList").viewer('update');
            $('#rankingList').viewer({
                url: 'data-original',
            });

            $(".myTableTooltip").css('font-weight', 'normal');
            $(".myTableTooltip").tooltip({
                effect:'toggle',
                cancelDefault: true
            });
        },

        resetRenderFunc: function() {
            $('#rankingList').empty();
        }
    };
    //searchHelper.init();
    droploadIngredientBase();

    $('.imgSelect').selectator({
        labels: {
            search: ''
        },
        showAllOptionsOnFocus: true//初始化复选
    });

    $('.imgSelect').next().css("width","100%");
    $("#disinReason").next().find(".selectator_option_subtitle").css("color","#cccccc");
    $("#disinReason").next().find(".selectator_option_right").css("background-color","transparent");
    //设置you

    //监听窗口，显示隐藏move top
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
});

function droploadIngredientBase(me){

    if (me == null) {
        $(window).unbind("scroll");//解绑每次点击请求调用此方法时的scroll事件
        curPag = 1;
        $("#rankingList").html("");//清空tab1的数据重新获取高度，否则切换tab会保留在之前位置，造成多次请求
    }
    if(allPage==0){
        $.ajax({
            type: 'POST',
            url: '/nuting/getRankingListBySdId',
            dataType: 'json',
            data: {
                page: curPag,
                limit: limCount,
                sdIds : $("input[name='sdIds']").val(),
                type : $("input[name='type']").val(),
                timeQuantum : $("input[name='timeQuantum']").val()
            },
            success: function (data) {
                if(!isKickOut(data.code)){

                    /*
                    重新加载echart
                    reloadDetailChar(data);*/

                    if(data.rows!=null){
                        if((data.total%limCount)>0){
                            allPage = accAdd(parseInt(accDiv(data.total,limCount)),1);
                        }else{
                            allPage = parseInt(accDiv(data.total,limCount));
                        }

                        /*if(curPag>=allPage){
                            me.noData();//用于显示“到底了”
                        }*/
                        var interHtml = initDateTableData2(data);
                        if (me == null) {
                            $("#rankingList").html(interHtml); //第一次请求载入数据
                            initScroll(true);
                        } else {
                            if (data.rows.length > 0) {
                                $("#rankingList").append(interHtml); //第二次请求追加数据
                            } else {
                                //如果没有数据
                                me.noData();//用于显示“到底了”
                                me.lock();//锁定滑动（可不要）
                            }
                            initScroll(false);
                        }

                        $("#rankingList").viewer('update');
                        $('#rankingList').viewer({
                            url: 'data-original',
                        });

                        $(".myTableTooltip").css('font-weight', 'normal');
                        $(".myTableTooltip").tooltip({
                            effect:'toggle',
                            cancelDefault: true
                        });
                    }else{
                        //锁定
                        droploader.lock();
                        //无数据
                        droploader.noData();
                    }
                }

            }
        });
    }else if(allPage!=0){
        if(curPag>allPage){
            me.noData();//用于显示“到底了”
            me.lock();//锁定滑动（可不要）
            me.resetload();
        }else{
            for(var i=0;i<1000;i++){
                console.log("loadding");
            }
            $.ajax({
                type: 'POST',
                url: '/nuting/getRankingListBySdId',
                dataType: 'json',
                data: {
                    page: curPag,
                    limit: limCount,
                    sdIds : $("input[name='sdIds']").val(),
                    type : $("input[name='type']").val(),
                    timeQuantum : $("input[name='timeQuantum']").val()
                },
                success: function (data) {
                    if(!isKickOut(data.code)){

                        /*
                        重新加载echart
                        reloadDetailChar(data);*/

                        if(data.rows!=null){
                            if((data.total%limCount)>0){
                                allPage = accAdd(parseInt(accDiv(data.total,limCount)),1);
                            }else{
                                allPage = parseInt(accDiv(data.total,limCount));
                            }
                            var interHtml = initDateTableData2(data);
                            if (me == null) {
                                $("#rankingList").html(interHtml); //第一次请求载入数据
                                initScroll(true);
                            } else {
                                if (data.rows.length > 0) {
                                    $("#rankingList").append(interHtml); //第二次请求追加数据
                                } else {
                                    //如果没有数据
                                    me.noData();//用于显示“到底了”
                                    me.lock();//锁定滑动（可不要）
                                }
                                initScroll(false);
                            }

                            $("#rankingList").viewer('update');
                            $('#rankingList').viewer({
                                url: 'data-original',
                            });

                            $(".myTableTooltip").css('font-weight', 'normal');
                            $(".myTableTooltip").tooltip({
                                effect:'toggle',
                                cancelDefault: true
                            });
                        }else{
                            //锁定
                            droploader.lock();
                            //无数据
                            droploader.noData();
                        }
                    }

                }
            });
        }
    }
}

// 初始化dropload
function initScroll(first) {

    if (first) {//是否第一次
        droploader = null;
        $(".dropload-down").remove();//去掉底部多个“到底了字样”

        droploader = $('.dataArea').dropload({
            scrollArea: window,
            domDown : {
                domClass   : 'dropload-down',
                // 滑动到底部显示内容
                domRefresh : '<div class="dropload-refresh">智飨平台提示:↑上拉加载更多</div>',
                // 内容加载过程中显示内容
                domLoad    : '<div class="dropload-load"><span class="loading"></span>智飨平台提示:○营养分析数据加载中...</div>',
                // 没有更多内容-显示提示
                domNoData  : '<div class="dropload-noData">智飨平台提示:营养分析数据已到底</div>'
            },
            // 2 . 上拉加载更多 回调函数
            loadDownFn: function (me) {
                curPag++;
                droploadIngredientBase(me);
            }
        });
        droploader.unlock();//解锁之前到底后的锁定 否则上拉加载不触发。droploader.resetload();
    } else {
        droploader.resetload();
    }
}

function showSCJCXXSearchRequest(formData, jqForm, options){
    return true; //只要不返回false，表单都会提交,在这里可以对表单元素进行验证
};
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}

/* 地图形式显示站点 */
function openMap() {
    map = showMap(map,'compose-modal');
}

function searchReload(){
    droploadIngredientBase();
    return false;
}

function initDateTableData2(data) {

    var innerHtml = '';
    //data.rows = [{"name":"小明","calorie":22.5,"siteName":"鸿达","sitePhoto":"http://121.43.35.181:85/2019/05/29/14/1559112042398262.jpg"}];
    $.each(data.rows,function (ind,val) {

        pm = accAdd(pm,1);

        var name = '';
        if(val.name){
            name = val.name;
        }

        var calorie = 0;
        if(val.calorie){
            calorie = val.calorie;
        }

        var siteName = '';
        if(val.siteName){
            siteName = val.siteName;
        }

        var sitePhoto='';
        if(val.sitePhoto){
            sitePhoto = val.sitePhoto;
        }

        innerHtml = innerHtml + "<div class='myTableTooltip' title='"+siteName+":所属' style='border-left:0;'>" +
            "<a href='javascript:void(0);' class='aui-flex'>" +
            "<div class='aui-goods-img'><img data-original='"+sitePhoto+"' src='"+sitePhoto+"' class='online' alt=''>" +
            "<span class='aui-goods-top aui-goods-top-img'>" +
            "</span>" +
            "</div>" +
            "<div class='aui-flex-box'>" +
            "<h4>"+name+"</h4>" +
            "<span>" +
            "<em><i class='aui-hot-tag'></i>卡路里共计:"+calorie+"car</em>" +
            "</span>" +
            "<div class='aui-flex aui-flex-clear'><em class='aui-flex-box'>" +
            "<i class='aui-goods-price' style='font-size: 0.8em;color: #b8b8b8;'>-当前排行由智飨科技提供-</i></em><em>排名："+pm+"</em>" +
            "</div></div></a></div>";
    });
    return innerHtml;
}

function choseRank(type){
    $("#timeQuantum").val("");
    if(type>0){
        $("#rankingType").val(type);
    }
    pm = 0;
    droploadIngredientBase(null);
}

function searchClick() {
    $("#rankingType").val("");
    droploadIngredientBase(null)
}

function showKeepSampleDetails(ksId,ksdId) {
    $.post("/keepSampleDetail/getDetailByIdSdId", {lyMainId:ksId,sdId:ksdId}, function(data){
        if (data.code==1000) {
            $("#composition-Ul").html("");
            $.each(data.rows,function (indI,valI) {

                var status = '';
                var checkStatusColor = '';
                var jcIcon = '';
                var transColor = '';
                switch (valI.lyTypes) {
                    //;1 制样;2 存样;3 出样;4 作废此次
                    case '1':{
                        status = "未存样品";
                        checkStatusColor = 'green';
                        jcIcon = 'iconzuoyexuanzhongzhuangtai';
                        transColor = '#7fc6a6';
                    }
                        break;
                    case '2':{
                        status = "识别存样";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                    case '3':{
                        status = "应急存样";
                        checkStatusColor = 'red';
                        jcIcon = 'iconzuoyeweixuanzhong';
                        transColor = '#d9534f';

                    }
                        break;
                }

                var statusCY = '';
                var checkStatusColorCY = '';
                var jcIconCY = '';
                var transColorCY = '';
                switch (valI.cyTypes) {
                    //;1 制样;2 存样;3 出样;4 作废此次
                    case '1':{
                        statusCY = "未出样品";
                        checkStatusColorCY = 'green';
                        jcIconCY = 'iconzuoyexuanzhongzhuangtai';
                        transColorCY = '#7fc6a6';
                    }
                        break;
                    case '2':{
                        statusCY = "识别出样";
                        checkStatusColorCY = 'red';
                        jcIconCY = 'iconzuoyeweixuanzhong';
                        transColorCY = '#d9534f';

                    }
                        break;
                    case '3':{
                        statusCY = "应急出样";
                        checkStatusColorCY = 'red';
                        jcIconCY = 'iconzuoyeweixuanzhong';
                        transColorCY = '#d9534f';

                    }
                        break;
                }

                var lyFoodName = "";
                if(valI.lyFoodName){
                    lyFoodName = valI.lyFoodName;
                }
                var lyItemLabel = "";
                if(valI.lyItemLabel){
                    lyItemLabel = valI.lyItemLabel;
                }
                var lyTime = '';
                if(valI.lyTime){
                    lyTime = valI.lyTime;
                }
                var lyLabelImg = "";
                if(valI.lyLabelImg){
                    lyLabelImg = valI.lyLabelImg;
                }
                var cyTime = 0;
                if(valI.cyTime){
                    cyTime = valI.cyTime;
                }
                var cyLabelImg = 0;
                if(valI.cyLabelImg){
                    cyLabelImg = valI.cyLabelImg;
                }

                $("#composition-Ul").append("<tr>" +
                    "<td data-label='菜品'>" +
                    "<span class='text'><i class='iconfont iconshicai-' style='color:#f44e42;'></i>&nbsp;"+lyFoodName+"</span>" +
                    "</td>" +
                    "<td data-label='ocr识码'>" +
                    /*"<small id='"+val.id+"-foodNameTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.ingredientName+"</small>" +*/
                    "<span class='text'><i class='iconfont icongouwuche' style='color:gold;'></i>&nbsp;"+lyItemLabel+"</span>" +
                    "</td>" +
                    "<td data-label='存入状态'>" +
                    /*"<small id='"+val.id+"-dwTd' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.meteringName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconbaobiaomiaoshu' style='color:#f44e42;'></i>&nbsp;"+status+"</>" +
                    "</td>" +
                    "<td data-label='存入时间'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<span class='text'><i class='iconfont icongouwuche' style='color:green;'></i>&nbsp;"+lyTime+"</>" +
                    "</td>" +
                    "<td data-label='存入现场图'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+lyLabelImg+"' src='"+lyLabelImg+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "<td data-label='出样状态'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconjiage1' style='color:green;'></i>&nbsp;"+statusCY+"</>" +
                    "</td>" +
                    "<td data-label='出样时间'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<span class='text'><i class='iconfont iconjiage1' style='color:gold;'></i>&nbsp;"+cyTime+"</>" +
                    "</td>" +
                    "<td data-label='出样作业人员图'>" +
                    /*"<small id='"+val.id+"-jcztTd' class='label label-danger' style='font-size: 90%;background-color:"+checkStatusColor+"'><i class='iconfont "+jcIcon+"'></i>&nbsp;"+statusName+"</small>" +*/
                    "<div class='my-table-imgDiv'><img data-original='"+cyLabelImg+"' src='"+cyLabelImg+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                    "</td>" +
                    "</tr>");
            });
            $("#composition-modal").viewer('update');
            $('#composition-modal').viewer({
                url: 'data-original',
            });
        }else{
            //系统接口错误
            showJqueryConfirmWindow("#icon-shangxin1",data.msg);
        }
    });
    $("#composition-modal").modal("show");
}

function cleanComposition(){
    //清除select2选中值
    cleanSelect2Val(["choseStatus","choseReason"]);
    //清除提交form
    commonResetForm("composition-Form");
    //隐藏
    $("#reasonDiv").css("display","none");
}