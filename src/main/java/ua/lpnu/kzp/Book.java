package ua.lpnu.kzp;

import java.util.Locale;
import java.util.Objects;

/**
 * Абстрактний базовий клас книжки для Лабораторної № 3.
 */
public abstract class Book {
    private final String title;
    private final String author;
    private final BookFormat format;

    protected Book(String title, String author, BookFormat format) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Назва книжки не може бути порожньою");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Автор не може бути порожнім");
        }
        if (format == null) {
            throw new IllegalArgumentException("Формат книжки не може бути null");
        }
        this.title = title.trim();
        this.author = author.trim();
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public BookFormat getFormat() {
        return format;
    }

    /**
     * Поліморфний метод розрахунку річної вартості обслуговування/зберігання.
     */
    public abstract double calculateAnnualCost();

    /**
     * Фабричний метод створення книжки з CSV-рядка.
     */
    public static Book fromCsv(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Рядок CSV не може бути порожнім");
        }
        String[] parts = line.split(";", -1);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Некоректний формат CSV: недостатньо полів");
        }

        String title = parts[0].trim();
        String author = parts[1].trim();

        // 1. Стандартний формат з 4 полів (PrintedBook): назва;автор;сторінки;ціна
        if (parts.length == 4) {
            int pages = Integer.parseInt(parts[2].trim());
            double price = Double.parseDouble(parts[3].trim());
            return new PrintedBook(title, author, pages, price);
        }

        // 2. Формат з 5 полів, де ELECTRONIC стоїть 3-м полем: назва;автор;ELECTRONIC;розмірМб;ціна
        String p2 = parts[2].trim().toUpperCase(Locale.ROOT);
        if ("ELECTRONIC".equals(p2)) {
            double fileSizeMb = Double.parseDouble(parts[3].trim());
            double price = Double.parseDouble(parts[4].trim());
            return new ElectronicBook(title, author, fileSizeMb, price);
        }

        // 3. Альтернативний формат з 5 полів, де ELECTRONIC стоїть 5-м полем: назва;автор;розмірМб;ціна;ELECTRONIC
        String p4 = parts[4].trim().toUpperCase(Locale.ROOT);
        if ("ELECTRONIC".equals(p4)) {
            double fileSizeMb = Double.parseDouble(parts[2].trim());
            double price = Double.parseDouble(parts[3].trim());
            return new ElectronicBook(title, author, fileSizeMb, price);
        }

        // За замовчуванням для 5 полів
        int pages = Integer.parseInt(parts[2].trim());
        double price = Double.parseDouble(parts[3].trim());
        return new PrintedBook(title, author, pages, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
               Objects.equals(author, book.author) &&
               format == book.format;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, format);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s: '%s' (Автор: %s)", format.getDisplayName(), title, author);
    }
}