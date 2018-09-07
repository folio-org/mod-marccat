package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.dao.AbstractDAO;

import java.io.Serializable;

public class Diacritics implements Persistence, Serializable
{   
	private static final long serialVersionUID = 8229065630727630431L;
	
	private Long idCharacter;
	private String setCharacter;
	private String font;
	private String character;
	private String characterName;
	private String unicodeCode;
	private String utf8Code;
    
    /** default constructor */
    public Diacritics() {
    }

	public Long getIdCharacter() {
		return idCharacter;
	}

	public void setIdCharacter(Long idCharacter) {
		this.idCharacter = idCharacter;
	}

	public String getSetCharacter() {
		return setCharacter;
	}

	public void setSetCharacter(String setCharacter) {
		this.setCharacter = setCharacter;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getUnicodeCode() {
		return unicodeCode;
	}

	public void setUnicodeCode(String unicodeCode) {
		this.unicodeCode = unicodeCode;
	}

	public String getUtf8Code() {
		return utf8Code;
	}

	public void setUtf8Code(String utf8Code) {
		this.utf8Code = utf8Code;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public void evict() throws DataAccessException {

	}

	@Override
	public int getUpdateStatus() {
		return 0;
	}

	@Override
	public void setUpdateStatus(int i) {

	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public boolean isDeleted() {
		return false;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public void markChanged() {

	}

	@Override
	public void markNew() {

	}

	@Override
	public void markUnchanged() {

	}

	@Override
	public void markDeleted() {

	}

	/*
	@Override
	public void generateNewKey() throws DataAccessException {

	}*/

	@Override
	public AbstractDAO getDAO() {
		return null;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		return false;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable serializable) {

	}
}
