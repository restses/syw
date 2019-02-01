package xyz.hsong.oexam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import xyz.hsong.oexam.common.Const;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.dao.AchievementMapper;
import xyz.hsong.oexam.dao.ExamUserMapper;
import xyz.hsong.oexam.dao.MarkMapper;
import xyz.hsong.oexam.dao.UserMapper;
import xyz.hsong.oexam.pojo.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserMapper userMapper;
	private final AchievementMapper achievementMapper;
	private final ExamUserMapper examUserMapper;
	private final MarkMapper markMapper;

	@Autowired
	public UserServiceImpl(UserMapper userMapper, AchievementMapper achievementMapper, ExamUserMapper examUserMapper,
			MarkMapper markMapper) {
		this.userMapper = userMapper;
		this.achievementMapper = achievementMapper;
		this.examUserMapper = examUserMapper;
		this.markMapper = markMapper;
	}

	@Override
	public ServerResponse<User> login(String email, String password) {

		User user;

		try {
			user = userMapper.checkUser(email, DigestUtils.md5DigestAsHex(password.getBytes()));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		if (user == null) {
			return ServerResponse.createError(ResponseCode.USER_CHECK_FAIL.getCode(),
					ResponseCode.USER_CHECK_FAIL.getDesc());
		}

		// 将密码清空
		user.setPassword(null);

		return ServerResponse.createSuccess(user);
	}

	@Override
	public ServerResponse register(User user) {
		// 对邮箱账号进行验证
		ServerResponse response = this.checkValid(user.getEmail());

		if (!response.isSuccess()) {
			return response;
		}

		// 设置用户角色为客户
		user.setRole(Const.Role.ROLE_CUSTOMER);
		// 将用户密码进行MD5加密
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

		try {

			if (userMapper.insertSelective(user) > 0) {
				// 注册成功
				return ServerResponse.createSuccess(ResponseCode.SUCCESS.getDesc(), user);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());

		}

		return ServerResponse.createError(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
	}

	@Override
	public ServerResponse checkValid(String email) {

		try {
			if (userMapper.checkEmail(email) > 0) {
				return ServerResponse.createError(ResponseCode.EMAIL_CHECK_FAIL.getCode(),
						ResponseCode.EMAIL_CHECK_FAIL.getDesc());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		return ServerResponse.createSuccess();
	}

	@Override
	public ServerResponse resetPassword(String email, String newPassword) {

		try {
			// 执行重置密码的操作
			if (userMapper.resetPassword(email, DigestUtils.md5DigestAsHex(newPassword.getBytes())) > 0) {
				return ServerResponse.createSuccess(ResponseCode.PASSWORD_RESET_SUCCESS.getDesc());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		return ServerResponse.createError(ResponseCode.PASSWORD_RESET_FAIL.getCode(),
				ResponseCode.PASSWORD_RESET_FAIL.getDesc());
	}

	@Override
	public ServerResponse updatePassword(String email, String oldPassword, String newPassword) {

		try {
			// 判断旧密码是否正确
			if (userMapper.checkPassword(email, DigestUtils.md5DigestAsHex(oldPassword.getBytes())) == 0) {
				return ServerResponse.createError(ResponseCode.PASSWORD_INCORRECT.getCode(),
						ResponseCode.PASSWORD_INCORRECT.getDesc());
			}

			// 修改密码操作
			if (userMapper.resetPassword(email, DigestUtils.md5DigestAsHex(newPassword.getBytes())) > 0) {
				return ServerResponse.createSuccess(ResponseCode.PASSWORD_UPDATE_SUCCESS.getDesc());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		return ServerResponse.createError(ResponseCode.PASSWORD_UPDATE_FAIL.getCode(),
				ResponseCode.PASSWORD_UPDATE_FAIL.getDesc());
	}

	@Override
	public ServerResponse updateUserInfo(User newUser, User currentUser) {

		User updateUser = new User();

		// 将ID填充完整
		updateUser.setId(currentUser.getId());

		// 判断用户已更新的信息
		if (!currentUser.getName().equals(newUser.getName())) {
			updateUser.setName(newUser.getName());
			currentUser.setName(updateUser.getName());
		}

		if (!currentUser.getDepartment().equals(newUser.getDepartment())) {
			updateUser.setDepartment(newUser.getDepartment());
			currentUser.setDepartment(updateUser.getDepartment());
		}

		if (!currentUser.getEmail().equals(newUser.getEmail())) {
			ServerResponse checkValid = this.checkValid(newUser.getEmail());
			if (!checkValid.isSuccess()) {
				return checkValid;
			}
			updateUser.setEmail(newUser.getEmail());
			currentUser.setEmail(updateUser.getEmail());
		}

		try {

			// 修改个人信息
			if (userMapper.updateByPrimaryKeySelective(updateUser) > 0) {
				return ServerResponse.createSuccess(currentUser);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		return ServerResponse.createError(ResponseCode.USERINFO_UPDATE_FAIL.getCode(),
				ResponseCode.USERINFO_UPDATE_FAIL.getDesc());
	}

	@Override
	public ServerResponse getUserInfo(Long userId) {

		User user;

		try {
			user = userMapper.selectByPrimaryKey(userId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		if (user == null) {
			return ServerResponse.createError(ResponseCode.USER_NOT_FIND.getCode(),
					ResponseCode.USER_NOT_FIND.getDesc());
		}

		return ServerResponse.createSuccess(user);
	}

	@Override
	public ServerResponse updateRole(Long id, Integer role) {

		try {
			if (userMapper.updateRole(id, role) > 0) {
				return ServerResponse.createSuccess(ResponseCode.USERROLE_UPDATE_SUCCESS.getDesc());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		return ServerResponse.createError(ResponseCode.UPDATE_ROLE_FAIL.getCode(),
				ResponseCode.UPDATE_ROLE_FAIL.getDesc());
	}

	@Override
	public ServerResponse getUserList(User user, Integer start, Integer offset) {

		try {
			List<User> users = userMapper.selectUserList(user, (start - 1) * offset, offset);
			return ServerResponse.createSuccess(users);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

	}

	@Override
	public ServerResponse getUserListCount(User user) {

		try {
			Integer integer = userMapper.selectUserListCount(user);
			return ServerResponse.createSuccess(integer);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());

		}
	}

	@Override
	public ServerResponse updateUserInfo(User user) {

		try {
			if (userMapper.updateByPrimaryKeySelective(user) > 0) {
				return ServerResponse.createSuccess(ResponseCode.USERINFO_UPDATE_FAIL.getDesc());
			}
			return ServerResponse.createError(ResponseCode.USERINFO_UPDATE_FAIL.getCode(),
					ResponseCode.USERINFO_UPDATE_FAIL.getDesc());

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

	}

	@Override
	@Transactional
	public ServerResponse deleteUser(Long userId) {

		try {

			userMapper.deleteByPrimaryKey(userId);
			achievementMapper.deleteByUserId(userId);
			examUserMapper.deleteByUserId(userId);
			markMapper.deleteByUserId(userId);

			return ServerResponse.createSuccess();

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}

	}

}
