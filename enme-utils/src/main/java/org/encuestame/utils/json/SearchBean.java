/*
 ************************************************************************************
 * Copyright (C) 2001-2013 encuestame: system online surveys Copyright (C) 2013
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.utils.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.encuestame.utils.web.AbstractUnitSurvey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Search Bean.
 *
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since October 10, 2013
 */
public class SearchBean extends AbstractUnitSurvey{

	 private static final long serialVersionUID = 5248987840986024954L;
	    @JsonProperty(value = "id")
	    private Long id;
	    @JsonProperty(value = "question")
	    private QuestionBean questionBean = new QuestionBean();
	    @JsonProperty(value = "closeNotification")
	    private Boolean closeNotification; //TODO: ???????
	    @JsonProperty(value = "resultNotification")
	    private Boolean resultNotification;
	    @JsonProperty(value = "publishPoll")
	    private Boolean publishPoll;
	    @JsonProperty(value = "scheduleDate")
	    private Date scheduleDate;
	    @JsonProperty(value = "allowLiveResults")
	    private Boolean allowLiveResults;
	    @JsonProperty(value = "schedule")
	    private Boolean schedule;
	    @JsonProperty(value = "completed")
	    private Boolean completed;
	    @JsonProperty(value = "favourites")
	    private Boolean favourites;
	    @JsonProperty(value = "userId")
	    private Long userId;
	    @JsonProperty(value = "tweetUrl")
	    private String tweetUrl;
	    @JsonProperty(value = "allowRepeatedVotes")
	    private Boolean allowRepeatedVotes;
	    @JsonProperty(value = "hits")
	    private Long hits;
	    @JsonProperty(value = "maxRepeatedVotes")
	    private Integer maxRepeatedVotes;
	    @JsonProperty(value = "limitVotesEnabled")
	    private Boolean limitVotesEnabled;
	    @JsonProperty(value = "resumeTweetPollDashBoard")
	    private Boolean resumeTweetPollDashBoard;
	    @JsonProperty(value = "moderated_comments")
	    private Boolean moderatedComments;
	    @JsonProperty(value = "createdDateAt")
	    private Date createdDateAt;
	    @JsonProperty(value = "limitVotesDate")
	    private Boolean limitVotesDate;
	    @JsonProperty(value = "dateToLimit")
	    private String dateToLimit;
	    @JsonProperty(value = "update_date")
	    private Date updateDate;

	    /** Required Captcha to Vote. **/
	    @JsonProperty(value = "captcha")
	    private Boolean captcha = false;

	    /** Limit Votes. **/
	    @JsonProperty(value = "limitVotes")
	    private Integer limitVotes = 100;

	    /** If true, system display in left nav live results. **/
	    @JsonProperty(value = "resumeLiveResults")
	    private Boolean resumeLiveResults = false;

	    /** Results. **/
	    @JsonProperty(value = "resume_live_results")
	    private List<ResumeResultTweetPoll> results = new ArrayList<ResumeResultTweetPoll>();
	    @JsonProperty(value = "tweetpoll_answers")
	    private List<TweetPollAnswerSwitchBean> answerSwitchBeans = new ArrayList<TweetPollAnswerSwitchBean>();

        @JsonProperty(value = "is_hidden")
        public  Boolean isHidden;

        @JsonProperty(value = "password_protected")
        public String password;

    /**
	     * Constructor.
	     */
	    public SearchBean() {
	    }

	    /**
	     * @return the id
	     */
	    @JsonIgnore
	    public final Long getId() {
	        return id;
	    }

	    /**
	     * @param id
	     *            the id to set
	     */
	    public final void setId(final Long id) {
	        this.id = id;
	    }

	    /**
	     * @return the userId
	     */
	    @JsonIgnore
	    public final Long getUserId() {
	        return userId;
	    }

	    /**
	     * @param userId
	     *            the userId to set
	     */
	    public final void setUserId(final Long userId) {
	        this.userId = userId;
	    }

	    /**
	     * @return the closeNotification
	     */
	    @JsonIgnore
	    public final Boolean getCloseNotification() {
	        return closeNotification;
	    }

	    /**
	     * @param closeNotification
	     *            the closeNotification to set
	     */
	    public final void setCloseNotification(final Boolean closeNotification) {
	        this.closeNotification = closeNotification;
	    }

	    /**
	     * @return the resultNotification
	     */
	    @JsonIgnore
	    public final Boolean getResultNotification() {
	        return resultNotification;
	    }

	    /**
	     * @param resultNotification
	     *            the resultNotification to set
	     */
	    public final void setResultNotification(final Boolean resultNotification) {
	        this.resultNotification = resultNotification;
	    }

	    /**
	     * @return the publishPoll
	     */
	    @JsonIgnore
	    public final Boolean getPublishPoll() {
	        return publishPoll;
	    }

	    /**
	     * @param publishPoll
	     *            the publishPoll to set
	     */
	    public final void setPublishPoll(final Boolean publishPoll) {
	        this.publishPoll = publishPoll;
	    }

	    /**
	     * @return the completed
	     */
	    @JsonIgnore
	    public final Boolean getCompleted() {
	        return completed;
	    }

	    /**
	     * @param completed
	     *            the completed to set
	     */
	    public final void setCompleted(final Boolean completed) {
	        this.completed = completed;
	    }

	    /**
	     * @return the questionBean
	     */
	    @JsonIgnore
	    public final QuestionBean getQuestionBean() {
	        return questionBean;
	    }

	    /**
	     * @param questionBean
	     *            the questionBean to set
	     */
	    public final void setQuestionBean(final QuestionBean questionBean) {
	        this.questionBean = questionBean;
	    }

	    /**
	     * @return the results
	     */
	    @JsonIgnore
	    public final List<ResumeResultTweetPoll> getResults() {
	        return results;
	    }

	    /**
	     * @param results
	     *            the results to set
	     */
	    public final void setResults(List<ResumeResultTweetPoll> results) {
	        this.results = results;
	    }

	    /**
	     * @param tweetUrl
	     *            the tweetUrl to set
	     */
	    public final void setTweetUrl(String tweetUrl) {
	        this.tweetUrl = tweetUrl;
	    }

	    /**
	     * @return the scheduleDate
	     */
	    @JsonIgnore
	    public final Date getScheduleDate() {
	        return scheduleDate;
	    }

	    /**
	     * @param scheduleDate
	     *            the scheduleDate to set
	     */
	    public final void setScheduleDate(Date scheduleDate) {
	        this.scheduleDate = scheduleDate;
	    }

	    /**
	     * @return the allowLiveResults
	     */
	    @JsonIgnore
	    public final Boolean getAllowLiveResults() {
	        return allowLiveResults;
	    }

	    /**
	     * @param allowLiveResults
	     *            the allowLiveResults to set
	     */
	    public final void setAllowLiveResults(Boolean allowLiveResults) {
	        this.allowLiveResults = allowLiveResults;
	    }

	    /**
	     * @return the schedule
	     */
	    @JsonIgnore
	    public final Boolean getSchedule() {
	        return schedule;
	    }

	    /**
	     * @param schedule
	     *            the schedule to set
	     */
	    public final void setSchedule(Boolean schedule) {
	        this.schedule = schedule;
	    }

	    /**
	     * @return the captcha
	     */
	    @JsonIgnore
	    public final Boolean getCaptcha() {
	        return captcha;
	    }

	    /**
	     * @param captcha the captcha to set
	     */
	    public final void setCaptcha(Boolean captcha) {
	        this.captcha = captcha;
	    }

	    /**
	     * @return the limitVotes
	     */
	    @JsonIgnore
	    public final Integer getLimitVotes() {
	        return limitVotes;
	    }

	    /**
	     * @param limitVotes the limitVotes to set
	     */
	    public final void setLimitVotes(Integer limitVotes) {
	        this.limitVotes = limitVotes;
	    }

	    /**
	     * @return the resumeLiveResults
	     */
	    @JsonIgnore
	    public final Boolean getResumeLiveResults() {
	        return resumeLiveResults;
	    }

	    /**
	     * @param resumeLiveResults the resumeLiveResults to set
	     */
	    public final void setResumeLiveResults(Boolean resumeLiveResults) {
	        this.resumeLiveResults = resumeLiveResults;
	    }

	    /**
	     * @return the favourites
	     */
	    @JsonIgnore
	    public Boolean getFavourites() {
	        return favourites;
	    }

	    /**
	     * @param favourites the favourites to set
	     */
	    public void setFavourites(final Boolean favourites) {
	        this.favourites = favourites;
	    }

	    /**
	     * @return the allowRepeatedVotes
	     */
	    @JsonIgnore
	    public Boolean getAllowRepeatedVotes() {
	        return allowRepeatedVotes;
	    }

	    /**
	     * @param allowRepeatedVotes the allowRepeatedVotes to set
	     */
	    public void setAllowRepeatedVotes(final Boolean allowRepeatedVotes) {
	        this.allowRepeatedVotes = allowRepeatedVotes;
	    }

	    /**
	     * @return the maxRepeatedVotes
	     */
	    @JsonIgnore
	    public Integer getMaxRepeatedVotes() {
	        return maxRepeatedVotes;
	    }

	    /**
	     * @param maxRepeatedVotes the maxRepeatedVotes to set
	     */
	    public void setMaxRepeatedVotes(Integer maxRepeatedVotes) {
	        this.maxRepeatedVotes = maxRepeatedVotes;
	    }

	    /**
	     * @return the limitVotesEnabled
	     */
	    @JsonIgnore
	    public Boolean getLimitVotesEnabled() {
	        return limitVotesEnabled;
	    }

	    /**
	     * @param limitVotesEnabled the limitVotesEnabled to set
	     */
	    public void setLimitVotesEnabled(Boolean limitVotesEnabled) {
	        this.limitVotesEnabled = limitVotesEnabled;
	    }

	    /**
	     * @return the resumeTweetPollDashBoard
	     */
	    @JsonIgnore
	    public Boolean getResumeTweetPollDashBoard() {
	        return resumeTweetPollDashBoard;
	    }

	    /**
	     * @param resumeTweetPollDashBoard the resumeTweetPollDashBoard to set
	     */
	    public void setResumeTweetPollDashBoard(Boolean resumeTweetPollDashBoard) {
	        this.resumeTweetPollDashBoard = resumeTweetPollDashBoard;
	    }

	    /**
	    * @return the createdDateAt
	    */
	    @JsonIgnore
	    public Date getCreatedDateAt() {
	        return createdDateAt;
	    }

	    /**
	    * @param createdDateAt the createdDateAt to set
	    */
	    public void setCreatedDateAt(Date createdDateAt) {
	        this.createdDateAt = createdDateAt;
	    }

	    /**
	     * @return the limitVotesDate
	     */
	    @JsonIgnore
	    public Boolean getLimitVotesDate() {
	        return limitVotesDate;
	    }

	    /**
	     * @param limitVotesDate the limitVotesDate to set
	     */
	    public void setLimitVotesDate(Boolean limitVotesDate) {
	        this.limitVotesDate = limitVotesDate;
	    }

	    /**
	     * @return the dateToLimit
	     */
	    @JsonIgnore
	    public String getDateToLimit() {
	        return dateToLimit;
	    }

	    /**
	     * @param dateToLimit the dateToLimit to set
	     */
	    public void setDateToLimit(String dateToLimit) {
	        this.dateToLimit = dateToLimit;
	    }

	    /**
	     * @return the answerSwitchBeans
	     */
	    @JsonIgnore
	    public List<TweetPollAnswerSwitchBean> getAnswerSwitchBeans() {
	        return answerSwitchBeans;
	    }

	    /**
	     * @param answerSwitchBeans the answerSwitchBeans to set
	     */
	    public void setAnswerSwitchBeans(
	            List<TweetPollAnswerSwitchBean> answerSwitchBeans) {
	        this.answerSwitchBeans = answerSwitchBeans;

	    }

	    /**
	    * @return the updateDate
	    */
	    @JsonIgnore
	    public Date getUpdateDate() {
	        return updateDate;
	    }

	    /**
	    * @param updateDate the updateDate to set
	    */
	    public void setUpdateDate(final Date updateDate) {
	        this.updateDate = updateDate;
	    }

	    /**
	     * @return the hits
	     */
	    @JsonIgnore
	    public Long getHits() {
	        return hits;
	    }

	    /**
	     * @param hits the hits to set
	     */
	    public void setHits(Long hits) {
	        this.hits = hits;
	    }	   	   

	    /**
		 * @return the moderatedComments
		 */
	    @JsonIgnore
		public Boolean getModeratedComments() {
			return moderatedComments;
		}

		/**
		 * @param moderatedComments the moderatedComments to set
		 */
		public void setModeratedComments(Boolean moderatedComments) {
			this.moderatedComments = moderatedComments;
		}


        public Boolean getIsHidden() {
            return isHidden;
        }

        public void setIsHidden(Boolean isHidden) {
            this.isHidden = isHidden;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    /* (non-Javadoc)
             * @see java.lang.Object#toString()
             */
	    @Override
	    public String toString() {
	        StringBuilder builder = new StringBuilder();
	        builder.append("]");
	        return builder.toString();
	    }


	}

