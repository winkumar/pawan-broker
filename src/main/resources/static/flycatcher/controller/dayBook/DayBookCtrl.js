'use strict';
(function() {
	angular.module('myApp.account').controller('DayBookCtrl', function($scope, $http, $attrs, $location,$filter) {
		$scope.dayBookList = null;
		$scope.ediMode = false;
		$scope.accountList = null;
		$scope.reportStartDate=$filter('date')(new Date(),'dd-MM-yyyy'); 
    	$scope.reportEndDate=$filter('date')(new Date(),'dd-MM-yyyy'); 
    	$scope.todayDate = $filter('date')(new Date(),'dd-MM-yyyy');
    	
    	$scope.setTransactionDate = function(){
			 $scope.daybook = {
			    transactionDate : new Date()
			 }
	    }
		
		$scope.init =function(){
			$scope.setTransactionDate();
			$scope.hide(false);
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
	
		$scope.editBook = function(daybook,accountType){
		   $scope.ediMode = true;
		   daybook.accountType = accountType;
		   $scope.loadAccounts(accountType);
		   daybook.transactionDate = new Date(daybook.transactionDate);
		   $scope.daybook = daybook;
	   };
	   
	   $scope.accountTypes = function(){
		   $http({
	    	    method: 'GET',
	    	    url: '/api/v1/accountTypes?sort=DESC',
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
	    		$scope.daybook = null;
	    		$scope.init();
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
	    		$scope.daybook = null;
	    		$scope.init();
	    		myform.$dirty = false;
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
	    		$scope.daybook = null;
	    		$scope.init();
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
	   };
	   
	   $scope.exportData = function () {
	        var blob = new Blob([document.getElementById('daybookContent').innerHTML], {
	            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
	        });
	        saveAs(blob, "DayBook-"+$scope.todayDate+".xls");
	   };
	    
	   $scope.hide = function(val){
		   $scope.dayBookView = val;
		   $scope.search(null);
	   } 
	   
	   $scope.daybookList = function(url){
		   $http({
	    	    method: 'GET',
	    	    url: url,
	    	    headers: {'Content-Type': 'application/json'}
	    	}).success(function(data, status, headers, config){
	    		$scope.dayBookSearchList = data;
	    	}).error(function(data, status, headers, config){
	    		$scope.errormessage = data.message;
	    	});
	   }
	   
	   
	    $scope.search = function(data){
	    	var url = "/api/v1/dayBooks/all?sort=ASC";
	    	if(data == null || data == undefined){
	    		$scope.daybookList(url);
	    	}else{
	    	if(data.startDate){
	    		url +="&startDate="+ $filter('date')(data.startDate,'dd-MM-yyyy'); 
	    		$scope.reportStartDate=$filter('date')(data.startDate,'dd-MM-yyyy'); 
	    	}
	    	if(data.endDate){
	    		url +="&endDate="+ $filter('date')(data.endDate,'dd-MM-yyyy'); 
	    		$scope.reportEndDate=$filter('date')(data.endDate,'dd-MM-yyyy'); 
	    	}
	    	 $scope.daybookList(url);
	    	}
	    };
	   	   
	});
}());