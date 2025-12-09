package employee;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс PersonService содержит методы для работы со списком сотрудников
 */
public class PersonService {
    
    private List<Person> people;
    
    /**
     * Конструктор класса
     *
     * @param people список людей для обработки
     */
    public PersonService(List<Person> people) {
        this.people = people;
    }
    
    /**
     * Гетер возвращает список всех людей
     *
     * @return список людей
     */
    public List<Person> getAllPeople() {
        return people;
    }
    
    /**
     * Метод findPersonById находит человека по его ID
     *
     * @param id идентификатор человека
     * @return объект Person или null, если не найден
     */
    public Person findPersonById(Long id) {
        return people.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Метод getPeopleByGender возвращает людей с указанным полом
     *
     * @param gender пол для фильтрации
     * @return список людей указанного пола
     */
    public List<Person> getPeopleByGender(Person.Gender gender) {
        return people.stream()
            .filter(p -> p.getGender() == gender)
            .collect(Collectors.toList());
    }
    
    /**
     * Метод groupPeopleByDivision() группирует людей по подразделениям
     *
     * @return карта, где ключ - название подразделения, значение - список людей в этом подразделении
     */
    public Map<String, List<Person>> groupPeopleByDivision() {
        return people.stream()
            .collect(Collectors.groupingBy(
                p -> p.getDivision().getName(),
                Collectors.toList()
            ));
    }
    
    /**
     * Метод calculateAverageSalary() вычисляет среднюю зарплату по всем сотрудникам
     *
     * @return средняя зарплата
     */
    public double calculateAverageSalary() {
        return people.stream()
            .mapToInt(Person::getSalary)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Метод getOldestPerson() возвращает самого старшего человека из списка
     *
     * @return самый старый человек или null, если список пуст
     */
    public Person getOldestPerson() {
        return people.stream()
            .min((p1, p2) -> p1.getBirthDate().compareTo(p2.getBirthDate()))
            .orElse(null);
    }
    
    /**
     * Метод getYoungestPerson() возвращает самого молодого человека из списка
     *
     * @return самый молодой человек или null, если список пуст
     */
    public Person getYoungestPerson() {
        return people.stream()
            .max((p1, p2) -> p1.getBirthDate().compareTo(p2.getBirthDate()))
            .orElse(null);
    }
    
    /**
     * Метод getPeopleCountByDivision() возвращает количество людей в каждом подразделении из списка
     *
     * @return карта с количеством людей по подразделениям
     */
    public Map<String, Long> getPeopleCountByDivision() {
        return people.stream()
            .collect(Collectors.groupingBy(
                p -> p.getDivision().getName(),
                Collectors.counting()
            ));
    }
}