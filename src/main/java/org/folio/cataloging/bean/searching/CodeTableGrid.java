package org.folio.cataloging.bean.searching;

import org.apache.commons.collections.map.LinkedMap;
import org.folio.cataloging.business.codetable.Avp;

import java.util.*;

/**
 * Rappresenta una tabella multilingua di CodeTable.
 * Attenzione: nel caso si apportassero modifiche ai valori delle singole celle
 * è necessario chiamare il metodo reset() per sincronizzare la tabella orizzontale
 * @author michele
 *
 */
public class CodeTableGrid {
	 LinkedMap /*<Locale, List<Avp>>*/ columns = new LinkedMap();
	
	/**
	 * Per ottimizzazione performance
	 */
	private List/*<List <Avp>>*/ horizontal = null;
	
	/**
	 * Aggiunge una nuova colonna in una specifica lingua
	 * @param codeTableList
	 */
	public void add(Locale locale, List/*<Avp>*/ codeTableList ){
		columns.put(locale, codeTableList);
		reset();
	}
	
	/**
	 * Restituisce la colonna i-esima
	 * @param i
	 * @return
	 */
	public List/*<Avp>*/ getCodeTableList(int i){
		return (List) columns.getValue(i);
	}
	
	/**
	 * Restituisce la colonna relativa alla lingua iso3Lang
	 * @param loc
	 * @return
	 */
	public List/*<Avp>*/ getCodeTableList(Locale loc){
		return (List) columns.get(loc);
	}
	
	/**
	 * Restituisce il numero di colonne della tabella
	 * @return
	 */
	public int size(){
		return columns.size();
	}

	public List getHorizontalGrid() {
		if(horizontal!=null) return horizontal;
		// creazione struttura nuova tabella con righe vuote
		horizontal = new ArrayList();

		int nRows = ((List)columns.getValue(0)).size();
		for(int i=0; i<nRows; i++){
			horizontal.add(new ArrayList());
		}
		
		// Riversare nella nuova tabella le n colonne
		//Mi serve iterator delle List <Avp>>
		Iterator/*<List <Avp>>*/ cols = columns.values().iterator();
		
		while(cols.hasNext()) {
			List column = (List) cols.next();
			for(int i=0; i<nRows; i++){
				((List)horizontal.get(i)).add(column.get(i));
			}
		}
		return horizontal;
	}
	
	/**
	 * azzera la tabella orizzontale in quanto alcuni valori sono cambiati
	 */
	public void reset(){
		horizontal = null;
	}

	protected void sort(int numColonna, boolean sortAscending){
		// creazione struttura nuova tabella con righe vuote
		reset();
		List/*<List <Avp>>*/ tab = getHorizontalGrid();
		
		// ordinare la nuva tabella in base alla colonna selezionata
		Collections.sort(tab, new GridValueElementComparator(numColonna));
		if(!sortAscending) {
			Collections.reverse(tab);
		}
		
		// Ricreare le nuove colonne dalla tabella ordinata
		updateGrid(tab);
	}

	/**
	 * Ricrea le nuove colonne dalla tabella ordinata
	 * @param tab
	 */
	private void updateGrid(List tab) {
		Iterator/*<List <Avp>>*/ cols = columns.values().iterator();
		int j=0;
		int nRows = ((List)columns.getValue(0)).size();
		while(cols.hasNext()) {
			List column = (List) cols.next();
			for(int i=0; i<nRows; i++){
				column.set(i,((List)tab.get(i)).get(j));
			}
			j++;
		}
		reset();
	}
	
	public Avp get(int column, int row){
		return (Avp) getCodeTableList(column).get(row);
	}
	
	public String toString() {
		return columns.toString();
		
	}
}
