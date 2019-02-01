package xyz.hsong.oexam.common;

public enum ResponseCode {

    SUCCESS(0, "SUCCESS"),
    LOGOUT_SUCCESS(0, "注销登陆成功"),
    PASSWORD_RESET_SUCCESS(0, "重置密码成功"),
    PASSWORD_UPDATE_SUCCESS(0, "修改密码成功"),
    USERROLE_UPDATE_SUCCESS(0, "修改用户角色成功"),
    QUESTION_ADD_SUCCESS(0, "添加试题成功"),
    QUESTION_UPDATE_SUCCESS(0, "修改试题成功"),
    PAPER_ADD_SUCCESS(0, "添加试卷成功"),
    PAPER_UPDATE_SUCCESS(0, "更新试卷成功"),
    PAPER_DELETE_SUCCESS(0, "删除试卷成功"),
    EXAM_USER_ADD_SUCCESS(0, "增加考试人员成功"),
    EXAM_USER_DELETE_SUCCESS(0, "删除考试人员成功"),
    EXAM_USER_UPDATE_SUCCESS(0, "更新考试人员状态成功"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "用户未登陆"),
    ILLEGAL_ARGUMENT(11, "参数错误"),
    USER_CHECK_FAIL(12, "用户名或密码错误"),
    EMAIL_CHECK_FAIL(13, "邮箱已被注册"),
    REGISTER_FAIL(14, "注册失败,请重试"),
    PASSWORD_RESET_FAIL(15, "重置密码失败"),
    PASSWORD_INCORRECT(16, "密码不正确"),
    PASSWORD_UPDATE_FAIL(17, "修改密码失败"),
    USERINFO_UPDATE_FAIL(18, "修改用户信息失败"),
    USER_NOT_FIND(19, "用户不存在"),
    DATABASE_ERROR(20, "数据库操作错误"),
    PERMISSION_DENIED(21, "用户未授权"),
    UPDATE_ROLE_FAIL(22, "修改用户角色失败"),
    QUESTION_ADD_ERROR(23, "添加试题失败"),
    QUESTION_NOT_FIND(24, "试题不存在"),
    QUESTION_UPDATE_FAIL(25, "更新试题失败"),
    PAPER_ADD_FAIL(26, "添加试卷失败"),
    PAPER_UPDATE_FAIL(27, "更新试卷失败"),
    PAPER_DELETE_FAIL(28, "删除试卷失败"),
    PAPER_NOT_FIND(29, "试卷不存在"),
    PAPER_QUESTION_NOT_FIND(30, "试卷试题列表为空"),
    PAPER_LIST_EMPTY(31, "试卷列表为空"),
    QUESTION_ADD_REPEATED(32, "试题已存在"),
    EXAM_USER_ADD_FAIL(33, "添加考试人员失败"),
    EXAM_USER_DELETE_FAIL(34, "删除考试人员失败"),
    EXAM_USER_UPDATE_FAIL(35, "更新考试人员状态失败"),
    TIME_ERROR(36, "考试时间错误"),
    SCORE_ERROR(37, "获取成绩错误"),
    EXAM_REPEATED(38, "重复考试"),
    CORRECT_REPEATED(39,"阅卷已在进行中");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
