package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.hsong.oexam.pojo.ExamUser;
import xyz.hsong.oexam.vo.ExamUserVo;

import java.util.List;
public interface ExamUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ExamUser record);

    int insertSelective(ExamUser record);

    ExamUser selectByPrimaryKey(Long id);

    int updateStatus(ExamUser record);

    int updateByPrimaryKey(ExamUser record);

    int deleteExamUser(@Param("userId") Long userId, @Param("paperId") Long paperId);

    List<ExamUser> selectExamUserList(@Param("paperId") Long paperId, @Param("start") Integer start, @Param("offset") Integer offset);

    Integer selectExamUserStatus(@Param("paperId") Long paperId, @Param("userId") Long userId);

    List<Long> selectPaperCorrectUserId(Long paperId);

    void deleteByPaperId(Long paperId);

    List<ExamUserVo> selectExamUserDetailList(Long paperId);

    Integer insertExamUsers(@Param("examUsers") List<ExamUser> examUsers);

    void deleteByUserId(Long userId);
}