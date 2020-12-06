<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>update_comment</title>
</head>
<body>
	<jsp:useBean id="comment" scope="request" class="ldg.mybatis.model.Comment" />
	<jsp:setProperty name="comment" property="*" />
	<%
			out.print(comment.get댓글번호()+"<br>");
			out.print(request.getParameter("Comment_num"));
			EcoDesignSessionRepository c = new EcoDesignSessionRepository();
			int result = c.updateComment(comment);
			
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
