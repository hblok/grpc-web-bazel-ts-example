package server;

import server.GreeterGrpc;
import server.HelloRequest;
import server.HelloResponse;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;


class GreeterServer extends GreeterGrpc.GreeterImplBase {

    private final int port;

    GreeterServer() {
	this.port = 12002;
    }

    public void sayHello(HelloRequest request,
			 StreamObserver<HelloResponse> responseObserver) {
	String name = request.getName();
	print("GRPC: sayHello called: name=" + name);

	String msg = name.isEmpty() ? "Greetings from the GRPC server!" : "Nice to meet you " + name + "!";
	
	HelloResponse response =  HelloResponse
	    .newBuilder()
	    .setMessage(msg)
	    .build();
	responseObserver.onNext(response);
	responseObserver.onCompleted();
    }

    public void start() throws IOException, InterruptedException {
	print("\n\nStarting GRPC server on " + this.port);

	ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(
	    this.port, InsecureServerCredentials.create());
	Server server = serverBuilder.addService(this).build();
	server.start();
	server.awaitTermination();
    }

    private void print(String str) {
	System.out.println(str);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
	GreeterServer s = new GreeterServer();
	s.start();
    }
}
