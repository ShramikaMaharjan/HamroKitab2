package io.virinchi.hamrokitab2.Repository;

import io.virinchi.hamrokitab2.Model.Order;
import io.virinchi.hamrokitab2.Model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {


    List<Order> findByBuyer(UserTbl buyer);

    List<Order> findBySeller(UserTbl seller);


}
