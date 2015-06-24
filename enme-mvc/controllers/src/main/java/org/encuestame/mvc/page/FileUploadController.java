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
package org.encuestame.mvc.page;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.encuestame.business.images.ThumbnailGeneratorEngine;
import org.encuestame.core.files.PathUtil;
import org.encuestame.mvc.controller.AbstractBaseOperations;
import org.encuestame.mvc.controller.AbstractViewController;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.persistence.exception.EnmeFailOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * Upload File Controller.
 * @author Picado, Juan juanATencuestame.org
 * @since Jan 16, 2011 11:47:33 AM
 * @version $Id:$
 */
@Controller
public class FileUploadController extends AbstractViewController {

    /**
     * Log.
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * {@link ThumbnailGeneratorEngine}.
     */
    @Autowired
    private ThumbnailGeneratorEngine thumbnailGeneratorEngine;

    /**
     * Upload Profile for User Account.
     * @param multipartFile
     * @return
     */
    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @RequestMapping(value = "/file/upload/profile", method = RequestMethod.POST)
    public ModelAndView handleUserProfileFileUpload(
            @RequestParam("file") MultipartFile multipartFile) {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        if (!multipartFile.isEmpty()) {
            log.debug(multipartFile.getName());
            String orgName = multipartFile.getOriginalFilename();
            log.debug("org name "+orgName);
            //TODO: convert name to numbers, MD5 hash.
            String filePath = null;
            try {
                log.debug("getting file path for this user");
                filePath = getPictureService().getAccountUserPicturePath(getSecurityService().getUserAccount(getUserPrincipalUsername()));
                InputStream stream = multipartFile.getInputStream();
                try {
                    //generate thumbnails
                    thumbnailGeneratorEngine.generateThumbnails(
                            PathUtil.DEFAUL_PICTURE_PREFIX,
                            stream,
                            multipartFile.getContentType(),
                            filePath);
                } catch (Exception e) {
                    //e.printStackTrace();
                    log.error(e);
                } finally {
                    stream.close();
                }
                //TODO: after save image, we need relationship user with profile picture.
                //I suggest store ID on user account table, to retrieve easily future profile image.
                //BUG 102
            } catch (IllegalStateException e) {
                //e.printStackTrace();
                log.error("File uploaded failed:" + orgName);
            } catch (IOException e) {
                //e.printStackTrace();
                log.error("File uploaded failed:" + orgName);
            } catch (EnMeNoResultsFoundException e) {
                ///e.printStackTrace();
                log.error("File uploaded failed:" + orgName);
            } catch (EnmeFailOperation e) {
                //e.printStackTrace();
                log.error("File uploaded failed:" + orgName);
            }
            // Save the file here
            mav.addObject("status", "saved");
            mav.addObject("id", filePath);
        } else {
            mav.addObject("status", "failed");
        }
        return mav;
    }

    /**
     * @return the thumbnailGeneratorEngine
     */
    public ThumbnailGeneratorEngine getThumbnailGeneratorEngine() {
        return thumbnailGeneratorEngine;
    }

    /**
     * @param thumbnailGeneratorEngine the thumbnailGeneratorEngine to set
     */
    public void setThumbnailGeneratorEngine(
            ThumbnailGeneratorEngine thumbnailGeneratorEngine) {
        this.thumbnailGeneratorEngine = thumbnailGeneratorEngine;
    }



    /**  TODO: we can add more methods to upload different types of files. **/
}
