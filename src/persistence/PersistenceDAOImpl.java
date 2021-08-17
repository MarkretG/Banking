package persistence;
import bankingManagement.Account;
import bankingManagement.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class PersistenceDAOImpl implements PersistenceDAO {
    private final int errorCodeForSqlInsertQuery = 402;
    private final int errorCodeForSqlSelectQuery = 403;
    private final int errorCodeForSqlUpdateQuery=404;
    private final int errorCodeEmptyList=405;

    @Override
    public ArrayList<Long> addCustomers(ArrayList<Customer> customers) throws PersistenceException {
        validateListOfCustomers(customers);
        ArrayList<Long> customer_ids = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query = "insert into customer_info(name,age,phone) values(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //preparedStatement.setLong(1, customer.getCustomer_id());
            for (Customer customer : customers) {
                preparedStatement.setString(1, customer.getName());
                preparedStatement.setInt(2, customer.getAge());
                preparedStatement.setLong(3, customer.getPhone());
                preparedStatement.addBatch();
            }
            handleBatchUpdateException(preparedStatement);
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                customer_ids.add(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception occur in insert query in customer table", errorCodeForSqlInsertQuery);
        }
        return customer_ids;
    }

    @Override
    public ArrayList<Customer> getCustomers(ArrayList<Long> customer_ids) throws PersistenceException {
        if (customer_ids==null||customer_ids.isEmpty())
        {
            throw  new PersistenceException("select  customers list is empty or null",errorCodeEmptyList);
        }
        ArrayList<Customer> customers = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query = "select customer_id,name from customer_info where customer_id in (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Long ids : customer_ids) {
                preparedStatement.setLong(1, ids);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Customer customerList = new Customer();
                customerList.setCustomer_id(resultSet.getLong(1));
                customerList.setName(resultSet.getString(2));
                customers.add(customerList);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in select query for customer table", errorCodeForSqlSelectQuery);
        }
        return customers;

    }


    @Override
    public ArrayList<Customer> getAllCustomers() throws PersistenceException {
        ArrayList<Customer> customers = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query = "select customer_id,name from customer_info";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Customer customerList = new Customer();
                customerList.setCustomer_id(resultSet.getLong(1));
                customerList.setName(resultSet.getString(2));
                customers.add(customerList);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in select query for customer table", errorCodeForSqlSelectQuery);
        }
        return customers;

    }

    @Override
    public void addAccount(long customer_id, double balance) throws PersistenceException {
        Connection connection = DBUtil.getConnection();
        String query = "insert into account_info(customer_id,balance) values(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, customer_id);
            preparedStatement.setDouble(2, balance);
            preparedStatement.executeUpdate();
            System.out.println("account added successfully");
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in insert query for add account", errorCodeForSqlInsertQuery);
        }
    }

    @Override
    public ArrayList<Long> addAccounts(HashMap<Long, Account> accounts) throws PersistenceException {

        ArrayList<Long> account_ids = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query = "insert into account_info(customer_id,balance) values(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (Map.Entry<Long, Account> entry : accounts.entrySet()) {
                System.out.println(entry.getKey());
                preparedStatement.setLong(1, entry.getKey());
                // preparedStatement.setLong(2, account.getAccount_id());
                preparedStatement.setDouble(2, entry.getValue().getBalance());
                preparedStatement.addBatch();
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                account_ids.add(resultSet.getLong(1));
            }
            System.out.println("accounts added successfully");
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in insert query for add account ", errorCodeForSqlInsertQuery);
        }
        return account_ids;
    }

    @Override
    public ArrayList<Account> getAccounts(ArrayList<Long> account_ids) throws PersistenceException {
        if (account_ids.isEmpty())
        {
            throw  new PersistenceException("select accounts list is empty",errorCodeEmptyList);
        }
        ArrayList<Account> accounts = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query = "select customer_id,account_id,balance  from  account_info where account_id in (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Long account_id : account_ids) {
                preparedStatement.setLong(1, account_id);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account1 = new Account();
                account1.setCustomer_id(resultSet.getLong(1));
                account1.setAccount_id(resultSet.getLong(2));
                account1.setBalance(resultSet.getDouble(3));
                accounts.add(account1);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in insert query in account table", errorCodeForSqlInsertQuery);
        }
        return accounts;
    }

    @Override
    public Account getAccount(long customer_id) throws PersistenceException {
        Account account = new Account();
        Connection connection = DBUtil.getConnection();
        String query="select * from  account_info where customer_id=?";
        try (PreparedStatement preparedStatement= connection.prepareStatement(query))
        {
            preparedStatement.setLong(1,customer_id);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()) {
                account.setCustomer_id(resultSet.getLong(1));
                account.setAccount_id(resultSet.getLong(2));
                account.setBalance(resultSet.getDouble(3));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in select query in account table", errorCodeForSqlSelectQuery);
        }
        return account;
    }


    @Override
    public ArrayList<Account> getAllAccounts() throws PersistenceException {
        ArrayList<Account> accounts = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select customer_id,account_id,balance from  account_info where status='activate'")) {
            while (resultSet.next()) {
                Account account = new Account();
                account.setCustomer_id(resultSet.getLong(1));
                account.setAccount_id(resultSet.getLong(2));
                account.setBalance(resultSet.getDouble(3));
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in select query in account table", errorCodeForSqlSelectQuery);
        }

        return accounts;
    }


    @Override
    public void updateAccount(long account_id,double balance) throws PersistenceException {
        Connection connection = DBUtil.getConnection();
        String query = "update account_info set balance=? where  account_id=?";
        try( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1,balance);
            preparedStatement.setLong(2,account_id);
            preparedStatement.executeUpdate();
            System.out.println("balance updated successfully");
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in update query for update account", errorCodeForSqlUpdateQuery);
        }

    }

    @Override
    public void deleteAccount(long account_id) throws PersistenceException{
        Connection connection = DBUtil.getConnection();
        String query = "update account_info set active=0 where account_id=?";
        try( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1,account_id);
            preparedStatement.executeUpdate();
            System.out.println("Account Id:"+account_id+"account is deleted successfully");
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in update query for update account", errorCodeForSqlUpdateQuery);
        }

    }
    public void handleBatchUpdateException(PreparedStatement preparedStatement) throws SQLException {
        try {
            int[] count = preparedStatement.executeBatch();
            System.out.println(count.length+" "+"customer added successfully");
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
    public void validateListOfCustomers(ArrayList<Customer> customers)throws PersistenceException
    {
        if (customers==null||customers.isEmpty())
        {
            throw new PersistenceException("add customers list is empty or null",errorCodeEmptyList);
        }
    }
    public void validateListOfAccounts(ArrayList<Account> accounts) throws PersistenceException {
        if (accounts==null||accounts.isEmpty())
        {
            throw  new PersistenceException("add accounts map is empty or null",errorCodeEmptyList);
        }
    }

    public void handleResultSet(ResultSet resultSet)
    {

    }


}