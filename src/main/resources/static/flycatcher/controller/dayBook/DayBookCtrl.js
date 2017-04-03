'use strict';
(function() {
	angular.module('myApp.account').controller('DayBookCtrl', function($scope, $http, $attrs, $location) {
				$scope.daybook = {
					'date':'',
					'account':'',
					'amount':'',
					'paise':'',
					'type':'',
					'description':''
				};
				var dayBooData = [];
				dayBooData.push($scope.daybook);
				$scope.dayBookList = dayBooData;
			});
}());