'use strict';

angular.module('footierepoApp')
    .factory('Properties', function ($resource, DateUtils) {
        return $resource('api/propertiess/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
