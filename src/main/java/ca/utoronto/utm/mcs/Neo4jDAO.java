package ca.utoronto.utm.mcs;

import org.json.*;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.List;

// All your database transactions or queries should 
// go in this class
public class Neo4jDAO {
    private final Session session;
    private final Driver driver;

    private final String uriDb = "bolt://localhost:7687";
    private final String username = "neo4j";
    private final String password = "123456";


    public Neo4jDAO() {
        this.driver = GraphDatabase.driver(this.uriDb, AuthTokens.basic(this.username, this.password));
        this.session = this.driver.session();
        
    }

    public boolean addActor(String name, String actorId) {
        String query;
        query = "MATCH (a:actor { actorId: \"%s\"}) RETURN a.actorId";
        query = String.format(query, actorId);
        Result result = this.session.run(query);
        if(result.hasNext()){
            System.out.println("Actor Found: " + actorId);
            return false;
        }
        query = "CREATE (a:actor {name: \"%s\", actorId: \"%s\"})";
        query = String.format(query, name, actorId);
        this.session.run(query);
        return true;
    }

    public String getActor(String actorId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;
        query = "MATCH (a:actor { actorId: \"%s\"}) RETURN a.name";
        query = String.format(query, actorId);

        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No actor with this ID");
            return "404";
        }
        List<Record> resultValues = result.list();
        response.put("actorId", actorId);
        response.put("name", resultValues.get(0).get("a.name").asString());

        query = "MATCH (a:actor { actorId: \"%s\"})-[r:ACTED_IN]->(m:movie) RETURN m.movieId";//maybe change Movie to lowercase
        query = String.format(query, actorId);
        result = this.session.run(query);
        resultValues = result.list();
        JSONArray movies = new JSONArray();
        resultValues.forEach((record)->{movies.put(record.get("m.movieId").asString());});
        response.put("movies", movies);
        return response.toString();
    }
}
