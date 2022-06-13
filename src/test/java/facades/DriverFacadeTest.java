package facades;

import entities.User;
import entities.Driver;
import entities.Car;
import entities.User;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DriverFacadeTest {

    private static EntityManagerFactory emf;
    private static DriverFacade facade;
    Car c1;
    Driver d1, d2;
    User u1, u2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = (DriverFacade) DriverFacade.getFacade(emf);


    }

    // Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    @AfterAll
    public static void tearDownClass() {
        // emf.close();
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();



            em.createNamedQuery("Driver.deleteAllRows").executeUpdate();
            em.createNamedQuery("Car.deleteAllRows").executeUpdate();
            em.createNamedQuery("Race.deleteAllRows").executeUpdate();

            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();


            c1 = new Car("Lightning McQueen", "Ford", "Taurus", "2012", "Shell", "red");

            d1 = new Driver("Larsen", "1999",  "rookie", "male");
            d2 = new Driver("Olsen", "1997", "pro", "female");

            u1 = new User("Larsen13", "test123", "Lars", "Larsen", "ll@mail.com");
            u2 = new User("Olsen123", "test123", "Olga", "Olsen", "oo@mail.com");

            c1.addDriver(d1);
            c1.addDriver(d2);

            d2.setUser(u2);

            em.persist(c1);
            em.persist(d1);
            em.persist(d2);
            em.persist(u1);
            em.persist(u2);


            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void create() {
        Driver expected = new Driver("Ronaldo", "1989", "rookie", "male");
        Driver actual = facade.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        Driver expected = d1;
        Driver actual = facade.getById(d1.getId());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getAll() {
        int actual = facade.getAll().size();
        assertEquals(2, actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        d1.setExperience("pro");
        Driver expected = d1;
        Driver actual = facade.update(d1);
        assertEquals(expected.getExperience(),actual.getExperience());
    }

    @Test
    void delete() throws EntityNotFoundException {
        Driver driver = facade.delete(d1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(driver.getId(),d1.getId());
    }

    @Test
    void addRelation() throws EntityNotFoundException {
        d1.setUser(u1);
        Driver driver = facade.addRelation(d1.getId(), u1.getId());
        assertEquals(driver.getUser().getId(), d1.getUser().getId());
        assertEquals(driver.getId(), d1.getId());
    }

    @Test
    void removeRelation() throws EntityNotFoundException {
        d2.setUser(null);
        Driver driver = facade.removeRelation(d2.getId(), u2.getId());
        assertNull(d2.getUser());
        assertEquals(driver.getId(), d2.getId());
    }

    @Test
    void getCount() {
        long actual = facade.getCount();
        assertEquals(2, actual);
    }

    @Test
    void getAllDriversByCarID() {
        List<Driver> driverList = facade.getAllDriversByCarID(c1.getId());
        assertEquals(driverList.size(), c1.getDriverList().size());
    }
}