function changePassword(){
    //用户更改密码
    var a = $("#changePassword").serializeArray();
    var data={};
    for (var i = 0; i < a.length; i++) {
        data[a[i].name] = a[i]['value']//形成键值对应
    }
    $.ajax({
        url: "/changePassword",
        type: "post",
        data:JSON.stringify(data),
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            if(data == 1){
                alert("密码更改成功");
            }else if(data == 2){
                alert("新密码不一致");
            }else {
                alert("旧密码不正确");
            }
        }
    });
}
function changeReply(id,name,NO){
    //更换回复者
    $("#replier"+NO).val(name);
    $("#replierID"+NO).val(id);
    $("#placeholder"+NO).attr('placeholder','回复'+'@'+name)
}
function changeNote() {
    //获取summernote的内容返回给输入框
    var code = $('#summernote').summernote('code');
    document.getElementById("note").value=code;
}
$(function href(){
    //评论后刷新并返回评论区
    url = window.location.hash;
    if(url.includes("Courses")){
        document.getElementById(url).click();
    }
})
function jumpToChat(id,name,role){
    //跳转到聊天页面（指定联系人）
    if(role=="student"){
        window.open("/student/chat.html#"+id+"-"+name);
    }else if(role=="staff"){
        window.open("/staff/chat.html#"+id+"-"+name);
    }
}