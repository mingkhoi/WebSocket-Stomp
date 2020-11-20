package com.dao;

import com.model.Conversation;
import java.util.List;

public interface IConversationDao {
  public List<Conversation> getAllConversation() throws Exception;

  public void insertConversation(Conversation data) throws Exception;

  public void deleteConversation(String _id) throws Exception;

  public List<Conversation> getConversation(String senderId) throws Exception;

  public List<Conversation> getConversationById(String _id) throws Exception;
}