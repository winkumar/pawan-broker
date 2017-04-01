
angular.module('myApp.account',['ngMessages']);
angular.module('myApp.login',[]);
angular.module('myApp.dayBook',[]);
angular.module('myApp.journal',[]);

var denpency = ['ngRoute','myApp.account','myApp.login','myApp.dayBook','myApp.journal'];

var myApp = angular.module('myApp',denpency);
myApp.config(function($routeProvider, $locationProvider) {
  $routeProvider
  	.when('/', {templateUrl: '/flycatcher/screen/login/Login.html'})
    .when('/account', {templateUrl: '/flycatcher/screen/account/account.create.html'})
    .when('/daybook', {templateUrl: '/flycatcher/screen/dayBook/daybook.entry.html'})
    .when('/journalView', {templateUrl: '/flycatcher/screen/journal/journal.view.html'})
    .otherwise({redirectTo: '/'})
})


