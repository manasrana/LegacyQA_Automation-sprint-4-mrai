package framework;

import java.awt.AWTException;
import java.io.IOException;

public class DBError {

	public static void navigateDBErrorPage() throws InterruptedException,
			AWTException, IOException {
		// Launching admin applciation in new tab.
		AdminBaseClass adminBaseClass = new AdminBaseClass();
		adminBaseClass.launchAdminInNewTab();

		// Granting permission to access db error page.
		dbErrorAccessPermission();
	}

	public static void dbErrorAccessPermission() throws IOException {
		PageBase.AdminPage().grantAllPermission();
		PageBase.AdminPage().navigateToDBError();
	}

	public static int totalErrorCount() {
		return PageBase.AdminPage().totalErrorCount();
	}

	public static boolean isDBErrorFound(int beforeRunningTestCount) {
		return PageBase.AdminPage().isDBErrorFound(beforeRunningTestCount);
	}
}
