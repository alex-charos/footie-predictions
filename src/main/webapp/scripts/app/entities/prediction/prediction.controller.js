'use strict';

angular.module('footierepoApp')
    .controller('PredictionController', function ($scope, $state, Prediction, Fixture, Principal) {


        $scope.predictionsEnd = new Date("June 10, 2016 00:00:00");
        $scope.currentDate = new Date();

        $scope.getid = function(){
            Principal.identity().then(function(data) {

                $scope.username = data.login;
                $scope.loadAll();

            });
        };

        $scope.getid();
        $scope.predictions = {};
        $scope.fixtures = [];
        $scope.editPredictions = function() {

            return $scope.currentDate <= $scope.predictionsEnd;
        }
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

                    $scope.fixtures.sort(function(a,b) {
                        if(a.group != b.group){
                            return a.group > b.group;
                        }else{
                            return a.order < b.order;
                        }
                    });

                    for (var i =0; i< $scope.fixtures.length; i++) {
                        if (pred.resultPerEvent === null || pred.resultPerEvent === undefined) {
                            pred.resultPerEvent = {};
                        }
                        if (pred.resultPerEvent[$scope.fixtures[i].id] === undefined || pred.resultPerEvent[$scope.fixtures[i].id] ===null )  {
                            pred.resultPerEvent[$scope.fixtures[i].id] = {homeScore:'', awayScore:''};
                        }
                    }

                    $scope.predictions = pred;

                });





            });
        };

        $scope.getGroupByFixtureId = function(id) {
             for (var i =0; i< $scope.fixtures.length; i++) {
                if ($scope.fixtures[i].id === id) {
                    return $scope.fixtures[i].group;
                }
              }

              return "n/a";

        }
        $scope.getHomeFlagByFixtureId = function(id){
              for (var i =0; i< $scope.fixtures.length; i++) {
                if ($scope.fixtures[i].id === id) {
                    return $scope.fixtures[i].homeFlag;
                }
              }

              return "n/a";
        }
         $scope.getAwayFlagByFixtureId = function(id){
              for (var i =0; i< $scope.fixtures.length; i++) {
                if ($scope.fixtures[i].id === id) {
                    return $scope.fixtures[i].awayFlag;
                }
              }

              return "n/a";
        }
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
