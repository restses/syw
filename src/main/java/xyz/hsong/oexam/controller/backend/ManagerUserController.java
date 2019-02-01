package xyz.hsong.oexam.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.hsong.oexam.common.Const;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.User;
import xyz.hsong.oexam.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/user")
public class ManagerUserController {

    private final UserService userService;

    @Autowired
    public ManagerUserController(UserService userService) {
        this.userService = userService;
    }

    // 登录(管理员)
    @RequestMapping(value = "/login.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public ServerResponse login(@RequestParam String email, @RequestParam String password, HttpSession session) {

        ServerResponse response = userService.login(email, password);

        User user = (User) response.getData();

        if (response.isSuccess()) {

            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            }

            return ServerResponse.createError(ResponseCode.PERMISSION_DENIED.getCode(), ResponseCode.PERMISSION_DENIED.getDesc());
        }

        return response;
    }

    // 更改用户角色
    @RequestMapping("/role.do")
    @ResponseBody
    public ServerResponse updateRole(@RequestParam Long id, @RequestParam Integer role) {

        if (role == Const.Role.ROLE_CUSTOMER) {
            return userService.updateRole(id, Const.Role.ROLE_CUSTOMER);
        } else if (role == Const.Role.ROLE_ADMIN) {
            return userService.updateRole(id, Const.Role.ROLE_ADMIN);
        }

        return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    // 获得用户列表(分页)
    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse getUserList(@RequestBody User user, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getUserList(user, page, pageSize);
    }

    // 统计用户
    @RequestMapping("/count.do")
    @ResponseBody
    public ServerResponse getUserListCount(@RequestBody User user) {
        return userService.getUserListCount(user);
    }

    // 更新用户信息
    @RequestMapping("/update.do")
    @ResponseBody
    public ServerResponse updateUserInfo(User user) {
        return userService.updateUserInfo(user);
    }

    // 根据id获取用户信息
    @RequestMapping("/info.do")
    @ResponseBody
    public ServerResponse getUserInfo(@RequestParam Long userId) {
        return userService.getUserInfo(userId);
    }

    // 删除用户
    @RequestMapping("/delete.do")
    @ResponseBody
    public ServerResponse deleteUser(@RequestParam Long userId) {
        try {
            return userService.deleteUser(userId);
        } catch (Exception e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    // 重置密码
    @RequestMapping("/resetPassword.do")
    @ResponseBody
    public ServerResponse resetPassword(String email, String newPassword) {
        return userService.resetPassword(email, newPassword);
    }
    
}
