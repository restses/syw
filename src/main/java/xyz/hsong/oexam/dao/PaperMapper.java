package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.Param;
import xyz.hsong.oexam.pojo.Paper;
import xyz.hsong.oexam.vo.PaperQuestionVo;
import xyz.hsong.oexam.vo.PaperStatusVo;

import java.util.List;

public interface PaperMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Paper record);

    int insertSelective(Paper record);

    Paper selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Paper record);

    int updateByPrimaryKey(Paper record);

    List<Paper> selectPaperList(@Param("start") Integer start, @Param("offset") Integer offset);

    List<PaperQuestionVo> selectPaperQuestionList(Long paperId);

    List<Paper> selectPapers(@Param("pager") Paper paper,@Param("start") Integer start,@Param("offset") Integer offset);

    List<PaperStatusVo> selectUserPaperList(@Param("userId") Long userId, @Param("start") Integer start, @Param("offset") Integer offset);

    Integer selectUserPaperListCount(Long userId);

    Integer selectPaperCount(Paper paper);
}