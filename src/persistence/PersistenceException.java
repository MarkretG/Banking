package persistence;
public class PersistenceException extends Exception{
    int errorCode;
    public PersistenceException(String message,int errorCode)
    {
        super(message);
        this.errorCode=errorCode;

    }
    public PersistenceException(String message,Throwable cause,int errorCode)
    {
        super(message, cause);
        this.errorCode=errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
