<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>

<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<article class="mobile-detail">
   <h4 class="enme mobile-question-detail">
              ${poll.questionBean.questionName}
   </h4>
   <article class="emne-box">
        <section class="web-wrapper-detail-wrapper">
            <article class="web-detail-chart">
                <div
                   data-dojo-type="me/web/widget/poll/detail/PollChartDetail"
                   pollId="${poll.id}"
                   percents="true"
                   username="${poll.ownerUsername}">
              </div>
            </article>
       </section>
       <section class="emne-box web-poll-answer-wrapper">
            <div class="web-tweetpoll-answer-answer">
                <header>
                     <div class="answer-label">
                          <spring:message code="commons_detail_answer" />
                     </div>
                     <div class="answer-votes">
                          <spring:message code="commons_detail_total_votes" />
                     </div>
                     <div class="answer-percent">
                          %
                     </div>
                </header>
                 <c:forEach items="${answers}" var="item">
                         <section data-dojo-type="me/web/widget/results/answers/GenericPercentResult"
                           itemId="${item.answerBean.answerId}"
                           color="${item.answerBean.color}"
                           votes="${item.votes}"
                           percent="${item.percent}"
                           questionId="${item.answerBean.questionId}"
                           labelResponse="${item.answerBean.answers}">
                         </section>
                 </c:forEach>
                 <div class="web-poll-options-button">
                   <a href="<%=request.getContextPath()%>/poll/vote/${poll.id}/${poll.questionBean.slugName}">
                     <button class="btn-block btn-warning btn enme-ui-button vote">
                       <spring:message code="options.vote" />
                     </button>
                   </a>
                 </div>
            </div>
       </section>
   </article>
   <article class="emne-box">
       <header class="mobile-home-subtitle category_color">
          <spring:message code="options.links" />
       </header>
       <section>
            <div data-dojo-type="me/web/widget/social/LinksPublished"
                 type="POLL"
                 more="false"
                 itemId="${poll.id}"
                 class="web-social-links">
            </div>
       </section>
    </article>
   <c:if test="${!empty hashtags}">
       <section class="emne-box">
           <header class="category_color">
                <spring:message code="options.hashtag" />
           </header>
           <div class="web-tweetpoll-hashtags ">
               <c:forEach items="${hashtags}" var="h">
                       <span data-dojo-type="me/web/widget/stream/HashTagInfo"
                        url="<%=request.getContextPath()%>/tag/${h.hashTagName}/"
                        hashTagName="${h.hashTagName}"></span>
               </c:forEach>
           </div>
       </section>
   </c:if>
   <section class="web-tweetpoll-comments emne-box">
      <header class="category_color mobile-home-subtitle">
          <spring:message code="options.comments" />
      </header>
      <c:if test="${logged}">
            <div name="comments" data-dojo-type="me/web/widget/comments/AddComment"
                 comment_limit="<%=EnMePlaceHolderConfigurer.getProperty("comments.max.length")%>"
                 type="poll"
                 isModerated="${isModerated}"
                 item_id="${poll.id}"
                 username="${account.username}"></div>
      </c:if>
      <c:if test="${!logged}">
         <div class="row comment-login">
              <div class="picture span2">
                  <img src="<%=request.getContextPath()%>/resources/images/default.png" width="60" height="60"/>
              </div>
              <div class="span4">
                  <div class="login">
                      <a href="<%=request.getContextPath()%>/user/signin">
                        <h4 class="enme">
                            <spring:message code="comments.login.post.comment" />
                        </h4>
                      </a>
                  </div>
              </div>
            </a>
          </div>
      </c:if>
      <div data-dojo-type="me/web/widget/comments/Comments" type="poll" item_id="${poll.id}"></div>
   </section>
</article>