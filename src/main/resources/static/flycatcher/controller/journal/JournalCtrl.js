'use strict';
(function() {
	angular.module('myApp.journal').controller('JournalCtrl',
			function($scope, $http, $attrs, $location) {
				var journalData = [];
				$http.get('/flycatcher/controller/journal/journal.json').success(function(data) {
					for (var i = 0; i < data.length; i++) {
						journalData.push(data[i]);
					}
				});
				$scope.journalList = journalData;
			});
}());