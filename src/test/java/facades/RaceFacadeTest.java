package facades;

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

class RaceFacadeTest {

    private static EntityManagerFactory emf;
    private static IFacade<Race> facade;
    Race r1, r2;
    Car c1;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = RaceFacade.getFacade(emf);


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

            em.createNamedQuery("Race.deleteAllRows").executeUpdate();
            em.createNamedQuery("Car.deleteAllRows").executeUpdate();


            r1 = new Race("Sunday Cup", "Roskilde", "01072022", 2);
            r2 = new Race("Rookie Cup", "Taastrup", "30062022", 1);

            c1 = new Car("Lightning McQueen", "Ford", "Taurus", "2012", "Shell", "red");
            r2.addCar(c1);

            em.persist(r1);
            em.persist(r2);
            em.persist(c1);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void create() {
        Race expected = new Race("Championship", "Valby", "20072022", 3);
        Race actual = facade.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        Race expected = r1;
        Race actual = facade.getById(r1.getId());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getAll() {
        int actual = facade.getAll().size();
        assertEquals(2, actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        r1.setName("Monday Cup");
        Race expected = r1;
        Race actual = facade.update(r1);
        assertEquals(expected.getName(),actual.getName());
    }

    @Test
    void delete() throws EntityNotFoundException {
        Race race = facade.delete(r1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(race.getId(),r1.getId());
    }

    @Test
    void addRelation() throws EntityNotFoundException {
        r1.addCar(c1);
        Race race = facade.addRelation(r1.getId(), c1.getId());
        assertEquals(1, r1.getCarList().size());
        assertEquals(race.getId(), r1.getId());
    }

    @Test
    void removeRelation() throws EntityNotFoundException {
        r2.getCarList().remove(c1);
        Race race = facade.removeRelation(r2.getId(), c1.getId());
        assertEquals(0, r2.getCarList().size());
        assertEquals(race.getId(), r2.getId());
    }

    @Test
    void getCount() {
        long actual = facade.getCount();
        assertEquals(2, actual);
    }
}