(function() {
    angular.module('myApp.login').controller('LoginCtrl', function($scope, $http, $attrs, $location) {
            $scope.getNextPage = function(model){
               if($scope.userName == "ram" && $scope.password == "123")
                     $location.path("/account");
           }
    });
} ());