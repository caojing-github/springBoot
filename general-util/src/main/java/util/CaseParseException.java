package util;

/**
 * 案例解析异常
 *
 * @author CaoJing
 * @date 2019/12/24 15:45
 */
public class CaseParseException extends RuntimeException {

    private static final long serialVersionUID = 6328080156962443814L;

    public CaseParseException() {
        super();
    }

    public CaseParseException(String message) {
        super(message);
    }

    public CaseParseException(String message, Throwable cause) {
        super(message, cause);
    }
}