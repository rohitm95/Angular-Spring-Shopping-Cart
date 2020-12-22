(function (window) {
  window.__env = window.__env || {};

  // API url

  window.__env.apiUrl = 'http://ec2-13-233-160-172.ap-south-1.compute.amazonaws.com:8080/covid-inventory';
  //window.__env.apiUrl = 'http://localhost:8080/covid-inventory';

  // Whether or not to enable debug mode
  // Setting this to false will disable console output
  window.__env.enableDebug = true;
}(this));
