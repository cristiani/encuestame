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
package org.encuestame.test.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.encuestame.persistence.dao.imp.AccountDaoImp;
import org.encuestame.persistence.domain.question.Question;
import org.encuestame.persistence.domain.question.QuestionAnswer;
import org.encuestame.persistence.domain.security.Account;
import org.encuestame.persistence.domain.security.Group;
import org.encuestame.persistence.domain.security.Permission;
import org.encuestame.persistence.domain.security.SocialAccount;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.domain.survey.Poll;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSwitch;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.test.config.AbstractBase;
import org.encuestame.utils.categories.test.DefaultTest;
import org.encuestame.utils.oauth.OAuth1Token;
import org.encuestame.utils.social.SocialProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * {@link AccountDaoImp} Test Case.
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since October 27, 2009
 */
@Category(DefaultTest.class)
public class TestUserDao extends AbstractBase {

    /** {@link Account} **/
    private Account account;

    /** {@link UserAccount} **/
    private UserAccount userAccount;

    /** {@link SocialAccount} **/
    private SocialAccount socialAccount;

    /** {@link QuestionAnswer}. **/
    private QuestionAnswer questionsAnswers1;

    /** {@link QuestionAnswer}. **/
    private QuestionAnswer questionsAnswers2;

    /** {@link TweetPollSwitch}. **/
    private TweetPollSwitch pollSwitch1;

    /** {@link TweetPollSwitch}. **/
    private TweetPollSwitch pollSwitch2;

    /** {@link TweetPoll}. **/
    private TweetPoll tweetPoll;

    /** {@link Poll} **/
    private Poll poll;

    /** {@link Question} **/
    private Question question;

    private String inviteCode = "04e0ca0b-3e80-4c21-bae2-ead56ec3f4ea";

    /**
     * Before.
     **/
    @Before
    public void initService(){
        this.account = createAccount();
        this.userAccount = createUserAccount("user 1", this.account);
        this.userAccount.setInviteCode(this.inviteCode);
        this.socialAccount = createDefaultSettedVerifiedSocialAccount(this.userAccount);
        this.question = createQuestion("What day is today?", "");
    }

    /***
     *Test Create User.
     */
    @Test
    public void testCreateUser() {
       final UserAccount user = createUserAccount("user 3", this.account);
       assertNotNull(user);
    }

    /**
     * Test delete user method.
     **/
    @Test
    public void testDeleteUser() {
        final UserAccount user = createUserAccount("user 2", this.account);
         getAccountDao().delete(user);
        assertEquals("Should be equals",1, getAccountDao().findAll().size());
    }

    /**
     * Test find all users method.
     */
    @Test
    public void testFindAllUsers() {
        createUserAccount("user 4", this.account);
        createUserAccount("user 5", this.account);
        assertEquals("Should be equals",3, getAccountDao().findAll().size());
    }

    /**
     * Test Update user.
     **/
    @Test
    public void testUpdateUser(){
        final String newPassword = "67809";
        final String newEmail = "user2@users.com";
        final UserAccount user = createUserAccount("user 6", this.account);
        user.setPassword(newPassword);
        user.setUserEmail(newEmail);
        getAccountDao().saveOrUpdate(user);
         final UserAccount retrieveUser = getAccountDao()
            .getUserAccountById(Long.valueOf(user.getUid()));
        assertEquals("Password should be",newPassword, retrieveUser.getPassword());
        assertEquals("Email should be",newEmail, retrieveUser.getUserEmail());
    }

    /**
     * Test Get User by Username.
     **/
    @Test
    public void testGetUserByUsername(){
        final UserAccount user = createUserAccount("user 3", this.account);
        final UserAccount retrieveUser = getAccountDao()
        .getUserByUsername(user.getUsername());
        assertEquals("Username should be",user.getUsername(), retrieveUser.getUsername());
    }

    /**
     * Test Assing Group to User.
     **/
/*    @SuppressWarnings("unchecked")
    @Test
    public void testAssingGroupToUser(){
         final SecUserSecondary user = createSecondaryUser("user 4", this.userPrimary);
         final SecGroup group = super.createGroups("group 1");
         user.getSecGroups().add(group);
         getSecGroup().saveOrUpdate(user);
         assertEquals("Should be equals", 1, user.getSecGroups().size());
    }*/

    /**
     * Test Add Permission to Group.
     */
    @Test
    public void testAddPermissionToGroup(){
        final Permission editor = createPermission("editor");
        final Permission admon = createPermission("publisher");
        final Permission permission = createPermission("administrator");
        final Group group = createGroups("group 1");
        group.getPermissions().add(editor);
        group.getPermissions().add(admon);
        group.getPermissions().add(permission);
        getGroup().saveOrUpdate(group);
        assertEquals("Should be equals", 3, group.getPermissions().size());
    }

    /**
     * Test.
     */
    @Test
    public void testSearchUsersByEmail(){
        final UserAccount secondary = createUserAccount("jhon", this.account);
        createUserAccount("paola", this.account);
        final List<UserAccount> users = getAccountDao().searchUsersByEmail(secondary.getUserEmail());
        assertEquals("Should be equals", 1, users.size());
    }

    /**
     * Test Retrieve Total Users.
     */
    @Test
    public void testRetrieveTotalUsers(){
         final Long totalUserAccount = getAccountDao().retrieveTotalUsers(this.account);
         assertEquals("Should be equals", 1, 1);
    }

    /**
     * Test Retrieve List Owner Users.
     */
    @Test
    public void testretRieveListOwnerUsers(){
        final List<UserAccount> usersAccount = getAccountDao().retrieveListOwnerUsers(this.account, 5, 0);
         assertEquals("Should be equals", 1, usersAccount.size());
    }

    /**
     * Test Get Twitter Account.
     */
    @Test
    public void testGetTwitterAccount(){
        final SocialAccount social = getAccountDao().getSocialAccountById(this.socialAccount.getId());
        assertEquals("Should be equals", this.socialAccount.getId(), social.getId());
    }

    /**
     * Test Get User by Id.
     */
    @Test
    public void testGetUserById(){
        final Account userAccount = getAccountDao().getUserById(this.account.getUid());
         assertEquals("Should be equals", this.account.getUid(), userAccount.getUid());
     }

    /**
     * Test Get User by Email.
     */
    @Test
    public void testGetUserByEmail(){
        final UserAccount userAcc = getAccountDao().getUserByEmail(this.userAccount.getUserEmail());
        assertNotNull(userAcc);
          assertEquals("Should be equals", this.userAccount.getUserEmail(), userAcc.getUserEmail());
    }

    /**
     * Test get Users By Username.
     */
    @Test
    public void testGetUsersByUsername(){
        final UserAccount user = getAccountDao().getUserByUsername(this.userAccount.getUsername());
        assertEquals("Should be equals", this.userAccount.getUsername(), user.getUsername());
    }

    /**
     * Test Get Twitter Verified Account By User.
     */
    @Test
    public void testgetTwitterVerifiedAccountByUser(){
        final List<SocialAccount> socAccount = getAccountDao().getSocialVerifiedAccountByUserAccount(this.account,
              SocialProvider.TWITTER);
        assertEquals("Should be equals", this.socialAccount.getVerfied(), socAccount.get(0).getVerfied());
        assertEquals("Should be equals", 1, socAccount.size());
        final List<SocialAccount> socAccount2 = getAccountDao().getSocialVerifiedAccountByUserAccount(this.account,
               SocialProvider.ALL);
        assertEquals("Should be equals", 1, socAccount2.size());
    }

    /**
     * Test Get Total TweetPoll by User.
     */
    @Test
    public void testGetTotalTweetPollByUser(){
        this.questionsAnswers1 = createQuestionAnswer("monday", question, "12345");
        this.questionsAnswers2 = createQuestionAnswer("sunday", question, "12346");
        this.tweetPoll = createPublishedTweetPoll(userAccount, question);
        this.pollSwitch1 = createTweetPollSwitch(questionsAnswers1, tweetPoll);
        this.pollSwitch2 = createTweetPollSwitch(questionsAnswers2, tweetPoll);
        createTweetPollResult(pollSwitch1, "192.168.0.1");
        createTweetPollResult(pollSwitch1, "192.168.0.2");
        createTweetPollResult(pollSwitch2, "192.168.0.3");
        createTweetPollResult(pollSwitch2, "192.168.0.4");
         final List<Long> tweets = getAccountDao().getTotalTweetPollByUser(this.account.getUid());
          assertEquals("Should be equals", 1, tweets.size());
    }

    /**
     * Test Get Total Poll by User.
     */
    @Test
    public void testGetTotalPollByUser(){
        this.poll = createPoll(new Date(), this.question, "FDK125", this.userAccount, Boolean.TRUE, Boolean.TRUE);
        final List<Long> polls = getAccountDao().getTotalPollByUser(this.account.getUid());
        assertEquals("Should be equals", 1, polls.size());
    }

    /**
     * Test for getAccountsEnabled.
     */
    @Test
    public void testGetAccountsEnabled(){
        for (int i = 0; i < 20; i++) {
            createAccount();
        }
        //create disabled account.
        createAccount(false);
        createAccount(false);
        createAccount(false);
        createAccount(false);
        final List<Long> d = getAccountDao().getAccountsEnabled(Boolean.TRUE);
        //20 + 2 on @Before.
        assertEquals("Should be equals", 22, d.size());
        if(log.isDebugEnabled()){
            for (Long long1 : d) {
                log.debug("d->"+long1);
            }
        }
    }

    /**
     * Test getSocialAccount.
     */
    //@Test
    public void testgetSocialAccount(){
        final SocialAccount ac = createSocialProviderAccount(this.userAccount, SocialProvider.GOOGLE_BUZZ);
        final SocialAccount ex = getAccountDao().getSocialAccount(ac.getId(), this.account);
        assertEquals("Should be equals", ac.getId(),ex.getId());
        final SocialAccount ex2 = getAccountDao().getSocialAccount(SocialProvider.GOOGLE_BUZZ, ex.getSocialProfileId());
        assertNotNull(ex2);
        assertEquals("Should be equals", ac.getId(), ex2.getId());
    }

    /**
     * Test getSocialAccountByAccount.
     */
    @Test
    public void testgetSocialAccountByAccount(){
        final List<SocialAccount> accounts = getAccountDao().getSocialAccountByAccount(this.account, SocialProvider.TWITTER);
        assertEquals("Should be equals", accounts.size(), 1);
    }

    /**
     * {@link } test case.
     * @throws EnMeNoResultsFoundException
     */
    @Test
    public void testisConnected() throws EnMeNoResultsFoundException{
        final UserAccount account = createUserAccount("jota", this.account);
        final OAuth1Token token = new OAuth1Token("token", "secret");
        //final AccountConnection ac = createConnection("TWITTER", token, "12345", account.getUid() , "ur");
        //final AccountConnection exAc = getAccountDao().getAccountConnection(ac.getUserAccout().getUid(), "TWITTER");
        //assertNotNull(exAc);
        //assertEquals("Should be equals", ac.getAccountConnectionId(), exAc.getAccountConnectionId());
//        final boolean conected = getAccountDao().isConnected(account.getUid(), "TWITTER");
//        assertTrue(conected);
//        getAccountDao().disconnect(account.getUid(), "TWITTER");
//        final boolean conected2 = getAccountDao().isConnected(account.getUid(), "TWITTER");
//        assertFalse(conected2);
    }

    /**
     * Disconected test case.
     * @throws EnMeNoResultsFoundException
     */
    //@Test(expected= EnMeNoResultsFoundException.class)
    public void testdisconnect() throws EnMeNoResultsFoundException{
        //getAccountDao().disconnect(account.getUid(), "TWITTER");
    }

    /**
     * Test getAccessToken.
     * @throws EnMeNoResultsFoundException
     */
    @Test
    public void testgetAccessToken() throws EnMeNoResultsFoundException{
//        final UserAccount account = createUserAccount("jota", this.account);
//        final OAuth1Token token = new OAuth1Token("token", "secret");
//        final AccountConnection accountConnection = createConnection("TWITTER", token, "12345",
//              account.getUid() , "ur");
//        final OAuth1Token token2 = getAccountDao().getAccessToken(account.getUid(), "TWITTER");
//        assertEquals("Should be equals", token.getSecret(),token2.getSecret());
//        assertEquals("Should be equals", token.getValue(),token2.getValue());
//        final AccountConnection ac2 = getAccountDao().findAccountConnectionBySocialProfileId("TWITTER",
//              accountConnection.getAccessToken());
//        assertNotNull(ac2);
//        final UserAccount exAccount = getAccountDao().findAccountByConnection("TWITTER",
//              accountConnection.getAccessToken());
//        assertNotNull(exAccount);
//        assertEquals("Should be equals", exAccount, account);
    }

    /**
     * Test exception getAccessToken.
     * @throws EnMeNoResultsFoundException
     */
	@Test(expected= EnMeNoResultsFoundException.class)
    public void testfindAccountByConnection() throws EnMeNoResultsFoundException{
		getAccountDao().findAccountByConnection(SocialProvider.FACEBOOK, "xxxxxxxx");
    }

    /**
     * Test exception getAccessToken.
     * @throws EnMeNoResultsFoundException
     */
    //@Test(expected= EnMeNoResultsFoundException.class)
    public void testgetAccessToken2() throws EnMeNoResultsFoundException{
         //getAccountDao().getAccessToken(account.getUid(), "TWITTER");
    }

    /**
     * Test getPublicProfiles.
     */
    @Test
    public void testgetPublicProfiles(){
        flushIndexes();
        final List<UserAccount> profiles = getAccountDao().getPublicProfiles("user", 100, 0);
        assertEquals("Should be equals", profiles.size(), 1);
    }

    /**
     * Test get {@link UserAccount} by invitation code.
     */
    @Test
    public void testGetUserAccountbyInviteCode(){
        assertNotNull(this.inviteCode);
        final UserAccount acc = getAccountDao().getUserAccountbyInvitationCode(this.inviteCode);
        assertNotNull(acc);
        assertEquals("Should be equals", acc.getInviteCode(), this.inviteCode);
    }

    /**
     *
     */
    @Test
    public void testgetSocialAccountStats() {
        createTweetPollPublicated(true, true, null, userAccount, createQuestion("test", this.userAccount.getAccount()));
        createTweetPollSavedPublishedStatus(tweetPoll, "12345", this.socialAccount, "hello encuestame");
        createTweetPollSavedPublishedStatus(tweetPoll, "12346", this.socialAccount, "hello encuestame 1");
        createTweetPollSavedPublishedStatus(tweetPoll, "12347", this.socialAccount, "hello encuestame 2");
        createTweetPollSavedPublishedStatus(tweetPoll, "12348", this.socialAccount, "hello encuestame 3");
        getAccountDao().getSocialAccountStats(this.socialAccount);
    }

    /**
     * Test get user account list by status.
     */
    @Test
    public void testGetUserAccountsbyStatus(){

        final Calendar createdAt = Calendar.getInstance();
        final Calendar currentDate = Calendar.getInstance();
        // Date range
        final Calendar beforeDate = Calendar.getInstance();
        beforeDate.add(Calendar.DATE, -7);
        beforeDate.add(Calendar.HOUR, +5);
        // final String expireValue = getProperty("account.expire.limit");


        for (int i = 0; i < 10; i++) {
            createdAt.add(Calendar.DATE, -i);
            createdAt.add(Calendar.HOUR, +i);
               final UserAccount uAcc = createUserAccount(Boolean.FALSE, createdAt.getTime(), "diana-"+i, this.account);

        }
        //create disabled account.g
        createdAt.add(Calendar.MONTH, +1);
        createUserAccount(Boolean.FALSE, createdAt.getTime() ,"user 2", this.account);
        createdAt.add(Calendar.DATE, +10);
        createUserAccount(Boolean.FALSE, createdAt.getTime() ,"user 3", this.account);
        createdAt.add(Calendar.MONTH, +12);
        createUserAccount(Boolean.FALSE, createdAt.getTime() ,"user 4", this.account);

        final List<UserAccount> userAcc = getAccountDao().getUserAccountsbyStatus(Boolean.FALSE, beforeDate.getTime(), currentDate.getTime());
           //10 + 1 on @Before.
        assertEquals("Should be equals", 5, userAcc.size());
           if(log.isDebugEnabled()){
               for (UserAccount userStatus : userAcc) {
                   log.debug("d->"+userStatus);
               }
           }
       }

    /**
     * Test get userAccounts.
     */
    @Test
    public void testGetUserAccounts() {
        createUserAccount("user 23", this.account);
        final List<UserAccount> userAccountList = getAccountDao()
                .getUserAccounts(Boolean.TRUE);
        assertEquals("Should be equals", 2, userAccountList.size());
    }
}