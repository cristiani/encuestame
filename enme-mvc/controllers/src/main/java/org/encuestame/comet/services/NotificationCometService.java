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
//package org.encuestame.comet.services;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.inject.Named;
//import javax.inject.Singleton;
//
//import org.apache.log4j.Logger;
//import org.cometd.annotation.Listener;
//import org.cometd.bayeux.server.ServerMessage;
//import org.cometd.bayeux.server.ServerSession;
//import org.encuestame.persistence.domain.security.UserAccount;
//import org.encuestame.persistence.exception.EnMeExpcetion;
//import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
//
///**
// * Notification comet service.
// * @author Picado, Juan juanATencuestame.org
// * @since Mar 4, 2011
// */
//@Named // Tells Spring that this is a bean
//@Singleton // Tells Spring that this is a singleton
//@org.cometd.annotation.Service("notificationService")
//public class NotificationCometService extends AbstractCometService {
//
//    /*
//     * Log.
//     */
//    private Logger log = Logger.getLogger(this.getClass());
//
//    /**
//     * Notification services response.
//     * @param service
//     * @param message
//     */
//    @Listener("/service/notification/status")
//    public void processNotification(final ServerSession service, final ServerMessage.Mutable message) {
//        final Map<String, Object> input = message.getDataAsMap();
//        //log.debug("Notification Input "+input);
//        final Map<String, Object> output = new HashMap<String, Object>();
//        UserAccount userAccount;
//        try {
//            final String username = getUserPrincipalUsername();
//            //log.debug("User get by getUserPrincipalUsername ---> " + getUserPrincipalUsername());
//            if(!username.isEmpty()) {
//                userAccount = getByUsername(username);
//                if (userAccount != null) {
//                    final Long totalNot = getNotificationDao().retrieveTotalNotificationStatus(userAccount.getAccount());
//                    log.debug("totalNot "+totalNot);
//                    final Long totalNewNot = getNotificationDao().retrieveTotalNotReadedNotificationStatus(userAccount.getAccount());
//                    log.debug("totalNewNot "+totalNewNot);
//                    output.put("totalNot", totalNot);
//                    output.put("totalNewNot", totalNewNot);
//                    log.debug(totalNewNot + " NEW of "+totalNot+" total not");
//                } else {
//                    output.put("totalNot", 0);
//                    output.put("totalNewNot", 0);
//                }
//            } else {
//                throw new EnMeExpcetion("Auth object is not valid");
//            }
//        } catch (EnMeExpcetion e) {
//             output.put("totalNot", 0);
//             output.put("totalNewNot", 0);
//             log.fatal("cometd: username invalid");
//        }
//        service.deliver(getServerSession(), message.getChannel(), output, null);
//    }
//}
