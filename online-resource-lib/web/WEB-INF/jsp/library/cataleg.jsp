<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Llibreria de Recursos Electrònics Online > Catàleg</title>
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
      </ul>
      <form class="navbar-form navbar-right">
        <a href="protegit/llista" class="btn btn-success">Descàrregues</a>
        <c:if test="${user != null}">
          <a href="logout" class="btn btn-info">Sortir</a>
        </c:if>
      </form>
      <c:if test="${user != null}">
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
  <div class="row">
    <div class="col-md-offset-2 col-md-8">
      <div class="panel panel-primary">
        <div class="panel-heading">Catàleg</div>
        <div class="panel-body">
          <div role="tabpanel">

            <!-- Nav tabs -->
            <ul id="contentSelector" class="nav nav-tabs" role="tablist">
              <li role="presentation" class="active">
                <a href="#videos" aria-controls="home" role="tab" data-toggle="tab">V&iacute;deos</a>
              </li>
              <li role="presentation">
                <a href="#books" aria-controls="profile" role="tab" data-toggle="tab">Llibres</a>
              </li>
              <li role="presentation">
                <a href="#music" aria-controls="messages" role="tab" data-toggle="tab">M&uacute;sica</a>
              </li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content">
              <div role="tabpanel" class="tab-pane active" id="videos">
                <table class="table">
                  <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Tipus</th>
                    <th>Cost</th>
                    <th></th>
                    <th></th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="item" items="${videos}">
                    <tr>
                      <td>
                          ${item.name}<br />
                      </td>
                      <td>
                          ${item.mimeType}
                      </td>
                      <td>
                          ${item.cost} &cent;
                      </td>
                      <td>
                        <form action="protegit/llista" method="POST">
                          <button type="submit" class ="btn btn-warning" <c:if test="${user != null && ( userAcquisitions.contains(item) || userShoppingBasket.contains(item) )}">disabled</c:if>>
                            <span class="glyphicon glyphicon-shopping-cart"></span>
                          </button>
                          <input type="hidden" name="resource" value="${item.id}">
                          <input type="hidden" name="_action_" value="addResourceToShoppingBasket">
                        </form>
                      </td>
                      <td>
                        <form action="protegit/llista" method="POST">
                          <button type="submit" class ="btn btn-success" <c:if test="${user != null && ( tokens < item.cost || userAcquisitions.contains(item) || userShoppingBasket.contains(item) )}">disabled</c:if>>
                            <span class="glyphicon glyphicon-credit-card"></span>
                          </button>
                          <input type="hidden" name="resource" value="${item.id}">
                          <input type="hidden" name="_action_" value="oneClickBuy">
                        </form>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
              <div role="tabpanel" class="tab-pane" id="books">
                <table class="table">
                  <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Tipus</th>
                    <th>Cost</th>
                    <th></th>
                    <th></th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="item" items="${books}">
                    <tr>
                      <td>
                          ${item.name}<br />
                      </td>
                      <td>
                          ${item.mimeType}
                      </td>
                      <td>
                          ${item.cost} &cent;
                      </td>
                      <td>
                        <form action="protegit/llista" method="POST">
                          <button type="submit" class ="btn btn-warning" <c:if test="${user != null && ( userAcquisitions.contains(item) || userShoppingBasket.contains(item) )}">disabled</c:if>>
                            <span class="glyphicon glyphicon-shopping-cart"></span>
                          </button>
                          <input type="hidden" name="resource" value="${item.id}">
                          <input type="hidden" name="_action_" value="addResourceToShoppingBasket">
                        </form>
                      </td>
                      <td>
                        <form action="protegit/llista" method="POST">
                          <button type="submit" class ="btn btn-success" <c:if test="${user != null && ( tokens < item.cost || userAcquisitions.contains(item) || userShoppingBasket.contains(item) )}">disabled</c:if>>
                            <span class="glyphicon glyphicon-credit-card"></span>
                          </button>
                          <input type="hidden" name="resource" value="${item.id}">
                          <input type="hidden" name="_action_" value="oneClickBuy">
                        </form>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
              <div role="tabpanel" class="tab-pane" id="music">

                <table class="table">
                  <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Tipus</th>
                    <th>Cost</th>
                    <th></th>
                    <th></th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="item" items="${music}">
                    <tr>
                      <td>
                          ${item.name}<br />
                      </td>
                      <td>
                          ${item.mimeType}
                      </td>
                      <td>
                          ${item.cost} &cent;
                      </td>
                      <td>
                        <form action="protegit/llista" method="POST">
                          <button type="submit" class ="btn btn-warning" <c:if test="${user != null && ( userAcquisitions.contains(item) || userShoppingBasket.contains(item) )}">disabled</c:if>>
                            <span class="glyphicon glyphicon-shopping-cart"></span>
                          </button>
                          <input type="hidden" name="resource" value="${item.id}">
                          <input type="hidden" name="_action_" value="addResourceToShoppingBasket">
                        </form>
                      </td>
                      <td>
                        <form action="protegit/llista" method="POST">
                          <button type="submit" class ="btn btn-success" <c:if test="${user != null && ( tokens < item.cost || userAcquisitions.contains(item) || userShoppingBasket.contains(item) )}">disabled</c:if>>
                            <span class="glyphicon glyphicon-credit-card"></span>
                          </button>
                          <input type="hidden" name="resource" value="${item.id}">
                          <input type="hidden" name="_action_" value="oneClickBuy">
                        </form>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
  (function($){

    $(function(){
      $('#contentSelector a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
      })
    });

  })(jQuery);
</script>
</body>
</html>