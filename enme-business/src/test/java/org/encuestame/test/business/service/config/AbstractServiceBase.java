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
package org.encuestame.test.business.service.config;

import org.encuestame.core.config.EnMePlaceHolderConfigurer;
import org.encuestame.test.config.AbstractBaseUnitBeans;
import org.junit.After;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

/**
 * Description Class.
 * @author Picado, Juan juanATencuestame.org
 * @since Oct 23, 2010 10:42:59 AM
 */
@ContextConfiguration(locations = {
        "classpath:spring-test/encuestame-test-email-context.xml",
        "classpath:spring-test/encuestame-test-security-context.xml",
        "classpath:spring-test/encuestame-test-service-context.xml",
        //"classpath:spring-test/encuestame-test-integration.xml",
        "classpath:spring-test/encuestame-test-search-context.xml",
        "classpath:spring-test/encuestame-param-test-context.xml"
         })
public abstract class AbstractServiceBase extends AbstractBaseUnitBeans{


    public File targetDir() {
        String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        File targetDir = new File(relPath);
        if(!targetDir.exists()) {
            targetDir.mkdir();
        }
        return targetDir;
    }


    @Before
    public void baseInitConfigBefore() {
        EnMePlaceHolderConfigurer.setProperty("encuestame.home", targetDir().getAbsolutePath());
    }

    @After
    public void baseInitConfigAfter(){
        //System.out.println("home-->"+EnMePlaceHolderConfigurer.getProperty("encuestame.home"));
    }


    /**
     * Constructror.
     */
    public AbstractServiceBase() {
    }

    /**
     *
     * @param o
     */
    public void logPrint(Object o){
        //System.out.println("debug::==>::" + o);
    }
}
