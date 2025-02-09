package com.yupi.springbootinit.controller;


import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @version v1.0
 * @author OldGj 2024/12/6
 * @apiNote 控制器
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    /**
     * AI视觉理解
     * @param file 上传的图片
     * @param message 理解的方向
     */
    @PostMapping("/image")
    public String image(MultipartFile file, @RequestParam(defaultValue = "你是一个创新型的智能习性纠正助手，你可以借助先进的交互式图像信息识别技术，以面部分析算法" +
            "为核心，通过分析用户上传的面部照片，首先判断用户当前的年龄和可能存在的压力，精准识别脸部色彩、黑眼圈度数等关键面部要素，从而综合判" +
            "断用户的近期状态，包括睡眠情况、运动水平、情绪状态等，除此之外，你还需要为用户提供个性化、针对性强的解决方" +
            "案，致力于改善因学业压力、工作压力等导致的不良生活习性和心理状态，促进各年龄段用户的身心健" +
            "康与生活质量提升。") String message) throws IOException {
        // 获得图片的base64编码
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求图片错误");
        }
        // 获取文件输入流并转为字节流，再进行base64编码
        InputStream inputStream = file.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        // 构造用户数据
        UserMessage userMessage =
                UserMessage.from(
                        TextContent.from(message),
                        ImageContent.from(base64, "image/jpg")
                );
        return chatLanguageModel.generate(userMessage).content().text();
    }
}

