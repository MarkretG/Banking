package persistence;
import bankingManagement.Account;
import bankingManagement.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class PersistenceDAOImpl implements PersistenceDAO {
    private final int Error_CODE_INSERT_QUERY = 402;
    private final int Error_CODE_SELECT_QUERY = 403;
    private final int Error_CODE_UPDATE_QUERY=404;
    private final int ERROR_CODE_FETCH_DATA=407;
    private final int ERROR_CODE_EMPTY_LIST=405;
    private final int ERROR_CODE=406;


    @Override
    public ArrayList<Long> addCustomers(ArrayList<Customer> customers) throws PersistenceException {
        if (customers==null||customers.isEmpty())
        {
            throw new PersistenceException("add customers list is empty or null",ERROR_CODE_EMPTY_LIST);
        }
        Connection connection = DBUtil.getConnection();
        String query = "insert into customer_info(name,age,phone) values(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (Customer customer : customers) {
                preparedStatement.setString(1, customer.getName());
                preparedStatement.setInt(2, customer.getAge());
                preparedStatement.setLong(3, customer.getPhone());
                preparedStatement.addBatch();
            }
            handleBatchUpdateException(preparedStatement);
            return getListOfGeneratedIdsFromResultSet(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in insert query in customer table",e,Error_CODE_INSERT_QUERY);
        }
    }

    @Override
    public ArrayList<Customer> getCustomers(ArrayList<Long> customer_ids) throws PersistenceException {
        if (customer_ids==null||customer_ids.isEmpty())
        {
            throw new PersistenceException("customers_id list is empty or null",ERROR_CODE_EMPTY_LIST);
        }
        Connection connection = DBUtil.getConnection();
        String query = "select customer_id,name from customer_info where customer_id in (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Long ids : customer_ids) {
                preparedStatement.setLong(1, ids);
            }
            return getListOfCustomersFromResultSet(preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in select query for customer table",e, Error_CODE_SELECT_QUERY);
        }

    }


    @Override
    public ArrayList<Customer> getAllCustomers() throws PersistenceException {
        ArrayList<Customer> customersList=new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query = "select customer_id,name from customer_info";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet=statement.executeQuery(query)){
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomer_id(resultSet.getLong(1));
                customer.setName(resultSet.getString(2));
                customersList.add(customer);
            }
            return customersList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in select query for customer table",e,Error_CODE_SELECT_QUERY);
        }
    }

    @Override
    public long addAccount(long customer_id, double balance) throws PersistenceException {
        if(customer_id<=0||balance<=0.0)
        {
            throw new PersistenceException("customer_id or balance is less than zero",ERROR_CODE);
        }
        long key=-1L;
        Connection connection = DBUtil.getConnection();
        String query = "insert into account_info(customer_id,balance) values(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, customer_id);
            preparedStatement.setDouble(2, balance);
            preparedStatement.executeUpdate();
            try (ResultSet resultSet=preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    key = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in insert query for add account",e, Error_CODE_INSERT_QUERY);
        }
        return key;
    }

    @Override
    public ArrayList<Long> addAccounts(HashMap<Long, Account> accounts) throws PersistenceException {
        if (accounts==null||accounts.isEmpty())
        {
            throw new PersistenceException("add accounts list is empty or null",ERROR_CODE_EMPTY_LIST);
        }
        Connection connection = DBUtil.getConnection();
        String query = "insert into account_info(customer_id,balance) values(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (Map.Entry<Long, Account> entry : accounts.entrySet()) {
                System.out.println(entry.getKey());
                preparedStatement.setLong(1, entry.getKey());
                preparedStatement.setDouble(2, entry.getValue().getBalance());
                preparedStatement.addBatch();
            }
            handleBatchUpdateException(preparedStatement);
            return getListOfGeneratedIdsFromResultSet(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in insert query for add account ",e, Error_CODE_INSERT_QUERY);
        }

    }

    @Override
    public ArrayList<Account> getAccounts(ArrayList<Long> account_ids) throws PersistenceException {
        if (account_ids==null||account_ids.isEmpty())
        {
            throw new PersistenceException("accounts_id  list is empty or null",ERROR_CODE_EMPTY_LIST);
        }
        Connection connection = DBUtil.getConnection();
        String query = "select customer_id,account_id,balance  from  account_info where account_id in (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Long account_id : account_ids) {
                preparedStatement.setLong(1, account_id);
            }
            return getListOfAccountsFromResultSet(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in insert query in account table",e,Error_CODE_INSERT_QUERY);
        }
    }

    @Override
    public Account getAccount(long customer_id) throws PersistenceException {
        if(customer_id<=0)
        {
            throw new PersistenceException("customer id is zero or less than zero",ERROR_CODE);
        }
        Account account = new Account();
        Connection connection = DBUtil.getConnection();
        String query="select * from  account_info where customer_id=?";
        try (PreparedStatement preparedStatement= connection.prepareStatement(query))
        {
            preparedStatement.setLong(1,customer_id);
            try(ResultSet resultSet=preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    account.setCustomer_id(resultSet.getLong(1));
                    account.setAccount_id(resultSet.getLong(2));
                    account.setBalance(resultSet.getDouble(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in select query in account table",e, Error_CODE_SELECT_QUERY);
        }
        return account;
    }


    @Override
    public ArrayList<Account> getAllAccounts() throws PersistenceException {
        ArrayList<Account> accountList=new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query="select customer_id,account_id,balance from  account_info where active=1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Account account = new Account();
                account.setCustomer_id(resultSet.getLong(1));
                account.setAccount_id(resultSet.getLong(2));
                account.setBalance(resultSet.getDouble(3));
                accountList.add(account);
            }
            return accountList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in select query in account table",e, Error_CODE_SELECT_QUERY);
        }
    }


    @Override
    public void updateAccount(long account_id,double balance) throws PersistenceException {
        if (account_id<=0||balance<=0.0)
        {
            throw new PersistenceException("account id and balance not less than zero",ERROR_CODE);
        }
        Connection connection = DBUtil.getConnection();
        String query = "update account_info set balance=? where  account_id=?";
        try( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1,balance);
            preparedStatement.setLong(2,account_id);
            int count=preparedStatement.executeUpdate();
            System.out.println(count+" "+"row(s) affected");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in update query for update account",e, Error_CODE_UPDATE_QUERY);
        }

    }

    @Override
    public void deleteAccount(long account_id) throws PersistenceException{
        if (account_id<=0)
        {
            throw new PersistenceException("account id is never less than zero",ERROR_CODE);
        }
        Connection connection = DBUtil.getConnection();
        String query = "update account_info set active=0 where account_id=?";
        try( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1,account_id);
            int count=preparedStatement.executeUpdate();
            System.out.println(count+" "+"row(s) affected");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in update query for update account",e, Error_CODE_UPDATE_QUERY);
        }

    }
    public void handleBatchUpdateException(PreparedStatement preparedStatement) throws SQLException {
        try {
            preparedStatement.executeBatch();
        }
        catch (BatchUpdateException e)
        {
            //getting the updated rows status before the exception has occurred
            int[] updateCount = e.getUpdateCounts();
            int count = 1;
            for (int i : updateCount) {
                if  (i == Statement.EXECUTE_FAILED) {
                    System.out.println("Error on Statement " + count +": Execution failed");
                }
                else {
                    System.out.println("Statement  " + count +": is executed");
                }
                count++; //Incrementing the count to display the next updated row no.
            }
            e.printStackTrace();
        }
    }
    private ArrayList<Long> getListOfGeneratedIdsFromResultSet(PreparedStatement preparedStatement) throws PersistenceException {
        ArrayList<Long> customerIdList=new ArrayList<>();

            try(ResultSet resultSet=preparedStatement.getGeneratedKeys()){
            while (resultSet.next()) {
                customerIdList.add(resultSet.getLong(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in get generated keys from table",e, ERROR_CODE_FETCH_DATA);
        }
        return customerIdList;
    }

    private ArrayList<Customer> getListOfCustomersFromResultSet(PreparedStatement preparedStatement) throws PersistenceException {
        ArrayList<Customer> customersList=new ArrayList<>();
        try(ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomer_id(resultSet.getLong(1));
                customer.setName(resultSet.getString(2));
                customersList.add(customer);
            }
            return  customersList;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in fetch data in customer table", e,ERROR_CODE_FETCH_DATA);
        }
    }

    private ArrayList<Account> getListOfAccountsFromResultSet(PreparedStatement preparedStatement) throws PersistenceException {
        ArrayList<Account> accountsList=new ArrayList<>();
        try( ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                Account account = new Account();
                account.setCustomer_id(resultSet.getLong(1));
                account.setAccount_id(resultSet.getLong(2));
                account.setBalance(resultSet.getDouble(3));
                accountsList.add(account);
            }
            return accountsList;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in fetch data in account table",e, ERROR_CODE_FETCH_DATA);
        }
    }

    @Override
    public void cleanUp() {
      DBUtil.closeConnection();
    }
}