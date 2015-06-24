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
import org.encuestame.persistence.domain.dashboard.Dashboard;
import org.encuestame.persistence.domain.dashboard.Gadget;
import org.encuestame.utils.categories.test.DefaultTest;
import org.encuestame.utils.enums.MethodJson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Description.
 * @author Picado, Juan juanATencuestame.org
 * @since 04/08/2011
 */
@Category(DefaultTest.class)
public class DashboardJsonControllerTestCase  extends AbstractJsonV1MvcUnitBeans{


    /**
     * Test Create Dashboard.
     * @throws IOException
     * @throws ServletException
     *
     */
    @Test
    public void testCreateDashboard() throws ServletException, IOException{
        initService("/api/common/dashboard", MethodJson.POST);
        setParameter("name", "stream");
        setParameter("desc", "test");
        setParameter("favorite", "true");
        setParameter("layout", "BA");
        final JSONObject response = callJsonService();
        //{"error":{},"success":{"dashboard":{"dashboard_name":"test","id":7608,"sequence":null,
        //"layout":"AAA","favorite":true,"favorite_counter":null,"dashboard_description":"test"}}}
        final JSONObject success = getSucess(response);
        final JSONObject dashboard = (JSONObject) success.get("dashboard");
        Assert.assertEquals(dashboard.get("dashboard_name").toString(), "stream");
        Assert.assertEquals(dashboard.get("layout").toString(), "BA");
        Assert.assertEquals(dashboard.get("favorite").toString(), "true");
        Assert.assertEquals(dashboard.get("dashboard_description").toString(), "test");
    }

    /**
     *
     * @throws ServletExceptionreateDashboardDefault(getSpringSecurityLoggedUserAccount())
     * @throws IOException
     */
    @Test
    public void testgetGadgets() throws ServletException, IOException{
        Dashboard dash = createDashboardDefault(getSpringSecurityLoggedUserAccount());
        createGadgetDefault(dash);
        createGadgetDefault(dash);
        initService("/api/common/gadgets/list.json", MethodJson.GET);
        setParameter("dashboardId", dash.getBoardId().toString());
        final JSONObject response2 = callJsonService();
        final JSONObject success2 = getSucess(response2);
        final JSONArray gadgets2 = (JSONArray) success2.get("gadgets");
        Assert.assertEquals(gadgets2.size(), 2);
    }


    /**
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
	public void testgetGadgetsFail() throws ServletException, IOException {
		initService("/api/common/gadgets/list.json", MethodJson.GET);
		setParameter("dashboardId", "1");

		final JSONObject response = callJsonService();
		final String error = getErrorsMessage(response);
		Assert.assertEquals(error, "dashboard id is missing");
    }

    /**
     * Test Get my dashboards json service.
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testGetMyDashboards() throws ServletException, IOException {
        createDashboardDefault(getSpringSecurityLoggedUserAccount());
        createDashboard("My second board", Boolean.TRUE, getSpringSecurityLoggedUserAccount());
        initService("/api/common/dashboard", MethodJson.GET);
        final JSONObject response = callJsonService();
        final JSONObject success = getSucess(response);
        // {"error":{},"success":{"items":[],"label":"hashTagName","identifier":"id"}}
        final JSONArray myDashboards = (JSONArray) success.get("items");
        final String label = (String) success.get("label");
        final String identifier = (String) success.get("identifier");
        Assert.assertNotNull(success);
        Assert.assertEquals(label, "dashboard_name");
        Assert.assertEquals(identifier, "id");
        Assert.assertEquals(myDashboards.size(), 2);
    }

    /**
     * Test add gadget on dashboard.
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testAddGadgetOnDashboard() throws ServletException, IOException{
		final Dashboard myBoard = createDashboard("My Third board",
				Boolean.TRUE, getSpringSecurityLoggedUserAccount());
		final Gadget myGadget = createGadgetDefault(myBoard);
		initService("/api/common/stream/gadget.json", MethodJson.POST);
		setParameter("boardId", myBoard.getBoardId().toString());
		final JSONObject response = callJsonService();
		final JSONObject success = getSucess(response);
		final JSONObject gadget = (JSONObject) success.get("gadget");
		final String message = (String) gadget.get("gadget_name");
		Assert.assertEquals(message, "stream");
    }


    @Test
    public void testAddInvalidGadgetOnDashboard() throws ServletException, IOException{
		final Dashboard myBoard = createDashboard("My Third board",
				Boolean.TRUE, getSpringSecurityLoggedUserAccount());
		createGadgetDefault(myBoard);
		// invalid gadget
		initService("/api/common/notvalid/gadget.json", MethodJson.POST);
		setParameter("boardId", myBoard.getBoardId().toString());
		final JSONObject response_error = callJsonService();
		final JSONObject error = getErrors(response_error);
		final String message_error = (String) error.get("message");
		Assert.assertEquals(message_error, "gadget invalid");
    }

    /**
     * Test move gadget on dashboard json.
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testMoveGadgetOnDashboard() throws ServletException, IOException{
        final Dashboard myBoard = createDashboard("My Surveys board", Boolean.TRUE, getSpringSecurityLoggedUserAccount());
        final Gadget myGadget = createGadgetDefault(myBoard);
        initService("/api/common/" + myGadget.getGadgetId().toString() + "/gadget.json", MethodJson.PUT);
        setParameter("position", "3" );
        setParameter("column",  "2");
        setParameter("dashboardId", myBoard.getBoardId().toString());
        final JSONObject response = callJsonService();
        assertSuccessResponse(response);
    }

    /**
     * Test remove gadget on dashboard.
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testRemoveGadgetOnDashboard() throws ServletException, IOException{
        final Dashboard tpBoard = createDashboard("My TweetPoll board", Boolean.TRUE, getSpringSecurityLoggedUserAccount());
        final Gadget myGadget = createGadgetDefault(tpBoard);
        initService("/api/common/" + myGadget.getGadgetId().toString() + "/gadget.json", MethodJson.DELETE);
        //setParameter("gadgetId", myGadget.getGadgetId().toString() );
        setParameter("dashboardId", tpBoard.getBoardId().toString());
        final JSONObject response = callJsonService();
        assertSuccessResponse(response);
    }
}
