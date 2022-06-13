package facades;

import utils.Utility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class DataFacade {
    private static DataFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private DataFacade() {
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static DataFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DataFacade();

        }
        return instance;
    }


    public String getRandomQuote() throws IOException {
        // Documentation: https://premium.zenquotes.io/zenquotes-documentation/
        return Utility.fetchData("https://zenquotes.io/api/random");
    }

    public String getRandomJoke() throws IOException {
        return Utility.fetchData("https://icanhazdadjoke.com");
    }

    public static void main(String[] args) {
        // Use this to test your data fetching.
        try {
            System.out.println(Utility.fetchData("https://zenquotes.io/api/random"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
