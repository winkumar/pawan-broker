'use strict';
(function() {
	angular.module('myApp.journal').controller('JournalCtrl',function($scope, $http, $attrs, $location,$filter) {
			
		$scope.journalList = null;
		$scope.setJournalSearchDate = function(){
			$scope.journalSearch = {
				  startDate : new Date(),
				  endDate : new Date()
			}
		}
		
		$scope.reportStartDate=$filter('date')(new Date(),'dd-MM-yyyy'); 
    	$scope.reportEndDate=$filter('date')(new Date(),'dd-MM-yyyy'); 
    	$scope.todayDate = $filter('date')(new Date(),'dd-MM-yyyy'); 
    	
		$scope.init = function(){
			$scope.setJournalSearchDate();
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
	    	if(journalSearch === null || journalSearch === undefined){
	    		$scope.getJournal(url);
	    	}else{
	    	if(journalSearch.startDate){
	    		url +="&startDate="+ $filter('date')(journalSearch.startDate,'dd-MM-yyyy'); 
	    		$scope.reportStartDate=$filter('date')(journalSearch.startDate,'dd-MM-yyyy'); 
	    	}
	    	if(journalSearch.endDate){
	    		url +="&endDate="+ $filter('date')(journalSearch.endDate,'dd-MM-yyyy'); 
	    		$scope.reportEndDate=$filter('date')(journalSearch.endDate,'dd-MM-yyyy'); 
	    	}
	    	$scope.getJournal(url);
	    }};
	    
	    $scope.exportData = function (journalSearch) {
	        var blob = new Blob([document.getElementById('tableContent').innerHTML], {
	            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
	        });
	        saveAs(blob, "JournalReport-"+$scope.todayDate+".xls");
	    };
	    
	    $scope.clearsearch = function() {
	    	$scope.setJournalSearchDate();
	        $scope.search(null);
	     }; 
	});
}());