package stepDefinitions;

import factory.ServiceLoader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WSBaseClass;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CommonSteps {

    private static final Logger logger = LogManager.getLogger("CommonSteps.class");

    @Given("the input values of {string}:")
    public void the_input_values_of(String service, DataTable table){

        Map<String, String> input = table.asMaps().get(0);
        ServiceLoader.loadInputTable(service, input);
    }

    @When("the service is called {string}")
    public void the_Service_Is_Called(String service) throws Exception {

        try {
            WSBaseClass.callService(service);
        }
        catch (Exception e){
            Path reqFile = Paths.get("./target/testxmls/request/" + ServiceLoader.wsdetails.getProperty(service) + ".xml");
            Serenity.recordReportData().withTitle("View Request").fromFile(reqFile);
            logger.error("Error in when the service is called: {}", e.getMessage());
            throw e;
        }
    }

    @Then("validate the response of {string}:")
    public void validate_The_Response_Of(String service, DataTable table) throws Exception {

        WSBaseClass.validateResponseXML(service, table);
    }
}
