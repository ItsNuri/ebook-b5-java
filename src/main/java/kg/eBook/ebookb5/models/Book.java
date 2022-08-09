package kg.eBook.ebookb5.models;

import kg.eBook.ebookb5.enums.BookStatus;
import kg.eBook.ebookb5.enums.BookType;
import kg.eBook.ebookb5.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "book_gen", sequenceName = "book_seq", allocationSize = 1)
    private Long id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private int price;

    private String author;

    private int pageSize;

    private String publishingHouse;

    private String description;

    private Language language;

    private LocalDate publishedDate;

    private int yearOfIssue;

    private int quantityOfBook;

    private int discount;

    private boolean bestseller;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String mainImage;

    private String secondImage;

    private String thirdImage;

    private BookType bookType;

    private String fragment;

    private String audioBookFragment;

    private LocalTime duration;

    private String audioBook;

    private String electronicBook;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<User> likes;

    public void setUserToBook(User user) {
        this.likes.add(user);
    }

    private BookStatus bookStatus;

    private boolean isEnabled;

    @ManyToMany
    private List<User> bookBasket;

    private Boolean isNew;
}
