package employee;

import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Класс CSVParser парсирует CSV файл с данными о сотрудниках
 */
public class CSVParser {
    
    private static final char SEPARATOR = ';';
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    
    /**
     * Метод parseCSV читает данные из CSV файла и преобразует их в список объектов Person
     *
     * @param csvFilePath путь к CSV файлу в ресурсах
     * @return список объектов Person
     * @throws IOException если возникает ошибка чтения файла
     * @throws ParseException если возникает ошибка парсинга даты
     * 
     * * 1. В оригинальном примере используется конструкция(приведённый в условии задания):
     *    try (InputStream in = ...; CSVReader reader = in == null ? null : ...)
     *    Проблема: try-with-resources не может работать с null
     *    Решение: сначала проверяем InputStream, затем создаем CSVReader
     * 
     * * 2. В оригинале(в приведённом условии задания): CSVReader reader = new CSVReader(new InputStreamReader(in), separator)
     *    Проблема: такой конструктор может не существовать в текущей версии OpenCSV
     *    Решение: используем CSVReaderBuilder и CSVParserBuilder для указания разделителя
     */
    public List<Person> parseCSV(String csvFilePath) throws IOException, ParseException {
        List<Person> people = new ArrayList<>();
        Map<String, Division> divisionCache = new HashMap<>();
        InputStream in = getClass().getClassLoader().getResourceAsStream(csvFilePath);
        // Если файл не найден, выбрасываем исключение сразу!
        if (in == null) {
            throw new FileNotFoundException("Файл не найден: " + csvFilePath);
        }
        try (com.opencsv.CSVReader reader = new com.opencsv.CSVReaderBuilder(new InputStreamReader(in))
                .withCSVParser(new com.opencsv.CSVParserBuilder()
                    .withSeparator(SEPARATOR)
                    .build())
                .build()) {
            // Пропускаем заголовок
            reader.readNext();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 6) {
                    Person person = parsePerson(nextLine, divisionCache);
                    people.add(person);
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException("Ошибка валидации CSV файла", e);
        } 
        return people;
    }
    
    /**
     * Метод parsePerson преобразует строку CSV в объект Person
     *
     * @param line массив значений из CSV строки
     * @param divisionCache кэш подразделений для избежания дублирования
     * @return объект Person
     * @throws ParseException если возникает ошибка парсинга даты
     */
    private Person parsePerson(String[] line, Map<String, Division> divisionCache) throws ParseException {
        Person person = new Person();
        
        // Парсинг ID (добавляем проверку)
        try {
            person.setId(Long.parseLong(line[0].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный ID: " + line[0], e);
        }
        // Парсинг имени
        person.setName(line[1].trim());
        // Парсинг пола
        String genderStr = line[2].trim();
        person.setGender("Male".equalsIgnoreCase(genderStr) ? 
            Person.Gender.MALE : Person.Gender.FEMALE);
        // Парсинг даты рождения
        try {
            Date date = dateFormatter.parse(line[3].trim());
            LocalDate birthDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            person.setBirthDate(birthDate);
        } catch (ParseException e) {
            throw new ParseException("Некорректная дата рождения: " + line[3], 0);
        }
        // Парсинг подразделения работника
        String divisionName = line[4].trim();
        Division division = divisionCache.get(divisionName);
        if (division == null) {
            division = new Division(divisionName);
            divisionCache.put(divisionName, division);
        }
        person.setDivision(division);
        // Парсинг зарплаты
        try {
            person.setSalary(Integer.parseInt(line[5].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректная зарплата: " + line[5], e);
        }
        return person;
    }
}
