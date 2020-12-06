package ldg.mybatis.repository.session;

import java.io.InputStream;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.*;
import ldg.mybatis.model.*;

public class EcoDesignSessionRepository {	
	private SqlSessionFactory getSqlSessionFactory(){
		//설정정보의 InputStream을 받아온다.
		String resource = "/mybatis-config.xml";
		InputStream inputStream;
		try {
			inputStream = getClass().getResourceAsStream(resource);
			System.out.println("Eco_SqlSessionFactory 정상작동");
		}catch(Exception e){
			System.out.println("SqlSessionFactory 에러발생");
			throw new IllegalArgumentException(e);
		}
		
		//InputStream을 통해 설정정보를 읽어서 SqlSessionFactory를 빌드한다.
		SqlSessionFactory s = new SqlSessionFactoryBuilder().build(inputStream);
		return s;
	}
	
	public 사용자 selectUserByPrimaryKey(String id) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.selectUserByPrimaryKey";
			사용자 c = sqlSesson.selectOne(stmt,id);
			System.out.println("사용자 조회 결과 : " + c.get이름());
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public int compareUser(String ID, String PW) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			if(ID == null || PW == null) {
				return 0; // 아이디 또는 비밀번호 미입력
			}
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.selectUserByPrimaryKey";
			사용자 c = sqlSesson.selectOne(stmt,ID);
			if(c.getID().equals(ID)) {
				if(c.getPW().contentEquals(PW)) {
					return 1; // 아이디, 패스워드 일치
				}else {
					return -2; // 비밀번호 불일치
				}
			}else {
				return -1; // 아이디 불일치
			}
		}catch(PersistenceException e1) {
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -3;
		}catch(Exception e){
			e.printStackTrace();
			return -4;
		}finally {
			sqlSesson.close();
		}
	}
	
	public Integer insertUser(사용자 buyer) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.insertUser";
			int result = sqlSesson.insert(stmt,buyer);
			if(result > 0) {
				sqlSesson.commit();
				System.out.println("사용자 커밋 완료");
			}
			return result;
		}catch(PersistenceException e1) {
			e1.printStackTrace();
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
	public Integer postUpload(게시글 f) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.postUpload";
			
			String num = getBoardNum();
			int board_num = 0;
			if(num == null) {
				board_num = 1;
			}else {
				board_num = Integer.parseInt(num) + 1;
			}
			f.set게시글번호(Integer.toString(board_num));
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
			String datestr = sdf.format(cal.getTime());
			f.set등록일(datestr);
			
			int result = sqlSesson.insert(stmt,f);
			if(result > 0) {
				
				System.out.println("게시물 업로드 완료");
				
				if(f.get분류().equals("DIY예약")) {
					stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.postUploadDIY";
				}else if(f.get분류().equals("상담예약")) {
					stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.postUploadReserve";
				}else if(f.get분류().equals(("공지사항"))){
					stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.postUploadNotice";
				}
				int result2 = sqlSesson.insert(stmt,f);
				if(result2 > 0) {
					System.out.println("세부 게시판 등록 완료");
					sqlSesson.commit();
				}
			}
			return board_num;
		}catch(PersistenceException e1) {
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
	public String getBoardNum() {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getBoardNum";
			String c = sqlSesson.selectOne(stmt);
			System.out.println("최근 게시글번호 : " + c);
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public List<게시글> getDIYBoard() {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getDIYBoard";
			List<게시글> c = sqlSesson.selectList(stmt);
			System.out.println("DIY 뷰 조회 완료");
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public List<게시글> getReserveBoard() {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getReserveBoard";
			List<게시글> c = sqlSesson.selectList(stmt);
			System.out.println("상담예약 뷰 조회 완료");
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public List<게시글> getNoticeBoard() {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getNoticeBoard";
			List<게시글> c = sqlSesson.selectList(stmt);
			System.out.println("공지사항 조회 완료");
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public 게시글 getDIYDetailBoard(String num) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getDIYDetailBoard";
			게시글 c = sqlSesson.selectOne(stmt,num);
			System.out.println("DIY 뷰 조회 완료");
			
			List<Comment> d = selectBoardComment(num);
			System.out.println("댓글조회결과 : "+d+", size : "+d.size());
			if(d.size() != 0) {
				c.set댓글(d); 
			}
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public 게시글 getReserveDetailBoard(String num) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getReserveDetailBoard";
			게시글 c = sqlSesson.selectOne(stmt,num);
			System.out.println("상담예약 뷰 조회 완료");
			
			List<Comment> d = selectBoardComment(num);
			if(d.size() != 0) {
				c.set댓글(d); 
			}
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public 게시글 getNoticeDetailBoard(String num) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getNoticeDetailBoard";
			게시글 c = sqlSesson.selectOne(stmt,num);
			System.out.println("공지사항 뷰 조회 완료");
			
			List<Comment> d = selectBoardComment(num);
			System.out.println("댓글조회결과 : "+d+", size : "+d.size());
			if(d.size() != 0) {
				c.set댓글(d); 
			}
			return c;
		}finally {
			sqlSesson.close();
		}
	}
	
	public List<Comment> selectBoardComment(String num){
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
		String stmt="ldg.mybatis.repository.mapper.Eco_design_Mapper.selectBoardComment";
		List<Comment> d = sqlSesson.selectList(stmt,num);
		System.out.println("댓글 조회 완료");
		
		return d;
		}finally {
			sqlSesson.close();
		}
	}
	
	public Integer insertComment(Comment f,String board_num) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.insertComment";
			System.out.println("댓글 작성중. 게시글 번호 : " + board_num);

			String num = getCommentNum(board_num);
			int comment_num = 0;
			if(num == null) {
				comment_num = 1;
			}else {
				comment_num = Integer.parseInt(num) + 1;
			}
			f.set댓글번호(Integer.toString(comment_num));
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
			String datestr = sdf.format(cal.getTime());
			f.set등록일(datestr);
			
			int result = sqlSesson.insert(stmt,f);
			if(result > 0) {
				
				System.out.println("댓글 작성 완료");
				sqlSesson.commit();
			}
			return result;
		}catch(PersistenceException e1) {
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
	public String getCommentNum(String num) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.getCommentNum";
			String c = sqlSesson.selectOne(stmt,num);
			
			if(c == null) {
				System.out.println("최근 댓글 없음");
				return null;
			}else {
				System.out.println("최근 댓글번호 : " + c);
				return c;
			}
		}finally {
			sqlSesson.close();
		}
	}
	
	public int deleteBoard(String board_num) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.deleteBoard";
			int c = sqlSesson.delete(stmt,board_num);
			
			if(c > 0) {
				System.out.println(board_num+"번 게시물 삭제 완료");
				sqlSesson.commit();
			}else {
				System.out.println("변경없음");
			}
			return c;
		}catch(PersistenceException e1) {
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
	public int deleteComment(String comment_num) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.deleteComment";
			int c = sqlSesson.delete(stmt,comment_num);
			
			if(c > 0) {
				System.out.println(comment_num+"번 댓글 삭제 완료");
				sqlSesson.commit();
			}
			return c;
		}catch(PersistenceException e1) {
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
	public Integer updateBoard(게시글 f) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.updateBoard";
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
			String datestr = sdf.format(cal.getTime());
			f.set등록일(datestr);
			
			int result = sqlSesson.update(stmt,f);
			if(result > 0) {
				
				System.out.println("게시물 업데이트 완료");
				
				if(f.get분류().equals("DIY예약")) {
					stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.updateDIYBoard";
				}else if(f.get분류().equals("상담예약")) {
					stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.updateReserveBoard";
				}else if(f.get분류().equals("공지사항")) {
					stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.updateNoticeBoard";
				}
				int result2 = sqlSesson.update(stmt,f);
				if(result2 > 0) {
					System.out.println("세부 게시판 업데이트 완료");
					sqlSesson.commit();
				}
			}
			return result;
		}catch(PersistenceException e1) {
			//e1.printStackTrace();
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
	public Integer updateComment(Comment f) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.updateComment";
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
			String datestr = sdf.format(cal.getTime());
			f.set등록일(datestr);
			System.out.println("댓글 내용 : "+f.get내용());
			System.out.println("commentnum 내용 : "+f.get댓글번호());
			int result = sqlSesson.update(stmt,f);
			System.out.println("댓글 업데이트 result : "+result);
			if(result > 0) {
				System.out.println("댓글 업데이트 완료");
				sqlSesson.commit();
			}
			return result;
		}catch(PersistenceException e1) {
			//e1.printStackTrace();
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
	public Integer uploadImg(게시글 f) {
		SqlSession sqlSesson = getSqlSessionFactory().openSession();
		try {
			String stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.postUpload";
			
			String num = getBoardNum();
			int board_num = 0;
			if(num == null) {
				board_num = 1;
			}else {
				board_num = Integer.parseInt(num) + 1;
			}
			f.set게시글번호(Integer.toString(board_num));
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
			String datestr = sdf.format(cal.getTime());
			f.set등록일(datestr);
			
			int result = sqlSesson.insert(stmt,f);
			if(result > 0) {
				
				System.out.println("게시물 업로드 완료");
				stmt = "ldg.mybatis.repository.mapper.Eco_design_Mapper.postUploadNotice";
				int result2 = sqlSesson.insert(stmt,f);
				if(result2 > 0) {
					System.out.println("공지사항 등록 완료");
					sqlSesson.commit();
				}
			}
			return result;
		}catch(PersistenceException e1) {
			System.out.println("Cause : "+e1.getCause().getMessage());
			ErrorCause ec = ErrorCause.getInstance();
			ec.setErrorMSG(e1.getCause().getMessage());
			return -2;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally {
			sqlSesson.close();
		}
	}
	
}