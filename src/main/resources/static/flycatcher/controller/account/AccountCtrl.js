'use strict';
(function () {
    angular.module('myApp.account').controller('AccountCtrl', function ($scope, $http, $attrs, $location) {
        var compdata = [];
        $scope.editMode = false;
        //   $http.get('controllers/data.json').success(function(data) {
        //       for(var i=0;i<data.length;i++){
        //             compdata.push(data[i]);
        //       }
        //     });   
        $scope.account = {
            "date": "",
            "accountNo": "AC101",
            "name": "tomato",
            "fathername": "ffdfs",
            "address": "m nagar",
            "area": "vilupuram",
            "city": "",
            "state": "",
            "pincode": "",
            "remarks": ""
        };
        compdata.push($scope.account);
        $scope.accountList = compdata;
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