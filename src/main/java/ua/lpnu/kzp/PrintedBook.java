package ua.lpnu.kzp;

import java.util.Locale;
import java.util.Objects;

/**
 * Друкована книжка (конкретний підклас).
 */
public class PrintedBook extends Book {
    private final int pages;
    private final double price;

    public PrintedBook(String title, String author, int pages, double price) {
        super(title, author, BookFormat.PRINTED);
        if (pages <= 0) {
            throw new IllegalArgumentException("Кількість сторінок мусить бути більшою за 0");
        }
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною або некоректною");
        }
        this.pages = pages;
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public double calculateAnnualCost() {
        return price * 0.05 + pages * 0.1;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        PrintedBook that = (PrintedBook) o;
        return pages == that.pages && Double.compare(that.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pages, price);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s, %d стор., ціна: %.2f грн", super.toString(), pages, price);
    }
}