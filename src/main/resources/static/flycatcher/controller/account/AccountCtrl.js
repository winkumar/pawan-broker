'use strict';
(function () {
    angular.module('myApp.account').controller('AccountCtrl', function ($scope, $http, $attrs, $location,$rootScope,$cookieStore,api) {
    	$scope.accountDetails = null;
    	$scope.init = function(){
      	   var url = "/api/v1/accounts?page=0&size=4&sort=ASC";
      	   $http({
  	    	    method: 'GET',
  	    	    url: url,
  	    	}).success(function(data, status, headers, config){
  	    		$scope.accountDetails = data;
  	    	}).error(function(data, status, headers, config){
  	    	});
         };
        var compdata = [];
        $scope.editMode = false;
        $scope.account = {
        	  "area": "string",
        	  "city": "string",
        	  "currentAddress": "string",
        	  "fatherName": "string",
        	  "firstName": "string",
        	  "lastName": "string",
        	  "pinCode": "string",
        	  "presentAddress": "string",
        	  "state": "string"
        	}
        compdata.push($scope.account);
        $scope.accountList = null;
        $scope.account = null;
        $scope.deleteAccount = function (index) {
            $scope.accountList.splice(index, 1);
            $scope.account = null;
        }
        $scope.addAccount = function (data) {
            if (!$scope.editMode) {
                data.accountNo = "AC102";
                compdata.push(data);
                $scope.accountList = compdata;
            }
            $scope.editMode = false;
            $scope.account = null;
        }

        $scope.edit = function (account) {
            $scope.editMode = true;
            $scope.account = account;
        }
        
        $scope.isValidate=function(myform,ele){
        	if(myform.$dirty && ele.$touched && ele.$invalid)
        		return true;
        	 return false;
        }
       
    });
} ());