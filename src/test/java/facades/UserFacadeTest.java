package facades;

import entities.Role;
import entities.User;
import entities.RenameMe;
import entities.User;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;


class UserFacadeTest {
    private static EntityManagerFactory emf;
    private static IFacade<User> facade;
    User u1,u2;
    Role userRole;
    RenameMe r1;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getFacade(emf);


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
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            u1 = new User("AnnaAnna", "test","Anna", "Andersen", "aa@mail.com");
            u2 = new User("BomberBo", "test","Bo", "Berthelsen", "bb@mail.com");

            r1 = new RenameMe("First", "First");

            u2.addRenameMe(r1);

            userRole = new Role("user");

            u1.addRole(userRole);
            u2.addRole(userRole);

            em.persist(userRole);
            em.persist(u1);
            em.persist(u2);
            em.persist(r1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    void create() {
        User expected = new User("Henning", "test","Henning", "Olsen", "ho@mail.com");
        User actual   = facade.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        User expected = u1;
        User actual = facade.getById(u1.getId());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getAll() {
        int actual = facade.getAll().size();
        assertEquals(2, actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        u2.setFirstName("Lone");
        User expected = u2;
        User actual = facade.update(u2);
        assertEquals(expected.getFirstName(),actual.getFirstName());
    }

    @Test
    void delete() throws EntityNotFoundException {
        User p = facade.delete(u1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(p.getId(),u1.getId());
    }

    @Test
    void addRelation() throws EntityNotFoundException {
        u1.addRenameMe(r1);
        User p = facade.addRelation(u1.getId(), r1.getId());
        assertEquals(1, u1.getRenameMesList().size());
        assertEquals(p.getId(), u1.getId());
    }

    @Test
    void removeRelation() throws EntityNotFoundException {
        u2.removeRenameMe(r1);
        User p = facade.removeRelation(u2.getId(), r1.getId());
        assertEquals(0, u2.getRenameMesList().size());
        assertEquals(p.getId(), u2.getId());
    }

    @Test
    void getCount() {
    }
}