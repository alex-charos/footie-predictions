'use strict';

angular.module('footierepoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('points', {
                parent: 'entity',
                url: '/points',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Points'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/standings/points.html',
                        controller: 'PointsController'
                    }
                },
                resolve: {
                }
            })


    });
