package employee;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CSVParserTest тестовый класс для проверки функциональности CSVParser
 */
class CSVParserTest {
    
    @Test
    void testParseCSV() throws Exception {
        CSVParser parser = new CSVParser();
        List<Person> people = parser.parseCSV("foreign_names.csv");
        assertNotNull(people);
        assertFalse(people.isEmpty());
        Person firstPerson = people.stream()
            .filter(p -> p.getId().equals(28281L))
            .findFirst()
            .orElse(null);
        if (firstPerson != null) {
            assertEquals("Aahan", firstPerson.getName());
            assertEquals(Person.Gender.MALE, firstPerson.getGender());
            assertNotNull(firstPerson.getDivision());
            assertEquals("I", firstPerson.getDivision().getName());
            assertEquals(4800, firstPerson.getSalary());
        }
    }
    
    @Test
    void testParseCSV_FileNotFound() {
        CSVParser parser = new CSVParser();
        Exception exception = assertThrows(Exception.class, () -> {
            parser.parseCSV("nonexistent.csv");
        });
        assertTrue(exception.getMessage().contains("Файл не найден"));
    }
}