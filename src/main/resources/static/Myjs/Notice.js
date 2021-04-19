$(function() {//消息加载
    $.ajax({
        url: "/notice/",
        type: "get",
        success: function (data) {
            if(data==""){//无新消息
                $("#point").remove();//关闭提醒
                var n ="<li>" +
                    "<a href=\"javascript:void(0)\" class = \"media\">" +
                    "<i class=\"fa fa-check-circle  fa-2x text-success\"></i>" +
                    "<div class = \"media-body\">" +
                    "<div class=\"message\">暂时没有新的通知</div>" +
                    "<span class=\"badge badge-outline status\"></span></div></a></li>"
                $("#notice").html(n);
            }else{
                var noticeData = ""
                for(var i in data){//data是map i为key data[i]为value
                    var n ="<li>" +
                        "<a href=\""+i+"\" class = \"media\">" +
                        "<i class=\"fa fa-exclamation-circle fa-2x text-warning\"></i>" +
                        "<div class = \"media-body\">" +
                        "<div class=\"message\">"+data[i]+"</div>" +
                        "<span class=\"badge badge-outline status\"></span></div></a></li>"
                    noticeData = noticeData+n;
                }
                $("#notice").html(noticeData);
            }
        }
    })
})
$("#noticeClear").click(function (){//清空通知
    $.ajax({
        url: "/noticeClear",
        type: "get",
        success: function (data) {
            $("#point").remove();//关闭提醒
            var n ="<li>" +
            "<a href=\"javascript:void(0)\" class = \"media\">" +
            "<i class=\"fa fa-check-circle  fa-2x text-success\"></i>" +
            "<div class = \"media-body\">" +
            "<div class=\"message\">暂时没有新的通知</div>" +
            "<span class=\"badge badge-outline status\"></span></div></a></li>"
            $("#notice").html(n);
        }
    })
})