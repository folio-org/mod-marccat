package org.folio.cataloging.business.serialControl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOCopy;
import org.folio.cataloging.dao.DAOPredictionPattern;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.model.Subfield;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Manages the business rules for serials control for a single bibliographic
 * item.
 * 
 * @author paul
 * 
 */
public class SerialsControlManager {
	private static final Log logger = LogFactory
			.getLog(SerialsControlManager.class);

	private static DAOPredictionPattern theDAO = new DAOPredictionPattern();
	private Integer amicusNumber;
	private SRL_PRED_PAT predictionPattern = new SRL_PRED_PAT();
	private List/* <SRL_PRED_PAT_DTL> */chronologies = new ArrayList();
	private List/* <SRL_ENUM> */enumerations = new ArrayList();
	private List/* <SRL_ORDR> */orders = new ArrayList();
	private List/* <SRL_ORDR> */deletedOrders = new ArrayList();

	public SerialsControlManager(int amicusNumber, int cataloguingView)
			throws DataAccessException {
		this.amicusNumber = new Integer(amicusNumber);
		predictionPattern = theDAO.getPatternByAmicusNumber(amicusNumber);
		if (predictionPattern != null) {
			chronologies = theDAO.loadChronologies(predictionPattern
					.getPredictionPatternNumber());
			enumerations = theDAO.loadEnumerations(predictionPattern
					.getPredictionPatternNumber());
			orders = theDAO.loadOrders(amicusNumber);
		} else {
			predictionPattern = new SRL_PRED_PAT(amicusNumber, cataloguingView);
			//ADD per Adempiere
			orders = theDAO.loadOrders(amicusNumber);
		}
	}

	public SerialsControlManager() {
	}

	/**
	 * @return the amicusNumber
	 */
	public Integer getAmicusNumber() {
		return amicusNumber;
	}

	/**
	 * @param amicusNumber
	 *            the amicusNumber to set
	 */
	public void setAmicusNumber(Integer amicusNumber) {
		this.amicusNumber = amicusNumber;
	}

	/**
	 * @return
	 * @see SRL_PRED_PAT#getCaption()
	 */
	public String getCaption() {
		return predictionPattern.getCaption();
	}

	/**
	 * @return
	 * @see SRL_PRED_PAT#getLabel()
	 */
	public String getLabel() {
		return predictionPattern.getLabel();
	}

	/**
	 * @param caption
	 * @see SRL_PRED_PAT#setCaption(String)
	 */
	public void setCaption(String caption) {
		predictionPattern.setCaption(caption);
	}

	/**
	 * @param label
	 * @see SRL_PRED_PAT#setLabel(String)
	 */
	public void setLabel(String label) {
		predictionPattern.setLabel(label);
	}

	public void save() throws DataAccessException {
		theDAO.save(this);
	}

	/**
	 * @return the predictionPattern
	 */
	public SRL_PRED_PAT getPredictionPattern() {
		return predictionPattern;
	}

	/**
	 * @param predictionPattern
	 *            the predictionPattern to set
	 */
	public void setPredictionPattern(SRL_PRED_PAT predictionPattern) {
		this.predictionPattern = predictionPattern;
	}

	/**
	 * @return the chronologies
	 */
	public List getChronologies() {
		return chronologies;
	}

	/**
	 * @param chronologies
	 *            the chronologies to set
	 */
	public void setChronologies(List chronologies) {
		this.chronologies = chronologies;
	}

	/**
	 * @return the enumerations
	 */
	public List getEnumerations() {
		return enumerations;
	}

	/**
	 * @param enumerations
	 *            the enumerations to set
	 */
	public void setEnumerations(List enumerations) {
		this.enumerations = enumerations;
	}

	/**
	 * @return the orders
	 */
	public List getOrders() {
		return orders;
	}

	/**
	 */
	public void setOrders(List orders) {
		this.orders = orders;
	}

	public void deleteChronology(Integer index) {
		if (index == null) {
			return;
		}
		chronologies.remove(index.intValue());
	}

	public void deleteEnumeration(Integer index) {
		if (index == null) {
			return;
		}
		enumerations.remove(index.intValue());
	}

	public void deleteOrder(Integer index) {
		if (index == null) {
			return;
		}
		SRL_ORDR order = (SRL_ORDR) orders.get(index.intValue());
		if (!order.isNew()) {
			getDeletedOrders().add(order);
			order.markDeleted();
		}
		List subscriptions = new ArrayList(order.getSubscriptions());
		for (int i = 0; i < subscriptions.size(); i++) {
			order.deleteSubscription(new Integer(i));
		}
		orders.remove(index.intValue());
	}

	/**
	 * @return the deletedOrders
	 */
	public List getDeletedOrders() {
		return deletedOrders;
	}

	/**
	 * @param deletedOrders
	 *            the deletedOrders to set
	 */
	public void setDeletedOrders(List deletedOrders) {
		this.deletedOrders = deletedOrders;
	}

	public void receiveCopy(SRL_ORDR order, SerialLogicalCopy subscription,
			SerialPart issue, String barcode, int usersMainLibrary)
			throws DataAccessException {
		CPY_ID cpy = null;
		if (issue.getCopyNumber() == null) {
			if (subscription.isCreateCopiesIndicator()) {
				cpy = new CPY_ID();
				cpy.setTransactionDate(new Date());
				cpy.setCreationDate(new Date());
				cpy.setBarCodeNumber(barcode);
				cpy.setBibItemNumber(getAmicusNumber().intValue());
				cpy.setBranchOrganisationNumber(subscription.getBranchNumber()
						.intValue());
				cpy.setCopyIdNumber(new SystemNextNumberDAO()
						.getNextNumber("HC"));
				//cpy.setCopyNumberDescription(issue.getEnumDescription());
				if(issue.getEnumDescription()!=null)
				  cpy.setCopyStatementText(Subfield.SUBFIELD_DELIMITER + "b"+issue.getEnumDescription());
				cpy.setLoanPrd(subscription.getLoanPeriod());
				cpy.setLocationNameCode(subscription.getLocationCode()
						.shortValue());
				cpy.setOrganisationNumber(usersMainLibrary);
				cpy.setShelfListKeyNumber(subscription.getShelfListKeyNumber());
				new DAOCopy().save(cpy);
			}
			issue.setCopyNumber(new Integer(cpy.getCopyIdNumber()));
			new DAOPredictionPattern().updateOrderLine(issue.getEnumDescription(),issue.getReceivedDateAsString(),order.getOrderNo(),order.getAmicusNumber());
		}
	}

	public void receiveSpecial(SRL_ORDR order, SerialLogicalCopy subscription,
			SerialPart newPart, String barcode, int usersMainLibrary)
			throws DataAccessException {
		newPart.setSerialPartNumber(0);
		newPart.markNew();
		newPart.setType(T_SRL_PRT_TYP.SPECIAL);
		receiveCopy(order, subscription, newPart, barcode, usersMainLibrary);
		subscription.getIssues().add(newPart);
	}

	public void combineWithPrevious(SerialLogicalCopy subscription,
			SerialPart issue, int index) throws SerialCombineException {
		try {
			SerialPart prev = (SerialPart) subscription.getIssues().get(
					index - 1);
			String[] words = issue.getEnumDescription().split(" ");
			prev.setEnumDescription(prev.getEnumDescription() + "/"
					+ words[words.length - 1]);
			subscription.deleteIssue(new Integer(index));
		} catch (Exception e) {
			logger.warn(e);
			throw new SerialCombineException();
		}
	}

}
