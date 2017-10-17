package librisuite.business.common;

/**
 * Marker to make all Persistence objects time shifted
 * @author michelem
 *
 */
public interface TimeShiftPersistence {

	void confirmChanges();

	void cancelChanges();
}
