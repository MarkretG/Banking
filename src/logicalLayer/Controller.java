package logicalLayer;
import inMemoryStorageHandling.InMemoryStorageDAO;
import persistence.PersistenceDAO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
public class Controller {

    private static final int ERROR_CODE_FILE=502;
    private static final int ERROR_CODE_CLASS=503;

    private static PersistenceDAO persistenceDAO=null;
    private static InMemoryStorageDAO inMemoryStorageDAO=null;

    static Properties properties = new Properties();

    public static Properties getProperties() throws LogicalException
    {
        if (!properties.isEmpty()) {
            return properties;
        }

        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        try (FileReader reader = new FileReader(path + "controller.properties")) {
            properties.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new LogicalException("file not found please check your file path",e, ERROR_CODE_FILE);
        } catch (IOException i) {
            i.printStackTrace();
            throw new LogicalException("you are try to read file that does not exist",i, ERROR_CODE_FILE);
        }
        return properties;
    }

    public static synchronized PersistenceDAO getPersistenceDAOHandler()throws LogicalException{

        if (persistenceDAO!= null) {
            return persistenceDAO;
        }
        try {
            String className = (String) getProperties().get("persistenceDAO");
            nullCheck(className);
            persistenceDAO = (PersistenceDAO) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException  e) {
            e.printStackTrace();
            throw new LogicalException("Exception occur in get persistenceDAO ",e, ERROR_CODE_CLASS);
        }
        return persistenceDAO;
    }

        public static synchronized InMemoryStorageDAO getInMemoryStorageDAOHandler()throws LogicalException{
        if (inMemoryStorageDAO != null) {
            return inMemoryStorageDAO;
        }
        try {
            String className = (String) getProperties().get("inMemoryStorageDAO");
            nullCheck(className);
            inMemoryStorageDAO = (InMemoryStorageDAO) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new LogicalException("Exception occur in get inMemoryStorageDAO",e, ERROR_CODE_CLASS);
        }
        return inMemoryStorageDAO;
    }

     private static void nullCheck(String className) throws LogicalException {
        if(className==null)
        {
            throw new LogicalException("class name pointing to null",ERROR_CODE_CLASS);
        }
    }
}