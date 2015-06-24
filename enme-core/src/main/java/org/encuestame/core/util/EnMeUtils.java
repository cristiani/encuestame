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
package org.encuestame.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.encuestame.persistence.domain.HashTag;
import org.encuestame.persistence.domain.question.QuestionAnswer;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSwitch;
import org.encuestame.persistence.domain.survey.Poll;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.persistence.exception.EnMeExpcetion;
import org.encuestame.utils.net.XFordwardedInetAddressUtil;
import org.encuestame.utils.web.HashTagBean;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Commons utils.
 * @author Picado, Juan juanATencuestame.org
 * @since Jul 10, 2011
 */
public class EnMeUtils {

    /** Front End Service Log. **/
    private static Logger log = Logger.getLogger(EnMeUtils.class);

    private static final int BASE = 2;

    /**
     *
     */
    public static final String ANONYMOUS_USER = "anonymousUser";

    private static final int MIN_SIZE = 12;

    public static final long RATE_DEFAULT = 1;

    public static final long VOTE_DEFAULT = 1;

    public static final long VOTE_MIN = 1;

    public static final long LIKE_DEFAULT = 0;

    public static final long DISLIKE_DEFAULT = 0;

    public static final long HIT_DEFAULT = 1;

    public static final String HASH = "#";
    
    public static final String DEFAULT_LANG = "en";

    public static final String SPACE = " ";

    public static final Integer DEFAULT_START = 0;

    /** Percentage value for like option vote. **/
    public static final float LIKE_PERCENTAGE_VALUE = 0.10F;

    /** Percentage value for dislike option vote. **/
    public static final float DISLIKE_PERCENTAGE_VALUE = 0.20F;

    /** Percentage value for hits received. **/
    public static final float HITS_PERCENTAGE_VALUE = 0.10F;

    /** Percentage value for hashtag hits received. **/
    public static final float HASHTAG_HITS_PERCENTAGE_VALUE = 0.05F;

    /** Percentage value for comments received.**/
    public static final float COMMENTS_PERCENTAGE_VALUE = 0.10F;

    /** Percentage value for social network published.**/
    public static final float SOCIAL_NETWORK_PERCENTAGE_VALUE = 0.20F;

    /** Percentage value for number votes received.**/
    public static final float VOTES_PERCENTAGE_VALUE = 0.25F;

    /**
     * Calculate percent.
     * @param total
     * @param value
     * @return
     */
    public static String calculatePercent(double total, double value) {
        if (total != 0 && value !=0) {
            double myAprValue = (value / total);
            final DecimalFormat percent = new DecimalFormat("##.##%");
            return percent.format(myAprValue);
        } else {
            return "0.00%";
        }
    }

    /**
     * Description.
     * <p>
     * The frequency is calculated based on the hits (visits) that receives the hashtag
     * and use has on tweetPolls, survey, etc.. Frequency is the use of hashtag.
     * </p>
     * @param frecuency Number of times the label has been used in polls, survey or tweetPolls
     * @param frecMax : Maximum number of frequency.
     * @param frecMin : Minimum number of frecuency.
     * @return
     */
    public static long calculateSizeTag(long frecuency, long  frecMax, long frecMin) {
        float frec = Float.valueOf(frecuency);
        float maxiFrec = Float.valueOf(frecMax);
        float miniFrec = Float.valueOf(frecMin);
        double minValue = Double.valueOf(EnMeUtils.MIN_SIZE);
        final float frecDiff = frecMax - miniFrec;
        double perRelative = ((frec - miniFrec) / (frecDiff == 0 ? 1 : frecDiff)) * maxiFrec;
        double perLog;
        if (perRelative == 0) {
            perLog = minValue;
        } else {
            perLog = (Math.log(perRelative) / Math.log(EnMeUtils.BASE))
             + minValue;
        }
        log.debug("Size tag Value ---------------> " + Math.round(perLog) );
        return Math.round(perLog);
    }

    /**
     * Copying One File to Another.
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copy(InputStream in, File dst) throws IOException {
        //log.debug("copy src" + src.getPath());
        log.debug("copy dst" + dst.getPath());
        //InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    /**
     * Calculate relevance value.
     * <p>
     * The relevance value is calculated based on the follower parameters received:
     *  Total like votes
     *  Number dislike votes
     *  Total hits number
     *  Maximum like vote value received by user.
     * </p>
     * @param totalLikeVote
     * @param totalDislikeVote
     * @param totalHits
     * @param totalComments
     * @param totalSocialAccounts
     * @param totalnumberVotes
     * @param totalhashTagHits
     * @return
     */
    public static long calculateRelevance(long totalLikeVote,
            long totalDislikeVote, long totalHits, final long totalComments,
            final long totalSocialAccounts, final long totalnumberVotes,
            final long totalhashTagHits) {
        double likeVotes = LIKE_PERCENTAGE_VALUE *  totalLikeVote;
        double dislikeVotes = DISLIKE_PERCENTAGE_VALUE * totalDislikeVote;
        double numberHits = HITS_PERCENTAGE_VALUE * totalHits;
        double comments = COMMENTS_PERCENTAGE_VALUE * totalComments;
        double socialAccounts = SOCIAL_NETWORK_PERCENTAGE_VALUE * totalSocialAccounts;
        double numberVotes = VOTES_PERCENTAGE_VALUE * totalnumberVotes;
        double hashTagHits = HASHTAG_HITS_PERCENTAGE_VALUE * totalhashTagHits;
        double relevance;
        final long roundRelevance ;
        relevance =  likeVotes + dislikeVotes + numberHits + comments + socialAccounts + numberVotes +hashTagHits;
        roundRelevance = relevance < 1 ? 1 : Math.round(relevance);
        log.trace(" RELEVANCE *******************************>  " + roundRelevance);
        return roundRelevance;
    }

    /**
     * Extract the {@link InetAddress} from {@link HttpServletRequest}.
     * @param request {@link HttpServletRequest}
     * @param proxy define if the server is behind a proxy server.
     * @return
     * @throws UnknownHostException
     */
    public static String getIP(HttpServletRequest request, boolean proxy) throws UnknownHostException {
        log.trace("getIP Force Proxy Pass ["+proxy+"]");
        log.trace("getIP request ["+request+"]");
        String ip = "";
        //FIXME: if your server use ProxyPass you need get IP from x-forwarder-for, we need create
        // a switch change for ProxyPass to normal get client Id.
        // Solution should be TOMCAT configuration.
        log.trace("X-getHeaderNames ["+ request.getHeaderNames()+"]");
        if (proxy) {
            ip = XFordwardedInetAddressUtil.getAddressFromRequest(request);
            log.trace("X-FORWARDED-FOR [" + ip + "]");
        } else {
            String _ip = request.getRemoteAddr();
            log.trace("NON XFORWARDEDddddddd IP [" + _ip + "]");
            InetAddress sip = InetAddress.getByName(_ip);
            log.trace("NON XFORWARDED IP sip [" + sip + "]");
            ip = sip.getHostAddress();
        }
        return ip;
    }
    
    /**
     * 
     * @param xml
     * @return
     * @throws Exception
     */
    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
    
    /**
     * 
     * @param arrayHashTags
     * @return
     */
    public static List<HashTagBean> createHashTagBeansList(final String[] arrayHashTags) {
        final List<HashTagBean> tagBeanlist = new ArrayList<HashTagBean>();
        for (int i = 0; i < arrayHashTags.length; i++) {
            final HashTagBean itemTagBean = new HashTagBean();
            itemTagBean.setHashTagName(arrayHashTags[i]);
            tagBeanlist.add(itemTagBean);
        }
        return tagBeanlist;
    }

    /**
     * Return a random ip.
     * @return
     */
    public static String ipGenerator() {
        Random r = new Random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }

    /**
     * Convert degrees cordinates to radians.
     * @param degreesValue
     * @return
     */
    public static Double convertDegreesToRadians(final double degreesValue) {
        final Double radiansValue = Math.toRadians(degreesValue);
        return radiansValue;
    }


    /**
     * Clean the version to possible extra string like, release, rc, m1, m2.
     * @param version
     * @return
     * @throws EnMeExpcetion 
     */
    public static  int[] cleanVersion(String version) throws EnMeExpcetion {
        if (version.endsWith("-SNAPSHOT")){
            version = version.replace("-SNAPSHOT", "");
        }
        final String[] versionArray = version.split("\\.");
        int[] arrayAsIng = new int[3];
        //convert to int
        for (int i = 0; i < versionArray.length; i++) {
            arrayAsIng[i] = Integer.valueOf(versionArray[i]);
        }
        if (arrayAsIng.length == 3) {
            return arrayAsIng;
        } else {
            throw new EnMeExpcetion("version not valid  or not compatible");
        }
    }

    /**
     * Create a tweetpoll body as html, links included.
     * @param tweetPoll TweetPoll
     * @param question
     * @param answers list of QuestionAnswer
     * @param hashTags list of HashTag
     * @return
     */
    public static String generateBodyTweetPollasHtml(
            final String domain,
            final TweetPoll tweetPoll,
            final String question,
            final List<TweetPollSwitch> answers,
            final Set<HashTag> hashTags){
        StringBuffer buffer = new StringBuffer();
        String q = tweetPoll.getQuestion().getQuestion();
        buffer.append("<b class=\"q-enme\">");
        buffer.append(q);
        buffer.append("</b>");
        buffer.append(" ");
        for (TweetPollSwitch answer : answers) {
            buffer.append("<b class=\"answer\">");
            buffer.append(answer.getAnswers().getAnswer());
            buffer.append("</b>");
            buffer.append(" ");
            buffer.append(" <a target=\"_blank\" href=\"");
            buffer.append(answer.getShortUrl());
            buffer.append("\">");
            buffer.append(answer.getShortUrl());
            buffer.append("");
            buffer.append("</a>");
        }
        for (HashTag hashTag : hashTags) {
            buffer.append(" ");
            buffer.append("<a target=\"_blank\" href=\"");
            buffer.append(domain + "\\tag\\");
            buffer.append(hashTag.getHashTag());
            buffer.append("\">");
            buffer.append("#");
            buffer.append(hashTag.getHashTag());
            buffer.append("</a>");
        }
        return buffer.toString();
    }

    /**
     * Return a format date as string.
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(final Date date, final String format) {
        DateTime convertDate = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        return convertDate.toString(fmt);
    }

    /**
     * Return a default poll url
     * @param poll
     * @return
     */
    public static String createUrlPollAccess(final String domain, final Poll poll) {
        StringBuffer urlBuffer = new StringBuffer(domain);
        urlBuffer.append("/poll/");
        urlBuffer.append(poll.getPollId());
        urlBuffer.append("/");
        urlBuffer.append(poll.getQuestion().getSlugQuestion());
        return urlBuffer.toString();
    }

    /**
     * Return a TweetPoll url
     * @param domain
     * @param tweetPoll
     * @return
     */
    public static String createTweetPollUrlAccess(final String domain, final TweetPoll tweetPoll) {
        final StringBuilder builder = new StringBuilder(domain);
        builder.append("/tweetpoll/");
        builder.append(tweetPoll.getTweetPollId());
        builder.append("/");
        builder.append(tweetPoll.getQuestion().getSlugQuestion());
        return builder.toString();
    }
}
