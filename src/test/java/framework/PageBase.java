package framework;

import pages.*;
import tests.EnvironmentSetup;

public class PageBase extends BrowserSettings {
	// region - Page object Initialization.
	public static LoginPageRetailPage LoginPageRetail() {
		LoginPageRetailPage loginPageRetail = new LoginPageRetailPage(driver);
		return loginPageRetail;
	}

	public static HomePageRetailPage HomePageRetail() {
		HomePageRetailPage homePageRetail = new HomePageRetailPage(driver);
		return homePageRetail;
	}

	public static EnvironmentSetup EnvironmentSetup() {
		EnvironmentSetup environmentSetup = new EnvironmentSetup();
		return environmentSetup;
	}

	public static CarrierCreditCheckPage CarrierCreditCheckPage() {
		CarrierCreditCheckPage carrierCreditCheckPage = new CarrierCreditCheckPage(
				driver);
		return carrierCreditCheckPage;
	}

	public static UECVerificationPage UECVerificationPage() {
		UECVerificationPage uECVerificationPage = new UECVerificationPage(
				driver);
		return uECVerificationPage;
	}

	public static UECAddLinesPage UECAddLinesPage() {
		UECAddLinesPage uECAddLinesPage = new UECAddLinesPage(driver);
		return uECAddLinesPage;
	}

	public static DeviceScanPage DeviceScanPage() {
		DeviceScanPage deviceScanPage = new DeviceScanPage(driver);
		return deviceScanPage;
	}

	public static VerizonEdgePage VerizonEdgePage() {
		VerizonEdgePage verizonEdgePage = new VerizonEdgePage(driver);
		return verizonEdgePage;
	}

	public static VerizonShopPlansPage VerizonShopPlansPage() {
		VerizonShopPlansPage verizonShopPlansPage = new VerizonShopPlansPage(
				driver);
		return verizonShopPlansPage;
	}

	public static CartPage CartPage() {
		CartPage cartPage = new CartPage(driver);
		return cartPage;
	}

	public static ChoosePathPage ChoosePathPage() {
		ChoosePathPage choosePathPage = new ChoosePathPage(driver);
		return choosePathPage;
	}

	public static CommonControls CommonControls() {
		CommonControls commonControls = new CommonControls(driver);
		return commonControls;
	}

	public static SelectPlanFeaturesPage SelectPlanFeaturesPage() {
		SelectPlanFeaturesPage selectPlanFeaturesPage = new SelectPlanFeaturesPage(
				driver);
		return selectPlanFeaturesPage;
	}

	public static VerizonSelectPlanFeaturesPage VerizonSelectPlanFeaturesPage() {
		VerizonSelectPlanFeaturesPage verizonSelectPlanFeaturesPage = new VerizonSelectPlanFeaturesPage(
				driver);
		return verizonSelectPlanFeaturesPage;
	}

	public static SelectProtectionPlanInsurancePage SelectProtectionPlanInsurancePage() {
		SelectProtectionPlanInsurancePage selectProtectionPlanInsurancePage = new SelectProtectionPlanInsurancePage(
				driver);
		return selectProtectionPlanInsurancePage;
	}

	public static NumberPortPage NumberPortPage() {
		NumberPortPage numberPortPage = new NumberPortPage(driver);
		return numberPortPage;
	}

	public static PortMyNumbersPage PortMyNumbersPage() {
		PortMyNumbersPage portMyNumbersPage = new PortMyNumbersPage(driver);
		return portMyNumbersPage;
	}

	public static TermsAndConditionsPage TermsAndConditionsPage() {
		TermsAndConditionsPage termsandConditionsPage = new TermsAndConditionsPage(
				driver);
		return termsandConditionsPage;
	}

	public static PrintMobileScanSheetPage PrintMobileScanSheetPage() {
		PrintMobileScanSheetPage printMobileScanSheetPage = new PrintMobileScanSheetPage(
				driver);
		return printMobileScanSheetPage;
	}

	public static PaymentRequiredPage PaymentRequiredPage() {
		PaymentRequiredPage paymentRequiredPage = new PaymentRequiredPage(
				driver);
		return paymentRequiredPage;
	}

	public static PaymentVerificationPage PaymentVerificationPage() {
		PaymentVerificationPage paymentVerificationPage = new PaymentVerificationPage(
				driver);
		return paymentVerificationPage;
	}

	public static DeviceVerificationaandActivationPage DeviceVerificationaandActivation() {
		DeviceVerificationaandActivationPage deviceVerificationaandActivation = new DeviceVerificationaandActivationPage(
				driver);
		return deviceVerificationaandActivation;
	}

	public static WirelessCustomerAgreementPage WirelessCustomerAgreementPage() {
		WirelessCustomerAgreementPage wirelessCustomerAgreementPage = new WirelessCustomerAgreementPage(
				driver);
		return wirelessCustomerAgreementPage;
	}

	public static AdminPage AdminPage() {
		AdminPage admin = new AdminPage(driver);
		return admin;
	}

	public static ShipAdminPage ShipAdminPage() {
		ShipAdminPage shipAdminPage = new ShipAdminPage(driver);
		return shipAdminPage;
	}

	public static OrderReviewAndConfirmPage OrderReviewAndConfirmPage() {
		OrderReviewAndConfirmPage orderReviewandConfirmPage = new OrderReviewAndConfirmPage(
				driver);
		return orderReviewandConfirmPage;
	}

	public static ReceiptPage OrderReceiptPage() {
		ReceiptPage orderReceiptPage = new ReceiptPage(driver);
		return orderReceiptPage;
	}

	public static ServiceProviderVerificationPage ServiceProviderVerificationPage() {
		ServiceProviderVerificationPage serviceProviderVerificationPage = new ServiceProviderVerificationPage(
				driver);
		return serviceProviderVerificationPage;
	}

	public static CreditCheckVerificationResultsPage CreditCheckVerificationResultsPage() {
		CreditCheckVerificationResultsPage ccVerificationResultsPage = new CreditCheckVerificationResultsPage(
				driver);
		return ccVerificationResultsPage;
	}

	public static OrderSummaryPage OrderSummaryPage() {
		OrderSummaryPage orderSummaryPage = new OrderSummaryPage(driver);
		return orderSummaryPage;
	}

	public static CustomerDetails CustomerDetails() {
		CustomerDetails customerDetails = new CustomerDetails();
		return customerDetails;
	}

	public static OrderActivationCompletePage OrderActivationCompletePage() {
		OrderActivationCompletePage orderActivationCompletePage = new OrderActivationCompletePage(
				driver);
		return orderActivationCompletePage;
	}

	public static ReturnOrExchangePagePreConditionsPage ReturnOrExhangePreConditions() {
		ReturnOrExchangePagePreConditionsPage returnOrExchangeDevice = new ReturnOrExchangePagePreConditionsPage(
				driver);
		return returnOrExchangeDevice;
	}

	public static InventoryManagementPage InventoryManagementPage() {
		InventoryManagementPage inventoryManagementPage = new InventoryManagementPage(
				driver);
		return inventoryManagementPage;
	}

	public static ReturnScanDevicePage ReturnScanDevicePage()

	{
		ReturnScanDevicePage returnScanDeviceDetails = new ReturnScanDevicePage(
				driver);
		return returnScanDeviceDetails;
	}

	public static SQLUtilAdminPage SQLUtilAdminPage() {
		SQLUtilAdminPage sQLUtilAdminPage = new SQLUtilAdminPage(driver);
		return sQLUtilAdminPage;
	}

	public static ReturnProcessPage ReturningProcessPage() {
		ReturnProcessPage ReturnProcessPageDetails = new ReturnProcessPage(
				driver);
		return ReturnProcessPageDetails;
	}

	public static CarrierResponseXMLPage CarrierResponseXMLPage() {
		CarrierResponseXMLPage selectXML = new CarrierResponseXMLPage(driver);
		return selectXML;
	}

	public static CustomerLookupPage CustomerLookupPage() {
		CustomerLookupPage custLookUp = new CustomerLookupPage(driver);
		return custLookUp;
	}

	public static MailVerificationPage MailVerificationPage() {
		MailVerificationPage mailVerficationPage = new MailVerificationPage(
				driver);
		return mailVerficationPage;
	}

	public static ReturnOrExchangeVerificationPage ReturnOrExchangeVerificationPage() {
		ReturnOrExchangeVerificationPage returnOrExchangeVerificationPage = new ReturnOrExchangeVerificationPage(
				driver);
		return returnOrExchangeVerificationPage;
	}

	public static GuestVerificationPage GuestVerificationPage() {
		GuestVerificationPage guestVerificationPage = new GuestVerificationPage(
				driver);
		return guestVerificationPage;
	}

	public static OrderHistory OrderHistory() {
		OrderHistory orderHistory = new OrderHistory(driver);
		return orderHistory;
	}

	public static PickYourPathPage PickYourPathPage() {
		PickYourPathPage pickYourPathPage = new PickYourPathPage(driver);
		return pickYourPathPage;
	}

	public static AccountPasswordPage AccountPasswordPage() {
		AccountPasswordPage accountPassword = new AccountPasswordPage(driver);
		return accountPassword;
	}

	public static ReturnConfirmation ReturnConfirmation() {
		ReturnConfirmation returnConfirmation = new ReturnConfirmation(driver);
		return returnConfirmation;
	}

	public static VerizonAccountPassword VerizonAccountPassword() {
		VerizonAccountPassword verizonAccountPassword = new VerizonAccountPassword(
				driver);
		return verizonAccountPassword;
	}

	public static InstallmentPage InstallmentPage() {
		InstallmentPage installmentPage = new InstallmentPage(driver);
		return installmentPage;
	}

	public static ProcessingErrorPage ProcessingErrorPage() {
		ProcessingErrorPage processingErrorPage = new ProcessingErrorPage(
				driver);
		return processingErrorPage;
	}

	public static SelectAnOptionPage SelectAnOptionPage() {
		SelectAnOptionPage selectAnOptionPage = new SelectAnOptionPage(driver);
		return selectAnOptionPage;
	}

	public static CSVOperations CSVOperations() {
		CSVOperations csvOperations = new CSVOperations();
		return csvOperations;
	}

	public static DeviceFinancingInstallmentContractPage DeviceFinancingInstallmentContractPage() {
		DeviceFinancingInstallmentContractPage csvOperations = new DeviceFinancingInstallmentContractPage(
				driver);
		return csvOperations;
	}

	public static SprintEasyPayPage SprintEasyPayPage() {
		SprintEasyPayPage sprintEasyPayPage = new SprintEasyPayPage(driver);
		return sprintEasyPayPage;
	}

	public static SprintEasyPayEligibilityResultPage SprintEasyPayEligibilityResultPage() {
		SprintEasyPayEligibilityResultPage sprintEasyPayEligibilityResultPage = new SprintEasyPayEligibilityResultPage(
				driver);
		return sprintEasyPayEligibilityResultPage;
	}

	public static SprintShopPlansPage SprintShopPlansPage() {
		SprintShopPlansPage sprintShopPlansPage = new SprintShopPlansPage(
				driver);
		return sprintShopPlansPage;
	}

	public static VirginMobileUSAPage VirginMobileUSAPage() {
		VirginMobileUSAPage virginMobileUSAPage = new VirginMobileUSAPage(
				driver);
		return virginMobileUSAPage;
	}

	public static PrePaidSelectionPage PrePaidSelectionPage() {
		PrePaidSelectionPage prePaidSelectionPage = new PrePaidSelectionPage(driver);
		return prePaidSelectionPage;
	}

	public static ShopDevicesPage ShopDevicesPage() {
		ShopDevicesPage shopDevicesPage = new ShopDevicesPage(
				driver);
		return shopDevicesPage;
	}

	public static ProductDetailsPage ProductDetailsPage() {
		ProductDetailsPage productDetailsPage = new ProductDetailsPage(
				driver);
		return productDetailsPage;
	}

	public static YourAccountInfoPage YourAccountInfoPage() {
			YourAccountInfoPage yourAccountInfoPage = new YourAccountInfoPage(
					driver);
			return yourAccountInfoPage;
	}

	public static BrowsePhones BrowsePhones() {
		BrowsePhones browsePhones =new BrowsePhones(
				driver);
		return browsePhones;
	}

	public static BrowsePlans BrowsePlans() {
		BrowsePlans browsePlans = new BrowsePlans(
				driver);
		return browsePlans;
	}

	public static EditSessionVarsPage EditSessionVars() {
		EditSessionVarsPage editSessionVars = new EditSessionVarsPage(
				driver);
		return editSessionVars;
	}

	public static APIHistoryViewPage APIHistoryViewPage() {
		APIHistoryViewPage aPIHistoryViewPage = new APIHistoryViewPage(
				driver);
		return aPIHistoryViewPage;
	}

	public static ATTFinanceProgramPage ATTFinanceProgramPage(){
		ATTFinanceProgramPage aTTFinanceProgramPage= new ATTFinanceProgramPage(driver);
		return aTTFinanceProgramPage;
	}

	public static ATTEligibiltyResultPage ATTEligibiltyResultPage() {
		ATTEligibiltyResultPage aTTEligibiltyResultPage = new ATTEligibiltyResultPage(
				driver);
		return aTTEligibiltyResultPage;
	}
	// endregion
}
