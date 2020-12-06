<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>회원가입</title>
</head>
<body>
	<jsp:useBean id="buyer" scope="request" class="ldg.mybatis.model.사용자" />
	<jsp:setProperty name="buyer" property="*" />
	<%
			EcoDesignSessionRepository c = new EcoDesignSessionRepository();
			int result = c.insertUser(buyer);
			
			if(result > 0){
				out.print("<notice>success</notice>");
			}else if(result == -1){
				out.print("<notice>error</notice>");
			}else if(result == -2){
				out.print("<notice>"+ErrorCause.getInstance().getErrorMSG()+"</notice>");
			}
	%>
</body>
</html>
