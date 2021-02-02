package library.books;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String genres;
    private boolean available;
    private String reservedBy;

    public Book(String title, String author, String genres, boolean available, String reservedBy) {
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.available = available;
        this.reservedBy = reservedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public String getGenres() {

        return genres;
    }

    public void setGenres(String genres) {

        this.genres = genres;
    }

    public boolean isAvailable() {

        return available;
    }

    public void setAvailable(boolean available) {

        this.available = available;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }

    @Override
    public String toString() {
        return "Book: " +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genres='" + genres + '\'' +
                ", available=" + available + '.';
    }
}
