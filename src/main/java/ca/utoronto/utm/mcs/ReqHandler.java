package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.inject.Inject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.*;

public class ReqHandler implements HttpHandler {

    // TODO Complete This Class

    private Neo4jDAO dao;

    @Inject
    public ReqHandler(Neo4jDAO dao) {
        this.dao = dao;
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
                        case "getMovie":
                            this.getMovie(exchange);
                            break;
                        case "hasRelationship":
                            this.hasRelationship(exchange);
                            break;
                        case "computeBaconPath":
                            this.computeBaconPath(exchange);
                            break;
                        case "computeBaconNumber":
                            this.computeBaconNumber(exchange);
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
                case "DELETE":
                    switch (request) {
                        case "deleteActor":
                            this.deleteActor(exchange);
                            break;
                        case "deleteMovie":
                            this.deleteMovie(exchange);
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

            if (deserialized.has("name") && deserialized.has("actorId")) {
                name = deserialized.getString("name");
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                r.sendResponseHeaders(this.dao.addActor(name, actorId), -1);
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

    public void addMovie(HttpExchange r) throws IOException, JSONException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String name, movieId;

            if (deserialized.has("name") && deserialized.has("movieId")) {
                name = deserialized.getString("name");
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                r.sendResponseHeaders(this.dao.addMovie(name, movieId), -1);
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

    public void addRelationship(HttpExchange r) throws IOException, JSONException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String actorId, movieId;

            if (deserialized.has("actorId") && deserialized.has("movieId")) {
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

    public void getActor(HttpExchange r) throws IOException {
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

    public void getMovie(HttpExchange r) throws IOException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);
            String movieId;

            if (deserialized.has("movieId")) {
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }
            String response = this.dao.getMovie(movieId);
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
    public void hasRelationship(HttpExchange r) throws IOException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);
            String movieId, actorId;

            if (deserialized.has("actorId") && deserialized.has("movieId")) {
                movieId = deserialized.getString("movieId");
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }
            String response = this.dao.hasRelationship(actorId, movieId);
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

    public void computeBaconNumber(HttpExchange r) throws IOException, JSONException{
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
            String response = this.dao.computeBaconNumber(actorId);
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

    public void computeBaconPath(HttpExchange r) throws IOException, JSONException{
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
            String response = this.dao.computeBaconPath(actorId);
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
    public void deleteActor(HttpExchange r) throws IOException {
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
            this.dao.deleteActor(actorId);
            r.sendResponseHeaders(200, -1);
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }
    
    public void deleteMovie(HttpExchange r) throws IOException {
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);
            String movieId;

            if (deserialized.has("movieId")) {
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }
            this.dao.deleteMovie(movieId);
            r.sendResponseHeaders(200, -1);
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }
}