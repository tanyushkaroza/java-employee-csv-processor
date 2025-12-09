package employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PersonServiceTest тестовый класс для проверки функциональности PersonService.
 */
class PersonServiceTest {
    
    private PersonService personService;
    private List<Person> testPeople;
    
    @BeforeEach
    void setUp() {
        Division divisionA = new Division("Division A");
        Division divisionB = new Division("Division B");
        testPeople = Arrays.asList(
            new Person(1L, "Ozzy Osbourne", Person.Gender.MALE, 
                LocalDate.of(1948, 12, 3), divisionA, 50000),
            new Person(2L, "Lady Gaga", Person.Gender.FEMALE, 
                LocalDate.of(1986, 3, 28), divisionA, 60000),
            new Person(3L, "Hans Rudolf Giger", Person.Gender.MALE, 
                LocalDate.of(1940, 2, 5), divisionB, 55000),
            new Person(4L, "Madonna", Person.Gender.FEMALE, 
                LocalDate.of(1958, 8, 16), divisionB, 70000),
            new Person(5L, "Alfredo James Pacino", Person.Gender.MALE, 
                LocalDate.of(1940, 4, 25), divisionA, 45000)
        );
        personService = new PersonService(testPeople);
    }
    
    @Test
    void testGetAllPeople() {
        // Проверка получения всех людей
        List<Person> result = personService.getAllPeople();
        assertEquals(5, result.size());
        assertEquals(testPeople, result);
    }
    
    @Test
    void testFindPersonById_Found() {
        // Проверка поиска существующего человека
        Person result = personService.findPersonById(2L);
        assertNotNull(result);
        assertEquals("Lady Gaga", result.getName());
        assertEquals(Person.Gender.FEMALE, result.getGender());
    }
    
    @Test
    void testFindPersonById_NotFound() {
        // Проверка поиска несуществующего человека
        Person result = personService.findPersonById(999L);
        assertNull(result);
    }
    
    @Test
    void testGetPeopleByGender() {
        // Проверка фильтрации по полу
        List<Person> males = personService.getPeopleByGender(Person.Gender.MALE);
        assertEquals(3, males.size());
        List<Person> females = personService.getPeopleByGender(Person.Gender.FEMALE);
        assertEquals(2, females.size());
    }
    
    @Test
    void testGroupPeopleByDivision() {
        // Проверка группировки по подразделениям
        var result = personService.groupPeopleByDivision();
        assertTrue(result.containsKey("Division A"));
        assertTrue(result.containsKey("Division B"));
        assertEquals(3, result.get("Division A").size());
        assertEquals(2, result.get("Division B").size());
    }
    
    @Test
    void testCalculateAverageSalary() {
        // Проверка вычисления средней зарплаты
        // 50000 + 60000 + 55000 + 70000 + 45000 = 280000
        // 280000 / 5 = 56000
        double expectedAverage = 56000.0;
        double result = personService.calculateAverageSalary();
        assertEquals(expectedAverage, result, 0.01);
    }
    
    @Test
    void testGetOldestPerson() {
        // Проверка нахождения самого старшего человека
        Person oldest = personService.getOldestPerson();
        assertNotNull(oldest);
        assertEquals(3L, oldest.getId());
        assertEquals("Hans Rudolf Giger", oldest.getName());
    }
    
    @Test
    void testGetYoungestPerson() {
        // Проверка нахождения самого молодого человека
        Person youngest = personService.getYoungestPerson();
        assertNotNull(youngest);
        assertEquals(2L, youngest.getId());
        assertEquals("Lady Gaga", youngest.getName());
    }
    
    @Test
    void testGetPeopleCountByDivision() {
        // Проверка подсчета людей по подразделениям
        var result = personService.getPeopleCountByDivision();
        assertEquals(3, result.get("Division A"));
        assertEquals(2, result.get("Division B"));
    }
    
    @Test
    void testEmptyList() {
        // Проверка работы с пустым списком
        PersonService emptyService = new PersonService(List.of());
        assertTrue(emptyService.getAllPeople().isEmpty());
        assertNull(emptyService.getOldestPerson());
        assertNull(emptyService.getYoungestPerson());
        assertEquals(0.0, emptyService.calculateAverageSalary());
    }
}
