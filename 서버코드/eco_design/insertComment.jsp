<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>댓글등록</title>
</head>
<body>
	<jsp:useBean id="comment" scope="request" class="ldg.mybatis.model.Comment" />
	<jsp:setProperty name="comment" property="*" />
	<%
	EcoDesignSessionRepository c = new EcoDesignSessionRepository();
			int result = c.insertComment(comment,request.getParameter("게시글번호"));
			
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
