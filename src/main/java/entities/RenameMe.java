package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "RENAMEME")
@NamedQuery(name = "RenameMe.deleteAllRows", query = "DELETE from RenameMe")
public class RenameMe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // TODO, delete this class, or rename to an Entity class that makes sense for what you are about to do
    // Delete EVERYTHING below if you decide to use this class, it's dummy data used for the initial demo
    @Column(name = "dummyStr1")
    private String dummyStr1;
    @Column(name = "dummyStr2")
    private String dummyStr2;

    @ManyToMany(mappedBy="renameMesList")
    private List<User> userList;

    public RenameMe() {
    }

    public RenameMe(String dummyStr1, String dummyStr2) {
        this.dummyStr1 = dummyStr1;
        this.dummyStr2 = dummyStr2;
        this.userList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDummyStr1() {
        return dummyStr1;
    }

    public void setDummyStr1(String dummyStr1) {
        this.dummyStr1 = dummyStr1;
    }

    public String getDummyStr2() {
        return dummyStr2;
    }

    public void setDummyStr2(String dummyStr2) {
        this.dummyStr2 = dummyStr2;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    @Override
    public String toString() {
        return "RenameMe{" +
                "id=" + id +
                ", dummyStr1='" + dummyStr1 + '\'' +
                ", dummyStr2='" + dummyStr2 +
                '}';
    }
}
