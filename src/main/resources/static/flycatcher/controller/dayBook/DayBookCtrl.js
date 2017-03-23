'use strict';
(function() {
	angular.module('myApp.account').controller('DayBookCtrl',
			function($scope, $http, $attrs, $location) {
				$scope.daybook = {
					'date':'',
					'account':'',
					'amount':'',
					'paise':'',
					'type':'',
					'description':''
				};
				var dayBookList = [];
				dayBookList.push($scope.daybook)
			});
}());