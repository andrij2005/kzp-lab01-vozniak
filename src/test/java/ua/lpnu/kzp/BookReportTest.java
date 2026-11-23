package ua.lpnu.kzp;

import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookReportTest {
    private final List<Book> books = List.of(
            new PrintedBook("Zeta", "Автор", 2020, 500, 100.0),
            new PrintedBook("Alpha", "Автор", 2021, 500, 100.0),
            new PrintedBook("Middle", "Автор", 2019, 300, 100.0),
            new PrintedBook("Beta", "Автор", 2022, 200, 100.0),
            new PrintedBook("Delta", "Автор", 2018, 100, 100.0),
            new PrintedBook("Gamma", "Автор", 2023, 50, 100.0),
            new ElectronicBook("Digital", "Автор", 2024, 5.0, 100.0));

    @Test
    void filtersBooksByYearAndHandlesEmptyInput() {
        assertEquals(
                List.of("Zeta", "Alpha", "Beta", "Gamma", "Digital"),
                BookReport.filterBooksByYear(books, 2020).stream().map(Book::getTitle).toList());
        assertTrue(BookReport.filterBooksByYear(List.of(), 2020).isEmpty());
    }

    @Test
    void mapsBooksToTitlesAndHandlesEmptyInput() {
        assertEquals(
                List.of("Zeta", "Alpha", "Middle", "Beta", "Delta", "Gamma", "Digital"),
                BookReport.getBookTitles(books));
        assertTrue(BookReport.getBookTitles(List.of()).isEmpty());
    }

    @Test
    void countsBooksByFormatAndHandlesEmptyInput() {
        assertEquals(Map.of(BookFormat.PRINTED, 6L, BookFormat.ELECTRONIC, 1L),
                BookReport.countByFormat(books));
        assertTrue(BookReport.countByFormat(List.of()).isEmpty());
    }

    @Test
    void summarizesPrintedPagesAndHandlesEmptyInput() {
        IntSummaryStatistics statistics = BookReport.getPrintedPagesStatistics(books);

        assertEquals(6, statistics.getCount());
        assertEquals(1650, statistics.getSum());
        assertEquals(50, statistics.getMin());
        assertEquals(500, statistics.getMax());
        assertEquals(275.0, statistics.getAverage());

        IntSummaryStatistics emptyStatistics = BookReport.getPrintedPagesStatistics(List.of());
        assertEquals(0, emptyStatistics.getCount());
        assertEquals(0.0, emptyStatistics.getAverage());
    }

    @Test
    void selectsTopPrintedBooksByPagesAndRespectsLimit() {
        assertEquals(
                List.of("Alpha", "Zeta", "Middle", "Beta", "Delta"),
                BookReport.getTopPrintedBooksByPages(books, 5).stream().map(Book::getTitle).toList());
        assertTrue(BookReport.getTopPrintedBooksByPages(books, 0).isEmpty());
        assertEquals(6, BookReport.getTopPrintedBooksByPages(books, 10).size());
        assertTrue(BookReport.getTopPrintedBooksByPages(List.of(), 5).isEmpty());
    }

    @Test
    void findsTitleIgnoringCaseAndReturnsEmptyWhenMissing() {
        Optional<Book> found = BookReport.findBookByTitle(books, "dIgItAl");

        assertTrue(found.isPresent());
        assertEquals(BookFormat.ELECTRONIC, found.orElseThrow().getFormat());
        assertFalse(BookReport.findBookByTitle(books, "Unknown").isPresent());
        assertFalse(BookReport.findBookByTitle(List.of(), "Zeta").isPresent());
    }
}
