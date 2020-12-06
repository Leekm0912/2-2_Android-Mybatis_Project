package ldg.mybatis.model;

public class ErrorCause {
	private static int ErrorCode = 0;
	private static String ErrorMSG;
	private static ErrorCause instance = new ErrorCause();

	public static ErrorCause getInstance() {
		return instance;
		
	}
	public int getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(int errorCode) {
		ErrorCode = errorCode;
	}
	public String getErrorMSG() {
		return ErrorMSG;
	}
	public void setErrorMSG(String errorMSG) {
		ErrorMSG = errorMSG;
	}

}
