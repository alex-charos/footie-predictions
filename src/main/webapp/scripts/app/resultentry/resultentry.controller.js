'use strict';

angular.module('footierepoApp')
    .controller('ResultEntryController', function ($scope, $state, Prediction, Fixture) {
         $scope.fixtures = [];
        
        $scope.loadAll = function() {
            Fixture.query(function(result) {
               $scope.fixtures = result;
            });
        };
        $scope.loadAll();


        $scope.saveAll = function() {
            for (var i =0; i<$scope.fixtures.length; i++) {
                Fixture.update($scope.fixtures[i]);
            }
        }

        $scope.updateResult = function(id, home,away) {

            Fixture.updateResult({'id':id, 'score':home+'_'+away});

        }

        $scope.recalculateAll = function(){
                Fixture.recalculateAll();            
        }
        
        
    });
