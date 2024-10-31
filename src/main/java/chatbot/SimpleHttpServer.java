package chatbot;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class SimpleHttpServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Создаем ServletHolder с новым экземпляром ChatServlet
        context.addServlet(new ServletHolder(new ChatServlet()), "/api/chat");

        server.start();
        System.out.println("The server is running on port 8080. Waiting for requests...");
        server.join();
    }
}
