var ws_protocol = 'ws'; // ws 或 wss
var ip = 'localhost'
var port = 2181

var heartbeatTimeout = 5000; // 心跳超时时间，单位：毫秒
var reconnInterval = 1000; // 重连间隔时间，单位：毫秒

var binaryType = 'blob'; // 'blob' or 'arraybuffer';//arraybuffer是字节
var handler = new DemoHandler()

var tiows

function initWs(user, group) {
    var queryString = ''
    if (user) {
        queryString += 'userid=' + user + '&'
    }
    if (group) {
        queryString += 'groupname=' + group + '&'
    }
    queryString = (queryString.substring(queryString.length - 1) == '&') ? queryString.substring(0, queryString.length - 1) : queryString;
    var param = null
    tiows = new tio.ws(ws_protocol, ip, port, queryString, param, handler, heartbeatTimeout, reconnInterval, binaryType)
    tiows.connect()
}


function send() {
    var j = {};
    j.msg = document.getElementById('textId').value;
    j.fs = document.getElementById('fs').value;
    j.fsz = document.getElementById('fsz').value;
    tiows.send(JSON.stringify(j))
}

function clearMsg() {
    document.getElementById('contentId').innerHTML = ''
}

function jinru() {
    var user = document.getElementById('user')
    if (!user.value) {
        alert("输入用户名")
        return
    }
    var group = document.getElementById('group')
    initWs(user.value, group.value)
}
