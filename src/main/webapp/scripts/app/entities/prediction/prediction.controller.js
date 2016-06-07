'use strict';

angular.module('footierepoApp')
    .controller('PredictionController', function ($scope, $state, Prediction, Fixture, Principal) {


        $scope.predictionsEnd = new Date("June 10, 2016 00:00:00");
        $scope.currentDate = new Date();

        $scope.groups = [];

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

                var tmpGroups = {};
                //create groups start
                for(var i=0;i<result.length;i++){
                    if(tmpGroups[result[i].group] == undefined){
                        tmpGroups[result[i].group] = {};
                        tmpGroups[result[i].group].name = result[i].group;
                        tmpGroups[result[i].group].teams = {};
                    }else{
                        if(tmpGroups[result[i].group].teams[result[i].home] == undefined){
                            tmpGroups[result[i].group].teams[result[i].home] = {};
                            tmpGroups[result[i].group].teams[result[i].home].name = result[i].home;
                            tmpGroups[result[i].group].teams[result[i].home].points = 0;
                            tmpGroups[result[i].group].teams[result[i].home].goals = 0;
                            tmpGroups[result[i].group].teams[result[i].home].goalsa = 0;
                        }
                        if(tmpGroups[result[i].group].teams[result[i].away] == undefined){
                            tmpGroups[result[i].group].teams[result[i].away] = {};
                            tmpGroups[result[i].group].teams[result[i].away].name = result[i].away;
                            tmpGroups[result[i].group].teams[result[i].away].points = 0;
                            tmpGroups[result[i].group].teams[result[i].away].goals = 0;
                            tmpGroups[result[i].group].teams[result[i].away].goalsa = 0;
                        }
                    }
                }


                for(var g in tmpGroups){
                    var _teams = [];
                    for(var t in tmpGroups[g].teams){
                        _teams.push(tmpGroups[g].teams[t]);
                    }
                    tmpGroups[g].teams = _teams;
                    $scope.groups.push(tmpGroups[g]);
                }
                //create groups end

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

        $scope.fotis = function(fixtureKey , value){

            var _home = $("#key_"+fixtureKey+"_home").val();
            var _away = $("#key_"+fixtureKey+"_away").val();

            if(!isNaN(_home) && !isNaN(_away) && _home!= undefined && _away!= undefined){
                if(_home > _away){
                    console.debug('_home > _away');
                }else if(_home == _away){
                    console.debug('_home == _away');
                }else{
                    console.debug('_home < _away');
                }
            }


            var _group = this.getGroupByFixtureId(fixtureKey);
            console.debug(_group);

        }
    });
