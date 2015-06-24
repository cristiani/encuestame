/*
 ************************************************************************************
 * Copyright (C) 2001-2011 encuestame: system online surveys Copyright (C) 2011
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.core.test.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.encuestame.core.integration.gateway.TwitterGateway;
import org.encuestame.core.test.config.AbstractIntegrationConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

@Ignore
public class IntegrationTestCase extends AbstractIntegrationConfig {

    @Autowired
    PublishSubscribeChannel bites;

    @Autowired
    DirectChannel transformerInputChannel;

    @Resource
    QueueChannel twitterTransformedChannel;

    @Autowired
    QueueChannel transformerOutputChannel;


    @Autowired
    DirectChannel twitterChannel;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss Z");


    @Autowired
    TwitterGateway twitterAdapter;


    private int messagesReceived = 0;

    @Test
    public void testTransformation() throws Exception {
        transformerInputChannel.send(new Message<String>() {
            public MessageHeaders getHeaders() {
                return new MessageHeaders(new HashMap<String, Object>());
            }

            public String getPayload() {
                return "original www.xebia.com message http://www.xebia.com";
            }
        });

        Message<?> transformedMessage = transformerOutputChannel.receive();
        log.debug("trams" + transformedMessage.getPayload());
        assertNotNull(transformedMessage);

        assertNotSame((String) transformedMessage.getPayload(),
                ("original www.xebia.com message http://www.xebia.com"));
        log.debug("tinyurl.com");
    }

    @Test
    public void testTwitterChannel(){
        log.debug("testTwitterChannel");
        MessageChannel twitterOutChannel = this.twitterTransformedChannel;
        Message<String> twitterUpdate = new GenericMessage<String>("22 Testing  http://www.google.es new Twitter samples for #springintegration "+RandomStringUtils.random(2));
        log.debug("twitterOutChannel message "+twitterUpdate.getPayload());
        twitterOutChannel.send(twitterUpdate);
        log.debug("twitterOutChannel");
    }

    @Test
    public void shouldAcceptBites() throws Exception {
        String message = "Test Message :"
                + dateFormat.format(Calendar.getInstance().getTime());

        // String message = "http://google.com";

        final AtomicReference<Message<?>> received = new AtomicReference<Message<?>>();
        bites.subscribe(new MessageHandler() {
            public void handleMessage(Message<?> message)
                    throws MessagingException {
                received.set(message);
            }
        });
        twitterAdapter.publishTweet(message);
        // assertThat(((String) received.get().getPayload()), is(message));
        // assertThat(received.get(), hasPayload(message));
    }

    protected void updateMessagesReceived() {
        messagesReceived++;

    }

    @Test
    public void testTweetLongUrl() throws Exception {

        bites.send(new Message<String>() {
            public MessageHeaders getHeaders() {
                return new MessageHeaders(new HashMap<String, Object>());
            }

            public String getPayload() {
                return "Url Message :"
                        + dateFormat.format(Calendar.getInstance().getTime())
                        + " http://www.google.es" + " url ";
            }
        });
    }
}
