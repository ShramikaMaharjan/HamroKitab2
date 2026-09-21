package io.virinchi.hamrokitab2.Service;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Model.Wishlist;
import io.virinchi.hamrokitab2.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;


@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public List<Wishlist> getUserWishlist(UserTbl user) {
        return wishlistRepository.findByUser(user);
    }

    public boolean addToWishlist(UserTbl user, Book book) {

        if (wishlistRepository.existsByUserAndBook(user, book)) {
            return false;
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);
        wishlist.setBook(book);

        wishlistRepository.save(wishlist);

        return true;
    }

    @Transactional
    public void removeFromWishlist(UserTbl user, Book book) {
        wishlistRepository.deleteByUserAndBook(user, book);
    }

    public boolean isInWishlist(UserTbl user, Book book) {
        return wishlistRepository.existsByUserAndBook(user, book);
    }
}


