function changeDisplay(id){
    //管理员编辑学生教师信息输入框显示/隐藏
    var display = document.getElementsByClassName(id);
    for(var i=0;i<display.length;i++) {
        if (display[i].style.display == "") {
            display[i].style.display = "none";
        } else {
            display[i].style.display = ""
        }
    }
}
function submitTea(id,name,gender,college,phone) {
    //提交教师信息修改
    var Cname = $("#" + name).val();
    var Cgender = $("#" + gender).val();
    var Ccollege = $("#" + college).val();
    var Cphone = $("#" + phone).val();
    if (Cgender == "男") {
        Cgender = 1;
    } else {
        Cgender = 0;
    }
    $.ajax({
        url: "/updateTea",
        type: "post",
        //发送的数据
        data: JSON.stringify({id: id, name: Cname, gender: Cgender, college: Ccollege, phone: Cphone}),//提交的数据
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function () {
            window.location.reload()
        }
    });
}
function submitStu(id,name,gender,classes,college,phone){
    //提交学生信息修改
    var Cname = $("#"+name).val();
    var Cgender = $("#"+gender).val();
    var Cclasses = $("#"+classes).val();
    var Ccollege = $("#"+college).val();
    var Cphone = $("#"+phone).val();
    if(Cgender=="男"){
        Cgender = 1;
    }else{
        Cgender = 0;
    }
    $.ajax({
        url: "/updateStu/id",
        type: "post",
        //发送的数据
        data: JSON.stringify({id: id,name: Cname,gender: Cgender,classes: Cclasses,college: Ccollege,phone: Cphone}),//提交的数据
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function () {
            window.location.reload();
        }
    });
}
function AddByExcel(role){
    //通过Excel表格增加
    if(role=='studentCourse'){
        var file = $("#SC")[0].files[0];
    }else {
        var file = $("#uploadFile")[0].files[0];
    }
    var formData = new FormData();
    formData.append("file",file)
    $.ajax({
        url: "/uploadExcel/"+role,
        type: "post",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            if(data == 1){
                alert("上传成功");
                window.location.reload();
            }
        }
    });
}
