<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>getBoard</title>
</head>
<body>
	<jsp:useBean id="board" scope="request" class="ldg.mybatis.model.게시글" />
	<jsp:setProperty name="board" property="*" />
	<%
	EcoDesignSessionRepository c = new EcoDesignSessionRepository();
		List<게시글> result = null;
		if(board.get분류() != null){
			if(board.get분류().equals("DIY예약")){
				 result = c.getDIYBoard();
			}else if(board.get분류().equals("상담예약")){
				 result = c.getReserveBoard();
			}else if(board.get분류().equals("공지사항")){
				 result = c.getNoticeBoard();
			}
		}
		// 출력
		if(result != null){
			out.print("<notice>success</notice>");
			for(게시글 temp : result){
				out.print("<boardtitle>"+ temp.get제목() +"</boardtitle>");
				out.print("<num>"+ temp.get게시글번호() +"</num>");
				out.print("<date>"+ temp.get등록일() +"</date>");
				out.print("<writer>"+ temp.get사용자_ID() +"</writer>");
				out.print("<filepath>"+ temp.getFilePath() +"</filepath>");
			}
		}else{
			out.print("<notice>fail</notice>");
		}
	%>
</body>
</html>
