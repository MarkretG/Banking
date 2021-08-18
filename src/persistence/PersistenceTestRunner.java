/*package persistence;
import bankingManagement.Customer;

import java.util.ArrayList;
import java.util.Scanner;
public class PersistenceTestRunner {

    public static void main(String[] args) throws PersistenceException {
        PersistenceDAOImpl persistenceDAO=new PersistenceDAOImpl();
        Scanner sc=new Scanner(System.in);
        System.out.println("1.add customers");
        System.out.println("2.addAccount");
        System.out.println("5.addAccounts");
        System.out.println("7.getAccount");
        System.out.println("9.updateAccount");
        System.out.println("10.deleteAccount");
        while (true)
        {
            int choice= sc.nextInt();
            switch (choice)
            {
                case 1:
                    ArrayList<Customer> customers=new ArrayList<>();
                    Customer customer=new Customer();
                    customer.setName("chithu");
                    customer.setAge(22);
                    customer.setName("vithya");
                    customers.add(new Customer("mugil",34,9967718121l));
                    customers.add(new Customer("akil",22,8863127661l));
                    customers.add(customer);
                    ArrayList<Long> customers1=persistenceDAO.addCustomers(customers);
                    for (Long customer1:customers1)
                    {
                        System.out.println(customer1);
                    }
                case 2:
                    System.out.println("add account");
                   // long customer_id= sc.nextLong();
                    //double balance=sc.nextDouble();
                    long l = persistenceDAO.addAccount(76789543, 78675);
                    System.out.println(l);
            }

            }

        }

    }



 */