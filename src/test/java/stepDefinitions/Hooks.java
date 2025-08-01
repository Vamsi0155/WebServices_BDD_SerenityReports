package stepDefinitions;

import factory.ServiceLoader;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.MyCustomListener;

public class Hooks {

    private static final Logger logger = LogManager.getLogger("Hooks.class");

    @BeforeAll
    public static void beforeAll(){

        ServiceLoader.loadProperties();
        logger.info("Automation Suit Started on {} environment", ServiceLoader.config.getProperty("EnvironmentType"));
        System.out.println("******************* Run Started **********************");
    }

    @AfterAll
    public static void afterAll(){

        System.out.println("******************* Run Completed **********************");
        logger.info("===================== Run Statistics =====================");
        logger.info("Automation Suit Completed on {} environment", ServiceLoader.config.getProperty("EnvironmentType"));
    }

    @Before
    public void beforeScenario(Scenario scenario){

        logger.info("Entered before scenario");

        String featurePath = scenario.getUri().getPath();
        MyCustomListener.getTestStartedLog(scenario.getName(), featurePath.substring(featurePath.indexOf("src")));

        System.out.println("********** " + scenario.getName() + " is started *********");
    }

    @After
    public void afterScenario(Scenario scenario){

        System.out.println("********** " + scenario.getName() + " is completed *********");

        MyCustomListener.getTestStatusLog(scenario.getName(), scenario.getStatus().toString());
        logger.info("Entered after scenario");
    }


}
