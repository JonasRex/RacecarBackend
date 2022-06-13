package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.DriverDTO;
import dtos.UserDTO;
import entities.User;
import entities.Driver;
import entities.Car;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class DriverResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Car c1;
    private static Driver d1, d2, d3;
    private static User u1, u2;


    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.createNamedQuery("Driver.deleteAllRows").executeUpdate();
            em.createNamedQuery("Car.deleteAllRows").executeUpdate();
            em.createNamedQuery("Race.deleteAllRows").executeUpdate();

            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();


            c1 = new Car("Lightning McQueen", "Ford", "Taurus", "2012", "Shell", "red");

            d1 = new Driver("Larsen", "1999", "rookie", "male");
            d2 = new Driver("Olsen", "1997", "pro", "female");

            u1 = new User("Larsen13", "test123", "Lars", "Larsen", "ll@mail.com");
            u2 = new User("Olsen123", "test123", "Olga", "Olsen", "oo@mail.com");

            c1.addDriver(d1);
            c1.addDriver(d2);

            d2.setUser(u2);

            em.persist(c1);
            em.persist(d1);
            em.persist(d2);
            em.persist(u1);
            em.persist(u2);


            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    public void testServerIsUp() {
        given().when().get("/driver").then().statusCode(200);
    }

    @Test
    void getAll() {
        List<DriverDTO> driverDTOS;

        driverDTOS = given()
                .contentType("application/json")
                .when()
                .get("/driver")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("", DriverDTO.class);


        assertEquals(driverDTOS.size(), 2);
    }

    @Test
    void getById() {
        given()
                .contentType("application/json")
                .get("/driver/{id}", d1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(d1.getName()))
                .body("experience", equalTo(d1.getExperience()))
                .body("gender", equalTo(d1.getGender()));
    }

    @Test
    public void testFailByID() {
        System.out.println("================================================================");
        System.out.println("OBS: Test failing on purpose, when finding entity by invalid ID: ");
        System.out.println("================================================================");

        given()
                .contentType("application/json")
                .get("/driver/99999")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("Driver with ID: 99999 was not found"));
    }


    @Test
    void create() {
        d3 = new Driver("Ronaldo", "1989", "rookie", "male");
        String requestBody = GSON.toJson(new DriverDTO(d3));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/driver")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(d3.getName()))
                .body("experience", equalTo(d3.getExperience()))
                .body("gender", equalTo(d3.getGender()));
    }

    @Test
    void update() {
        DriverDTO driverDTO = new DriverDTO(d1);
        driverDTO.setExperience("pro");
        String requestBody = GSON.toJson(driverDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/driver/" + d1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(d1.getId()))
                .body("experience", equalTo("pro"));
    }

    @Test
    void delete() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", d1.getId())
                .delete("/driver/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(d1.getId()));
    }

    @Test
    void addRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", d1.getId()).pathParam("entityid", u1.getId())
                .put("/driver/add/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(d1.getId()));

        // TODO Miss check for User being added to driver.
    }

    @Test
    void removeRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", d2.getId()).pathParam("entityid", u1.getId())
                .delete("/driver/remove/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(d2.getId()))
                .body("userDTO", equalTo(null));


    }

    @Test
    public void getCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/driver/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
}