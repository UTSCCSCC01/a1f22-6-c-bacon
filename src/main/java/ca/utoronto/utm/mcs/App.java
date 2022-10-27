package ca.utoronto.utm.mcs;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App
{
    static int port = 8080;

    public static void main(String[] args) throws IOException
    {
        ReqHandlerComponent component = DaggerReqHandlerComponent.create();
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/api/v1/", component.buildHandler());
        server.start();

        // TODO Create Your Server Context Here, There Should Only Be One Context
        System.out.printf("Server started on port %d\n", port);

        // This code is used to get the neo4j address, you must use this so that we can mark :)
        Dotenv dotenv = Dotenv.load();
        String addr = dotenv.get("NEO4J_ADDR");
        System.out.println(addr);
    }
}
