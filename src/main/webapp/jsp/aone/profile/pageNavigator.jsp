<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="WEB-INF/tld/vwb.tld" prefix="vwb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="templates.default" />
<link rel="stylesheet" href="${contextPath}/jsp/aone/css/uiLib.css" type="text/css" />
<link rel="stylesheet" href="${contextPath}/jsp/aone/css/aone.css" type="text/css" />
<script type="text/javascript" src="${contextPath}/jsp/aone/js/uiLib-jQuery.js"></script>
<script type="text/javascript" src="${contextPath}/jsp/aone/js/global.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>

<div class="ui-wrap">
	<jsp:include page="/jsp/aone/ms_left.jsp"></jsp:include>
	<div id="a1-content">
		<div id="profile">
			<span class="pf-name">${userProfile.mame}</span>
			<span class="ui-text-note">${userProfile.uid}</span>
<!--			<a href="" target="_blank">个人首页（这个可以不要）</a>  -->
		</div>
	
		<h3>最近创建的页面</h3>
		<c:choose>
			<c:when test="${empty createdPages}">
				<p class="a1-feed-none">还没有创建过页面。</p>
			</c:when>
			<c:otherwise>
				<ul class="a1-pf-list">
					<c:forEach items="${createdPages}" var="item">
						<li>
							<span class="ui-RTCorner a1-time"><fmt:formatDate value="${item.time}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
							<a href="<vwb:Link page='${item.resourceId}' format='url'/>">${item.title}</a>
						</li>		
					</c:forEach>
				</ul>
				<div class="ui-clear"></div>
			</c:otherwise>
		</c:choose>
		
		<h3>最近修改的页面</h3>
		<c:choose>
			<c:when test="${empty editedPages}">
				<p class="a1-feed-none">最近没有修改过页面。</p>
			</c:when>
			<c:otherwise>
				<ul class="a1-pf-list">
					<c:forEach items="${editedPages}" var="item">
						<li>
							<span class="ui-RTCorner a1-time"><fmt:formatDate value="${item.time}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
							<a href="<vwb:Link page='${item.resourceId}' format='url'/>">${item.title}</a>
						</li>
					</c:forEach>
				</ul>
				<div class="ui-clear"></div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

