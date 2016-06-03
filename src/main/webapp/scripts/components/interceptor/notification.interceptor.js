 'use strict';

angular.module('footierepoApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-footierepoApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-footierepoApp-params')});
                }
                return response;
            }
        };
    });
