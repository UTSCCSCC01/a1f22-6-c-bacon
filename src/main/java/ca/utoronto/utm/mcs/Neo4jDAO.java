package ca.utoronto.utm.mcs;

import org.neo4j.driver.*;

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
}
