package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.Param;
import xyz.hsong.oexam.pojo.Mark;
import xyz.hsong.oexam.vo.MarkVo;

import java.util.List;

public interface MarkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Mark record);

    int insertSelective(Mark record);

    Mark selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Mark record);

    int updateByPrimaryKey(Mark record);

    Integer selectMark(@Param("userId") Long userId, @Param("paperId") Long paperId);

    void deleteByPaperId(Long paperId);

    void deleteByUserIdWithPaperId(@Param("userId") Long userId,@Param("paperId") Long paperId);

    List<MarkVo> selectMarkList(Long paperId);

    void deleteByUserId(Long userId);
}