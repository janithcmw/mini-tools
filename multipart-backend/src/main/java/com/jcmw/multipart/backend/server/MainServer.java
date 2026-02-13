package com.jcmw.multipart.backend.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class MainServer {

    public static void main(String[] args) throws Exception {

        int port = 8085;

        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new MultipartBackendServer()), "/multipart");

        server.setHandler(context);

        server.start();
        System.out.println("Multipart backend started on http://localhost:" + port + "/multipart");

        server.join();
    }
}
