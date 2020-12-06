package ldg.mybatis.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class 게시글 { 
	private List<Comment> 댓글; // 톰켓
	private String 등록일; // 톰켓
	private String 게시글번호; // 톰켓
	
	private String 제목; // 톰켓 or 안드로이드
	private String 내용; // 톰켓 or 안드로이드
	private String 사용자_ID; // 톰켓 or 안드로이드
	
	private String 분류; // 안드로이드
	private String filePath = null; //안드로이드

	
	public String get제목() {
		return 제목;
	}
	public void set제목(String 제목) {
		try {
			this.제목 = URLDecoder.decode(제목, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}
	public String get내용() {
		return 내용;
	}
	public void set내용(String 내용) {
		try {
			this.내용 = URLDecoder.decode(내용, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}
	public String get등록일() {
		return 등록일;
	}
	public void set등록일(String 등록일) {
		this.등록일 = 등록일;
	}
	public List<Comment> get댓글() {
		return 댓글;
	}
	public void set댓글(List<Comment> 댓글) {
		this.댓글 = 댓글;
	}
	public void add댓글(Comment 댓글) {
		this.댓글.add(댓글);
	}
	public String get분류() {
		return 분류;
	}
	public void set분류(String 분류) {
		this.분류 = 분류;
	}
	public String get사용자_ID() {
		return 사용자_ID;
	}
	public void set사용자_ID(String 사용자_ID) {
		this.사용자_ID = 사용자_ID;
	}
	public String get게시글번호() {
		return 게시글번호;
	}
	public void set게시글번호(String 게시글번호) {
		this.게시글번호 = 게시글번호;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
