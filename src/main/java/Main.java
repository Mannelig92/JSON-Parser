import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> listCSV = parseCSV(columnMapping, fileName);
        List<Employee> listXML = parseXML("data.xml");

        String csvToJson = listToJson(listCSV);
        String dataCsvToJson = "data.json";
        writeString(csvToJson, dataCsvToJson);

        String xmlToJson = listToJson(listXML);
        String dataXmlToJson = "data2.json";
        writeString(xmlToJson, dataXmlToJson);
    }

    private static List<Employee> parseXML(String data) { //проблема полагаю тут
        Document doc = null;
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(data));

            Node root = doc.getDocumentElement();    //получение корневого узла
            NodeList nodeList = root.getChildNodes();   //извлечение списка узлов
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                if (Node.ELEMENT_NODE == node_.getNodeType()) {
                    Element element = (Element) node_;
                    long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                    list.add(new Employee(id, firstName, lastName, country, age));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String data) {
        try (FileWriter file = new FileWriter(data)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
