nurseApp.controller("AdminPanelCtrl", function($scope, $modal, NurseService) {
    $scope.nurse = {};

    NurseService.get({}, function(result) {
        if (result.status == 'SUCCESS') {
            $scope.nurses = result.list;
        } else {
            alert(result.message);
        }
    });
    $scope.save = function() {
        NurseService.save($scope.nurse, function(result) {
            if (result.status == 'SUCCESS') {
                $scope.nurses.push(result.object);
                $scope.nurse = {};
            }
        });
    };

    $scope.delete = function(index) {
        var id = $scope.nurses[index].id;
        NurseService.delete({id: id}, function(result) {
            if (result.status == 'SUCCESS') {
                $scope.nurses.splice(index, 1);
            }
        });
    };

    $scope.show = function() {
        var scheduleModal = $modal.open({
            templateUrl: 'resources/lib/ng-app/templates/scheduleModal.html',
            resolve: {
                items: function() {
                    return ["one", "two", "three"];
                }
            },
            controller: function($scope, $modalInstance, items) {
                $scope.items = items;
                $scope.closeModal = function() {
                    $modalInstance.dismiss('cancel');
                }
            },
            size: 'lg'
        });
    };
});
