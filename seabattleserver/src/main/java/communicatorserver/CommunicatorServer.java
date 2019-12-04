package communicatorserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;


public class CommunicatorServer {
    public static void main(String[] args){
        startWebSocketServer();
    }

    public static void startWebSocketServer(){
        Server webSocketServer = new Server();
        ServerConnector connector = new ServerConnector(webSocketServer);
        connector.setPort(8095);
        webSocketServer.addConnector(connector);

        ServletContextHandler webSocketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);

        try{
            ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(webSocketContext);
            wsContainer.addEndpoint(CommunicatorServerWebSocket.class);
            webSocketServer.start();
            webSocketServer.join();
        }catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
