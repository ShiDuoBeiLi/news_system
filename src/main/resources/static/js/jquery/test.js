<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->

    layui.use('layedit', function(){
    var layedit = layui.layedit
    ,$ = layui.jquery;

    //构建一个默认的编辑器
    var index = layedit.build('LAY_demo1');

    //编辑器外部操作
    var active = {
    content: function(){
    alert(layedit.getContent(index)); //获取编辑器内容
}
    ,text: function(){
    alert(layedit.getText(index)); //获取编辑器纯文本内容
}
    ,selection: function(){
    alert(layedit.getSelection(index));
}
};

    $('.site-demo-layedit').on('click', function(){
    var type = $(this).data('type');
    active[type] ? active[type].call(this) : '';
});

    //自定义工具栏
    layedit.build('LAY_demo2', {
    tool: ['face', 'link', 'unlink', '|', 'left', 'center', 'right']
    ,height: 100
})
});