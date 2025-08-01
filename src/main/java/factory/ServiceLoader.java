package factory;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ServiceLoader {

    private static final Logger logger = LogManager.getLogger("ServiceLoader.class");

    public static final Properties config = new Properties();
    public static final Properties wsdetails = new Properties();

    private static final Map<String, Map<String, String>> inputTable = new HashMap<>();
    private static final Map<String, Map<String, String>> validationXMLMap = new HashMap<>();
    private static final Map<String, Object> globalValues = new HashMap<>();



    public static void loadInputTable(String service, Map<String, String> input){
        inputTable.put(service, input);
    }

    public static String getInputTable(String service, String columnName){
        return inputTable.get(service) != null ? (inputTable.get(service)).getOrDefault(columnName, "") : "";
    }

    public static void clearInputTable(){
        inputTable.clear();
    }

    public static Map<String, Object> getGlobalValues() {
        return globalValues;
    }

    public static void setGlobalValue(String key, Object value) {
        globalValues.put(key, value);
    }

    public static void clearGlobalValues(){
        globalValues.clear();
    }


    public static void loadProperties(){
        loadProperties(config, System.getProperty("user.dir") + "//configure.properties", "configuration");
        loadProperties(wsdetails, System.getProperty("user.dir") + "//src//test//resources//properties//wsdls.properties", "WS endpoints");
        loadValidationXMLs();
        createDirectories("./target/testxmls/request/");
        createDirectories("./target/testxmls/response/");
    }
    private static void loadProperties(Properties properties, String filePath, String fileType) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            logger.info("{} properties loaded successfully from: {}", fileType, filePath);
        } catch (FileNotFoundException e) {
            logger.error("{} file not found: {}", fileType, filePath, e);
        } catch (IOException e) {
            logger.error("Error while loading {} properties from: {}", fileType, filePath, e);
        }
    }

    private static void loadValidationXMLs(){
        File xmlFile = null;
        int xmlCount = 0;
        try {
            String xmlDataDir = Thread.currentThread().getContextClassLoader().getResource("validationXMLFiles").getFile();
            Iterator<File> files = FileUtils.iterateFiles(new File(xmlDataDir), new String[]{"xml"}, true);
            while (files.hasNext()){
                Map<String, String> fields = new LinkedHashMap<>();
                xmlFile = files.next();
                DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
                docBuildFact.setNamespaceAware(true);
                DocumentBuilder builder = docBuildFact.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("XMLTag");
                String service = FilenameUtils.removeExtension(xmlFile.getName());

                for (int i = 0; i < nodeList.getLength(); ++i) {
                    String expectedParam = doc.getElementsByTagName("ExpectedParameter").item(i).getTextContent();
                    String tag = doc.getElementsByTagName("Tag").item(i).getTextContent();
                    String fileName = doc.getElementsByTagName("RequestFile").item(i).getTextContent();
                    fields.put(expectedParam, fileName+"|"+tag);
                }
                validationXMLMap.put(service, fields);
            }
            xmlCount = validationXMLMap.size();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            logger.error("Failed while loading XMLs, No.of validation XML files loaded: {}", xmlCount );
            logger.error("Failed at the line: {}", e.getMessage());
            if(xmlFile!=null){
                logger.error("Validation xml failed for file: {}", xmlFile.getName());
            }
        }
        logger.info("No.of validation xml files loaded: {}", xmlCount);
    }

    public static String getValidationField(String fileName, String expectedField) {
        return validationXMLMap.get(fileName).getOrDefault(expectedField, "");
    }

    private static void createDirectories(String path){

        File directory =new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                logger.info("Directory created: " + path);
            } else {
                logger.error("Failed to create directory.");
            }
        }
    }

}
