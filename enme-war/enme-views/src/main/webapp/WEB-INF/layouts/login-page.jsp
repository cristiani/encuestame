<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<c:if test="${detectedDevice}">
    <%@ include file="mobile/login-page.jsp"%>
</c:if>

<c:if test="${!detectedDevice}">
    <%@ include file="web/login-page.jsp"%>
</c:if>