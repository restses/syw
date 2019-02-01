$(function () {

    var validator = $("#registerForm").validate({

        submitHandler: function (form) {
            $("#btn-submit").button('loading');
            $(form).ajaxSubmit({
                dataType: "json", success: function (data) {
                    // 解析数据时报错，原因是返回的数据已经是object格式，无需再使用var result = JSON.parse(data); 进行转换，
                    // 直接使用var result =data.data；
                    if (data.error == 0) {
                        alert("注册成功");
                        window.open("user/exam.html", "_self");
                    } else if (data.error == 10) {
                        window.open("../index.html", "_self");
                    } else {
                        alert("修改失败，错误代码：" + data.error + ",错误信息：" + data.msg);
                        $("#btn-submit").button('reset');
                    }
                }
            });
        },
        errorClass: "text-danger",
        errorElement: "span",
        rules: {
            email: {
                required: true,
                email: true,
                remote: {
                    url: "user/valid.do",
                    type: "get",
                    dataType: "json",
                    data: {
                        email: function () {
                            return $("#email").val();
                        }
                    },
                    dataFilter: function (data, type) { //过滤返回结果
                        var response = JSON.parse(data);
                        return response.error == 0; //false代表用户名已经存在
                    }
                }

            },
            password: {
                required: true,
                minlength: 6,
                maxlength: 18
            },
            password_confirm: {
                required: true,
                equalTo: "#password"
            },
            name: {
                required: true,
                minlength: 2
            },
            department: "required"

        },
        messages: {
            email: {
                required: "请输入邮箱地址",
                email: "邮箱格式不正确",
                remote: "邮箱已被注册,请重新输入"
            },
            password: {
                required: "请输入密码",
                minlength:
                    "密码长度不能小于 6 个字符",
                maxlength:
                    "密码长度不能超过 18 个字符"
            },
            password_confirm: {
                required: "请再次输入密码",
                minlength:
                    "密码长度不能小于 6 个字母",
                equalTo:
                    "两次密码输入不一致"
            },
            name: {
                required: "请输入真实姓名",
                minlength: "姓名长度不正确"
            },
            department: "部门（班级）信息不能为空"
        }
    });

    $("#btn-reset").click(function () {
        $("#registerForm").get(0).reset();
        validator.resetForm();
    });

});
