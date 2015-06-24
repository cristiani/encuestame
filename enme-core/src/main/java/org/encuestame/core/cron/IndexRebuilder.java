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
package org.encuestame.core.cron;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.encuestame.persistence.domain.HashTag;
import org.encuestame.persistence.domain.Project;
import org.encuestame.persistence.domain.notifications.Notification;
import org.encuestame.persistence.domain.question.Question;
import org.encuestame.persistence.domain.security.SocialAccount;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.domain.survey.PollFolder;
import org.encuestame.persistence.domain.survey.SurveyFolder;
import org.encuestame.persistence.domain.tweetpoll.TweetPollFolder;
import org.encuestame.persistence.domain.tweetpoll.TweetPollResult;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSavedPublishedStatus;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.search.FullTextSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Index Rebuilder.
 * @author Picado, Juan juanATencuestame.org
 * @since Jul 8, 2010 10:54:12 PM
 * @version $Id:$
 */
public class IndexRebuilder {

    /**
     * Log.
     */
    private static final Log log = LogFactory.getLog(IndexRebuilder.class);

    /**
     * {@link SessionFactory}.
     */
    @Autowired
    public HibernateTemplate hibernateTemplate;


    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /**
     * Reindex Entities.
     * @throws Exception exception.
     */
    public void reindexEntities() throws Exception {
        long start = System.currentTimeMillis();
        SessionFactory factory = sessionFactory;
        final FullTextSession fullTextSession = org.hibernate.search.Search.getFullTextSession(factory.openSession());
        IndexRebuilder.reindex(fullTextSession, Question.class);
        IndexRebuilder.reindex(fullTextSession, UserAccount.class);
        IndexRebuilder.reindex(fullTextSession, TweetPollSavedPublishedStatus.class);
        IndexRebuilder.reindex(fullTextSession, TweetPollFolder.class);
        IndexRebuilder.reindex(fullTextSession, SurveyFolder.class);
        IndexRebuilder.reindex(fullTextSession, PollFolder.class);
        IndexRebuilder.reindex(fullTextSession, Project.class);
        IndexRebuilder.reindex(fullTextSession, Notification.class);
        IndexRebuilder.reindex(fullTextSession, SocialAccount.class);
        IndexRebuilder.reindex(fullTextSession, TweetPollResult.class);
        IndexRebuilder.reindex(fullTextSession, HashTag.class);
        fullTextSession.close();
        long end = System.currentTimeMillis();
        log.debug("Indexing : took " + (end - start) + " milliseconds");
    }

    /**
     * Reindex domain object.
     * @param fullTextSession {@link FullTextSession}.
     * @param clazz domain class.
     */
    public static void reindex(final FullTextSession fullTextSession, final Class<?>
    clazz) {
            log.debug(clazz.getName() + " purge index ...");
            //purge all index content.
            fullTextSession.purgeAll(clazz);
            fullTextSession.flushToIndexes();
            fullTextSession.getSearchFactory().optimize(clazz);
            log.debug(clazz.getName() + " starting index ...");
            final long startTime = System.currentTimeMillis();
            fullTextSession.setFlushMode(FlushMode.MANUAL);
            fullTextSession.setCacheMode(CacheMode.IGNORE);
            final Transaction transaction = fullTextSession.beginTransaction();
            //Scrollable results will avoid loading too many objects in memory
            final ScrollableResults results = fullTextSession.createCriteria(clazz)
                .setFetchSize(100)
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .scroll(ScrollMode.FORWARD_ONLY);
            int index = 0;
            while(results.next()) {
                index++;
                fullTextSession.index(results.get(0));
                if (index % 100 == 0) {
                    fullTextSession.flushToIndexes();
                    fullTextSession.flush();
                    fullTextSession.clear();
                }
            }
            fullTextSession.flushToIndexes();
            fullTextSession.getSearchFactory().optimize(clazz);
            transaction.commit();
            log.debug(clazz.getName() + " Reindex end in "
                    + ((System.currentTimeMillis() - startTime) / 1000.0)
                    + " seconds.");
        }

    /**
     * @return the hibernateTemplate
     */
    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    /**
     * @param hibernateTemplate the hibernateTemplate to set
     */
    public void setHibernateTemplate(final HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

}
