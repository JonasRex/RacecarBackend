package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.RaceDTO;
import entities.Car;
import entities.Race;
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

class RaceResourceTest {    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Race r1, r2, r3;
    private static Car c1;


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


            r1 = new Race("Sunday Cup", "Roskilde", "01072022", 2);
            r2 = new Race("Rookie Cup", "Taastrup", "30062022", 1);

            c1 = new Car("Lightning McQueen", "Ford", "Taurus", "2012", "Shell", "red");
            r2.addCar(c1);

            em.persist(r1);
            em.persist(r2);
            em.persist(c1);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    public void testServerIsUp() {
        given().when().get("/race").then().statusCode(200);
    }

    @Test
    void getAll() {
        List<RaceDTO> raceDTOS;

        raceDTOS = given()
                .contentType("application/json")
                .when()
                .get("/race")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("", RaceDTO.class);


        assertEquals(raceDTOS.size(), 2);
    }

    @Test
    void getById() {
        given()
                .contentType("application/json")
                .get("/race/{id}", r1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(r1.getName()))
                .body("location", equalTo(r1.getLocation()))
                .body("duration", equalTo(r1.getDuration()));
    }

    @Test
    public void testFailByID() {
        System.out.println("================================================================");
        System.out.println("OBS: Test failing on purpose, when finding entity by invalid ID: ");
        System.out.println("================================================================");

        given()
                .contentType("application/json")
                .get("/race/99999")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("Race with ID: 99999 was not found"));
    }


    @Test
    void create() {
        r3 = new Race("Championship", "Valby", "20072022", 3);
        String requestBody = GSON.toJson(new RaceDTO(r3));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/race")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(r3.getName()))
                .body("location", equalTo(r3.getLocation()))
                .body("duration", equalTo(r3.getDuration()));
    }

    @Test
    void update() {
        RaceDTO raceDTO = new RaceDTO(r1);
        raceDTO.setName("Monday Cup");
        String requestBody = GSON.toJson(raceDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/race/" + r1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(r1.getId()))
                .body("name", equalTo("Monday Cup"));
    }

    @Test
    void delete() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", r1.getId())
                .delete("/race/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(r1.getId()));
    }

    @Test
    void addRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", r1.getId()).pathParam("entityid", c1.getId())
                .put("/race/add/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(r1.getId()))
                .body("carDTOs", hasItems(hasEntry("id",c1.getId())));

    }

    @Test
    void removeRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", r2.getId()).pathParam("entityid", c1.getId())
                .delete("/race/remove/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(r2.getId()))
                .body("carDTOs", empty());
    }

    @Test
    public void getCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/race/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
}