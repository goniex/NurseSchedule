nurseApp.service("ScheduleService", function($resource) {
    return $resource('schedule/:action/:id', {}, {
        get: {method: 'GET', params: {action: 'get'}}
    });
});