<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<div class="navbar enme-header">
  <div class="navbar-inner">
    <div class="container">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar">a</span>
            <span class="icon-bar">d</span>
            <span class="icon-bar">v</span>
        </a>
      <a id="enme-logo" class="brand" href="<%=request.getContextPath()%>/">
            <%@ include file="/WEB-INF/layouts/logo.jsp"%>
      </a>
      <div class="nav-collapse collapse">
          <ul class="nav pull-right center-search">
            <c:if test="${logged && !hide_header_menu}">
               <li>
                <a href="<%=request.getContextPath()%>/home">
                    <spring:message code="header.public.line" /> </a>
               </li>
            </c:if>
            <c:if test="${logged && !hide_header_menu}">
               <li>
                    <a data-dojo-type="me/web/widget/menu/DashBoardMenu"
                       contextPath="<%=request.getContextPath()%>">
                    </a>
               </li>
            </c:if>

              <c:if test="${!logged}">
                  <li class="signin-home">
                        <a href="<%=request.getContextPath()%>/user/signin" id="signin-home">
                            <spring:message code="header.signin" />
                        </a>
                  </li>
              </c:if>

              <c:if test="${!hide_header_menu}">
                  <form class="navbar-search pull-left" action="" id="searchMenu">
                             <div data-dojo-type="me/web/widget/menu/SearchMenu"></div>
                  </form>
              </c:if>
            </ul>
        </div>
    </div>
  </div>
</div>


