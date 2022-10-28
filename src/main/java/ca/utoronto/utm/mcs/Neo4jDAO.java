package ca.utoronto.utm.mcs;

import org.json.*;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.List;

import javax.inject.Inject;

// All your database transactions or queries should 
// go in this class
public class Neo4jDAO {
    private final Session session;
    private final Driver driver;

    private final String kevinBid = "nm0000102";

    @Inject
    public Neo4jDAO(Driver driver) {
        this.driver = driver;
        this.session = this.driver.session();
        
    }

    public int addActor(String Name, String id) {
        String query;
        query = "MATCH (a:actor { id: \"%s\"}) RETURN a.id";
        query = String.format(query, id);
        Result result = this.session.run(query);
        if(result.hasNext()){
            System.out.println("Actor Found: " + id);
            return 400;
        }
        query = "CREATE (a:actor {Name: \"%s\", id: \"%s\"})";
        query = String.format(query, Name, id);
        this.session.run(query);
        return 200;
    }

    public int addMovie(String Name, String id) {
        String query;
        query = "MATCH (m:movie { id: \"%s\"}) RETURN m.id";
        query = String.format(query, id);
        Result result = this.session.run(query);
        if(result.hasNext()){
            System.out.println("Movie Found: " + id);
            return 400;
        }
        query = "CREATE (m:movie {Name: \"%s\", id: \"%s\"})";
        query = String.format(query, Name, id);
        this.session.run(query);
        return 200;
    }

    public int addRelationship(String actorId, String movieId) {
        String query;
        query = "MATCH (a:actor { id: \"%s\"}) RETURN a.id";
        query = String.format(query, actorId);
        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("Actor not found: " + actorId);
            return 404;
        }

        query = "MATCH (m:movie { id: \"%s\"}) RETURN m.id";
        query = String.format(query, movieId);
        result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("Movie not found: " + movieId);
            return 404;
        }

        query = "MATCH (a:actor {id: \"%s\"})-[:ACTED_IN]->(m:movie {id: \"%s\"}) RETURN a.id";
        query = String.format(query, actorId, movieId);
        result = this.session.run(query);
        if(result.hasNext()){
            System.out.println("Relationship Found: " + actorId +"->"+ movieId);
            return 400;
        }

        query = "MATCH (a:actor {id: \"%s\"}), (m:movie {id: \"%s\"}) CREATE (a)-[r:ACTED_IN]->(m)";
        query = String.format(query, actorId, movieId);
        this.session.run(query);
        return 200;
    }

    public String getActor(String id) throws JSONException {
        JSONObject response = new JSONObject();
        String query;
        query = "MATCH (a:actor { id: \"%s\"}) RETURN a.id";
        query = String.format(query, id);

        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No actor with this ID");
            return "404";
        }
        List<Record> resultValues = result.list();
        response.put("id", id);
        response.put("Name", resultValues.get(0).get("a.Name").asString());

        query = "MATCH (a:actor { id: \"%s\"})-[r:ACTED_IN]->(m:movie) RETURN m.id";//maybe change Movie to lowercase
        query = String.format(query, id);
        result = this.session.run(query);
        resultValues = result.list();
        JSONArray movies = new JSONArray();
        resultValues.forEach((record)->{movies.put(record.get("m.id").asString());});
        //movies.put(record.get("m.id").asString());
        response.put("movies", movies);
        return response.toString();
    }

    public String getMovie(String movieId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;
        query = "MATCH (m:movie { id: \"%s\"}) RETURN m.Name";
        query = String.format(query, movieId);

        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No movie with this ID");
            return "404";
        }
        List<Record> resultValues = result.list();
        response.put("movieId", movieId);
        response.put("name", resultValues.get(0).get("m.Name").asString());

        query = "MATCH (a:actor)-[r:ACTED_IN]->(m:movie { id: \"%s\"}) RETURN a.id";
        query = String.format(query, movieId);
        result = this.session.run(query);
        resultValues = result.list();
        JSONArray actors = new JSONArray();
        resultValues.forEach((record)->{actors.put(record.get("a.id").asString());});
        response.put("actors", actors);
        return response.toString();
    }
    public String hasRelationship(String actorId, String movieId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;

        query = "MATCH (m:movie { id: \"%s\"}) RETURN m.Name";
        query = String.format(query, movieId);
        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No movie with this ID");
            return "404";
        }

        query = "MATCH (a:actor { id: \"%s\"}) RETURN a.Name";
        query = String.format(query, actorId);
        result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No actor with this ID");
            return "404";
        }

        response.put("movieId", movieId);
        response.put("actorId", actorId);

        query = "MATCH (a:actor  { id: \"%s\"})-[r:ACTED_IN]->(m:movie { id: \"%s\"}) RETURN r";
        query = String.format(query, actorId,movieId);
        result = this.session.run(query);
        if(!result.hasNext()){
            response.put("hasRelationship", false);
        }else{
            response.put("hasRelationship", true);
        }
        return response.toString();
    }

    public String computeBaconNumber(String actorId) throws JSONException{
        JSONObject response = new JSONObject();
        String query;
        int baconNumber = 0;
        
        if(actorId.equals(kevinBid)){
            response.put("baconNumber", 0);
            return response.toString();
        }
        query = "MATCH (a:actor {id: \"%s\"}), (KevinB:actor {id: \"%s\"} ), p = shortestPath((a)-[:ACTED_IN*]-(KevinB)) RETURN nodes(p)";
        query = String.format(query, actorId, kevinBid);
        
        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No path");
            return "404";
        }
        Record record = result.next();
        for(int i=0; i<record.get(0).size(); i++){
            if(record.get(0).get(i).asNode().hasLabel("actor")){
                baconNumber++;
            }
        }
        response.put("baconPath", baconNumber);
        return response.toString();
    }

    public String computeBaconPath(String actorId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;
        JSONArray path = new JSONArray();

        if(actorId.equals(kevinBid)){
            path.put(kevinBid);
            response.put("baconPath", kevinBid);
            return response.toString();
        }
        query = "MATCH (a:actor {id: \"%s\"}), (KevinB:actor {id: \"%s\"} ), p = shortestPath((a)-[:ACTED_IN*]-(KevinB)) RETURN nodes(p)";
        query = String.format(query, actorId, kevinBid);
        
        Result result = this.session.run(query);
        System.out.println(result);
        if(!result.hasNext()){
            System.out.println("No path");
            return "404";
        }
        Record record = result.next();
        for(int i=0; i<record.get(0).size(); i++){
            path.put(record.get(0).get(i).get("id").asString());
        }
        response.put("baconPath", path);
        return response.toString();
    }

    public void deleteMovie(String movieId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;

        query = "MATCH (m:movie { id: \"%s\"}) DETACH DELETE m";
        query = String.format(query, movieId);
        Result result = this.session.run(query);
    }

    public void deleteActor(String actorId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;
        query = "MATCH (a:actor { id: \"%s\"}) DETACH DELETE a";
        query = String.format(query, actorId);
        Result result = this.session.run(query);
    }
}
