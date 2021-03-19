var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility
        = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {
    var token = document.getElementById('dispatcherToken').value;
    var socket = new SockJS('http://localhost:8004/client/message?dtk=' + token);
    stompClient = Stomp.over(socket);
    stompClient.reconnect_delay = 5000;

    // TODO: try this
    var headers = {
        login: 'marius',
        passcode: 'mypasscode',
        host: 'host',
        dispToken: document.getElementById('dispatcherToken').value
    };
    stompClient.connect(headers, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        subscribeTo("/command/board");
    });
}

function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function subscribeTo(topic) {
    stompClient.subscribe(topic, function (image) {
        console.log(topic);
    });
}

function sendMessage() {
    var text = document.getElementById('text').value;
    stompClient.send("/app/client/message", {},
        JSON.stringify({'commandId':text}));
}

function showMessageOutput(messageOutput) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(messageOutput.from + ": "
        + messageOutput.text + " (" + messageOutput.time + ")"));
    response.appendChild(p);
}