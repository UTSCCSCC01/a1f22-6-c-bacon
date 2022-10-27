package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;

import javax.inject.Inject;

public class Server {
    // TODO Complete This Class
    private HttpServer httpServer;
    @Inject
    public Server(HttpServer httpServer){
        this.httpServer = httpServer;
    }
    public void createContext(String path, ReqHandler reqHandler){
        this.httpServer.createContext(path, reqHandler);
    }
    public void startServer(){
        this.httpServer.start();
    }
}
