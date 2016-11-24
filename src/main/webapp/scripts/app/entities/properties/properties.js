'use strict';

angular.module('footierepoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('properties', {
                parent: 'entity',
                url: '/propertiess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Propertiess'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/properties/propertiess.html',
                        controller: 'PropertiesController'
                    }
                },
                resolve: {
                }
            })
            .state('properties.detail', {
                parent: 'entity',
                url: '/properties/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Properties'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/properties/properties-detail.html',
                        controller: 'PropertiesDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Properties', function($stateParams, Properties) {
                        return Properties.get({id : $stateParams.id});
                    }]
                }
            })
            .state('properties.new', {
                parent: 'properties',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/properties/properties-dialog.html',
                        controller: 'PropertiesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    displayStandings: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('properties', null, { reload: true });
                    }, function() {
                        $state.go('properties');
                    })
                }]
            })
            .state('properties.edit', {
                parent: 'properties',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/properties/properties-dialog.html',
                        controller: 'PropertiesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Properties', function(Properties) {
                                return Properties.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('properties', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('properties.delete', {
                parent: 'properties',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/properties/properties-delete-dialog.html',
                        controller: 'PropertiesDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Properties', function(Properties) {
                                return Properties.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('properties', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
