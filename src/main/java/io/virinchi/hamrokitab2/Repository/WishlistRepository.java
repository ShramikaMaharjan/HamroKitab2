package io.virinchi.hamrokitab2.Repository;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {


    List<Wishlist> findByUser(UserTbl user);

    Optional<Wishlist> findByUserAndBook(UserTbl user, Book book);

    boolean existsByUserAndBook(UserTbl user, Book book);

    void deleteByUserAndBook(UserTbl user, Book book);


}
