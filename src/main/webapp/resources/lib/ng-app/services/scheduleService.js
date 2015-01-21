nurseApp.service("ScheduleService", function($resource) {
    return $resource('schedule/:action/:id', {}, {
        get: {method: 'GET', params: {action: 'get'}},
    	generate: {method: 'GET', params: {action: 'generate'}}
    });
});