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
package org.encuestame.mvc.test.config.v2;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.custommonkey.xmlunit.XMLUnit;
import org.encuestame.core.util.JSONUtils;
import org.encuestame.test.business.security.AbstractSpringSecurityContext;
import org.encuestame.utils.enums.MethodJson;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 *
 * @author Picado, Juan juanATencuestame.org
 * @since Sep 26, 2010 8:12:32 PM
 * @version Id:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:spring-test/encuestame-test-json-controller-context.xml",
    "classpath:spring-test/encuestame-test-rss-context.xml",
    "classpath:spring-test/encuestame-test-upload-context.xml"})
public abstract class AbstractJsonMvcUnitBeans extends AbstractSpringSecurityContext {


    @Autowired
    private WebApplicationContext wac;

    public MockMvc mockMvc;

    /**
     * Fake Request.
     */
    protected MockHttpServletRequest request;

    /**
     * Fake Response.
     */
    protected MockHttpServletResponse response;

    /**
     * Fake Session.
     */
    protected MockHttpSession session;

    /**
     * Json View.
     */
    public MappingJackson2JsonView jacksonJsonView = new MappingJackson2JsonView();

    /**
     * Model and View.
     */
    public ModelAndView modelAndView = new ModelAndView();

    // this servlet is going to be instantiated by ourselves
    // so that we can test the servlet behaviour w/o actual web container
    // deployment
    protected DispatcherServlet servlet;

    /**
     * Constructor.
     */
    public AbstractJsonMvcUnitBeans() {
        //jacksonJsonView.setPrefixJson(false);
        //jacksonJsonView.setDisableCaching(true);
        //session = new MockHttpSession();
        //modelAndView.setView(jacksonJsonView);
    }

    /**
     * Call Json Service.
     * @param url
     * @param method
     * @throws ServletException
     */
    public void initService(final String url, final MethodJson method){
        Assert.assertNotNull(url);
        Assert.assertNotNull(method);
        this.request = new MockHttpServletRequest(method.name(), url);
        this.response = new MockHttpServletResponse();
    }

    /**
     * Call String Service.
     * @return
     * @throws ServletException servletException
     * @throws IOException io exception.
     */
    private String callStringService() throws ServletException, IOException{
        servlet.init(new MockServletConfig());
        Assert.assertNotNull("Request is null, you need call initServices first ",this.request);
        Assert.assertNotNull("Response is null, you need call initServices first ",this.response);
        servlet.service(this.request, this.response);
        return this.response.getContentAsString();
    }

    /**
     * Call Feed Service.
     * @return {@link MockHttpServletResponse}
     * @throws ServletException exception
     * @throws IOException
     * @throws JDOMException
     */
    public Document callFeedService() throws ServletException, IOException, JDOMException{
        final String responseAsString = this.callStringService();
        final SAXBuilder builder = new SAXBuilder();
        final Document document = builder.build(new StringReader(responseAsString));
       return document;
    }

    /**
     * Call JSON Service.
     * @return {@link MockHttpServletResponse}
     * @throws ServletException exception
     * @throws IOException
     */
    public JSONObject callJsonService() throws ServletException, IOException{
        final String responseAsString = this.callStringService();
        Assert.assertNotNull(responseAsString);
        log.debug(responseAsString);
        return (JSONObject) JSONValue.parse(responseAsString);
    }

    /**
     * Call
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public Map<String, Object> callMapJsonService() throws ServletException, IOException{
        final String responseAsString = this.callStringService();
        Assert.assertNotNull(responseAsString);
        log.debug(responseAsString);
        return JSONUtils.convertJsonToObject(responseAsString);
    }

    /**
     *
     * @param
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSuccess() throws ServletException, IOException{
        return (Map<String, Object>) callMapJsonService().get("success");
    }

    /**
     *
     * @param
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getError() throws ServletException, IOException{
        return (Map<String, Object>) callMapJsonService().get("error");
    }

    /**e
     * Get Errors on response.
     * @param response response.
     * @return
     */
    public JSONObject getErrors(final JSONObject response){
        Assert.assertNotNull("You need call first callJsonService", this.response);
        log.debug("error =--->"+response);
        final JSONObject error = (JSONObject) response.get("error");
        Assert.assertNotNull(error);
        return error;
    }

    /**
     *
     * @param response
     * @return
     */
    public String getErrorsMessage(final JSONObject response) {
        final String error = (String) this.getErrors(response).get("message");
        Assert.assertNotNull(error);
        return error;
    }

    /**
     * Get Success response.
     * @param response response
     * @return
     */
    public JSONObject getSucess(final JSONObject response) {
        //System.out.println(response);
        Assert.assertNotNull("You need call first callJsonService", this.response);
        if(response.get("success") == null) {
            return new JSONObject();
        } else {
            return (JSONObject) response.get("success");
        }
    }

    /**
     * Set Parameter.
     * @param param param
     * @param value value
     */
    public void setParameter(final String param, final String value){
        Assert.assertNotNull(request);
        this.request.setParameter(param, value);
    }


    /**
     * using @Before is convenient, but with JUnit these annotated methods must
     * be public (not like e.g. testNG) so be aware that no "secrets" are being
     * set/got in these init-style methods ;_)! !
     */
    @Before
    public void initDispatcherServlet() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        /** Servlet. **/
//        this.servlet = new DispatcherServlet() {
//
//            /** Serial. **/
//            private static final long serialVersionUID = 34324324332432L;
//
//            /**
//             * Web Application Context.
//             */
//            @Override
//            protected WebApplicationContext createWebApplicationContext(
//                    final WebApplicationContext parent) throws BeansException {
//                final GenericWebApplicationContext gwac = new GenericWebApplicationContext();
//                gwac.setParent(applicationContext);
//                gwac.refresh();
//                return gwac;
//            }
//        };
//        XMLUnit.setIgnoreComments(true);
//        XMLUnit.setIgnoreWhitespace(true);
//        XMLUnit.setIgnoreAttributeOrder(true);
//        XMLUnit.setNormalizeWhitespace(true);
//        XMLUnit.setNormalize(true);
    }

    /**
     * Assert succes json response.
     * @param response JSON response.
     */
    public void assertSuccessResponse(final JSONObject response){
         final JSONObject sucess = getSucess(response);
        Assert.assertEquals(sucess.get("r").toString(), "0");
    }

    /**
     *
     * @param response
     */
    public void assertFailedResponse(final JSONObject response){
        final JSONObject error = getErrors(response);
        Assert.assertNotNull(error);
   }

}