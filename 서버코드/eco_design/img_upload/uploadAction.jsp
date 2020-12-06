<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<!-- 파일업로드 위한 라이브러리 임포트 -->
<%@ page import="java.io.File"%>

<!-- 파일 이름이 동일한게 나오면 자동으로 다른걸로 바꿔주고 그런 행동 해주는것 -->

<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>

<!-- 실제로 파일 업로드 하기 위한 클래스 -->

<%@ page import="com.oreilly.servlet.MultipartRequest"%>

<%@ page import="java.util.*"%>
<%@ page import="ldg.mybatis.model.*"%>
<%@ page import="ldg.mybatis.repository.session.*"%>
<%@ page import="java.sql.SQLIntegrityConstraintViolationException"%>
<!-- 위에것들 head 태그 위에 추가해줄 것 -->
<html>
<head>
<title>jsp_page</title>
</head>
<body>

	<%
	// 해당 폴더에 이미지를 저장시킨다
	/*
	String uploadDir = this.getClass().getResource("").getPath();
	uploadDir = uploadDir.substring(1, uploadDir.indexOf(".metadata"));
	uploadDir += "yonam-market/WebContent/market/img_upload/img";
	out.println("절대경로 : " + uploadDir + "<br/>");
	*/
	String uploadDir = "Z:\\web\\eco_img";

	// 총 100M 까지 저장 가능하게 함

	int maxSize = 1024 * 1024 * 100;

	String encoding = "UTF-8";

	// 사용자가 전송한 파일정보 토대로 업로드 장소에 파일 업로드 수행할 수 있게 함

	MultipartRequest multipartRequest

			= new MultipartRequest(request, uploadDir, maxSize, encoding,

			new DefaultFileRenamePolicy());

	// 중복된 파일이름이 있기에 fileRealName이 실제로 서버에 저장된 경로이자 파일

	// fineName은 사용자가 올린 파일의 이름이다

	// 이전 클래스 name = "file" 실제 사용자가 저장한 실제 네임

	String fileName = multipartRequest.getOriginalFileName("file");

	
	EcoDesignSessionRepository c = new EcoDesignSessionRepository();
	게시글 post = new 게시글();
	post.setFilePath(request.getHeader("uploaded_file"));
	post.set사용자_ID(request.getParameter("사용자_ID"));
	post.set제목(request.getParameter("제목"));
	post.set내용(request.getParameter("내용"));
	post.set분류(request.getParameter("분류"));
	System.out.println("분류 : "+post.get분류());
	int result = c.postUpload(post);
	
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
