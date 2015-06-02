<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html ng-app="crawler">
<head>
  <title>Llibreria de Recursos Electrònics Online > Pàgina inicial</title>
  <meta charset="utf-8">
  <base href="${contextBaseUrl}">
  <link type="text/css" href="static/css/bootstrap.css" rel="stylesheet" />
  <link type="text/css" href="static/css/bootstrap-theme.css" rel="stylesheet" />
  <link type="text/css" href="static/css/library.css" rel="stylesheet" />
  <script type="text/javascript" src="static/js/jquery-2.1.3.js"></script>
  <script type="text/javascript" src="static/js/angular-1.3.15.js"></script>
  <script type="text/javascript" src="static/js/libcrawler.js"></script>
</head>
<body ng-controller="UserInterfaceController">

<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container">
    <div class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li><a class="navbar-brand" href="#">Client Web de Llibreria de Recursos Electrònics Online</a></li>
      </ul>
    </div>
  </div>
</nav>

<div class="container">
  <div class="row">
    <div class="col-md-8">
      <div class="panel panel-default">
        <div class="panel-heading">Searcher</div>
        <div class="panel-body">
          <form class="form-inline" style="margin: 0px;">
            <div class="form-group" style="width: 100%;">
              <label class="sr-only" for="nameToSearch">Cerca un recurs electrònic pel seu nom</label>
              <div class="input-group" style="width: 100%; position: relative;">
                <div class="input-group-addon" style="width: 25%;">Nom del recurs</div>
                <input type="text" class="form-control col-md-4" id="nameToSearch" ng-model="nameToSearch" placeholder="Escriu un text">
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="panel panel-primary">
        <div class="panel-heading">Recursos disponibles</div>
        <table class="table table-condensed table-striped">
          <thead>
          <tr>
            <th width="15%">Llibreria</th>
            <th width="55%">Nom</th>
            <th width="10%" style="text-align: center;">Tipus</th>
            <th width="10%" style="text-align: right;">Cost</th>
          </tr>
          </thead>
          <tbody>
            <tr ng-repeat="resource in resources | filter: nameToSearch | orderBy:['-cost', 'name']">
              <td>
                {{resource.host}}
              </td>
              <td>
                {{resource.desc}} (<a href="{{resource.link}}">Link</a>)
              </td>
              <td style="text-align: center;">
                <span class="{{iconResources[resource.type]}}"></span>
              </td>
              <td style="text-align: right;">
                <b>{{resource.cost}}</b>&cent;
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="col-md-4">
      <div class="panel panel-warning">
        <div class="panel-heading">Llibreries disponibles</div>
        <table class="table table-condensed table-striped">
          <thead>
            <tr>
              <th>Hostname</th>
              <th style="text-align: center;"><span class="glyphicon glyphicon-film"></span></th>
              <th style="text-align: center;"><span class="glyphicon glyphicon-cd"></span></th>
              <th style="text-align: center;"><span class="glyphicon glyphicon-book"></span></th>
            </tr>
          </thead>
          <tbody>
            <tr ng-repeat="server in servers">
              <td>
                {{server.host}}:{{server.port}}
              </td>
              <td style="text-align: center;">
                {{server.VIDEO}}
              </td>
              <td style="text-align: center;">
                {{server.AUDIO}}
              </td>
              <td style="text-align: center;">
                {{server.BOOK}}
              </td>
            </tr>
          </tbody>
        </table>
        <div class="panel-footer">
          Total: <b>{{servers.length}}</b>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
