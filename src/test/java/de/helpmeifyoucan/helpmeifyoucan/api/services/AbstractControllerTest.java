package de.helpmeifyoucan.helpmeifyoucan.api.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import de.helpmeifyoucan.helpmeifyoucan.StaticDbClear;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.LoginResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StaticDbClear clear;
    @Autowired
    protected TestRestTemplate client;
    protected String host;
    protected UriBuilderFactory factory = new DefaultUriBuilderFactory();

    @Before
    public final void setUp() {
        host = "http://localhost:" + port;
        clear.clearDb();
        this.initDB();
    }

    protected abstract void initDB();

    protected ResponseEntity<LoginResponse> login(String email, String password) throws URISyntaxException {
        // var uri = factory.uriString(this.host + "{path}").build("/auth/signin");
        var uri = new URI(this.host + "/auth/signin");
        var credentials = new Credentials(email, password);
        var response = this.post(uri, credentials, LoginResponse.class);
        return response;
    }

    protected String LoginAndGetToken(String email, String password) throws URISyntaxException {
        return this.login(email, password).getBody().getToken();
    }

    protected <T> ResponseEntity<T> post(URI uri, Object body, Class<T> clazz) {
        var request = RequestEntity.post(uri).body(body);
        return this.client.exchange(request, clazz);
    }

    protected <T> ResponseEntity<T> post(URI uri, String token, Object body, Class<T> clazz) {
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        var request = RequestEntity.post(uri).headers(headers).body(body);
        return this.client.exchange(request, clazz);
    }

    protected <T> ResponseEntity<T> get(URI uri, String token, Class<T> clazz) {
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        var request = RequestEntity.get(uri).headers(headers).build();
        return this.client.exchange(request, clazz);
    }

    protected <T> ResponseEntity<T> delete(URI uri, String token, Class<T> clazz) {
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        var request = RequestEntity.delete(uri).headers(headers).build();
        return this.client.exchange(request, clazz);
    }

    protected <T> ResponseEntity<T> patch(URI uri, String token, Object body, Class<T> clazz) {
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        var request = RequestEntity.patch(uri).headers(headers).body(body);
        return this.client.exchange(request, clazz);
    }
}