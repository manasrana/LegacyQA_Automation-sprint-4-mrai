package att.tests;

import static org.testng.AssertJUnit.assertEquals;

import framework.CSVOperations;
import framework.CustomerDetails;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.ServiceProviderVerificationPage;

import java.io.IOException;

public class ExchangeTests {
    @Test(groups = {"att"})
    public void Test() throws IOException {
        try {
            CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSVOnce(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
