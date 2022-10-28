package ca.utoronto.utm.mcs;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Test
    public void addActorPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "TestActor")
                .put("actorId", "12345678901");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addActor", "PUT", confirmReq.toString());
        sendRequest("/api/v1/deleteActor", "DELETE", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void addActorFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "TestActor");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addActor", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void addMoviePass() throws JSONException, IOException, InterruptedException {
        
        JSONObject confirmReq = new JSONObject()
            .put("name", "TestMovie")
            .put("movieId", "12345678901");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addMovie", "PUT", confirmReq.toString());
        sendRequest("/api/v1/deleteMovie", "DELETE", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

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
                .put("name", "TestActor")
                .put("actorId", "123456789011");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addActor", "PUT", confirmReq.toString());
        confirmReq = new JSONObject()
            .put("name", "TestMovie")
            .put("movieId", "123456789012");
        confirmRes = sendRequest("/api/v1/addMovie", "PUT", confirmReq.toString());
        confirmReq = new JSONObject()
            .put("actorId", "123456789011")
            .put("movieId", "123456789012");
        confirmRes = sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());
        sendRequest("/api/v1/deleteActor", "DELETE", confirmReq.toString());
        sendRequest("/api/v1/deleteMovie", "DELETE", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void addRelationshipFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
            .put("movieId", "123123");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void getActorPass() throws JSONException, IOException, InterruptedException {
        JSONObject setupReq = new JSONObject()
                     .put("name", "TestActor")
                     .put("actorId", "12345678901");
        sendRequest("/api/v1/addActor", "PUT", setupReq.toString());

        JSONObject confirmReq = new JSONObject()
                .put("actorId", "12345678901");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getActor", "GET", confirmReq.toString());
        
        sendRequest("/api/v1/deleteActor", "DELETE", setupReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void getActorFail() throws JSONException, IOException, InterruptedException {
        JSONObject setupReq = new JSONObject()
                     .put("name", "TestActor")
                     .put("actorId", "12345678901");
        sendRequest("/api/v1/addActor", "PUT", setupReq.toString());

        JSONObject confirmReq = new JSONObject()
                .put("name", "TestActor");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getActor", "GET", confirmReq.toString());

        sendRequest("/api/v1/deleteActor", "DELETE", setupReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void getMoviePass() throws JSONException, IOException, InterruptedException {
        JSONObject setupReq = new JSONObject()
                 .put("name", "TestMovie")
                 .put("movieId", "12345678901");
        HttpResponse<String> setupRes = sendRequest("/api/v1/addMovie", "PUT", setupReq.toString());

        JSONObject confirmReq = new JSONObject()
                .put("movieId", "12345678901");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getMovie", "GET", confirmReq.toString());

        JSONObject deleteReq = new JSONObject()
                .put("movieId", "12345678901");
        HttpResponse<String> deleteRes = sendRequest("/api/v1/deleteMovie", "DELETE", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void getMovieFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "TestMovie");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getMovie", "GET", confirmReq.toString());

        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void hasRelationshipPass() throws JSONException, IOException, InterruptedException {
        JSONObject setupReq = new JSONObject()
                .put("name", "TestMovie")
                .put("movieId", "12345678901");
        HttpResponse<String> setupRes = sendRequest("/api/v1/addMovie", "PUT", setupReq.toString());

       setupReq = new JSONObject()
                .put("name", "TestActor")
                .put("actorId", "12345678902");
       setupRes = sendRequest("/api/v1/addActor", "PUT", setupReq.toString());

        setupReq = new JSONObject()
                .put("movieId", "12345678901")
                .put("actorId", "12345678902");
        setupRes = sendRequest("/api/v1/addRelationship", "PUT", setupReq.toString());

        JSONObject confirmReq = new JSONObject()
                .put("movieId", "12345678901")
                .put("actorId", "12345678902");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/hasRelationship", "GET", confirmReq.toString());

        JSONObject deleteReq = new JSONObject()
                .put("movieId", "12345678901");
        HttpResponse<String> deleteRes = sendRequest("/api/v1/deleteMovie", "DELETE", confirmReq.toString());
        deleteReq = new JSONObject()
                .put("actorId", "12345678902");
        deleteRes = sendRequest("/api/v1/deleteActor", "DELETE", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void hasRelationshipFail() throws JSONException, IOException, InterruptedException {
        JSONObject setupReq1 = new JSONObject()
                .put("name", "TestMovie")
                .put("movieId", "12345678901");
        sendRequest("/api/v1/addMovie", "PUT", setupReq1.toString());

        JSONObject setupReq2 = new JSONObject()
                .put("name", "TestActor")
                .put("actorId", "12345678902");
        sendRequest("/api/v1/addActor", "PUT", setupReq2.toString());

        JSONObject setupReq3 = new JSONObject()
                .put("movieId", "12345678901")
                .put("actorId", "12345678902");
        sendRequest("/api/v1/addRelationship", "PUT", setupReq3.toString());

        JSONObject confirmReq = new JSONObject()
                .put("movieId", "12345678901");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/hasRelationship", "GET", confirmReq.toString());

        sendRequest("/api/v1/deleteMovie", "DELETE", setupReq1.toString());
        sendRequest("/api/v1/deleteActor", "DELETE", setupReq2.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void computeBaconNumberPass() throws JSONException, IOException, InterruptedException {
        boolean deleteKB = false;
        JSONObject setupReq1 = new JSONObject()
                .put("name", "Kevin Bacon")
                .put("actorId", "nm0000102");
        HttpResponse<String> setupRes = sendRequest("/api/v1/addActor", "PUT", setupReq1.toString());
        
        if(setupRes.statusCode() == 200){
            deleteKB = true;
        }

        JSONObject setupReq2 = new JSONObject()
                .put("name", "TestActor")
                .put("actorId", "12345678902");
        sendRequest("/api/v1/addActor", "PUT", setupReq2.toString());

        JSONObject setupReq3 = new JSONObject()
                .put("name", "TestMovie")
                .put("movieId", "12345678901");
        sendRequest("/api/v1/addMovie", "PUT", setupReq3.toString());

        JSONObject confirmReq = new JSONObject()
            .put("actorId", "12345678902")
            .put("movieId", "12345678901");
        sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());

        confirmReq = new JSONObject()
            .put("actorId", "nm0000102")
            .put("movieId", "12345678901");
        sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());

        confirmReq = new JSONObject()
            .put("actorId", "12345678902");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconNumber", "GET", confirmReq.toString());

        if(deleteKB){
            sendRequest("/api/v1/deleteActor", "DELETE", setupReq1.toString());
        }
        sendRequest("/api/v1/deleteActor", "DELETE", setupReq2.toString());
        sendRequest("/api/v1/deleteMovie", "DELETE", setupReq3.toString());

        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void computeBaconNumberFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
            .put("name", "Kevin Bacon");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconNumber", "GET", confirmReq.toString());

        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void computeBaconPathPass() throws JSONException, IOException, InterruptedException {
        boolean deleteKB = false;
        JSONObject setupReq1 = new JSONObject()
                .put("name", "Kevin Bacon")
                .put("actorId", "nm0000102");
        HttpResponse<String> setupRes = sendRequest("/api/v1/addActor", "PUT", setupReq1.toString());
        
        if(setupRes.statusCode() == 200){
            deleteKB = true;
        }

        JSONObject setupReq2 = new JSONObject()
                .put("name", "TestActor")
                .put("actorId", "12345678902");
        sendRequest("/api/v1/addActor", "PUT", setupReq2.toString());

        JSONObject setupReq3 = new JSONObject()
                .put("name", "TestMovie")
                .put("movieId", "12345678901");
        sendRequest("/api/v1/addMovie", "PUT", setupReq3.toString());

        JSONObject confirmReq = new JSONObject()
            .put("actorId", "12345678902")
            .put("movieId", "12345678901");
        sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());

        confirmReq = new JSONObject()
            .put("actorId", "nm0000102")
            .put("movieId", "12345678901");
        sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());

        confirmReq = new JSONObject()
            .put("actorId", "12345678902");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconPath", "GET", confirmReq.toString());

        if(deleteKB){
            sendRequest("/api/v1/deleteActor", "DELETE", setupReq1.toString());
        }
        sendRequest("/api/v1/deleteActor", "DELETE", setupReq2.toString());
        sendRequest("/api/v1/deleteMovie", "DELETE", setupReq3.toString());

        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void computeBaconPathFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
            .put("name", "Kevin Bacon");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconPath", "GET", confirmReq.toString());

        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }
}
