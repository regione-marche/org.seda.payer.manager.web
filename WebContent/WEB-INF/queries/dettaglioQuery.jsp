<%@ page session="true" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jp:mondrianQuery id="query01" dataSource="${dataSource}"  catalogUri="${fileName}">
select {[Measures].[Importo], [Measures].[Num Bollettini], [Measures].[Num Trans]} ON COLUMNS,
  {([Organizzazione].[Tutti], [Tipo Servizio].[Tutte], [Tipo Bollettino].[Tutte], [Data Trans].[Tutti], [Data Pagam].[Tutti])} ON ROWS
from [Dettaglio]
</jp:mondrianQuery>