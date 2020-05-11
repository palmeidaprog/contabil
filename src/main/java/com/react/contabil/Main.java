package com.react.contabil;

import com.react.contabil.configuracao.ConfiguracaoWebService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final int PORTA = 8080;

    private final static Logger LOGGER =
            LoggerFactory.getLogger(Main.class.getName());


    public static void main(String[] args) throws Exception {
        Server server = new Server(PORTA);
        server.setHandler(Main.getHandler());
        server.start();

        LOGGER.info("main :: Webservice Contabil inicializado com sucesso");
        // espera pela thread principal do servlet ser terminada para finalizar
        server.join();
    }

    private static Handler getHandler() {
        ClassLoader cl = Main.class.getClassLoader();

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.NO_SESSIONS);

        ServletHolder servlet = context.addServlet(
                HttpServletDispatcher.class, "/");
        servlet.setInitParameter("dirAllowed","true");
        servlet.setInitParameter("javax.ws.rs.Application",
                ConfiguracaoWebService.class.getCanonicalName());
        return context;
    }
}
