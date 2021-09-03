module.exports = {
  pages: {
    index: {
      entry: 'src/main.js'
    }
  },
  publicPath: '/',
  devServer: {
    proxy: {
      '/back': {
        target: 'http://localhost:9999',
        pathRewrite:{'^/back':''},   //重写路径，利用正则表达式
        ws: true,           //用于支持WebSocket
        changeOrigin: true  //匿名代理
      }
    }
  }
}