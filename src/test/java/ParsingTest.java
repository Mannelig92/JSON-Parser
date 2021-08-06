import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ParsingTest {
    String fileName;

    @BeforeEach
    void setParameters() {
        fileName = "data.csv";
    }

    @Test
    void testParseCSV() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        //when
        List<Employee> result = Parsing.parseCSV(columnMapping, fileName);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertFalse(result.isEmpty()));
    }

    @Test
    void testParseXML() {
        List<Employee> result = Parsing.parseXML("data.xml");

        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertFalse(result.isEmpty()));
    }

    @Test
    void testListToJson() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> listCSV = Parsing.parseCSV(columnMapping, fileName);
        String expected = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Smith\",\n" +
                "    \"country\": \"USA\",\n" +
                "    \"age\": 25\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"firstName\": \"Inav\",\n" +
                "    \"lastName\": \"Petrov\",\n" +
                "    \"country\": \"RU\",\n" +
                "    \"age\": 23\n" +
                "  }\n" +
                "]";

        String result = Parsing.listToJson(listCSV);

        Assertions.assertEquals(expected, result);
    }

}
