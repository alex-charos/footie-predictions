'use strict';

angular.module('footierepoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('enter-results', {
                parent: 'entity',
                url: '/result-entry',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Enter Results'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/resultentry/resultentry.html',
                        controller: 'ResultEntryController'
                    }
                },
                resolve: {
                }
            })
              
     
    });
