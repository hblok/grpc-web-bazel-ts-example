package(default_visibility = ["//visibility:public"])

load("@rules_proto_grpc//js:defs.bzl", "js_grpc_web_compile")
load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")

proto_library(
    name = "greeter_proto",
    srcs = ["greeter.proto"],
)

js_grpc_web_compile(
    name = "greeter_grpcweb",
    protos = ["greeter_proto"],
    verbose = 3,
)

java_proto_library(
     name = "greeter_java_proto",
     deps = [":greeter_proto"],
)

java_grpc_library(
    name = "greeter_java_grpc",
    srcs = [":greeter_proto"],
    deps = [
      ":greeter_java_proto",
      # Compatibility issue with older Java versions.
      "@maven//:javax_annotation_javax_annotation_api_1_3_2",
    ],
)
