var nurseApp = angular.module("nurseApp", ['ngResource', 'ui.bootstrap', 'mwl.calendar', 'ui.bootstrap.modal']);

nurseApp.config(function($httpProvider) {
   $httpProvider.interceptors.push('blockInterceptor');
});

nurseApp.factory('blockInterceptor', function($q) {
    return {
        request: function(config) {
            return config;
        },
        response: function(result) {
            return result;
        },
        responseError: function(result) {
            return result;
        }
    }
});
