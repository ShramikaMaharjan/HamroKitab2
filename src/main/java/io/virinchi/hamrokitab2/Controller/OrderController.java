package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.Order;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.BookRepository;
import io.virinchi.hamrokitab2.Service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookRepository bookRepository;


    // ================= SHOW ORDER FORM =================

    @GetMapping("/order/{id}")
    public String showOrderForm(
            @PathVariable int id,
            Model model,
            HttpSession session) {

        UserTbl buyer =
                (UserTbl) session.getAttribute("loggedInUser");

        if (buyer == null) {
            return "redirect:/login";
        }

        Book book =
                bookRepository.findById(id).orElse(null);

        if (book == null || book.getOwner() == null) {
            return "redirect:/product";
        }

        if (buyer.getId() == book.getOwner().getId()) {
            return "redirect:/product";
        }

        model.addAttribute("book", book);

        return "order";
    }


    // ================= PLACE ORDER =================

    @PostMapping("/placeOrder")
    public String placeOrder(
            @RequestParam int bookId,
            @RequestParam String buyerName,
            @RequestParam String phoneNumber,
            @RequestParam String province,
            @RequestParam String city,
            @RequestParam String deliveryAddress,
            @RequestParam(required = false) String deliveryNote,
            HttpSession session,
            Model model) {

        UserTbl buyer =
                (UserTbl) session.getAttribute("loggedInUser");


        // If user is not logged in, temporarily save form data

        if (buyer == null) {

            session.setAttribute("pendingBookId", bookId);
            session.setAttribute("pendingBuyerName", buyerName);
            session.setAttribute("pendingPhoneNumber", phoneNumber);
            session.setAttribute("pendingProvince", province);
            session.setAttribute("pendingCity", city);
            session.setAttribute(
                    "pendingDeliveryAddress",
                    deliveryAddress
            );
            session.setAttribute(
                    "pendingDeliveryNote",
                    deliveryNote
            );

            return "redirect:/login";
        }


        Book book =
                bookRepository.findById(bookId).orElse(null);

        if (book == null || book.getOwner() == null) {
            return "redirect:/product";
        }

        UserTbl seller = book.getOwner();


        // Prevent users from ordering their own books

        if (buyer.getId() == seller.getId()) {

            model.addAttribute("book", book);
            model.addAttribute(
                    "error",
                    "You cannot order your own book."
            );

            return "order";
        }


        try {

            Order savedOrder = orderService.placeOrder(
                    book,
                    buyer,
                    seller,
                    buyerName,
                    phoneNumber,
                    province,
                    city,
                    deliveryAddress,
                    deliveryNote
            );


            // Remove temporary checkout data

            session.removeAttribute("pendingBookId");
            session.removeAttribute("pendingBuyerName");
            session.removeAttribute("pendingPhoneNumber");
            session.removeAttribute("pendingProvince");
            session.removeAttribute("pendingCity");
            session.removeAttribute("pendingDeliveryAddress");
            session.removeAttribute("pendingDeliveryNote");


            return "redirect:/order-confirmation/"
                    + savedOrder.getId();

        } catch (IllegalArgumentException e) {

            model.addAttribute("book", book);
            model.addAttribute("error", e.getMessage());

            model.addAttribute("buyerName", buyerName);
            model.addAttribute("phoneNumber", phoneNumber);
            model.addAttribute("province", province);
            model.addAttribute("city", city);
            model.addAttribute("deliveryAddress", deliveryAddress);
            model.addAttribute("deliveryNote", deliveryNote);

            return "order";
        }
    }


    // ================= MY ORDERS =================

    @GetMapping("/myOrders")
    public String myOrders(
            HttpSession session,
            Model model) {

        UserTbl buyer =
                (UserTbl) session.getAttribute("loggedInUser");

        if (buyer == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "orders",
                orderService.getBuyerOrders(buyer)
        );

        return "my-orders";
    }


    // ================= SELLER ORDERS =================

    // Both URLs are supported for compatibility

    @GetMapping({"/sellerOrders", "/sellerorders"})
    public String sellerOrders(
            HttpSession session,
            Model model) {

        UserTbl seller =
                (UserTbl) session.getAttribute("loggedInUser");

        if (seller == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "orders",
                orderService.getSellerOrders(seller)
        );

        return "sellerorders";
    }


    // ================= ACCEPT ORDER =================

    @PostMapping("/order/accept/{id}")
    public String acceptOrder(
            @PathVariable int id,
            HttpSession session) {

        UserTbl seller =
                (UserTbl) session.getAttribute("loggedInUser");

        if (seller == null) {
            return "redirect:/login";
        }

        orderService.acceptOrder(id, seller);

        return "redirect:/sellerOrders";
    }


    // ================= ON THE WAY =================

    @PostMapping("/order/onTheWay/{id}")
    public String onTheWay(
            @PathVariable int id,
            HttpSession session) {

        UserTbl seller =
                (UserTbl) session.getAttribute("loggedInUser");

        if (seller == null) {
            return "redirect:/login";
        }

        orderService.markOnTheWay(id, seller);

        return "redirect:/sellerOrders";
    }


    // ================= DELIVERED =================

    @PostMapping("/order/delivered/{id}")
    public String delivered(
            @PathVariable int id,
            HttpSession session) {

        UserTbl seller =
                (UserTbl) session.getAttribute("loggedInUser");

        if (seller == null) {
            return "redirect:/login";
        }

        orderService.markDelivered(id, seller);

        return "redirect:/sellerOrders";
    }


    // ================= CANCEL ORDER =================

    @PostMapping("/order/cancel/{id}")
    public String cancelOrder(
            @PathVariable int id,
            HttpSession session) {

        UserTbl buyer =
                (UserTbl) session.getAttribute("loggedInUser");

        if (buyer == null) {
            return "redirect:/login";
        }

        orderService.cancelOrder(id, buyer);

        return "redirect:/myOrders";
    }


    // ================= ORDER CONFIRMATION =================

    @GetMapping("/order-confirmation/{id}")
    public String orderConfirmation(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        UserTbl buyer =
                (UserTbl) session.getAttribute("loggedInUser");

        if (buyer == null) {
            return "redirect:/login";
        }

        Order order =
                orderService.getOrderById(id);

        if (order == null) {
            return "redirect:/myOrders";
        }

        if (order.getBuyer().getId() != buyer.getId()) {
            return "redirect:/myOrders";
        }

        model.addAttribute("order", order);

        return "order-confirmation";
    }


    // ================= CONTINUE ORDER =================

    @GetMapping("/continueOrder")
    public String continueOrder(
            HttpSession session,
            Model model) {

        UserTbl buyer =
                (UserTbl) session.getAttribute("loggedInUser");

        if (buyer == null) {
            return "redirect:/login";
        }


        Integer bookId =
                (Integer) session.getAttribute("pendingBookId");

        String buyerName =
                (String) session.getAttribute("pendingBuyerName");

        String phoneNumber =
                (String) session.getAttribute("pendingPhoneNumber");

        String province =
                (String) session.getAttribute("pendingProvince");

        String city =
                (String) session.getAttribute("pendingCity");

        String deliveryAddress =
                (String) session.getAttribute(
                        "pendingDeliveryAddress"
                );

        String deliveryNote =
                (String) session.getAttribute(
                        "pendingDeliveryNote"
                );


        if (bookId == null) {
            return "redirect:/product";
        }

        Book book =
                bookRepository.findById(bookId).orElse(null);

        if (book == null || book.getOwner() == null) {
            return "redirect:/product";
        }

        if (buyer.getId() == book.getOwner().getId()) {
            return "redirect:/product";
        }


        model.addAttribute("book", book);
        model.addAttribute("buyerName", buyerName);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("province", province);
        model.addAttribute("city", city);
        model.addAttribute("deliveryAddress", deliveryAddress);
        model.addAttribute("deliveryNote", deliveryNote);

        return "order";
    }
}