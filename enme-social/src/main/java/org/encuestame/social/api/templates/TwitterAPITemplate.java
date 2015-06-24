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
package org.encuestame.social.api.templates;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.encuestame.persistence.domain.security.SocialAccount;
import org.encuestame.social.AbstractSocialAPISupport;
import org.encuestame.social.api.operation.TwitterAPIOperations;
import org.encuestame.utils.TweetPublishedMetadata;
import org.encuestame.utils.social.SocialUserProfile;
import org.springframework.util.Assert;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Twitter Service.
 * @author Picado, Juan juanATencuestame.org
 * @since Feb 13, 2010 4:07:03 PM
 */

public class TwitterAPITemplate extends AbstractSocialAPISupport implements TwitterAPIOperations{

    /**
     * Log.
     */
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Twitter consumer secret.
     */
    private String consumerSecret;

    /**
     * Twitter consumer key.
     */
    private String consumerKey;

    /**
     * Access Token.
     */
    private String accessToken;

    /**
     * Secret Token.
     */
    private String secretToken;

    /**
     *
     */
    private ConfigurationBuilder configurationBuilder;

    /**
     *
     */
    private SocialAccount socialAccount;


    /**
     * Create Twitter API instance.
     * @param consumerSecret
     * @param consumerKey
     */
    @Deprecated
    public TwitterAPITemplate(
            final String consumerSecret,
            final String consumerKey, final String accessToken,
            final String secretToken) {
        Assert.notNull(consumerKey);
        Assert.notNull(consumerSecret);
        Assert.notNull(accessToken);
        Assert.notNull(secretToken);
        log.debug("consumer key "+consumerKey);
        log.debug("consumer secret "+consumerSecret);
        log.debug("secret TOKEN "+secretToken);
        log.debug("acces TOKEN "+accessToken);
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.secretToken = secretToken;
        this.accessToken = accessToken;
        this.configurationBuilder = new ConfigurationBuilder();
        this.configurationBuilder.setDebugEnabled(true)
          .setOAuthConsumerKey(this.consumerKey)
          .setOAuthConsumerSecret(this.consumerSecret)
          .setOAuthAccessToken(this.accessToken)
          .setOAuthAccessTokenSecret(this.secretToken);
    }

    /**
     *
     * @param consumerSecret
     * @param consumerKey
     * @param account
     */
    public TwitterAPITemplate(
            final String consumerSecret,
            final String consumerKey,
            final SocialAccount account) {
        Assert.notNull(consumerKey);
        Assert.notNull(consumerSecret);
        Assert.notNull(account);
        log.debug("consumer key "+consumerKey);
        log.debug("consumer secret "+consumerSecret);
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.secretToken = account.getSecretToken();
        this.accessToken = account.getAccessToken();
        this.socialAccount = account;
        this.configurationBuilder = new ConfigurationBuilder();
        this.configurationBuilder.setDebugEnabled(true)
          .setOAuthConsumerKey(this.consumerKey)
          .setOAuthConsumerSecret(this.consumerSecret)
          .setOAuthAccessToken(this.accessToken)
          .setOAuthAccessTokenSecret(this.secretToken);
    }

    /**
     *
     * @param socialTwitterAccount
     * @return
     */
   private Twitter getTwitterInstance(){
       return this.getOAuthAuthorizedInstance();
   }

    /**
     * OAuth Public Tweet.
     * @param socialTwitterAccount
     * @param tweet
     * @return
     * @throws TwitterException
     */
    public TweetPublishedMetadata updateTwitterStatus(final String tweet) throws TwitterException{
        log.debug("twitter update status 2--> "+tweet);
        final Twitter twitter = this.getTwitterInstance();
        final Status twitterStatus = twitter.updateStatus(tweet);
        log.debug("twitter update status "+twitterStatus);
        TweetPublishedMetadata status = createStatus(tweet);
        status.setTweetId(String.valueOf(twitterStatus.getId()));
        //statusTweet.set status.g
        status.setDatePublished(twitterStatus.getCreatedAt());
        status.setProvider(this.socialAccount.getAccounType().name());
        status.setSocialAccountId(this.socialAccount.getId());
        status.setSocialAccountName(this.socialAccount.getSocialAccountName());
        //statusTweet.setProvider(SocialProvider.TWITTER);
        log.debug("twitter update statusTweet "+status);
        return status;
    }

    /**
     *
     */
    public TweetPublishedMetadata updateStatus(final String tweet){
        log.debug("twitter update status 1--> "+tweet);
        try {
            return this.updateTwitterStatus(tweet);
        } catch (TwitterException e) {
            log.error(e);
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Get twitter user profile.
     * @param socialTwitterAccount {@link SocialAccount}.
     * @return
     * @throws TwitterException
     */
    public User getUser() throws TwitterException{
            return getTwitterInstance().verifyCredentials();
    }

    /**
     * Verify Credentials.
     * @param socialAccount
     * @return
     */
    public Boolean verifyCredentials(){
        final Twitter twitter = this.getTwitterInstance();
        User user;
        try {
            user = twitter.verifyCredentials();
        } catch (TwitterException e) {
            user = null;
            log.error(e);
        }
        return user == null ? false : true;
    }

    /**
     * Get OAuthorized Token.
     * @param socialTwitterAccount {@link SocialAccount}.
     * @return {@link Twitter}.
     */
    private Twitter getOAuthAuthorizedInstance(){
        final TwitterFactory tf = new TwitterFactory(this.configurationBuilder.build());
        final Twitter twitter = tf.getInstance();
        return twitter;
    }

    /**
     * Get Twitter Ping.
     * @param consumerKey consumer key
     * @param consumerSecret consumer secret
     * @return {@link RequestToken}
     * @throws TwitterException exception
     */
    public RequestToken getTwitterPing() throws TwitterException {
        if (consumerKey == null) {
            throw new IllegalArgumentException("Consumer key is missing");
        }
        if (consumerSecret == null) {
            throw new IllegalArgumentException("Consumer secret is missing");
        }
        final Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        return twitter.getOAuthRequestToken();
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.social.SocialAPIOperations#getProfile()
     */
    @Override
    public SocialUserProfile getProfile() throws Exception {
        final SocialUserProfile profile = new SocialUserProfile();
        User user = this.getUser();
        profile.setId(String.valueOf(user.getId()));
        profile.setCreatedAt(user.getCreatedAt());
        profile.setProfileUrl("http://www.twitter.com/"+user.getScreenName());
        profile.setName(user.getName());
        profile.setScreenName(user.getScreenName());
        profile.setUsername(user.getScreenName());
        profile.setProfileImageUrl(user.getProfileImageURL().toString());
        profile.setDescription(user.getDescription());
        profile.setCreatedAt(user.getCreatedAt());
        profile.setLocation(user.getLocation());
        return profile;
    }
}
