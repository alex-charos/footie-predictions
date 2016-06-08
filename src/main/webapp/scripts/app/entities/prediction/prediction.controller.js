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
                            return a.order > b.order;
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

            var teams = {};

            for (var i =0; i< $scope.fixtures.length; i++) {
                var elmntH = $(".home_id_"+$scope.fixtures[i].id);
                var elmntHVal = $(".home_id_"+$scope.fixtures[i].id).val();
                var elmntHTeam = $(elmntH).attr("data-teamId");

                var elmntA = $(".away_id_"+$scope.fixtures[i].id);
                var elmntAVal = $(".away_id_"+$scope.fixtures[i].id).val();
                var elmntATeam = $(elmntA).attr("data-teamId");

                if(teams[elmntHTeam] == undefined){
                    teams[elmntHTeam] = {};
                    teams[elmntHTeam].points = 0;
                    teams[elmntHTeam].goals = 0;
                    teams[elmntHTeam].goalsa = 0;
                }
                if(teams[elmntATeam] == undefined){
                    teams[elmntATeam] = {};
                    teams[elmntATeam].points = 0;
                    teams[elmntATeam].goals = 0;
                    teams[elmntATeam].goalsa = 0;
                }

                if(!isNaN(elmntHVal) &&  !isNaN(elmntAVal) && elmntHVal != undefined && elmntAVal != undefined && elmntHVal != "" && elmntAVal != ""){
                    if(elmntHVal > elmntAVal){
                        teams[elmntHTeam].points += 3;
                    }else if(elmntHVal == elmntAVal){
                        teams[elmntHTeam].points += 1;
                        teams[elmntATeam].points += 1;
                    }else {
                        teams[elmntATeam].points += 3;
                    }
                    teams[elmntHTeam].goals += parseInt(elmntHVal , 10);
                    teams[elmntHTeam].goalsa += parseInt(elmntAVal , 10);
                    teams[elmntATeam].goals += parseInt(elmntAVal , 10);
                    teams[elmntATeam].goalsa += parseInt(elmntHVal , 10);
                }

            }

            for(var g in $scope.groups){
                for(var t in $scope.groups[g].teams){
                    $scope.groups[g].teams[t].points = teams[$scope.groups[g].teams[t].name].points;
                    $scope.groups[g].teams[t].goals = teams[$scope.groups[g].teams[t].name].goals;
                    $scope.groups[g].teams[t].goalsa = teams[$scope.groups[g].teams[t].name].goalsa;
                }
            }

        }
    });
