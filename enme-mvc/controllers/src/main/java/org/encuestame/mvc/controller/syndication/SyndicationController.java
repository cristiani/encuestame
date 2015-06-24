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
package org.encuestame.mvc.controller.syndication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.exception.EnMeExpcetion;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.utils.enums.TypeSearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.syndication.feed.rss.Item;

/**
 * Tweet Poll feed controller, display rss or atom tweetpoll feeds.
 * @author Picado, Juan juanATencuestame.org
 * @since Jul 2, 2010 1:17:17 PM
 * @version $Id:$
 */
@Controller
public class SyndicationController extends AbstractFeedController {

    /**
     * Log.
     */
    private Logger log = Logger.getLogger(this.getClass());


    /**
     *
     */
    @Value("${rss.display.items}") private Integer rssDisplayItems;


    /**
     * Default Tweet Poll Feed Title.
     */
    final String TWEET_POLL_FEED_TITLE = "TweetPolls published by %s";

    /**
     * Build Feed Body.
     * @param username
     * @param model
     * @param request
     * @throws EnMeNoResultsFoundException
     */
    private void buildTweetPollFeedBody(
            final String username,
            final Model model,
            final HttpServletRequest request,
            final UserAccount secUserSecondary) throws EnMeNoResultsFoundException{
        //TODO: ??
    }

    /**
     * Display TweetPoll ATOM Feed.
     * @param username
     * @param model
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/feed/{username}/tweetpoll.atom", method = RequestMethod.GET)
    public String tweetPollAtom(@PathVariable String username, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        final UserAccount secUserSecondary = findByUsername(username);
        if(secUserSecondary != null){
            try {
                model.addAttribute("items", this.getEntryAtomFeed(username, request, TypeSearchResult.TWEETPOLL, rssDisplayItems));
            } catch (EnMeExpcetion e) {
                log.error(e);
                //e.printStackTrace();
                model.addAttribute("items",ListUtils.EMPTY_LIST);
            }
        }
        return "tweetPollAtomFeedView";
    }

   /**
    *
    * @param model
    * @param request
    * @return
    */
   @RequestMapping(value = "/feed/tweetpoll.atom", method = RequestMethod.GET)
   public String tweetPollAtom(Model model, HttpServletRequest request) {
       model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
       return "tweetPollAtomFeedView";
   }

    /**
     * Display TweetPoll RSS Feed.
     * @param username username
     * @param model model
     * @param request request
     * @return
     */
    @RequestMapping(value = "/feed/{username}/tweetpoll.rss", method = RequestMethod.GET)
    public String tweetPollRss(@PathVariable String username, Model model, HttpServletRequest request) {
        final UserAccount secUserSecondary = findByUsername(username);
        if (secUserSecondary != null) {
             try {
                model.addAttribute("items", this.getItemRssFeed(username, request, TypeSearchResult.TWEETPOLL, rssDisplayItems));
             } catch (Exception e) {
                 log.error(e);
                 //e.printStackTrace();
                 model.addAttribute("items",ListUtils.EMPTY_LIST);
             }
        }
        return "tweetPollRssFeedView";
    }

    /**
    *
    * @param model
    * @param request
    * @return
    */
   @RequestMapping(value = "/feed/tweetpoll.rss", method = RequestMethod.GET)
   public String tweetPollRss(Model model, HttpServletRequest request) {
       model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
       return "tweetPollRssFeedView";
   }

    /**
     * Display profile RSS Feed.
     * @param username
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/feed/{username}/profile.rss", method = RequestMethod.GET)
    public String profileRss(@PathVariable String username, Model model, HttpServletRequest request) {
        final UserAccount secUserSecondary = findByUsername(username);
        if (secUserSecondary != null) {
             try {
                final List<Item> items = this.getItemRssFeed(username, request, TypeSearchResult.PROFILE, rssDisplayItems);
                log.debug("/feed/{username}/profile.rss items size "+items.size());
                model.addAttribute("items", items);
             } catch (Exception e) {
                 log.error(e);
                 //e.printStackTrace();
                 model.addAttribute("items",ListUtils.EMPTY_LIST);
             }
        }
        return "profileRssFeedView";
    }

    /**
     * Display profile ATOM Feed.
     * @param username
     * @param model
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/feed/{username}/profile.atom", method = RequestMethod.GET)
    public String profileAtom(@PathVariable String username, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        final UserAccount secUserSecondary = findByUsername(username);
        if(secUserSecondary != null){
            try {
                model.addAttribute("items", this.getEntryAtomFeed(username, request, TypeSearchResult.PROFILE, rssDisplayItems));
                this.buildTweetPollFeedBody(username, model, request, secUserSecondary);
            } catch (EnMeExpcetion e) {
                log.error(e);
                //e.printStackTrace();
                model.addAttribute("items",ListUtils.EMPTY_LIST);
            }
        }
        return "profileAtomFeedView";
    }

    /**
     * Display survey Atom Feed.
     * @param username
     * @param model
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/feed/{username}/survey.atom", method = RequestMethod.GET)
    public String surveyAtom(@PathVariable String username, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        final UserAccount secUserSecondary = findByUsername(username);
        if(secUserSecondary != null){
            try {
                model.addAttribute("items", this.getEntryAtomFeed(username, request, TypeSearchResult.SURVEY, rssDisplayItems));
            } catch (EnMeExpcetion e) {
                log.error(e);
            }
        }
        return "surveyAtomFeedView";
    }

    /**
    *
    * @param model
    * @param request
    * @return
    */
   @RequestMapping(value = "/feed/survey.atom", method = RequestMethod.GET)
   public String surveyAtom(Model model, HttpServletRequest request) {
       model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
       return "surveyAtomFeedView";
   }

    /**
     * Display survey RSS Feed.
     * @param username username
     * @param model model
     * @param request request
     * @return
     */
    @RequestMapping(value = "/feed/{username}/survey.rss", method = RequestMethod.GET)
    public String surveyRss(@PathVariable String username, Model model, HttpServletRequest request) {
        final UserAccount secUserSecondary = findByUsername(username);
        if(secUserSecondary != null){
             try {
                model.addAttribute("items", this.getItemRssFeed(username, request, TypeSearchResult.SURVEY, rssDisplayItems));
             } catch (Exception e) {
                 log.error(e);
                 //e.printStackTrace();
                 model.addAttribute("items",ListUtils.EMPTY_LIST);
             }
        }
        return "surveyRssFeedView";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/feed/survey.rss", method = RequestMethod.GET)
    public String surveyRss(Model model, HttpServletRequest request) {
        model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
        return "surveyRssFeedView";
    }

    /**
     * Display polls ATOM Feed.
     * @param username
     * @param model
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/feed/{username}/poll.atom", method = RequestMethod.GET)
    public String pollAtom(@PathVariable String username, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        final UserAccount secUserSecondary = findByUsername(username);
        if(secUserSecondary != null){
            try {
                model.addAttribute("items", this.getEntryAtomFeed(username, request, TypeSearchResult.POLL, rssDisplayItems));
            } catch (EnMeExpcetion e) {
                log.error(e);
            }
        }
        return "pollAtomFeedView";
    }

    /**
     *
     * @param username
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/feed/poll.atom", method = RequestMethod.GET)
    public String pollAtom(Model model, HttpServletRequest request) {
        model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
        return "pollAtomFeedView";
    }

    /**
     * Display polls RSS Feed.
     * @param username username
     * @param model model
     * @param request request
     * @return
     */
    @RequestMapping(value = "/feed/{username}/poll.rss", method = RequestMethod.GET)
    public String pollRss(@PathVariable String username, Model model, HttpServletRequest request) {
        final UserAccount secUserSecondary = findByUsername(username);
        if(secUserSecondary != null){
             try {
                model.addAttribute("items", this.getItemRssFeed(username, request, TypeSearchResult.POLL, rssDisplayItems));
             } catch (Exception e) {
                 log.error(e);
             }
        }
        return "pollRssFeedView";
    }

   /**
    *
    * @param username
    * @param model
    * @param request
    * @return
    */
   @RequestMapping(value = "/feed/poll.rss", method = RequestMethod.GET)
   public String pollRss(Model model, HttpServletRequest request) {
       model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
       return "pollRssFeedView";
   }

    /**
     * Display projects ATOM Feed.
     * @param username
     * @param model
     * @param request
     * @return
     */
//    @RequestMapping(value = "/feed/{username}/project.atom", method = RequestMethod.GET)
//    public String projectAtom(@PathVariable String username, Model model, HttpServletRequest request) {
//        final UserAccount secUserSecondary = findByUsername(username);
//        if(secUserSecondary != null){
//            try {
//                model.addAttribute("items", this.getEntryAtomFeed(username, request, "projects", rssDisplayItems));
//            } catch (EnMeExpcetion e) {
//                log.error(e);
//            }
//        }
//        return "projectAtomFeedView";
//    }

    /**
    *
    * @param model
    * @param request
    * @return
    */
//   @RequestMapping(value = "/feed/projects.atom", method = RequestMethod.GET)
//   public String projectAtom(Model model, HttpServletRequest request) {
//       model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
//       return "projectAtomFeedView";
//   }


    /**
     * Display projects RSS Feed.
     * @param username username
     * @param model model
     * @param request request
     * @return
     */
//    @RequestMapping(value = "/feed/{username}/project.rss", method = RequestMethod.GET)
//    public String projectRss(@PathVariable String username, Model model, HttpServletRequest request) {
//        final UserAccount secUserSecondary = findByUsername(username);
//        if(secUserSecondary != null){
//             try {
//                model.addAttribute("items", this.getItemRssFeed(username, request, "projects", rssDisplayItems));
//             } catch (EnMeNoResultsFoundException e) {
//                 log.error(e);
//             }
//        }
//        return "projectRssFeedView";
//    }

//   /**
//    *
//    * @param model
//    * @param request
//    * @return
//    */
//   @RequestMapping(value = "/feed/projects.rss", method = RequestMethod.GET)
//   public String projectRss(Model model, HttpServletRequest request) {
//       model.addAttribute("items", ListUtils.EMPTY_LIST); //TODO: without filter.
//       return "projectRssFeedView";
//   }

    /**
     * Display frontend ATOM Feed.
     * @param username
     * @param model
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/feed/home.atom", method = RequestMethod.GET)
    public String frontendAtom(
        Model model,
        HttpServletRequest request) throws UnsupportedEncodingException {
            try {
                model.addAttribute("items", this.getEntryAtomFeed("", request, TypeSearchResult.ALL, rssDisplayItems));
            } catch (EnMeExpcetion e) {
                log.error(e);
            }
        return "frontEndAtomFeedView";
    }


    /**
     * Display frontend RSS Feed.
     * @param username username
     * @param model model
     * @param request request
     * @return
     */
    @RequestMapping(value = "/feed/home.rss", method = RequestMethod.GET)
    public String frontendRss(
            Model model,
            HttpServletRequest request) {
             try {
                model.addAttribute("items", this.getItemRssFeed(null, request, TypeSearchResult.ALL, rssDisplayItems));
             } catch (Exception e) {
                 log.error(e);
                 //e.printStackTrace();
                 model.addAttribute("items",ListUtils.EMPTY_LIST);
             }
        return "frontEndRssFeedView";
    }

    /**
     * @param rssDisplayItems the rssDisplayItems to set
     */
    public void setRssDisplayItems(final Integer rssDisplayItems) {
        this.rssDisplayItems = rssDisplayItems;
    }
}
