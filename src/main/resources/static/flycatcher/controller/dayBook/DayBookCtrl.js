'use strict';
(function() {
	angular.module('myApp.account').controller('DayBookCtrl', function($scope, $http, $attrs, $location) {
		$scope.dayBookList = null;
		$scope.ediMode = false;
		
		$scope.init =function(){
			$http({
	    	    method: 'GET',
	    	    url: '/api/v1/dayBooks',
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.dayBookList = data.dayBookInfos;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
		};
	
	   $scope.editDayBook = function(daybook){
		   $scope.ediMode = true;
		   $scope.daybook = daybook;	
	   };
	   
	   
	   $scope.saveOrUpdateDaybook= function(myform,daybook){
		   myform.dirty = false;
		   if($scope.ediMode){
			   $scope.addDayBook(myform,daybook);
		   }else{
			   $scope.editDayBook(myform,daybook);
		   }
	   }
	   
	   $scope.editDayBook = function (myform,daybook){
		   $http({
	    	    method: 'PUT',
	    	    url: '/api/v1/dayBooks'+daybook.dayBookId,
	    	    data  : daybook,
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.init();
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
	   }
	   
	   
	   $scope.addDayBook = function (myform,daybook){
		   $http({
	    	    method: 'POST',
	    	    url: '/api/v1/dayBooks',
	    	    data  : daybook,
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.init();
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
	   }
	});
}());