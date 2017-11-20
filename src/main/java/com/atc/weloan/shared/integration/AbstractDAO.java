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
public final class AbstractDAO
{
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
}