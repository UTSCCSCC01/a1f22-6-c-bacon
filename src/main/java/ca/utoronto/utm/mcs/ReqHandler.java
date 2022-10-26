package ca.utoronto.utm.mcs;

import java.io.IOException;
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
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    //this.handleGet(exchange);
                    break;
                case "POST":
                    this.handlePost(exchange);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlePost(HttpExchange r) throws IOException, JSONException {
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
                this.dao.addActor(name, actorId);
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