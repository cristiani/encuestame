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
package org.encuestame.core.service.imp;


/**
 * Interface to Service Manager.
 * @author Picado, Juan juanATencuestame.org
 * @since 26/04/2009 20:12:31
 * @version $Id$
 */
public interface IServiceManager{

    /**
     * Getter {@link IApplicationServices}.
     * @return {@link IApplicationServices}
     */
    IApplicationServices getApplicationServices();

}
