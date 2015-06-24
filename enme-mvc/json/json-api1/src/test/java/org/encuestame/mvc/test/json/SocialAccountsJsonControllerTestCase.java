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
package org.encuestame.mvc.test.json;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.encuestame.mvc.test.config.AbstractJsonV1MvcUnitBeans;
import org.encuestame.persistence.domain.security.SocialAccount;
import org.encuestame.utils.categories.test.DefaultTest;
import org.encuestame.utils.enums.MethodJson;
import org.encuestame.utils.social.SocialProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Social Account Json Service Test Cases.
 * @author Picado, Juan juanATencuestame.org
 * @since  Feb 19, 2011 13:20:58 AM
 */
@Category(DefaultTest.class)
public class SocialAccountsJsonControllerTestCase extends AbstractJsonV1MvcUnitBeans{

    /**
     *
     */
    private SocialAccount socialAccount;

      /**
       *
       */
      @Before
      public void beforeSocialTest(){
          this.socialAccount = createDefaultSettedSocialAccount(getSpringSecurityLoggedUserAccount());
          createSocialProviderAccount(getSpringSecurityLoggedUserAccount(), SocialProvider.TWITTER);
      }

      /**
       * test load social accounts.
       * @throws ServletException
       * @throws IOException
       */
      @Test
      public void testLoadSocialAccounts() throws ServletException, IOException{
          initService("/api/common/social/accounts.json", MethodJson.GET);
          setParameter("provider", "twitter");
          final org.json.simple.JSONObject response = callJsonService();
          //System.out.println(response);
          //{"error":{},"success":{"items":[],"label":"socialAccounts","identifier":"id"}}
          org.json.simple.JSONArray list = (org.json.simple.JSONArray) getSucess(response).get("items");
          Assert.assertEquals(list.size(), 2);
      }

      /**
       * test providers service.
       * @throws ServletException
       * @throws IOException
       */
      //@Test
      //FIXME
      //estGetProviders(org.encuestame.mvc.test.json.SocialAccountsJsonControllerTestCase): Could not resolve view with name 'api/common/social/providers' in servlet with name ''
      public void testGetProviders() throws ServletException, IOException{
          initService("/api/common/social/providers.json", MethodJson.GET);
          setParameter("provider", "twitter");
          final org.json.simple.JSONObject response = callJsonService();
          //{"error":{},"success":{"items":[],"label":"socialAccounts","identifier":"id"}}
          org.json.simple.JSONArray list = (org.json.simple.JSONArray) getSucess(response).get("provider");
          Assert.assertEquals(list.size(), 5);
      }
}
