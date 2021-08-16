package logicalLayer;

import inMemoryStorageHandling.InMemoryStorageDAO;
import persistence.PersistenceDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Controller {

    private static final int errorCodeForFile=502;
    private static final int errorCodeForClass=503;

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
            throw new LogicalException("file not found please check your file path", errorCodeForFile);
        } catch (IOException i) {
            throw new LogicalException("you are try to read file that does not exist", errorCodeForFile);
        }
        return properties;
    }

    public static synchronized PersistenceDAO getPersistenceDAOHandler()throws LogicalException{

        if (persistenceDAO!= null) {
            return persistenceDAO;
        }
        try {
            String className = (String) getProperties().get("persistenceDAO");
                persistenceDAO = (PersistenceDAO) Class.forName(className).newInstance();
        } catch (NullPointerException|InstantiationException | IllegalAccessException | ClassNotFoundException  e) {
            throw new LogicalException("class name pointing to null or class not found exception occur ", errorCodeForClass);
        } catch (LogicalException e) {
            System.out.println("ERROR CODE:"+e.getErrorCode()+" "+e.getMessage());
        }
        return persistenceDAO;
    }

        public static synchronized InMemoryStorageDAO getInMemoryStorageDAOHandler()throws LogicalException{
        if (inMemoryStorageDAO != null) {
            return inMemoryStorageDAO;
        }
        try {
            String className = (String) getProperties().get("inMemoryStorageDAO");
            try{
            inMemoryStorageDAO = (InMemoryStorageDAO) Class.forName(className).newInstance();}
            catch (NullPointerException e)
            {
                throw new NullPointerException("class name pointing null  check your class name");
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new LogicalException("class not found in properties file", errorCodeForClass);
        } catch (LogicalException e) {
            System.out.println("ERROR CODE:"+e.getErrorCode()+" "+e.getMessage());
        }
        return inMemoryStorageDAO;
    }
}