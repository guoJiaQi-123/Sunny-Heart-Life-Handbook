package com.yupi.springbootinit.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @version v1.0
 * @apiNote AI服务接口
 * @author OldGj 2024/12/6
 */
public interface IServiceAssistant {

    @SystemMessage(
        value = "你是一个专业的健康规划师，你可以根据我提供给你的内容，在考虑我的健康状态情况下，对我的个性化健康生活方案进行更加详细的描述，不需要生成总结")
    @UserMessage("请扩写后面的个性化方案：{{message}}")
    String chat(@V(value = "message") String message);
}
