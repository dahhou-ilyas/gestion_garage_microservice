const Eureka = require('eureka-js-client').Eureka;

const eurekaHost = process.env.EUREKA_HOST || 'localhost';
const eurekaPort = process.env.EUREKA_PORT || 8761;


const client = new Eureka({
  instance: {
    app: 'auth-service',
    hostName: process.env.HOSTNAME || 'localhost',
    ipAddr: process.env.IPADDR || '127.0.0.1',
    port: {
      '$': process.env.PORT || 3000,
      '@enabled': true
    },
    vipAddress: 'auth-service',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn'
    }
  },
  eureka: {
    host: eurekaHost,
    port: eurekaPort,
    servicePath: '/eureka/apps/'
  }
});
  
module.exports = client;