package io.virinchi.hamrokitab2.Repository;

import io.virinchi.hamrokitab2.Model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findAllByOrderByIdDesc();
}