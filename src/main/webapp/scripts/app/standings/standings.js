'use strict';

angular.module('footierepoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('standings', {
                parent: 'entity',
                url: '/standings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Standings'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/standings/standings.html',
                        controller: 'StandingsController'
                    }
                },
                resolve: {
                }
            })


    });
