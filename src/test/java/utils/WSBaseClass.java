package utils;

import factory.ServiceLoader;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class WSBaseClass {

    private static final Logger logger = LogManager.getLogger("WSBaseClass.class");
    private static Response response;

    public static void callService(String service) throws NoSuchMethodException {

        createRequestXML(service);
    }

    public static void createRequestXML(String service) throws NoSuchMethodException {

        String templatePath = Thread.currentThread().getContextClassLoader().getResource( "requestXMLTemplates/").getFile();
        String requestPath = "./target/testxmls/request/";
        String sampleFilename = templatePath + ServiceLoader.wsdetails.getProperty(service) + ".xml";
        String requestFileName = requestPath + ServiceLoader.wsdetails.getProperty(service) + ".xml";

        String line = "";
        Path path = Paths.get(requestFileName);
        deleteFiles (path);

        StringBuilder requestFile = new StringBuilder();
        Throwable var10;
        String part1;
        String part2;
        String columnName;

        try {
            FileWriter fileWriter = new FileWriter(requestFileName);
            var10 = null;

            try {
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                Throwable var12 = null;

                try {
                    FileReader fileReader = new FileReader(sampleFilename);
                    Throwable var14 = null;

                    try {
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        Throwable var16 = null;

                        try {
                            String attributeValue;

                            label1072:
                            while (true) {
                                while (true){
                                    if((line = bufferedReader.readLine())==null) {
                                        break label1072;
                                    }

                                    if(!line.contains("{$")) {
                                        requestFile.append(line);
                                        requestFile.append(System.getProperty("line.separator"));
                                        bufferedWriter.write(line);
                                        bufferedWriter.newLine();
                                    }
                                    else {
                                        part1 =line.split("\\{")[0];
                                        part2 =line.split("\\}")[1];
                                        columnName =line.split( "\\{")[1].substring( 1).split( "\\}")[0];
                                        attributeValue =ServiceLoader.getInputTable(service, columnName);

                                        if (!attributeValue.isEmpty()) {
                                            requestFile.append(part1).append(attributeValue).append(part2);
                                            requestFile.append(System.getProperty("line.separator"));
                                            bufferedWriter.write( part1 + attributeValue + part2);
                                            bufferedWriter.newLine();
                                        }
                                    }
                                }
                            }
                        }
                        catch (Throwable var2){
                            var16 = var2;
                            throw var2;
                        }
                        finally {
                            if (var16 != null) {
                                try {
                                    bufferedReader.close();
                                } catch (Throwable var3) {
                                    var16.addSuppressed(var3);
                                }
                            } else {
                                bufferedReader.close();
                            }
                        }
                    }
                    catch (Throwable var4){
                        var14 = var4;
                        throw var4;
                    }
                    finally {
                        if (var14 != null) {
                            try {
                                fileReader.close();
                            } catch (Throwable var3) {
                                var14.addSuppressed(var3);
                            }
                        } else {
                            fileReader.close();
                        }
                    }
                }
                catch (Throwable var5){
                    var12 = var5;
                    throw var5;
                }
                finally {
                    if (var12 != null) {
                        try {
                            bufferedWriter.close();
                        } catch (Throwable var3) {
                            var12.addSuppressed(var3);
                        }
                    } else {
                        bufferedWriter.close();
                    }
                }

            } catch (Throwable var6) {
                var10 = var6;
                throw var6;
            }
            finally {
                if (var10 != null) {
                    try {
                        fileWriter.close();
                    } catch (Throwable var3) {
                        var10.addSuppressed(var3);
                    }
                } else {
                    fileWriter.close();
                }
            }
        } catch (Exception e) {
            logger.error("Error while building the request XML: {}", e.getMessage());
            throw new NoSuchMethodException(e + " Error while building the request XML");
        }

        logger.info("Request file built for service for {} : {}", ServiceLoader.wsdetails.getProperty(service), requestFile);
        runService(requestFile, service);

    }

    public static void runService(StringBuilder request, String service){

        String serviceName = ServiceLoader.wsdetails.getProperty(service);
        String wsdl = ServiceLoader.wsdetails.getProperty(service + "_url");
        String baseUrlName = wsdl.split("\\{")[1].substring(1).split("}")[0];
        String url = wsdl.replace("{$"+baseUrlName+"}",ServiceLoader.config.getProperty(baseUrlName+"_baseurl"));
        String outputPath = "./target/testxmls/response/";
        Path path =Paths.get( outputPath + serviceName + ".xml");
        deleteFiles(path);
        //............................................................................................

        response = given().
                    header("Content-Type", "application/soap+xml; charset=utf-8").
                    //header("SOAPAction", ServiceLoader.wsdetails.getProperty(service + "_SOAPAction")).
                    body(request.toString()).
                    when().post(url).
                    thenReturn();

        //.............................................................................................
        if(response.getStatusCode() != 200){
            String msg = response.getBody().xmlPath().getString("faultstring");
            throw new RuntimeException("Error code " + response.getStatusCode() + " with message is "+msg);
        }
        logger.info("Response File Built for service {} : {}", serviceName, response.getBody().asString());

        File f1= new File( outputPath + serviceName + ".xml");
        try {
            BufferedWriter bwr = new BufferedWriter(new FileWriter(f1));
            Throwable var14 = null;
            try {
                bwr.write(response.getBody().asPrettyString());
            } catch (Throwable var46) {
                var14 = var46;
                throw var46;
            } finally {
                if (var14 != null) {
                    try {
                        bwr.close();
                    } catch (Throwable var2){
                        var14.addSuppressed(var2);
                    }
                }else {
                    bwr.close();
                }
            }
        }
        catch (Exception var49) {
            logger.error("Error while writing the output xml: {}", var49.getMessage());
        }
    }

    public static void validateResponseXML(String service, DataTable output) throws ParserConfigurationException, IOException, SAXException {

        Map<String, String> outputValues = output.asMaps().get(0);
        String actualValue;

        if(outputValues.containsKey("Status")){
            response.then().assertThat().statusCode(Integer.parseInt(outputValues.get("Status")));
        }
        else
            response.then().assertThat().statusCode(200);
        response.then().assertThat().contentType("application/soap+xml; charset=utf-8");

        File responseFile = new File("./target/testxmls/response/" + ServiceLoader.wsdetails.getProperty(service) + ".xml");
        DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
        docBuildFact.setNamespaceAware(true);
        DocumentBuilder builder = docBuildFact.newDocumentBuilder();
        Document doc = builder.parse(responseFile);
        doc.getDocumentElement().normalize();

        for (String expectedField : outputValues.keySet()){
            String expectedValue = outputValues.get(expectedField);
            String serviceTag = ServiceLoader.getValidationField(ServiceLoader.wsdetails.getProperty(service)+"_validation", expectedField);
            String serviceName = StringUtils.substringBefore(serviceTag, "|");
            String tagName = StringUtils.trim(StringUtils.substringAfter(serviceTag, "|"));

            if(tagName.contains(":")) {
                String[] tagNames = tagName.split(":");
                actualValue = doc.getDocumentElement().getElementsByTagNameNS("*", tagNames[1]).item(0).getTextContent();
            } else if (tagName.contains("/")) {
                actualValue = doc.getDocumentElement().getElementsByTagNameNS("*", tagName).item(0).getTextContent();
            } else
                actualValue = doc.getDocumentElement().getElementsByTagNameNS("*", tagName).item(0).getTextContent();

            if (expectedValue.equals("NotNull")) {
                Assertions.assertNotNull(actualValue);
            }
            else {
                if (actualValue.contains(".") || expectedValue.contains(".")) {
                    actualValue = String.valueOf(Double.valueOf(actualValue));
                    expectedValue = String.valueOf(Double.valueOf(expectedValue));
                }
                Assertions.assertEquals(expectedValue, actualValue);
            }
        }
    }

    private static void deleteFiles(Path path) {
        if(Files.exists(path)) {
            try{
                Files.delete(path);
                logger.info("file in the path: {} is deleted", path);
            }
            catch (IOException e){
                logger.error("Error while deleting {} file{}", path, e);
            }
        }
    }
}
