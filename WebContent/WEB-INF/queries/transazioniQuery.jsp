<%@ page session="true" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01" dataSource="${dataSource}" catalogUri="${fileName}" >
select {[Measures].[Imp Complessivo], [Measures].[Costi Trans Contribuente], 
[Measures].[Costi Banca], [Measures].[Spese Notifica], [Measures].[Costi Trans Ente], 
[Measures].[Tot Trans]} ON COLUMNS,
  {( [Societa].[Tutte], [Canale Pagam].[Tutti], [Esito].[Tutti], [Gateway].[Tutti], 
  [Data Pagam].[Tutti], [Data Trans].[Tutti])} ON ROWS
from [Transazioni]
</jp:mondrianQuery>