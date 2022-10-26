package ca.utoronto.utm.mcs;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


// TODO Please Write Your Tests For CI/CD In This Class. You will see
// these tests pass/fail on github under github actions.
public class AppTest {

    final static String API_URL = "http://localhost:8080";

    private static HttpResponse<String> sendRequest(String endpoint, String method, String reqBody) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + endpoint))
                .method(method, HttpRequest.BodyPublishers.ofString(reqBody))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    @Test
    public void exampleTest() {
        assertTrue(true);
    }

    // @Test
    // public void addActorPass() throws JSONException, IOException, InterruptedException {
    //     JSONObject confirmReq = new JSONObject()
    //             .put("name", "TestActor")
    //             .put("actorId", "12345678901");
    //     HttpResponse<String> confirmRes = sendRequest("/api/v1/addActor", "PUT", confirmReq.toString());
    //     assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    // }

    @Test
    public void addActorFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "TestActor");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addActor", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    // @Test
    // public void addMoviePass() throws JSONException, IOException, InterruptedException {
    //     JSONObject confirmReq = new JSONObject()
    //         .put("name", "TestMovie")
    //         .put("movieId", "12345678901");
    //     HttpResponse<String> confirmRes = sendRequest("/api/v1/addMovie", "PUT", confirmReq.toString());
    //     assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    // }

    @Test
    public void addMovieFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
            .put("name", "TestMovie");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addMovie", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void addRelationshipPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
            .put("movieId", "x");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }
}
