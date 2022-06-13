package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.CarDTO;
import entities.Driver;
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

class CarResourceTest {    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Race r1;
    private static Car c1, c2, c3;
    private static Driver d1;


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

            c1 = new Car("Lightning McQueen", "Ford", "Taurus", "2012", "Shell", "red");
            c2 = new Car("SilverArrow", "Mercedes", "AMC", "2019", "SAS", "silver");

            d1 = new Driver("Larsen", "1999",  "pro", "male");

            r1.addCar(c1);
            r1.addCar(c2);

            c2.addDriver(d1);

            em.persist(r1);
            em.persist(c1);
            em.persist(c2);
            em.persist(d1);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    public void testServerIsUp() {
        given().when().get("/car").then().statusCode(200);
    }

    @Test
    void getAll() {
        List<CarDTO> carDTOS;

        carDTOS = given()
                .contentType("application/json")
                .when()
                .get("/car")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("", CarDTO.class);


        assertEquals(carDTOS.size(), 2);
    }

    @Test
    void getById() {
        given()
                .contentType("application/json")
                .get("/car/{id}", c1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(c1.getName()))
                .body("brand", equalTo(c1.getBrand()))
                .body("make", equalTo(c1.getMake()));
    }

    @Test
    public void testFailByID() {
        System.out.println("================================================================");
        System.out.println("OBS: Test failing on purpose, when finding entity by invalid ID: ");
        System.out.println("================================================================");

        given()
                .contentType("application/json")
                .get("/car/99999")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("Car with ID: 99999 was not found"));
    }


    @Test
    void create() {
        c3 = new Car("Bender", "Oldsmobile", "Old", "1956", "ATP", "brown");
        String requestBody = GSON.toJson(new CarDTO(c3));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/car")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(c3.getName()))
                .body("brand", equalTo(c3.getBrand()))
                .body("make", equalTo(c3.getMake()));
    }

    @Test
    void update() {
        CarDTO carDTO = new CarDTO(c1);
        carDTO.setColor("blue");
        String requestBody = GSON.toJson(carDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/car/" + c1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(c1.getId()))
                .body("color", equalTo("blue"));
    }

    @Test
    void delete() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", c1.getId())
                .delete("/car/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(c1.getId()));
    }

    @Test
    void addRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", c1.getId()).pathParam("entityid", d1.getId())
                .put("/car/add/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(c1.getId()))
                .body("driverDTOS", hasItems(hasEntry("id",d1.getId())));

    }

    @Test
    void removeRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", c2.getId()).pathParam("entityid", d1.getId())
                .delete("/car/remove/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(c2.getId()))
                .body("driverDTOS", empty());
    }

    @Test
    public void getCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/car/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
}