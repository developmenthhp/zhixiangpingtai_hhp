var basePath;

$(function(){

    basePath = getContextPath();

});

function getContextPath(){
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}

function isKickOut(code){

    //没有踢出
    var flag = false;

    if(code!=undefined&&code!="undefined"){
        if(code==1101){
            //被踢出
            flag = true;

            $.confirm({
                icon: 'fa fa-frown-o',
                theme: 'modern',
                title: '同账号在线人数异常',
                content: "您已在别处登录，请您修改密码或重新登录",
                animation: 'news',//动画
                closeAnimation: 'news',//关闭动画
                autoClose: 'showMyMsg|3000',
                buttons: {
                    showMyMsg: {
                        text: '关闭',
                        action: function () {
                            window.location.href="/login";
                        }
                    }
                }
            });
        }else if(code==1533){
            //被踢出
            flag = true;

            $.confirm({
                icon: 'fa fa-frown-o',
                theme: 'modern',
                title: '权限不足',
                content: "您的权限不足，该系统只可登录智飨内部账号",
                animation: 'news',//动画
                closeAnimation: 'news',//关闭动画
                autoClose: 'showMyMsg|3000',
                buttons: {
                    showMyMsg: {
                        text: '关闭',
                        action: function () {
                            window.location.href="/login";
                        }
                    }
                }
            });
        }
    }

    return flag;
}