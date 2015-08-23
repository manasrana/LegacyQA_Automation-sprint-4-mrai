package framework;

public class Constants {
    //region Order Status
    public static final String SHIPPED = "Shipped";
    public static final String IN_STORE_BILLING = "In-Store Billing";
    public static final String ACTIVATION_SCAN_REQUIRED = "Activation Scan Required";
    public static final String WCA_SIGNATURE_REQUIRED = "WCA Signature Required";
    public static final String ORDER_CANCELLED_BY_USER = "Cancelled";
    public static final String ACTIVATION_SUCCEEDED_COMMENT1 = "Activation Completed Successfully";
    public static final String SPRINT_WIRELESS_XML = "Sprint XML";

    //endregion Order Status

    //region Phone Models
    public static final String SAMSUNG_GALAXY_S4_16GB_WHITE_FROST = "Samsung Galaxy S4 16GB - White Frost";
    //endregion Phone Models

    //region Store Locations
    public static final String SAN_FRANCISCO_CENTRAL_2766 = "2766 - TARGET - SAN FRANCISCO CENTRAL\n789 Mission St\nSan Francisco, CA 94103\n415-3436272";
    public static final String IN_STORE_ACTIVATION = "In-Store Activation";
    //endregion Store Locations

    //region Comments Ship Admin
    public static final String RECEIPT_SUBMISSION_SUCCEEDED_COMMENT = "Receipt submission succeeded.";
    public static final String SHIPPED_BUT_NOT_LITERALLY_COMMENT = "Shipped.  ...but not literally. Order is technically non-shipping.";
    public static final String ACTIVATION_SUCCEEDED_COMMENT = "Activation succeeded.";
    public static final String PARKING_SUCCEEDED_COMMENT = "Parking succeeded.";
    public static final String MAP_SUCCEEDED_COMMENT = "Map Succeeded.";
    public static final String ACTIVATION_ORDER_VALIDATION_PASSED = "Activation Order Validation passed.";
    public static final String INQUIRING_NUMBER_PORT_ELIGIBILITY = "Inquiring number port eligibility ...";
    public static final String ASSIGNED_RETURN_ORDER_NUMBER = "Assigned return order number:";
    public static final String FINANCE_CONTRACT = "Finance Contract";
    public static final String ORDER_VALIDATION_PASSED = "Order Validation Passed.";
    public static final String LINE_ACTIVATION_SUCCEEDED = "Line activation succeeded.";
    public static final String ACTIVATION_COMPLETED_SUCCESSFULLY = "Activation Completed Successfully";
    public static final String NO_DEPOSIT_REQUIRED = "NO_DEPOSIT_REQUIRED";
    //endregion Comments Ship Admin

    //region Order Buy Type Ship Admin
    public static final String HANDSET_UPGRADE = "Handset Upgrade";
    public static final String PHONE_AND_PLAN = "Phone and Plan";
    //endregion Order Buy Type Ship Admin

    //region Additional Info Ship Admin
    public static final String EXISTING_ACCOUNT_ORDER = "Existing Account Order";
    public static final String NUMBER_PORTABILITY = "Number Portability";
    public static final String VIEW_ACTIVATION_INFO = "View Activation Info";
    public static final String AWAITING_CARRIER_RESOLUTION = "Awaiting Carrier Resolution";
    //endregion Additional Info Ship Admin

    //region Partner Ship Admin
    public static final String VERIZON_WIRELESS_XML = "Verizon Wireless XML";
    public static final String SPRINT_XML = "Sprint XML";
    //endregion Partner Ship Admin

    //region Device Status Inventory Management
    public static final String SOLD = "Sold";
    public static final String RETURN_TO_INVENTORY = "Return To Vendor";
    //endregion Device Status Inventory Management

    //region Prices
    public static final String upgradeFees40 = "$40.00";
    //endregion Prices

    public static final String SUCCESS = "SUCCESS";
    public static final String APPROVED = "APPROVED";
    public static final String EXCHANGE_ORDER_CREATED = "Exchange order created";
    public static final String SUPPORT_PAGE_MESSAGE = "Please call 1-800-570-5762 for assistance activating this order. Reference ";
    public static final String SUPPORT_PAGE_URL = "retail/support.htm";

    //region Default Numbers in XML Files
    public static final String DEFAULT_XML_NUMBER_8155491829 = "8155491829";
    public static final String DEFAULT_XML_NUMBER_4152647954 = "4152647954";
    public static final String DEFAULT_XML_NUMBER_8159547507 = "8159547507";
    public static final String DEFAULT_XML_NUMBER_4152648022 = "4152648022";
    public static final String DEFAULT_XML_NUMBER_6567778895 = "6567778895";
    public static final String DEFAULT_XML_NUMBER_7325551212 = "7325551212";
    public static final String DEFAULT_XML_STOREID_9999 = "9999";
    public static final String DEFAULT_XML_STOREID_88887 = "88887";
    public static final String DEFAULT_XML_NUMBER_OF_LINES = "002";
    public static final String DEFAULT_XML_ZipCode = "07059";
    public static final String DEFAULT_XML_SSN_7777 = "7777";
    public static final String DEFAULT_XML_ZipCode_60007 = "60007";
    public static final String DEFAULT_XML_NUMBER_6092340476 = "6092340476";
    public static final String DEFAULT_XML_NUMBER_6097315076 = "6097315076";
    public static final String DEFAULT_XML_NUMBER_6232157629 = "6232157629";
    public static final String APPROVE_WITH_DEPOSIT = "securityDepositAmountForContract>0</securityDepositAmountForContract";
    public static final String APPROVE_WITH_DEPOSIT_VALUE = "securityDepositAmountForContract>200</securityDepositAmountForContract";
    public static final String DEFAULT_XML_NUMBER_8482009941 = "8482009941";
    public static final String DEFAULT_XML_NUMBER_6157157755 = "6157157755";
    public static final String DEFAULT_XML_NUMBER_4193881179 = "4193881179";
    public static final String DEFAULT_ACCOUNT_VALIDATION_PTN_NUMBER = "<ptn>8125453522</ptn>";
    //endregion Default Numbers in XML Files

    //region exchange Device Messages
    public static final String EXCHANGE_MESSAGE = "Notice: A Rep must return the order then place a separate upgrade order for all Edge to Edge, Edge to non-Edge and non-Edge to Edge device exchanges.";
    //endregion exchange Device Messages

    //region Invalid Credentials
    public static final String INVALID_USERNAME = "xyz";
    public static final String INVALID_PASSWORD = "xyz";
    //endregion Invalid Credentials

    // region Shipadmin
    public static final String retailOperationCenter = "Retail Operation Center";
    //endregion Shipadmin

    //region API History View APIs
    public static final String RETRIEVE_CUSTOMER_DETAILS_API = "retrieveCustomerDetails";
    public static final String SUBMIT_CREDIT_APPLICATION_API = "submitCreditApplication";
    public static final String RETRIEVE_CREDIT_APPLICATION_API = "retrieveCreditApplication";
    public static final String RETRIEVE_PRICE_PLANS_API = "retrievePricePlans";
    public static final String SUBMIT_SERVICE_DETAILS_API = "submitServiceDetails";
    public static final String SUBMIT_ACTIVATION_API = "submitActivation";
    public static final String SUBMIT_RECEIPT_API = "submitReceipt";
    public static final String RETRIEVE_EXISTING_CUSTOMER_INSTALLMENT_DETAILS_API = "retrieveExistingCustomerInstallmentDetails";
    public static final String GENERAL_RETURN_LABEL_API = "generateReturnLabel";
    //endregion API History View APIs
    public static final String DISCLAIMER_TEXT = "Taxes & surcharges apply may vary. Federal Universal Service Charge of 16.10% of\n" +
            "interstate & int\\'l telecom charges (varies quarterly based on FCC rate), a $.16 Regulatory\n" +
            "& a $.90 Administrative Charge per line/month are our charges, not taxes. The iPhone 4 is\n" +
            "configured to work only with the wireless services of Verizon Wireless, and may not work\n" +
            "on another carrier\\'s network, even after completion of your contract term.\n" +
            "\n" +
            "I understand that if I am porting in my phone number from another service provider, I may\n" +
            "owe that provider an early termination fee and other charges, and I understand that,\n" +
            "during the porting process, the ability for me to receive calls, including return calls from\n" +
            "911 personnel, will not be available.\n" +
            "\n" +
            "A data package (minimum $30/month) is required during the minimum contract term for any\n" +
            "smartphone activated on a non-MORE Everything plan.\n" +
            "\n" +
            "I AGREE TO THE CURRENT VERIZON WIRELESS CUSTOMER AGREEMENT\n" +
            "(CA),INCLUDING THE CALLING PLAN, (WITH EXTENDED LIMITED\n" +
            "WARRANTY/SERVICE CONRACT, IF APPLICABLE), AND OTHER TERMS AND\n" +
            "CONDITIONS FOR SERVICES AND SELECTED FEATURES I HAVE AGREED TO\n" +
            "PURCHASE AS REFLECTED ON THE RECEIPT, AND WHICH HAVE BEEN\n" +
            "PRESENTED TO ME BY THE SALES REP AND WHICH I HAD THE OPPORTUNITY\n" +
            "TO REVIEW. I UNDERSTAND THAT I AM AGREEING TO AN EARLY\n" +
            "TERMINATION FEE PER LINE AS REFLECTED ON THIS RECEIPT,\n" +
            "LIMITATIONS.";
    public static final String PARKING_FAILED_INVALID_SIM = "Parking failed.";
    public static final String SL_WITH_DEPOSIT = "<spending-limit-amount>150.0</spending-limit-amount>";
    public static final String DEPOSIT_AMOUNT = "<deposit-amount>100.0</deposit-amount>";
}