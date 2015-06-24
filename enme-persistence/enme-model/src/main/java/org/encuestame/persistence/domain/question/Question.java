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
package org.encuestame.persistence.domain.question;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.encuestame.persistence.domain.security.Account;
import org.encuestame.persistence.domain.survey.SurveySection;
import org.encuestame.utils.enums.QuestionPattern;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

/**
 * Questions.
 * @author Picado, Juan juanATencuestame.org
 * @since October 17, 2009
 */
@Entity
@Indexed(index="Question")
@Table(name = "questions")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question {

    /**
     * Id.
     */
    private Long qid;
    /**
     * Question description.
     */
    private String question;

    /**
     * Slug question.
     */
    private String slugQuestion;
    /**
     * String optional id.
     */
    private String qidKey;

    /**
     * Is this question shared?
     */
    private Boolean sharedQuestion;

    /**
     * When this questions was created.
     */
    private Date createDate = Calendar.getInstance().getTime();

    /**
     * Hits.
     */
    private Long hits;

    /**
     * Account relationship.
     */
    private Account accountQuestion;

    /**
     * Define the pattern of the question.
     */
    private QuestionPattern questionPattern = QuestionPattern.SINGLE_SELECTION;

    /**
     * This questions belongs to user collection.
     */
    private Set<QuestionColettion> questionColettions = new HashSet<QuestionColettion>();

    /**
     * This question belongs to a survey section.
     */
    private SurveySection section;

    /**
     * @return qid
     */
    @Id
    @DocumentId
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "qid", unique = true, nullable = false)
    public Long getQid() {
        return this.qid;
    }

    /**
     * @param qid qid
     */
    public void setQid(final Long qid) {
        this.qid = qid;
    }

    /**
     * @return question
     */
    @Field(index=Index.TOKENIZED, store=Store.YES)
    @Column(name = "question", nullable = false)
    public String getQuestion() {
        return this.question;
    }

    /**
     * @param question question
     */
    public void setQuestion(final String question) {
        this.question = question;
    }

    /**
     * @return qidKey
     */
    @Field(index = Index.TOKENIZED, store = Store.YES)
    @Column(name = "qid_key")
    public String getQidKey() {
        return this.qidKey;
    }

    /**
     * @param qidKey qidKey
     */
    public void setQidKey(final String qidKey) {
        this.qidKey = qidKey;
    }

    /**
     * @return the questionColettions
     */
    @ManyToMany()
    @JoinTable(name="question_relations",
              joinColumns={@JoinColumn(name="question_id")},
              inverseJoinColumns={@JoinColumn(name="id_q_colection")})
    public Set<QuestionColettion> getQuestionColettions() {
        return questionColettions;
    }

    /**
     * @param questionColettions the questionColettions to set
     */
    public void setQuestionColettions(final Set<QuestionColettion> questionColettions) {
        this.questionColettions = questionColettions;
    }

    /**
     * @return the secUsersQuestion
     */
    @ManyToOne()
    @JoinColumn(name = "uid", nullable = false)
    public Account getAccountQuestion() {
        return accountQuestion;
    }

    /**
     * @param secUsersQuestion the secUsersQuestion to set
     */
    public void setAccountQuestion(final Account accountQuestion) {
        this.accountQuestion = accountQuestion;
    }

    /**
     * @return the sharedQuestion
     */
    @Column(name = "shared_question")
    public Boolean getSharedQuestion() {
        return sharedQuestion;
    }

    /**
     * @param sharedQuestion the sharedQuestion to set
     */
    public void setSharedQuestion(final Boolean sharedQuestion) {
        this.sharedQuestion = sharedQuestion;
    }

    /**
     * @return the createDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateBridge(resolution = Resolution.DAY)
    @Column(name = "question_created_date", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the hits
     */
    @Column(name = "question_hits")
    public Long getHits() {
        return hits;
    }

    /**
     * @param hits the hits to set
     */
    public void setHits(final Long hits) {
        this.hits = hits;
    }

    /**
     * @return the slugQuestion
     */
    @Field(index=Index.TOKENIZED, store=Store.YES)
    @Column(name = "question_slug", nullable = false)
    public String getSlugQuestion() {
        return slugQuestion;
    }

    /**
     * @param slugQuestion the slugQuestion to set
     */
    public void setSlugQuestion(final String slugQuestion) {
        this.slugQuestion = slugQuestion;
    }

    /**
     * @return the section
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    public SurveySection getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(final SurveySection section) {
        this.section = section;
    }

    /**
     * @return the questionPattern
     */
    @Column(name="question_pattern")
    @Enumerated(EnumType.ORDINAL)
    public QuestionPattern getQuestionPattern() {
        return questionPattern;
    }

    /**
     * @param questionPattern the questionPattern to set
     */
    public void setQuestionPattern(QuestionPattern questionPattern) {
        this.questionPattern = questionPattern;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Question [qid=" + qid + ", question=" + question
                + ", slugQuestion=" + slugQuestion + ", qidKey=" + qidKey
                + ", createDate=" + createDate + ", hits=" + hits + "]";
    }


}
