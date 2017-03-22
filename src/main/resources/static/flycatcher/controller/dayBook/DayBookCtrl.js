'use strict';
(function() {
	angular.module('myApp.account').controller('DayBookCtrl',
			function($scope, $http, $attrs, $location) {
				$scope.journal = {
					'date':'',
					'account':'',
					'amount':'',
					'paise':'',
					'type':'',
					'description':''
				};
			});
}());