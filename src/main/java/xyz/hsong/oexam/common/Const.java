package xyz.hsong.oexam.common;

public class Const {
	
    public static final String CURRENT_USER = "current_user";

    public static final String PHONE = "phone";

    public static final String USERNAME = "username";

    public static final String SUCCESS = "success";

    public static final String TOKEN_PREFIX = "Token_";

    public static final String CURRENT_CORRECT = "current_correct";

    public interface OrderBy {
        String PRICE_ASC = "price_asc";
        String PRICE_DESC = "price_desc";
    }

    public interface Role {
        int ROLE_CUSTOMER = 1;//普通用户
        int ROLE_ADMIN = 2;//管理员
    }
}
