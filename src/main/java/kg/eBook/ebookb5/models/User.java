package kg.eBook.ebookb5.models;

import kg.eBook.ebookb5.enums.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    @OneToMany
    private List<Book> books = new ArrayList();

    private Roles role;

    @OneToMany
    private List<PromoCode> promoCodes = new ArrayList<>();

    @OneToMany
    private List<Book> basket = new ArrayList<>();

    @OneToMany
    private List<Book> favorite = new ArrayList<>();

}

