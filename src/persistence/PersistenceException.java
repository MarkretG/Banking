package persistence;
public class PersistenceException extends Exception{
    int errorCode;
    String message;
    public PersistenceException(String message,int errorCode)
    {
        this.errorCode=errorCode;
        this.message=message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
