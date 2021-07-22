module.exports = {
  // proxy all webpack dev-server requests starting with /api
  // to our Spring Boot backend (localhost:8098) using http-proxy-middleware
  // see https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
    proxy: {
      "/api/realtime/service": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
      "/api/realtime": {
        target: "http://localhost:8080",
        ws: true,
        changeOrigin: true,
      },
      "/api/users": {
        target: "http://localhost:8080",
        ws: false,
        changeOrigin: true,
      },
      "/api/rules": {
        target: "http://localhost:8080",
        ws: false,
        changeOrigin: true,
      },
      "/api/event": {
        target: "http://localhost:8080",
        ws: false,
        changeOrigin: true,
      },
      "/api/alert": {
        target: "http://localhost:8080",
        ws: false,
        changeOrigin: true,
      },
    },
  },
  // Change build paths to make them Maven compatible
  // see https://cli.vuejs.org/config/
  outputDir: "target/dist",
  assetsDir: "static",
};
