package com.zhiwei.park.config;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnergyAIConfig {


    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Bean
    public ChatClient healthChatClient() {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("你是一个能效评估专家,专门用来评估企业的能效信息")// 配置系统提示词
                .build();
    }
}
