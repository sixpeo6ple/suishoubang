//package com.xyy.config;
//
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author WhiteRunner
// * @create 2021-11-12 21:34
// */
//@Configuration
////实现http到https的重定向
//public class HttpsConfig {
//    @Bean
//    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector) {
//        TomcatServletWebServerFactory tomcat =
//                new TomcatServletWebServerFactory() {
//                    @Override
//                    protected void postProcessContext(Context context) {
//                        SecurityConstraint securityConstraint = new SecurityConstraint();
//                        securityConstraint.setUserConstraint("CONFIDENTIAL");
//                        SecurityCollection collection = new SecurityCollection();
//                        collection.addPattern("/*");
//                        securityConstraint.addCollection(collection);
//                        context.addConstraint(securityConstraint);
//                    }
//                };
//        tomcat.addAdditionalTomcatConnectors(connector);
//        return tomcat;
//    }
//
//    @Bean
//    public Connector connector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        //http端口
//        connector.setPort(8080);
//        connector.setSecure(false);
//        //https端口
//        connector.setRedirectPort(4433);
//        return connector;
//    }
//}
