package ua.lpnu.kzp;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Потокові запити Stream API для каталогу книжок.
 */
public final class BookReport {
    private BookReport() {
    }

    /** Повертає книжки, видані не раніше вказаного року. */
    public static List<Book> filterBooksByYear(List<Book> books, int minYear) {
        return books.stream()
                .filter(book -> book.getYear() >= minYear)
                .toList();
    }

    /** Повертає назви всіх книжок. */
    public static List<String> getBookTitles(List<Book> books) {
        return books.stream()
                .map(Book::getTitle)
                .toList();
    }

    /** Підраховує кількість книжок у кожному форматі. */
    public static Map<BookFormat, Long> countByFormat(List<Book> books) {
        return books.stream()
                .collect(Collectors.groupingBy(Book::getFormat, Collectors.counting()));
    }

    /** Повертає статистику кількості сторінок лише для друкованих книжок. */
    public static IntSummaryStatistics getPrintedPagesStatistics(List<Book> books) {
        return books.stream()
                .filter(book -> book.getFormat() == BookFormat.PRINTED)
                .mapToInt(Book::getPages)
                .summaryStatistics();
    }

    /** Повертає друковані книжки з найбільшою кількістю сторінок, сортує однакові за назвою. */
    public static List<Book> getTopPrintedBooksByPages(List<Book> books, int limit) {
        Comparator<Book> byPagesDescendingThenTitle =
                Comparator.comparingInt(Book::getPages)
                        .reversed()
                        .thenComparing(Book::getTitle);
        return books.stream()
                .filter(book -> book.getFormat() == BookFormat.PRINTED)
                .sorted(byPagesDescendingThenTitle)
                .limit(limit)
                .toList();
    }

    /** Знаходить першу книжку за назвою без урахування регістру. */
    public static Optional<Book> findBookByTitle(List<Book> books, String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }
}
