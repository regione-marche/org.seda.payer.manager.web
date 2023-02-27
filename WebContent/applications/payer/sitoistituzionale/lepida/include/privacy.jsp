<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:setBundle name="org.seda.payer.i18n.resources.TemplateStrings" />

<s:div name="divSitoIstituzionaleContent" cssclass="divSitoIstituzionaleContent">
	<s:div name="divSitoIstituzionaleTitle" cssclass="divSitoIstituzionaleTitle">
		Privacy
	</s:div>

	<p>
		<b>1. Titolare del trattamento</b><br/>
		Il Titolare del trattamento dei dati personali di cui alla presente Informativa è Lepida S.p.A., Viale Aldo Moro n. 64, 40127, Bologna.<br />
		Ai sensi dell'art. 13 del D. lgs. n. 196/2003 - "Codice in materia di protezione dei dati personali" (di seguito denominato "Codice"),
		Lepida S.p.A., in qualità di Titolare del trattamento, è tenuta a fornirle informazioni in merito all'utilizzo dei suoi dati personali.  
	</p>
	<p>
		<b>2. Finalità del trattamento</b><br/>
		Ai sensi dell'articolo 13 del Codice citato e successive modificazioni ed integrazioni, la informiamo che i Suoi dati personali 
		saranno trattati solo ed esclusivamente per per le operazioni necessarie all'inizializzazione ed effettuazione dei pagamenti, 
		per la registrazione delle transazioni di pagamento a fini di monitoraggio delle stesse, per il monitoraggio sulla corretta 
		operatività del sistema, per l'adempimento di prescrizioni normative, per la rilevazione del grado di soddisfazione degli utenti 
		e a fini statistici. 
	</p>
	<p>
		<b>3. Modalità del trattamento</b><br/>
		Il trattamento dei dati forniti dall'utente avvengono nel rispetto del Codice e di ogni altra normativa in materia di tutela della privacy. <br />
		I dati forniti vengono trattati a mezzo del nostro sistema informatico, nel rispetto delle predette finalità ed in modo 
		da garantire la sicurezza e la riservatezza dei dati medesimi. Specifiche misure di sicurezza sono osservate per prevenire 
		la perdita dei dati, usi illeciti o non corretti ed accessi non autorizzati.
	</p>
	<p>
		<b>4. Comunicazione e Diffusione</b><br/>
		I suoi dati personali potranno essere conosciuti esclusivamente dagli operatori individuati quali Incaricati del trattamento 
		dal Titolare o dai Responsabili del trattamento. Lepida S.p.A. impartirà le opportune istruzioni scritte ai soggetti che opereranno 
		come Incaricati ai sensi dell'art. 30 del D. lgs. 30 giugno 2003, n. 196 s.m., vigilando sul loro operato.<br />
		I dati pertinenti al pagamento effettuato tramite lo strumento del RID online potranno essere comunicati e trasmessi al prestatore 
		del servizio di pagamento selezionato da Lepida S.p.A. ai fini del corretto svolgimento del relativo procedimento per il raggiungimento 
		delle finalità indicate.<br />
		I dati trattati non sono soggetti a diffusione.
	</p>
	<p>
		<b>5. Facoltatività del conferimento dei dati</b><br/>
		Il conferimento dei dati è facoltativo, ma in mancanza non sarà possibile adempiere alle finalità descritte al punto 2.
	</p>
	<p>
		<b>6. Diritti dell'Interessato</b><br/>
		La informiamo, infine, in ogni momento potrà esercitare i Suoi diritti nei confronti del titolare del trattamento, ai sensi dell'art. 7 
		del Codice della privacy. In particolare Lei potrà chiedere di conoscere l'esistenza di trattamenti di dati che possono riguardarla; 
		di ottenere senza ritardo la comunicazione in forma intellegibile dei medesimi dati e della loro origine, la cancellazione, 
		la trasformazione in forma anonima o il blocco dei dati trattati in violazione di legge; l'aggiornamento, la rettificazione 
		ovvero l'integrazione dei dati; l'attestazione che le operazioni predette sono state portate a conoscenza di coloro ai quali i dati 
		sono stati comunicati, eccettuato il caso in cui tale adempimento si riveli impossibile o comporti un impiego di mezzi manifestamente 
		sproporzionato rispetto al diritto tutelato; di opporsi, in tutto o in parte, per motivi legittimi, al trattamento dei dati personali 
		che la riguardano, ancorché pertinenti allo scopo della raccolta.
	</p>
	
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<c:choose>
			<c:when test="${!empty sessionScope.j_user_bean.nome}">
				<s:hyperlink name="hlIndietro" text="Indietro" href="../default/default.do" cssclass="hlStyleIndietro" />
			</c:when>
			<c:otherwise>
				<s:hyperlink name="hlIndietro" text="Indietro" href="../login/login.do" cssclass="hlStyleIndietro" />
			</c:otherwise>
		</c:choose>
	</s:div>
				
</s:div>
		
	
