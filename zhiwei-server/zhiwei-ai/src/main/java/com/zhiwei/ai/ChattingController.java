package com.zhiwei.ai;

import com.zhiwei.common.utils.SecurityUtils;
import com.zhiwei.service.ChatHistoryService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stringtemplate.v4.ST;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class ChattingController {

    //注入ChatClient聊天客户端对象
    @Autowired
    @Qualifier("openai")
    private ChatClient chatClient;

    @Autowired
    private ChatHistoryService chatHistoryService;

   /* Map<String, List<Message>> messageCache;
      @PostMapping("/chat")
      public String chatting(String prompt, String chatId) {
          // 在调用大模型之前，查当前会话的历史消息列表
          List<Message> historyMessageList = messageCache.get(chatId);
          if (historyMessageList == null)
              historyMessageList = new ArrayList<>();

          // 添加用户问题消息到历史消息列表中
          Message userMessage = new UserMessage(prompt);
          historyMessageList.add(userMessage);

          // 组装历史消息列表
          Prompt targetprompt = new Prompt(historyMessageList);
          String llmResponse = chatClient.prompt(targetprompt)
                  .call()
                  .content();

          // 把本次大模型的回复消息添加到历史消息列表中
          Message assistantMessage = new AssistantMessage(llmResponse);
          historyMessageList.add(assistantMessage);
          messageCache.put(chatId, historyMessageList);
          return llmResponse;
      }*/

    @PostMapping("/chat")
    public Flux<String> chattingWithFlux(String prompt, String chatId) {

        // 每次聊天都保存会话ID
        chatHistoryService.saveHistory(SecurityUtils.getUserId().toString(), chatId);

        Flux<String> content = chatClient
                .prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
        return content;
    }


}
