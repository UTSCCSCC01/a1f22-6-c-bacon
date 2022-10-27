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

    private final String uriDb = "bolt://localhost:7687";
    private final String username = "neo4j";
    private final String password = "123456";

    @Inject
    public Neo4jDAO(Driver driver) {
        this.driver = driver;
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

    public boolean addMovie(String name, String movieId) {
        String query;
        query = "MATCH (m:movie { movieId: \"%s\"}) RETURN m.movieId";
        query = String.format(query, movieId);
        Result result = this.session.run(query);
        if(result.hasNext()){
            System.out.println("Movie Found: " + movieId);
            return false;
        }
        query = "CREATE (m:movie {name: \"%s\", movieId: \"%s\"})";
        query = String.format(query, name, movieId);
        this.session.run(query);
        return true;
    }

    public int addRelationship(String actorId, String movieId) {
        String query;
        query = "MATCH (a:actor { actorId: \"%s\"}) RETURN a.actorId";
        query = String.format(query, actorId);
        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("Actor not found: " + actorId);
            return 404;
        }

        query = "MATCH (m:movie { movieId: \"%s\"}) RETURN m.movieId";
        query = String.format(query, movieId);
        result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("Movie not found: " + movieId);
            return 404;
        }

        query = "MATCH (a:actor {actorId: \"%s\"})-[:ACTED_IN]->(m:movie {movieId: \"%s\"}) RETURN a.actorId";
        query = String.format(query, actorId, movieId);
        result = this.session.run(query);
        if(result.hasNext()){
            System.out.println("Relationship Found: " + movieId);
            return 400;
        }

        query = "MATCH (a:actor {actorId: \"%s\"}), (m:movie {movieId: \"%s\"}) CREATE (a)-[r:ACTED_IN]->(m)";
        query = String.format(query, actorId, movieId);
        this.session.run(query);
        return 200;
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

        query = "MATCH (a:actor { actorId: \"%s\"})-[r:ACTED_IN]->(m:movie) RETURN m.movieId";
        query = String.format(query, actorId);
        result = this.session.run(query);
        resultValues = result.list();
        JSONArray movies = new JSONArray();
        resultValues.forEach((record)->{movies.put(record.get("m.movieId").asString());});
        response.put("movies", movies);
        return response.toString();
    }
    public String getMovie(String movieId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;
        query = "MATCH (m:movie { movieId: \"%s\"}) RETURN m.name";
        query = String.format(query, movieId);

        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No movie with this ID");
            return "404";
        }
        List<Record> resultValues = result.list();
        response.put("movieId", movieId);
        response.put("name", resultValues.get(0).get("m.name").asString());

        query = "MATCH (a:actor)-[r:ACTED_IN]->(m:movie { movieId: \"%s\"}) RETURN a.actorId";
        query = String.format(query, movieId);
        result = this.session.run(query);
        resultValues = result.list();
        JSONArray actors = new JSONArray();
        resultValues.forEach((record)->{actors.put(record.get("a.actorId").asString());});
        response.put("actors", actors);
        return response.toString();
    }
    public String hasRelationship(String actorId, String movieId) throws JSONException {
        JSONObject response = new JSONObject();
        String query;

        query = "MATCH (m:movie { movieId: \"%s\"}) RETURN m.name";
        query = String.format(query, movieId);
        Result result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No movie with this ID");
            return "404";
        }

        query = "MATCH (a:actor { actorId: \"%s\"}) RETURN a.name";
        query = String.format(query, actorId);
        result = this.session.run(query);
        if(!result.hasNext()){
            System.out.println("No actor with this ID");
            return "404";
        }

        response.put("movieId", movieId);
        response.put("actorId", actorId);

        query = "MATCH (a:actor  { actorId: \"%s\"})-[r:ACTED_IN]->(m:movie { movieId: \"%s\"}) RETURN r";
        query = String.format(query, actorId,movieId);
        result = this.session.run(query);
        if(!result.hasNext()){
            response.put("hasRelationship", false);
        }else{
            response.put("hasRelationship", true);
        }
        return response.toString();
    }
}
