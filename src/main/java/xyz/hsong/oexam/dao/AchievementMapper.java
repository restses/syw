package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import xyz.hsong.oexam.bo.QuestionBo;
import xyz.hsong.oexam.pojo.Achievement;

import java.util.List;
import java.util.Map;

public interface AchievementMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(Achievement record);

    int insertSelective(Achievement record);

    Achievement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Achievement record);

    int updateByPrimaryKey(Achievement record);

    int insertAchievement(@Param("achievements") List<Achievement> achievements);

    @MapKey("id")
    Map<Long, QuestionBo> selectByUserIdAndPaperId(@Param("userId") Long userId, @Param("paperId") Long paperId);

    List<Long> selectUserAnswers(@Param("userId") Long userId, @Param("paperId") Long paperId);

    void deleteByQuestionId(Long questionId);

    void deleteByPaperId(Long paperId);

    void deleteByUserIdWithPaperId(@Param("userId") Long userId, @Param("paperId") Long paperId);

    void deleteByUserId(Long userId);
}