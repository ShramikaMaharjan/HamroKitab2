package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.Message;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Service.BookService;
import io.virinchi.hamrokitab2.Service.MessageService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MessageService messageService;


    // ================= CONTACT SELLER =================

    @GetMapping("/contactSeller/{id}")
    public String contactSeller(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        // Save book ID before login
        if (user == null) {
            session.setAttribute("pendingMessageBookId", id);
            return "redirect:/login";
        }

        Book book = bookService.getBookById(id);

        if (book == null) {
            return "redirect:/product";
        }

        // Prevent contacting yourself
        if (book.getOwner().getId() == user.getId()) {
            return "redirect:/product";
        }

        model.addAttribute("book", book);

        return "contactSeller";
    }


    // ================= CONTINUE AFTER LOGIN =================

    @GetMapping("/continueContactSeller")
    public String continueContactSeller(
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Integer bookId =
                (Integer) session.getAttribute("pendingMessageBookId");

        if (bookId == null) {
            return "redirect:/product";
        }

        Book book = bookService.getBookById(bookId);

        if (book == null) {
            session.removeAttribute("pendingMessageBookId");
            return "redirect:/product";
        }

        if (book.getOwner().getId() == user.getId()) {
            session.removeAttribute("pendingMessageBookId");
            return "redirect:/product";
        }

        session.removeAttribute("pendingMessageBookId");

        model.addAttribute("book", book);

        return "contactSeller";
    }


    // ================= SEND MESSAGE =================

    @PostMapping("/contactSeller")
    public String sendMessage(
            @RequestParam("bookId") int bookId,
            @RequestParam("message") String messageText,
            HttpSession session) {

        UserTbl sender =
                (UserTbl) session.getAttribute("loggedInUser");

        if (sender == null) {
            session.setAttribute("pendingMessageBookId", bookId);
            return "redirect:/login";
        }

        Book book = bookService.getBookById(bookId);

        if (book == null) {
            return "redirect:/product";
        }

        // Prevent sending message to yourself
        if (book.getOwner().getId() == sender.getId()) {
            return "redirect:/product";
        }

        if (messageText == null || messageText.trim().isEmpty()) {
            return "redirect:/contactSeller/" + bookId;
        }

        Message message = new Message();

        message.setMessage(messageText);
        message.setSender(sender);
        message.setReceiver(book.getOwner());
        message.setBook(book);

        messageService.saveMessage(message);

        return "redirect:/messages";
    }


    // ================= INBOX =================

    @GetMapping("/messages")
    public String messages(
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<Message> messages =
                messageService.getReceivedMessages(user);

        model.addAttribute("messages", messages);

        return "messages";
    }


    // ================= SENT MESSAGES =================

    @GetMapping("/sentMessages")
    public String sentMessages(
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<Message> messages =
                messageService.getSentMessages(user);

        model.addAttribute("messages", messages);

        return "sent-messages";
    }

    // ================= REPLY FORM =================

    @GetMapping("/reply/{id}")
    public String replyForm(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Message originalMessage =
                messageService.getMessageById(id);

        if (originalMessage == null) {
            return "redirect:/messages";
        }

        // Only the receiver of the message can reply
        if (originalMessage.getReceiver().getId() != user.getId()) {
            return "redirect:/messages";
        }

        model.addAttribute("originalMessage", originalMessage);

        return "reply";
    }


// ================= SEND REPLY =================

    @PostMapping("/sendReply")
    public String sendReply(
            @RequestParam("messageId") int messageId,
            @RequestParam("message") String messageText,
            HttpSession session) {

        UserTbl sender =
                (UserTbl) session.getAttribute("loggedInUser");

        if (sender == null) {
            return "redirect:/login";
        }

        Message originalMessage =
                messageService.getMessageById(messageId);

        if (originalMessage == null) {
            return "redirect:/messages";
        }

        // Only the receiver can reply
        if (originalMessage.getReceiver().getId() != sender.getId()) {
            return "redirect:/messages";
        }

        if (messageText == null || messageText.trim().isEmpty()) {
            return "redirect:/reply/" + messageId;
        }

        Message reply = new Message();

        reply.setMessage(messageText.trim());

        // Current logged-in user sends the reply
        reply.setSender(sender);

        // Reply goes to the original sender
        reply.setReceiver(originalMessage.getSender());

        // Reply remains connected to the same book
        reply.setBook(originalMessage.getBook());

        messageService.saveMessage(reply);

        return "redirect:/messages";
    }

    
// ================= CONVERSATION =================

    @GetMapping("/conversation/{id}")
    public String conversation(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        // Get the message that was clicked
        Message selectedMessage =
                messageService.getMessageById(id);

        if (selectedMessage == null) {
            return "redirect:/messages";
        }

        // Make sure the logged-in user is part of this conversation
        boolean isSender =
                selectedMessage.getSender().getId() == user.getId();

        boolean isReceiver =
                selectedMessage.getReceiver().getId() == user.getId();

        if (!isSender && !isReceiver) {
            return "redirect:/messages";
        }

        // Find the other person
        UserTbl otherUser;

        if (isSender) {
            otherUser = selectedMessage.getReceiver();
        } else {
            otherUser = selectedMessage.getSender();
        }

        Book book = selectedMessage.getBook();

        // Get all messages between both users about this book
        List<Message> conversation =
                messageService.getConversation(
                        book,
                        user,
                        otherUser
                );

        model.addAttribute("messages", conversation);
        model.addAttribute("book", book);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("currentUser", user);

        return "conversation";
    }


}