layui.use(['form','jquery','layer'], function () {
    var form = layui.form,
        $ = layui.jquery;

//    自定义验证规则
    form.verify({
        uname: function (value) {
            //用户名正则，4到10位（字母，数字，下划线）
            let reg_uname = /^[a-zA-Z0-9]{4,10}$/;
            if (!reg_uname.test(value)) {
                return "用户账号格式不正确,请重新输入";
            }
        },
        password: function (value) {
            var uname = value;
            // 密码（最少6位，包括至少，一位字母，一个数字）：
            var reg_uname = /(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d$@!%*#?&]{6,}$/;
            if (!reg_uname.test(uname)) {
                return "密码格式不正确，请重新输入";
            }

        },
        repassword: function (value) {
            var getname = $("#password").val();
            if (value != getname) {
                return "确认密码错误，请重新输入";
            }
        },
        tel: function (value) {
            // 指定手机号格式
            let reg_tel = /^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\d{8}$/;
            if (!reg_tel.test(value)) {
                return "手机号格式错误，请重新输入";
            }
        },
        checkcode: function (value) {
            //验证码为四位
            let reg_verify = /^\w{4}$/;
            if (!reg_verify.test(value)) {
                return "输入有误，验证码只能为四位";
            }
        }
    });
    form.on('submit(register)', function (data) {
        // console.log("发送的数据为"+data);
       var param=data.field;
       $.ajax({
          url:'/user/register/result',
           type:'post',
           // async:false,
           data:JSON.stringify(param),
           contentType: "application/json; charset=utf-8",
           dataType:"json",
           success:function (data){
               // console.log("接收的数据为"+data);
              //注册失败
                if (data.status==400 || data.status==401){
                    layer.msg(data.msg,{icon:0,time :3000});
                }
                //注册成功
                if (data.status==200){
                    layer.alert(data.msg,function (){
                        window.location.href='/user/login';
                    })

                   // layer.msg(data.msg,{icon:1,time: 3000})
                   //  setTimeout(function () {window.location.href='/index';},2000);
                   //window.location.href='/index';
                }

           }

       });

    });
});
