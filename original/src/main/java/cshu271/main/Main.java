
package cshu271.main;

import cshu271.datastore.DefaultData;

import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


public class Main {

    private static int getPort(int defaultPort) {
        //grab port from environment, otherwise fall back to default port 9998
        String httpPort = System.getProperty("jersey.test.port");
        if (null != httpPort) {
            try {
                return Integer.parseInt(httpPort);
            } catch (NumberFormatException e) {
            }
        }
        return defaultPort;
    }
	
	private static URI getBaseURI()
	{
		return UriBuilder.fromUri("http://0.0.0.0/").port(getPort(9998)).build();
	}


    public static final URI BASE_URI = getBaseURI();
	

    
    public static HttpServer startServer() throws IOException {
        ResourceConfig resourceConfig = new ResourceConfig().packages("cshu271.resource");

        System.out.println("Starting grizzly2...");
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
    }
    
    public static void main(String[] args) throws IOException {
		
		DefaultData.populateUsers();
		DefaultData.populateArticles();
		DefaultData.populateComments();
		DefaultData.populateVotes();
		
        // Grizzly 2 initialization
        HttpServer httpServer = startServer();
		CLStaticHttpHandler staticHttpHandler = new CLStaticHttpHandler(Main.class.getClassLoader());
		httpServer.getServerConfiguration().addHttpHandler(staticHttpHandler, "/web");
		//httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("D:\\Projects\\bsu1\\src\\content\\"),"/web");   
        System.out.println("Main page available at http://127.0.0.1:9998/web/index.html");                
        System.in.read();
        httpServer.stop();
    }    
}
