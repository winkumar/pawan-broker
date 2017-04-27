'use strict';
(function () {
    angular.module('myApp.account').controller('AccountCtrl', function ($scope, $http, $attrs, $location,$localStorage,$rootScope,api) {
    	$scope.accountDetails = null;
    	$scope.account = null;
    	$scope.editMode = false;
    	
    	$scope.init = function(){
    	   var url = "/api/v1/accounts?sort=ASC";
      	   $http({
  	    	    method: 'GET',
  	    	    url: url,
  	    	}).success(function(data, status, headers, config){
  	    		$scope.accountDetails = data;
  	    		$scope.accountTypes();
  	    	}).error(function(data, status, headers, config){
  	    	});
         };
         
        $scope.deleteAccount = function (accountId) {
        	var url = "/api/v1/accounts/"+accountId
        	   $http({
    	    	    method: 'DELETE',
    	    	    url: url,
    	    	}).success(function(data, status, headers, config){
    	    		$scope.init();
    	    		$scope.editMode = false;
    	    	}).error(function(data, status, headers, config){
    	    	})
        };
        
        $scope.addAccount = function (myform,account) {
           if($scope.editMode){
        	   $scope.editAndSave(myform,account);
           }else{
        	   $scope.saveAccount(myform,account);
           }
        };

        $scope.edit = function (account) {
        	$scope.editMode = true;
        	$scope.account = account;
        };
        
        $scope.editAndSave = function (myform,account) {
          var url = "/api/v1/accounts/"+account.accountId
       	   $http({
   	    	    method: 'PUT',
   	    	    url: url,
   	    	    data : account,
   	    	}).success(function(data, status, headers, config){
   	    		$scope.init();
   	    		$scope.account =null;
   	    		$scope.editMode = false;
   	    		myform.$dirty = false;
   	    	}).error(function(data, status, headers, config){
   	    	});
        };
        
        $scope.saveAccount = function (myform,account) {
        	account.lastName = account.firstName;
            var url = "/api/v1/accounts";
            $http({
	    	    method: 'POST',
	    	    url: url,
	    	    data : account,
	    	}).success(function(data, status, headers, config){
	    		$scope.init();
	    		$scope.account =null;
	    		myform.$dirty = false;
	    	}).error(function(data, status, headers, config){
	    	});
         };
         
         
        $scope.isValidate=function(myform,ele){
        	if(myform.$dirty && ele.$touched && ele.$invalid)
        		return true;
        	return false;
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
 	   }
      
        $scope.clear = function (myform) {
        	if(myform.$dirty)
        	 $scope.account =null;
        }
        
        $scope.getNextAndPreviousPage = function(url){
        	$http({
 	    	    method: 'GET',
 	    	    url: url,
 	    	    headers: {'Content-Type': 'application/json'}
 	    	}).success(function(data, status, headers, config){
 	    		$scope.accountDetails = data;
 	    	}).error(function(data, status, headers, config){
 	    		$scope.errormessage = data.message;
 	    	});
        }
        
        $scope.next = function () {
        	var currentPage = $scope.accountDetails.pagePropertys.pageNumber+1;
        	var totalPage = $scope.accountDetails.pagePropertys.totalPages;
        	if(currentPage < totalPage) 
        		$scope.getNextAndPreviousPage('/api/v1/accounts?page='+currentPage+'&sort=ASC');
        };
        
        $scope.previous = function () {
        	var currentPage = $scope.accountDetails.pagePropertys.pageNumber-1;
        	var totalPage = $scope.accountDetails.pagePropertys.totalPages;
        	if(currentPage >= 0) 
        		$scope.getNextAndPreviousPage('/api/v1/accounts?page='+currentPage+'&sort=ASC');
        };
    });
} ());