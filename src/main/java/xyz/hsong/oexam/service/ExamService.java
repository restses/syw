package xyz.hsong.oexam.service;

import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.Achievement;

import java.util.List;

public interface ExamService {

    ServerResponse savePaper(Long userId, Long paperId, List<Achievement> achievements);
    
    void correct(Long paperId);

    ServerResponse updateUserExamStatus(Long userId, Long paperId, Integer status);

    ServerResponse getUserExamStatus(Long paperId, Long userId);

    ServerResponse getExamScore(Long userId, Long paperId);

    ServerResponse getUserPaperList(Long userId, Integer page, Integer pageSize);

    ServerResponse getUserPaperDetail(Long userId, Long paperId);

    ServerResponse getUserPaperListCount(Long userId);

    ServerResponse getUserSubmitPaperDetail(Long userId,Long paperId);

    ServerResponse getExamUserList(Long paperId);

    ServerResponse getExamScoreList(Long paperId);
}
