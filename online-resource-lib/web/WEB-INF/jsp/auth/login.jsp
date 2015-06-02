<%@ page import="edu.ub.sd.onlinereslib.webframework.ServletDispatcher" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String baseContextUrl = ServletDispatcher.getContextBaseUrl(request);
%>
<html>
<head>
  <title>Llibreria de Recursos Electrònics Online > Autenticaci&oacute;</title>
  <meta charset="utf-8">
  <base href="<%= baseContextUrl %>">
  <link type="text/css" href="static/css/bootstrap.css" rel="stylesheet" />
  <link type="text/css" href="static/css/bootstrap-theme.css" rel="stylesheet" />
  <link type="text/css" href="static/css/library.css" rel="stylesheet" />
  <script type="text/javascript" src="static/js/jquery-2.1.3.js"></script>
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-md-offset-2 col-md-8">
      <div class="panel panel-default">
        <div class="panel-heading">Login Form</div>
        <div class="panel-body">
          <form method="POST" action="<%= response.encodeURL("j_security_check") %>" class="form-horizontal">
            <div class="form-group">
              <label for="username" class="col-sm-5 control-label">Username</label>
              <div class="col-sm-5">
                <input id="username" name="j_username" type="text" placeholder="username" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label for="password" class="col-sm-5 control-label">Password</label>
              <div class="col-sm-5">
                <input id="password" name="j_password" type="password" placeholder="password" class="form-control">
              </div>
            </div>
            <div class="col-sm-offset-5 col-sm-5">
              <button type="submit" class="btn btn-default">Submit</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
