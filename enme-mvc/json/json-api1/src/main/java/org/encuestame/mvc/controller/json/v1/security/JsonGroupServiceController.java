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
package org.encuestame.mvc.controller.json.v1.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.encuestame.core.util.ConvertDomainBean;
import org.encuestame.mvc.controller.AbstractJsonControllerV1;
import org.encuestame.persistence.domain.security.Group;
import org.encuestame.utils.web.UnitGroupBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Group Json Service Controller.
 *
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since December 16, 2010
 */

@Controller
public class JsonGroupServiceController extends AbstractJsonControllerV1 {

    /**
     * Log.
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * Create or Update Group.
     *
     * @param groupName
     * @param groupDesc
     * @param stateId
     * @param request
     * @param response
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @RequestMapping(value = "/api/groups/{type}Group.json", method = RequestMethod.GET)
    public @ResponseBody
    ModelMap createGroup(
            @RequestParam(value = "groupName", required = true) String groupName,
            @RequestParam(value = "groupDescription", required = false) String groupDesc,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @PathVariable String type, HttpServletRequest request,
            HttpServletResponse response) throws JsonGenerationException,
            JsonMappingException, IOException {
        try {
            // log.debug(" "+folderName);
            final Map<String, Object> sucess = new HashMap<String, Object>();
            if ("create".equals(type)) {
                final UnitGroupBean unitGroupBean = new UnitGroupBean();
                unitGroupBean.setGroupDescription(groupDesc == null ? ""
                        : groupDesc);
                unitGroupBean.setGroupName(groupName);
                unitGroupBean.setStateId(stateId); // TODO: remove
                sucess.put(
                        "groupBean",
                        getSecurityService().createGroup(unitGroupBean,
                                getUserPrincipalUsername()));
                setItemResponse(sucess);
            } else if ("update".equals(type)) {
                final Group groupDomain = getSecurityService()
                        .getGroupbyIdandUser(groupId,
                                getUserPrincipalUsername());// find by group Id
                                                            // and principal
                                                            // User.
                if (groupDomain != null) {
                    final UnitGroupBean groupBean = ConvertDomainBean
                            .convertGroupDomainToBean(groupDomain);
                    groupBean.setGroupDescription(groupDesc);
                    // TODO: Set state
                    if (!groupName.isEmpty()) {
                        groupBean.setGroupName(groupName);
                    }
                    getSecurityService().updateGroup(groupBean);
                    sucess.put("g", groupBean);
                    setItemResponse(sucess);
                }
            }
        } catch (Exception e) {
            log.error(e);
            setError(e.getMessage(), response);
        }
        return returnData();
    }

    /**
     * Remove Group.
     *
     * @param groupId
     * @param request
     * @param response
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @RequestMapping(value = "/api/groups/removeGroup.json", method = RequestMethod.GET)
    public @ResponseBody
    ModelMap deleteGroup(
            @RequestParam(value = "groupId", required = true) Long groupId,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonGenerationException, JsonMappingException, IOException {
        try {
            log.debug("Group Id" + groupId);
            getSecurityService().deleteGroup(groupId);
            setSuccesResponse();
        } catch (Exception e) {
            log.error(e);
            setError(e.getMessage(), response);
        }
        return returnData();
    }

    /**
     * Load Groups.
     *
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @RequestMapping(value = "/api/groups/groups.json", method = RequestMethod.GET)
    public @ResponseBody
    ModelMap loadGroups(HttpServletRequest request, HttpServletResponse response)
            throws JsonGenerationException, JsonMappingException, IOException {
        try {
            final Map<String, Object> jsonResponse = new HashMap<String, Object>();
            jsonResponse
                    .put("groups",
                            getSecurityService().loadGroups(
                                    getUserPrincipalUsername()));
            setItemResponse(jsonResponse);
        } catch (Exception e) {
            log.error(e);
            setError(e.getMessage(), response);
        }
        return returnData();
    }

    /**
     * Get Users by Group.
     *
     * @param request
     * @param response
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @RequestMapping(value = "/api/groups/countUsersByGroup.json", method = RequestMethod.GET)
    public @ResponseBody
    ModelMap countUsersByGroup(
            @RequestParam(value = "groupId", required = true) Long groupId,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonGenerationException, JsonMappingException, IOException {
        try {
            final Map<String, Object> sucess = new HashMap<String, Object>();
            log.debug("Group Id " + groupId);
            sucess.put(
                    "userGroup",
                    getSecurityService().getUserbyGroup(groupId,
                            getUserPrincipalUsername()));
            setItemResponse(sucess);
        } catch (Exception e) {
            log.error(e);
            ////e.printStackTrace();
        }
        return returnData();
    }
}
