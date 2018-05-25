<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Proyecto ASR</title>
</head>
<body>
<h1>Ejemplo de Proyecto de ASR con Cloudant ahora con DevOps JC - Prueba 2</h1>
<hr />
<p>Opciones de la clase de ASR:</p>
<ul>
<li><a href="listar">Listar</a></li>
<li><a href="insertar?palabra=hola">Insertar</a></li>
<li><form action="traducir">
  Palabra a traducir
  <input type="text" name="palabra">
  <input type="submit" value="Guardar en Cloudant">
</form></li>

<li><a href="analyzer">Analizar un texto cualquiera</a></li>

<li><form action="speechtotext" enctype="multipart/form-data" method="POST">
 Introducir audio
  <input type="file" name="audio">
  <input type="submit" value="audio">
</form></li>

${requestScope.sentimiento}
${requestScope.audio}

</ul>
</body>
</html>