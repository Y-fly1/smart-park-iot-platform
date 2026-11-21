package com.zhiwei;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootTest
public class ETLTest {

    @Autowired
    private VectorStore vectorStore;

    @Test
    public void testTestSplitter() throws FileNotFoundException {
        // 读取文件
        InputStreamResource resource = new InputStreamResource(new FileInputStream("D:\\巡检员工工作手册.pdf"));

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0) // 设置页眉边距
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0) // 删除页眉顶部的文本行数
                                .build())
                        .withPagesPerDocument(1) // 每个文档的页数
                        .build());

        // 只读取一次，存到变量
        List<Document> originDocs = pdfReader.read();
        System.out.println("分隔之前的文档数：" + originDocs.size());

        // 创建TextSplitter
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> documents = textSplitter.apply(originDocs);

        System.out.println("分隔之后的文档数：" + documents.size());
        // 打印每个分片内容，方便调试
        for (Document doc : documents) {
            System.out.println("====分片====");
            System.out.println(doc.getText());
            System.out.println(doc.getMetadata());
        }

        //存储到向量数据库中
        vectorStore.add(documents );
    }


    @Test
    public void retrieval(){
        List<Document> documents = vectorStore.similaritySearch(SearchRequest
                .builder()
                .query("巡检服务宗旨与核心价值是什么")
                .similarityThreshold(0.5)
                .topK(1)
                .build());
        documents.forEach(System.out::println);
    }
}
