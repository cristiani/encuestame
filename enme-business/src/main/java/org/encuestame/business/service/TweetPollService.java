/*
 ************************************************************************************
 * Copyright (C) 2001-2011 encuestame: system online surveys Copyright (C) 2009
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.business.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.encuestame.core.config.EnMePlaceHolderConfigurer;
import org.encuestame.core.exception.EnMeFailSendSocialTweetException;
import org.encuestame.core.service.imp.ITweetPollService;
import org.encuestame.core.util.ConvertDomainBean;
import org.encuestame.core.util.EnMeUtils;
import org.encuestame.core.util.SocialUtils;
import org.encuestame.persistence.domain.HashTag;
import org.encuestame.persistence.domain.Schedule;
import org.encuestame.persistence.domain.question.Question;
import org.encuestame.persistence.domain.question.QuestionAnswer;
import org.encuestame.persistence.domain.security.SocialAccount;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.domain.survey.Poll;
import org.encuestame.persistence.domain.survey.Survey;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.persistence.domain.tweetpoll.TweetPollFolder;
import org.encuestame.persistence.domain.tweetpoll.TweetPollResult;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSavedPublishedStatus;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSwitch;
import org.encuestame.persistence.exception.EnMeExpcetion;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.persistence.exception.EnMeTweetPollNotFoundException;
import org.encuestame.persistence.exception.EnmeFailOperation;
import org.encuestame.utils.DateUtil;
import org.encuestame.utils.RestFullUtil;
import org.encuestame.utils.TweetPublishedMetadata;
import org.encuestame.utils.ValidationUtils;
import org.encuestame.utils.enums.CommentOptions;
import org.encuestame.utils.enums.NotificationEnum;
import org.encuestame.utils.enums.QuestionPattern;
import org.encuestame.utils.enums.Status;
import org.encuestame.utils.enums.TypeSearch;
import org.encuestame.utils.enums.TypeSearchResult;
import org.encuestame.utils.json.FolderBean;
import org.encuestame.utils.json.LinksSocialBean;
import org.encuestame.utils.json.QuestionBean;
import org.encuestame.utils.json.SearchBean;
import org.encuestame.utils.json.SocialAccountBean;
import org.encuestame.utils.json.TweetPollAnswerSwitchBean;
import org.encuestame.utils.json.TweetPollBean;
import org.encuestame.utils.json.TweetPollScheduledBean;
import org.encuestame.utils.social.SocialProvider;
import org.encuestame.utils.web.HashTagBean;
import org.encuestame.utils.web.PollDetailBean;
import org.encuestame.utils.web.QuestionAnswerBean;
import org.encuestame.utils.web.TweetPollDetailBean;
import org.encuestame.utils.web.TweetPollResultsBean;
import org.encuestame.utils.web.search.TweetPollSearchBean;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * {@link TweetPoll} service support.
 * @author Morales, Diana Paola paola AT encuestame.org
 * @since  April 02, 2010
 */
@Service
@Transactional
public class TweetPollService extends AbstractSurveyService implements ITweetPollService {

    /**
     * Log.
     */
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Get Tweet Polls by User Id.
     * @param username username.
     * @return list of Tweet polls bean
     * @throws EnMeNoResultsFoundException
     */
    public List<TweetPollBean> getTweetsPollsByUserName(
            final String username,
            final HttpServletRequest httpServletRequest,
            final TweetPollSearchBean tpollSearch)
            throws EnMeNoResultsFoundException {
        log.debug("tweetPoll username: " + username);
        List<TweetPoll> tweetpollsSearchResult = new ArrayList<TweetPoll>();
        final List<TweetPoll> tweetPolls = getTweetPollDao().retrieveTweetsByUserId(tpollSearch.getKeyword(), getUserAccountId(username),
                        tpollSearch.getMax(), tpollSearch.getStart(),
                        tpollSearch.getIsComplete(),
                        tpollSearch.getIsScheduled(),
                        tpollSearch.getIsPublished(),
                        tpollSearch.getIsFavourite(),
                        tpollSearch.getPeriod());
        tweetpollsSearchResult = this.getTweetPollSearchResult(tweetPolls, tpollSearch.getProviders(), tpollSearch.getSocialAccounts());
        log.info("tweetPoll size: " + tweetpollsSearchResult.size());
        return this.setTweetPollListAnswers(tweetpollsSearchResult, Boolean.TRUE, httpServletRequest);
    }

    /**
     *
     * @param username
     * @param httpServletRequest
     * @param tpollSearch
     * @return
     * @throws EnMeNoResultsFoundException
     */
    public List<SearchBean> getTweetsPollsByUserNameSearch(
            final String username,
            final HttpServletRequest httpServletRequest,
            final TweetPollSearchBean tpollSearch)
            throws EnMeNoResultsFoundException {
        log.debug("tweetPoll username: " + username);
        List<TweetPoll> tweetpollsSearchResult = new ArrayList<TweetPoll>();
        final List<TweetPoll> tweetPolls = getTweetPollDao().retrieveTweetsByUserId(tpollSearch.getKeyword(), getUserAccountId(username),
                        tpollSearch.getMax(), tpollSearch.getStart(),
                        tpollSearch.getIsComplete(),
                        tpollSearch.getIsScheduled(),
                        tpollSearch.getIsPublished(),
                        tpollSearch.getIsFavourite(),
                        tpollSearch.getPeriod());
        tweetpollsSearchResult = this.getTweetPollSearchResult(tweetPolls, tpollSearch.getProviders(), tpollSearch.getSocialAccounts());
        log.info("tweetPoll size: " + tweetpollsSearchResult.size());
        return this.setTweetPollListAnswersSearch(tweetpollsSearchResult, Boolean.TRUE, httpServletRequest);
    }


    /*
     * (non-Javadoc)
     *
     * @see org.encuestame.core.service.imp.ITweetPollService#
     * filterTweetPollByItemsByType
     * (org.encuestame.utils.web.search.TweetPollSearchBean,
     * javax.servlet.http.HttpServletRequest)
     */
//    public List<TweetPollBean> filterTweetPollByItemsByType(
//            final TweetPollSearchBean tpollSearch,
//            final HttpServletRequest httpServletRequest)
//            throws EnMeNoResultsFoundException, EnMeExpcetion {
//        log.info("filterTweetPollByItemsByType typeSearch: "+tpollSearch.getTypeSearch());
//        log.info("filterTweetPollByItemsByType keyword: "+ tpollSearch.getKeyword());
//        log.info("filterTweetPollByItemsByType max: "+ tpollSearch.getMax());
//        log.info("filterTweetPollByItemsByType start: "+ tpollSearch.getStart());
//        final List<TweetPollBean> list = new ArrayList<TweetPollBean>();
////        if (TypeSearch.KEYWORD.equals(tpollSearch.getTypeSearch())) {
////            list.addAll(this.searchTweetsPollsByKeyWord(getUserPrincipalUsername(),
////                    tpollSearch.getKeyword(), httpServletRequest, tpollSearch));
////        } else
//        if (TypeSearch.BYOWNER.equals(tpollSearch.getTypeSearch())) {
//            list.addAll(this.getTweetsPollsByUserName(getUserPrincipalUsername(), httpServletRequest, tpollSearch));
//        } else if (TypeSearch.ALL.equals(tpollSearch.getTypeSearch())) {
//            //TODO: this method return only the tweetpoll by owner.
//            list.addAll(this.getTweetsPollsByUserName(getUserPrincipalUsername(),
//                            httpServletRequest, tpollSearch));
//        } else if (TypeSearch.LASTDAY.equals(tpollSearch.getTypeSearch())) {
//            list.addAll(this.searchTweetsPollsToday(getUserPrincipalUsername(),
//                    httpServletRequest, tpollSearch));
//        } else if (TypeSearch.LASTWEEK.equals(tpollSearch.getTypeSearch())) {
//            list.addAll(this.searchTweetsPollsLastWeek(
//                    getUserPrincipalUsername(), httpServletRequest, tpollSearch));
//        } else if (TypeSearch.FAVOURITES.equals(tpollSearch.getTypeSearch())) {
//            list.addAll(this.searchTweetsPollFavourites(
//                    getUserPrincipalUsername(), httpServletRequest, tpollSearch));
//        } else if (TypeSearch.SCHEDULED.equals(tpollSearch.getTypeSearch())) {
//            list.addAll(this.searchTweetsPollScheduled(
//                    getUserPrincipalUsername(), httpServletRequest, tpollSearch));
//        } else {
//            log.error("filterTweetPollByItemsByType no type");
//            throw new EnMeExpcetion("filterTweetPollByItemsByType no type");
//        }
//        log.info("filterTweetPollByItemsByType list: "+list.size());
//        return list;
//    }

    public List<SearchBean> filterTweetPollByItemsByTypeSearch(
            final TweetPollSearchBean tpollSearch,
            final HttpServletRequest httpServletRequest)
            throws EnMeNoResultsFoundException, EnMeExpcetion {
        log.info("filterTweetPollByItemsByType typeSearch: "+tpollSearch.getTypeSearch());
        log.info("filterTweetPollByItemsByType keyword: "+ tpollSearch.getKeyword());
        log.info("filterTweetPollByItemsByType max: "+ tpollSearch.getMax());
        log.info("filterTweetPollByItemsByType start: "+ tpollSearch.getStart());
        final List<SearchBean> list = new ArrayList<SearchBean>();
        //TODO: why this code is commented?
//        if (TypeSearch.KEYWORD.equals(tpollSearch.getTypeSearch())) {
//            list.addAll(this.searchTweetsPollsByKeyWord(getUserPrincipalUsername(),
//                    tpollSearch.getKeyword(), httpServletRequest, tpollSearch));
//        } else
        if (TypeSearch.BYOWNER.equals(tpollSearch.getTypeSearch())) {
            list.addAll(this.getTweetsPollsByUserNameSearch(getUserPrincipalUsername(), httpServletRequest, tpollSearch));
        } else if (TypeSearch.ALL.equals(tpollSearch.getTypeSearch())) {
             //TODO: this method return only the tweetpoll by owner. WHY return the same data of BYOWNER, should be return all tweetpoll???
             list.addAll(this.getTweetsPollsByUserNameSearch(getUserPrincipalUsername(),
                             httpServletRequest, tpollSearch));
        } else if (TypeSearch.LASTDAY.equals(tpollSearch.getTypeSearch())) {
            list.addAll(this.searchTweetsPollsToday(getUserPrincipalUsername(),
                    httpServletRequest, tpollSearch));
        } else if (TypeSearch.LASTWEEK.equals(tpollSearch.getTypeSearch())) {
            list.addAll(this.searchTweetsPollsLastWeek(
                    getUserPrincipalUsername(), httpServletRequest, tpollSearch));
        } else if (TypeSearch.FAVOURITES.equals(tpollSearch.getTypeSearch())) {
            list.addAll(this.searchTweetsPollFavourites(
                    getUserPrincipalUsername(), httpServletRequest, tpollSearch));
        } else if (TypeSearch.SCHEDULED.equals(tpollSearch.getTypeSearch())) {
            list.addAll(this.searchTweetsPollScheduled(
                    getUserPrincipalUsername(), httpServletRequest, tpollSearch));
        } else {
            log.error("filterTweetPollByItemsByTypeSEARCH no type");
            throw new EnMeExpcetion("filterTweetPollByItemsByType SEARCH no type");
        }
        log.info("filterTweetPollByItemsByType list: "+list.size());
        return list;
    }


    /**
     * Set List Answer.
     * @param listTweetPolls List of {@link TweetPoll}
     * @return
     * @throws EnMeExpcetion
     */
    public List<TweetPollBean> setTweetPollListAnswers(
            final List<TweetPoll> listTweetPolls,
            final Boolean results,
            final HttpServletRequest httpServletRequest){
        final List<TweetPollBean> tweetPollsBean = new ArrayList<TweetPollBean>();
        for (TweetPoll tweetPoll : listTweetPolls) {
             final List<TweetPollSwitch> answers = this.getTweetPollSwitch(tweetPoll);
             final TweetPollBean unitTweetPoll = ConvertDomainBean.convertTweetPollToBean(tweetPoll);
             final List<TweetPollAnswerSwitchBean> listSwitchs = new ArrayList<TweetPollAnswerSwitchBean>();
             if (results) {
                 final List<TweetPollResultsBean> list = new ArrayList<TweetPollResultsBean>();
                 for (TweetPollSwitch tweetPollSwitch : answers) {
                     final TweetPollAnswerSwitchBean answerResults = ConvertDomainBean.convertTweetPollSwitchToBean(tweetPollSwitch, httpServletRequest);
                     final TweetPollResultsBean rBean = this.getVotesByTweetPollAnswerId(tweetPoll.getTweetPollId(), tweetPollSwitch.getAnswers());
                     answerResults.setResultsBean(rBean);
                     list.add(rBean);
                     listSwitchs.add(answerResults);
                }
                 this.calculatePercents(list);
             }
             unitTweetPoll.setAnswerSwitchBeans(listSwitchs);
             tweetPollsBean.add(unitTweetPoll);
        }
        return tweetPollsBean;
    }

    /**
     * Create a list of {@link SearchBean} based on a List of {@link TweetPoll}
     * @param listTweetPolls array of {@link TweetPoll}
     * @param results define if the list will have the results for each {@link TweetPoll}
     * @param httpServletRequest {@link HttpServletRequest} reference
     * @return array of {@link SearchBean}
     */
    private List<SearchBean> setTweetPollListAnswersSearch(
            final List<TweetPoll> listTweetPolls,
            final Boolean results,
            final HttpServletRequest httpServletRequest){
        final List<SearchBean> tweetPollsBean = new ArrayList<SearchBean>();
        for (TweetPoll tweetPoll : listTweetPolls) {
             final List<TweetPollSwitch> answers = this.getTweetPollSwitch(tweetPoll);
             // Convertir Tweetpoll a SearchTweetpollBean
             final SearchBean unitTweetPoll = ConvertDomainBean.convertTweetPollToSearchBean(tweetPoll);
             final List<TweetPollAnswerSwitchBean> listSwitchs = new ArrayList<TweetPollAnswerSwitchBean>();
             if (results) {
                 final List<TweetPollResultsBean> list = new ArrayList<TweetPollResultsBean>();
                 for (TweetPollSwitch tweetPollSwitch : answers) {
                     final TweetPollAnswerSwitchBean answerResults = ConvertDomainBean.convertTweetPollSwitchToBean(tweetPollSwitch, httpServletRequest);
                     final TweetPollResultsBean rBean = this.getVotesByTweetPollAnswerId(tweetPoll.getTweetPollId(), tweetPollSwitch.getAnswers());
                     answerResults.setResultsBean(rBean);
                     list.add(rBean);
                     listSwitchs.add(answerResults);
                }
                 this.calculatePercents(list);
             }
             unitTweetPoll.setAnswerSwitchBeans(listSwitchs);
             tweetPollsBean.add(unitTweetPoll);
        }
        return tweetPollsBean;
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#searchTweetsPollsByKeyWord
     * (java.lang.String, java.lang.String, java.lang.Integer,
     * java.lang.Integer, javax.servlet.http.HttpServletRequest,
     * org.encuestame.utils.web.search.TweetPollSearchBean)
     */
    @Deprecated
    public List<TweetPollBean> searchTweetsPollsByKeyWord(
                               final String username,
                               final String keyword,
                               final HttpServletRequest httpServletRequest,
                               final TweetPollSearchBean tpollSearch) throws EnMeExpcetion{
         log.info("search keyword tweetPoll  "+keyword);
         List<TweetPoll> tweetPolls  = new ArrayList<TweetPoll>();
         List<TweetPoll> tpollsbysocialNetwork = new ArrayList<TweetPoll>();

         if (keyword == null) {
             throw new EnMeExpcetion("keyword is missing");
         } else {
             //TODO: migrate search to Hibernate Search.
             tweetPolls = getTweetPollDao().retrieveTweetsByQuestionName(tpollSearch.getKeyword(), getUserAccountId(username), tpollSearch.getMax(),
                     tpollSearch.getStart(), tpollSearch.getIsComplete(),
                     tpollSearch.getIsScheduled(),
                     tpollSearch.getIsFavourite(),
                     tpollSearch.getIsPublished(), tpollSearch.getPeriod());
             /*
              * 1- Iterate Tweetpoll list retrieved by questionName
              * 2- For every tweetpoll verify if published in a social network through social network list.
              * 3- If the value returned by the search of publications on social networks eses greater than 0
              * 4- Tweetpoll add to the list of search results
              */
             tpollsbysocialNetwork = this.getTweetPollSearchResult(tweetPolls, tpollSearch.getProviders(), tpollSearch.getSocialAccounts());
         }
         log.info("search keyword tweetPoll size "+tweetPolls.size());
         return this.setTweetPollListAnswers(tpollsbysocialNetwork, Boolean.TRUE, httpServletRequest);
     }

    /**
     *
     * @param tweetPolls
     * @param socialNetworks
     * @param socialAccounts
     * @return
     * @throws EnMeNoResultsFoundException
     */
    private List<TweetPoll> getTweetPollSearchResult(
            final List<TweetPoll> tweetPolls,
            final List<SocialProvider> socialNetworks,
            final List<Long> socialAccounts) throws EnMeNoResultsFoundException {
        List<TweetPoll> tpollsbysocialNetwork = new ArrayList<TweetPoll>();

        if ((socialNetworks.size() > 0) || (socialAccounts.size() > 0)) {
            tpollsbysocialNetwork = this.retrieveTweetPollsPostedOnSocialNetworks(tweetPolls, socialNetworks, socialAccounts);
        } else {
            tpollsbysocialNetwork = tweetPolls;
        }
        log.info("tweetPoll size: " + tweetPolls.size());
        return tpollsbysocialNetwork;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#searchTweetsPollsToday
     * (java.lang.String, java.lang.Integer, java.lang.Integer,
     * javax.servlet.http.HttpServletRequest,
     * org.encuestame.utils.web.search.TweetPollSearchBean)
     */
    public List<SearchBean> searchTweetsPollsToday(
            final String username,
            final HttpServletRequest httpServletRequest, final TweetPollSearchBean tpollSearch) throws EnMeExpcetion{
        List<TweetPoll> tweetPollSearchResult = new ArrayList<TweetPoll>();
        final List<TweetPoll> tpolls = getTweetPollDao().retrieveTweetPollToday(
                        getAccount(username), tpollSearch.getMax(),
                        tpollSearch.getStart(), tpollSearch.getIsComplete(),
                        tpollSearch.getIsScheduled(),
                        tpollSearch.getIsFavourite(),
                        tpollSearch.getIsPublished(), tpollSearch.getKeyword(), tpollSearch.getPeriod());
        tweetPollSearchResult = this.getTweetPollSearchResult(tpolls, tpollSearch.getProviders(), tpollSearch.getSocialAccounts());
        return this.setTweetPollListAnswersSearch(tweetPollSearchResult, Boolean.TRUE, httpServletRequest);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#searchTweetsPollsLastWeek
     * (java.lang.String, java.lang.Integer, java.lang.Integer,
     * javax.servlet.http.HttpServletRequest,
     * org.encuestame.utils.web.search.TweetPollSearchBean)
     */
    public List<SearchBean> searchTweetsPollsLastWeek(final String username,
            final HttpServletRequest httpServletRequest,
            final TweetPollSearchBean tpollSearch) throws EnMeExpcetion {
        List<TweetPoll> tweetPollSearchResult = new ArrayList<TweetPoll>();
        final List<TweetPoll> tweetPolls = getTweetPollDao()
                .retrieveTweetPollLastWeek(getAccount(username), tpollSearch.getMax(),
                        tpollSearch.getStart(), tpollSearch.getIsComplete(),
                        tpollSearch.getIsScheduled(),
                        tpollSearch.getIsFavourite(),
                        tpollSearch.getIsPublished(), tpollSearch.getKeyword(),
                        tpollSearch.getPeriod());
        tweetPollSearchResult = this.getTweetPollSearchResult(tweetPolls,
                tpollSearch.getProviders(), tpollSearch.getSocialAccounts());
        return this.setTweetPollListAnswersSearch(tweetPollSearchResult, Boolean.TRUE,
                httpServletRequest);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#searchTweetsPollFavourites
     * (java.lang.String, java.lang.Integer, java.lang.Integer,
     * javax.servlet.http.HttpServletRequest,
     * org.encuestame.utils.web.search.TweetPollSearchBean)
     */
    public List<SearchBean> searchTweetsPollFavourites(
            final String username, final HttpServletRequest httpServletRequest,
            final TweetPollSearchBean tpollSearch) throws EnMeExpcetion {
        List<TweetPoll> tweetPollSearchResult = new ArrayList<TweetPoll>();
        final List<TweetPoll> favouriteTweetPolls = getTweetPollDao().retrieveFavouritesTweetPoll(
                getAccount(username), tpollSearch.getMax(),
                tpollSearch.getStart(), tpollSearch.getIsComplete(),
                tpollSearch.getIsScheduled(),
                tpollSearch.getIsFavourite(),
                tpollSearch.getIsPublished(), tpollSearch.getKeyword(), tpollSearch.getPeriod());
        tweetPollSearchResult = this.getTweetPollSearchResult(
                favouriteTweetPolls, tpollSearch.getProviders(),
                tpollSearch.getSocialAccounts());
        return this.setTweetPollListAnswersSearch(tweetPollSearchResult, Boolean.TRUE, httpServletRequest);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#searchTweetsPollScheduled
     * (java.lang.String, java.lang.Integer, java.lang.Integer,
     * javax.servlet.http.HttpServletRequest,
     * org.encuestame.utils.web.search.TweetPollSearchBean)
     */
    public List<SearchBean> searchTweetsPollScheduled(final String username,
            final HttpServletRequest httpServletRequest,
            final TweetPollSearchBean tpollSearch) throws EnMeExpcetion {
        if (tpollSearch.getIsScheduled() == null || !tpollSearch.getIsScheduled()) {
            tpollSearch.setIsScheduled(Boolean.TRUE); //must be true
        }
        List<TweetPoll> tweetPollSearchResult = new ArrayList<TweetPoll>();
        final List<TweetPoll> tweetPolls = getTweetPollDao()
                .retrieveScheduledTweetPoll(getUserAccountId(username),
                        tpollSearch.getMax(), tpollSearch.getStart(), tpollSearch.getIsComplete(),
                        tpollSearch.getIsScheduled(),
                        tpollSearch.getIsFavourite(),
                        tpollSearch.getIsPublished(), tpollSearch.getKeyword(),
                        tpollSearch.getPeriod());
        tweetPollSearchResult = this.getTweetPollSearchResult(tweetPolls, tpollSearch.getProviders(), tpollSearch.getSocialAccounts());
        return this.setTweetPollListAnswersSearch(tweetPollSearchResult, Boolean.TRUE,
                httpServletRequest);
    }

    /**
     * Create tweetPoll.
     * @param tweetPollBean {@link TweetPollBean}
     * @param question {@link Question}.
     * @return
     */
    private TweetPoll newTweetPoll(final TweetPollBean tweetPollBean, Question question){
        final TweetPoll tweetPollDomain = new TweetPoll();
        log.debug(tweetPollBean.toString());
        tweetPollDomain.setQuestion(question);
        tweetPollDomain.setCloseNotification(tweetPollBean.getCloseNotification());
        tweetPollDomain.setCompleted(Boolean.FALSE);
        tweetPollDomain.setCaptcha(tweetPollBean.getCaptcha());
        tweetPollDomain.setAllowLiveResults(tweetPollBean.getAllowLiveResults());
        tweetPollDomain.setLimitVotes(tweetPollBean.getLimitVotes());
        tweetPollDomain.setLimitVotesEnabled((tweetPollBean.getLimitVotesEnabled()));
        UserAccount acc = null;
        try {
            acc = getUserAccount(getUserPrincipalUsername());
        } catch (EnMeNoResultsFoundException e) {
           log.error("User not found");
        }
        tweetPollDomain.setTweetOwner(acc.getAccount());
        tweetPollDomain.setEditorOwner(getUserAccountonSecurityContext());
        tweetPollDomain.setResultNotification(tweetPollBean.getResultNotification());
        tweetPollDomain.setPublishTweetPoll(Boolean.FALSE);
        tweetPollDomain.setRelevance(EnMeUtils.RATE_DEFAULT);
        tweetPollDomain.setHits(EnMeUtils.VOTE_DEFAULT);
        tweetPollDomain.setLikeVote(EnMeUtils.LIKE_DEFAULT);
        tweetPollDomain.setNumbervotes(EnMeUtils.VOTE_DEFAULT);
        tweetPollDomain.setDislikeVote(EnMeUtils.DISLIKE_DEFAULT);
        tweetPollDomain.setCreateDate(Calendar.getInstance().getTime());
        tweetPollDomain.setScheduleTweetPoll(tweetPollBean.getSchedule());
        tweetPollDomain.setScheduleDate(tweetPollBean.getScheduleDate());
        tweetPollDomain.setUpdatedDate(Calendar.getInstance().getTime());
        tweetPollDomain.setDateLimit((tweetPollBean.getLimitVotesDate() == null ? true : tweetPollBean.getLimitVotesDate()));
        final Calendar limit = Calendar.getInstance();
        limit.add(Calendar.DAY_OF_YEAR, 7); //TODO: define when is the default limit for each tweetpoll.
        tweetPollDomain.setDateLimited(limit.getTime());
        this.getTweetPollDao().saveOrUpdate(tweetPollDomain);
        return tweetPollDomain;
    }

    /**
     * Create new question with answers.
     * @param questionName
     * @param answers
     * @param user
     * @return
     * @throws EnMeExpcetion
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public Question createTweetPollQuestion(
            final String questionName,
            final UserAccount user) throws EnMeExpcetion, NoSuchAlgorithmException, UnsupportedEncodingException{
        final QuestionBean questionBean = new QuestionBean();
        questionBean.setQuestionName(questionName);
        questionBean.setUserId(user.getUid());
        final Question questionDomain = createQuestion(questionBean, user, QuestionPattern.LINKS);
        return questionDomain;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#createTweetPoll(org.encuestame.utils.web.TweetPollBean, java.lang.String, java.lang.String[], java.lang.String[], org.encuestame.persistence.domain.security.UserAccount)
     */
    public TweetPoll createTweetPoll(
            final TweetPollBean tweetPollBean,
            final String questionName,
            final UserAccount user,
            final HttpServletRequest request) throws EnMeExpcetion {
        try{
            final Question question = createTweetPollQuestion(questionName, user);
            log.debug("question found:{" + question);
            if (question == null) {
                throw new EnMeNoResultsFoundException("question not found");
            } else {
                final TweetPoll tweetPollDomain = newTweetPoll(tweetPollBean, question);
                //save Hash tags for this tweetPoll.
                log.debug("HashTag size:{"+tweetPollBean.getHashTags().size());
                //update TweetPoll.
                if (tweetPollBean.getHashTags().size() > 0) {
                    tweetPollDomain.getHashTags().addAll(retrieveListOfHashTags(tweetPollBean.getHashTags()));
                    log.debug("Update Hash Tag");
                    getTweetPollDao().saveOrUpdate(tweetPollDomain);
                }
                //update tweetpoll switch support
                this.updateTweetPollSwitchSupport(tweetPollDomain, request);
                return tweetPollDomain;
            }
        } catch (Exception e) {
            log.error("Error creating TweetlPoll:{"+e);
            throw new EnMeExpcetion(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#updateTweetPoll(org.encuestame.persistence.domain.tweetpoll.TweetPoll, java.lang.String[], java.util.List)
     */
    public TweetPoll updateTweetPoll(final TweetPollBean tweetPollBean) throws EnMeNoResultsFoundException {
        log.debug("Updated tweetpoll with id :"+tweetPollBean.getId());
        final TweetPoll tweetPoll = getTweetPoll(tweetPollBean.getId(), getUserPrincipalUsername());
        Assert.notNull(tweetPoll);
        if (tweetPoll == null) {
            throw new EnMeTweetPollNotFoundException();
        }
        //TODO: disabled to create hashtag directly from own service.
        //final List<HashTag> newList = retrieveListOfHashTags(tweetPollBean.getHashTags());
        //log.debug("new list of hashtags size: "+newList.size());

        //update question name.
        final Question questionDomain = tweetPoll.getQuestion();
        Assert.notNull(questionDomain);
        questionDomain.setQuestion(tweetPollBean.getQuestionBean().getQuestionName());
        questionDomain.setSlugQuestion(RestFullUtil.slugify(tweetPollBean.getQuestionBean().getQuestionName()));
        questionDomain.setCreateDate(Calendar.getInstance().getTime());
        getQuestionDao().saveOrUpdate(questionDomain);

        //update hashtags.
        //TODO: disabled to create hashtag directly from own service.
        //tweetPoll.getHashTags().addAll(retrieveListOfHashTags(tweetPollBean.getHashTags())); //TODO check if this action remove old hashtags.

        //update options.
        tweetPoll.setAllowLiveResults(tweetPollBean.getAllowLiveResults());
        tweetPoll.setAllowRepatedVotes(tweetPollBean.getAllowRepeatedVotes());
        tweetPoll.setCaptcha(tweetPollBean.getCaptcha());
        tweetPoll.setCloseNotification(tweetPollBean.getCloseNotification());
        tweetPoll.setLimitVotes(tweetPollBean.getLimitVotes());
        tweetPoll.setLimitVotesEnabled(tweetPollBean.getLimitVotesEnabled());
        tweetPoll.setMaxRepeatedVotes(tweetPollBean.getMaxRepeatedVotes());
        tweetPoll.setResultNotification(tweetPollBean.getResultNotification());
        tweetPoll.setResumeLiveResults(tweetPollBean.getResumeLiveResults());
        tweetPoll.setScheduleDate(tweetPollBean.getScheduleDate());
        tweetPoll.setResumeTweetPollDashBoard(tweetPollBean.getResumeTweetPollDashBoard());
        tweetPoll.setUpdatedDate(Calendar.getInstance().getTime());
        getTweetPollDao().saveOrUpdate(tweetPoll);
        log.debug("removing answers for tweetpoll id: "+tweetPoll.getTweetPollId());

        /*
         * answer auto-save handler.
         */
        //no make sense remove all questions. disabled.
        //this.removeAllQuestionsAnswers(tweetPoll);

        //create new answers.
//        for (int i = 0; i < answers.length; i++) {
//            log.debug("Creating new answer:{ "+answers[i].toString());
//            log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//            //createQuestionAnswer(new QuestionAnswerBean(answers[i]), tweetPoll.getQuestion());
//            log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        }
        log.debug("update switchs question.");
        //update switchs question..
        //updateTweetPollSwitchSupport(tweetPoll);
        return tweetPoll;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#removeAllQuestionsAnswers(org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
   /* public void removeAllQuestionsAnswers(final TweetPoll tweetPoll){
        final Question question = tweetPoll.getQuestion();
        final Set<QuestionAnswer> currentQuestionAnswers = question.getQuestionsAnswers();
        //removing old answers.
        for (QuestionAnswer questionAnswer : currentQuestionAnswers) {
            this.removeQuestionAnswer(questionAnswer);
        }
        getQuestionDao().saveOrUpdate(question);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#removeQuestionAnswer(org.encuestame.persistence.domain.question.QuestionAnswer)
     */
    public void removeQuestionAnswer(final QuestionAnswer questionAnswer) {
        //removing old data.
        final List<TweetPollSwitch> list = getTweetPollDao().getAnswerTweetSwitch(questionAnswer);
        log.debug("removeQuestionAnswer switch size:"+list.size());
        for (TweetPollSwitch tweetPollSwitch : list) {
             getTweetPollDao().delete(tweetPollSwitch);
        }
        log.debug("removing answer:{"+questionAnswer.getQuestionAnswerId());
        getQuestionDao().delete(questionAnswer); //remove answer.
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#createTweetPollQuestionAnswer(org.encuestame.utils.web.QuestionAnswerBean, org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
    public TweetPollSwitch createTweetPollQuestionAnswer(
            final QuestionAnswerBean answerBean,
            final TweetPoll tp,
            final HttpServletRequest request)
            throws EnMeNoResultsFoundException {
        final Question question = tp.getQuestion();
        //create answer
        final QuestionAnswer questionAnswer = createQuestionAnswer(answerBean,
                question);
        if (questionAnswer == null) {
            throw new EnMeNoResultsFoundException("answer is missing");
        } else {
            //create tweet poll switch with tp and new answer.
            log.debug("createTweetPollQuestionAnswer: short url provider:{ "+questionAnswer.getProvider());
            final TweetPollSwitch tpSwitch = this.createTweetPollSwitch(tp, questionAnswer, request);
            return tpSwitch;
        }
    }

   /*
    * (non-Javadoc)
    * @see org.encuestame.business.service.imp.ITweetPollService#getTweetPollSwitch(org.encuestame.persistence.domain.tweetpoll.TweetPoll)
    */
    public List<TweetPollSwitch> getTweetPollSwitch(final TweetPoll tweetPoll){
        return getTweetPollDao().getListAnswesByTweetPoll(tweetPoll);
    }

    /**
     * Generate TweetPoll Text.
     * @param tweetPoll tweetPoll
     * @param url url
     * @return tweet text
     * @throws EnMeExpcetion exception
     */
    public String generateTweetPollContent(final TweetPoll tweetPollDomain) throws EnMeExpcetion{
        String tweetQuestionText = "";
        try{
            if (log.isDebugEnabled()) {
                log.debug("generateTweetPollText");
                log.debug("TweetPoll ID: "+tweetPollDomain.getTweetPollId());
            }
            tweetQuestionText = tweetPollDomain.getQuestion().getQuestion();
            log.debug("Question text: "+tweetQuestionText);
            final List<TweetPollSwitch> tweetPollSwitchs = getTweetPollSwitch(tweetPollDomain);
            log.debug("generateTweetPollText tweetPollSwitchs:{ "+tweetPollSwitchs.size());
            final StringBuilder builder = new StringBuilder(tweetQuestionText);

            for (final TweetPollSwitch tpswitch : tweetPollSwitchs) {
                 final QuestionAnswer questionsAnswers = tpswitch.getAnswers();
                 log.debug("Answer ID: "+questionsAnswers.getQuestionAnswerId());
                 log.debug("Answer Question: "+questionsAnswers.getAnswer());
                 builder.append(" ");
                 builder.append(questionsAnswers.getAnswer());
                 builder.append(" ");
                 builder.append(tpswitch.getShortUrl());
            }

            // build Hash Tag.
            for (final HashTag tag : tweetPollDomain.getHashTags()) {
                if (log.isDebugEnabled()) {
                     log.debug("Hash Tag ID: "+tag.getHashTagId());
                     log.debug("Tag Name "+tag.getHashTag());
                }
                 builder.append(EnMeUtils.SPACE);
                 builder.append(EnMeUtils.HASH);
                 builder.append(tag.getHashTag());
            }
            tweetQuestionText = builder.toString();
        } catch (Exception e) {
            throw new EnMeExpcetion(e);
        }
        log.debug("Tweet Text Generated: "+tweetQuestionText);
        log.debug("Tweet Text Generated: "+tweetQuestionText.length());
        if (tweetQuestionText.length() > SocialUtils.TWITTER_LIMIT) {
            throw new EnMeFailSendSocialTweetException("tweet exceed the maximun allowed");
        }
        return tweetQuestionText;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#publishMultiplesOnSocialAccounts(java.util.List, java.lang.Long, java.lang.String)
     */
    public List<TweetPollSavedPublishedStatus> publishMultiplesOnSocialAccounts(
            final List<SocialAccountBean> twitterAccounts,
            final TweetPoll tweetPoll,
            final String tweetText, final TypeSearchResult type, final Poll poll, final Survey survey){
            log.debug("publicMultiplesTweetAccounts:{"+twitterAccounts.size());
            final List<TweetPollSavedPublishedStatus> results = new ArrayList<TweetPollSavedPublishedStatus>();
            for (SocialAccountBean unitTwitterAccountBean : twitterAccounts) {
                log.debug("publicMultiplesTweetAccounts unitTwitterAccountBean:{ "+unitTwitterAccountBean.toString());
            results.add(this.publishTweetBySocialAccountId(
                    unitTwitterAccountBean.getAccountId(), tweetPoll,
                    tweetText, type, poll, survey));
            }
            return results;
    }

    /**
     *
     * @param tweetPoll
     * @throws EnMeNoResultsFoundException
     */
    @Deprecated
    public void createTweetPollNotification(final TweetPoll tweetPoll) throws EnMeNoResultsFoundException {
        createNotification(NotificationEnum.TWEETPOLL_PUBLISHED,
                tweetPoll.getQuestion().getQuestion(),
                this.createTweetPollUrlAccess(tweetPoll), false);

    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#createTweetPollNotification(org.encuestame.persistence.domain.tweetpoll.TweetPollSavedPublishedStatus)
     */
      public void createTweetPollNotification(final TweetPollSavedPublishedStatus tweetPollPublished) throws EnMeNoResultsFoundException {
        createNotification(NotificationEnum.TWEETPOLL_PUBLISHED,
                tweetPollPublished.getTweetPoll().getQuestion().getQuestion(),
                SocialUtils.getSocialTweetPublishedUrl(
                        tweetPollPublished.getTweetId(), tweetPollPublished
                                .getTweetPoll().getEditorOwner().getUsername(),
                        tweetPollPublished.getApiType()), false);
    }

    /**
     * Create url to acces to tweetPoll.
     * format tweetpoll/932/test
     * @param tweetPoll
     * @return
     */
    private String createTweetPollUrlAccess(final TweetPoll tweetPoll){
        final StringBuilder builder = new StringBuilder("/tweetpoll/");
        builder.append(tweetPoll.getTweetPollId());
        builder.append("/");
        builder.append(tweetPoll.getQuestion().getSlugQuestion());
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#publishTweetPoll(java.lang.Long, org.encuestame.persistence.domain.tweetpoll.TweetPoll, org.encuestame.persistence.domain.social.SocialProvider)
     */
    @SuppressWarnings("unused")
    public TweetPollSavedPublishedStatus publishTweetBySocialAccountId(
            final Long socialAccountId,
            final TweetPoll tweetPoll,
            final String tweetText,
            final TypeSearchResult type,
            final Poll poll,
            final Survey survey) {
         log.debug("publicMultiplesTweetAccounts tweetPoll" + tweetPoll);
        //get social account
         final SocialAccount socialAccount = getAccountDao().getSocialAccountById(socialAccountId);
         Set<HashTag> hashTags = new HashSet<HashTag>();
         log.debug("publishTweetPoll socialTwitterAccounts: {"+socialAccount);
         //create tweet status
         final TweetPollSavedPublishedStatus publishedStatus = new TweetPollSavedPublishedStatus();
         //social provider.
         publishedStatus.setApiType(socialAccount.getAccounType());
         //checking required values.
         if (type.equals(TypeSearchResult.TWEETPOLL)) {
            //adding tweetpoll
             publishedStatus.setTweetPoll(tweetPoll);
             hashTags = tweetPoll.getHashTags();
         } else if(type.equals(TypeSearchResult.POLL)) {
            //adding tweetpoll
             publishedStatus.setPoll(poll);
             hashTags = poll.getHashTags();
         } else if(type.equals(TypeSearchResult.SURVEY)) {
             publishedStatus.setSurvey(survey);
             hashTags = survey.getHashTags();
         } else {
             log.error("Type not defined");
         }

         if (socialAccount != null) {
             log.debug("socialAccount Account NAME:{"+socialAccount.getSocialAccountName());
             //adding social account
             publishedStatus.setSocialAccount(socialAccount);
             try {
                 log.debug("publishTweetPoll Publishing... "+tweetText.length());
                 final TweetPublishedMetadata metadata = publicTweetPoll(tweetText, socialAccount, hashTags);
                 if (metadata == null || metadata.getTweetId() == null) {
                     throw new EnMeFailSendSocialTweetException("status not valid");
                 }//getMessageProperties(propertieId)
                 if (metadata.getTweetId() == null) {
                     log.warn("tweet id is empty");
                 }
                 //store original tweet id.
                 publishedStatus.setTweetId(metadata.getTweetId());
                 //store original publication date.
                 publishedStatus.setPublicationDateTweet(metadata.getDatePublished());
                 //success publish state..
                 publishedStatus.setStatus(Status.SUCCESS);
                 //store original tweet content.
                 publishedStatus.setTweetContent(metadata.getTextTweeted());
                 //create notification
                 //createNotification(NotificationEnum.TWEETPOLL_PUBLISHED, "tweet published", socialAccount.getAccount());
                 createNotification(NotificationEnum.SOCIAL_MESSAGE_PUBLISHED, tweetText, SocialUtils.getSocialTweetPublishedUrl(
                         metadata.getTweetId(), socialAccount.getSocialAccountName(), socialAccount.getAccounType()), Boolean.TRUE);
             } catch (Exception e) {
                 //e.printStackTrace();
                 log.error("Error publish tweet:{"+e);
                 //change status to failed
                 publishedStatus.setStatus(Status.FAILED);
                 //store error descrition
                 if (e.getMessage() != null && e.getMessage().isEmpty()) {
                     publishedStatus.setDescriptionStatus(e.getMessage().substring(254)); //limited to 254 characters.
                 } else {
                     publishedStatus.setDescriptionStatus("");
                 }
                 //save original tweet content.
                 publishedStatus.setTweetContent(tweetText);
             }
         } else {
             log.warn("Twitter Account Not Found [Id:"+socialAccountId+"]");
             publishedStatus.setStatus(Status.FAILED);
             //throw new EnMeFailSendSocialTweetException("Twitter Account Not Found [Id:"+accountId+"]");
             if(type.equals(TypeSearchResult.TWEETPOLL)){
                tweetPoll.setPublishTweetPoll(Boolean.FALSE);
                //getTweetPollDao().saveOrUpdate(tweetPoll);
            }

         }
         log.info("Publish Status Social :{------------>"+publishedStatus.toString());
         getTweetPollDao().saveOrUpdate(publishedStatus);
         return publishedStatus;
    }

   /*
    * (non-Javadoc)
    * @see org.encuestame.core.service.imp.ITweetPollService#createTweetPollScheduled(org.encuestame.utils.json.TweetPollScheduledBean)
    */
    public List<Schedule> createTweetPollScheduled(
            final TweetPollScheduledBean bean,
            final TypeSearchResult searchResult) throws EnMeExpcetion, EnMeNoResultsFoundException {
        final List<Schedule> list = new ArrayList<Schedule>();
        final TweetPoll tp = this.getTweetPollById(bean.getId());
        if (tp.getScheduleTweetPoll() != null || !tp.getScheduleTweetPoll()) {
            tp.setScheduleTweetPoll(Boolean.TRUE);
            getTweetPollDao().saveOrUpdate(tp);
        }
        final String tweetText = generateTweetPollContent(tp);
        for (SocialAccountBean socialBean : bean.getSocialAccounts()) {
            final SocialAccount soc = getAccountDao().getSocialAccountById(socialBean.getAccountId());
            final Schedule schedule = new Schedule();
            schedule.setScheduleDate(tp.getScheduleDate());
            schedule.setTpoll(tp);
            schedule.setTypeSearch(searchResult);
            schedule.setTweetText(tweetText);
            schedule.setSocialAccount(soc);
            schedule.setStatus(Status.FAILED);
            schedule.setPublishAttempts(0);
            schedule.setTpollSavedPublished(null);
            getTweetPollDao().saveOrUpdate(schedule);
            list.add(schedule);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#createScheduled(java.util.Date, org.encuestame.utils.enums.TypeSearchResult, org.encuestame.persistence.domain.tweetpoll.TweetPollSavedPublishedStatus)
     */
    public Schedule createTweetPollPublishedStatusScheduled(
            final Date scheduleDate,
            final TypeSearchResult typeSearch,
            final TweetPollSavedPublishedStatus tpollSaved) {
        final SocialAccount socialAccount = tpollSaved.getSocialAccount();
        final Schedule schedule = new Schedule();
        schedule.setScheduleDate(scheduleDate);
        if (typeSearch.equals(TypeSearchResult.TWEETPOLL)) {
            schedule.setTpoll(tpollSaved.getTweetPoll());
            schedule.setTypeSearch(TypeSearchResult.TWEETPOLL);
        } else if(typeSearch.equals(TypeSearchResult.POLL)) {
            schedule.setPoll(tpollSaved.getPoll());
            schedule.setTypeSearch(TypeSearchResult.POLL);
        } else if(typeSearch.equals(TypeSearchResult.SURVEY)) {
            schedule.setSurvey(tpollSaved.getSurvey());
            schedule.setTypeSearch(TypeSearchResult.SURVEY);
        }
        schedule.setTweetText(tpollSaved.getTweetContent());
        schedule.setSocialAccount(socialAccount);
        schedule.setStatus(Status.FAILED);
        schedule.setPublishAttempts(0);
        schedule.setTpollSavedPublished(tpollSaved);
        getTweetPollDao().saveOrUpdate(schedule);
        return schedule;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#tweetPollVote(org.encuestame.persistence.domain.tweetpoll.TweetPollSwitch, java.lang.String, java.util.Date)
     */
    public void tweetPollVote(final TweetPollSwitch pollSwitch, final String ip, final Date voteDate) {
        final TweetPollResult pollResult = new TweetPollResult();
        pollResult.setIpVote(ip.trim());
        pollResult.setTweetPollSwitch(pollSwitch);
        pollResult.setTweetResponseDate(voteDate);
        getTweetPollDao().saveOrUpdate(pollResult);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#getTweetPollPublishedById(java.lang.Long)
     */
    public TweetPoll getTweetPollPublishedById(final Long tweetPollId) throws EnMeNoResultsFoundException{
        final TweetPoll tweetPoll = getTweetPollDao().getPublicTweetPollById(tweetPollId);
        if (tweetPoll == null) {
            throw new EnMeNoResultsFoundException("tweetpoll [" + tweetPollId
                    + "] is not published");
        }
        return tweetPoll;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#retrieveTweetPollSavedPublished(org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
    public List<TweetPollSavedPublishedStatus> retrieveTweetPollSavedPublished(
            final TweetPoll tweetPoll) throws EnMeNoResultsFoundException {
        final List<TweetPollSavedPublishedStatus> tpollSaved = getTweetPollDao()
                .getAllLinks(tweetPoll, null, null, TypeSearchResult.TWEETPOLL);
        if (tpollSaved.size() == 0) {
            throw new EnMeNoResultsFoundException("tweetpoll saved published["
                    + tweetPoll + "] not results found");
        }
        return tpollSaved;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#getTweetPollSavedPublishedStatusById(java.lang.Long)
     */
    public TweetPollSavedPublishedStatus getTweetPollSavedPublishedStatusById(final Long id) throws EnMeNoResultsFoundException{
        final TweetPollSavedPublishedStatus statusBean = getTweetPollDao().getTweetPollPublishedStatusbyId(id);
        if (statusBean == null) {
            throw new EnMeNoResultsFoundException("TweetPollSavedPublishedStatus [" + id
                    + "] is missing");
        }
        return statusBean;
    }



   /*
    * (non-Javadoc)
    * @see org.encuestame.core.service.imp.ITweetPollService#getTweetPollByIdSlugName(java.lang.Long, java.lang.String)
    */
    public TweetPoll getTweetPollByIdSlugName(final Long tweetPollId, final String slug) throws EnMeNoResultsFoundException {
        TweetPoll tweetPoll;
        try {
                tweetPoll = getTweetPollDao().getTweetPollByIdandSlugName(tweetPollId, slug);
            if (tweetPoll == null) {
                log.error("tweet poll invalid with this id "+tweetPollId);
                throw new EnMeTweetPollNotFoundException("tweet poll invalid with this id "+tweetPollId);
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e);
            tweetPoll = null;
        }
        return tweetPoll;
    }

    /**
     * Get Tweet Poll Folder by User and FolderId.
     * @param id folder id.
     * @throws EnMeNoResultsFoundException if username not exist.
     */
    private TweetPollFolder getTweetPollFolderByFolderId(final Long folderId) throws EnMeNoResultsFoundException{
        final TweetPollFolder folder = this.getTweetPollDao()
                .getTweetPollFolderByIdandUser(folderId,
                        getUserAccount(getUserPrincipalUsername()).getAccount());
        if (folder == null) {
            throw new EnMeNoResultsFoundException("tweetpoll folder not valid");
        }
        return folder;
    }

    /**
     * Get TweetPoll.
     * @param tweetPollId
     * @param username
     * @return
     * @throws EnMeNoResultsFoundException
     */
    private TweetPoll getTweetPoll(final Long tweetPollId, final String username) throws EnMeNoResultsFoundException{
        return this.getTweetPollById(tweetPollId, username);
    }

    /**
     * Get Results By {@link TweetPoll}.
     * @param tweetPollId tweetPoll Id
     * @throws EnMeNoResultsFoundException
     */
    public List<TweetPollResultsBean> getResultsByTweetPollId(final Long tweetPollId) throws EnMeNoResultsFoundException{
        if (log.isDebugEnabled()) {
            log.debug("getResultsByTweetPollId "+tweetPollId);
        }
        final List<TweetPollResultsBean> pollResults = new ArrayList<TweetPollResultsBean>();
        final TweetPoll tweetPoll = getTweetPollById(tweetPollId, null);
        if (log.isDebugEnabled()) {
           // log.debug("Answers Size "+tweetPoll.getQuestion().getQuestionsAnswers().size());
            log.debug("tweetPoll "+tweetPoll);
        }
        for (QuestionAnswer questionsAnswer : getQuestionDao().getAnswersByQuestionId(tweetPoll.getQuestion().getQid())) {
              if (log.isDebugEnabled()) {
                  log.debug("Question Name " + tweetPoll.getQuestion().getQuestion());
              }
              pollResults.add(this.getVotesByTweetPollAnswerId(tweetPollId, questionsAnswer));
        }
        this.calculatePercents(pollResults);
        return pollResults;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#getTweetPollTotalVotes(org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
    public Long getTweetPollTotalVotes(final TweetPoll tweetPoll) {
        long totalVotes = 0;
        for (QuestionAnswer questionsAnswers : getQuestionDao().getAnswersByQuestionId(tweetPoll.getQuestion().getQid())) {
            totalVotes += this.getVotesByTweetPollAnswerId(tweetPoll.getTweetPollId(), questionsAnswers).getVotes();
      }
        return totalVotes;
    }

    /**
     * Calculate tweetpoll votes percent.
     * @param list
     */
    private void calculatePercents(final List<TweetPollResultsBean> list) {
        double totalVotes = 0;
        for (TweetPollResultsBean tweetPollResultsBean : list) {
            totalVotes = totalVotes + tweetPollResultsBean.getVotes();
        }

        for (TweetPollResultsBean tweetPollResultsBean : list) {
            tweetPollResultsBean.setPercent(EnMeUtils.calculatePercent(totalVotes, tweetPollResultsBean.getVotes()));
        }
    }

    /**
     *
     * @param tweetPollId
     * @param answerId
     * @return
     */
    public TweetPollResultsBean getVotesByTweetPollAnswerId(
            final Long tweetPollId,
            final QuestionAnswer answer) {
        final List<Object[]> result = getTweetPollDao().getResultsByTweetPoll(tweetPollId, answer.getQuestionAnswerId());
        //log.debug("result getVotesByTweetPollAnswerId- "+result.size());
        final TweetPollResultsBean tweetPollResult = new TweetPollResultsBean();
        tweetPollResult.setAnswerName(answer.getAnswer());
        //FIXME: the next block must be in ConvertDomainBean
        if (result.size() == 0) {
            tweetPollResult.setAnswerId(answer.getQuestionAnswerId());
            tweetPollResult.setColor(answer.getColor());
            tweetPollResult.setVotes(0L);
        } else {
            for (Object[] objects : result) {
                tweetPollResult.setAnswerId(answer.getQuestionAnswerId());
                tweetPollResult.setColor(answer.getColor());
                //FIXME: where is the short url ??
                tweetPollResult.setShortUrl(answer.getUrlAnswer());
                tweetPollResult.setUrl(answer.getUrlAnswer());
                tweetPollResult.setVotes(Long.valueOf(objects[2].toString()));

            }
        }
        tweetPollResult.setAnswerBean(ConvertDomainBean.convertAnswerToBean(answer));
        return tweetPollResult;
    }

    /**
     * Validate TweetPoll IP.
     * @param ipVote  ipVote
     * @param tweetPoll tweetPoll
     * @return {@link TweetPollResult}
     */
    @Deprecated
    public TweetPollResult validateTweetPollIP(final String ipVote, final TweetPoll tweetPoll){
        return getTweetPollDao().validateVoteIP(ipVote, tweetPoll);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#validateIpVote(java
     * .lang.String, org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
    public List<TweetPollResult> validateIpVote(final String ipVote,
            final TweetPoll tweetPoll) throws EnmeFailOperation {
        List<TweetPollResult> tpResult = new ArrayList<TweetPollResult>();
        tpResult = getTweetPollDao().validateTweetPollResultsIP(ipVote, tweetPoll);
        if (tpResult.size() > 0) {
            if (tweetPoll.getAllowRepatedVotes()
                    && (tpResult.size() < tweetPoll.getMaxRepeatedVotes())) {
                return tpResult;
            } else {
                throw new EnmeFailOperation(
                        "Maximum quota of votes has been exceeded");
            }

        } else {
            return tpResult;
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#validateLimitVotes(
     * org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
    public Boolean validateLimitVotes(final TweetPoll tweetPoll) {
        Boolean limitVote = Boolean.FALSE;
        log.debug("tweetPoll.getLimitVotesEnabled()" + tweetPoll.getLimitVotesEnabled());
        if (tweetPoll.getLimitVotesEnabled() != null && tweetPoll.getLimitVotesEnabled()) {
            final Long totalVotes = getTweetPollDao()
                    .getTotalVotesByTweetPollId(tweetPoll.getTweetPollId());
            if (totalVotes >= Long.valueOf(tweetPoll.getLimitVotes())  ) {
                limitVote = Boolean.TRUE;
            }

        }
        return limitVote;
    }

    /**
     * Restrict Votes by Date.
     */
    public Boolean validateVotesByDate(final TweetPoll tweetPoll) {
        Boolean limitVoteByDate = Boolean.FALSE;
        if (tweetPoll.getDateLimit()) {
            limitVoteByDate = DateUtil.compareToCurrentDate(tweetPoll
                    .getDateLimited());
        }
         return limitVoteByDate;
    }

    /**
     * Create TweetPoll Folder.
     * @param folderName
     * @param username
     * @return
     * @throws EnMeNoResultsFoundException
     */
    public FolderBean createTweetPollFolder(final String folderName, final String username) throws EnMeNoResultsFoundException{
        final TweetPollFolder tweetPollFolderDomain = new TweetPollFolder();
        tweetPollFolderDomain.setUsers(getUserAccount(username).getAccount());
        tweetPollFolderDomain.setCreatedAt(new Date());
        tweetPollFolderDomain.setCreatedBy(getUserAccount(getUserPrincipalUsername()));
        tweetPollFolderDomain.setStatus(org.encuestame.utils.enums.Status.ACTIVE);
        tweetPollFolderDomain.setFolderName(folderName);
        this.getTweetPollDao().saveOrUpdate(tweetPollFolderDomain);
        return ConvertDomainBean.convertFolderToBeanFolder(tweetPollFolderDomain);
    }

    /**
     * Get List of TweetPoll Folders.
     * @return
     * @throws EnMeNoResultsFoundException
     */
    public List<FolderBean> getFolders() throws EnMeNoResultsFoundException {
        final List<TweetPollFolder> folders = getTweetPollDao()
                .retrieveTweetPollFolderByAccount(getUserAccount(getUserPrincipalUsername()).getAccount());
        log.debug("List of Folders :"+folders.size());
        List<FolderBean> foldersBean = ConvertDomainBean.convertListTweetPollFoldertoBean(folders);
        for (FolderBean folderItem : foldersBean) {
            //FUTURE: ENCUESTAME-263 maybe is posible to improve this query
            final List<TweetPollBean> tweetPollsByFolder = this.searchTweetPollsByFolder(folderItem.getId(), getUserPrincipalUsername());
            folderItem.setItems(Long.valueOf(tweetPollsByFolder.size()));
        }
        return foldersBean;

    }

    /**
     * Update Tweet Poll Folder.
     * @throws EnMeNoResultsFoundException
     */
    public FolderBean updateTweetPollFolder(final Long folderId, final String folderName, final String username) throws EnMeNoResultsFoundException {
        final TweetPollFolder tweetPollFolder = this.getTweetPollFolder(folderId);
        if (tweetPollFolder == null) {
            throw new EnMeNoResultsFoundException("Tweet Poll Folder not found");
        } else {
            tweetPollFolder.setFolderName(folderName);
            getTweetPollDao().saveOrUpdate(tweetPollFolder);
        }
         return ConvertDomainBean.convertFolderToBeanFolder(tweetPollFolder);
     }

     /**
     * Remove TweetPoll Folder.
     * @param TweetPoll folderId
     * @throws EnMeNoResultsFoundException
     */
    public void deleteTweetPollFolder(final Long folderId) throws EnMeNoResultsFoundException{
        final TweetPollFolder tweetPollfolder = this.getTweetPollFolder(folderId);
        if(tweetPollfolder != null) {
            getTweetPollDao().delete(tweetPollfolder);
        } else {
            throw new EnMeNoResultsFoundException("TweetPoll folder not found");
        }
    }

    /**
     * Get Tweet Poll Folder.
     * @param id
     * @return
     */
    private TweetPollFolder getTweetPollFolder(final Long folderId){
        return this.getTweetPollDao().getTweetPollFolderById(folderId);
    }

    /**
     *
     * @param folderId
     * @return
     * @throws EnMeNoResultsFoundException
     */
    public TweetPollFolder getTweetPollFolderbyId(final Long folderId) throws EnMeNoResultsFoundException{
         return this.getTweetPollDao().getTweetPollFolderByIdandUser(folderId, getAccount(getUserPrincipalUsername()));
    }

    /**
     * Add {@link TweetPoll} to Folder.
     * @param folderId
     * @throws EnMeNoResultsFoundException
     */
    public void addTweetPollToFolder(final Long folderId, final String username, final Long tweetPollId)
           throws EnMeNoResultsFoundException {
        final TweetPollFolder tpfolder = this.getTweetPollFolderByFolderId(folderId);
         if (tpfolder != null) {
             final TweetPoll tpoll = this.getTweetPollById(tweetPollId);
             tpoll.setTweetPollFolder(tpfolder);
             getTweetPollDao().saveOrUpdate(tpoll);
         } else {
             throw new EnMeNoResultsFoundException("tweetPoll folder not found");
         }
    }

    /**
     * Change Status {@link TweetPoll}.
     * @param tweetPollId
     * @param username
     * @throws EnMeNoResultsFoundException
     */
    public void changeStatusTweetPoll(final Long tweetPollId, final String username)
           throws EnMeNoResultsFoundException, EnmeFailOperation{
        final TweetPoll tweetPoll = getTweetPoll(tweetPollId, username);
        if (tweetPoll != null) {
            tweetPoll.setCloseNotification(Boolean.TRUE);
            getTweetPollDao().saveOrUpdate(tweetPoll);
        } else {
               throw new EnmeFailOperation("Fail Change Status Operation");
        }
    }

    /**
     * Set if {@link TweetPoll} as favorite.
     * @param tweetPollId
     * @param username
     * @throws EnMeNoResultsFoundException
     * @throws EnmeFailOperation
     */
    public void setFavouriteTweetPoll(final Long tweetPollId, final String username) throws
           EnMeNoResultsFoundException, EnmeFailOperation{
        final TweetPoll tweetPoll = getTweetPoll(tweetPollId, username);
        if (tweetPoll != null) {
            tweetPoll.setFavourites(tweetPoll.getFavourites() == null ? false : !tweetPoll.getFavourites());
            getTweetPollDao().saveOrUpdate(tweetPoll);
        } else {
               throw new EnmeFailOperation("Fail Change Status Operation");
        }
    }


    /**
     * Change Allow Live Results {@link TweetPoll}.
     * @param tweetPollId
     * @param username
     * @throws EnMeNoResultsFoundException
     */
    public void changeAllowLiveResultsTweetPoll(final Long tweetPollId, final String username)
                throws EnMeNoResultsFoundException, EnmeFailOperation{
        final TweetPoll tweetPoll = getTweetPollDao().getTweetPollByIdandUserId(tweetPollId, getUserAccountId(username));
        if (tweetPoll != null){
            tweetPoll.setAllowLiveResults(tweetPoll.getAllowLiveResults() == null
                      ? false : !tweetPoll.getAllowLiveResults());
            getTweetPollDao().saveOrUpdate(tweetPoll);
        }
        else {
            throw new EnmeFailOperation("Fail Change Allow Live Results Operation");
        }
    }

    /**
     * Change Allow Live Results {@link TweetPoll}.
     * @param tweetPollId
     * @param username
     * @throws EnMeNoResultsFoundException
     */
    public void changeAllowCaptchaTweetPoll(final Long tweetPollId, final String username)
           throws EnMeNoResultsFoundException, EnmeFailOperation{
        final TweetPoll tweetPoll = getTweetPoll(tweetPollId, username);
        if (tweetPoll != null) {
             tweetPoll.setCaptcha(tweetPoll.getCaptcha() == null ? false : !tweetPoll.getCaptcha());
             getTweetPollDao().saveOrUpdate(tweetPoll);
        } else {
            throw new EnmeFailOperation("Fail Change Allow Captcha Operation");
        }
    }

    /**
     * Change Resume Live Results {@link TweetPoll}.
     * @param tweetPollId
     * @param username
     * @throws EnMeNoResultsFoundException
     */
    public void changeResumeLiveResultsTweetPoll(final Long tweetPollId, final String username)
           throws EnMeNoResultsFoundException, EnmeFailOperation {
        final TweetPoll tweetPoll = getTweetPoll(tweetPollId, username);
        if (tweetPoll != null) {
            tweetPoll.setResumeLiveResults(tweetPoll.getResumeLiveResults() == null
                      ? false : !tweetPoll.getResumeLiveResults());
            getTweetPollDao().saveOrUpdate(tweetPoll);
        }
        else {
            throw new EnmeFailOperation("Fail Change Resume Live Results Operation");
        }
    }

   /*
    * (non-Javadoc)
    * @see org.encuestame.core.service.imp.ITweetPollService#changeAllowRepeatedTweetPoll(java.lang.Long, java.lang.String)
    */
    public void changeAllowRepeatedTweetPoll(final Long tweetPollId, final String username)
                throws EnMeNoResultsFoundException, EnmeFailOperation{
        final TweetPoll tweetPoll = this.getTweetPoll(tweetPollId, username);
        if (tweetPoll != null){
            tweetPoll.setAllowRepatedVotes(tweetPoll.getAllowRepatedVotes() == null ? false : !tweetPoll.getAllowRepatedVotes());
            getTweetPollDao().saveOrUpdate(tweetPoll);
        } else {
            throw new EnmeFailOperation("Fail Change Allow Repeated Operation");
        }
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#chaneCommentStatusTweetPoll(java.lang.Long, java.lang.String)
     */
    public void chaneCommentStatusTweetPoll(final Long tweetPollId, final String username)
            throws EnMeNoResultsFoundException, EnmeFailOperation{
    final TweetPoll tweetPoll = this.getTweetPoll(tweetPollId, username);
    final CommentOptions commentOption = tweetPoll.getShowComments();
    if (commentOption == null) {
        tweetPoll.setShowComments(CommentOptions.MODERATE);
    } else if (commentOption.equals(CommentOptions.MODERATE)) {
        tweetPoll.setShowComments(CommentOptions.PUBLISHED);
    } else if (commentOption.equals(CommentOptions.PUBLISHED)) {
        tweetPoll.setShowComments(CommentOptions.MODERATE);
    }
    getTweetPollDao().saveOrUpdate(tweetPoll);
}

    /**
     * Change Close Notification.
     * @param tweetPollId
     * @param username
     * @throws EnMeNoResultsFoundException
     * @throws EnmeFailOperation
     */
    public void changeCloseNotificationTweetPoll(final Long tweetPollId, final String username)
           throws EnMeNoResultsFoundException, EnmeFailOperation{
        final TweetPoll tweetPoll = this.getTweetPoll(tweetPollId, username);
        if (tweetPoll != null){
            tweetPoll.setCloseNotification(tweetPoll.getCloseNotification() == null
                      ? false : !tweetPoll.getCloseNotification());
            getTweetPollDao().saveOrUpdate(tweetPoll);
        } else {
            throw new EnmeFailOperation("Fail Change Allow Repeated Operation");
        }
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#saveOrUpdateTweetPoll(org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
    public void saveOrUpdateTweetPoll(final TweetPoll tweetPoll){
        if(tweetPoll != null) {
            getTweetPollDao().saveOrUpdate(tweetPoll);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.business.service.imp.ITweetPollService#checkTweetPollCompleteStatus(org.encuestame.persistence.domain.tweetpoll.TweetPoll)
     */
    public void checkTweetPollCompleteStatus(final TweetPoll tweetPoll) {
        boolean next = true;

        log.debug("checkTweetPollCompleteStatus tweetPoll.getLimitVotesEnabled() " + tweetPoll.getLimitVotesEnabled());
        boolean votesEnabled = tweetPoll.getLimitVotesEnabled() == null ? false : tweetPoll.getLimitVotesEnabled();
        if (votesEnabled) {
            long limitVotes = tweetPoll.getLimitVotes() == null ? 0 : tweetPoll.getLimitVotes();
            if (limitVotes <= this.getTweetPollTotalVotes(tweetPoll)) {
                log.debug("checkTweetPollCompleteStatus limis vote");
                tweetPoll.setCompleted(Boolean.TRUE);
                next = false;
            }
        }

        log.debug("checkTweetPollCompleteStatus tweetPoll.getDateLimit() " + tweetPoll.getDateLimit());
        if (next && tweetPoll.getDateLimited() != null) {
            DateTime date = new DateTime(tweetPoll.getDateLimited());
            log.debug(date);
            if (date.isBeforeNow()) {
                log.debug("checkTweetPollCompleteStatus is After Now");
                tweetPoll.setCompleted(Boolean.TRUE);
                next = false;
            }
        }
        //TODO: other possibles validates.
        this.saveOrUpdateTweetPoll(tweetPoll);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#getTweetPollLinks(org
     * .encuestame.persistence.domain.tweetpoll.TweetPoll,
     * org.encuestame.persistence.domain.survey.Poll,
     * org.encuestame.persistence.domain.survey.Survey,
     * org.encuestame.utils.enums.TypeSearchResult)
     */
    public List<LinksSocialBean> getTweetPollLinks(
            final TweetPoll tweetPoll,
            final Poll poll,
            final Survey survey,
            final TypeSearchResult type) {
       List<TweetPollSavedPublishedStatus> links = new ArrayList<TweetPollSavedPublishedStatus>();
      if (type.equals(TypeSearchResult.TWEETPOLL)) {
          links = getTweetPollDao().getLinksByTweetPoll(tweetPoll , null, null, TypeSearchResult.TWEETPOLL);
      } else if(type.equals(TypeSearchResult.POLL)) {
          links = getTweetPollDao().getLinksByTweetPoll(null , null, poll, TypeSearchResult.POLL);
      }
      log.debug("getTweetPollLinks: "+links.size());
      return ConvertDomainBean.convertTweetPollSavedPublishedStatus(links);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#addHashtagToTweetPoll
     * (org.encuestame.persistence.domain.tweetpoll.TweetPoll,
     * org.encuestame.utils.web.HashTagBean)
     */
    public HashTag addHashtagToTweetPoll(
            final TweetPoll tweetPoll,
            final HashTagBean hashTagBean)
            throws EnMeNoResultsFoundException {
        log.debug("Adding hashtag to TP "+tweetPoll.getTweetPollId());
        log.debug("Adding hashTagBean to TP "+hashTagBean.getHashTagName());
        //validate the hashtag bean.
        hashTagBean.setHashTagName(ValidationUtils.removeNonAlphanumericCharacters(hashTagBean.getHashTagName()));
        HashTag hashtag = getHashTag(hashTagBean.getHashTagName(), false);
        if (hashtag == null) {
            hashtag = createHashTag(hashTagBean.getHashTagName().toLowerCase());
            tweetPoll.getHashTags().add(hashtag);
            getTweetPollDao().saveOrUpdate(tweetPoll);
            log.debug("Added new hashtag done :"+hashtag.getHashTagId());
            return hashtag;
        } else {
            tweetPoll.getHashTags().add(hashtag);
            getTweetPollDao().saveOrUpdate(tweetPoll);
            log.debug("Added previous hashtag done :"+hashtag.getHashTagId());
            return hashtag;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#removeHashtagFromTweetPoll
     * (org.encuestame.persistence.domain.tweetpoll.TweetPoll,
     * org.encuestame.persistence.domain.HashTag)
     */
    public void removeHashtagFromTweetPoll(final TweetPoll tweetPoll,
            final HashTag hashTag) {
        log.debug("Remove hashtag disabled");
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#getTweetPollsbyRange(java.lang.Integer, java.lang.Integer, java.util.Date)
     */
    public List<TweetPoll> getTweetPollsbyRange(final Integer maxResults,
            final Integer start, final Date range){
        final List<TweetPoll> tweetPolls = getTweetPollDao().getTweetPolls(
                maxResults, start, range);
        return tweetPolls;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.imp.ITweetPollService#searchTweetPollsByFolder(java.lang.Long, java.lang.String)
     */
    public List<TweetPollBean> searchTweetPollsByFolder(final Long folderId,
            final String username) throws EnMeNoResultsFoundException {
        List<TweetPoll> tweetPollsbyFolder = new ArrayList<TweetPoll>();
        final TweetPollFolder tweetPollFolder = getTweetPollDao()
                .getTweetPollFolderById(folderId);
        if (tweetPollFolder == null) {
            throw new EnMeTweetPollNotFoundException(
                    "Tweetpoll folder not found");

        } else {
            tweetPollsbyFolder = getTweetPollDao().retrieveTweetPollByFolder(
                    getUserAccount(getUserPrincipalUsername()).getAccount().getUid(),
                    folderId);
        }
        log.info("search polls by folder size " + tweetPollsbyFolder.size());
        return ConvertDomainBean.convertListToTweetPollBean(tweetPollsbyFolder);
    }

    /**
     * Retrieve {@link TweetPoll} posted on social networks.
     * @param tpolls
     * @param providers
     * @return
     * @throws EnMeNoResultsFoundException
     */
    private List<TweetPoll> retrieveTweetPollsPostedOnSocialNetworks(
            final List<TweetPoll> tpolls, final List<SocialProvider> providers, final List<Long> socialAccounts) {
        final List<TweetPoll> tpollsPostedOnSocialNet = new ArrayList<TweetPoll>();
        List<TweetPollSavedPublishedStatus> tpSavedPublished = new ArrayList<TweetPollSavedPublishedStatus>();
        final List<SocialAccount> socialAccountList = this.retrieveSocialAccountsbyId(socialAccounts, getUserPrincipalUsername());
        for (TweetPoll tweetPoll : tpolls) {
            tpSavedPublished = getTweetPollDao()
                    .getSocialLinksByTweetPollSearch(tweetPoll,
                            TypeSearchResult.TWEETPOLL, providers, socialAccountList);
            if (tpSavedPublished.size() > 0) {
                tpollsPostedOnSocialNet.add(tweetPoll);
            }
        }
        return tpollsPostedOnSocialNet;
    }


    /**
     * Remove Tweetpoll.
     * @param tpoll {@link TweetPoll}
     */
    public void removeTweetPoll(final TweetPoll tpoll) throws EnMeNoResultsFoundException {
        final Set<HashTag> hashTagSet = new HashSet<HashTag>();

        // Retrieve all TweetpollSwitch.
          final List<TweetPollSwitch> tpollSwitch = getTweetPollDao()
                .getListAnswersByTweetPollAndDateRange(tpoll);

        for (TweetPollSwitch tweetPollSwitch : tpollSwitch) {
            // Retrieve all TweetpollResult by switch
            final List<TweetPollResult> tpollResult = getTweetPollDao()
                    .getTweetPollResultsByTweetPollSwitch(tweetPollSwitch);

            for (TweetPollResult tweetPollResult : tpollResult) {
                // Remove all  TweetpollResult,
                getTweetPollDao().delete(tweetPollResult);
            }
            // Remove all TweetpollSwith
            getTweetPollDao().delete(tweetPollSwitch);

        }

        // Retrieve all Tweetpolls saved published.
        final List<TweetPollSavedPublishedStatus> tpollSaved = getTweetPollDao().getAllLinks(tpoll, null, null, TypeSearchResult.TWEETPOLL);
        for (TweetPollSavedPublishedStatus tweetPollSavedPublishedStatus : tpollSaved) {
            // Remove TweetpOllSavePublished
            getTweetPollDao().delete(tweetPollSavedPublishedStatus);
        }

        // Remove all hashtags by Tweetpoll.
        tpoll.setHashTags(hashTagSet);
        getTweetPollDao().saveOrUpdate(tpoll);


        // Remove Question Answers
        final List<QuestionAnswer> answers = getQuestionDao().getAnswersByQuestionId(tpoll.getQuestion().getQid());
        for (QuestionAnswer questionAnswer : answers) {
            getQuestionDao().delete(questionAnswer);
        }

        // Remove Tweetpoll
        getTweetPollDao().delete(tpoll);

    }

    /**
     * Publish all scheduled items
     * @param status {@link Status}
     * @param minimumDate {@link Date}
     * @throws EnMeNoResultsFoundException
     */
    public void publishScheduledItems(
            final Status status,
            final Date minimumDate) throws EnMeNoResultsFoundException {

    Boolean publish = Boolean.FALSE;
    final String totalAttempts = EnMePlaceHolderConfigurer.getProperty("attempts.scheduled.publication");
         // 1. Retrieve all records scheduled before currently date.
    final List<Schedule> scheduledRecords = getScheduledDao()
                .retrieveScheduled(status, minimumDate);
        // 2. Iterate the results and for each try to publish again Call
        // Service to publish Tweetpoll/ Poll or Survey.
        // 3. If list > O - iterate list
        if (scheduledRecords.size() > 0) {
            for (Schedule schedule : scheduledRecords) {
                TweetPollSavedPublishedStatus tpollSaved = new TweetPollSavedPublishedStatus();
                // Retrieve attempt constant for properties file.
                if (schedule.getPublishAttempts() < 5) {
                    // Set Status PROCESSING
                    schedule.setStatus(Status.PROCESSING);
                    getScheduledDao().saveOrUpdate(schedule);
                    tpollSaved = this
                            .publishTweetBySocialAccountId(schedule
                                    .getSocialAccount().getId(), schedule
                                    .getTpoll(), schedule.getTweetText(),
                                    schedule.getTypeSearch(), schedule
                                            .getPoll(), schedule.getSurvey());
                     // If tpollsavedpublished isnt null and Status is failed, is
                    // necessary re publish
                    if ((tpollSaved != null) && (tpollSaved.getStatus()).equals(Status.FAILED)) {
                        log.trace("******* Item not published *******");
                        // Update ScheduleRecord and set counter und Date
                        DateTime dt = new DateTime(schedule.getScheduleDate());
                        // Set a new value to republishing
                        schedule.setScheduleDate(dt.plusMinutes(3).toDate());
                        // Increment the counter of attempts
                        int counter = schedule.getPublishAttempts();
                        counter = counter + 1;
                        schedule.setPublishAttempts(counter);
                        schedule.setStatus(Status.FAILED);
                        getScheduledDao().saveOrUpdate(schedule);
                    } else {
                        log.trace("******* Published *******");
                        final Date currentDate = DateUtil.getCurrentCalendarDate();
                        schedule.setStatus(Status.SUCCESS);
                        schedule.setPublicationDate(currentDate);
                        getScheduledDao().saveOrUpdate(schedule);
                        createNotification(
                                NotificationEnum.WELCOME_SIGNUP,
                                getMessageProperties("notification.tweetpoll.scheduled.success",
                                  Locale.ENGLISH, //FIXME: fix this, locale is fixed
                                  new Object[] {
                                          tpollSaved.getTweetPoll().getQuestion().getQuestion(),
                                          currentDate.toString()
                                  }), //FIXME: currentDate shouldbe passed as ISO date.
                                  null,
                                false,
                                tpollSaved.getTweetPoll().getEditorOwner());
                    }
                }
            }
        }
     }

    public TweetPollDetailBean getTweetPollDetailInfo(final Long tpollId)
            throws EnMeNoResultsFoundException {
        final TweetPollDetailBean tpollDetail = new TweetPollDetailBean();

        final TweetPoll tpoll = getTweetPollById(tpollId);
        tpollDetail.setTpollBean(ConvertDomainBean
                .convertTweetPollToBean(tpoll));
        tpollDetail.setResults(this.getResultsByTweetPollId(tpoll
                .getTweetPollId()));
        tpollDetail.setListAnswers(ConvertDomainBean
                .convertAnswersToQuestionAnswerBean(getQuestionDao()
                        .getAnswersByQuestionId(tpoll.getQuestion().getQid())));
        return tpollDetail;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#removeScheduledItems
     * (org.encuestame.utils.enums.Status, java.lang.Integer)
     */
    public void removeScheduledItems(final Status status, final Integer attempts) {
        final List<Schedule> removeList = getScheduledDao()
                .retrieveFailedScheduledItems(attempts, status);
        for (Schedule schedule : removeList) {
            getScheduledDao().delete(schedule);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.imp.ITweetPollService#retrieveFoldersbyKeyword
     * (org.encuestame.persistence.domain.security.UserAccount,
     * java.lang.String)
     */
    public List<TweetPollFolder> retrieveFoldersbyKeyword(final String keyword) throws EnMeNoResultsFoundException {
         List<TweetPollFolder> folders = new ArrayList<TweetPollFolder>();
        if(keyword!=null){
            folders = getTweetPollDao().getTweetPollFolderByKeyword(keyword,  getUserAccount(getUserPrincipalUsername()));
        }
         return folders;
    }
}
