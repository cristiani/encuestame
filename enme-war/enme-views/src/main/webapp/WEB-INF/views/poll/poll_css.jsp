<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<c:if test="${!development}">
	<link rel="stylesheet"  href="<c:url value="/resources/css/poll.min.${detectedDevice ? 'mobile' : 'web'}.css" />" />
</c:if>

<c:if test="${development}">
	<link rel="stylesheet"  href="<c:url value="/resources/dev/${detectedDevice ? 'mobile_' : ''}poll.css" />" />
</c:if>