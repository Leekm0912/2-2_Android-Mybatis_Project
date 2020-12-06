<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>getDetailBoard</title>
</head>
<body>
	<jsp:useBean id="board" scope="request" class="ldg.mybatis.model.게시글" />
	<jsp:setProperty name="board" property="*" />
	<%
	EcoDesignSessionRepository c = new EcoDesignSessionRepository();
		게시글 result = null;
		if(board.get분류() != null){
			if(board.get분류().equals("DIY예약")){
				 result = c.getDIYDetailBoard(board.get게시글번호());
			}else if(board.get분류().equals("상담예약")){
				 result = c.getReserveDetailBoard(board.get게시글번호());
			}else if(board.get분류().equals("공지사항")){
				 result = c.getNoticeDetailBoard(board.get게시글번호());
			}
		}
		// 출력
		if(result != null){
			out.print("<notice>success</notice>");

			out.print("<boardtitle>"+ result.get제목() +"</boardtitle>");
			out.print("<num>"+ result.get게시글번호() +"</num>");
			out.print("<date>"+ result.get등록일() +"</date>");
			out.print("<writer>"+ result.get사용자_ID() +"</writer>");
			out.print("<text>"+ result.get내용() +"</text>");
			out.print("<filepath>"+ result.getFilePath() +"</filepath>");
			if(result.get댓글() != null){
				for(Comment comment : result.get댓글()){
					out.print("<c_writer>"+ comment.get작성자() +"</c_writer>");
					out.print("<c_date>"+ comment.get등록일() +"</c_date>");
					out.print("<c_text>"+ comment.get내용() +"</c_text>");
					out.print("<c_num>"+ comment.get댓글번호() +"</c_num>");
				}
			}
			
			
		}else{
			out.print("<notice>fail</notice>");
		}
	%>
</body>
</html>
