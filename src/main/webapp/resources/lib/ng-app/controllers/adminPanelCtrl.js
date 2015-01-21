nurseApp.controller("AdminPanelCtrl", function($scope, $modal, NurseService, ScheduleService) {
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
        var events = [];
        ScheduleService.get({}, function(result) {
            if (result.status == 'SUCCESS') {
                events = result.list;
            } else {
                alert(result.message);
            }

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
                    angular.forEach(events, function(index) {
                        console.log(index);
                    });
                    $scope.events = events;

                    $scope.closeModal = function() {
                        $modalInstance.dismiss('cancel');
                    }
                },
                size: 'lg'
            });
        });
    };
    
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
});
