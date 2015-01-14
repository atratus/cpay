/**
 *
 */
package trsit.cpay;

import java.util.EnumSet;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.DispatcherType;

import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import trsit.cpay.web.CPayApplication;

/**
 * @author black
 */
@Service
public class ServerManager {
    @Inject
    private ApplicationContext appCtx;

    private final static Logger log = LoggerFactory.getLogger(ServerManager.class);

    @PostConstruct
    private void init() throws Exception {
        CPayApplication.SPRING_CTX = appCtx;
        final Server server = new Server(getPort());
        final WebAppContext appCtx = new WebAppContext();
        appCtx.setContextPath("/cpay");
        appCtx.setResourceBase(".");

        //        appCtx.addEventListener(new ContextLoaderListener());
        //        appCtx.setInitParameter("contextConfigLocation", "classpath*:**/context.xml");

        final FilterHolder wicketFilterHolder = appCtx.addFilter(WicketFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        wicketFilterHolder.setInitParameter("applicationClassName", CPayApplication.class.getName());
        wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
        server.setHandler(appCtx);

        server.start();

        log.info("Context constructed");
    }

    private static int getPort() {
        return Integer.parseInt(System.getProperty("cpay.port", "8081"));
    }

}
