package org.folio.marccat.dao;

import net.sf.hibernate.Session;
import org.folio.marccat.dao.persistence.S_SYS_GLBL_VRBL;
import org.folio.marccat.exception.DataAccessException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Provides access to S_SYS_GLBL_VRBL
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class GlobalVariableDAO extends AbstractDAO {

  /**
   * Return all global variables
   *
   * @param session the hibernate session
   * @return  all global variables.
   * @throws DataAccessException in case of data access failure
   */
  public Map<String, String> getAllGlobalVariable(final Session session){
    final Map<String, String> hash = new HashMap<>();
    final List<S_SYS_GLBL_VRBL> listAllKeys = find(session, "from S_SYS_GLBL_VRBL");
    final Iterator<S_SYS_GLBL_VRBL> iter = listAllKeys.iterator();
    while (iter.hasNext()) {
      S_SYS_GLBL_VRBL rawGlobalVar = iter.next();
      hash.put(rawGlobalVar.getName(), rawGlobalVar.getValue());
    }
    return hash;
  }
}
