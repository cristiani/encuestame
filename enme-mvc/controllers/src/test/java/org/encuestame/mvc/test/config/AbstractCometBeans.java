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
package org.encuestame.mvc.test.config;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;

/**
 * Abstract mvc unit beans.
 * @author Picado, Juan juanATencuestame.org
 * @since April 10, 2011
 */
@ContextConfiguration(locations = {
        "classpath:spring-test/encuestame-test-comet-context.xml"})
public abstract class AbstractCometBeans extends AbstractMvcUnitBeans {

    /**
     *
     */
    @Before
    public void setUp2() {
    }
}
