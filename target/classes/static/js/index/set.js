// JavaScript Document

layui.use('form', function(){
	var form = layui.form
  	,layer = layui.layer

    //    自定义验证规则
    form.verify({
        uname: function (value) {
            //用户名正则，4到10位（字母，数字，下划线）
            let reg_uname = /^[a-zA-Z0-9]{4,10}$/;
            if (!reg_uname.test(value)) {
                return "用户名格式不正确,请重新输入";
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
        }
    });
    //修改用户信息
    form.on('submit(alterInfo)', function (data) {
        var param=data.field;
        console.log("发送的数据为"+JSON.stringify(param));
        $.ajax({
            url:'/user/alterInfo',
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
                if (data.flag==200){
                    // layer.alert(data.msg,function (){
                    //     window.location.href='/user/login';
                    // })

                    layer.msg("用户信息修改成功",{icon:1,time: 3000})
                    setTimeout(function () {window.location.href='/user/set';},2000);
                    //window.location.href='/index';
                }

            }

        });

    });

    //修改用户密码
    form.on('submit(alterPassword)', function (data) {
        var param=data.field;
        console.log("发送的数据为"+JSON.stringify(param));
        $.ajax({
            url:'/user/alterPassword',
            type:'post',
            // async:false,
            data:JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            dataType:"json",
            success:function (data){
                // console.log("接收的数据为"+data);
                //注册失败
                if (data.flag==400 ){
                    layer.msg("当前密码输入错误，请重新输入",{icon:0,time :3000});
                }
                //注册成功
                if (data.flag==200){
                    // layer.alert(data.msg,function (){
                    //     window.location.href='/user/login';
                    // })
                    //
                    layer.msg("用户密码修改成功",{icon:1,time: 3000})
                    setTimeout(function () {window.location.href='/user/set';},2000);
                    //window.location.href='/index';
                }

            }

        });

    });

});

layui.use('upload', function(){
	var $ = layui.jquery
  ,upload = layui.upload;
//设定文件大小限制
  upload.render({
    elem: '#upload_head'
    ,url: '/user/uploadHeadImg'
    ,size: 1024 //限制文件大小，单位 KB,
      , auto:false,
      bindAction:'#cer_head'
      ,accept:'images',
      acceptMime: 'image/*'
    ,done: function(res){
          console.log(res)
        if (res.code==0){
            console.log("1");
            $('#user_img').attr("src", res.data.src);
        }
    }
  });
});

//用户手动发送激活邮件
function active(){
    $.ajax({
        url:'/user/per_active',
        type:'get',
        success:function (data){
            // console.log("接收的数据为"+data);
            //邮件发送成功
            if (data.flag==200){
                layer.alert("已向您邮箱发送激活邮件，请前往邮件内点击链接激活账号",);
            }

        }

    });

}