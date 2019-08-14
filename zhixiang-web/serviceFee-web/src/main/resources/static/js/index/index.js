var picCode = false;

var formCo = false;

$(function(){
    var path=window.location.href;
//    	 console.info("==请求的uri:"+path);
    if(path.indexOf("kickout")>0){
        window.location.href="/login";
    }



    /*$('#loginForm').bootstrapValidator({
//        live: 'disabled',
        message: '请按规则输入',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                message: '请按规则输入正确用户名',
                validators: {
                    notEmpty: {
                        message: '请输入用户名'
                    },
                    stringLength: {
                        min: 6,
                        max: 30,
                        message: '用户名长度为6-30位'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '字母数字_.开头'
                    },
                    /!*校验用户名是否存在
                    remote: {
                        url: 'remote.php',
                        message: 'The username is not available'
                    },*!/
                    different: {
                        field: 'password',
                        message: '用户名和密码不可一致'
                    }
                }
            },
            /!*email: {
                validators: {
                    emailAddress: {
                        message: '请输入正确的邮箱地址'
                    }
                }
            },*!/
            password: {
                validators: {
                    notEmpty: {
                        message: '请输入密码'
                    },
                    /!*identical: {
                        field: 'confirmPassword',
                        message: '两次密码不一致'
                    },*!/
                    different: {
                        field: 'username',
                        message: '用户名和密码不可一致'
                    }
                }
            },
            mobile: {
                validators: {
                    notEmpty: {
                        message: '请输入手机号'
                    },
                    regexp: {
                        regexp: /^(13|15|17|14|18)\d{9}$/,
                        message: '请输入正确格式的手机号'
                    }
                }
            }
            /!*confirmPassword: {
                validators: {
                    notEmpty: {
                        message: '请输入确认密码'
                    },
                    identical: {
                        field: 'password',
                        message: '两次密码不一致'
                    },
                    different: {
                        field: 'username',
                        message: '用户名和密码不可一致'
                    }
                }
            },*!/
            /!*birthday: {
                validators: {
                    date: {
                        format: 'YYYY-MM-DD',
                        message: '请输入正确格式的生日'
                    }
                }
            },*!/
            /!*gender: {
                validators: {
                    notEmpty: {
                        message: 'The gender is required'
                    }
                }
            },*!/
            /!*'languages[]': {
                validators: {
                    notEmpty: {
                        message: 'Please specify at least one language you can speak'
                    }
                }
            },
            'programs[]': {
                validators: {
                    choice: {
                        min: 2,
                        max: 4,
                        message: 'Please choose 2 - 4 programming languages you are good at'
                    }
                }
            },
            captcha: {
                validators: {
                    callback: {
                        message: 'Wrong answer',
                        callback: function(value, validator) {
                            var items = $('#captchaOperation').html().split(' '), sum = parseInt(items[0]) + parseInt(items[2]);
                            return value == sum;
                        }
                    }
                }
            }*!/
        }
    });*/

    /*$(".glyphicon-remove").css("top","35.5%");
    $(".glyphicon-ok").css("top","45%");*/

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
        //highlight: function (element, errorClass, validClass) { //未通过验证
        //    // TODO
        //}
        //,
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

/*function validatePhone(){
    var reg=/^(13|15|17|14|18)\d{9}$/;
    if($.trim($("#phone").val())==''){
        $("#phoneSpan").text("请输入用户名/手机号");
        $("#phoneSpan").css("display","block");
        picCode=false;
    }else{
        /!*输入的是手机号*!/
        if(reg.test($("#phone").val())){
            $("#phone").attr("name","mobile");
        }else{
            //username
            $("#phone").attr("name","username");
        }
        $("#phoneSpan").css("display","none");
        picCode=true;
    }
}
function validatePsw(){
    if($.trim($("#psw").val())==""){
        $("#pswSpan").text("请输入密码");
        $("#pswSpan").css("display","block");
        picCode=false;
    }else{
        $("#pswSpan").css("display","none");
        picCode=true;
    }
}*/

function login(){


    /*$('#loginForm').bootstrapValidator('validate');*/
    /*$(".glyphicon-remove").css("top","35.5%");
    $(".glyphicon-ok").css("top","45%");*/
    /*alert($('#loginForm').bootstrapValidator('validate'));*/

    if(picCode&&formCo){
        $.post("/user/login",$('#loginForm').serialize(),function(data){
            if(data.code=="1000"){
                /*layer.alert("登录成功",function () {
                    window.location.href="/home";
                });*/
                window.location.href="/home";
            }else{
                //$("#password").val("");

                valiMyCanv();

                $("#code").val("");
                $("#smsCode").val("");

                showJqueryConfirmWindow("#icon-tishi1",data.message);

                /*layer.alert(data.message,function(){
                    layer.closeAll();//关闭所有弹框
                    //关闭发送验证码按钮倒计时
                    closeSend();
                });*/
            }
        });

    }



}
