'use strict';
(function() {
	angular.module('myApp.NavCtrl').controller('NavCtrl', function($scope, $http, $attrs, $location,$localStorage,api) {
		
		$scope.logout = function(){
			$localStorage.token = "";
			api.init(null);
            $location.path('#/');
		}
	});
}());