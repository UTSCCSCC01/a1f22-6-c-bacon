package ca.utoronto.utm.mcs;

import java.io.IOException;
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
                    break;
                case "POST":
                    switch (request) {
                        case "addActor":
                            this.addActor(exchange);
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
}