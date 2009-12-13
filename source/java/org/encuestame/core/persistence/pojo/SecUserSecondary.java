/*
 ************************************************************************************
 * Copyright (C) 2001-2009 encuestame: system online surveys Copyright (C) 2009
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.core.persistence.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Security User Secondary.
 * @author Picado, Juan juan@encuestame.org
 * @since 08/12/2009 19:01:26
 * @version $Id$
 */
@Entity
@Table(name = "sec_user_secondary")
public class SecUserSecondary {

    private Long uid;
    private String completeName;
    private String userEmail;
    private String username;
    private String password;
    private SecUsers secUser;
    private String inviteCode;
    private Date enjoyDate;
    private Boolean isPublisher;
    private Boolean isOwner;
    private Boolean userStatus;
    private String userTwitterAccount;

    private Set<SecGroupUser> secGroupUsers = new HashSet<SecGroupUser>(0);
    private Set<SecUserPermission> secUserPermissions = new HashSet<SecUserPermission>(
            0);

    /**
     * @return uid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uid", unique = true, nullable = false)
    public Long getUid() {
        return this.uid;
    }

    /**
     * @param uid uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }


    /**
     * @return completeName
     */
    @Column(name = "name", nullable = false, length = 50)
    public String getCompleteName() {
        return this.completeName;
    }

    /**
     * @param completeName completeName
     */
    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    /**
     * @return userEmail userEmail
     */
    @Column(name = "email", unique = true, nullable = false, length = 100)
    public String getUserEmail() {
        return this.userEmail;
    }

    /**
     * @param userEmail userEmail
     */
    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return username
     */
    @Column(name = "username")
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return the secUser
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    public SecUsers getSecUser() {
        return secUser;
    }

    /**
     * @param secUser the secUser to set
     */
    public void setSecUser(SecUsers secUser) {
        this.secUser = secUser;
    }

    /**
     * @return inviteCode
     */
    @Column(name = "invite_code")
    public String getInviteCode() {
        return this.inviteCode;
    }

    /**
     * @param inviteCode inviteCode
     */
    public void setInviteCode(final String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * @return enjoyDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_new", nullable = true, length = 0)
    public Date getEnjoyDate() {
        return this.enjoyDate;
    }

    /**
     * @param enjoyDate enjoyDate
     */
    public void setEnjoyDate(final Date enjoyDate) {
        this.enjoyDate = enjoyDate;
    }

    /**
     * @return isPublisher
     */
    @Column(name = "publisher", nullable = true, length = 2)
    public Boolean getPublisher() {
        return this.isPublisher;
    }

    /**
     * @param isPublisher isPublisher
     */
    public void setPublisher(final Boolean isPublisher) {
        this.isPublisher = isPublisher;
    }

    /**
     * @return isOwner
     */
    @Column(name = "owner", length = 2)
    public Boolean getOwner() {
        return this.isOwner;
    }

    /**
     * @param isOwner isOwner
     */
    public void setOwner(final Boolean isOwner) {
        this.isOwner = isOwner;
    }

    /**
     * @return userStatus
     */
    @Column(name = "status", nullable = true)
    public Boolean isUserStatus() {
        return this.userStatus;
    }

    /**
     * @param userStatus userStatus
     */
    public void setUserStatus(final boolean userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * @return secGroupUsers
     */
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "secUsers")
    public Set<SecGroupUser> getSecGroupUsers() {
        return this.secGroupUsers;
    }

    /**
     * @param secGroupUsers secGroupUsers
     */
    public void setSecGroupUsers(final Set<SecGroupUser> secGroupUsers) {
        this.secGroupUsers = secGroupUsers;
    }

    /**
     * @return secUserPermissions
     */
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "secUsers")
    public Set<SecUserPermission> getSecUserPermissions() {
        return this.secUserPermissions;
    }

    /**
     * @param secUserPermissions secUserPermissions
     */
    public void setSecUserPermissions(final Set<SecUserPermission> secUserPermissions) {
        this.secUserPermissions = secUserPermissions;
    }

    /**
     * @return userTwitterAccount
     */
    @Column(name = "twitter", length = 2)
    public String getUserTwitterAccount() {
        return this.userTwitterAccount;
    }

    /**
     * @param userTwitterAccount userTwitterAccount
     */
    public void setUserTwitterAccount(final String userTwitterAccount) {
        this.userTwitterAccount = userTwitterAccount;
    }

}
