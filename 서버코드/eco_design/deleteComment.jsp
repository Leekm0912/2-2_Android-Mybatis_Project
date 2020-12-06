<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>delete_comment</title>
</head>
<body>
	<%
	EcoDesignSessionRepository c = new EcoDesignSessionRepository();
			int result = c.deleteComment(request.getParameter("comment_num"));
			
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
