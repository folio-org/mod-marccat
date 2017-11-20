package org.folio.cataloging.dao;

import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.TAG_GRP_MODEL;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.folio.cataloging.dao.common.HibernateUtil;

public class DAOGroupModel extends HibernateUtil {
	
	public List getModelList(String tagCoded, int parent, String iso3Language)throws DataAccessException {
		List result =
				find(
				" select a "
					+ " from "
					+ " TAG_MODEL  as a, TAG_GRP_MODEL  as b "
					+ " where b.code = ?"
					+ " and b.key.sequence = a.keyGroup"
					+ " and a.key.language = b.key.language "
					+ " and b.key.language = ?"
	                + " and b.parentSequence = ? order by a.key.sequence",
				new Object[] { tagCoded, iso3Language, new Integer(parent)},
				new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.INTEGER });
		
		return result;
	}
	
	public TAG_GRP_MODEL getGroupModel(String tagCoded, int parent, String iso3Language)throws DataAccessException {
		List result =
				find(
					"select b from "
					+ " TAG_GRP_MODEL b"
					+ " where b.code = ? "
					+ " and b.key.language = ?"
	                + " and b.parentSequence = ?",
				new Object[] {tagCoded,iso3Language, new Integer(parent)},
				new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.INTEGER });
		if(result.size()==1) return (TAG_GRP_MODEL) result.get(0);
		else if(result.size()==0) return null;
		throw new DataAccessException("Multiple TAG_GRP_MODEL retrieved with key ("+tagCoded+","+iso3Language+","+parent+")");
	}

	public List getAllBasicGroupModels()throws DataAccessException {
		List result = find("select b from TAG_GRP_MODEL b where b.parentSequence = 0");
		return result;
	}
	
	public List getChildList(String tagCoded, String iso3Language)throws DataAccessException {
		List result = getChildGroupModel(tagCoded,1,iso3Language);
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			TAG_GRP_MODEL rawGrp = (TAG_GRP_MODEL) iter.next();
			List rawChildren = getChildModelList(tagCoded,  iso3Language, rawGrp.getKey().getSequence());
			rawGrp.setModels(rawChildren);
		}
		return result;
	}
	
	public List getChildGroupModel(String tagCoded, int parent, String iso3Language)throws DataAccessException {
		List result =
				find(
					"select b from "
					+ " TAG_GRP_MODEL b"
					+ " where b.code = ? "
					+ " and b.key.language = ?"
	                + " and b.parentSequence =?",
				new Object[] { tagCoded,iso3Language, new Integer(parent)},
				new Type[] { Hibernate.STRING, Hibernate.STRING , Hibernate.INTEGER });
	 return result;
	}

	public List getChildModelList(String tagCoded, String iso3Language,int sequence)throws DataAccessException {
		List result =
				find(
				" select a "
					+ " from "
					+ " TAG_MODEL  as a, TAG_GRP_MODEL  as b "
					+ " where b.code = ?"
					+ " and a.key.language = b.key.language "
				    + " and b.key.language = ?"
				    + " and a.keyGroup = b.key.sequence"
					+ " and a.keyGroup = ?"
	                + " and b.parentSequence = 1",
				new Object[] {tagCoded, iso3Language, new Integer(sequence)},
				new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.INTEGER });
		
		return result;
	}

}
