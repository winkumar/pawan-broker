(function() {
    angular.module('myApp.login').controller('LoginCtrl', function($scope, $http, $attrs, $location,$rootScope,$cookieStore,api) {
        $scope.login = function(){
        	
    	    	$scope.dataToPost = {
    	    			username:$scope.username,
    	    			password:$scope.password
    	    	};
    	    	$http({
    	    	    method: 'POST',
    	    	    url: '/api/v1/auth/login',
    	    	    data: $scope.dataToPost,
    	    	    headers: {'Content-Type': 'application/json'}
    	    	}).success(function(data, status, headers, config){
    	    		var token = data.accessToken;
    	            api.init(token);
    	            $cookieStore.put('token', token);
    	            $location.path('/account');
    	    	}).error(function(data, status, headers, config){
    	    		$scope.errormessage = data.message;
    	    	});
    	     }
    });
} ());