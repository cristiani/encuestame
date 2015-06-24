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
package org.encuestame.core.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.encuestame.core.config.EnMePlaceHolderConfigurer;
import org.encuestame.core.util.ConvertDomainBean;
import org.encuestame.persistence.dao.CommentsOperations;
import org.encuestame.persistence.dao.IAccountDao;
import org.encuestame.persistence.dao.IClientDao;
import org.encuestame.persistence.dao.IDashboardDao;
import org.encuestame.persistence.dao.IEmail;
import org.encuestame.persistence.dao.IFrontEndDao;
import org.encuestame.persistence.dao.IGeoPoint;
import org.encuestame.persistence.dao.IGeoPointTypeDao;
import org.encuestame.persistence.dao.IGroupDao;
import org.encuestame.persistence.dao.IHashTagDao;
import org.encuestame.persistence.dao.INotification;
import org.encuestame.persistence.dao.IPermissionDao;
import org.encuestame.persistence.dao.IPoll;
import org.encuestame.persistence.dao.IProjectDao;
import org.encuestame.persistence.dao.IQuestionDao;
import org.encuestame.persistence.dao.ISurvey;
import org.encuestame.persistence.dao.ITweetPoll;
import org.encuestame.persistence.dao.imp.AccountDaoImp;
import org.encuestame.persistence.dao.imp.ClientDao;
import org.encuestame.persistence.dao.imp.GeoPointTypeDao;
import org.encuestame.persistence.dao.imp.HashTagDao;
import org.encuestame.persistence.dao.imp.NotificationDao;
import org.encuestame.persistence.dao.imp.ProjectDaoImp;
import org.encuestame.persistence.domain.GeoPoint;
import org.encuestame.persistence.domain.HashTag;
import org.encuestame.persistence.domain.Project;
import org.encuestame.persistence.domain.security.Account;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.exception.EnMeExpcetion;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.utils.ValidationUtils;
import org.encuestame.utils.web.UnitProjectBean;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Abstract Data Services.
 * @author Picado, Juan juanATencuestame.org
 * @since April 27, 2009
 * @version $Id: DataSource.java 478 2010-04-07 03:39:10Z dianmorales $
 */
@Service
public abstract class AbstractDataSource extends AbstractSecurityContext{

    /** {@link GeoPoint}. */
    @Autowired
    private IGeoPoint geoPointDao;
    /** {@link GeoPointTypeDao}. */
    @Autowired
    private IGeoPointTypeDao geoPointTypeDao;
    /** {@link ProjectDaoImp}. */
    @Autowired
    private IProjectDao projectDaoImp;
    /** {@link ClientDao}. **/
    @Autowired
    private IClientDao clientDao;
    /** {@link AccountDaoImp}. **/
    @Autowired
    private IAccountDao accountDao;
    /** {@link HashTagDao}. **/
    @Autowired
    private IHashTagDao hashTagDao;
    /*** {@link NotificationDao}. **/
    @Autowired
    private INotification notificationDao;
    /** {@link FrontEndService}. **/
    @Autowired
    private IFrontEndDao frontEndDao;
    /** Log. */
    private Log log = LogFactory.getLog(this.getClass());

    /** {@link IQuestionDao}**/
    @Autowired
    private IQuestionDao questionDao;

    /**{@link IPoll}**/
    @Autowired
    private IPoll pollDao;

    @Autowired
    private ISurvey surveyDaoImp;

    /**{@link ITweetPoll}**/
    @Autowired
    private ITweetPoll tweetPollDao;

    /** {@link IGroupDao}. **/
    @Autowired
    private IGroupDao groupDao;

    /** {@link IPermissionDao} **/
    @Autowired
    private IPermissionDao permissionDao;

   /** {@link IEmail} **/
    @Autowired
    private IEmail emailListsDao;

    /** {@link IDashboardDao} **/
    @Autowired
    private IDashboardDao dashboardDao;

    @Autowired
    private CommentsOperations commentsOperations;

    /**
     *
     */
    @Before
    public void initTestSec() {
        EnMePlaceHolderConfigurer.setSystemInitialized(false);
    }

    /**
     * Get {@link UserAccount} by Username.
     * @param username username
     * @return user domain
     * @throws EnMeNoResultsFoundException exception
     */
    public final UserAccount getUserAccount(final String username) throws EnMeNoResultsFoundException {
        log.debug("getUserAccount username:: "+username);
        final UserAccount userAccount =  this.findUserByUserName(username);
        if (userAccount == null) {
            log.info(" user not found {"+username+"}");
            throw new EnMeNoResultsFoundException(" user not found {"+username+"}");
        } else {
            //TODO: we can add others validations, like is disabled, banned or the account is expired.
            return userAccount;
        }
    }

    /**
     * Get {@link UserAccount} by Id.
     * @param userId user id
     * @return {@link UserAccount}.
     * @throws EnMeNoResultsFoundException
     */
   public final UserAccount getUserAccount(final Long userId) throws EnMeNoResultsFoundException {
        final UserAccount userAccount = getAccountDao().getUserAccountById(userId);
        if(userAccount == null){
            throw new EnMeNoResultsFoundException(" user id not found {"+userId+"}");
        } else {
            //TODO: we can add others validations, like is disabled, banned or the account is expired.
            return userAccount;
        }
    }

    /**
     * Find {@link UserAccount} by UserName
     * @param username user name
     * @return {@link UserAccount}
     */
    public UserAccount findUserByUserName(final String username) {
        log.trace("findUserByUserName username: "+username);
        return getAccountDao().getUserByUsername(username);
    }

    /**
     * Find {@link UserAccount} by email.
     * @param email
     * @return
     */
    public UserAccount findUserAccountByEmail(final String email) {
        return getAccountDao().getUserByEmail(email);
    }

    /**
     * Get Primary User Id.
     * @param username
     * @return
     * @throws EnMeNoResultsFoundException exception
     */
    public final Long getUserAccountId(final String username) throws EnMeNoResultsFoundException{
        return getAccount(username).getUid();
     }

    /**
     * Get {@link Account}.
     * @param username
     * @return
     * @throws EnMeNoResultsFoundException
     */
    public Account getAccount(final String username) throws EnMeNoResultsFoundException{
        return getUserAccount(username).getAccount();
     }

    /**
     * Load List of Project.
     * @param userId user id.
     * @return {@link Collection} of {@link UnitProjectBean}
     * @throws EnMeExpcetion exception
     */
    public final Collection<UnitProjectBean> loadListProjects(final Long userId) {
            final Collection<UnitProjectBean> listProjects = new LinkedList<UnitProjectBean>();
            final Collection<Project> projectList = getProjectDaoImp().findProjectsByUserID(userId);
            log.info("project by user id: "+projectList.size());
            for (Project project : projectList) {
                log.info("adding project "+project.getProjectDescription());
                log.info("groups available in this project "+project.getGroups().size());
                listProjects.add(ConvertDomainBean.convertProjectDomainToBean(project));
            }
            log.info("projects loaded: "+ listProjects.size());
            return listProjects;
    }

    /**
     * Load project info.
     * @param projectBean {@link Project}
     * @return {@link UnitProjectBean}
     * @throws EnMeExpcetion excepcion
     */
    public UnitProjectBean loadProjectInfo(final UnitProjectBean projectBean) throws EnMeExpcetion {
        if (projectBean.getId()!= null) {
            final Project projectDomain = getProjectDaoImp().getProjectbyId(projectBean.getId());
            if (projectDomain != null) {
                final UnitProjectBean projectBeanRetrieved = ConvertDomainBean.convertProjectDomainToBean(projectDomain);
                //projectBeanRetrieved.setGroupList(ConvertListDomainSelectBean.convertListGroupDomainToSelect(projectDomain.getGroups()));
                return projectBeanRetrieved;
            } else {
                log.info("id project is not found");
                throw new EnMeExpcetion("id project is not found");
            }
        } else {
            log.info("id project is null");
            throw new EnMeExpcetion("id project is null");
        }
    }

    /**
     * Create Project.
     * @param projectBean {@link UnitProjectBean}
     * @return {@link UnitProjectBean}
     * @throws EnMeExpcetion exception
     */
    public final UnitProjectBean createProject(final UnitProjectBean projectBean) throws EnMeExpcetion {
        log.info("create project");
        if (projectBean != null) {
            try {
                final Project projectDomain = new Project();
                //projectDomain.setStateProject(getState(projectBean.getState()));
                projectDomain.setProjectDateFinish(projectBean.getDateFinish());
                projectDomain.setProjectDateStart(projectBean.getDateInit());
                projectDomain.setProjectDescription(projectBean.getName());
                projectDomain.setProjectInfo(projectBean.getDescription());
                projectDomain.setHideProject(projectBean.getHide());
                projectDomain.setNotifyMembers(projectBean.getNotify());
                if(projectBean.getLeader()!=null){
                    projectDomain.setLead(getAccountDao().getUserAccountById(projectBean.getLeader()));
                }
                projectDomain.setUsers(getAccountDao().getUserById(projectBean.getUserId()));
                getProjectDaoImp().saveOrUpdate(projectDomain);
                projectBean.setId(projectDomain.getProyectId());
                log.debug("created domain project");
            } catch (HibernateException e) {
                throw new EnMeExpcetion(e);
            } catch (Exception e) {
                throw new EnMeExpcetion(e);
            }
            return projectBean;
        } else {
            throw new EnMeExpcetion("project is null");
        }
    }

    /**
     * Create {@link HashTag}.
     * @param name tag name
     * @return {@link HashTag}.
     */
    public final HashTag createHashTag(final String name){
        final HashTag hashTag = new HashTag();
        hashTag.setHashTag(ValidationUtils.removeNonAlphanumericCharacters(name));
        hashTag.setHits(1L); //FIXME: should be parametrized ?
        hashTag.setSize(12L); //FIXME: should be parametrized ?
        hashTag.setUpdatedDate(Calendar.getInstance().getTime());
        getHashTagDao().saveOrUpdate(hashTag);
        return hashTag;
    }

    /**
     * @return the geoPointDao
     */
    public final IGeoPoint getGeoPointDao() {
        return geoPointDao;
    }

    /**
     * @param geoPointDao the geoPointDao to set
     */

    public final void setGeoPointDao(final IGeoPoint geoPointDao) {
        this.geoPointDao = geoPointDao;
    }

    /**
     * @return the projectDaoImp
     */
    public final IProjectDao getProjectDaoImp() {
        return projectDaoImp;
    }

    /**
     * @param projectDaoImp the projectDaoImp to set
     */
    public void setProjectDaoImp(final IProjectDao projectDaoImp) {
        this.projectDaoImp = projectDaoImp;
    }

    /**
     * @return the geoPointTypeDao
     */
    public final IGeoPointTypeDao getGeoPointTypeDao() {
        return geoPointTypeDao;
    }

    /**
     * @param geoPointTypeDao the geoPointTypeDao to set
     */
    public final void setGeoPointTypeDao(final IGeoPointTypeDao geoPointTypeDao) {
        this.geoPointTypeDao = geoPointTypeDao;
    }

    /**
     * @return the clientDao
     */
    public final IClientDao getClientDao() {
        return clientDao;
    }

    /**
     * @param clientDao the clientDao to set
     */
    public final void setClientDao(final IClientDao clientDao) {
        this.clientDao = clientDao;
    }

    /**
     * @return the accountDao
     */
    public final IAccountDao getAccountDao() {
        return accountDao;
    }

    /**
     * @param accountDao the accountDao to set
     */
    public final void setAccountDao(final IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * @return the questionDao
     */
    public final IQuestionDao getQuestionDao() {
        return questionDao;
    }

    /**
     * @param questionDao the questionDao to set
     */
    public final void setQuestionDao(final IQuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    /**
     * @return the pollDao
     */
    public final IPoll getPollDao() {
        return pollDao;
    }

    /**
     * @param pollDao the pollDao to set
     */
    public final void setPollDao(final IPoll pollDao) {
        this.pollDao = pollDao;
    }

    /**
     * @return the surveyDaoImp
     */
    public final ISurvey getSurveyDaoImp() {
        return surveyDaoImp;
    }

    /**
     * @param surveyDaoImp the surveyDaoImp to set
     */
    public final void setSurveyDaoImp(final ISurvey surveyDaoImp) {
        this.surveyDaoImp = surveyDaoImp;
    }

    /**
     * @return the tweetPollDao
     */
    public ITweetPoll getTweetPollDao() {
        return tweetPollDao;
    }

    /**
     * @param tweetPollDao the tweetPollDao to set
     */
    public void setTweetPollDao(final ITweetPoll tweetPollDao) {
        this.tweetPollDao = tweetPollDao;
    }

    /**
     * @return the groupDao
     */
    public final IGroupDao getGroupDao() {
        return groupDao;
    }

    /**
     * @param groupDao the groupDao to set
     */
    public final void setGroupDao(final IGroupDao groupDao) {
        this.groupDao = groupDao;
    }

    /**
     * @return the permissionDao
     */
    public final IPermissionDao getPermissionDao() {
        return permissionDao;
    }

    /**
     * @param permissionDao the permissionDao to set
     */
    public final void setPermissionDao(IPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    /**
     * @return the emailListsDao
     */
    public final IEmail getEmailListsDao() {
        return emailListsDao;
    }

    /**
     * @param emailListsDao the emailListsDao to set
     */
    public final void setEmailListsDao(final IEmail emailListsDao) {
        this.emailListsDao = emailListsDao;
    }

    /**
     * @return the hashTagDao
     */
    public IHashTagDao getHashTagDao() {
        return hashTagDao;
    }

    /**
     * @param hashTagDao the hashTagDao to set
     */
    public void setHashTagDao(final IHashTagDao hashTagDao) {
        this.hashTagDao = hashTagDao;
    }

    /**
     * @return the notificationDao
     */
    public final INotification getNotificationDao() {
        return notificationDao;
    }

    /**
     * @param notificationDao the notificationDao tgetNotificationDaoo set
     */
    public final void setNotificationDao(final INotification notificationDao) {
        this.notificationDao = notificationDao;
    }

    /**
     * Getter Front End.
     * @return the frontEndDao
     */
    public final IFrontEndDao getFrontEndDao() {
        return frontEndDao;
    }

    /**
     * @param frontEndDao the frontEndDao to set
     */
    public final void setFrontEndDao(final IFrontEndDao frontEndDao) {
        this.frontEndDao = frontEndDao;
    }

    /**
     * @return the dashboardDao
     */
    public IDashboardDao getDashboardDao() {
        return dashboardDao;
    }

    /**
     * @param dashboardDao the dashboardDao to set
     */
    public void setDashboardDao(final IDashboardDao dashboardDao) {
        this.dashboardDao = dashboardDao;
    }

    /**
     * @return the commentsOperations
     */
    public CommentsOperations getCommentsOperations() {
        return commentsOperations;
    }

    /**
     * @param commentsOperations the commentsOperations to set
     */
    public void setCommentsOperations(final CommentsOperations commentsOperations) {
        this.commentsOperations = commentsOperations;
    }

}
