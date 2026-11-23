package ua.lpnu.kzp;

import java.util.Locale;
import java.util.Objects;

/**
 * Абстрактний базовий клас книжки для Лабораторної № 3.
 */
public abstract sealed class Book permits PrintedBook, ElectronicBook {
    private final String title;
    private final String author;
    private final int year;
    private final BookFormat format;

    protected Book(String title, String author, int year, BookFormat format) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.format = format;
    }

    protected static String validatedTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Назва книжки не може бути порожньою");
        }
        return title.trim();
    }

    protected static String validatedAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Автор не може бути порожнім");
        }
        return author.trim();
    }

    protected static int validatedYear(int year) {
        if (year <= 0) {
            throw new IllegalArgumentException("Рік видання має бути більшим за 0");
        }
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public BookFormat getFormat() {
        return format;
    }

    /** Повертає кількість друкованих сторінок або 0, якщо вона не застосовується. */
    public abstract int getPages();

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
        if (parts.length != 6) {
            throw new IllegalArgumentException("Некоректний формат CSV: очікується 6 полів");
        }

        String title = parts[0].trim();
        String author = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        double price = Double.parseDouble(parts[4].trim());
        BookFormat format = BookFormat.valueOf(parts[5].trim().toUpperCase(Locale.ROOT));

        return switch (format) {
            case PRINTED -> new PrintedBook(
                    title, author, year, Integer.parseInt(parts[3].trim()), price);
            case ELECTRONIC -> new ElectronicBook(
                    title, author, year, Double.parseDouble(parts[3].trim()), price);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
               Objects.equals(author, book.author) &&
               year == book.year &&
               format == book.format;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, year, format);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s: '%s' (Автор: %s)", format.getDisplayName(), title, author);
    }
}