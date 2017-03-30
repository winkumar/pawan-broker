'use strict';

angular.module('myApp.account',[]);
angular.module('myApp.login',[]);
angular.module('myApp.dayBook',[]);
angular.module('myApp.journal',[]);

var myApp = angular.module('myApp',['myApp.account','myApp.login','myApp.dayBook','myApp.journal']);
myApp.config(function($routeProvider, $locationProvider) {
  $routeProvider
  	.when('/', {templateUrl: '/flycatcher/screen/login/Login.html'})
    .when('/account', {templateUrl: '/flycatcher/screen/account/account.create.html'})
    .when('/daybook', {templateUrl: '/flycatcher/screen/dayBook/daybook.entry.html'})
    .when('/journalView', {templateUrl: '/flycatcher/screen/journal/journal.view.html'})
    .otherwise({redirectTo: '/'})
})


