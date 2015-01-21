nurseApp.service("NurseService", function($resource) {
    return $resource('nurse/:action/:id/', {}, {
        get: {method: 'GET', params: {action: 'get'}},
        getByEmail: {method: 'POST', params: {action: 'getByEmail'}},
        save: {method: 'POST', params: {action: 'save'}},
        delete: {method: 'DELETE', params: {action: 'delete'}}
    });
});