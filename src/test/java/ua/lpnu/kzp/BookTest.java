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
        PrintedBook book = new PrintedBook("Кобзар", "Тарас Шевченко", 350, 250.0);
        assertEquals("Кобзар", book.getTitle());
        assertEquals("Тарас Шевченко", book.getAuthor());
        assertEquals(BookFormat.PRINTED, book.getFormat());
        assertEquals(350, book.getPages());
        assertEquals(250.0, book.getPrice());
        assertTrue(book.calculateAnnualCost() > 0);
    }

    @Test
    @DisplayName("Перевірка створення ElectronicBook та обчислення вартості")
    void testElectronicBookCreation() {
        ElectronicBook book = new ElectronicBook("Місто", "Валеріян Підмогильний", 4.5, 120.0);
        assertEquals("Місто", book.getTitle());
        assertEquals("Валеріян Підмогильний", book.getAuthor());
        assertEquals(BookFormat.ELECTRONIC, book.getFormat());
        assertEquals(4.5, book.getFileSizeMb());
        assertEquals(120.0, book.getPrice());
        assertTrue(book.calculateAnnualCost() > 0);
    }

    @Test
    @DisplayName("Перевірка некоректних даних у конструкторах")
    void testInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("", "Автор", 100, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("Назва", " ", 100, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("Назва", "Автор", 0, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook("Назва", "Автор", 100, -10.0));
        assertThrows(IllegalArgumentException.class, () -> new ElectronicBook("Назва", "Автор", -1.0, 50.0));
    }

    @Test
    @DisplayName("Перевірка відсіювання дублікатів у HashSet (equals/hashCode)")
    void testHashSetDeduplication() {
        Set<Book> set = new HashSet<>();
        PrintedBook b1 = new PrintedBook("Кайдашева сім'я", "Іван Нечуй-Левицький", 200, 150.0);
        PrintedBook b2 = new PrintedBook("Кайдашева сім'я", "Іван Нечуй-Левицький", 200, 150.0);

        set.add(b1);
        set.add(b2);

        assertEquals(1, set.size(), "Дублікати мають відсіюватися у HashSet");
    }

    @Test
    @DisplayName("Перевірка фабричного методу fromCsv")
    void testFromCsvParsing() {
        Book printed = Book.fromCsv("Тигролови;Іван Багряний;300;180.0");
        assertTrue(printed instanceof PrintedBook);
        assertEquals("Тигролови", printed.getTitle());

        Book electronic = Book.fromCsv("Тигролови;Іван Багряний;ELECTRONIC;5.2;140.0");
        assertTrue(electronic instanceof ElectronicBook);
        assertEquals(BookFormat.ELECTRONIC, electronic.getFormat());
    }
}
