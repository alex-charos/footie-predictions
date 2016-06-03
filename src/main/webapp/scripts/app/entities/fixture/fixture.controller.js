'use strict';

angular.module('footierepoApp')
    .controller('FixtureController', function ($scope, $state, Fixture) {

        $scope.fixtures = [];
        $scope.loadAll = function() {
            Fixture.query(function(result) {
               $scope.fixtures = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.fixture = {
                home: null,
                away: null,
                homeGoals: null,
                awayGoals: null,
                hasResult: null,
                result: null,
                group: null,
                homeFlag: null,
                awayFlag: null,
                id: null
            };
        };
    });
