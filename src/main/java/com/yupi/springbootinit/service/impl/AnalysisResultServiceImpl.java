package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.AnalysisResultMapper;
import com.yupi.springbootinit.model.entity.AnalysisResult;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.vo.AnalysisResultVO;
import com.yupi.springbootinit.service.AnalysisResultService;
import com.yupi.springbootinit.service.IServiceAssistant;
import com.yupi.springbootinit.service.UserService;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author HX
 * @description 针对表【analysis_result(分析)】的数据库操作Service实现
 * @createDate 2025-02-10 17:12:53
 */
@Service
public class AnalysisResultServiceImpl extends ServiceImpl<AnalysisResultMapper, AnalysisResult> implements AnalysisResultService {

    private static final String promote = "一、角色设定\n"
            + "你是一个创新型的智能习性纠正助手，具备先进的交互式图像信息识别技术，尤其擅长面部分析算法。\n"
            + "二、功能介绍\n"
            + "面部要素分析：当用户上传面部照片时，你能精准识别脸部色彩、黑眼圈度数等关键面部要素。\n"
            + "状态判断：通过面部要素，综合判断用户的睡眠情况、运动水平、情绪状态等近期状态。\n"
            + "个性化方案提供：\n"
            + "生活习惯改善：依据用户年龄、性别、职业、生活环境等，制定详细饮食计划、作息时间和日常行为建议。\n"
            + "运动建议：结合用户身体状况、运动目标和兴趣爱好，推荐合适运动项目、运动强度，给出运动指导和注意事项。\n"
            + "心理调节：借助专业心理专家团队，根据用户情绪状态和心理压力水平，提供个性化心理疏导方法和情绪调节技巧。\n"
            + "三、目标用户与问题解决\n"
            + "针对因学业压力、工作压力等导致不良生活习性和心理状态的各年龄段用户，致力于促进其身心健康与生活质量提升。\n"
            + "四、格式要求\n"
            + "必须进行分析！不要输出功能介绍以外的无关内容，功能介绍的三个标题使用### 格式，不要输出总结，内容尽可能丰富深刻";

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @Autowired
    private UserService userService;

    @Autowired
    private AnalysisResultMapper analysisResultMapper;

    @Autowired
    private IServiceAssistant iServiceAssistant;

    @Override
    public String analysisFace(MultipartFile file, String message, HttpServletRequest request) throws IOException {
        // 获取文件输入流并转为字节流，再进行base64编码
        InputStream inputStream = file.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        // 构造用户数据
        if (message == null || message.isEmpty()) {
            message = promote;
        }
        // 构造发给大模型的请求
        UserMessage userMessage = UserMessage.from(TextContent.from(message), ImageContent.from(base64, "image/jpg"));
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 构造分析结果
        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.setStatus(0);
        analysisResult.setUserId(loginUser.getId());
        // 保存分析记录到数据库
        boolean saveResult = this.save(analysisResult);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "分析失败，数据库错误");
        }
        // 发送请求，等待大模型返回响应
        String allContent = chatLanguageModel.generate(userMessage).content().text();
        analysisResult.setAll_content(allContent);
        List<String> strings = splitAllText(allContent);
        analysisResult.setFacial_analysis(strings.getFirst());
        analysisResult.setStatus_judgment(strings.get(1));
        analysisResult.setIndividualized(strings.get(2));
        analysisResult.setStatus(1);
        analysisResultMapper.updateById(analysisResult);
        return analysisResult.getAll_content();
    }

    @Override
    public List<AnalysisResultVO> getHistoryAnalysisResult(User user) {
        QueryWrapper<AnalysisResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", user.getId());
        queryWrapper.eq("isDelete", 0);
        queryWrapper.eq("status", 1);
        List<AnalysisResult> analysisResults = analysisResultMapper.selectList(queryWrapper);
        List<AnalysisResultVO> list = new ArrayList<>();
        analysisResults.forEach(x -> list.add(AnalysisResultVO.objToVo(x)));
        return list;
    }

    @Override
    public String getIndividualizedDesc(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        QueryWrapper<AnalysisResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.eq("isDelete", 0);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("createTime");
        queryWrapper.last("LIMIT 1");
        AnalysisResult analysisResult = analysisResultMapper.selectOne(queryWrapper);
        if (analysisResult.getIndividualizedDesc() != null) {
            return analysisResult.getIndividualizedDesc();
        }
        String result = iServiceAssistant.chat(analysisResult.getIndividualized());
        analysisResult.setIndividualizedDesc(result);
        analysisResultMapper.updateById(analysisResult);
        return result;
    }

    @Override
    public String getResultOne(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        QueryWrapper<AnalysisResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.eq("isDelete", 0);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("createTime");
        queryWrapper.last("LIMIT 1");
        AnalysisResult analysisResult = analysisResultMapper.selectOne(queryWrapper);
        if (analysisResult.getStatus_judgment() != null) {
            return analysisResult.getStatus_judgment();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public String getAnalysisOne(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        QueryWrapper<AnalysisResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.eq("isDelete", 0);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("createTime");
        queryWrapper.last("LIMIT 1");
        AnalysisResult analysisResult = analysisResultMapper.selectOne(queryWrapper);
        if (analysisResult.getFacial_analysis() != null) {
            return analysisResult.getFacial_analysis();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    private List<String> splitAllText(String allText) {
        // 使用正则表达式拆分文本，匹配以 ### 开头且后面紧跟一个空格的行
        String[] parts = allText.split("(?m)^### ");
        // 过滤掉空白字符串
        List<String> validParts = new ArrayList<>();
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                validParts.add("### " + part);
            }
        }
        return validParts;
    }
}




