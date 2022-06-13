package facades;
import java.util.List;
import errorhandling.EntityNotFoundException;

public interface IFacade<T> {
    T create(T t);
    T getById(int id) throws EntityNotFoundException;
    List<T> getAll();
    T update(T t) throws EntityNotFoundException;
    T delete(int id) throws EntityNotFoundException;
    T addRelation(int id1, int id2) throws EntityNotFoundException;
    T removeRelation(int id1, int id2) throws EntityNotFoundException;
    long getCount();
}