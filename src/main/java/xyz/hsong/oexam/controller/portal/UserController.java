package xyz.hsong.oexam.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.hsong.oexam.common.Const;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.User;
import xyz.hsong.oexam.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 登录
    @RequestMapping(value = "/login.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public ServerResponse login(@RequestParam String email, @RequestParam String password, HttpSession session) {

        ServerResponse response = userService.login(email, password);

        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    // 退出登录
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createSuccess(ResponseCode.LOGOUT_SUCCESS.getDesc());
    }

    // 注册
    @RequestMapping(value = "/register.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ServerResponse register(User user, HttpSession session) {
        ServerResponse response = userService.register(user);

        //判断用户注册是否成功
        if (response.isSuccess()) {
            //将用户存入session中
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    @RequestMapping(value = "/valid.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ServerResponse checkValid(@RequestParam String email) {
        return userService.checkValid(email);
    }

    @RequestMapping(value = "/info.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ServerResponse getCurrentUserInfo(HttpSession session) {
        User user;

        if ((user = (User) session.getAttribute(Const.CURRENT_USER)) != null) {
            return ServerResponse.createSuccess(user);
        }

        return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }
    
    // 更新用户信息
    @RequestMapping(value = "/update.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ServerResponse updateUserInfo(User user, HttpSession session) {

        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);

        if (currentUser == null) {
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse response = userService.updateUserInfo(user, currentUser);

        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    // 修改密码
    @RequestMapping(value = "/password.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ServerResponse updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session) {
        User user;
        if ((user = (User) session.getAttribute(Const.CURRENT_USER)) != null) {
            return userService.updatePassword(user.getEmail(), oldPassword, newPassword);
        } else {
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
    }
}
