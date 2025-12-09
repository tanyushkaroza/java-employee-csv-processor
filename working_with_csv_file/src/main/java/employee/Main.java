package employee;

import java.util.List;

/**
 * Основной класс приложения для демонстрации работы с CSV данными
 */
public class Main {
    
    /**
     * Точка входа в приложение
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        try {
            // 1. Парсинг CSV файла
            CSVParser parser = new CSVParser();
            List<Person> people = parser.parseCSV("foreign_names.csv");
            System.out.println("Успешно загружено " + people.size() + " записей");
            
            // 2. Создание сервиса для работы с данными
            PersonService service = new PersonService(people);
            
            // 3. Примеры использования сервиса
            System.out.println("\n Статистика по работникам\n"+
            		"\nВсего людей: " + service.getAllPeople().size() +
            	    "\nМужчин: " + service.getPeopleByGender(Person.Gender.MALE).size() +
            	    "\nЖенщин: " + service.getPeopleByGender(Person.Gender.FEMALE).size() +
            	    String.format("\nСредняя зарплата: %.2f", service.calculateAverageSalary()));
            
            System.out.println("\nСамый возрастной работник");
            Person oldest = service.getOldestPerson();
            if (oldest != null) {
                System.out.println("ID: " + oldest.getId() + ", Имя: " + oldest.getName() + 
                    ", Дата рождения: " + oldest.getBirthDate());
            }
            
            System.out.println("\nСамый молодой работник");
            Person youngest = service.getYoungestPerson();
            if (youngest != null) {
                System.out.println("ID: " + youngest.getId() + ", Имя: " + youngest.getName() + 
                    ", Дата рождения: " + youngest.getBirthDate());
            }
            
            System.out.println("\nКоличество людей в подразделениях");
            service.getPeopleCountByDivision().forEach((division, count) -> 
                System.out.println(division + ": " + count + " человек"));
            
            // 4. Пример поиска конкретного человека
            System.out.println("\nПоиск человека с ID 28300");
            Person person = service.findPersonById(28300L);
            if (person != null) {
                System.out.println("Найден: " + person);
            } else {
                System.out.println("Человек не найден");
            }
            
        } catch (Exception e) {
            System.err.println("Ошибка при обработке CSV файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
}