
package io.virinchi.hamrokitab2.Service;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.Message;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    // ================= SAVE MESSAGE =================

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }


    // ================= RECEIVED MESSAGES =================

    public List<Message> getReceivedMessages(UserTbl user) {

        return messageRepository.findByReceiver(user);
    }


    // ================= SENT MESSAGES =================

    public List<Message> getSentMessages(UserTbl user) {

        return messageRepository.findBySender(user);
    }


    // ================= GET MESSAGE BY ID =================

    public Message getMessageById(int id) {

        return messageRepository.findById(id).orElse(null);
    }


    // ================= GET CONVERSATION =================

    public List<Message> getConversation(
            Book book,
            UserTbl user1,
            UserTbl user2) {

        List<Message> messages = new ArrayList<>();

        // Messages from user1 to user2
        messages.addAll(
                messageRepository.findByBookAndSenderAndReceiver(
                        book,
                        user1,
                        user2
                )
        );

        // Messages from user2 to user1
        messages.addAll(
                messageRepository.findByBookAndSenderAndReceiver(
                        book,
                        user2,
                        user1
                )
        );

        // Arrange messages from oldest to newest
        messages.sort(
                Comparator.comparing(Message::getId)
        );

        return messages;
    }
}

