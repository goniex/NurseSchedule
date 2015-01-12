<%@ include file="header.jsp" %>

        <main ng-controller="AdminPanelCtrl" class="col-xs-12 admin-panel-main">
        <div class="col-xs-12 user">
            <div class="col-xs-4 col-xs-offset-2">
                <div class="col-xs-5 admin photo">
                    <img src=<c:url value="/resources/img/ic_person.png"/> class="img-circle avatar">
                    <a href="#" title=""><img src=<c:url value="/resources/img/ic_photo.png"/> class="change-photo"></a>
                </div>
                <div class="col-xs-5 col-xs-offset-2 user-data">
                    <h2>Admin</h2>
                    <h4>name@gmail.com</h4>
                </div>
            </div>
            <div class="col-xs-3 col-xs-offset-1">
                <button type="button" class="btn btn-danger btn-block">
                    GENERATE NEW
                </button>
                <button type="button" class="btn btn-danger btn-block schedule">
                    SEE SCHEDULE
                    <h6>01.01.2012-01.02.2012</h6>
                </button>
            </div>
        </div>
        <div class="col-xs-10 col-xs-offset-1 all-users">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <td>NAME/EMAIL</td>
                        <td>WORK TIME(h)</td>
                        <td>SCHEDULE</td>
                        <td>DELETE</td>
                        <td>EDIT</td>
                    </tr>
                    <tr ng-repeat="nurse in nurses">
                        <td>
                            <div class="single-user-data">
                                <img src=<c:url value="/resources/img/ic_person.png"/> class="small-avatar">
                                <h4>{{nurse.name}} {{nurse.lastName}}</h4>
                                <h6>{{nurse.email}}</h6>
                            </div>
                        </td>
                        <td class="small-center">{{nurse.workTime}}</td>
                        <td class="small-center"><a href="#" title=""><img src=<c:url value="/resources/img/ic_schedule_small.png"/>/></a></td>
                        <td class="small-center"><a ng-click="delete($index)" ng-href="" title=""><img src=<c:url value="/resources/img/ic_remove.png"/>/></a></td>
                        <td class="small-center"><a href="#" title=""><img src=<c:url value="/resources/img/ic_edit.png"/>/></a></td>
                    </tr>
                </thead>
            </table>
        </div>
        <div class="col-xs-12 adding-nurse">
            <div class="centering">
                <div class="col-xs-3">
                    <input type="text" ng-model="nurse.name" class="form-control" placeholder="nurse name">
                </div>
                <div class="col-xs-3">
                    <input type="text" ng-model="nurse.lastName" class="form-control" placeholder="nurse lastname">
                </div>
                <div class="col-xs-2">
                    <input type="email" ng-model="nurse.email" class="form-control" placeholder="name@gmail.com">
                </div>
                <div class="col-xs-2">
                    <input type="number" ng-model="nurse.workTime" class="form-control" placeholder="job time">
                </div>
                <div class="col-xs-2">
                    <button ng-click="save()" type="button" class="btn btn-danger btn-block">ADD NEW NURSE</button>
                </div>
            </div>
        </div>
        </main>
        
<%@ include file="footer.jsp" %>