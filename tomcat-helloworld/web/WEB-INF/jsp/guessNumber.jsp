<%@ page import="edu.ub.sd.tomcat.helloworld.GuessNumber" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Boolean didWin = (Boolean) request.getAttribute(GuessNumber.REQUEST_ATTR_WIN);
  Boolean wrongGuess = (Boolean) request.getAttribute(GuessNumber.REQUEST_ATTR_WRONGGUESS);
  Integer number = (Integer) request.getAttribute(GuessNumber.REQUEST_ATTR_NUMBER);
%>
<!DOCTYPE html>
<html>
<head>
  <title>Guess Number</title>
</head>
<body>
  <form action="" method="GET">
    <% if ( !didWin ) { %>
    <% if ( wrongGuess ) { %>
    <div style="display: block; text-align: center;">
      <iframe width="420" height="315" src="https://www.youtube.com/embed/amg5oDtLWDw?rel=0&autoplay=1" frameborder="0" allowfullscreen></iframe>
    </div>
    <% } %>
    <div style="display: block">
      <label for="n">Which number am I thinking right now?</label>
      <input id="n" name="n" type="number" value="0">
    </div>
    <div style="display: block">
      <input type="submit" value="Send">
    </div>
    <% } else { %>
    <div style="display: block">
      <p>You win!</p>
    </div>
    <div style="display: block">
      <input type="submit" value="Another game">
    </div>
    <% } %>
  </form>
  <span id="SOOPADOOPA_SECURED_SECRET_NUMBER_TO_GUESS" style="display: none;"><%= number %></span>
</body>
</html>
