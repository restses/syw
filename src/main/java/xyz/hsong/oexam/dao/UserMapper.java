package xyz.hsong.oexam.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.hsong.oexam.pojo.User;

import java.util.List;
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User checkUser(@Param("email") String email, @Param("password") String password);

    int checkEmail(String email);

    int resetPassword(@Param("email") String email, @Param("password") String password);

    int checkPassword(@Param("email") String email, @Param("password") String password);

    int updateRole(@Param("id") Long id, @Param("role") Integer role);

    List<User> selectUserList(@Param("user") User user, @Param("start") Integer start, @Param("offset") Integer offset);

    Integer selectUserListCount(User user);

}