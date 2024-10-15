import greeter_pb from "greeter_pb";
import greeter_grpc_web_pb from "greeter_grpc_web_pb";

const client = new greeter_grpc_web_pb.GreeterClient("http://localhost:12000/api/");

function sendRequest(name) {
    const request = new greeter_pb.HelloRequest();
    request.setName(name);

    client.sayHello(request, {}, (err, resp) => {
	if (err) {
	    console.error("gRPC error", err);
	} else {
	    const reply = resp.getMessage();
	    console.log(reply);

	    document.getElementById("msg").textContent = reply;
	}
    });
}

function sayHello() {
    let name = document.getElementById("name").value;
    sendRequest(name);
}

window.onload = function() {
    sendRequest("");

    document.getElementById("name").onkeyup = function(){ sayHello() };
};
