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
package org.encuestame.core.service.imp;

import java.util.List;

import org.encuestame.persistence.domain.Comment;
import org.encuestame.persistence.domain.survey.Poll;
import org.encuestame.persistence.domain.survey.Survey;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.persistence.exception.EnMeExpcetion;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.persistence.exception.EnmeFailOperation;
import org.encuestame.persistence.exception.EnmeNotAllowedException;
import org.encuestame.utils.enums.CommentOptions;
import org.encuestame.utils.enums.CommentsSocialOptions;
import org.encuestame.utils.enums.SearchPeriods;
import org.encuestame.utils.enums.TypeSearchResult;
import org.encuestame.utils.web.CommentBean;
import org.hibernate.HibernateException;

/**
 * Comment Service.
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since August 14, 2011
 */
public interface ICommentService {

    /**
     *
     * @param searchResult
     * @param itemId
     * @param max
     * @param start
     * @return
     * @throws EnMeExpcetion
     */
    List<Comment> getComments(
            final TypeSearchResult searchResult,
            final Long itemId,
            final Integer max,
            final Integer start) throws EnMeExpcetion;

    /**
     * Get comment by id.
     * @param commentId
     * @return
     * @throws EnMeNoResultsFoundException
     * @throws HibernateException
     */
    Comment getCommentbyId(final Long commentId) throws EnMeNoResultsFoundException, HibernateException;

    /**
     * Get comments by user.
     * @param maxResults
     * @param start
     * @return
     * @throws EnMeNoResultsFoundException
     */
    List<CommentBean> getCommentsbyUser(final Integer maxResults,
            final Integer start, final List<CommentOptions> commentOptions) throws EnMeNoResultsFoundException;

    /**
     * Get comments by keyword.
     * @param keyword
     * @param maxResults
     * @param start
     * @return
     * @throws EnMeExpcetion
     */
    List<CommentBean> getCommentsbyKeyword(
                final String keyword,
                final Integer maxResults,
                final Integer start) throws EnMeExpcetion;

    /**
     * Create comment.
     * @param commentBean
     * @return
     * @throws EnMeNoResultsFoundException
     * @throws EnmeNotAllowedException
     */
    Comment createComment(final CommentBean commentBean) throws EnMeNoResultsFoundException, EnmeNotAllowedException;

    /***
     * Get comments by TweetPoll.
     * @param tweetPollId
     * @param maxResults
     * @param start
     * @return
     * @throws EnMeNoResultsFoundException
     */
    List<Comment> getCommentsbyTweetPoll(final TweetPoll tweetPollId,
                final Integer maxResults,
                final Integer start) throws EnMeNoResultsFoundException;

    /**
     * Vote comment social option.
     * @param commentId
     * @param vote
     * @throws EnMeNoResultsFoundException
     * @throws HibernateException
     * @throws EnmeFailOperation
     */
     void voteCommentSocialOption(final Long commentId, final CommentsSocialOptions vote) throws EnMeNoResultsFoundException,
        HibernateException, EnmeFailOperation;

     /**
      * Get top rated comments.
      * @param socialCommentOption
      * @param maxResults
      * @param start
      * @return
      */
     List<CommentBean> getTopRatedComments(final CommentsSocialOptions socialCommentOption, final Integer maxResults,
             final Integer start);

     /**
      * Retrieve {@link Comment} by Status and Comment type.
      * @param id
      * @param typeSearch
      * @param maxResults
      * @param start
      * @param commentOptions
      * @return
      */
     List<CommentBean> retrieveCommentsByTypeAndStatus(final Long id,
 			final TypeSearchResult typeSearch, final Integer maxResults,
 			final Integer start, final CommentOptions commentOptions, final SearchPeriods period);

     /**
      * Retrieve total comments by type: {@link TweetPoll} {@link Poll} {@link Survey}
      * @param id
      * @param itemType
      * @param commentOptions
      * @param period
      * @return
      * @throws EnMeNoResultsFoundException
      */
     Long totalCommentsbyType(final Long id,
 			final TypeSearchResult itemType,
 			final CommentOptions commentOptions, final SearchPeriods period) throws EnMeNoResultsFoundException;


     /**
      * Retrieve total comments by type: {@link TweetPoll}, {@link Poll} or Survey and Status : CommentOptions.
      * @param itemType
      * @param commentOptions
      * @param period
      * @return
      * @throws EnMeNoResultsFoundException
      */
     Long totalCommentsbyTypeAndStatus(final TypeSearchResult itemType,
 			final CommentOptions commentOptions, final SearchPeriods period)
 			throws EnMeNoResultsFoundException;
}
