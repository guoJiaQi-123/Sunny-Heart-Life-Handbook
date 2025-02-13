package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.entity.AnalysisResult;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.vo.AnalysisResultVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author HX
 * @description 针对表【analysis_result(分析)】的数据库操作Service
 * @createDate 2025-02-10 17:12:53
 */
public interface AnalysisResultService extends IService<AnalysisResult> {

    String analysisFace(MultipartFile file, String message, HttpServletRequest request) throws IOException;

    List<AnalysisResultVO> getHistoryAnalysisResult(User user);

    String getIndividualizedDesc(HttpServletRequest request);

    String getResultOne(HttpServletRequest request);

    String getAnalysisOne(HttpServletRequest request);

}
