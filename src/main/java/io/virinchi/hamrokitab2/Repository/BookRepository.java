package io.virinchi.hamrokitab2.Repository;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByOwner(UserTbl owner);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByCategoryIgnoreCase(String category);

    List<Book> findBySemesterIgnoreCase(String semester);

    List<Book> findBySubjectContainingIgnoreCase(String subject);

    List<Book> findByInstitutionContainingIgnoreCase(String institution);

    List<Book> findByConditionIgnoreCase(String condition);

    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR :title = '' OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:category IS NULL OR :category = '' OR LOWER(b.category) = LOWER(:category)) AND " +
            "(:semester IS NULL OR :semester = '' OR LOWER(b.semester) = LOWER(:semester)) AND " +
            "(:condition IS NULL OR :condition = '' OR LOWER(b.condition) = LOWER(:condition))")
    List<Book> searchAndFilter(
            @Param("title") String title,
            @Param("category") String category,
            @Param("semester") String semester,
            @Param("condition") String condition
    );

    
}