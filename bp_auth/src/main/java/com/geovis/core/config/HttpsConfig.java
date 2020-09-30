package com.geovis.core.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfig {
    /**
     * it's for set http url auto change to https
     */
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer(){
//        TomcatEmbeddedServletContainerFactory tomcat=new TomcatEmbeddedServletContainerFactory(){
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint=new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");//confidential
//                SecurityCollection collection=new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//    }
//
//    @Bean
//    public Connector httpConnector(){
//        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8989);
//        connector.setSecure(false);
//        connector.setRedirectPort(443);
//        return connector;
//    }
}
