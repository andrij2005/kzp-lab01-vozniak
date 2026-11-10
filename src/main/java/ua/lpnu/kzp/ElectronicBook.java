package ua.lpnu.kzp;

import java.util.Locale;
import java.util.Objects;

/**
 * Електронна книжка (конкретний підклас).
 */
public class ElectronicBook extends Book {
    private final double fileSizeMb;
    private final double price;

    public ElectronicBook(String title, String author, double fileSizeMb, double price) {
        super(title, author, BookFormat.ELECTRONIC);
        if (fileSizeMb <= 0) {
            throw new IllegalArgumentException("Розмір файлу мусить бути більшим за 0 МБ");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною");
        }
        this.fileSizeMb = fileSizeMb;
        this.price = price;
    }

    public double getFileSizeMb() {
        return fileSizeMb;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public double calculateAnnualCost() {
        return price * 0.02 + fileSizeMb * 0.5;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        ElectronicBook that = (ElectronicBook) o;
        return Double.compare(that.fileSizeMb, fileSizeMb) == 0 && Double.compare(that.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileSizeMb, price);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s, %.1f МБ, ціна: %.2f грн", super.toString(), fileSizeMb, price);
    }
}