<%@ include file="header.jsp" %>

        <main class="col-xs-12 user-panel-main">
        <div class="centering">
            <div class="col-xs-4 col-xs-offset-2 main_content">
                <div class=" user photo">
                    <div class="user-foto">
                    <img src=<c:url value="/resources/img/ic_person.png"/> class="img-circle avatar">
                    <a href="#" title=""><img src=<c:url value="/resources/img/ic_photo.png"/> class="change-photo"></a>
                    </div>
                </div>
                <h2>Imie i nazwisko</h2>
                <h4>email@gmail.com</h4>
                <h6>4/4</h6>
                <p>czas pracy</p>
            </div>
            <div class="col-xs-4 col-xs-offset-1 main_content">
                <button type="button" class="btn btn-danger btn-block schedule">
                        SEE SCHEDULE
                        <h6>01.01.2012-01.02.2012</h6>
                </button>

                <textarea class="form-control" rows="6" placeholder="Sent special request about schedule for next time"></textarea>
                <button type="button" class="btn btn-danger btn-block send_button">SEND</button>
            </div>
            </div>
        </main>

<%@ include file="footer.jsp" %>