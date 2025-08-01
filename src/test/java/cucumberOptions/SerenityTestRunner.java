package cucumberOptions;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features="src/test/resources/features",
        glue="stepDefinitions",
        monochrome=true,
        tags="",
        plugin={"pretty",
                "utils.MyCustomListener",
                "html:reports/CucumberReports/Report.html",
                "rerun:target/rerun/failedScenarios.txt"
        }

)
public class SerenityTestRunner {

}