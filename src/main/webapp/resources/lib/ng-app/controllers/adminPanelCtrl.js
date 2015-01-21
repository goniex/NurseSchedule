nurseApp.controller("AdminPanelCtrl", function($scope, $modal, NurseService, ScheduleService) {
    $scope.nurse = {};
    $scope.disableAdd = true;

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
        var confirmModal = $modal.open({
            templateUrl: 'resources/lib/ng-app/templates/confirmModal.html',
            size: 'sm',
            resolve: {
                infoMsg: function () {
                    return "Are you sure you want to delete this nurse?";
                }
            },
            controller: function($scope, $modalInstance, infoMsg) {
                $scope.msg = infoMsg;

                $scope.ok = function () {
                    $modalInstance.close(true);
                };

                $scope.cancel = function() {
                    $modalInstance.close(false);
                }
            }
        });

        confirmModal.result.then(function(confirm) {
            if (confirm == true) {
                var id = $scope.nurses[index].id;
                NurseService.delete({id: id}, function(result) {
                    if (result.status == 'SUCCESS') {
                        $scope.nurses.splice(index, 1);
                    }
                });
            }
        });
    };

    $scope.show = function() {
        var events = [];
        ScheduleService.get({}, function(result) {
            if (result.status == 'SUCCESS') {
                events = result.list;
            } else {
                alert(result.message);
            }
            scheduleModal(events);
        });
    };

    $scope.showById = function(index) {
        var id = $scope.nurses[index].id;
        var events = [];
        ScheduleService.get({id: id}, function(result) {
            if (result.status == 'SUCCESS') {
                events = result.list;
            } else {
                alert(result.message);
            }
            scheduleModal(events);
        });
    }
    
    $scope.generateReport = function(index) {
        ScheduleService.generate({}, function(result) {
        	
        	var info='';
            if (result.status == 'SUCCESS') {
            	info = 'Report generation was successful!';
            }
            else {
            	info = 'Error!';
            }
            
            
	       	 var scheduleModal = $modal.open({
	                templateUrl: 'resources/lib/ng-app/templates/infoModal.html',
	                resolve: {
	               	 infoMsg: function () {
	                        return info;
	                      }
	                },
	                controller: function($scope, $modalInstance, infoMsg) {
	               	 $scope.msg = infoMsg;
	               	 
	               	  $scope.ok = function () {
	               	    $modalInstance.close();
	               	  };
	                },
	                size: 'sm'
	            });
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

    $scope.$watch('nurse', validate, true);

    function validate(newVal, oldVal, scope) {
        var name = true;
        var lastName = true;
        var email = true;
        var workTime = true;
        if (newVal.name != "" && newVal.name != undefined) name = false;
        if (newVal.lastName != "" && newVal.lastName != undefined) lastName = false;
        if (newVal.email != "" && newVal.email != undefined) email = false;
        if (newVal.workTime != "" && newVal.workTime != undefined && parseInt(newVal.workTime, 10)) workTime = false;
        if (!name && !lastName && !email && !workTime) {
            $scope.disableAdd = false;
        } else {
            $scope.disableAdd = true;
        }
    }
});
