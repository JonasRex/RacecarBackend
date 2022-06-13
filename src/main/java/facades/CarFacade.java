package facades;

import entities.Car;
import entities.Driver;
import entities.User;
import errorhandling.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class CarFacade implements IFacade<Car> {
    private static CarFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CarFacade() {
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static IFacade<Car> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CarFacade();
        }
        return instance;
    }

    @Override
    public Car create(Car car) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(car);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return car;
    }

    @Override
    public Car getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Car car = em.find(Car.class, id);
        if (car == null)
            throw new EntityNotFoundException("Car with ID: " + id + " was not found");
        return car;
    }

    @Override
    public List<Car> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Car> query = em.createQuery("SELECT c FROM Car c", Car.class);
        return query.getResultList();
    }

    @Override
    public Car update(Car car) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Car c = em.find(Car.class, car.getId());
        if (c == null) {
            throw new EntityNotFoundException("Car with ID: " + car.getId() + " was not found");
        }
        c.setName(car.getName());
        c.setBrand(car.getBrand());
        c.setMake(car.getMake());
        c.setYear(car.getYear());
        c.setSponsor(car.getSponsor());
        c.setColor(car.getColor());

        em.getTransaction().begin();
        Car updated = em.merge(c);
        em.getTransaction().commit();
        return car;
    }

    @Override
    public Car delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Car c = em.find(Car.class, id);
        if (c == null)
            throw new EntityNotFoundException("Could not remove Car with id: " + id);



        for (Driver driver : c.getDriverList()) {
            driver.setCar(null);
        }

        if (c.getRaceList() != null)
            c.getRaceList().remove(c);  // TODO not sure this is correct. Might have to merge race also.


        em.getTransaction().begin();
        em.remove(c);
        em.getTransaction().commit();
        return c;
    }

    @Override
    public Car addRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            Car car = em.find(Car.class,id1);
            if(car == null){
                throw new EntityNotFoundException("Car with ID: " + id1  + " was not found");
            }
            Driver driver = em.find(Driver.class,id2);
            if(driver == null){
                throw new EntityNotFoundException("Driver with ID: " + id2  + " was not found");
            }
            car.addDriver(driver);
            em.getTransaction().begin();
            Car updated = em.merge(car);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    @Override
    public Car removeRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            Car car = em.find(Car.class, id1);
            if(car == null){
                throw new EntityNotFoundException("Car with ID: " + id1 + " not found");
            }
            Driver driver = em.find(Driver.class, id2);
            if(driver == null){
                throw new EntityNotFoundException("Driver with ID: " + id2 + " not found");
            }

            car.removeDriver(driver);

            em.getTransaction().begin();
            Car updated = em.merge(car);
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
            return (long)em.createQuery("SELECT COUNT(c) FROM Car c").getSingleResult();
        }finally{
            em.close();
        }
    }
}
