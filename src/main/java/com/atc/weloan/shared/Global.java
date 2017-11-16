package com.atc.weloan.shared;

/**
 * WeLoan global constants.
 * With "Global" we mean a constant that 
 * 
 * <ul>
 * 		<li>is supposed to be shared at least between 2 modules.</li>
 * 		<li>needs to be used within this "shared" module</li>
 * </ul>
 * 
 * @author Andrea Gazzarini
 * @since 1.0
 */
public interface Global 
{
	String APPLICATION_NAME = "WE-LOAN";
	String CNF_SYSPROPERTY_NAME = "weloan.cnf";
	Integer COPY = 0;
	Integer PERSON = 1;
	
	String EMPTY_STRING = "";
	
	int ITEM_IN_TRANSIT_RETURN_CODE = 100111;
	int ITEM_ARRIVED_FROM_TRANSIT = 100100292;
	int BORROWER_IN_BLACKLIST = 100003; 
	int BORROWER_IS_TRAPSET = 100004;
	int OPERATION_COMPLETED_SUCCESSFULLY = 1;
	
	int OPERATION_FAILED = 0;
	int OPERATION_EXECUTED = OPERATION_COMPLETED_SUCCESSFULLY;
	int JUST_CHARGED_OUT = 197202;
	int COPY_DOESNT_BELONG_TO_THIS_BRANCH =100092;
	int COPY_ALREADY_NOT_IN_CIRCULATION = 100024;
	int COPY_CANNOT_BE_FOUND = 197200;
	int CHARGED_OUT_TO_USER = 197201;
	int COPY_ALREADY_IN_CIRCULATION=	 197202;
	int COPY_ALREADY_RESERVED = 100010;
	int COPY_LOAN_NOT_ALLOWED = 1001002923;
	
	int COPY_IN_TRANSIT_RETURNED = 197203;
	int BORROWER_NOT_FOUND = 197204;
	int INTERBRANCH_RENEWAL_NOT_ALLOWED = 100100298;
	int MAX_RENEWALS_EXCEEDED = 100100299;
	int NO_LIMIT_POLICY_DEFINED = 100100300;	
	int MAX_LOANS_EXCEEDED = 100100301;
	int LIBRARY_DOESNT_HAVE_THIS_COPY = 100100302;
	static final String SUBFIELD_DELIMITER = "\u001f";
	
	String SWITCH_DATABASE = "switch.database";
	String SCHEMA_CUSTOMER_KEY = "CUSTOM_KEY";
	String SCHEMA_SUITE_KEY = "SUITE_KEY";
	String SCHEMA_CUSTOM_VALUE = "CUSTOM";
	String SCHEMA_OLISUITE_VALUE = "OLISUITE";
	String SCHEMA_LIBRISUITE_VALUE = "LIBRISUITE";
	String SCHEMA_CASALINI_VALUE = "CASALINI";
	
}
