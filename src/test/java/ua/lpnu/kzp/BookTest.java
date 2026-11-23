package ua.lpnu.kzp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тести ієрархії класів Book для Лабораторної № 3")
class BookTest {

    @Test
    @DisplayName("Перевірка створення PrintedBook та обчислення вартості")
    void testPrintedBookCreation() {
        PrintedBook book = new PrintedBook("Кобзар", "Тарас Шевченко", 1960, 350, 250.0);
        assertEquals("Кобзар", book.getTitle());
        assertEquals("Тарас Шевченко", book.getAuthor());
        assertEquals(1960, book.getYear());
        assertEquals(BookFormat.PRINTED, book.getFormat());
        assertEquals(350, book.getPages());
        assertEquals(250.0, book.getPrice());
        assertTrue(book.calculateAnnualCost() > 0);
    }

    @Test
    @DisplayName("Перевірка створення ElectronicBook та обчислення вартості")
    void testElectronicBookCreation() {
        ElectronicBook book = new ElectronicBook("Місто", "Валеріян Підмогильний", 1928, 4.5, 120.0);
        assertEquals("Місто", book.getTitle());
        assertEquals("Валеріян Підмогильний", book.getAuthor());
        assertEquals(1928, book.getYear());
        assertEquals(BookFormat.ELECTRONIC, book.getFormat());
        assertEquals(0, book.getPages());
        assertEquals(4.5, book.getFileSizeMb());
        assertEquals(120.0, book.getPrice());
        assertTrue(book.calculateAnnualCost() > 0);
    }

    @Test
    @DisplayName("Перевірка некоректних даних у конструкторах")
    void testInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("", "Автор", 2024, 100, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("Назва", " ", 2024, 100, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("Назва", "Автор", 0, 100, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("Назва", "Автор", 2024, 0, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("Назва", "Автор", 2024, 100, -10.0));
        assertThrows(IllegalArgumentException.class,
                () -> new ElectronicBook("Назва", "Автор", 2024, -1.0, 50.0));
    }

    @Test
    @DisplayName("Перевірка відхилення NaN та нескінченності")
    void testRejectsNaNAndInfinity() {
        assertThrows(IllegalArgumentException.class,
                () -> new PrintedBook("Кобзар", "Шевченко", 2024, 100, Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> new PrintedBook("Кобзар", "Шевченко", 2024, 100, Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class,
                () -> new ElectronicBook("Місто", "Підмогильний", 2024, Double.NaN, 100.0));
        assertThrows(IllegalArgumentException.class,
                () -> new ElectronicBook("Місто", "Підмогильний", 2024, Double.POSITIVE_INFINITY, 100.0));
        assertThrows(IllegalArgumentException.class,
                () -> new ElectronicBook("Місто", "Підмогильний", 2024, 5.0, Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> new ElectronicBook("Місто", "Підмогильний", 2024, 5.0, Double.POSITIVE_INFINITY));
    }

    @Test
    @DisplayName("Перевірка відсіювання дублікатів у HashSet (equals/hashCode)")
    void testHashSetDeduplication() {
        Set<Book> set = new HashSet<>();
        PrintedBook b1 = new PrintedBook("Кайдашева сім'я", "Іван Нечуй-Левицький", 1879, 200, 150.0);
        PrintedBook b2 = new PrintedBook("Кайдашева сім'я", "Іван Нечуй-Левицький", 1879, 200, 150.0);

        set.add(b1);
        set.add(b2);

        assertEquals(1, set.size(), "Дублікати мають відсіюватися у HashSet");
    }

    @Test
    @DisplayName("Рік видання враховується в equals і hashCode")
    void testYearParticipatesInEquality() {
        Book first = new PrintedBook("Кобзар", "Тарас Шевченко", 1840, 350, 250.0);
        Book second = new PrintedBook("Кобзар", "Тарас Шевченко", 1960, 350, 250.0);

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("Перевірка фабричного методу fromCsv")
    void testFromCsvParsing() {
        Book printed = Book.fromCsv("Тигролови;Іван Багряний;1944;300;180.0;PRINTED");
        assertTrue(printed instanceof PrintedBook);
        assertEquals("Тигролови", printed.getTitle());
        assertEquals(1944, printed.getYear());

        Book electronic = Book.fromCsv("Тигролови;Іван Багряний;1944;5.2;140.0;ELECTRONIC");
        assertTrue(electronic instanceof ElectronicBook);
        assertEquals(BookFormat.ELECTRONIC, electronic.getFormat());
        assertEquals(0, electronic.getPages());
    }

    @Test
    @DisplayName("Фабричний метод відхиляє CSV не з шести полів")
    void testFromCsvRejectsOldFormat() {
        assertThrows(IllegalArgumentException.class,
                () -> Book.fromCsv("Тигролови;Іван Багряний;300;180.0"));
    }
}
