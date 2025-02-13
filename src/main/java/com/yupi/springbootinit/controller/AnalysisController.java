package com.yupi.springbootinit.controller;


import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.vo.AnalysisResultVO;
import com.yupi.springbootinit.service.AnalysisResultService;
import com.yupi.springbootinit.service.IServiceAssistant;
import com.yupi.springbootinit.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/12/6
 * @apiNote 控制器
 */
@RestController
@RequestMapping("/chat")
public class AnalysisController {

    @Resource
    private AnalysisResultService analysisResultService;
    @Resource
    private UserService userService;
    @Resource
    private IServiceAssistant iServiceAssistant;


    /**
     * AI视觉理解
     * @param file 上传的图片
     * @param message 理解的方向
     */
    @PostMapping("/image")
    public BaseResponse<String> image(MultipartFile file, @RequestParam(required = false) String message, HttpServletRequest request) throws IOException {
        // 获得图片的base64编码
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求图片错误");
        }
        String analysisResult = analysisResultService.analysisFace(file, message, request);
        return ResultUtils.success(analysisResult);
    }

    @GetMapping("/history")
    public BaseResponse<List<AnalysisResultVO>> history(HttpServletRequest request) throws IOException {
        // 获得图片的base64编码
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        // 获取当前用户的所有分析记录
        List<AnalysisResultVO> historyAnalysisResult = analysisResultService.getHistoryAnalysisResult(loginUser);
        return ResultUtils.success(historyAnalysisResult);
    }

    @GetMapping("/individualized")
    public BaseResponse<String> individualized(HttpServletRequest request) {
        String individualizedDesc = analysisResultService.getIndividualizedDesc(request);
        return ResultUtils.success(individualizedDesc);
    }

    @GetMapping("/result")
    public BaseResponse<String> result(HttpServletRequest request) {
        String result = analysisResultService.getResultOne(request);
        return ResultUtils.success(result);
    }


    @GetMapping("/analysis")
    public BaseResponse<String> analysis(HttpServletRequest request) {
        String result = analysisResultService.getAnalysisOne(request);
        return ResultUtils.success(result);
    }
}

