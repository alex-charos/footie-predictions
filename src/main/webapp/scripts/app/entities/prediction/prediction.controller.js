'use strict';

angular.module('footierepoApp')
    .controller('PredictionController', function ($scope, $state, Prediction, Fixture) {

        $scope.predictions = {};
        $scope.fixtures = [];
        $scope.username = "test";
        $scope.save = function(){
            Prediction.update($scope.predictions);
        }
        $scope.loadAll = function() {
            Fixture.query(function(result) {
                $scope.fixtures = result;
                Prediction.get({"username" :$scope.username}, function(result) {
                    var pred = result;
                    if (pred === undefined || pred === null) {
                        pred = {username:$scope.username};
                    }

                    for (var i =0; i< $scope.fixtures.length; i++) {
                        if (pred.resultPerEvent === null || pred.resultPerEvent === undefined) {
                            pred.resultPerEvent = {};
                        }
                        if (pred.resultPerEvent[$scope.fixtures[i].id] === undefined || pred.resultPerEvent[$scope.fixtures[i].id] ===null )  {
                            pred.resultPerEvent[$scope.fixtures[i].id] = {homeScore:0, awayScore:0};
                        }
                    }

                    $scope.predictions = pred;
                   
                });



            });
        };
        $scope.loadAll();

        $scope.getHomeByFixtureId = function(id){
              for (var i =0; i< $scope.fixtures.length; i++) {
                if ($scope.fixtures[i].id === id) {
                    return $scope.fixtures[i].home;
                }
              }

              return "n/a";
        }
         $scope.getAwayByFixtureId = function(id){
              for (var i =0; i< $scope.fixtures.length; i++) {
                if ($scope.fixtures[i].id === id) {
                    return $scope.fixtures[i].away;
                }
              }

              return "n/a";
        }

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.prediction = {
                username: null,
                points: null,
                correctScores: null,
                correctResults: null,
                id: null
            };
        };
    });
