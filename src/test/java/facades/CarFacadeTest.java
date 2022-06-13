package facades;

import entities.Driver;
import entities.Car;
import entities.Race;
import entities.User;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class CarFacadeTest {

    private static EntityManagerFactory emf;
    private static IFacade<Car> facade;
    Race r1;
    Car c1, c2;
    Driver d1;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CarFacade.getFacade(emf);


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

            r1 = new Race("Sunday Cup", "Roskilde", "01072022", 2);

            c1 = new Car("Lightning McQueen", "Ford", "Taurus", "2012", "Shell", "red");
            c2 = new Car("SilverArrow", "Mercedes", "AMC", "2019", "SAS", "silver");

            d1 = new Driver("Larsen", "1999",  "pro", "male");

            r1.addCar(c1);
            r1.addCar(c2);

            c2.addDriver(d1);

            em.persist(r1);
            em.persist(c1);
            em.persist(c2);
            em.persist(d1);


            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void create() {
        Car expected = new Car("Bender", "Oldsmobile", "Old", "1956", "ATP", "brown");
        Car actual = facade.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        Car expected = c1;
        Car actual = facade.getById(c1.getId());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getAll() {
        int actual = facade.getAll().size();
        assertEquals(2, actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        c1.setColor("blue");
        Car expected = c1;
        Car actual = facade.update(c1);
        assertEquals(expected.getColor(),actual.getColor());
    }

    @Test
    void delete() throws EntityNotFoundException {
        Car car = facade.delete(c1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(car.getId(),c1.getId());
    }

    @Test
    void addRelation() throws EntityNotFoundException {
        c1.addDriver(d1);
        Car car = facade.addRelation(c1.getId(), d1.getId());
        assertEquals(1, c1.getDriverList().size());
        assertEquals(car.getId(), c1.getId());
    }

    @Test
    void removeRelation() throws EntityNotFoundException {
        c2.getDriverList().remove(d1);
        Car car = facade.removeRelation(c2.getId(), d1.getId());
        assertEquals(0, c2.getDriverList().size());
        assertEquals(car.getId(), c2.getId());
    }

    @Test
    void getCount() {
        long actual = facade.getCount();
        assertEquals(2, actual);
    }
}