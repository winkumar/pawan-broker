'use strict';
(function() {
	angular.module('myApp.journal').controller('JournalCtrl',function($scope, $http, $attrs, $location,$filter) {
			
		$scope.journalList = null;
		$scope.journalSearch = {
			  startDate : "",
			  endDate : ""
		};
		
		$scope.init = function(){
			$scope.getJournal('/api/v1/journals?sort=ASC');
		};
		
		
		$scope.getJournal = function(url){
			$http({
				method : 'GET',
				url : url,
				headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.journalList = data;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
		}
		
		
		$scope.isValidate=function(myform,ele){
	       	if(myform.$dirty && ele.$touched && ele.$invalid)
	       		return true;
	       	return false;
	    };
	    
	    $scope.search = function(journalSearch){
	    	var url = '/api/v1/journals?sort=ASC';
	    	if(journalSearch.startDate){
	    		url +="&startDate="+ $filter('date')(journalSearch.startDate,'dd-MM-yyyy'); 
	    	}
	    	if(journalSearch.endDate){
	    		url +="&endDate="+ $filter('date')(journalSearch.endDate,'dd-MM-yyyy');; 
	    	}
	    	$scope.getJournal(url);
	    }
	});
}());