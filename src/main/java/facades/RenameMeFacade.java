package facades;

import entities.RenameMe;
import errorhandling.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class RenameMeFacade implements IFacade<RenameMe>{
    private static RenameMeFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private RenameMeFacade() {}

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static IFacade<RenameMe> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RenameMeFacade();
        }
        return instance;
    }

    @Override
    public RenameMe create(RenameMe renameMe) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(renameMe);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return renameMe;
    }

    @Override
    public RenameMe getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        RenameMe renameMe = em.find(RenameMe.class, id);
        if (renameMe == null)
            throw new EntityNotFoundException("The entity with ID: " + id + " was not found");
        return renameMe;
    }

    @Override
    public List<RenameMe> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<RenameMe> query = em.createQuery("SELECT r FROM RenameMe r", RenameMe.class);
        return query.getResultList();
    }

    @Override
    public RenameMe update(RenameMe renameMe) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        RenameMe r = em.find(RenameMe.class,renameMe.getId());
        if(r == null){
            throw new EntityNotFoundException("The entity with ID: " + renameMe.getId() + " was not found");
        }
        r.setDummyStr1(renameMe.getDummyStr1());
        r.setDummyStr2(renameMe.getDummyStr2());

        em.getTransaction().begin();
        RenameMe updated = em.merge(renameMe);
        em.getTransaction().commit();
        return updated;
    }

    @Override
    public RenameMe delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        RenameMe r = em.find(RenameMe.class, id);
        if (r == null)
            throw new EntityNotFoundException("Could not remove RENAMEME with id: "+id);
        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
        return r;
    }

    @Override
    public RenameMe addRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public RenameMe removeRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public long getCount() {
        EntityManager em = getEntityManager();
        try{
            long renameMeCount = (long)em.createQuery("SELECT COUNT(r) FROM RenameMe r").getSingleResult();
            return renameMeCount;
        }finally{
            em.close();
        }
    }
}
