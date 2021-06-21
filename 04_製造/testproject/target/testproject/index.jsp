<html>
<head>
	<title>TEST TOP</title>
</head>
<body>
<h1>TEST TOP</h1>
<p>This is top page and default page.</p>
<hr noshade>
<h2>NORMAL CASE</h2>
<h3>[GET]</h3>
<p><a href="webresources/myresource/test_get">CASE_01</a>(test_get)</p>
<p><a href="webresources/myresource/no_exist">CASE_04</a>(no_exist)</p>
<hr noshade>
<h3>[POST_INSERT]</h3>
<form method="post" action="/testproject/webresources/myresource/test_post_insert" id="id_insert">
<p>id:<input type="text" name="id">&nbsp;&nbsp;
name:<input type="text" name="name">&nbsp;&nbsp;
email:<input type="text" name="email"></p>
</form>
<p><button type="submit" form="id_insert">CASE_02</button>(test_post_insert)</p>
<hr noshade>
<h3>[POST_UPDATE]</h3>
<form method="post" action="/testproject/webresources/myresource/test_post_update" id="id_update">
<p>id:<input type="text" name="id">&nbsp;&nbsp;
name:<input type="text" name="name">&nbsp;&nbsp;
email:<input type="text" name="email"></p>
</form>
<p><button type="submit" form="id_update">CASE_03</button>(test_post_update)</p>
<hr noshade>
<h2>ERROR CASE</h2>
<h3>[GET]</h3>
<p><a href="webresources/myresource/error_case_view">CASE_05</a>(error_case_view)</p>
<p><a href="webresources/myresource/error_case_sql">CASE_06</a>(error_case_sql)</p>
<p><a href="webresources/myresource/error_case_error_view">CASE_07</a>(error_case_error_view)</p>
<p><a href="webresources/myresource/error_execute_sql">CASE_08</a>(error_execute_sql)</p>
<hr noshade>
</body>
</html>
