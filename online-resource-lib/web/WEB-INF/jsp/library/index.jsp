<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>Llibreria de Recursos Electrònics Online > Pàgina inicial</title>
    <meta charset="utf-8">
    <base href="${contextBaseUrl}">
    <link type="text/css" href="static/css/bootstrap.css" rel="stylesheet" />
    <link type="text/css" href="static/css/bootstrap-theme.css" rel="stylesheet" />
    <link type="text/css" href="static/css/library.css" rel="stylesheet" />
    <script type="text/javascript" src="static/js/jquery-2.1.3.js"></script>
  </head>
  <body>

    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a class="navbar-brand" href="/llibreria">Llibreria de Recursos Electrònics Online</a></li>
          </ul>
          <c:if test="${user == null}">
            <form class="navbar-form navbar-right" role="search">
              <a href="protegit/llista" class="btn btn-success">Descàrregues</a>
            </form>
          </c:if>
        </div>
      </div>
    </nav>

    <div class="container">
      <div class="row">
        <div class="col-md-offset-3 col-md-6" style="text-align: center; position: relative; ">
          <a href="cataleg" class="btn btn-primary"style="position: absolute; top: 100px; z-index: 2;">Cataleg</a>
          <img src="static/img/tiendaonline.jpg" style="text-align: center; position: absolute; top: 150px; left: 110px; z-index: 0">
        </div>
      </div>
    </div>
  </body>
</html>
