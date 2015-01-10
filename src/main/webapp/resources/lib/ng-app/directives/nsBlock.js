nurseApp.directive('nsBlock', function($http) {
    return {
        restrict: 'E',
        templateUrl: 'resources/lib/ng-app/templates/nsBlock.html',
        link: function(scope) {
            scope.$watch(function() {
                return $http.pendingRequests.length;
            }, function(pendingRequests) {
                if (pendingRequests > 0) {
                    scope.isPending = true;
                } else {
                    scope.isPending = false;
                }
            })
        }
    }
});