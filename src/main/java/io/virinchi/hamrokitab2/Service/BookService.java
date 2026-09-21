package io.virinchi.hamrokitab2.Service;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.virinchi.hamrokitab2.Repository.MessageRepository;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MessageRepository messageRepository;

   
    // Save a book
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Delete book by ID
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    // Delete book by object
    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    // Get books by owner
    public List<Book> getBooksByOwner(UserTbl owner) {
        return bookRepository.findByOwner(owner);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> filterByCategory(String category) {
        return bookRepository.findByCategoryIgnoreCase(category);
    }

    public List<Book> filterBySemester(String semester) {
        return bookRepository.findBySemesterIgnoreCase(semester);
    }

    public List<Book> filterBySubject(String subject) {
        return bookRepository.findBySubjectContainingIgnoreCase(subject);
    }

    public List<Book> filterByInstitution(String institution) {
        return bookRepository.findByInstitutionContainingIgnoreCase(institution);
    }

    public List<Book> filterByCondition(String condition) {
        return bookRepository.findByConditionIgnoreCase(condition);
    }

    public List<Book> searchAndFilter(
            String title,
            String category,
            String semester,
            String condition) {

        return bookRepository.searchAndFilter(title, category, semester, condition);
    }

    public boolean hasMessages(Book book) {
        return messageRepository.existsByBook(book);
    }

}