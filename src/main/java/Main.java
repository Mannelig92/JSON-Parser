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

        String CsvToJson = listToJson(listCSV);
        String dataCsvToJson = "data.json";
        writeString(CsvToJson, dataCsvToJson);

        String xmlToJson = listToJson(listXML);
        String dataXmlToJson = "data2.json";
        writeString(xmlToJson, dataXmlToJson);
    }

    private static List<Employee> parseXML(String s) { //проблема полагаю тут
        Document doc = null;
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(s));

            Node root = doc.getDocumentElement();    //получение корневого узла
            NodeList nodeList = root.getChildNodes();   //извлечение списка узлов
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                if(Node.ELEMENT_NODE == node_.getNodeType()) {
                    System.out.println("Текущий узел: " + node_.getNodeName());
                    Element element = (Element) node_;
                    Employee employee = (Employee) element;
                    list.add(employee);
                }
            }
//            for (Employee i : list) {
//                System.out.println(i);
//            }
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

    private static String listToJson(List<Employee> list) { // не понимаю куда вставить \n что бы было не в одну строку
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
