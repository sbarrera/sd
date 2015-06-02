<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Llibreria de Recursos Electrònics Online > Error de sistema</title>
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
      </div>
    </div>
  </nav>

  <div class="container">
    <div class="row">
      <div class="col-md-offset-1 col-md-10">
        <div class="panel panel-danger">
          <div class="panel-heading">Error d'aplicatiu - Codi d'estat HTTP [${httpStatusCode}]</div>
          <div class="panel-body">
            <span class="label label-danger">Missatge d'error</span>
            <pre>${message}</pre>
            <span class="label label-danger">Stack Trace</span>
            <pre>${stackTrace}</pre>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>