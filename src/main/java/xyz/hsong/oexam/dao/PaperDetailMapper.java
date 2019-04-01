package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.hsong.oexam.pojo.PaperDetail;
import xyz.hsong.oexam.vo.QuestionWithScoreVo;

import java.util.List;
import java.util.Map;
public interface PaperDetailMapper {

    int deleteByPrimaryKey(Long id);

    int insert(PaperDetail record);

    int insertSelective(PaperDetail record);

    PaperDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaperDetail record);

    int updateByPrimaryKey(PaperDetail record);

    int insertQuestions(@Param("paperId") Long paperId, @Param("questions") List<QuestionWithScoreVo> questions);

    int deleteQuestion(@Param("paperId") Long paperId, @Param("questionId") Long questionId);

    int deleteByPaperId(Long paperId);

    int selectDetail(PaperDetail paperDetail);

    @MapKey("question_id")
    Map<Long, Map<String, Integer>> selectScoreByPaperId(Long paperId);

    int deleteByQuestionId(Long questionId);
}