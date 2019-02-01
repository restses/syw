package xyz.hsong.oexam.service;

import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.User;

public interface UserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse register(User user);

    ServerResponse checkValid(String email);

    ServerResponse resetPassword(String username, String newPassword);

    ServerResponse updatePassword(String username, String oldPassword, String newPassword);

    ServerResponse updateUserInfo(User newUser, User currentUser);

    ServerResponse getUserInfo(Long userId);

    ServerResponse updateRole(Long id, Integer role);

    ServerResponse getUserList(User user, Integer start, Integer offset);

    ServerResponse getUserListCount(User user);

    ServerResponse updateUserInfo(User user);

    ServerResponse deleteUser(Long userId);
}
