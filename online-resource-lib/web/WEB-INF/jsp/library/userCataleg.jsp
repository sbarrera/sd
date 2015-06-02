<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Llibreria de Recursos Electrònics Online > Coses afegides</title>
  <meta charset="utf-8">
  <base href="${contextBaseUrl}">
  <link type="text/css" href="static/css/bootstrap.css" rel="stylesheet" />
  <link type="text/css" href="static/css/bootstrap-theme.css" rel="stylesheet" />
  <link type="text/css" href="static/css/library.css" rel="stylesheet" />
  <script type="text/javascript" src="static/js/jquery-2.1.3.js"></script>
  <script type="text/javascript" src="static/js/bootstrap.js"></script>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container">
    <div class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li><a class="navbar-brand" href="/llibreria">Llibreria de Recursos Electrònics Online</a></li>
        <li><a class="navbar-brand" href="cataleg">Catàleg</a></li>
      </ul>
      <c:if test="${user != null}">
      <form class="navbar-form navbar-right" role="search">
        <a href="logout" class="btn btn-info">Sortir</a>
      </form>
      <p class="navbar-text navbar-right">
        Crèdit disponible <span class="label label-primary" style="font-size: 110%;">${tokens}&cent;</span>
      </p>
      <p class="navbar-text navbar-right">
        Benvingut, <b>${user.name}</b>
      </p>
      </c:if>
    </div>
  </div>
</nav>
<div class="container">
  <c:if test="${messageType != null}">
  <div class="row">
    <div class="alert alert-${messageType} alert-dismissible" role="alert">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      ${messageContent}
    </div>
  </div>
  </c:if>
  <div class="row">
    <div class="col-md-8">
      <div class="panel panel-primary">
        <div class="panel-heading">Llista de continguts per descarregar</div>
        <table class="table">
          <thead>
          <tr>
            <th>Nom</th>
            <th>Tipus</th>
            <th></th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="item" items="${boughtResources}">
            <tr>
              <td>
                  ${item.name}
              </td>
              <td>
                  ${item.mimeType}
              </td>
              <td>
                <a href="protegit/descarrega/${item.id}" class="btn btn-primary">
                  <span class="glyphicon glyphicon-cloud-download"></span>
                </a>
              </td>
              <td>
                <a href="protegit/descarrega/${item.id}?_action_=stream" target="_blank" class="btn btn-info">
                  <span class="glyphicon glyphicon-eye-open"></span>
                </a>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
    <div class="col-md-4">
      <div class="panel panel-warning">
        <div class="panel-heading">Recursos per comprar</div>
        <table class="table table-condensed table-striped">
          <thead>
            <tr>
              <th>Nom</th>
              <th>Cost</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
             <c:forEach var="item" items="${resources}">
            <tr>
              <td>
                ${item.name}
              </td>
              <td>
                  ${item.cost}&cent;
              </td>
              <td>
                <form action="protegit/llista" method="POST">
                  <button type="submit" class="btn btn-danger">
                    <span class="glyphicon glyphicon-remove"></span>
                  </button>
                  <input type="hidden" name="resource" value="${item.id}">
                  <input type="hidden" name="_action_" value="removeResourceFromShoppingBasket">
                </form>
              </td>
            </tr>
            </c:forEach>
          </tbody>
        </table>
        <div class="panel-footer" style="text-align: right;">
          <form action="protegit/llista" method="POST" style="margin: 0;">
            Total: <b>${resources.getTotalCost()}&cent;</b>
            <button type="submit" class="btn btn-success" <c:if test="${tokens < item.cost}">disabled</c:if> >
              <span class="glyphicon glyphicon-shopping-cart"></span>
            </button>
            <input type="hidden" name="_action_" value="buyResources">
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>