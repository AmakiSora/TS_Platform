$(function() {//消息加载
    $.ajax({
        url: "/news/",
        type: "get",
        success: function (data) {
            console.log(data);
            var newsData = ""
            for(var i in data){
                var n = "<li><div class=\"feeds-left\"><span class=\"avatar avatar-blue\"><i class=\"fa fa-check\"></i></span></div><div class=\"feeds-body ml-3\"> <p class=\"text-muted mb-0\">"+data[i]+"</p></div></li>"
                newsData=newsData+n;
            }
            $("#news").html(newsData);
        }
    })
})