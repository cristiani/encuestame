///*
// ************************************************************************************
// * Copyright (C) 2001-2011 encuestame: system online surveys Copyright (C) 2011
// * encuestame Development Team.
// * Licensed under the Apache Software License version 2.0
// * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to  in writing,  software  distributed
// * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
// * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
// * specific language governing permissions and limitations under the License.
// ************************************************************************************
// */
//package org.encuestame.comet.server;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.inject.Inject;
//import javax.servlet.ServletContext;
//
//import org.apache.log4j.Logger;
//import org.cometd.annotation.ServerAnnotationProcessor;
//import org.cometd.bayeux.server.BayeuxServer;
//import org.cometd.server.BayeuxServerImpl;
//import org.cometd.server.ext.AcknowledgedMessagesExtension;
//import org.cometd.server.transport.JSONPTransport;
//import org.cometd.server.transport.JSONTransport;
//import org.cometd.websocket.server.WebSocketTransport;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.ServletContextAware;
//
///**
// * Comet Bayeux Server Initializer.
// * @author Picado, Juan juanATencuestame.org
// * @since Mar 4, 2011
// */
//@Component
//public class CometBayeuxInitializer implements DestructionAwareBeanPostProcessor,
//        ServletContextAware {
//
//    /*
//     * Log.
//     */
//    private Logger log = Logger.getLogger(this.getClass());
//
//    private BayeuxServer bayeuxServer;
//    private ServerAnnotationProcessor processor;
//
//    @Inject
//    private void setBayeuxServer(BayeuxServer bayeuxServer)
//    {
//        this.bayeuxServer = bayeuxServer;
//    }
//
//    @PostConstruct
//    private void init()
//    {
//        this.processor = new ServerAnnotationProcessor(bayeuxServer);
//    }
//
//    @PreDestroy
//    private void destroy()
//    {
//    }
//
//    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException
//    {
//        processor.processDependencies(bean);
//        processor.processConfigurations(bean);
//        processor.processCallbacks(bean);
//        return bean;
//    }
//
//    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException
//    {
//        return bean;
//    }
//
//    public void postProcessBeforeDestruction(Object bean, String name) throws BeansException
//    {
//        processor.deprocessCallbacks(bean);
//    }
//
//    /**
//     * Bayeux Server.
//     * @return {@link BayeuxServer}.
//     */
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public BayeuxServer bayeuxServer() {
//        BayeuxServerImpl bean = new BayeuxServerImpl();
//        bean.setOption(BayeuxServerImpl.LOG_LEVEL, "0");
//        //http://cometdaily.com/2009/03/27/cometd-acknowledged-message-extension/
//        bean.addExtension(new AcknowledgedMessagesExtension());
//        return bean;
////         log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
////         log.debug("&&&&&&&&&&&&   COMET SERVER INITIALIZED &&&&&&&&&&&&&&&&&&&&");
////         BayeuxServerImpl bean = new BayeuxServerImpl();
////         bean.setTransports(new WebSocketTransport(bean), new JSONTransport(bean), new JSONPTransport(bean));
////         log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//         //return bean;
//    }
//
//    /**
//     * Set spring servlet context.
//     */
//    public void setServletContext(ServletContext servletContext) {
//        servletContext.setAttribute(BayeuxServer.ATTRIBUTE, bayeuxServer);
//    }
//}
