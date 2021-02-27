$(function(){
    var ws = new WebSocket("ws://localhost:8080/chat.html");
    var userID = getUserID();
    ws.onopen = function(){
        //建立连接后触发
        console.log(111);
    }
    ws.onmessage = function(evt){
        //接受到服务器推送的消息触发
        console.log(222);
        var dataStr = evt.data;
        //将dataStr转换为json对象
        var res = JSON.parse(dataStr);
        //判断是否是系统信息
        if(res.system){
            //是系统信息
            var Keys = res.message;
            //好友列表展示
            var userlistStr = "";
            for(let key of Keys){
                if(key.id != userID){
                    var avatar = getAvatar(key.id);
                    userlistStr +="<li><a onclick='showChat(\""+key.id+"\"\,\""+key.name+"\")' class=\"media\"><img class=\"media-object\" src=\""+avatar+"\" alt><div class=\"media-body\"><span class=\"id\">"+key.name+"</span><span class=\"message\">在线</span></div></a></li>";
                }
            }
            $("#userlist").html(userlistStr);

        }else{
            //不是系统信息
            var Key = res.key;
            var time = new Date(res.time);
            var t =  time.getMonth()+1 + "-"
                + time.getDate() + " "
                + time.getHours() + ":"
                + time.getMinutes();
            var avatar = getAvatar(Key.id);
            var Str = "<li class=\"other-message\"><img class=\"avatar mr-3\" src=\""+avatar+"\" alt=\"avatar\"><div class=\"message\"><p class=\"bg-light-cyan\">"+res.message+"</p><span class=\"time\">"+t+"</span></div></li>"
            //如果是当前聊天的用户则推送
            if(toID == Key.id){
                $("#msgs").append(Str);
                $('#msgs').animate({scrollTop: $('#msgs').get(0).scrollHeight}, 1500);
            }
            var chatData = sessionStorage.getItem(Key.id);
            if(chatData != null){
                Str = chatData + Str;
            }
            //历史记录存进session里
            sessionStorage.setItem(Key.id,Str);

        }
    }
    ws.onclose = function(){
        console.log(333);
    }
    //展示信息
    showChat = function(id,name){
        toID = id;
        toKey = {'id':id,'name':name};
        //清空聊天区
        $("#msgs").html("");
        $("#chatName").html(name);
        //将保留的消息展示出来
        var chatData = sessionStorage.getItem(toKey.id);
        if (chatData != null){
            $("#msgs").html(chatData);
            $('#msgs').animate({scrollTop: $('#msgs').get(0).scrollHeight}, 1500);
        }else{
            //查数据库里的聊天记录
            $.ajax({
                url: "/chatRecord",
                type: "post",
                data: {'id':userID,'chatID':toID},
                success: function (data) {
                    chatData = "";
                    var avatar = getAvatar(toID);
                    for(var i in data){
                        if(data[i].id==userID){
                            var time = new Date(data[i].time);
                            var t =  time.getMonth()+1 + "-"
                                + time.getDate() + " "
                                + time.getHours() + ":"
                                + time.getMinutes();
                            var StrA = "<li class=\"my-message\"><div class=\"message\"><p class=\"bg-light-gray\">"+data[i].message+"</p><span class=\"time\">"+t+"</span></div></li>";
                            $("#msgs").append(StrA);
                            chatData = chatData+StrA;
                        }else {
                            var time = new Date(data[i].time);
                            var t =  time.getMonth()+1 + "-"
                                + time.getDate() + " "
                                + time.getHours() + ":"
                                + time.getMinutes();
                            var StrA = "<li class=\"other-message\"><img class=\"avatar mr-3\" src=\"" + avatar + "\" alt=\"avatar\"><div class=\"message\"><p class=\"bg-light-cyan\">" + data[i].message + "</p><span class=\"time\">" + t + "</span></div></li>";
                            chatData = chatData+StrA;
                            $("#msgs").append(StrA);
                        }
                    }
                    $('#msgs').animate({scrollTop: $('#msgs').get(0).scrollHeight}, 1500);
                    sessionStorage.setItem(toID,chatData);
                }
            });
        }
    };
    //发送信息
    $("#send").click(function(){
        var data = $("#message").val();
        //清除输入框
        $("#message").val("");
        //推送
        var json = {"key":toKey,"message":data};
        ws.send(JSON.stringify(json));
        //将信息展示在自己的区域
        var time = new Date();
        var t =  time.getMonth()+1 + "-"
            + time.getDate() + " "
            + time.getHours() + ":"
            + time.getMinutes();
        var Str = "<li class=\"my-message\"><div class=\"message\"><p class=\"bg-light-gray\">"+data+"</p><span class=\"time\">"+t+"</span></div></li>";
        $("#msgs").append(Str);
        $('#msgs').animate({scrollTop: $('#msgs').get(0).scrollHeight}, 1500);
        //历史记录存进session里
        var chatData = sessionStorage.getItem(toKey.id);
        if(chatData != null){
            Str = chatData + Str;
        }
        sessionStorage.setItem(toKey.id,Str);
    });

})