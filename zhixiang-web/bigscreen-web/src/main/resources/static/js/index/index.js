var picCode = false;
var formCo = false;
$(function(){
    var path=window.location.href;
//    	 console.info("==请求的uri:"+path);
    if(path.indexOf("kickout")>0){
        window.location.href="/login";
    }
    $("#loginForm").validate({
        rules: {
            ingredientName: {
                required: true
            },
            password: 'required',
            mobile: {
                required: true/*,
                regexp: /^(13|15|17|14|18)\d{9}$/*/
            }
        },
        messages: {
            username: {
                required: "请输入用户名"
            },
            password: "请输入密码",
            mobile: {
                required: "请输入手机号"/*,
                regexp: "请输入正确的手机号"*/
            }
        },
        errorClass: "error",
        success: 'valid',
        unhighlight: function (element, errorClass, validClass) { //验证通过
            $(element).tooltip('destroy').removeClass(errorClass);
        },
        errorPlacement: function (error, element) {
            if ($(element).next("div").hasClass("tooltip")) {
                $(element).attr("data-original-title", $(error).text()).tooltip("show");
            } else {
                $(element).attr("title",
                    $(error).text()).tooltip("show");
            }
        },
        submitHandler: function (form) {
            formCo = true;
        }
    })
    //监听窗口，显示隐藏move top
    $(window).scroll(function () {

        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
});
$('#mpanel6').pointsVerify({
    defaultNum : 4,	//默认的文字数量
    checkNum : 2,	//校对的文字数量
    vSpace : 5,	//间隔
    imgName : ['1-1.jpg', '2.jpg'],
    imgSize : {
        width: '100%',
        height: '200px',
    },
    barSize : {
        width : '100%',
        height : '40px',
    },
    ready : function() {
    },
    success : function() {
        picCode = true;

    },
    error : function() {
        picCode = false;
    }
});
function valiMyCanv(){
    $("#mpanel6").html("");
    $('#mpanel6').pointsVerify({
        defaultNum : 4,	//默认的文字数量
        checkNum : 2,	//校对的文字数量
        vSpace : 5,	//间隔
        imgName : ['1-1.jpg', '2.jpg'],
        imgSize : {
            width: '100%',
            height: '200px',
        },
        barSize : {
            width : '100%',
            height : '40px',
        },
        ready : function() {
        },
        success : function() {
            //alert('验证成功，添加你自己的代码！');
            picCode = true;
        },
        error : function() {
            alert('验证失败！');
            picCode = false;
        }

    });

}
function login(){
    if(picCode&&formCo){
        $.post("/user/login",$('#loginForm').serialize(),function(data){
            if(data.code=="1000"){
                /*layer.alert("登录成功",function () {
                    window.location.href="/home";
                });*/
                window.location.href="/home";
            }else{
                valiMyCanv();
                $("#code").val("");
                $("#smsCode").val("");
                showJqueryConfirmWindow("#icon-tishi1",data.message);
            }
        });
    }
}
