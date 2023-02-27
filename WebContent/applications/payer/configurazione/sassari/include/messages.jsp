<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="seda" %>
		<seda:form name="frmConfirm" hasbtn3="false" hasbtn2="false" hasbtn1="false" btn1text="..."  
			btn1onclick="this.form.submit();" method="post" action="${varname}.do?action=search">
			<center>
				<seda:label name="lblMessage" text="${message}" cssclass="lblMessage"/>
			</center>
			<br /><br />
			<center>
                <!--<seda:imagebutton imageurl="../applications/templates/configurazione/img/back_icon.gif" alt="" onclick="this.form.submit();" name="btnbck" cssclass="image_hyperlink"/>
				<seda:button id="indietro" type="submit" text="Indietro"
							 onclick="this.form.submit();" cssclass="btnStyle" />
							--> 
							 <seda:button id="indietro" type="submit" text="Indietro"
							 onclick="" cssclass="btnStyle" />
			</center>
		</seda:form>