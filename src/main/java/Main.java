import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> listCSV = Parsing.parseCSV(columnMapping, fileName);
        List<Employee> listXML = Parsing.parseXML("data.xml");

        String csvToJson = Parsing.listToJson(listCSV);
        System.out.println(csvToJson);  //проверка
        String dataCsvToJson = "data.json";
        Parsing.writeString(csvToJson, dataCsvToJson);

        String xmlToJson = Parsing.listToJson(listXML);
        String dataXmlToJson = "data2.json";
        Parsing.writeString(xmlToJson, dataXmlToJson);
    }
}
