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

    @Override
    public ArrayList<Long> addCustomers(ArrayList<Customer> customers) throws PersistenceException {
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
            preparedStatement.executeBatch();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                customer_ids.add(resultSet.getLong(1));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in insert query in customer table", errorCodeForSqlInsertQuery);
        }
        return customer_ids;
    }

    @Override
    public ArrayList<Customer> selectCustomers(ArrayList<Long> customer_ids) throws PersistenceException {
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
    public ArrayList<Customer> selectAllCustomers() throws PersistenceException {
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
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in insert query for add account", errorCodeForSqlInsertQuery);
        }
    }

    @Override
    public ArrayList<Long> addAccounts(HashMap<Long, Account> account) throws PersistenceException {
        ArrayList<Long> account_ids = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String query = "insert into account_info(customer_id,balance) values(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (Map.Entry<Long, Account> entry : account.entrySet()) {
                System.out.println(entry.getKey());
                preparedStatement.setLong(1, entry.getKey());
                // preparedStatement.setLong(2, account.getAccount_id());
                preparedStatement.setDouble(2, entry.getValue().getBalance());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                account_ids.add(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in insert query for add account ", errorCodeForSqlInsertQuery);
        }
        return account_ids;
    }

    @Override
    public ArrayList<Account> selectAccounts(ArrayList<Long> account_ids) throws PersistenceException {
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
    public Account selectAccount(long customer_id) throws PersistenceException {
        Account account = new Account();
        Connection connection = DBUtil.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from  account_info where customer_id=customer_id")) {
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
    public ArrayList<Account> selectAllAccounts() throws PersistenceException {
        ArrayList<Account> accounts = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from  account_info where status='active'")) {
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
        String query = "update account_info set balance=balance where  account_id=account_id";
        try( Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in update query for update account", errorCodeForSqlUpdateQuery);
        }

    }

    @Override
    public void deleteAccount(long customer_id,long account_id) throws PersistenceException{
        Connection connection = DBUtil.getConnection();
        String query = "update account_info set status='inactivate' where customer_id=customer_id and account_id=account_id";
        try( Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new PersistenceException("Exception occur in update query for update account", errorCodeForSqlUpdateQuery);
        }

    }

}