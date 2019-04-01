package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.hsong.oexam.bo.QuestionBo;
import xyz.hsong.oexam.pojo.Question;
import xyz.hsong.oexam.vo.PaperQuestionVo;

import java.util.List;
import java.util.Map;
import java.util.Set;
public interface QuestionMapper {

    int insertSelective(Question question);

    Question selectByPrimaryKey(Long id);

    List<Question> selectQuestionList(@Param("start") Integer start, @Param("offset") Integer offset);

    List<Question> selectQuestion(@Param("question") Question question,@Param("start") Integer start,@Param("offset") Integer offset);

    int updateQuestion(Question question);

    @MapKey("id")
    Map<Long, QuestionBo> selectAnswerByQuestionId(@Param("list") Set<Long> questionId);

    Integer selectQuestionCount(Question question);

    int deleteQuestion(Long id);

    PaperQuestionVo selectQuestionDetail(Long id);
}
