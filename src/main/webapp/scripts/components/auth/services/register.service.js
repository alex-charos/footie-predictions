'use strict';

angular.module('footierepoApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


