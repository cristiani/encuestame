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

package org.encuestame.persistence.dao;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.encuestame.persistence.dao.imp.TweetPollDao;
import org.encuestame.persistence.domain.question.QuestionAnswer;
import org.encuestame.persistence.domain.security.Account;
import org.encuestame.persistence.domain.security.SocialAccount;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.domain.survey.Poll;
import org.encuestame.persistence.domain.survey.Survey;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.persistence.domain.tweetpoll.TweetPollFolder;
import org.encuestame.persistence.domain.tweetpoll.TweetPollResult;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSavedPublishedStatus;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSwitch;
import org.encuestame.utils.enums.SearchPeriods;
import org.encuestame.utils.enums.TypeSearchResult;
import org.encuestame.utils.social.SocialProvider;
import org.hibernate.HibernateException;

/**
 * Interface to {@link TweetPollDao}.
 * @author Picado, Juan juanATencuestame.org
 * @since Feb 17, 2010 8:27:20 PM
 * @version $Id: change to one dolar simbol
 */

public interface ITweetPoll extends IBaseDao{

    /**
     * Get TweetPoll by Id.
     * @param tweetPollId tweetPollId
     * @return {@link TweetPoll}
     * @throws HibernateException exception
     */
    TweetPoll getTweetPollById(final Long tweetPollId) throws HibernateException;

    /**
     * Get a {@link TweetPollSavedPublishedStatus} by Id
     * @param id id
     * @return {@link TweetPollSavedPublishedStatus}
     */
    TweetPollSavedPublishedStatus getTweetPollPublishedStatusbyId(final Long id);

    /**
     * Get published tweetpoll by id.
     * @param tweetPollId
     * @return
     */
    TweetPoll getPublicTweetPollById(final Long tweetPollId);

    /**
     * Retrieve Tweets Poll by User Id.
     * @param userId userId
     * @return list of tweet pools.
     */
    List<TweetPoll> retrieveTweetsByUserId(final String keyword, final Long userId,
            final Integer maxResults, final Integer start,
            final Boolean isCompleted, final Boolean isScheduled,
            final Boolean isPublished, final Boolean isFavourite,
            final String period);

     /**
      * Retrieve Tweets Poll Switch.
      * @param tweetCode tweetCode
      * @return switch
      */
     TweetPollSwitch retrieveTweetsPollSwitch(final String tweetCode);

     /**
      * Validate Vote IP.
      * @param ip ip
      * @param tweetPoll tweetPoll
      * @return {@link TweetPollSwitch}
      */
     TweetPollResult validateVoteIP(final String ip, final TweetPoll tweetPoll);

     /**
      * Get Results By {@link TweetPoll} && {@link QuestionAnswer}.
      * @param tweetPoll {@link TweetPoll}
      * @param answers {@link QuestionAnswer}
      * @return List of {@link TweetPollResult}
      */
      List<Object[]> getResultsByTweetPoll(final TweetPoll tweetPoll, QuestionAnswer answers);

      /**
       * Get TweetPoll by Question Name.
       * @param keyWord keyword
       * @param userId user Id.
       * @return
       */
      List<TweetPoll> retrieveTweetsByQuestionName(final String keyWord, final Long userId,
              final Integer maxResults,
              final Integer start,
              final Boolean isCompleted, final Boolean isScheduled,
              final Boolean isFavourite, final Boolean isPublished, final String period);

      /**
       * Get List of Switch Answers by TweetPoll.
       * @param tweetPoll {@link TweetPoll}.
       * @return List of {@link TweetPollSwitch}
       */
      List<TweetPollSwitch> getListAnswesByTweetPoll(final TweetPoll tweetPoll);

      /**
       * Get Votes By {@link TweetPollSwitch}..
       * @param pollSwitch {@link TweetPollSwitch}..
       * @return
       */
      List<Long> getVotesByAnswer(final TweetPollSwitch pollSwitch);


      /**
       * Get Total Votes By {@link TweetPoll}.
       * @param tweetPoll {@link TweetPoll}.
       * @return List of Votes.
       */
      List<Object[]> getTotalVotesByTweetPoll(final Long tweetPollId);

      /**
       * Retrieve TweetPoll Folder by User Id.
       * @param account {@link Account}.
       * @return
       */
      List<TweetPollFolder> retrieveTweetPollFolderByAccount(final Account account);

      /**
       * Retrieve TweetPoll by Folder Id.
       * @param userId
       * @param folderId
       * @return
       */
      List<TweetPoll> retrieveTweetPollByFolder(final Long userId, final Long folderId);

      /**
       * Get Tweet Poll Folder by Id.
       * @param folderId
       * @return
       */
      TweetPollFolder getTweetPollFolderById(final Long folderId);

      /**
       * Get TweetPoll Folders by Id and UserId.
       * @param folderId
       * @param account
       * @return
       */
      TweetPollFolder getTweetPollFolderByIdandUser(final Long folderId, final Account account);

      /**
       *
       * @param tweetPollId
       * @param userId
       * @return
       */
      TweetPoll getTweetPollByIdandUserId(final Long tweetPollId, final Long userId);

      /**
       * Get {@link TweetPoll} by id, userid and slug name.
       * @param tweetPollId tweet poll id.
       * @param userId user id.
       * @param slugName slug name.
       * @return
     * @throws UnsupportedEncodingException
       */
      TweetPoll getTweetPollByIdandSlugName(final Long tweetPollId, final String slugName) throws UnsupportedEncodingException;

      /**
       * Retrieve TweetPoll Today.
       * @param account
       * @param maxResults
       * @param start
       * @param isCompleted
       * @param isScheduled
       * @param isFavourite
       * @param isPublished
       * @param keyword
       * @param period
       * @return
       */
    List<TweetPoll> retrieveTweetPollToday(final Account account,
            final Integer maxResults, final Integer start,
            final Boolean isCompleted, final Boolean isScheduled,
            final Boolean isFavourite, final Boolean isPublished,
            final String keyword, final String period);

      /**
       * Retrieve TweetPoll Last Week.
       * @param keyWord
       * @param userId
       * @return
       */
      List<TweetPoll> retrieveTweetPollLastWeek(
              final Account account,
              final Integer maxResults,
              final Integer start,
              final Boolean isCompleted,
              final Boolean isScheduled,
              final Boolean isFavourite,
              final Boolean isPublished,
              final String keyword,
              final String period);

      /**
       * Retrieve Favourites TweetPolls.
       * @param keyWord
       * @param userId
       * @return
       */
      List<TweetPoll> retrieveScheduledTweetPoll(
              final Long userId,
              final Integer maxResults,
              final Integer start,
              final Boolean isCompleted,
              final Boolean isScheduled,
              final Boolean isFavourite,
              final Boolean isPublished,
              final String keyword,
              final String period);

      /**
       * Retrieve Favourites TweetPolls.
       * @param keyWord
       * @param userId
       * @return
       */
      List<TweetPoll> retrieveFavouritesTweetPoll(
              final Account account,
              final Integer maxResults,
              final Integer start,
              final Boolean isCompleted,
              final Boolean isScheduled,
              final Boolean isFavourite,
              final Boolean isPublished,
              final String keyword,
              final String period);

      /**
       * Retrieve Total Votes by TweetPoll Id.
       * @param tweetPollId
     * @return
       * @return
       */
     Long getTotalVotesByTweetPollId(final Long tweetPollId);

     /**
      * Retrieve TweetPoll by Date
      * @param account
      * @param initDate
      * @param maxResults
      * @param start
      * @param isCompleted
      * @param isScheduled
      * @param isFavourite
      * @param isPublished
      * @param keyword
      * @param period
      * @return
      */
    List<TweetPoll> retrieveTweetPollByDate(final Account account,
            final Integer maxResults, final Integer start,
            final Boolean isCompleted, final Boolean isScheduled,
            final Boolean isFavourite, final Boolean isPublished,
            final String keyword, final String period, final Date initDate);

    /**
     * Retrieve {@link TweetPollSwitch} by tweetpoll and answer. (should be unique)
     * @param tweetPoll {@link TweetPoll}
     * @param questionAnswer {@link QuestionAnswer}
     * @return {@link TweetPollSwitch}.
     */
    TweetPollSwitch getAnswerTweetSwitch(final TweetPoll tweetPoll, final QuestionAnswer questionAnswer);

    /**
     * Retrieve all {@link TweetPollSwitch} by {@link QuestionAnswer}.
     * @param questionAnswer {@link QuestionAnswer}.
     * @return
     */
    List<TweetPollSwitch> getAnswerTweetSwitch(final QuestionAnswer questionAnswer);

    /**
     * Get tweetPoll by top rated.
     * @param tagName
     * @param startResults
     * @param limit
     * @param filterby
     * @return
     */
    List<TweetPoll> getTweetpollByHashTagName(final String tagName,
            final Integer startResults, final Integer limit,
            final TypeSearchResult filterby,
            final SearchPeriods periods);


  /**
   * Get results by tweetpoll.
   * @param tweetPollId
   * @param answerId
   * @return
   */
   List<Object[]> getResultsByTweetPoll(final Long tweetPollId, final Long answerId);

   /**
    * Return all links published by {@link TweetPoll}, {@link Survey}, {@link Poll}.
    * @param tweetPoll
    * @param survey
    * @param poll
    * @param itemType
    * @return
    */
   public List<TweetPollSavedPublishedStatus> getLinksByTweetPoll(final TweetPoll tweetPoll, final Survey survey, final Poll poll, final TypeSearchResult itemType);

   /**
    * Get max tweetPoll like votes by user.
    * @param userId
    * @return
    */
   Long getMaxTweetPollLikeVotesbyUser(final Long userId);

   /**
    * Get tweetPolls.
    * @param maxResults
    * @param start
    * @param range
    * @return
    */
    List<TweetPoll> getTweetPolls(final Integer maxResults,
            final Integer start, final Date range);

    /**
     * Get total tweetpolls by user.
     * @param user
     * @param publishTweetPoll
     * @return
     */
    Long getTotalTweetPoll(final UserAccount user,
            final Boolean publishTweetPoll);

    /**
     * Get Total social links by Type: {@link TweetPoll}, {@link Poll} and {@link Survey}.
     * @param tweetPoll
     * @param survey
     * @param poll
     * @param itemType
     * @return
     */
    Long getSocialLinksByType(final TweetPoll tweetPoll, final Survey survey,
            final Poll poll, final TypeSearchResult itemType);

    /**
     * Get total tweepolls published by hashtag.
     * @param tagName
     * @param period
     * @return
     */
    List<TweetPoll> getTweetPollsbyHashTagNameAndDateRange(
                    final String tagName,
                    final SearchPeriods period);

    /**
     * Get social links by type( {@link Poll}, {@link TweetPoll} or {@link Survey}) and date range.
     * @param tweetPoll
     * @param survey
     * @param poll
     * @param itemType
     * @return
     */
    List<TweetPollSavedPublishedStatus> getSocialLinksByTypeAndDateRange(final TweetPoll tweetPoll,
            final Survey survey, final Poll poll,
            final TypeSearchResult itemType);

    /**
     * Return a list of tweetpoll by {username} order by recent.
     * @param limitResults
     * @param account
     * @return
     */
    public List<TweetPoll> getTweetPollByUsername(
            final Integer limitResults,
            final UserAccount account);

    /**
     * Get List Answers(Votes) by TweetPoll And date range.
     * @param tweetPoll
     * @param period
     * @param startResults
     * @param limit
     * @return
     */
    List<TweetPollSwitch> getListAnswersByTweetPollAndDateRange(
            final TweetPoll tweetPoll);

    /**
     *
     * @param tweetPollId
     * @param period
     * @return
     */
    Long getTotalVotesByTweetPollIdAndDateRange(final Long tweetPollId, final String period);

    /**
     * Get counter total tweetpoll results by {@link TweetPollSwitch}
     * @param pollSwitch
     * @return
     */
    Long getTotalTweetPollResultByTweetPollSwitch(
            final TweetPollSwitch pollSwitch, final SearchPeriods period);

    /**
     * Get all tweetpoll results by tweetpoll switch.
     * @param pollSwitch
     * @return
     */
    List<TweetPollResult> getTweetPollResultsByTweetPollSwitch(final TweetPollSwitch pollSwitch);

    /**
     *
     * @param latitude
     * @param longitude
     * @param distance
     * @param radius
     * @param maxItems
     * @param type
     * @param period
     * @return
     */
    List<Object[]> retrieveTweetPollsBySearchRadiusOfGeoLocation(
            final double latitude, final double longitude, final double distance,
            final double radius, final int maxItems,
            final TypeSearchResult type, final SearchPeriods period);

    TweetPoll checkIfTweetPollHasHashTag(final String tagName, final SearchPeriods periods,
            final Long id);

    /**
     * Validate {@link TweetPollResult} Ip.
     * @param ip
     * @param tweetPoll
     * @return
     */
    List<TweetPollResult> validateTweetPollResultsIP(final String ip, final TweetPoll tweetPoll);

    /**
     * Retrieve published and unpublished tweetpolls.
     * @param account
     * @param maxResults
     * @param start
     * @param isPublished
     * @return
     */
    List<TweetPoll> retrievePublishedUnpublishedTweetPoll(final Account account,
                final Integer maxResults, final Integer start, final Boolean isPublished);

    /**
     * Retrieve completed tweetpolls.
     * @param account
     * @param maxResults
     * @param start
     * @param isComplete
     * @return
     */
    List<TweetPoll> retrieveCompletedTweetPoll(final Account account,
                final Integer maxResults, final Integer start, final Boolean isComplete);

    /**
     *
     * @param isPublished
     * @param isComplete
     * @param favourites
     * @param scheduled
     * @param user
     * @param start
     * @param max
     * @param period
     * @param keyword
     * @return
     */
    List<TweetPoll> advancedSearch(final Boolean isPublished,
            final Boolean isComplete, final Boolean favourites,
            final Boolean scheduled, final Account user,
            final Integer start, final Integer max,
            final Integer period, final String keyword);

    /**
     * Get social Links by tweetpoll.
     * @param tweetPoll
     * @param itemType
     * @param splist
     * @return
     */
    List<TweetPollSavedPublishedStatus> getSocialLinksByTweetPollSearch(
            final TweetPoll tweetPoll, final TypeSearchResult itemType,
            final List<SocialProvider> splist, final List<SocialAccount> socialAccounts);

    /**
     * Retrieve {@link TweetPoll} stats
     * @param tagName
     * @param period
     * @return
     */
	List<Object[]> getTweetPollsRangeStats(final String tagName,
			final SearchPeriods period);
	/**
	 * Return all possible links related with one asset
	 * @param tweetPoll
	 * @param survey
	 * @param poll
	 * @param itemType
	 * @return
	 */
	List<TweetPollSavedPublishedStatus> getAllLinks(
            final TweetPoll tweetPoll, final Survey survey, final Poll poll,
            final TypeSearchResult itemType);

	List<TweetPollSavedPublishedStatus> searchSocialLinksbyType(
	            final TweetPoll tweetPoll,  final Poll poll, final TypeSearchResult itemType, final List<SocialProvider> splist, final List<SocialAccount> socialAccounts);

	/**
	 * Retrieve {@link TweetPollFolder} by keyword
	 * @param keyword
	 * @param userAcc
	 * @return
	 */
	List<TweetPollFolder> getTweetPollFolderByKeyword(final String keyword,
			final UserAccount userAcc);

	  /**
     *  Retrieve Polls by Keyword without User.
     * @param keyword
     * @param start
     * @param maxResults
     * @return
     */
	List<TweetPoll> retrieveTweetPollByKeyword(final String keyword,
			final Integer start,
			final Integer maxResults);
}