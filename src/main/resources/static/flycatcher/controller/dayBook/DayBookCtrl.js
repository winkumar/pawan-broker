'use strict';
(function() {
	angular.module('myApp.account').controller('DayBookCtrl', function($scope, $http, $attrs, $location,$filter) {
		$scope.dayBookList = null;
		$scope.ediMode = false;
		$scope.accountList = null;
		$scope.daybook = null;
		$scope.init =function(){
			$http({
	    	    method: 'GET',
	    	    url: '/api/v1/dayBooks',
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.dayBookList = data.dayBookInfos;
	    		$scope.accountTypes();
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
		};
	
		$scope.editBook = function(daybook){
		   $scope.ediMode = true;
		   daybook.accountType = 1;
		   $scope.loadAccounts(daybook.accountType);
		   daybook.transactionDate = new Date(daybook.transactionDate);
		   $scope.daybook = daybook;
	   };
	   
	   $scope.accountTypes = function(){
		   $http({
	    	    method: 'GET',
	    	    url: '/api/v1/accountTypes?sort=ASC',
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.accountTypeList = data;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
	   };
	   
	   $scope.loadAccounts = function(accountTypeId){
		 if(accountTypeId !== undefined && accountTypeId!==null){
		   $http({
	    	    method: 'GET',
	    	    url: '/api/v1/accountTypes/'+accountTypeId+'/accounts?sort=ASC',
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.accountList = data;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
		   }
	   }
	   
	   $scope.isValidate=function(myform,ele){
       	if(myform.$dirty && ele.$touched && ele.$invalid)
       		return true;
       	return false;
       };
       
	   $scope.saveOrUpdateDaybook= function(myform,daybook){
		   myform.dirty = false;
		   if($scope.ediMode){
			   $scope.editDayBook(myform,daybook);
			   $scope.ediMode =false;
		   }else{
			   $scope.addDayBook(myform,daybook);
		   }
	   }
	   
	   $scope.concatName = function(v1,v2){
		   if(v1 !== null && v2 !== null){
			   return (v1 +"-"+v2);
		   }
	   }
	   
	   $scope.editDayBook = function (myform,daybook){
		   $http({
	    	    method: 'PUT',
	    	    url: '/api/v1/dayBooks/'+daybook.dayBookId,
	    	    data  : daybook,
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.init();
	    		$scope.daybook = null;
	    		myform.$dirty = false;
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
	    		myform.$dirty = false;
	    		$scope.daybook = null;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
	   }
	   
	   $scope.deleteBook = function (daybook){
		   $http({
	    	    method: 'DELETE',
	    	    url: '/api/v1/dayBooks/'+daybook.dayBookId,
	    	    data  : daybook,
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.init();
	    		$scope.daybook = null;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
	   }
	   
	});
}());