'use strict';

angular.module('footierepoApp')
    .controller('PropertiesController', function ($scope, $state, Properties) {

        $scope.propertiess = [];
        $scope.loadAll = function() {
            Properties.query(function(result) {
               $scope.propertiess = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.properties = {
                displayStandings: null,
                id: null
            };
        };
    });
