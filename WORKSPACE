workspace(
    # How this workspace would be referenced with absolute labels from another workspace
    name = "vac",
    # Map the @npm bazel workspace to the node_modules directory.
    # This lets Bazel use the same node_modules as other local tooling.
    managed_directories = {"@npm": ["node_modules"]},
)

# Top-level setup
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
http_archive(
    name = "build_bazel_rules_nodejs",
    sha256 = "f9e7b9f42ae202cc2d2ce6d698ccb49a9f7f7ea572a78fd451696d03ef2ee116",
    urls = [
        "https://github.com/bazelbuild/rules_nodejs/releases/download/1.6.0/rules_nodejs-1.6.0.tar.gz",
    ],
)

# Node version is the default
load("@build_bazel_rules_nodejs//:index.bzl", "node_repositories", "yarn_install")
node_repositories(
    package_json = ["//:package.json"],
    #node_version = "12.13.0",
)
# Dependencies: Bazel-managed by Yarn
yarn_install(
    name = "npm",
    package_json = "//:package.json",
    yarn_lock = "//:yarn.lock",
)


# Install any Bazel rules which were extracted earlier by the yarn_install rule.
load("@npm//:install_bazel_dependencies.bzl", "install_bazel_dependencies")
install_bazel_dependencies()

# TypeScript toolchain
load("@npm_bazel_typescript//:index.bzl", "ts_setup_workspace")
ts_setup_workspace()

# Proto toolchain
http_archive(
    name = "com_google_protobuf",
    strip_prefix = "protobuf-master",
    urls = ["https://github.com/protocolbuffers/protobuf/archive/master.zip"],
)
load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")
protobuf_deps()

# Hack: I can't use grpc-tools' node wrappers properly, they don't like being
# used as a plugin by the ts_grpc_proto_library rules.
# So I depend directly on the plugin as a binary.
http_archive(
    name = "grpc_node_plugin",
    urls = [" https://node-precompiled-binaries.grpc.io/grpc-tools/v1.8.1/linux-x64.tar.gz"],
    sha256 = "ddfc9081c2993d574216870760775703fd974282dfd1cf417f89f58bdc08f1ed",
    build_file = "//:grpc_node_plugin.BUILD.bazel",
)

