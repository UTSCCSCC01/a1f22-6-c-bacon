package ca.utoronto.utm.mcs;

import dagger.Module;
import dagger.Provides;
import org.neo4j.driver.*;


@Module
public class ReqHandlerModule {
    // TODO Complete This Module
    private final String uriDb = "bolt://localhost:7687";
    private final String username = "neo4j";
    private final String password = "123456";

    @Provides
    public Neo4jDAO provideNeo4jDAO(Driver driver) {
        
		return new Neo4jDAO(driver);
	}    

    @Provides
    public Driver provideDriver(){
        return GraphDatabase.driver(this.uriDb, AuthTokens.basic(this.username, this.password));
    }
}
