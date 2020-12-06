package ldg.mybatis.model;

public class Comment {
	private String 댓글번호; // tomcat
	private String 등록일; // tomcat
	
	private String 게시글번호; // android
	private String 작성자; // android
	private String 내용; // android

	public String get등록일() {
		return 등록일;
	}
	public void set등록일(String 등록일) {
		this.등록일 = 등록일;
	}
	public String get작성자() {
		return 작성자;
	}
	public void set작성자(String iD) {
		작성자 = iD;
	}
	public String get내용() {
		return 내용;
	}
	public void set내용(String 내용) {
		this.내용 = 내용;
	}
	public String get댓글번호() {
		return 댓글번호;
	}
	public void set댓글번호(String 댓글번호) {
		this.댓글번호 = 댓글번호;
	}
	public String get게시글번호() {
		return 게시글번호;
	}
	public void set게시글번호(String 게시글번호) {
		this.게시글번호 = 게시글번호;
	}
}
