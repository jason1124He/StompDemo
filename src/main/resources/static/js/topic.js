/*
* 简单的获取stomp发布到消息队列中的消息*/
// TODO: 如何判断连接状态

var stompClient = null;

function setConnected(connected) {
    document.getElementById("connect").disabled = connected;
    document.getElementById("disconnect").disabled = !connected;
    $("#response").html();
}

function connect() {
    //默认开启了心跳检测
    // //客户端每 10000ms  向服务端发起心跳
    // stompClient.heartbeat.outgoing = 3000;
    // //客户端端每 10000ms   从服务端获取心跳
    // stompClient.heartbeat.incoming = 3000;

    /*
      * 1.  发送心跳信号，服务端返回心跳信号，用来表示服务器没挂掉
      * 2.  断线重连 ，断开网络后，心跳包无法发送出去，所以如果当前时间距离上次成功心跳超过 30 s，手动关闭连接
      * 3.  第一次关闭连接时websocket会尝试重连，设置了一个时间期限，10秒。10秒内如果能连上（恢复网络连接）就可以继续收发消息，连不上就关闭了，并且不会重连。
      * 4.  30秒内收不到服务器消息（心跳每秒发送），我就认为服务器已经挂了，就会调用close事件，然后进入第3步。
     */
    if (stompClient == null || !stompClient.connected) {
        var socket = new SockJS("/webServer");
        stompClient = Stomp.over(socket);
        stompClient.heartbeat.outgoing = 3000;//客户端发送心跳包
        stompClient.heartbeat.incoming = 3000;//客户端接收服务端发送的心跳包

        //连接
        console.log("当前处于断开状态,尝试连接");
        setConnected(true);

        var headers = {};
        /* stompClient.connect(headers, function (frame) {
             console.info("连接成功");
             console.log('Connected: ' + frame);
             // stompClient.subscribe('/toUser/zhangsan/getResponse', function (response) {
             stompClient.subscribe('/toUser/getResponse', function (response) {
                 console.log(response.body);
             });
         }, function (error) {
             console.info("连接失败");
         });*/

        stompClient.connect(headers, connectCallback, errorCallback);

        stompClient.debug = function (str) {
            console.log(str);
        }
    } else {
        console.log("当前处于连接状态");
    }

}

function disconnect() {
    //主动断开连接
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var name = document.getElementById('name').value;
    console.info(1111111111);
    stompClient.send("/topic/subscribe", {}, JSON.stringify({'name': name}));
}


//连接成功时回调
function connectCallback(frame) {
    //订阅了一个消息接收推送
    // stompClient.connect({}, function (frame) {
    //如果连接成功，是订阅主题，接受消息推送

    console.log('Connected: ' + frame);
    //订阅 user/zhangsan/message 消息 ，后台发布消息时，使用 SimpMessagingTemplate::convertAndSendToUser("zhangsan", "/message", name); 发布到指定用户
    //document.getElementById('user').value = "zhangsan"
    stompClient.subscribe('/topic/getResponse', function (response) {
        var response1 = document.getElementById('response');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        p.appendChild(document.createTextNode(response.body));
        response1.appendChild(p);
    }, function (error) {
        console.log("连接失败");
    });
    // });
}


//连接失败时，在一分钟内每隔10s，尝试重新连接，超过一分钟时，提示连接失败
function errorCallback() {

    setInterval(function () {

        setTimeout(function () {
            connect();
        }, perTime)
    }, timeOut, function () {

        console.log("连接失败");
        //关闭连接
        disconnect();

    }, perTime);

}

/*//todo:心跳检测
function heartCheck() {
    // 订阅某个主体，向该主题发送心跳消息,后端返回心跳信号，如果没有拿到，继续请求，如果30s还没拿到，手动断开连接重连，若果连接不上，提示连接已断开
    stompClient.subscribe();
}*/
