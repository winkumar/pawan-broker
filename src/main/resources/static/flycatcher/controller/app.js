
angular.module('myApp.account',['ngMessages']);
angular.module('myApp.login',[]);
angular.module('myApp.dayBook',[]);
angular.module('myApp.journal',[]);

var denpency = [
				'ngRoute',
				'ngCookies',
				'ngResource',
				'ngSanitize',
				'myApp.account',
				'myApp.login',
				'myApp.dayBook',
				'myApp.journal'
				];

var myApp = angular.module('myApp',denpency);

myApp.config(function($routeProvider, $locationProvider,$httpProvider) {
  $routeProvider
  	.when('/', {templateUrl: '/flycatcher/screen/login/Login.html'})
    .when('/account', {templateUrl: '/flycatcher/screen/account/account.create.html'})
    .when('/daybook', {templateUrl: '/flycatcher/screen/dayBook/daybook.entry.html'})
    .when('/journalView', {templateUrl: '/flycatcher/screen/journal/journal.view.html'})
    .otherwise({redirectTo: '/'})
});

myApp.run(function(api) {
	api.init();				
});

myApp.factory('api', function($http, $cookieStore) {
	return {
		init : function(token) {
			$http.defaults.headers.common['X-Access-Token'] = token || $cookieStore.token;
		}
	};
});


myApp.factory('authInterceptor', ['$rootScope', '$q', '$cookies', '$location', '$timeout', function ($rootScope, $q, $cookies, $location, $timeout) {
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
