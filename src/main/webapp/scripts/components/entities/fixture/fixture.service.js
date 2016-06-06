'use strict';

angular.module('footierepoApp')
    .factory('Fixture', function ($resource, DateUtils) {
        return $resource('api/fixtures/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'updateResult': {
                method:'GET',
                url: 'api/fixtures/update/result/:id'
            }
        });
    });
