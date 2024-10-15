# rollup_bundle not used for now.
#load("@npm//@bazel/rollup:index.bzl", "rollup_bundle")

package(default_visibility = ["//visibility:public"])

filegroup(
    name = "static_web",
    srcs = ["index.html"],
)

# Makes "local" copies of the grpcweb outputs available so they can be
# easily referenced by the second genrule.
# This is not ideal, and would be unnecessary if this was implemented through
# a custom rule or proper Provider.
genrule(
    name = "copy_greeter_grpcweb",
    srcs = ["//proto:greeter_grpcweb"],
    outs = [
        "greeter_pb.d.ts",
        "greeter_grpc_web_pb.d.ts",
        "greeter_pb.js",
        "greeter_grpc_web_pb.js",
    ],
    cmd = "cp $(SRCS) $(@D)",
)

# A custom call to the rollup "transpiler".
# Again, this is not ideal, and should be avoided.
# Using more modern ts_library and rollup rules from Aspect, or creating
# our own would help with this.
genrule(
    name = "custom_roll",
    srcs = [
        ":rollup.config.js",
        ":main.js",
        ":greeter_pb.js",
	":greeter_grpc_web_pb.js",
	"@npm//google-protobuf",
	"@npm//grpc-web",
	"@npm//@rollup/plugin-commonjs",
    ],
    tools = [
      "@npm//rollup/bin:rollup",
    ],
    outs = [
        "js/main.js",
	"js/greeter_pb.js",
	"js/greeter_grpc_web_pb.js",
	"js/google-protobuf.js",
	"js/index.js",
	"js/helpers.js",
    ],
    cmd = """
	ln -sv bazel-out/k8-fastbuild/bin/external/npm/node_modules

	echo @D: $(@D)
        echo "PWD: $$PWD"
	tree -d -L 5

	$(location @npm//rollup/bin:rollup) \
	  --format es \
	  -p commonjs \
	  --preserveEntrySignatures strict \
	  --config $(location rollup.config.js) \
	  -i $(location :main.js) \
	  -i $(location :greeter_pb.js) \
	  -i $(location :greeter_grpc_web_pb.js) \
	  -i $(location @npm//grpc-web)/index.js \
	  -i $(location @npm//google-protobuf)/google-protobuf.js \
	  -d $(@D)/js

	# Avoid the random hash in filename of output files. (Luckily, there's only one)
	helpers=$$(basename $(@D)/js/_commonjsHelpers-*js)
	mv $(@D)/js/$$helpers $(@D)/js/helpers.js
	sed -i "s/$$helpers/helpers.js/" $(@D)/js/*js

	echo "-------------------"
	#tree -L 5 -d
	echo "-------------------"
	tree $(@D)
    """
    )

sh_binary(
    name = "local_server",
    srcs = ["local_server.sh"],
    data = [
        ":static_web",
	":custom_roll",
    ],
)
