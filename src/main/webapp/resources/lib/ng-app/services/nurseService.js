nurseApp.service("NurseService", function($resource) {
    return $resource('nurse/get', {}, {
        get: {method: 'GET'}
    });
});