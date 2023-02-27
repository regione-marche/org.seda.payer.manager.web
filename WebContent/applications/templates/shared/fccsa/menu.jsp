<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>

<c:if test="${!empty sessionScope.menuLivelloUno}">
	
	<c:choose>
		<c:when test="${requestScope.mnLivello==1}">
			<c:set var="current1Liv" value="${requestScope.mnId}" />
		</c:when>
		<c:when test="${requestScope.mnLivello==2}">
			<c:set var="current1Liv" value="${requestScope.mnId1}" />
			<c:set var="current2Liv" value="${requestScope.mnId}" />
		</c:when>
		<c:when test="${requestScope.mnLivello==3}">
			<c:set var="current1Liv" value="${requestScope.mnId1}" />
			<c:set var="current2Liv" value="${requestScope.mnId2}" />
			<c:set var="current3Liv" value="${requestScope.mnId}" />
		</c:when>
		<c:otherwise>
			<c:set var="current1Liv" value="" />
			<c:set var="current2Liv" value="" />
			<c:set var="current3Liv" value="" />
		</c:otherwise>
	</c:choose>
	
	<s:div name="menucontainer" cssclass="menucontainer">
		<s:div name="menu" cssclass="menu">
			<ul>
			   <c:forEach var="menuItem" items="${sessionScope.menuLivelloUno}">
			   		<c:choose>
			   			<c:when test="${menuItem.idMenu==current1Liv}">
							<li>			
								<s:hyperlink cssclass="selected" alt="${menuItem.tooltipVoceMenu}" name= "linkLivelloUno${menuItem.idMenu}"  text="${menuItem.voceDescrizioneMenu}" href="${menuItem.urlAction}"/>
							</li>		
			   			</c:when>
			   			<c:otherwise>
							<li>			
								<s:hyperlink alt="${menuItem.tooltipVoceMenu}" name="linkLivelloUno${menuItem.idMenu}"  text="${menuItem.voceDescrizioneMenu}" href="${menuItem.urlAction}"/>
							</li>		
			   			</c:otherwise>
			   		</c:choose>
				</c:forEach>
			</ul>
		</s:div>
	</s:div>
			
	<c:if test="${requestScope.mnLivello==1 || requestScope.mnLivello==2 || requestScope.mnLivello==3}">	
		<s:div name="submenucontainer1" cssclass="submenucontainer">
			<s:div name="submenu" cssclass="sub_menu">
				<ul>
					<c:forEach var="menuItem" items="${sessionScope.menuLivelloDue}">
						<c:if test="${menuItem.idParent == current1Liv}">
							<c:choose>
								<c:when test="${menuItem.idMenu==current2Liv}">
									<li>
										<s:hyperlink cssclass="selected" alt="${menuItem.tooltipVoceMenu}" name="linkLivelloDue${menuItem.idMenu}" text="${menuItem.voceDescrizioneMenu}" href="${menuItem.urlAction}&mnId1=${menuItem.idParent}"/>			
									</li>
								</c:when>
								<c:otherwise>
									<li>
										<s:hyperlink alt="${menuItem.tooltipVoceMenu}" name="linkLivelloDue${menuItem.idMenu}" text="${menuItem.voceDescrizioneMenu}" href="${menuItem.urlAction}&mnId1=${menuItem.idParent}"/>			
									</li>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
				</ul>
			</s:div>
		</s:div>
	</c:if>

	<c:if test="${requestScope.mnLivello==2 || requestScope.mnLivello==3 }">
		<s:div name="submenucontainer2" cssclass="submenucontainer">
			<s:div name="subsubmenu" cssclass="subsub_menu">
				<ul>
					<c:forEach var="menuItem" items="${sessionScope.menuLivelloTre}">
						<c:if test="${menuItem.idParent==current2Liv}">
							<c:choose>
								<c:when test="${menuItem.idMenu==current3Liv}">
									<li>
										<s:hyperlink cssclass="selected" alt="${menuItem.tooltipVoceMenu}" name="linkLivelloTre${menuItem.idMenu}" text="${menuItem.voceDescrizioneMenu}" href="${menuItem.urlAction}&mnId1=${current1Liv}&mnId2=${menuItem.idParent}"/>
									</li>
								</c:when>
								<c:otherwise>
									<li>
										<s:hyperlink alt="${menuItem.tooltipVoceMenu}" name="linkLivelloTre${menuItem.idMenu}" text="${menuItem.voceDescrizioneMenu}" href="${menuItem.urlAction}&mnId1=${current1Liv}&mnId2=${menuItem.idParent}"/>
									</li>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
				</ul>
			</s:div>
		</s:div>
	</c:if>
	
</c:if>

