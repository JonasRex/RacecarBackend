package facades;

import entities.RenameMe;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.EntityNotFoundException;
import security.errorhandling.AuthenticationException;

import java.util.List;

public class UserFacade implements IFacade<User> {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    public static UserFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = '" + username + "'", User.class);
            user = query.getSingleResult();
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    @Override
    public User create(User user) {
        EntityManager em = getEntityManager();
        Role role = em.find(Role.class, "user");
        user.addRole(role);
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
        return user;
    }

    @Override
    public User getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        User user = em.find(User.class, id);
        if (user == null)
            throw new EntityNotFoundException("User with ID: " + id + " was not found");
        return user;
    }

    @Override
    public List<User> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public User update(User user) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        try {
            User u = em.find(User.class, user.getId());
            if (u == null) {
                throw new EntityNotFoundException("User with ID: " + user.getId() + " was not found");
            }
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setEmail(user.getEmail());


            em.getTransaction().begin();
            User updated = em.merge(u);
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }

    @Override
    public User delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        try {
            User u = em.find(User.class, id);
            if (u == null)
                throw new EntityNotFoundException("Could not remove User with id: " + id);

            if (u.getRenameMesList() != null)
                for (RenameMe renameMe : u.getRenameMesList()) {
                    u.removeRenameMe(renameMe);
                }



            em.getTransaction().begin();
            em.remove(u);
            em.getTransaction().commit();
            return u;
        } finally {
            em.close();
        }
    }

    @Override
    public User addRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            User user = em.find(User.class,id1);
            if(user == null){
                throw new EntityNotFoundException("User with ID: " + id1  + " not found");
            }
            RenameMe renameMe = em.find(RenameMe.class,id2);
            if(renameMe == null){
                throw new EntityNotFoundException("RenameMe with ID: " + id2  + " not found");
            }
            user.addRenameMe(renameMe);
            em.getTransaction().begin();
            User updated = em.merge(user);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    @Override
    public User removeRelation(int id1, int id2) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try{
            User user = em.find(User.class, id1);
            if(user == null){
                throw new EntityNotFoundException("User with ID: " + id1 + " not found");
            }
            RenameMe renameMe = em.find(RenameMe.class, id2);
            if(renameMe == null){
                throw new EntityNotFoundException("RenameMe with ID: " + id2 + " not found");
            }

            user.getRenameMesList().remove(renameMe);

            em.getTransaction().begin();
            User updated = em.merge(user);
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
            return  (long)em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();

        }finally{
            em.close();
        }
    }

    public boolean usernameExists(String username) {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = '" + username + "'", User.class);
            List<User> results = query.getResultList();
            return !results.isEmpty();

        } finally {
            em.close();
        }


    }
}
