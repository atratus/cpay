/**
 *
 */
package trsit.cpay.service;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;

import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.annotations.AbstractDiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.AnnotationDecorator;
import org.eclipse.jetty.annotations.AnnotationParser;
import org.eclipse.jetty.annotations.AnnotationParser.DiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.ClassNameResolver;
import org.eclipse.jetty.annotations.WebFilterAnnotationHandler;
import org.eclipse.jetty.annotations.WebListenerAnnotationHandler;
import org.eclipse.jetty.annotations.WebServletAnnotationHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import trsit.cpay.security.SecurityConfigurer;
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
        final WebAppContext jettyCtx = new WebAppContext();
        jettyCtx.setContextPath("/cpay");
        jettyCtx.setResourceBase(".");

        final Configuration[] configs = new Configuration[] { new AnnotationConfiguration() {
            @Override
            public void configure(final WebAppContext context) throws Exception {
                final boolean metadataComplete = context.getMetaData().isMetaDataComplete();
                context.addDecorator(new AnnotationDecorator(context));
                // Even if metadata is complete, we still need to scan for
                // ServletContainerInitializers - if there are any
                AnnotationParser parser = null;
                if (!metadataComplete) {
                    // If metadata isn't complete, if this is a servlet 3 webapp
                    // or isConfigDiscovered is true, we need to search for
                    // annotations
                    if (context.getServletContext().getEffectiveMajorVersion() >= 3
                            || context.isConfigurationDiscovered()) {
                        _discoverableAnnotationHandlers.add(new WebServletAnnotationHandler(context));
                        _discoverableAnnotationHandlers.add(new WebFilterAnnotationHandler(context));
                        _discoverableAnnotationHandlers.add(new WebListenerAnnotationHandler(context));
                    }
                }
                // Regardless of metadata, if there are any
                // ServletContainerInitializers with @HandlesTypes, then we need
                // to scan all the
                // classes so we can call their onStartup() methods correctly
                createServletContainerInitializerAnnotationHandlers(context, getNonExcludedInitializers(context));
                if (!_discoverableAnnotationHandlers.isEmpty() || _classInheritanceHandler != null
                        || !_containerInitializerAnnotationHandlers.isEmpty()) {
                    parser = new AnnotationParser() {
                        @Override
                        public void parse(final Resource aDir, final ClassNameResolver aResolver) throws Exception {
                            if (aDir.isDirectory())
                                super.parse(aDir, aResolver);
                            else
                                super.parse(aDir.getURI(), aResolver);
                        }
                    };
                    parse(context, parser);
                    for (final DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers)
                        context.getMetaData().addDiscoveredAnnotations(
                                ((AbstractDiscoverableAnnotationHandler) h).getAnnotationList());
                }
            }

            private void parse(final WebAppContext context, final AnnotationParser parser) throws Exception {
                final List<Resource> _resources = getResources(getClass().getClassLoader());
                for (final Resource _resource : _resources) {
                    if (_resource == null)
                        return;
                    parser.clearHandlers();
                    for (final DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers) {
                        if (h instanceof AbstractDiscoverableAnnotationHandler)
                            ((AbstractDiscoverableAnnotationHandler) h).setResource(null); //
                    }
                    parser.registerHandlers(_discoverableAnnotationHandlers);
                    parser.registerHandler(_classInheritanceHandler);
                    parser.registerHandlers(_containerInitializerAnnotationHandlers);
                    parser.parse(_resource, new ClassNameResolver() {
                        @Override
                        public boolean isExcluded(final String name) {
                            if (context.isSystemClass(name))
                                return true;
                            if (context.isServerClass(name))
                                return false;
                            return false;
                        }

                        @Override
                        public boolean shouldOverride(final String name) {
                            // looking at webapp classpath, found already-parsed
                            // class of same name - did it come from system or
                            // duplicate in webapp?
                            if (context.isParentLoaderPriority())
                                return false;
                            return true;
                        }
                    });
                }
            }

            private List<Resource> getResources(final ClassLoader aLoader) throws IOException {
                if (aLoader instanceof URLClassLoader) {
                    final List<Resource> _result = new ArrayList<Resource>();
                    final URL[] _urls = ((URLClassLoader) aLoader).getURLs();
                    for (final URL _url : _urls)
                        _result.add(Resource.newResource(_url));
                    return _result;
                }
                return Collections.emptyList();
            }
        } };
        jettyCtx.setConfigurations(configs);
        jettyCtx.addServlet(DefaultServlet.class, "/*");


        jettyCtx.addEventListener(new ContextLoaderListener() {

            @Override
            protected ApplicationContext loadParentContext(final ServletContext servletContext) {
                return appCtx;
            }

        });
        jettyCtx.setInitParameter("contextConfigLocation", SecurityConfigurer.class.getName());
        jettyCtx.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());

        final FilterHolder wicketFilterHolder =
                jettyCtx.addFilter(WicketFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        wicketFilterHolder.setInitParameter("applicationClassName", CPayApplication.class.getName());
        wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");

        server.setHandler(jettyCtx);

        server.start();

        log.info("Context constructed");
    }

    private static int getPort() {
        return Integer.parseInt(System.getProperty("cpay.port", "8081"));
    }

}
