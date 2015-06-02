<%@ page import="edu.ub.sd.onlinereslib.webframework.ServletDispatcher" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String baseContextUrl = ServletDispatcher.getContextBaseUrl(request);
%>
<html>
<head>
  <title>Llibreria de Recursos Electrònics Online > Error d'autenticaci&oacute;</title>
  <meta charset="utf-8">
  <base href="<%= baseContextUrl %>">
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
    <div class="col-md-2"></div>
    <div class="col-md-8">
      <div class="panel panel-danger">
        <div class="panel-heading">Error d'autenticació</div>
        <div class="panel-body">
          <p>T'has equivocat amb els credencials d'autenticació?</p>
          <p><a href="<%= baseContextUrl %>/login">Torna a intentar-ho</a></p>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
