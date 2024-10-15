java_binary(
    name = "GreeterServer",
    srcs = ["GreeterServer.java"],
    main_class = "server.GreeterServer",
    deps = [
       "//proto:greeter_java_grpc",
       "//proto:greeter_java_proto",
       "@io_grpc_grpc_java//api",
       "@io_grpc_grpc_java//stub",
       "@maven//:io_grpc_grpc_netty_shaded",
       ],
)
