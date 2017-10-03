'use strict';

angular.module('footierepoApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';
        $scope.isSocial = false;

         Principal.identity(false).then(function(data) {

           $scope.isSocial =  data.social;
           console.log($scope.isSocial);
        });
        $scope.logout = function () {
        	        console.log(Principal);

            Auth.logout();
            $state.go('login');
        };
    });
