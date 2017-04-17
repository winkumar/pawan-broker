'use strict';
(function() {
	angular.module('myApp.balanceSheet').controller('BalanceSheetCtrl',function($scope, $http, $attrs, $location,$filter) {
			
		$scope.balanceSheetList = null;
		$scope.reportStartDate=$filter('date')(new Date(),'dd-MM-yyyy'); 
    	$scope.reportEndDate=$filter('date')(new Date(),'dd-MM-yyyy'); 
    	$scope.todayDate = $filter('date')(new Date(),'dd-MM-yyyy'); 
    	
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
	    		$scope.reportStartDate=$filter('date')(balanceSheetSearch.startDate,'dd-MM-yyyy'); 
	    	}
	    	if(balanceSheetSearch.endDate){
	    		url +="&endDate="+ $filter('date')(balanceSheetSearch.endDate,'dd-MM-yyyy');
	    		$scope.reportEndDate=$filter('date')(balanceSheetSearch.endDate,'dd-MM-yyyy'); 
	    	}
	    	$scope.getBalance(url);
	    };
	    
	    $scope.exportData = function (balanceSheetSearch) {
	        var blob = new Blob([document.getElementById('balanceSheetContent').innerHTML], {
	            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
	        });
	        saveAs(blob, "BalanceSheetReport-"+$scope.todayDate+".xls");
	    };
	    
	});
}());