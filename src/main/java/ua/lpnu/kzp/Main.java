package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Головний клас програми для Лабораторної роботи № 3.
 */
public class Main {
    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        List<Book> books = new ArrayList<>();
        int totalRows = 0;
        int validRows = 0;
        int errorRows = 0;

        if (Files.exists(inputPath)) {
            try {
                List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line.isBlank()) {
                        continue;
                    }
                    totalRows++;
                    try {
                        Book book = Book.fromCsv(line);
                        books.add(book);
                        validRows++;
                    } catch (Exception e) {
                        errorRows++;
                    }
                }
            } catch (IOException e) {
                System.err.println("Помилка читання файлу: " + e.getMessage());
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("=== ЗВІТ КАТАЛОГУ КНИЖОК (Лабораторна № 3) ===\n");
        report.append(String.format(Locale.ROOT, "Оброблено рядків: %d (Успішно: %d, Помилок: %d)\n\n",
                totalRows, validRows, errorRows));

        double totalAnnualCost = 0.0;
        for (Book book : books) {
            double cost = book.calculateAnnualCost();
            totalAnnualCost += cost;
            report.append(String.format(Locale.ROOT, "- %s | Річна вартість: %.2f грн\n",
                    book.toString(), cost));
        }

        report.append(String.format(Locale.ROOT, "\nЗагальна річна вартість обслуговування каталогу: %.2f грн\n", totalAnnualCost));

        System.out.print(report);
    }
}