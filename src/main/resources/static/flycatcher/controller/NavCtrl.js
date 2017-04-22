'use strict';
(function() {
	angular.module('myApp.NavCtrl').controller('NavCtrl', function($scope, $http, $attrs, $location,$localStorage) {
		
		$scope.logout = function(){
			$localStorage.token = "";
            $location.path('#/');
		}
	});
}());