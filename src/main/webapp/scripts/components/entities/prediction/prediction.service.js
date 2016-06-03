'use strict';

angular.module('footierepoApp')
    .factory('Prediction', function ($resource, DateUtils) {
        return $resource('api/predictions/:username', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray: false,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
