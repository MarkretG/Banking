package logicalLayer;
public class LogicalException extends Exception{
    int errorCode;

    public LogicalException(String message,int errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }
    public LogicalException(String message,Throwable cause,int errorCode)
    {
        super(message, cause);
        this.errorCode=errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }

}
