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
package org.encuestame.utils.json;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Unit Folder.
 * @author Picado, Juan juanATencuestame.org
 * @since Oct 9, 2010 12:34:11 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderBean implements Serializable {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -980650572883612949L;

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "name")
    private String folderName;

    @JsonProperty(value = "create_date")
    private Date createAt;

    @JsonProperty(value = "items")
    private Long items = 0L;

    public FolderBean() {
    }

    public FolderBean(Long id) {
        super();
        this.id = id;
    }

    /**
     * @return the id
     */
    @JsonIgnore
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the folderName
     */
    @JsonIgnore
    public String getFolderName() {
        return folderName;
    }

    /**
     * @param folderName the folderName to set
     */
    public void setFolderName(final String folderName) {
        this.folderName = folderName;
    }

    @JsonIgnore
    public Long getItems() {
        return items;
    }

    public void setItems(Long items) {
        this.items = items;
    }

    /**
     * @return the createAt
     */
    @JsonIgnore
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt the createAt to set
     */
    public void setCreateAt(final Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "FolderBean [id=" + id + ", folderName=" + folderName
                + ", createAt=" + createAt + "]";
    }


}
