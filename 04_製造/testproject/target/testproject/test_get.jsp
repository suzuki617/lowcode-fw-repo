<html>
<head>
	<title>TEST GET</title>
</head>
<body>
	<h1>TEST GET</h1>
	<table border="1" cellspacing="0" cellpadding="0">
	  <tr>
	    <th>id</th>
	    <th>name</th>
	    <th>email</th>
	  </tr>
	  <tr>
	    <td>${it.model.get(0).id}</td>
	    <td>${it.model.get(0).name}</td>
	    <td>${it.model.get(0).email}</td>
	  </tr>
	</table>
</body>
</html>