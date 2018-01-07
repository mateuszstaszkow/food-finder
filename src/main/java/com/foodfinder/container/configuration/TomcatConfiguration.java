package com.foodfinder.container.configuration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration {

    @Value("${food-finder.security-user-constraint}")
    private String securityUserConstraint;

    @Value("${food-finder.security-pattern}")
    private String securityPattern;

    @Value("${food-finder.http-connector}")
    private String httpConnector;

    @Value("${food-finder.http-scheme}")
    private String httpScheme;

    @Value("${food-finder.http-port}")
    private Integer httpPort;

    @Value("${food-finder.http-secure}")
    private Boolean httpSecure;

    @Value("${server.port}")
    private Integer httpsPort;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint(securityUserConstraint);
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern(securityPattern);
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {
        Connector connector = new Connector(httpConnector);
        connector.setScheme(httpScheme);
        connector.setPort(httpPort);
        connector.setSecure(httpSecure);
        connector.setRedirectPort(httpsPort);

        return connector;
    }
}
