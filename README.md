![test result](https://github.com/hblok/grpc-web-bazel-ts-example/actions/workflows/main.yml/badge.svg)


# Example: gRPC + ECMAScript + Envoy Proxy + Bazel

This example stack demonstrates a web front-end communicating through a GRPC client in JavaScript to a Java GRPC server, over an Envoy proxy. Based on a fork of [Braden Shepherdson's example](https://github.com/bshepherdson/grpc-web-bazel-ts-example), it takes a radically different approach in some areas.

In particular, I felt it was not necessary to use TypeScript everywhere. A strong point of Protocol Buffers and GRPC is after all communication and sharing across different languages. Therefore I opted for a simple Java server on the back-end. Furthermore, I shortcut the static web server with the Python embedded 'http.server' module. Obviously, not for a production setup. Finally, I used the modern Envoy proxy, routing both static page and GRPC requests, which also avoids the CORS problem Shepherdson mentioned.

In summary, this stack includes:
- A Python web server for static content.
- A Java GRPC server.
- An Envoy proxy.
- Bazel for building everything


## Prerequisites

This example assumes a Debian or Ubuntu based OS.

To build and run this demo, you'll need to install the following:

- Python 3
- Java JDK (11 or newer)
- Bazel
- NPM, Yarnpkg
- Docker

The following should take you far:

```
apt install python3 npm yarnpkg openjdk-21-jdk-headless
```

Install Bazel [according to these instructions](https://bazel.build/install/ubuntu#install-on-ubuntu). I personally prefer the Apt repository option.

Install Docker [as described here](https://docs.docker.com/engine/install/ubuntu/#install-using-the-repository). Again, I prefer the Apt repository install. 

## Starting

Once you have everything installed, clone this project, and build with:

```
cd grpc-web-bazel-ts-example
bazel build ...
```

The first time this executes, everything will be downloaded and compiled, which can take quite some time. There are many dependencies to fetch, including Bazel dependencies, NPM packages, Java packages. Usually, the entire GRPC stack is compiled from source. However, the good news is that Bazel is brilliant at caching its work, so the following executions will be blazing fast.

To start the three server instances, use three terminal windows to execute each of the following:

```
bazel run //server:GreeterServer
```

```
bazel run //web:local_server
```

```
proxy/start_proxy.sh
```

Notice that the last starts a Docker container for the Envoy proxy. The first time, its base images are downloaded, at about 150 MB.

Alternatively, you can start all in the same window, although this will garble the log output from the different servers:

```
cd grpc-web-bazel-ts-example
./start_all.sh
```


## Architecture

Here's a brief overview of the components of the stack.

### Protocol Buffers

These are defined in [proto/greeter.proto](proto/greeter.proto), giving the service definitions.
[proto/BUILD](proto/BUILD) generates code from these protos for both the Java server and the gRPC-web side.

### ECMAScript for the web browser

Although the Bazel rule for the proto is called `js_grpc_web_compile`, it is not immediately ready to be used with a web browser. Together with its supporting libraries of grpc-web and google-protobuf, it has to be converted (aka "transpiled"). There are many ways to skin this cat, but I opted for the Rollup route. However, here I hit a lot of problems.

There is a [rollup_bundle rule](https://www.npmjs.com/package/@bazel/rollup) for Bazel, however, it did not produce the output I needed. Therefore, I ended up rolling my own call to `rollup`, using a Bazel `genrule`. It's not pretty, and the area which screams most for improvement in this example. Or in other words, don't copy this part of the code!

### Python web server for static content

To server the static .html and .js files, I'm starting the simple HTTP server embedded as a module in Python. It's simple, but works. Again, this is a not a production example, so do not replicate this in production.

### Java GRPC server

The Java server is very small and simple. It's the bare minimum of a GRPC server. It replies to incoming "hello" requests by repeating the incoming name. Notice that each key-press in the text field of the web site will result in a new call.

Again I had some problems with outdated dependencies. Using a more modern JDK and Bazel bzl MODULE should hopefully improve this.

### Envoy proxy server

The Envoy proxy has emerged as a industry darling for application level routing. Although, its configuration options and format is pretty daunting. (YAML is such a horrible tool for formatting code). But, for this example, it works well enough.

Worth noting, is the filtering and routing of static vs. GRPC requests. The key here is the filtering section which takes URLs with a `/api` prefix and send them to the GRPC server (But notice the rewrite rule which drop the "/api" part first). Everything else is routed to the static Python server.

```
routes:
  - match: { prefix: "/api/" }
    route:
      prefix_rewrite: "/"
      cluster: greeter_service
  - match: { prefix: "/" }
    route:
      cluster: static_service
```

### Bazel

Tying it all together is Google's Bazel (aka Blaze) build framework. With support for all major languages, and a plethora of plugin rules, it makes defining the compile process and build dependencies a breeze. Or so is at least the promise.

The integration with JavaScript for a browser client was somewhat underwhelming. Compared to other languages and integration, the bar to entry was surprisingly high. In the end, the existing `rollup_bundle` rule proved ill-suited, so instead a custom call to Rollup under NPM was needed. However, developing for another complex build and repository framework from within Bazel is extremely tedious.

All of this is not Bazel's fault. Rather, web development and integration with many backend technologies is still a jungle, some twenty years after the dawn of Web 2.0, AJAX et.al.

On the bright side, this leave plenty of opportunity for improvements. Hopefully some will come under this repo in the near future.


## Future work

Taking Shepherdson's example from four years back into a modern environment has proved an interesting exercise. But there are still areas to improve on:

- Upgrade from the Bazel WORKSPACE to [MODULE.bazel](https://bazel.build/rules/lib/globals/module).
- Upgrade and use the [rollup_bundle rule](https://www.npmjs.com/package/@bazel/rollup).
- Consider the [Aspect Build rules and tools](https://docs.aspect.build/).
- Ensure a modern JDK version is used throughout.
- Revisit Python integration and a Python GRPC server.


## References

While working on this example, the following resources proved useful:

- [gRPC tutorial for Java](https://grpc.io/docs/languages/java/basics/#defining-the-service)
- [@bazel/rollup rule documentation](https://www.npmjs.com/package/@bazel/rollup)
- [Rollup CLI documentation](https://rollupjs.org/command-line-interface/)
- [Rollup.js cheat-sheet](https://devhints.io/rollup)
- [gRPC-web tutorial](https://grpc.io/docs/platforms/web/basics/)
- [Envoy gRPC](https://www.envoyproxy.io/docs/envoy/v1.31.2/intro/arch_overview/other_protocols/grpc#arch-overview-grpc)
- [Eugene Khabarov's gRPC, Bazel, Envoy example](https://ekhabarov.com/post/envoy-as-an-api-gateway-grpc-microservice/)
