'use strict';

angular.module('footierepoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('activeFixtures', {
                parent: 'entity',
                url: '/activeFixtures',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ActiveFixtures'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/activeFixtures/activeFixtures.html',
                        controller: 'ActiveFixturesController'
                    }
                },
                resolve: {
                }
            })


    });
