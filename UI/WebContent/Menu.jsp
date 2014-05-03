<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Menu</title>
</head>
<body>
<form method="post" action= "TestController" onsubmit="">
<center>
<h1>select the metric to view graph</h1>
<select name="menu" id="menu">
<option value="mem">memory</option>
<option value="sys">system</option>
<option value="net">network</option>
<option value="disk">IO</option>
<option value="cpu">cpu</option>
</select>
<br/>
<br/>
<input type ="submit" value="submit"/>
</center>
</form>
</body>
</html>