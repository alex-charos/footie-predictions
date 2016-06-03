'use strict';

angular.module('footierepoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prediction', {
                parent: 'entity',
                url: '/predictions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Predictions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prediction/predictions.html',
                        controller: 'PredictionController'
                    }
                },
                resolve: {
                }
            })
            .state('prediction.detail', {
                parent: 'entity',
                url: '/prediction/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Prediction'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prediction/prediction-detail.html',
                        controller: 'PredictionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Prediction', function($stateParams, Prediction) {
                        return Prediction.get({id : $stateParams.id});
                    }]
                }
            })
            .state('prediction.new', {
                parent: 'prediction',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/prediction/prediction-dialog.html',
                        controller: 'PredictionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    username: null,
                                    points: null,
                                    correctScores: null,
                                    correctResults: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('prediction', null, { reload: true });
                    }, function() {
                        $state.go('prediction');
                    })
                }]
            })
            .state('prediction.edit', {
                parent: 'prediction',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/prediction/prediction-dialog.html',
                        controller: 'PredictionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Prediction', function(Prediction) {
                                return Prediction.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prediction', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('prediction.delete', {
                parent: 'prediction',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/prediction/prediction-delete-dialog.html',
                        controller: 'PredictionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Prediction', function(Prediction) {
                                return Prediction.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prediction', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
