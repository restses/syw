package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.Param;
import xyz.hsong.oexam.pojo.Option;

import java.util.List;

public interface OptionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Option record);

    int insertSelective(Option record);

    Option selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Option record);

    int updateByPrimaryKey(Option record);

    int insertOptions(@Param("questionId") Long questionId, @Param("options") List<Option> options);

    int updateOptions(@Param("options") List<Option> options);

    int deleteByQuestionId(Long questionId);
}