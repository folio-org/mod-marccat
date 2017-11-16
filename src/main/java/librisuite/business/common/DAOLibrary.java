/*
 * (c) LibriCore
 * 
 * Created on 02-nov-2004
 * 
 * DAOLibrary.java
 */
package librisuite.business.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import librisuite.hibernate.LIB;
import librisuite.hibernate.LIB_DTE_CLSE;
import librisuite.hibernate.LIB_HRS_OPRTN;
import librisuite.hibernate.LibraryDateClosedKey;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author Elena
 * @version $Revision: 1.3 $, $Date: 2007/04/09 09:58:07 $
 * @since 1.0
 */
public class DAOLibrary extends HibernateUtil {
    private Log logger = LogFactory.getLog(DAOLibrary.class);

    public LIB load(int organisationNumber) throws DataAccessException {
        return (LIB)load(LIB.class, new Integer(organisationNumber));
    }
    
    public LIB_HRS_OPRTN loadLibHours(int organisationNumber) throws DataAccessException, NullPointerException
    {
		Session s = currentSession();
		
		LIB_HRS_OPRTN result = null;
		
		try {
		    result = (LIB_HRS_OPRTN) s.get(LIB_HRS_OPRTN.class, new Integer(organisationNumber));
		
		} catch (HibernateException e) {
		    logAndWrap(e);
		}
		return result;
    }

    public String getLibrarySymbol(int organisationNumber) throws DataAccessException {
    	LIB lib = load(organisationNumber);
    	if (lib != null) {
    		return lib.getLibrarySymbolCode();
    	}
    	else {
    		return null;
    	}
    }
    
    public String getLibrarySymbol(int organisationNumber,int bibItmNbr) throws DataAccessException {
    	Connection connection = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	String result = "";
    	try
    	{
    		connection = currentSession().connection();
    		stmt = connection.prepareStatement("select lib_smbl_cde from smry_hldg where org_nbr=? and bib_itm_nbr=?");
    		stmt.setInt(1, organisationNumber);
    		stmt.setLong(2, bibItmNbr);

    		rs = stmt.executeQuery();
    	
    	while(rs.next())
    	{
    		return rs.getString("lib_smbl_cde");
    	}
    	}catch (Exception e) {
    		try {
    			logAndWrap(e);
    		} catch (DataAccessException e1) {
    			e1.printStackTrace();
    		}
    	}

    	finally
    	{
    			try {
    				rs.close();
    			} catch (SQLException e) {
    			}
    			try {
    				stmt.close();
    			} catch (SQLException e) {
    			}
    	}
    	return result;
    }
    
    public LIB_HRS_OPRTN loadSchedule(int organisationNumber)
            throws DataAccessException, NullPointerException {
        Session s = currentSession();

        LIB_HRS_OPRTN result = null;

        try {
            result = (LIB_HRS_OPRTN) s.get(LIB_HRS_OPRTN.class, new Integer(
                    organisationNumber));

        } catch (HibernateException e) {
            logAndWrap(e);
        }
        return result;
    }

    public LIB_DTE_CLSE loadDateClose(int organisationNumber, Date theDateClosed)
    throws DataAccessException, NullPointerException {
Session s = currentSession();

LIB_DTE_CLSE result = null;

try {
    result = (LIB_DTE_CLSE) s.get(LIB_DTE_CLSE.class, new LibraryDateClosedKey(
            organisationNumber, theDateClosed));

} catch (HibernateException e) {
    logAndWrap(e);
}
return result;
}
    public String getLibScheduleOpening(int orgNbr) throws DataAccessException,
            HibernateException {
        String result = new String("");
        Session s = currentSession();
        Date theDate = new Date();
        int theHour = 0;
        int theMinutes = 0;

        LIB_HRS_OPRTN libSchedule = loadSchedule(orgNbr);
        Calendar theDay = new GregorianCalendar();
        int dayOfWeek = theDay.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.MONDAY) {
            theDate = libSchedule.getMondayHoursOpeningTime();
        } else if (dayOfWeek == Calendar.TUESDAY) {
            theDate = libSchedule.getTuesdayHoursOpeningTime();
		} else if (dayOfWeek == Calendar.WEDNESDAY) {
            theDate = libSchedule.getWednesdayHoursOpeningTime();
		} else if (dayOfWeek == Calendar.THURSDAY) {
            theDate = libSchedule.getThursdayHoursOpeningTime();
		} else if (dayOfWeek == Calendar.FRIDAY) {
            theDate = libSchedule.getFridayHoursOpeningTime();
		} else if (dayOfWeek == Calendar.SATURDAY) {
            theDate = libSchedule.getSaturdayHoursOpeningTime();
		} else if (dayOfWeek == Calendar.SUNDAY) {
            theDate = libSchedule.getSundayHoursOpeningTime();
        }
        
        theDay.setTime(theDate);
        theHour = theDay.get(Calendar.HOUR_OF_DAY);
        theMinutes = theDay.get(Calendar.MINUTE);
        if (theHour < 10)
            result = "0" + String.valueOf(theHour) + ":";
        else
            result = String.valueOf(theHour) + ":";
        if (theMinutes < 10)
            result = result + "0" + String.valueOf(theMinutes);
        else
            result = result + String.valueOf(theMinutes);
        return result;
    }

    public String getLibScheduleClosing(int orgNbr) throws DataAccessException,
            HibernateException {
        String result = new String("");
        Session s = currentSession();
        Date theDate = new Date();
        int theHour = 0;
        int theMinutes = 0;
        try {
            LIB_HRS_OPRTN libSchedule = (LIB_HRS_OPRTN) s.get(
                    LIB_HRS_OPRTN.class, new Integer(orgNbr));
            Calendar theDay = new GregorianCalendar();
            int dayOfWeek = theDay.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.MONDAY) {
                theDate = libSchedule.getMondayHoursClosingTime();
            } else if (dayOfWeek == Calendar.TUESDAY) {
                theDate = libSchedule.getTuesdayHoursClosingTime();
			} else if (dayOfWeek == Calendar.WEDNESDAY) {
                theDate = libSchedule.getWednesdayHoursClosingTime();
			} else if (dayOfWeek == Calendar.THURSDAY) {
                theDate = libSchedule.getThursdayHoursClosingTime();
			} else if (dayOfWeek == Calendar.FRIDAY) {
                theDate = libSchedule.getFridayHoursClosingTime();
			} else if (dayOfWeek == Calendar.SATURDAY) {
                theDate = libSchedule.getSaturdayHoursClosingTime();
			} else if (dayOfWeek == Calendar.SUNDAY) {
                theDate = libSchedule.getSundayHoursClosingTime();
            }
            theDay.setTime(theDate);
            theHour = theDay.get(Calendar.HOUR_OF_DAY);
            theMinutes = theDay.get(Calendar.MINUTE);
            if (theHour < 10)
                result = "0" + String.valueOf(theHour) + ":";
            else
                result = String.valueOf(theHour) + ":";
            if (theMinutes < 10)
                result = result + "0" + String.valueOf(theMinutes);
            else
                result = result + String.valueOf(theMinutes);

        } catch (HibernateException e) {
            // TODO e.printStackTrace() is evil. If you catch, handle the
            e.printStackTrace();
        }
        
        return result;
    }

    
    public String getLibOpeningDate( int orgNumber, int day)throws DataAccessException,HibernateException {
       
        String result = new String("");
        Session s = currentSession();
        Date theDate = new Date();
        int theHour = 0;
        int theMinutes = 0;
//        logger.info("eguna " + orgNumber + " "+ day);
        try {
            LIB_HRS_OPRTN libSchedule = (LIB_HRS_OPRTN) s.get(
                    LIB_HRS_OPRTN.class, new Integer(orgNumber));
            if (!(libSchedule == null)){
	            if (day == 1) {
//	                logger.info("astelehena " + libSchedule.getMondayHoursOpeningTime());
	                theDate = libSchedule.getMondayHoursOpeningTime();
	            } else if (day == 2) {
	                theDate = libSchedule.getTuesdayHoursOpeningTime();
	            } else if (day == 3) {
	                theDate = libSchedule.getWednesdayHoursOpeningTime();
	            } else if (day == 4) {
	                theDate = libSchedule.getThursdayHoursOpeningTime();
	            } else if (day == 5) {
	                theDate = libSchedule.getFridayHoursOpeningTime();
	            } else if (day == 6) {
	                theDate = libSchedule.getSaturdayHoursOpeningTime();
	            } else if (day == 7) {
	                theDate = libSchedule.getSundayHoursOpeningTime();
	            }
	            Calendar theDay = new GregorianCalendar();
	            theDay.setTime(theDate);
	            theHour = theDay.get(Calendar.HOUR_OF_DAY);
	            theMinutes = theDay.get(Calendar.MINUTE);
	            if (theHour < 10)
	                result = "0" + String.valueOf(theHour) + ":";
	            else
	                result = String.valueOf(theHour) + ":";
	            if (theMinutes < 10)
	                result = result + "0" + String.valueOf(theMinutes);
	            else
	                result = result + String.valueOf(theMinutes);
            
            }else result = "00:00";
        } catch (HibernateException e) {
            // TODO e.printStackTrace() is evil. If you catch, handle the
            e.printStackTrace();
        }
        return result;
        
    }
    
    public String getLibClosingDate( int orgNumber, int day)throws DataAccessException,HibernateException {
        
        String result = new String("");
        Session s = currentSession();
        Date theDate = new Date();
        int theHour = 0;
        int theMinutes = 0;

        try {
            LIB_HRS_OPRTN libSchedule = (LIB_HRS_OPRTN) s.get(
                    LIB_HRS_OPRTN.class, new Integer(orgNumber));
            
            if (!(libSchedule == null)){
	            if (day == 1) {
	                theDate = libSchedule.getMondayHoursClosingTime();
	            } else if (day == 2) {
	                theDate = libSchedule.getTuesdayHoursClosingTime();
	            } else if (day == 3) {
	                theDate = libSchedule.getWednesdayHoursClosingTime();
	            } else if (day == 4) {
	                theDate = libSchedule.getThursdayHoursClosingTime();
	            } else if (day == 5) {
	                theDate = libSchedule.getFridayHoursClosingTime();
	            } else if (day == 6) {
	                theDate = libSchedule.getSaturdayHoursClosingTime();
	            } else if (day == 7) {
	                theDate = libSchedule.getSundayHoursClosingTime();
	            }
	            Calendar theDay = new GregorianCalendar();
	            theDay.setTime(theDate);
	            theHour = theDay.get(Calendar.HOUR_OF_DAY);
	            theMinutes = theDay.get(Calendar.MINUTE);
	            if (theHour < 10)
	                result = "0" + String.valueOf(theHour) + ":";
	            else
	                result = String.valueOf(theHour) + ":";
	            if (theMinutes < 10)
	                result = result + "0" + String.valueOf(theMinutes);
	            else
	                result = result + String.valueOf(theMinutes);
            }else result = "00:00";

        } catch (HibernateException e) {
            // TODO e.printStackTrace() is evil. If you catch, handle the
            e.printStackTrace();
        }
        return result;
        
    }
    
    public List getTimeTable(int orgNumber) throws DataAccessException,HibernateException {
    
       DAOLibrary dl = new DAOLibrary();
       List result = new Vector();
       int i = 1;
/*      logger.info("Liburutegi timetlabe 1");
       for (i = 1;i<8;i++){
//           logger.info("Liburutegi timetlabe hasiera" + i);
           LibraryDayElementBean dayElement = new LibraryDayElementBean();
           
           dayElement.setDay(String.valueOf(i));
           dayElement.setOpeningTime(dl.getLibOpeningDate(orgNumber,i));
           dayElement.setClosingTime(dl.getLibClosingDate(orgNumber,i));
          
           result.add(dayElement);
       }
 */
       return result;

    }
    
    
    public void save(final  LIB_HRS_OPRTN libHours) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException {
			s.save(libHours);
			}
		}.execute();		
	}

    
    public void edit(final LIB_HRS_OPRTN libHours) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException {
				s.update(libHours);
			}
		}.execute();
	}
    
    
    public void delete(final LIB_HRS_OPRTN libHours) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException {
				s.delete(libHours);
			}
		}.execute();
	}

    public void saveClosedDates(final LIB_DTE_CLSE closedDates) throws DataAccessException {
        new TransactionalHibernateOperation() {
            public void doInHibernateTransaction(Session s)
                    throws HibernateException {
                s.save(closedDates);
            }
        }.execute();
    }


    public void deleteClosedDates(final LIB_DTE_CLSE closedDates) throws DataAccessException {
        new TransactionalHibernateOperation() {
            public void doInHibernateTransaction(Session s)
                    throws HibernateException {
                s.delete(closedDates);
            }
        }.execute();
    }
}