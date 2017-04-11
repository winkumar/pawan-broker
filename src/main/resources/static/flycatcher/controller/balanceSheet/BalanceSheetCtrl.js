'use strict';
(function() {
	angular.module('myApp.balanceSheet').controller('BalanceSheetCtrl',function($scope, $http, $attrs, $location,$filter) {
			
		$scope.balanceSheetList = null;
		$scope.balanceSheetSearch = {
			  startDate : "",
			  endDate : ""
		};
		
		$scope.init = function(){
			$scope.getBalance('/api/v1/balanceSheets?sort=ASC');
		};
		
		
		$scope.getBalance = function(url){
			$http({
				method : 'GET',
				url : url,
				headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.balanceSheetList = data;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
		}
		
		
		$scope.isValidate=function(myform,ele){
	       	if(myform.$dirty && ele.$touched && ele.$invalid)
	       		return true;
	       	return false;
	    };
	    
	    $scope.search = function(balanceSheetSearch){
	    	var url = '/api/v1/balanceSheets?sort=ASC';
	    	if(balanceSheetSearch.startDate){
	    		url +="&startDate="+ $filter('date')(balanceSheetSearch.startDate,'dd-MM-yyyy'); 
	    	}
	    	if(balanceSheetSearch.endDate){
	    		url +="&endDate="+ $filter('date')(balanceSheetSearch.endDate,'dd-MM-yyyy');; 
	    	}
	    	$scope.getBalance(url);
	    }
	});
}());