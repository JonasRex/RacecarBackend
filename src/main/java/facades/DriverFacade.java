package facades;

import entities.Driver;
import entities.User;
import errorhandling.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class DriverFacade implements IFacade<Driver> {
    private static DriverFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private DriverFacade() {
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static IFacade<Driver> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DriverFacade();
        }
        return instance;
    }

    @Override
    public Driver create(Driver driver) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(driver);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return driver;
    }

    @Override
    public Driver getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Driver driver = em.find(Driver.class, id);
        if (driver == null)
            throw new EntityNotFoundException("Driver with ID: " + id + " was not found");
        return driver;
    }

    @Override
    public List<Driver> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Driver> query = em.createQuery("SELECT d FROM Driver d", Driver.class);
        return query.getResultList();
    }

    @Override
    public Driver update(Driver driver) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Driver d = em.find(Driver.class, driver.getId());
        if (d == null) {
            throw new EntityNotFoundException("Driver with ID: " + driver.getId() + " was not found");
        }
        d.setName(driver.getName());
        d.setName(driver.getName());
        d.setBirthYear(driver.getBirthYear());
        d.setExperience(driver.getExperience());
        d.setGender(driver.getGender());

        em.getTransaction().begin();
        Driver updated = em.merge(d);
        em.getTransaction().commit();
        return driver;
    }

    @Override
    public Driver delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Driver d = em.find(Driver.class, id);
        if (d == null)
            throw new EntityNotFoundException("Could not remove Driver with id: " + id);



        d.setUser(null);


        em.getTransaction().begin();
        em.remove(d);
        em.getTransaction().commit();
        return d;
    }

    @Override
    public Driver addRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            Driver driver = em.find(Driver.class,id1);
            if(driver == null){
                throw new EntityNotFoundException("Driver with ID: " + id1  + " was not found");
            }
            User user = em.find(User.class,id2);
            if(user == null){
                throw new EntityNotFoundException("User with ID: " + id2  + " was not found");
            }
            driver.setUser(user);
            em.getTransaction().begin();
            Driver updated = em.merge(driver);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    @Override
    public Driver removeRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            Driver driver = em.find(Driver.class, id1);
            if(driver == null){
                throw new EntityNotFoundException("Driver with ID: " + id1 + " not found");
            }
            User user = em.find(User.class, id2);
            if(user == null){
                throw new EntityNotFoundException("User with ID: " + id2 + " not found");
            }

            driver.setUser(null);

            em.getTransaction().begin();
            Driver updated = em.merge(driver);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    @Override
    public long getCount() {
        EntityManager em = getEntityManager();
        try{
            return (long)em.createQuery("SELECT COUNT(d) FROM Driver d").getSingleResult();
        }finally{
            em.close();
        }
    }
}
