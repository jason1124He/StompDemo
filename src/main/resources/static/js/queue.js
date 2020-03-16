var stompClient = null;

//设置超时时间 1分钟
var timeOut = 60000;

//每次执行间隔 10s
var perTime = 10000;

function setConnected(connected) {
    document.getElementById("connect").disabled = connected;
    document.getElementById("disconnect").disabled = !connected;
    $("#response").html();
}

function connect() {
    if (stompClient == null || !stompClient.connected) {
        var socket = new SockJS("/queueServer");
        stompClient = Stomp.over(socket);
        stompClient.heartbeat.outgoing = 5000;//客户端发送心跳包
        stompClient.heartbeat.incoming = 5000;//客户端接收服务端发送的心跳包

        //连接
        console.log("当前处于断开状态,尝试连接");
        setConnected(true);
        var headers = {
            username: "admin",
            password: "admin"
        };
        stompClient.connect(headers, connectCallback, errorCallback);

        stompClient.debug = function (str) {
            console.log(str);
        }

    } else {
        console.log("当前处于连接状态");
    }
}


//连接成功时回调
function connectCallback(frame) {
    //订阅了一个消息接收推送
    // stompClient.connect({}, function (frame) {
    //如果连接成功，是订阅主题，接受消息推送

    console.log('Connected: ' + frame);

    //订阅 user/zhangsan/message 消息 ，后台发布消息时，使用 SimpMessagingTemplate::convertAndSendToUser("zhangsan", "/message", name); 发布到指定用户
    //document.getElementById('user').value = "zhangsan"
    // stompClient.subscribe('/user/' + document.getElementById('user').value + '/message', function (response) {
    var UUID = "123456123456";
    stompClient.subscribe('/user/' + UUID + '/message', function (response) {
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


//断开连接
function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var name = document.getElementById('name').value;
    console.info(1111111111);
    if (null != stompClient && stompClient.connected) {
        stompClient.send("/queue", {}, JSON.stringify({
            'name': name,
            "description": document.getElementById('user').value
        }));
    } else {
        //重连
        // connect();
        console.log("连接已断开");
    }

}

function test() {
    // console.log("123");
    return "123";
}