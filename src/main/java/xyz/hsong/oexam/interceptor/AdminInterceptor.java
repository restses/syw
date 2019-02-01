package xyz.hsong.oexam.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.hsong.oexam.common.Const;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 后台登陆拦截器,拦截非管理员请求
 */
public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
			throws Exception {

		HttpSession session = httpServletRequest.getSession();
		User user = (User) session.getAttribute(Const.CURRENT_USER);

		// 不是管理员，拦截
		if (user.getRole() != 2) {
			httpServletRequest.setCharacterEncoding("utf-8");
			httpServletResponse.setCharacterEncoding("utf-8");
			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

			ObjectMapper objectMapper = new ObjectMapper();
			PrintWriter writer = httpServletResponse.getWriter();
			ServerResponse<Object> error = ServerResponse.createError(ResponseCode.PERMISSION_DENIED.getCode(),
					ResponseCode.PERMISSION_DENIED.getDesc());
			String response = objectMapper.writeValueAsString(error);
			writer.write(response);
			writer.close();

			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
	}
}
