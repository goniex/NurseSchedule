nurseApp.controller("NursePanelCtrl", function($scope, $modal, NurseService, ScheduleService) {
    $scope.nurse = {};

    angular.element(document).ready(function() {
        var email = document.getElementById('NurseEmail').value;
        $scope.nurse.email = email;
        NurseService.getByEmail($scope.nurse, function(result) {
            if (result.status == 'SUCCESS') {
                $scope.nurse = result.object;
            } else {
                alert(result.message);
            }
        });
    });

    $scope.show = function() {
        var id = $scope.nurse.id
        var events = [];
        ScheduleService.get({id: id}, function(result) {
            if (result.status == 'SUCCESS') {
                events = result.list;
            } else {
                alert(result.message);
            }
            scheduleModal(events);
        });
    };

    function scheduleModal(events) {
        var scheduleModal = $modal.open({
            templateUrl: 'resources/lib/ng-app/templates/scheduleModal.html',
            resolve: {
                events: function() {
                    return events;
                }
            },
            controller: function($scope, $modalInstance, events) {
                $scope.calendarView = 'month';
                $scope.calendarDay = new Date();
                $scope.events = events;
                $scope.closeModal = function() {
                    $modalInstance.dismiss('cancel');
                }
            },
            size: 'lg'
        });
    }
});

