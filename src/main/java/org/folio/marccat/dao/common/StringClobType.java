package org.folio.cataloging.dao.common;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;

/**
 * @author wimc
 * @since 1.0
 */
public class StringClobType implements UserType {

  private static final Log logger = LogFactory.getLog(StringClobType.class);

  @Override
  public Object deepCopy(final Object object) {
    return object != null ? object.toString() : null;
  }

  @Override
  public boolean equals(final Object objectOne, final Object objectTwo) {
    return (objectOne == objectTwo)
      || (objectOne != null && objectTwo != null && (objectOne.equals(objectTwo)));
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Object nullSafeGet(
    final ResultSet resultSet,
    final String[] names,
    final Object owner) throws SQLException {
    final Clob clob = resultSet.getClob(names[0]);
    if (clob != null) {
      StringBuffer stringBuffer = new StringBuffer("");
      Reader reader = clob.getCharacterStream();
      int charactersRead = 0;
      char[] charArrayBuffer = new char[1024];

      while (charactersRead != -1) {
        charactersRead = -1;
        try {
          charactersRead = reader.read(charArrayBuffer, 0, 1024);
        } catch (IOException ioException) {
          logger.error("", ioException);
        }
        if (charactersRead != -1) {
          stringBuffer.append(charArrayBuffer, 0, charactersRead);
        }
      }

      return new String(stringBuffer);
    } else {
      return null;
    }
  }

  @Override
  @Deprecated
  public void nullSafeSet(
    PreparedStatement preparedStatement,
    Object object,
    int index)
    throws HibernateException, SQLException {
    throw new IllegalArgumentException("Don't call me!");
/*
		if (preparedStatement instanceof OraclePreparedStatement) {
			oracle.sql.CLOB clob =
				oracle.sql.CLOB.createTemporary(
					preparedStatement.getConnection(),
					false,
					oracle.sql.CLOB.DURATION_SESSION);
			clob.open(oracle.sql.CLOB.MODE_READWRITE);

			Writer writer = clob.getCharacterOutputStream();

			try {
				if (object == null) {
					writer.write(" ");
				} else {
					writer.write((String) object);
				}
				writer.flush();
				writer.close();
			} catch (IOException ioException) {
				logger.error("", ioException);
			}
			clob.close();
			((OraclePreparedStatement) preparedStatement).setCLOB(index, clob);
		} else {
			if (object == null) {
				preparedStatement.setClob(index,new StringReader(" "));
			} else {
				preparedStatement.setClob(index,new StringReader((String) object));
			}
		}
*/
  }

  @Override
  public Class returnedClass() {
    return String.class;
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.CLOB};
  }
}
