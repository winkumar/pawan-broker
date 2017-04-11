'use strict';

angular.module('myApp.account',['ngMessages']);
angular.module('myApp.login',[]);
angular.module('myApp.dayBook',[]);
angular.module('myApp.journal',[]);
angular.module('myApp.balanceSheet',[]);

var denpency = [
				'ngRoute',
				'ngIdle',
				'ngStorage',
				'myApp.account',
				'myApp.login',
				'myApp.dayBook',
				'myApp.journal',
				'myApp.balanceSheet'
				];

var myApp = angular.module('myApp',denpency);

myApp.config(function($routeProvider, $locationProvider,$httpProvider) {
  $routeProvider
  	.when('/', {templateUrl: '/flycatcher/screen/login/Login.html'})
    .when('/account', {templateUrl: '/flycatcher/screen/account/account.create.html'})
    .when('/daybook', {templateUrl: '/flycatcher/screen/dayBook/daybook.entry.html'})
    .when('/journalView', {templateUrl: '/flycatcher/screen/journal/journal.view.html'})
    .when('/balanceSheetView', {templateUrl: '/flycatcher/screen/balanceSheet/balance.sheet.html'})
    .otherwise({redirectTo: '/'})
});

myApp.config(function(IdleProvider, KeepaliveProvider) {
	 IdleProvider.idle(5);
	 IdleProvider.timeout(5);
	 KeepaliveProvider.interval(10);
});

myApp.run(function(Idle,api,$q,$rootScope,$window,$localStorage) {
	  api.init();
	  Idle.watch();
	  $rootScope.$on('IdleTimeout', function(){
		  //$window.location.href ="/";
	  });
});

myApp.factory('api', function($http,$localStorage) {
	return {
		init : function(token) {
			$http.defaults.headers.common['X-Access-Token'] = token || $localStorage.token;
		}
	};
});


myApp.factory('authInterceptor', ['$rootScope', '$q', '$location', '$timeout', function ($rootScope, $q, $location, $timeout) {
	return {
	    request: function (config) {
	      delete $rootScope.errorKey;
	      
	      if(!/\.html/.test(config.url)) {
	        var defer = $q.defer();
	        config.timeout = defer.promise;
	      }
	      return config;
	    },
	    responseError: function (response) {
	      var status = response.status;
	      if(status === 401) {
	        $timeout(function () {
	          $location.path('/login');
	        }, 300);

	      } else if(status !== 0) {
	        $rootScope.showErrorMsg = true;
	        $timeout(function() {
	          $rootScope.showErrorMsg = false;
	        }, 200);
	      }
	      return $q.reject(response);
	    }
	  };
}]);

//myApp.config(['$httpProvider', function($httpProvider) {
//	  $httpProvider.interceptors.push('authInterceptor');
//}]);
 