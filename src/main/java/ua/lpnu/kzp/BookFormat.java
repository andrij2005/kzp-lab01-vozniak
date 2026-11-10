package ua.lpnu.kzp;

/**
 * Перелічення можливих форматів книжок для Лабораторної № 3.
 */
public enum BookFormat {
    PRINTED("Друкована"),
    ELECTRONIC("Електронна");

    private final String displayName;

    BookFormat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}