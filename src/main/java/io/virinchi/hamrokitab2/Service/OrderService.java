package io.virinchi.hamrokitab2.Service;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.Order;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    // ================= PLACE ORDER =================

    public Order placeOrder(
            Book book,
            UserTbl buyer,
            UserTbl seller,
            String buyerName,
            String phoneNumber,
            String province,
            String city,
            String deliveryAddress,
            String deliveryNote) {


        // Validate basic order details

        if (book == null || buyer == null || seller == null) {
            throw new IllegalArgumentException(
                    "Invalid order details."
            );
        }


        // Prevent ordering own book

        if (buyer.getId() == seller.getId()) {
            throw new IllegalArgumentException(
                    "You cannot order your own book."
            );
        }


        // Validate buyer name

        if (buyerName == null ||
                buyerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Buyer name is required."
            );
        }


        // Validate phone number

        if (phoneNumber == null ||
                phoneNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Phone number is required."
            );
        }


        // Validate province

        if (province == null ||
                province.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Province is required."
            );
        }


        // Validate city

        if (city == null ||
                city.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "City is required."
            );
        }


        // Validate delivery address

        if (deliveryAddress == null ||
                deliveryAddress.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Delivery address is required."
            );
        }


        // Create order

        Order order = new Order();

        order.setBook(book);
        order.setBuyer(buyer);
        order.setSeller(seller);

        order.setPrice(book.getPrice());

        order.setBuyerName(buyerName);
        order.setPhoneNumber(phoneNumber);

        order.setProvince(province);
        order.setCity(city);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryNote(deliveryNote);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");


        return orderRepository.save(order);
    }


    // ================= GET BUYER ORDERS =================

    public List<Order> getBuyerOrders(UserTbl buyer) {

        return orderRepository.findByBuyer(buyer);
    }


    // ================= GET SELLER ORDERS =================

    public List<Order> getSellerOrders(UserTbl seller) {

        return orderRepository.findBySeller(seller);
    }


    // ================= GET ORDER BY ID =================

    public Order getOrderById(int id) {

        return orderRepository.findById(id).orElse(null);
    }


    // ================= ACCEPT ORDER =================

    public boolean acceptOrder(int orderId, UserTbl seller) {

        Order order = getOrderById(orderId);

        if (order == null || seller == null) {
            return false;
        }

        if (order.getSeller().getId() != seller.getId()) {
            return false;
        }

        if (!"PENDING".equals(order.getStatus())) {
            return false;
        }

        order.setStatus("ACCEPTED");

        orderRepository.save(order);

        return true;
    }


    // ================= MARK ON THE WAY =================

    public boolean markOnTheWay(int orderId, UserTbl seller) {

        Order order = getOrderById(orderId);

        if (order == null || seller == null) {
            return false;
        }

        if (order.getSeller().getId() != seller.getId()) {
            return false;
        }

        if (!"ACCEPTED".equals(order.getStatus())) {
            return false;
        }

        order.setStatus("ON_THE_WAY");

        orderRepository.save(order);

        return true;
    }


    // ================= MARK DELIVERED =================

    public boolean markDelivered(int orderId, UserTbl seller) {

        Order order = getOrderById(orderId);

        if (order == null || seller == null) {
            return false;
        }

        if (order.getSeller().getId() != seller.getId()) {
            return false;
        }

        if (!"ON_THE_WAY".equals(order.getStatus())) {
            return false;
        }

        order.setStatus("DELIVERED");

        orderRepository.save(order);

        return true;
    }


    // ================= CANCEL ORDER =================

    public boolean cancelOrder(int orderId, UserTbl buyer) {

        Order order = getOrderById(orderId);

        if (order == null || buyer == null) {
            return false;
        }

        if (order.getBuyer().getId() != buyer.getId()) {
            return false;
        }

        if (!"PENDING".equals(order.getStatus())) {
            return false;
        }

        order.setStatus("CANCELLED");

        orderRepository.save(order);

        return true;
    }
}