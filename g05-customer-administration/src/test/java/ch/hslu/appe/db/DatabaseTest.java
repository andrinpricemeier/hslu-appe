package ch.hslu.appe.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.hslu.appe.entities.Customer;
import ch.hslu.appe.entities.CustomerRepository;
import ch.hslu.appe.micro.ApplicationConfig;

import org.junit.jupiter.api.*;

import java.util.List;

@Disabled
/**
 * Testcases for Customer database
 * Disabled because the tests are successful in the local environment, but not on gitlab
 */
public class DatabaseTest {
    private ApplicationConfig appConfigTest;
    private CustomerRepository customerRepositoryTest;

    /**
     * Setup test database
     */
    @BeforeEach
    void beforeEach(){
        appConfigTest = new ApplicationConfig();
        customerRepositoryTest = new MongoDBCustomerRepository(appConfigTest.getDbUser(), appConfigTest.getDbPassword(), appConfigTest.getDbUrl(), appConfigTest.getDbPort(), "customer-mongodb-test");
    }

    /**
     * Drop test database
     */
    @AfterEach
    void afterEach(){
        customerRepositoryTest.getMongoDatabase().drop();
    }

    /**
     * Test method for adding customer to database
     */
    @Test
    final void testDbAddCustomer(){
        customerRepositoryTest.add(new Customer(1,"Hans", "Muster", "Musterstrasse", "1", "1000", "Luzern"));
        customerRepositoryTest.add(new Customer (2,"Peter", "Meier", "Musterstrasse", "2", "2000", "Zürich"));
        assertThat(customerRepositoryTest.getAllCustomer().size()).isEqualTo(2);
    }

    /**
     * Test method for getting customer from database
     */
    @Test
    final void testGetCustomerOne(){
        customerRepositoryTest.add(new Customer(1,"Hans", "Muster", "Musterstrasse", "1", "1000", "Luzern"));
        customerRepositoryTest.add(new Customer (2,"Peter", "Meier", "Musterstrasse", "2", "2000", "Zürich"));
        List<Customer> customerList;
        customerList = customerRepositoryTest.getAllCustomer();
        assertAll("CustomerDB",
                () -> assertThat(customerList.get(0).getCustomerId()).isEqualTo(1),
                () -> assertThat(customerList.get(0).getFirstName()).isEqualTo("Hans"),
                () -> assertThat(customerList.get(0).getLastName()).isEqualTo("Muster"),
                () -> assertThat(customerList.get(0).getStreet()).isEqualTo("Musterstrasse"),
                () -> assertThat(customerList.get(0).getNumber()).isEqualTo("1"),
                () -> assertThat(customerList.get(0).getPostalCode()).isEqualTo("1000"),
                () -> assertThat(customerList.get(0).getCity()).isEqualTo("Luzern"));
    }
}
