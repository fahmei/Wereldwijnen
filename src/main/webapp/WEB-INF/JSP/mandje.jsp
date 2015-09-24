<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib uri='http://vdab.be/tags' prefix='v'%>
<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@taglib uri='http://java.sun.com/jsp/jstl/fmt' prefix='fmt'%>

<!doctype html>
<html lang="nl">

<head>
<v:head title="Wereldwijnen"></v:head>
</head>
<body>
	<v:menu />

	<c:url var="urlTerugNaarIndexMetParams" value="index.htm" />
	<a href="${urlTerugNaarIndexMetParams}"> terug naar overzicht </a>

	<!-- Overzicht mandje -->

	
		<table class="mandje">
			<thead>
				<tr>
					<th>Wijn</th>
					<th>Prijs</th>
					<th>Aantal</th>
					<th>Te betalen</th>
				</tr>
			</thead>	
			<tbody>
				<c:forEach var="lijn" items="${bestelBon.bestelbonLijnen}">
					
					<tr>
						<td>${lijn.wijn.soort.land.naam} 
							${lijn.wijn.soort.naam}
							${lijn.wijn.jaar}
						</td>
						<td class="numeriek"><fmt:formatNumber pattern="0.00">${lijn.wijn.prijs}</fmt:formatNumber></td>
						<td class="numeriek">${lijn.aantal}</td>
						<td class="numeriek"><fmt:formatNumber pattern="0.00">${lijn.totaalLijn}</fmt:formatNumber></td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td colspan = "3"></td>
					<td class="numeriek">Totaal: <fmt:formatNumber pattern="0.00">${bestelBon.totaalTeBetalen}</fmt:formatNumber></td>
				</tr>
			</tfoot>
		</table>
	

	<!-- Velden -->
	<form method="post">
		<label>Naam<span class="fout">${fouten.naam}</span> <input
			type="text" name="naam" autofocus required value="${param.naam}">
			
		</label> <label>Straat<span class="fout">${fouten.straat}</span> <input
			type="text" name="straat" autofocus required value="${param.straat}">
			
		</label> <label>Huisnummer<span class="fout">${fouten.huisnummer}</span>
			<input type="text" name="huisnummer" autofocus required
			value="${param.huisnummer}">
			
		</label> <label>Postcode<span class="fout">${fouten.postcode}</span> 
			<input type="text" name="postcode" autofocus required
			value="${param.postcode}">
			
		</label> <label>Gemeente<span class="fout">${fouten.gemeente}</span> <input
			type="text" name="gemeente" autofocus required
			value="${param.gemeente}">
			
		</label> 
		
		<label><input type="radio" name="bestelwijze" value="AFHALEN"
			${param.bestelwijze =='0' ? 'checked' : ''}>Afhalen<span class="fout">${fouten.bestelwijze}</span></label> 
		
		<label><input type="radio" name="bestelwijze" value="OPSTUREN"
			${param.bestelwijze =='1' ? 'checked' : ''}>Opsturen</label> 
		
		<input type="submit" name="bevestigingsKnop"
			value="Als bestelbon bevestigen">
	</form>
	

</body>

</html>