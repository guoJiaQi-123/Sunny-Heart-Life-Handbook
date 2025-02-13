package com.yupi.springbootinit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Knife4j 接口文档配置
 * https://doc.xiaominfo.com/knife4j/documentation/get_start.html
 *
 * @author 郭家旗
 * @from
 */
@Configuration
@Profile({"dev", "test"})
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("晴心生活助手")
                .version("1.0")
                .description(
                    "晴心生活助手，它集合了丰富多样的功能模块代码。包括精准的人脸识别技术代码，用于用户身份识别与认证；全面的健康追踪代码，涵盖生理数据监测与分析；便捷的购物功能代码，满足用户的消费需求；详细的健康报告生成代码，为用户提供健康状况总结；实用的健康打卡代码，帮助用户养成良好的健康习惯；以及丰富的健康手册相关代码，提供专业的健康知识。整个仓库的代码结构清晰，功能完善，致力于为用户打造一个全方位、个性化的生活服务平台。")
                .termsOfService("https://test.com")
                .contact(new Contact().name("Hmi").url("https://test.com").email("test@gmail.com"))
            );
    }
}