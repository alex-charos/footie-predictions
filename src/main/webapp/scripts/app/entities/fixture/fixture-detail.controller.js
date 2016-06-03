'use strict';

angular.module('footierepoApp')
    .controller('FixtureDetailController', function ($scope, $rootScope, $stateParams, entity, Fixture) {
        $scope.fixture = entity;
        $scope.load = function (id) {
            Fixture.get({id: id}, function(result) {
                $scope.fixture = result;
            });
        };
        var unsubscribe = $rootScope.$on('footierepoApp:fixtureUpdate', function(event, result) {
            $scope.fixture = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
