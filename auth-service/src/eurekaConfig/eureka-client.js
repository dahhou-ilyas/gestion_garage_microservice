const Eureka = require('eureka-js-client').Eureka;

const client = new Eureka({
  instance: {
    app: 'AUTH-SERVICE',
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
    },
    healthCheckUrl: `http://${process.env.HOSTNAME || 'localhost'}:${process.env.PORT || 3000}/health`,
    statusPageUrl: `http://${process.env.HOSTNAME || 'localhost'}:${process.env.PORT || 3000}/info`
  },
  eureka: {
    host: process.env.EUREKA_HOST || 'localhost',
    port: process.env.EUREKA_PORT || 8761,
    servicePath: '/eureka/apps/',
    fetchRegistryInterval: 30000,
    registerWithEureka: true,
    maxRetries: 5,
    requestRetryDelay: 5000
  }
});


client.start();

client.on('registered', () => {
  console.log('Registered with Eureka');
});

client.on('deregistered', () => {
  console.log('Deregistered from Eureka');
});

client.on('error', (error) => {
  console.error('Eureka Client Error:', error);
});

module.exports = client;