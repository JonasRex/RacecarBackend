package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.Role;
import entities.User;
import entities.RenameMe;
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

class UserResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User u1, u2, u3;
    private static Role userRole;
    private static RenameMe r1;

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
        em.createNamedQuery("User.deleteAllRows").executeUpdate();
        em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
        em.createNamedQuery("Role.deleteAllRows").executeUpdate();

        u1 = new User("AnnaAnna", "test","Anna", "Andersen", "aa@mail.com");
        u2 = new User("BomberBo", "test","Bo", "Berthelsen", "bb@mail.com");
        r1 = new RenameMe("First", "First");
        u2.addRenameMe(r1);


            userRole = new Role("user");

            u1.addRole(userRole);
            u2.addRole(userRole);

            em.persist(userRole);

            em.persist(u1);
            em.persist(u2);
            em.persist(r1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/user").then().statusCode(200);
    }

    @Test
    void getAll() {
        List<UserDTO> userDTOS;

        userDTOS = given()
                .contentType("application/json")
                .when()
                .get("/user")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("", UserDTO.class);


        assertEquals(userDTOS.size(), 2);
    }

    @Test
    void getById() {
        given()
                .contentType("application/json")
                .get("/user/{id}", u1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo(u1.getFirstName()))
                .body("lastName", equalTo(u1.getLastName()))
                .body("email", equalTo(u1.getEmail()));
    }

    @Test
    public void testFailByID() {
        System.out.println("================================================================");
        System.out.println("OBS: Test failing on purpose, when finding entity by invalid ID: ");
        System.out.println("================================================================");

        given()
                .contentType("application/json")
                .get("/user/99999")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("User with ID: 99999 was not found"));
    }


    @Test
    void create() {
        u3 = new User("Charlie", "test","Charlie", "Cameron", "cc@mail.com");
        String requestBody = GSON.toJson(new UserDTO(u3));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/user")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo(u3.getFirstName()))
                .body("lastName", equalTo(u3.getLastName()))
                .body("email", equalTo(u3.getEmail()));
    }

    @Test
    void update() {
        UserDTO userDTO = new UserDTO(u1);
        userDTO.setFirstName("Lone");
        String requestBody = GSON.toJson(userDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/user/"+u1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(u1.getId()))
                .body("firstName", equalTo("Lone"));
    }

    @Test
    void delete() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", u1.getId())
                .delete("/user/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(u1.getId()));
    }

    @Test
    void addRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", u1.getId()).pathParam("entityid", r1.getId())
                .put("/user/add/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(u1.getId()))
                .body("renameMeDTOS", hasItems(hasEntry("id",r1.getId())));

    }

    @Test
    void removeRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", u2.getId()).pathParam("entityid", r1.getId())
                .delete("/user/remove/{id}/{entityid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(u2.getId()))
                .body("renameMeDTOS", empty());
    }

    @Test
    public void getCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/user/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
}