
package io.virinchi.hamrokitab2.Repository;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.Message;
import io.virinchi.hamrokitab2.Model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByReceiver(UserTbl receiver);

    List<Message> findBySender(UserTbl sender);

    boolean existsByBook(Book book);

    // Messages from one user to another about a specific book
    List<Message> findByBookAndSenderAndReceiver(
            Book book,
            UserTbl sender,
            UserTbl receiver
    );
}

