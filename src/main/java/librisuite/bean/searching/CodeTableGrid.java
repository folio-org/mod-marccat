package librisuite.bean.searching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import librisuite.business.codetable.ValueLabelElement;

import org.apache.commons.collections.map.LinkedMap;

/**
 * Rappresenta una tabella multilingua di CodeTable.
 * Attenzione: nel caso si apportassero modifiche ai valori delle singole celle
 * è necessario chiamare il metodo reset() per sincronizzare la tabella orizzontale
 * @author michele
 *
 */
public class CodeTableGrid {
	 LinkedMap /*<Locale, List<ValueLabelElement>>*/ columns = new LinkedMap();
	
	/**
	 * Per ottimizzazione performance
	 */
	private List/*<List <ValueLabelElement>>*/ horizontal = null;
	
	/**
	 * Aggiunge una nuova colonna in una specifica lingua
	 * @param iso3Lang
	 * @param codeTableList
	 */
	public void add(Locale locale, List/*<ValueLabelElement>*/ codeTableList ){
		columns.put(locale, codeTableList);
		reset();
	}
	
	/**
	 * Restituisce la colonna i-esima
	 * @param i
	 * @return
	 */
	public List/*<ValueLabelElement>*/ getCodeTableList(int i){
		return (List) columns.getValue(i);
	}
	
	/**
	 * Restituisce la colonna relativa alla lingua iso3Lang
	 * @param loc
	 * @return
	 */
	public List/*<ValueLabelElement>*/ getCodeTableList(Locale loc){
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
		//Mi serve iterator delle List <ValueLabelElement>>
		Iterator/*<List <ValueLabelElement>>*/ cols = columns.values().iterator();
		
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
		List/*<List <ValueLabelElement>>*/ tab = getHorizontalGrid();
		
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
		Iterator/*<List <ValueLabelElement>>*/ cols = columns.values().iterator(); 
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
	
	public ValueLabelElement get(int column, int row){
		return (ValueLabelElement) getCodeTableList(column).get(row);
	}
	
	public String toString() {
		return columns.toString();
		
	}
}
