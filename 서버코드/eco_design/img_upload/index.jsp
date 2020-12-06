<%@ page contentType="text/html; charset=utf-8"%>
<% request.setCharacterEncoding("utf-8"); %>
<html>
<head>
	<title>jsp_page</title>
</head>
<body>

	<!-- enctype은 파입 업로드에서 무조건 사용되어야한다 -->

	<form action="uploadAction.jsp" method="post" enctype="multipart/form-data">

		파일 : <input type="file" name="file"><br>

		<input type="submit" value="업로드"><br>

	</form>

</body>
</html>
