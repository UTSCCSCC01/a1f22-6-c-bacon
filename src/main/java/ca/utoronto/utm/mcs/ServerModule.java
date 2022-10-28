package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;
import dagger.Module;
import dagger.Provides;

import java.io.IOException;
import java.net.InetSocketAddress;

@Module
public class ServerModule {
    // TODO Complete This Module
    @Provides
    public InetSocketAddress provideInetSocketAddress(){
        return new InetSocketAddress("0.0.0.0", 8080);
    }
    @Provides
    public HttpServer provideHttpServer(InetSocketAddress inetSocketAddress) {
        try {
            HttpServer server = HttpServer.create(inetSocketAddress, 0);

            return server;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    public Server provideServer(HttpServer httpServer){
        return new Server(httpServer);
    }
}
