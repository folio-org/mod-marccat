/*---------------------------------------------------------------------------*\
|
| Copyright (c) 2010 @Cult s.r.l. All rights reserved.
|
| @Cult s.r.l. makes no representations or warranties about the 
| suitability of the software, either express or implied, including
| but not limited to the implied warranties of merchantability, fitness
| for a particular purpose, or non-infringement. 
| @Cult s.r.l.not be liable for any damage suffered by 
| licensee as a result of using, modifying or distributing this software 
| or its derivates.
|
| This copyright notice must appear in all copies of this software.
|
\*---------------------------------------------------------------------------*/
package com.atc.weloan.shared.integration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Base class for all Data Access Objects.
 * 
 * @author Andrea Gazzarini
 * @since 1.0
 */
public abstract class AbstractDAO
{
	protected final DataSource datasource;
	
	protected static final int BARCODE_LENGTH= 14;
	
	protected static final String SELECT_NXT_NBR = "select A.key_fld_nxt_nbr l_nxt_nbr from s_nxt_nbr A where A.key_fld_cde=? for update";
	protected static final String UPDATE_NXT_NBR = "update s_nxt_nbr A set key_fld_nxt_nbr=A.key_fld_nxt_nbr+1 where A.key_fld_cde= ?";
	
	protected static final String CS_NORMALIZE_SORT_FORM = "begin ? := pack_sortform.sf_normalize(buffersize => ?, inputstrptr => ?, outputbufptr => ?); end;"; 
	protected static final int BUFFER_SIZE = 256;
	
	/**
	 * Builds a new DAO with the given datasource.
	 * 
	 * @param datasource the datasource.
	 */
	public AbstractDAO(DataSource datasource)
	{
		this.datasource = datasource;
	}
	
	/**
	 * Adds blank spaces to the given string until it reaches the given length.
	 * @param toPad	the string to pad.
	 * @param padLength the padding length.
	 * @return the padded string.
	 */
	public static String fixedCharPadding(String toPad, int padLength)
	{
		toPad = toPad.trim().replaceFirst("^0+", "");
		StringBuilder builder = new StringBuilder(toPad);
		for (int index = 0 , howManyTimes = (padLength - toPad.length()); index < howManyTimes; index++)
		{
			builder.append(" ");
		}
		return builder.toString();
	}
	
	/**
	 * For a given type of object (E.g QQ means Borrower) return the next key number to be used.
	 * At same time, updates the current value to next one.
	 * @param conn An existing SQL connection.
	 * @param type The String key representing the desired object.
	 * @return The next number to use as key.
	 * @throws SQLException
	 */
	protected int getNextNumber(Connection conn, String type) throws SQLException
	{
		int result = -1;
		PreparedStatement queryStatement = null;
		PreparedStatement queryStatement2 = null;
		ResultSet resultSet = null;
		try{
			queryStatement = conn.prepareStatement(SELECT_NXT_NBR);
			queryStatement.setString(1, type);		

			 resultSet = queryStatement.executeQuery();
			if(resultSet.next())
			{
				result = resultSet.getInt("l_nxt_nbr");	

				queryStatement2 = conn.prepareStatement(UPDATE_NXT_NBR);
				queryStatement2.setString(1, type);
				queryStatement2.executeUpdate();
			} else
			{
				assert false : "Type "+type+" does not exist";
			}		
			return result;
		}catch(SQLException e)
		{
			throw e;
		}catch(Exception e)
		{
			throw new SQLException(e);
		}finally
		{
			try{ resultSet.close(); }catch(Exception ignore) {}
			try{ queryStatement.close(); }catch(Exception ignore) {}
			try{ queryStatement2.close(); }catch(Exception ignore) {}
		}
	}
	
	/**
	 * Returns the normalized sort form of a given string.
	 * @param conn The database connection.
	 * @param toNormalize The string to normalize.
	 * @return The normalized string. Null value if a null is passed in input.
	 * @throws SQLException
	 */
	public static String getNormalizedSortForm(Connection conn, String toNormalize) throws SQLException
	{
		if(toNormalize==null)
			return null;
		if("".equals(toNormalize))
			return "";
		
		CallableStatement cs = null;
		try{
			
			cs = conn.prepareCall(CS_NORMALIZE_SORT_FORM);
			cs.registerOutParameter(1, java.sql.Types.INTEGER);
			cs.setInt(2, BUFFER_SIZE);
			cs.setString(3, toNormalize);
			cs.registerOutParameter(4, java.sql.Types.VARCHAR);		
			cs.execute();
			return cs.getString(4);	
			
		} catch(SQLException e)
		{
			throw e;
		} catch(Exception e)
		{
			throw new SQLException(e);
		} finally
		{
			try { cs.close(); } catch(Exception ignore){}
		}
	}
	
	/**
	 * Returns the normalized sort form of a given string.
	 * @param toNormalize The string to normalize.
	 * @return The normalized string. Null value if a null is passed in input.
	 * @throws SQLException
	 */
	public  String getNormalizedSortForm(String toNormalize) throws SQLException
	{
		if(toNormalize==null)
			return null;
		if("".equals(toNormalize))
			return "";
		
		Connection conn = datasource.getConnection();
		
		CallableStatement cs = null;
		try{
			
			cs = conn.prepareCall(CS_NORMALIZE_SORT_FORM);
			cs.registerOutParameter(1, java.sql.Types.INTEGER);
			cs.setInt(2, BUFFER_SIZE);
			cs.setString(3, toNormalize);
			cs.registerOutParameter(4, java.sql.Types.VARCHAR);		
			cs.execute();
			return cs.getString(4);	
			
		} catch(SQLException e)
		{
			throw e;
		} catch(Exception e)
		{
			throw new SQLException(e);
		} finally
		{
			try { cs.close(); } catch(Exception ignore){}
			try { conn.close(); } catch(Exception ignore){}
		}
	}
	
	
	/**
	 * Utility method that checks if the given value is null and eventually
	 * provides the PreparedStatement.setNull shortcut.
	 * 
	 * @param ps the prepared statement.
	 * @param position the index position within the prepared statement of the given parameter.
	 * @param value the parameter value.
	 * @throws SQLException  in case of database failure.
	 */
	protected void setNullOrInteger(PreparedStatement ps, int position, Integer value) throws SQLException
	{
		if(value == null)
		{
			ps.setNull(position, java.sql.Types.INTEGER);
		}else
		{
			ps.setInt(position, value);
		}
	}
	
	/**
	 * Utility method used for retrieving integer values from database that
	 * could be null.
	 * 
	 * @param rs the resultset
	 * @param field the name of the field to be gathered.
	 * @return the value of the requested integer parameter (or null).
	 * @throws SQLException in case of database failure.
	 */
	protected Integer getNullOrInteger(ResultSet rs, String field) throws SQLException
	{
		Object value = rs.getObject(field);
		if(value == null)
		{
			return null;
		}else
		{
			return rs.getInt(field);
		}
	}
	
	/**
	 * Utility method used for retrieving double values from database that
	 * could be null.
	 * 
	 * @param rs the resultset
	 * @param field the name of the field to be gathered.
	 * @return the value of the requested double parameter (or null).
	 * @throws SQLException in case of database failure.
	 */
	protected Double getNullOrDouble(ResultSet rs, String field) throws SQLException
	{
		Object value = rs.getObject(field);
		if(value == null)
		{
			return null;
		}else
		{
			return rs.getDouble(field);
		}
	}
	
	/**
	 * Utility method that checks if the given double value is null and eventually
	 * provides the PreparedStatement.setNull shortcut.
	 * 
	 * @param ps the prepared statement.
	 * @param position the index position within the prepared statement of the given parameter.
	 * @param value the parameter value.
	 * @throws SQLException  in case of database failure.
	 */
	protected void setNullOrDouble(PreparedStatement ps, int position, Double value) throws SQLException
	{
		if(value == null)
		{
			ps.setNull(position, java.sql.Types.DOUBLE);
		}else
		{
			ps.setDouble(position, value);
		}
	}
	
	/**
	 * Utility method that checks if the given date value is null and eventually
	 * provides the PreparedStatement.setNull shortcut.
	 * 
	 * @param ps the prepared statement.
	 * @param position the index position within the prepared statement of the given parameter.
	 * @param value the parameter value.
	 * @throws SQLException  in case of database failure.
	 */
	protected void setNullOrTimestamp(PreparedStatement ps, int position, java.util.Date value) throws SQLException
	{
		if(value==null)
		{
			ps.setNull(position, java.sql.Types.TIMESTAMP);
		}else
		{
			ps.setTimestamp(position, new java.sql.Timestamp(value.getTime()));
		}
	}
	
	/**
	 * Utility method that checks if the given date value is null and eventually
	 * provides the PreparedStatement.setNull shortcut.
	 * 
	 * @param ps the prepared statement.
	 * @param position the index position within the prepared statement of the given parameter.
	 * @param value the parameter value.
	 * @throws SQLException  in case of database failure.
	 */
	protected void setNullOrDate(PreparedStatement ps, int position, java.util.Date value) throws SQLException
	{
		if(value==null)
		{
			ps.setNull(position, java.sql.Types.DATE);
		}else
		{
			ps.setDate(position, new java.sql.Date(value.getTime()));
		}
	}
	
	/**
	 * Returns the value for the sequence passed as parameter
	 * @param sequence
	 * @return
	 * @throws SQLException
	 */
	protected int getSequenceValue(String sequence) throws SQLException
	{
		Connection conn = null;
		PreparedStatement insertStatement = null;
		ResultSet rs = null;
		try
		{
			conn = this.datasource.getConnection();		

				insertStatement = conn.prepareStatement("select "+sequence+" from DUAL");
				rs = insertStatement.executeQuery();
				
				return rs.next()? rs.getInt(1) : 0;
			
		}catch(SQLException e)
		{
			throw e;
		}catch(Exception e)
		{
			throw new SQLException();
		}finally
		{
			try{ rs.close(); }catch(Exception i){}
			try{ insertStatement.close(); } catch(Exception i) {}
			try{ conn.close(); }catch(Exception i){}			
		}
	}
	
	protected int count(int borrowerId, String sql) throws SQLException
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try
		{
			connection = datasource.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, borrowerId);
			rs = statement.executeQuery();
			return (rs.next()) ? rs.getInt(1) : 0;
		} catch (SQLException exception) 
		{
			throw exception;
		} catch (Exception exception)
		{
			throw new SQLException(exception);
		} finally 
		{
			try {  rs.close(); } catch (Exception exception) { }
			try {  statement.close(); } catch (Exception exception) { }
			try {  connection.close(); } catch (Exception exception) { }
		}
	}	
}
