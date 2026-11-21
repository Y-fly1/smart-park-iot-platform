package com.zhiwei.config;

import com.zhiwei.constants.Constants;
import com.zhiwei.tool.InspectionItemTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

/*
    聊天的客户端有很多配置不适合在yml中
    比如:
        1.系统提示词很长
        2.模型的对接方式, 到底是本地的,还是openAI的, 还是其他方法
            对接不同的方式需要创建不同的对象才行!!!!!!!!
       ...
    综合以上因素, 这个客户端必须由我们自己创建而且还需要放入ioc容器中
 */
@Configuration // 标注这是一个配置类
public class ChatClientConfig {

    //注入OpenAi读取配置文件信息内容整合模型参数对象
    @Autowired
    private OpenAiChatModel openAiChatModel;

//    @Autowired
//    private OllamaChatModel ollamaChatModel;

    @Autowired
    private InspectionItemTool inspectionItemTool;

    @Autowired
    private ChatMemory chatMemory;

    /** 向量库地址（与业务 Redis 可分离部署，需 Redis Stack 支持 RediSearch） */
    @Value("${vector-store.host:localhost}")
    private String vectorStoreHost;

    /** 向量库端口 */
    @Value("${vector-store.port:6379}")
    private int vectorStorePort;

    /** 向量索引名称 */
    @Value("${vector-store.index-name:spring-ai-index}")
    private String vectorStoreIndexName;

    /** 是否在启动时自动创建向量索引 */
    @Value("${vector-store.initialize-schema:true}")
    private boolean initializeVectorSchema;

    @Bean("openai")
    public ChatClient openAIChatClient(ChatMemory chatMemory,
                                       VectorStore vectorStore) {
        // 构建聊天客户端, 必须有模型的参数, 才能构建, 还可以设置其他的重要的参数
        return
                ChatClient.builder(openAiChatModel)
                        .defaultSystem(Constants.PROMPT) // 配置系统提示词
                        .defaultTools(inspectionItemTool)// 配置技能
                        .defaultAdvisors(new SimpleLoggerAdvisor(),
                                MessageChatMemoryAdvisor.builder(chatMemory)
                                        .build(),
                                QuestionAnswerAdvisor.builder(vectorStore)
                                        .searchRequest(SearchRequest.builder()
                                                .similarityThreshold(0.8)
                                                .topK(1)
                                                .build())
                                        .build())  //添加默认的advisor 记录日志
                        .build();
    }

//    @Bean("ollamaai")
//    public ChatClient ollamaChatClient() {
//        // 构建聊天客户端, 必须有模型的参数, 才能构建, 还可以设置其他的重要的参数
//        return
//                ChatClient.builder(ollamaChatModel)
//                        .defaultSystem(Constants.PROMPT)// 配置系统提示词
//                        .defaultTools(inspectionItemTool)// 配置技能
//                        .build();
//    }
    @Bean
    public VectorStore redisVectorStore(OpenAiEmbeddingModel openAiEmbeddingModel) {
        // 向量库支持独立部署，未单独配置时复用业务 Redis 地址
        JedisPooled jedisPooled = new JedisPooled(vectorStoreHost, vectorStorePort);
        return RedisVectorStore.builder(jedisPooled, openAiEmbeddingModel)
                .indexName(vectorStoreIndexName) //向量索引名称
                .prefix("doc:") //向量数据前缀
                .initializeSchema(initializeVectorSchema) //应用启动自动创建索引，首次启动true；生产关闭自动建索引false
                .build();
    }
}