nurseApp.controller("AdminPanelCtrl", function($scope, NurseService) {
    $scope.nurse = {};

    NurseService.get({}, function(result) {
        if (result.status == 'SUCCESS') {
            $scope.nurses = result.list;
        } else {
            alert(result.message);
        }
    })
});
