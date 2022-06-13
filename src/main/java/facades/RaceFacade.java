package facades;

import entities.Car;
import entities.Race;
import errorhandling.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class RaceFacade implements IFacade<Race> {
    private static RaceFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private RaceFacade() {
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static IFacade<Race> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RaceFacade();
        }
        return instance;
    }


    @Override
    public Race create(Race race) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(race);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return race;
    }

    @Override
    public Race getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Race race = em.find(Race.class, id);
        if (race == null)
            throw new EntityNotFoundException("Race with ID: " + id + " was not found");
        return race;
    }

    @Override
    public List<Race> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Race> query = em.createQuery("SELECT r FROM Race r", Race.class);
        return query.getResultList();
    }

    @Override
    public Race update(Race race) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Race r = em.find(Race.class, race.getId());
        if (r == null) {
            throw new EntityNotFoundException("Race with ID: " + race.getId() + " was not found");
        }
        r.setName(race.getName());
        r.setLocation(race.getLocation());
        r.setDate(race.getDate());
        r.setDuration(race.getDuration());

        em.getTransaction().begin();
        Race updated = em.merge(r);
        em.getTransaction().commit();
        return race;
    }

    @Override
    public Race delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Race r = em.find(Race.class, id);
        if (r == null)
            throw new EntityNotFoundException("Could not remove Race with id: " + id);


        if (r.getCarList() != null)
            for (Car car : r.getCarList() ) {
                r.removeCar(car);
            }


        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
        return r;
    }

    @Override
    public Race addRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            Race race = em.find(Race.class,id1);
            if(race == null){
                throw new EntityNotFoundException("Race with ID: " + id1  + " not found");
            }
            Car car = em.find(Car.class,id2);
            if(car == null){
                throw new EntityNotFoundException("Car with ID: " + id2  + " not found");
            }
            race.addCar(car);
            em.getTransaction().begin();
            Race updated = em.merge(race);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    @Override
    public Race removeRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            Race race = em.find(Race.class, id1);
            if(race == null){
                throw new EntityNotFoundException("Race with ID: " + id1 + " not found");
            }
            Car car = em.find(Car.class, id2);
            if(car == null){
                throw new EntityNotFoundException("Car with ID: " + id2 + " not found");
            }

            race.getCarList().remove(car);

            em.getTransaction().begin();
            Race updated = em.merge(race);
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
            return  (long)em.createQuery("SELECT COUNT(r) FROM Race r").getSingleResult();

        }finally{
            em.close();
        }
    }
}
