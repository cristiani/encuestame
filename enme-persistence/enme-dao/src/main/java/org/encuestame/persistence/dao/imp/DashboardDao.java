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
package org.encuestame.persistence.dao.imp;

import java.util.List;

import org.encuestame.persistence.dao.IDashboardDao;
import org.encuestame.persistence.domain.dashboard.Dashboard;
import org.encuestame.persistence.domain.dashboard.Gadget;
import org.encuestame.persistence.domain.dashboard.GadgetProperties;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.utils.enums.GadgetType;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

/**
 * Dashboard Dao.
 * @author Morales,Diana Paola paolaATencuestame.org
 * @since July 27, 2011
 */
@Repository("dashboardDao")
public class DashboardDao extends AbstractHibernateDaoSupport implements IDashboardDao {

    @Autowired
    public DashboardDao(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#getDashboardbyId(java.lang.Long)
     */
    public Dashboard getDashboardbyId(final Long boardId){
             return (Dashboard) getHibernateTemplate().get(Dashboard.class, boardId);
     }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#getDashboard(java.lang.Long, java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public Dashboard getDashboardbyIdandUser(final Long boardId, final UserAccount userAcc){
          final DetachedCriteria criteria = DetachedCriteria.forClass(Dashboard.class);
          criteria.add(Restrictions.eq("userBoard", userAcc));
          criteria.add(Restrictions.eq("boardId", boardId));
          return (Dashboard) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#retrieveDashboards(java.lang.Long, org.encuestame.persistence.domain.security.UserAccount)
     */
    @SuppressWarnings("unchecked")
    public List<Dashboard> retrieveDashboardsbyUser(final UserAccount userAcc, final Integer maxResults,
            final Integer start){
        final DetachedCriteria criteria = DetachedCriteria.forClass(Dashboard.class);
            criteria.add(Restrictions.eq("userBoard", userAcc));
            criteria.addOrder(Order.desc("boardSequence"));
            return (List<Dashboard>) filterByMaxorStart(criteria, maxResults, start);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#retrieveFavouritesDashboards(java.lang.Long, java.lang.Integer, java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    public List<Dashboard> retrieveFavouritesDashboards(
            final UserAccount userAcc,
            final Integer maxResults,
            final Integer start){
            final DetachedCriteria criteria = DetachedCriteria.forClass(Dashboard.class);
            criteria.createAlias("userBoard","userBoard");
            criteria.add(Restrictions.eq("favorite", Boolean.TRUE));
            criteria.add(Restrictions.eq("userBoard", userAcc));
            return (List<Dashboard>) filterByMaxorStart(criteria, maxResults, start);
        }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#getGadgetbyId(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public Gadget getGadgetbyIdandBoard(final Long gadgetId, final Dashboard dashboard){
         final DetachedCriteria criteria = DetachedCriteria.forClass(Gadget.class);
         criteria.add(Restrictions.eq("dashboard", dashboard));
         criteria.add(Restrictions.eq("gadgetId", gadgetId));
         //return (Gadget) getHibernateTemplate().get(Gadget.class, gadgetId);
         return (Gadget) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#getGadgetbyId(java.lang.Long)
     */
    public Gadget getGadgetbyId(final Long gadgetId) {
         return (Gadget) getHibernateTemplate().get(Gadget.class, gadgetId);
    }


    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#getGadgetbyKeyword(java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    public List<Gadget> getGadgetbyKeyword(final String keyword, final Integer maxResults, final Integer start){
        final DetachedCriteria criteria = DetachedCriteria.forClass(Gadget.class);
       // criteria.createAlias("gadgetName","gadgetName");
        criteria.add(Restrictions.like("gadgetName", keyword, MatchMode.ANYWHERE));
        return (List<Gadget>) filterByMaxorStart(criteria, maxResults, start);
        }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#retrieveDashboardbyKeyword(java.lang.String, java.lang.Long, java.lang.Integer, java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    public List<Dashboard> retrieveDashboardbyKeyword(final String keyword,
            final UserAccount userAcc,
            final Integer maxResults,
            final Integer start){
        final DetachedCriteria criteria = DetachedCriteria.forClass(Dashboard.class);
        criteria.createAlias("userBoard", "userBoard");
        criteria.add(Restrictions.eq("userBoard", userAcc));
        //TODO: Future update to hibernate search dashboard.
        criteria.add(Restrictions.like("pageBoardName", keyword, MatchMode.ANYWHERE));
        return (List<Dashboard>) filterByMaxorStart(criteria, maxResults, start);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#retrievePropertiesbyGadget(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public List retrievePropertiesbyGadget(final Long gadgetId){
        final DetachedCriteria criteria = DetachedCriteria.forClass(GadgetProperties.class);
        criteria.createAlias("gadget", "gadget");
        criteria.add(Restrictions.eq("gadget.gadgetId", gadgetId));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#retrieveGadgetsbyDashboard(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public List retrieveGadgetsbyDashboard(final Long boardId) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Gadget.class);
        criteria.createAlias("dashboard","dashboard");
        criteria.add(Restrictions.eq("dashboard.boardId", boardId));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#retrieveGadgetsbyType(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List retrieveGadgetsbyType(final GadgetType gadgetType) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Gadget.class);
        criteria.add(Restrictions.eq("gadgetType", gadgetType));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.persistence.dao.IDashboardDao#retrieveGadgets(java.lang.Boolean)
     */
    @SuppressWarnings("unchecked")
    public List retrieveGadgets(final Dashboard board){
        final DetachedCriteria criteria = DetachedCriteria.forClass(Gadget.class);
        criteria.add(Restrictions.eq("dashboard", board));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
