package com.chat.Services;

import com.chat.Controllers.Response;
import com.chat.LogManager.LogUtils;
import com.chat.Models.Conversation;
import com.chat.Models.Message;
import com.chat.PropertyManager.DatabaseSupplier;
import com.chat.Repository.IMessageDAO;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class MessageService {
  private IMessageDAO messageDao;

  @Autowired
  MessageService(
    @Qualifier(DatabaseSupplier.MongoDB.Chat.Message) IMessageDAO messageIml
  ) {
    this.messageDao = messageIml;
  }

  public Optional<Object> AddMessage(Message message) {
    try {
      ZoneId zoneId = ZoneId.systemDefault();

      message.setId(UUID.randomUUID());
      message.setUnixTime(
        String.valueOf(LocalDateTime.now().atZone(zoneId).toEpochSecond())
      );
      messageDao.InsertMessage(message);

      return Optional.of(true);
      //
    } catch (Exception e) {
      RuntimeException exception = new RuntimeException(e);

      LogUtils.LogError("[ERROR]", exception);

      return Optional.empty();
    }
  }

  /**
   * Get 15 messages at a time
   * @param {Conversation} conversation
   * @param {int} index (index of a collection, collection start at 0)
   * @return List<Message>
   */
  public Optional<List<Message>> GetMessageByIndex(
    Conversation conversation,
    int index
  ) {
    final int messageCountEach = 15;
    try {
      FindIterable<Document> cursor = messageDao.GetMessage(conversation);
      List<Message> messages = new ArrayList<>();

      for (Document doc : cursor) {
        messages.add(new Gson().fromJson(doc.toJson(), Message.class));
      }

      if (index > messages.size()) {
        return Optional.of(new ArrayList<Message>());
        //
      } else {
        if (index + messageCountEach >= messages.size()) {
          return Optional.of(messages.subList(index, messages.size() - 1));
          //
        } else {
          return Optional.of(messages.subList(index, index + messageCountEach));
        }
      }
      //
    } catch (Exception e) {
      RuntimeException exception = new RuntimeException(e);

      LogUtils.LogError("[ERROR]", exception);

      return Optional.empty();
    }
  }
}
