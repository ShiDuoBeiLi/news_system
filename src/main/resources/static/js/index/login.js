layui.use('form',function (){
    var form = layui.form,
        $ = layui.jquery;
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
        checkcode: function (value) {
            //验证码为四位
            let reg_verify = /^\w{4}$/;
            if (!reg_verify.test(value)) {
                return "输入有误，验证码只能为四位";
            }
        }
    });

    form.on('submit(login)', function (data) {
        console.log("发送的数据为"+data);
        var param=data.field;
        $.ajax({
            url:'/user/login/result',
            type:'post',
            // async:false,
            data:JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            dataType:"json",
            success:function (data){
                console.log("接收的数据为"+data);
                //验证码错误或者用户不存在
                if (data.status==400 || data.status==401){
                    layer.msg(data.msg,{icon:0,time :3000});
                }
                //登录成功
                if (data.status==200){
                    layer.alert(data.msg,function (){
                        window.location.href='/index';
                    })

                }

            }

        });

    });
});