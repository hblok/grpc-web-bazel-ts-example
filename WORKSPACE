# Top-level setup
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_file")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

# Version 4.6.0 of rules-proto-grpc is the last which supports the grpcweb rule.
# It was moved out in version 5
proto_grpc_version = "4.6.0"

http_archive(
    name = "rules_proto_grpc",
    urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/archive/refs/tags/%s.tar.gz" % (
        proto_grpc_version)],
    strip_prefix = "rules_proto_grpc-%s" % proto_grpc_version,
    sha256 = "c0d718f4d892c524025504e67a5bfe83360b3a982e654bc71fed7514eb8ac8ad",
)   

load("@rules_proto_grpc//:repositories.bzl",
     "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")
rules_proto_grpc_toolchains()
rules_proto_grpc_repos()

load("@rules_proto_grpc//js:repositories.bzl", rules_proto_grpc_js_repos = "js_repos")
rules_proto_grpc_js_repos()


load("@build_bazel_rules_nodejs//:repositories.bzl", "build_bazel_rules_nodejs_dependencies")
build_bazel_rules_nodejs_dependencies()

load("@build_bazel_rules_nodejs//:index.bzl", "yarn_install")

yarn_install(
    name = "npm",
    package_json = "//:package.json",
    yarn_lock = "//:yarn.lock",
)

# Google GRPC
grpc_version = "1.43.2"
http_archive(
    name = "com_github_grpc_grpc",
    url = "https://github.com/grpc/grpc/archive/refs/tags/v%s.zip" % grpc_version,
    strip_prefix = "grpc-%s" % grpc_version,
#    sha256 = "d6827949d9a32d205c802a3338196f161c0c058416a0248bd50a13a8e124b53c",
)

load("@com_github_grpc_grpc//bazel:grpc_deps.bzl", "grpc_deps")
grpc_deps()
load("@com_github_grpc_grpc//bazel:grpc_extra_deps.bzl", "grpc_extra_deps")
grpc_extra_deps()


# rules_python
http_archive(
    name = "rules_python",
    sha256 = "ca77768989a7f311186a29747e3e95c936a41dffac779aff6b443db22290d913",
    strip_prefix = "rules_python-0.36.0",
    url = "https://github.com/bazelbuild/rules_python/releases/download/0.36.0/rules_python-0.36.0.tar.gz",
)

load("@rules_python//python:repositories.bzl", "py_repositories")

py_repositories()


git_repository(
    name = "io_bazel_stardoc",
    remote = "https://github.com/bazelbuild/stardoc.git",
    #tag = "0.4.0",
    commit = "4378e9b6bb2831de7143580594782f538f461180",
)

load("@io_bazel_stardoc//:setup.bzl", "stardoc_repositories")
stardoc_repositories()


git_repository(
    name = "io_bazel_rules_python",
    remote = "https://github.com/bazelbuild/rules_python.git",
    commit = "44fdf5f24a4b634ef7b0f86e7a017f4d8d5aa899",
)


# Optional: Docker setup
# http_archive(
#     name = "io_bazel_rules_docker",
#     strip_prefix = "rules_docker-0.14.1",
#     sha256 = "dc97fccceacd4c6be14e800b2a00693d5e8d07f69ee187babfd04a80a9f8e250",
#     urls = ["https://github.com/bazelbuild/rules_docker/releases/download/v0.14.1/rules_docker-v0.14.1.tar.gz"],
# )

# load(
#     "@io_bazel_rules_docker//repositories:repositories.bzl",
#     container_repositories = "repositories",
# )

# container_repositories()

# load(
#     "@io_bazel_rules_docker//nodejs:image.bzl",
#     _nodejs_image_repos = "repositories",
# )
