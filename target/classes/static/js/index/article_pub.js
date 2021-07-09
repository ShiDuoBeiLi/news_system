// JavaScript Document
layui.use(['form', 'layedit'], function () {
    var form = layui.form
        , layer = layui.layer
            , layedit = layui.layedit,
    $ = layui.jquery;

    layedit.set({
        uploadImage: {
            url: '/upload/uploadFile' //接口url
            , type: 'post', //默认post
        }
    });

//建立编辑器
    //var editIndex = layedit.build('content',{
    //   height: 400, //设置编辑器高度
    // tool: ['strong', 'italic', 'underline', 'left', 'center', 'right', '|', 'link', 'image', 'code']
    //}); 
    var editIndex = layedit.build('content');


    //测试，编辑器外部操作
    var active = {
        content: function () {
            alert(layedit.getContent(editIndex)); //获取编辑器内容
        }
        , text: function () {
            alert(layedit.getText(editIndex)); //获取编辑器纯文本内容
        }
        , selection: function () {
            alert(layedit.getSelection(editIndex));
        }
    };
    $('.site-demo-layedit').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    form.on('submit(public)',function (data){
        var param=data.field;
        $.ajax({
            url: '/articleSave',
            type: "post",
            // async:false,
            data:JSON.stringify(param),
            dataType:"json",
            // async:true,
            contentType: "application/json; charset=utf-8",
            success:function(data){
                if (data.data==200){
                    layer.msg('发布成功', {icon: 1, time: 3000});
                    // console.log("success");
                    setTimeout(function () {window.location.href='/index';},2000);
                }
                if (data.data==201){
                    layer.msg('修改成功', {icon: 1, time: 3000});
                    // console.log("success");
                    setTimeout(function () {window.location.href='/user/user_center';},2000);
                }
                if (data.data==400){
                    layer.alert("发布失败，该账号尚未通过邮箱激活认证，请先激活账号",);
                    // layer.msg('发布失败，该账号尚未通过邮箱激活认证，请先激活账号', {icon: 0, time: 3000});
                    // console.log("success");
                    // setTimeout(function () {window.location.href='/index';},2000);
                }


            },
            error:function(){
                console.log("false");
                layer.msg('发布失败',{icon:0,time:3000});
               setTimeout(function () {window.location.href='/article_pub';},2000);
            }
        })
    });


//自定义验证规则
    form.verify({
        title: function (value) {
            if (value.length < 5) {
                return '标题至少得5个字符';
            }
        }
        , content: function (value) {
            var content = layedit.getContent(editIndex);  //获取编辑器的内容
            if (content.length <= 0) {
                return '文章内容不能为空';
            }
            return layedit.sync(editIndex);
        }
        , type: function (value) {
            if (value.length <= 0) {
                return '请选择文章类型';
            }
        }
        // , code: function (value) {
        //     if (value.length <= 0) {
        //         return '验证码不能为空';
        //     }
        // }
    });

});