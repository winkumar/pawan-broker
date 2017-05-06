'use strict';

angular.module('myApp.NavCtrl',[]);
angular.module('myApp.account',['ngMessages']);
angular.module('myApp.login',[]);
angular.module('myApp.dayBook',[]);
angular.module('myApp.journal',[]);
angular.module('myApp.balanceSheet',[]);

var denpency = [
				'ngRoute',
				'ngIdle',
				'ngStorage',
				 'myApp.NavCtrl',
				'myApp.account',
				'myApp.login',
				'myApp.dayBook',
				'myApp.journal',
				'myApp.balanceSheet'
				];

var myApp = angular.module('myApp',denpency);

myApp.config(function($routeProvider, $locationProvider,$httpProvider,IdleProvider, KeepaliveProvider) {
  $routeProvider
  	.when('/', {templateUrl: '/flycatcher/screen/login/Login.html'})
    .when('/account', {templateUrl: '/flycatcher/screen/account/account.create.html'})
    .when('/daybook', {templateUrl: '/flycatcher/screen/dayBook/daybook.entry.html'})
    .when('/journalView', {templateUrl: '/flycatcher/screen/journal/journal.view.html'})
    .when('/balanceSheetView', {templateUrl: '/flycatcher/screen/balanceSheet/balance.sheet.html'})
    .otherwise({redirectTo: '/'})
    
    $httpProvider.interceptors.push('authInterceptor');
  	$httpProvider.defaults.transformRequest.push(function(data){
  		$('#loading').show();
  		return data;
  	});
     
  	 IdleProvider.idle(900);
	 IdleProvider.timeout(10);
	 KeepaliveProvider.interval(3);
});

myApp.run(function(Idle,api,$q,$rootScope,$window) {
	  api.init();
	  Idle.watch();
	  $rootScope.$on('IdleTimeout', function(){
		  $window.location.href ="/";
	  });
});

myApp.factory('api', function($http,$localStorage) {
	return {
		init : function(token) {
			$http.defaults.headers.common['X-Access-Token'] = token || $localStorage.token;
		}
	};
});


myApp.factory('authInterceptor', function ($rootScope, $q, $location,$injector) {
	return {
	    request: function (config) {
	      $('#loading').hide();	
	      var $http = $http || $injector.get('$http');
		  if($http.pendingRequests.length > 1){
			  $http.pendingRequests.filter(function(ele,index){
				  $http.pendingRequests.splice(index,1);
			  });
		  }	
	      return config;
	    },
	    response : function(response){
	      var $http = $http || $injector.get('$http');
	      if($http.pendingRequests.length < 1)
	    	$('#loading').hide();
	     return response;
	    },
	    responseError: function (response) {
	      return $q.reject(response);
	    }
	  };
});
