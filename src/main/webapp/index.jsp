<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<script type="text/javascript">
function do_change(){
document.getElementById("hide-me").style.display = "block";
}
function do_change2(){
	document.getElementById("hide-me2").style.display = "block";
	}
function do_change3(){
	document.getElementById("hide-me3").style.display = "block";
	}
function pageScroll() {
    window.scrollBy(0,10);
    scrolldelay = setTimeout(pageScroll,10);
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<title>Bootstrap Theme Company</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<style>
.jumbotron { 
    background-color: #f4511e; /* Orange */
    color: #ffffff;
}
.navbar {
    margin-bottom: 0;
    background-color: #f4511e;
    z-index: 9999;
    border: 0;
    font-size: 12px !important;
    line-height: 1.42857143 !important;
    letter-spacing: 4px;
    border-radius: 0;
}


.navbar li a, .navbar .navbar-brand {
    color: #fff !important;
}

.navbar-nav li a:hover, .navbar-nav li.active a {
    color: #f4511e !important;
    background-color: #fff !important;
}

.navbar-default .navbar-toggle {
    border-color: transparent;
    color: #fff !important;
}

</style>
<body>
  <div class="jumbotron text-center">
  <nav class="navbar navbar-default navbar-fixed-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span> 
      </button>
      <a class="navbar-brand" href="#">JC</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right"> 
        <li><a onclick="pageScroll()">LISTAR</a></li>
        <li><a onclick="pageScroll()">AÑADIR</a></li>
        <li><a onclick="pageScroll()">TRADUCIR</a></li>
      </ul>
    </div>
  </div>
</nav>
  <h1>Analizador de sentimientos</h1> 
  <p>Arquitectura de servicios en red</p> 
  <form class="form-inline" action="speechtotext" enctype="multipart/form-data" method="POST">
    <div class="input-group">
      <input  type="file" name="audio" class="form-control-file" id="exampleFormControlFile1" size="50" placeholder="vsn" required>
      <br>
      <center>
	      <div class="input-group-btn" text-align="center">
	      <input type="submit" class="btn btn-info" value="Analizar sentimiento" onclick="do_change3()">
	      </div>
      </center>
    </div>
  </form>
</div>

<div class="container">            
  <table class="table">
    <thead>
      <tr>
        <th>Mensaje entendido</th>
        <th>Sentimiento</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>${requestScope.audio}</td>
        <td>${requestScope.sentimiento}</td>
      </tr>
    </tbody>
  </table>
</div>

<div style="width:23%">
<div align="center">
 <a href="listar" class="btn btn-info" role="button" name="palabra" placeholder="Listar">Listar</a> 
</div>
   
    <div class="panel panel-default">
 	 <div class="panel-heading">Contenido de la base de datos</div>
  	 <div class="panel-body">${requestScope.palabrasalmacenadas}</div>
	</div>
</div>


<div>
<form action="insertar" method="POST">
  <div class="input-group" >
    <span class="input-group-addon">Almacenar</span>
    <div style="width:23%">
    	<input id="msg" type="text" class="form-control" name="palabra" placeholder="Palabra a almacenar">
    </div>
    <div>
    	<input id="msg" type="submit" class="btn btn-info" value ="Introducir" name="msg" placeholder="Introducir">
    </div>
  </div>
</form>
<br>
<b>${requestScope.palabraaañadir}</b>
</div>


<br>


	<div>
		<form action="traducir" method="POST">
		  <div class="input-group">
		    <span class="input-group-addon">Traducir</span>
		    <div style="width:23%">
		    	<input id="msg" type="text" class="form-control" name="palabra" placeholder="en-es">
		    </div>
		    <div>
		    	<input id="msg" type="submit" class="btn btn-info" value ="Introducir" placeholder="Introducir">
		    </div>
		  </div>
		</form>

		<div>
		<b>${requestScope.palabratraducida}</b>
		</div>
	</div>

</body>
</html>