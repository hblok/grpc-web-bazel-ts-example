load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")


## GRPC

proto_grpc_version = "4.6.0"

http_archive(
    name = "rules_proto_grpc",
    urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/archive/refs/tags/%s.tar.gz" % (
        proto_grpc_version)],
    strip_prefix = "rules_proto_grpc-%s" % proto_grpc_version,
    sha256 = "c0d718f4d892c524025504e67a5bfe83360b3a982e654bc71fed7514eb8ac8ad",
)   


load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")

rules_proto_grpc_toolchains()

rules_proto_grpc_repos()


load("@rules_proto_grpc//js:repositories.bzl", rules_proto_grpc_js_repos = "js_repos")

rules_proto_grpc_js_repos()



## NodeJS, Yarn (NPM)

load("@build_bazel_rules_nodejs//:repositories.bzl", "build_bazel_rules_nodejs_dependencies")

build_bazel_rules_nodejs_dependencies()

load("@build_bazel_rules_nodejs//:index.bzl", "yarn_install")

yarn_install(
    name = "npm",
    package_json = "//:package.json",
    yarn_lock = "//:yarn.lock",
)



## Java, Maven

RULES_JVM_EXTERNAL_TAG = "6.4"
http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = "8c92f7c7a57273c692da459f70bd72464c87442e86b9e0b495950a7c554c254f",
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
artifacts = [
	"com.google.api.grpc:proto-google-common-protos:2.46.0",
	"com.google.guava:guava:33.3.1-jre",
	"io.grpc:grpc-api:1.68.0",
	"io.grpc:grpc-netty-shaded:1.68.0",
        "javax.annotation:javax.annotation-api:1.3.2",
        "org.apache.tomcat:annotations-api:6.0.13",

    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)

grpc_java_version = "1.67.1"
http_archive(
    name = "io_grpc_grpc_java",
    urls = ["https://github.com/grpc/grpc-java/archive/v%s.tar.gz" % grpc_java_version],
    strip_prefix = "grpc-java-" + grpc_java_version,
    integrity = "sha256-dNPOEqDnirIwWBr3A++3hSL2d8+x9Q3Z57cNRqgY9f8="
)
