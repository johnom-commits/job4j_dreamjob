<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate can = new Candidate(0, "");
    if (id != null) {
        can = PsqlStore.instOf().findCandidateById(Integer.parseInt(id));
    }
    String file = (String) request.getAttribute("file");
%>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp"> <c:out value="${user.name}"/> |
                    Выйти</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат.
                <% } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body" id="candidate">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=can.getId()%>&file=<%=file%>"
                      method="post">
                    <div class="form-group">
                        <label for="nameCandidate">Имя</label>
                        <input type="text" class="form-control" name="name" id="nameCandidate"
                               value="<%=can.getName()%>" title="Введите имя пользователя">
                    </div>
                    <p>
                        <label for="sel">Город</label>
                        <select class="custom-select custom-select-sm" id="sel" name="city">
                        </select>
                    </p>
                    <p>
                        <a href="<%=request.getContextPath()%>/uploadPhoto.do?id=<%=can.getId()%>">Загрузить фото</a>
                    </p>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    window.addEventListener('load', loadOptions);

    function loadOptions() {
        $.ajax({
            type: "GET",
            url: 'http://localhost:8080/dreamjob/cities.do',
            data: 'name=' + <%=can.getCity()%>
        }).done(function (data) {
            $('#sel').empty().html(data);
        }).fail(function (err) {
            alert(err);
        })
    }

    document.querySelector('#candidate').addEventListener('submit', checkName);

    function checkName(event) {
        if (event.target.name.value === '') {
            event.preventDefault();
            alert(event.target.name.title)
        }
    }
</script>
</body>
</html>