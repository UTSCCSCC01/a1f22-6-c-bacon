package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.*;

public class ReqHandler implements HttpHandler {

    // TODO Complete This Class

    public Neo4jDAO dao;

    public ReqHandler() {
        this.dao = new Neo4jDAO();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
        String request = requestURI.toString().split("/")[3];
        System.out.println(request);
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    switch (request) {
                        case "getActor":
                            this.getActor(exchange);
                            break;
                        default:
                            break;
                    }
                    break;
                case "PUT":
                    switch (request) {
                        case "addActor":
                            this.addActor(exchange);
                            break;
                        case "addMovie":
                            this.addMovie(exchange);
                            break;
                        case "addRelationship":
                            this.addRelationship(exchange);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addActor(HttpExchange r) throws IOException, JSONException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String name, actorId;

            if (deserialized.length() == 2 && deserialized.has("name") && deserialized.has("actorId")) {
                name = deserialized.getString("name");
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                if(this.dao.addActor(name, actorId) == false){
                    r.sendResponseHeaders(400, -1);
                    return;
                }
            } catch (Exception e) {
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
            r.sendResponseHeaders(200, -1);
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void addMovie(HttpExchange r) throws IOException, JSONException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String name, movieId;

            if (deserialized.length() == 2 && deserialized.has("name") && deserialized.has("movieId")) {
                name = deserialized.getString("name");
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                if(this.dao.addMovie(name, movieId) == false){
                    r.sendResponseHeaders(400, -1);
                    return;
                }
            } catch (Exception e) {
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
            r.sendResponseHeaders(200, -1);
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void addRelationship(HttpExchange r) throws IOException, JSONException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String actorId, movieId;

            if (deserialized.length() == 2 && deserialized.has("actorId") && deserialized.has("movieId")) {
                actorId = deserialized.getString("actorId");
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                int rCode = this.dao.addRelationship(actorId, movieId);
                r.sendResponseHeaders(rCode, -1);
                return;
            } catch (Exception e) {
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void getActor(HttpExchange r) throws IOException, JSONException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);
            String actorId;

            if (deserialized.has("actorId")) {
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }
            String response = this.dao.getActor(actorId);
            try {
                if(response.equals("404")){
                    r.sendResponseHeaders(404, -1);
                    return;
                }
            } catch (Exception e) {
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
            r.sendResponseHeaders(200, response.length());
            OutputStream os = r.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }
}