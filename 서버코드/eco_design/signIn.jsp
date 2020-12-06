<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*" %>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException" %>

<html>
<head>
<title>로그인</title>
</head>
<body>
	<jsp:useBean id="buyer" scope="request" class="ldg.mybatis.model.사용자" />
	<jsp:setProperty name="buyer" property="*" />
	<%
			EcoDesignSessionRepository c = new EcoDesignSessionRepository();
			if(buyer.getPW() == null || buyer.getPW().equals("")){
				out.print("<notice>아이디 또는 패스워드를 입력하지 않으셨습니다.</notice>");
				return;
			}
			int result = c.compareUser(buyer.getID(),SHA256Util.encrypt(buyer.getPW()));
			
			if(result == 1){
				사용자 s = c.selectUserByPrimaryKey(buyer.getID());
				out.print("<notice>success</notice>");
				out.print("<ID>"+ s.getID() +"</ID>");
				out.print("<PW>"+ s.getPW() +"</PW>");
				out.print("<BIRTHDAY>"+ s.get생년월일() +"</BIRTHDAY>");
				out.print("<NAME>"+ s.get이름() +"</NAME>");
				out.print("<PHONE>"+ s.get전화번호() +"</PHONE>");
				out.print("<ADDRESS>"+ s.get주소() +"</ADDRESS>");
			}else if(result == -1){
				out.print("<notice>아이디를 확인해 주세요</notice>");
			}else if(result == -2){
				out.print("<notice>패스워드를 확인해 주세요</notice>");
			}else if(result == 0){
				out.print("<notice>아이디 또는 패스워드를 입력하세요</notice>");
			}else if(result == -4){
				out.print("<notice>없는 아이디.</notice>");
			}else if(result == -3){
				out.print("<notice>"+ErrorCause.getInstance().getErrorMSG()+"</notice>");
			}
	%>
</body>
</html>
