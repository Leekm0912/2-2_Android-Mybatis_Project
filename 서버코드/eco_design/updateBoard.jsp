<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>update_board</title>
</head>
<body>
	<jsp:useBean id="post" scope="request" class="ldg.mybatis.model.게시글" />
	<jsp:setProperty name="post" property="*" />
	<%
	EcoDesignSessionRepository c = new EcoDesignSessionRepository();
			int result = c.updateBoard(post);
			
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
