package xyz.hsong.oexam.service;

import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.Paper;
import xyz.hsong.oexam.pojo.PaperDetail;
import xyz.hsong.oexam.vo.QuestionWithScoreVo;

import java.util.List;
import java.util.Map;

public interface PaperService {

    ServerResponse addPaper(Paper paper, List<QuestionWithScoreVo> questions);

    ServerResponse updatePaper(Paper paper);

    ServerResponse deletePaper(Long paperId);

    ServerResponse searchPaper(Paper paper,Integer page,Integer pageSize);

    ServerResponse addQuestion(PaperDetail paperDetail);

    ServerResponse deleteQuestion(Long paperId, Long questionId);

    ServerResponse getPaperList(Integer page, Integer pageSize);

    ServerResponse getPaperQuestionList(Long paperId);

    ServerResponse getPaperDetail(Long paperId);

    ServerResponse getPaperCount(Paper paper);
}
