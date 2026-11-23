package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Головний клас програми для каталогу книжок.
 */
public class Main {
    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        Path outputDirectory = Path.of("out");
        Path outputPath = outputDirectory.resolve("report.txt");
        List<Book> books = new ArrayList<>();
        int totalRows = 0;
        int errorRows = 0;

        try {
            List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) {
                    continue;
                }
                totalRows++;
                try {
                    books.add(Book.fromCsv(line));
                } catch (IllegalArgumentException e) {
                    errorRows++;
                    System.err.printf("Помилка в рядку %d: %s%n", i + 1, e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка читання файлу " + inputPath + ": " + e.getMessage());
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("=== ЗВІТ КАТАЛОГУ КНИЖОК (Лабораторна № 4) ===\n");
        report.append(String.format(Locale.ROOT, "Оброблено рядків: %d (Успішно: %d, Помилок: %d)%n%n",
                totalRows, books.size(), errorRows));
        report.append("Книжки в каталозі:\n");
        books.stream()
                .forEach(book -> report.append(String.format(Locale.ROOT, "- %s | Річна вартість: %.2f грн%n",
                        book, book.calculateAnnualCost())));

        report.append("\nНазви книжок:\n");
        BookReport.getBookTitles(books).forEach(title -> report.append("- ").append(title).append('\n'));

        report.append("\nКількість книжок за форматом:\n");
        Map<BookFormat, Long> countsByFormat = BookReport.countByFormat(books);
        countsByFormat.forEach((format, count) ->
                report.append(String.format(Locale.ROOT, "- %s: %d%n", format.getDisplayName(), count)));

        var pageStatistics = BookReport.getPrintedPagesStatistics(books);
        report.append(String.format(Locale.ROOT,
                "%nСтатистика сторінок друкованих книжок: кількість=%d, сума=%d, мінімум=%d, максимум=%d, середнє=%.2f%n",
                pageStatistics.getCount(), pageStatistics.getSum(), pageStatistics.getMin(),
                pageStatistics.getMax(), pageStatistics.getAverage()));

        report.append("\nДруковані книжки, видані від 2020 року:\n");
        BookReport.filterBooksByYear(books, 2020)
                .stream()
                .filter(book -> book.getFormat() == BookFormat.PRINTED)
                .forEach(book -> report.append("- ").append(book.getTitle()).append('\n'));

        report.append("\nТоп-5 друкованих книжок за кількістю сторінок:\n");
        BookReport.getTopPrintedBooksByPages(books, 5)
                .forEach(book -> report.append(String.format(Locale.ROOT, "- %s (%d стор.)%n",
                        book.getTitle(), book.getPages())));

        double totalAnnualCost = books.stream()
                .mapToDouble(Book::calculateAnnualCost)
                .sum();
        report.append(String.format(Locale.ROOT,
                "%nЗагальна річна вартість обслуговування каталогу: %.2f грн%n", totalAnnualCost));

        System.out.print(report);

        try {
            Files.createDirectories(outputDirectory);
            Files.writeString(outputPath, report.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Помилка запису у файл звіту " + outputPath + ": " + e.getMessage());
        }
    }
}