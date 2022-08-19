package kg.eBook.ebookb5.services;

import kg.eBook.ebookb5.dto.requests.UserRegisterRequest;
import kg.eBook.ebookb5.dto.responses.*;
import kg.eBook.ebookb5.enums.Role;
import kg.eBook.ebookb5.exceptions.AlreadyExistException;
import kg.eBook.ebookb5.exceptions.NotFoundException;
import kg.eBook.ebookb5.models.User;
import kg.eBook.ebookb5.repositories.BookRepository;
import kg.eBook.ebookb5.repositories.UserRepository;
import kg.eBook.ebookb5.security.JWT.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static kg.eBook.ebookb5.dto.responses.PurchasedUserBooksResponse.viewUserBooks;
import static kg.eBook.ebookb5.dto.responses.UserResponse.view;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final BookRepository bookRepository;

    public JwtResponse registerUser(UserRegisterRequest userRegisterRequest) {

        User person = new User(
                userRegisterRequest.getFirstName(),
                userRegisterRequest.getEmail()
        );
        person.setCreatedAt(LocalDate.now());
        person.setRole(Role.USER);
        person.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        if(personRepository.existsByEmail(userRegisterRequest.getEmail()))
            throw new AlreadyExistException("Эта почта " + userRegisterRequest.getEmail() + " уже занята!");

        User savedPerson = personRepository.save(person);
        String token = jwtUtil.generateToken(userRegisterRequest.getEmail());

        return new JwtResponse(
                savedPerson.getId(),
                token,
                savedPerson.getRole(),
                savedPerson.getFirstName()
        );
    }

    public List<UserResponse> findAllUsers() {
        return view(personRepository.findAllUsers());
    }

    public UserResponse findById(Long userId) {
        return new UserResponse(personRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найдено")
        ));
    }

    public SimpleResponse deleteByUserId(Long userId) {
        User user = personRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найдено")
        );

        // detach table users_basket_books
        user.getUserBasket().forEach(x -> x.removeUserFromBasket(user));
        bookRepository.saveAll(user.getUserBasket());

        // detach table users_favorite_books
        user.getFavorite().forEach(x -> x.removeUserFromLikes(user));
        bookRepository.saveAll(user.getFavorite());

        personRepository.delete(user);
        return new SimpleResponse("Пользователь успешно удален!");
    }

    public List<PurchasedUserBooksResponse> findAllUsersFavoriteBooks(Long userId) {
        User user = personRepository.findById(userId).get();
        return viewUserBooks(user.getFavorite());
    }

    public List<PurchasedUserBooksResponse> findAllUserBooksInBasket(Long userId) {
        User user = personRepository.findById(userId).get();
        return viewUserBooks(user.getUserBasket());
    }
}
