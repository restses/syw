var currentUser;
var paperList;//试卷列表
var paperCount;//试卷列表总页数
var page = 1;//当前页码

function getPage(data) {

    if (data >= 1 && data <= paperCount) {
        page = data;
        getPaperList();
        setPageSlide();
    }

}

function previous() {

    if (page > 1) {
        page--;
        getPaperList();
        setPageSlide();
    }
}

function next() {

    if (page < paperCount) {
        page++;
        getPaperList();
        setPageSlide();
    }
}

function setPageSlide() {

    var $pageSlide = $("#page-slide");
    $pageSlide.html("");
    var pre = "<li><a href=\"javascript:previous();\" aria-label=\"Previous\" id=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>";

    $pageSlide.append(pre);

    for (var i = 1; i <= paperCount; i++) {
        var html;
        if (i == page) {
            html = "<li class='active'><a>" + i + "</a></li>"
        } else {
            html = "<li><a href=\"javascript:getPage(" + i + ");\">" + i + "</a></li>"
        }

        $pageSlide.append(html);
    }

    var next = "<li><a href=\"javascript:next();\" aria-label=\"Next\"><span aria-hidden=\"true\" id=\"Next\">&raquo;</span></a></li>";

    $pageSlide.append(next);
}


function getPaperListCount() {
    $.get("../paper/count.do", function (data) {
        if (data.error == 0) {
            var rows = data.data;
            if (rows % 5 != 0) {
                paperCount = parseInt(rows / 5) + 1;
            } else {
                paperCount = rows / 5;
            }

            setPageSlide();
        }
    })
}

function setInfo() {
    $("#currentUser").html(currentUser.name);
    $("#email").val(currentUser.email);
    $("#realname").val(currentUser.name);
    $("#department").val(currentUser.department);
}

function setPaperList(data) {

    var $paperList = $("#paper-content");
    $paperList.html("");
    if (data.length != 0) {

        for (var i = 0; i < data.length; i++) {

            var html = "<div class=\"paper-item col-xs-12\">\n" +
                "                <div class=\"col-sm-8 col-md-7 col-xs-12\">\n" +
                "                    <h4><span class=\"glyphicon glyphicon-bookmark\" aria-hidden=\"true\"></span>" + data[i].title + "</h4>\n" +
                "                    <p>考试时间：<span id='ps-" + data[i].id + "'>" + new Date(data[i].startTime).toLocaleString() + "</span>&nbsp;~&nbsp;<span id='pe-" + data[i].id + "'>" + new Date(data[i].endTime).toLocaleString() + "</span></p>\n" +
                "                    <p>答卷时间：<span>" + data[i].time + "</span>&nbsp;分钟</p>\n" +
                "                </div>\n";
            var button_active = "<div class=\"col-sm-3 col-sm-offset-1 col-xs-12\">\n" +
                "                    <a class=\"btn btn-default btn-primary\" role=\"button\" href=\"paper.html#" + data[i].id + "\" id='pid-" + data[i].id + "'>立即参加</a>\n" +
                "                </div>\n" +
                "            </div>";
            var button_disable = "<div class=\"col-sm-3 col-sm-offset-1 col-xs-12\">\n" +
                "                    <a class=\"btn btn-default btn-primary disabled\" role=\"button\" href=\"paper.html#" + data[i].id + "\" id='pid-" + data[i].id + "'>试卷评阅中</a>\n" +
                "                </div>\n" +
                "            </div>";
            var button_review = "<div class=\"col-sm-3 col-sm-offset-1 col-xs-12\">\n" +
                "                    <a class=\"btn btn-default btn-primary \" role=\"button\" href=\"score.html#" + data[i].id + "\" id='pid-" + data[i].id + "'>查看成绩</a>\n" +
                "                </div>\n" +
                "            </div>";

            if (data[i].status == 0) {
                $paperList.append(html + button_active);
            } else if (data[i].status == 2) {
                $paperList.append(html + button_disable);
            } else {
                $paperList.append(html + button_review);
            }

        }
    } else {
        $paperList.append("<span class='text-center'>没有需要参加的考试</span>");
    }
}

function getPaperList() {
    $.get("../paper/list.do?page=" + page, function (data) {
        if (data.error == 0) {
            paperList = data.data;
            setPaperList(paperList);
        }
    })
}


$(function () {

    $.get("info.do", function (data) {
        if (data.error == 0) {
            currentUser = data.data;
            setInfo();
            getPaperList();
            getPaperListCount();
        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }
    });


    $("#logout").click(function () {

        var flag = confirm("确认退出？");

        if (flag) {
            $.get("logout.do", function (data) {
                if (data.error == 0) {
                    window.open("../index.html", "_self");
                } else {
                    alert(data.msg);
                }
            });
        }
    });

    $("#btn-info-save").click(function () {
        var btn = $(this);
        btn.button('loading');
        $.post("update.do", {
            id: currentUser.id,
            email: currentUser.email,
            name: $("#realname").val(),
            department: $("#department").val()
        }, function (data) {
            if (data.error == 0) {
                currentUser = data.data;
                btn.button('reset');
                setInfo();
                alert("修改成功");
                $("#myInfo").modal('toggle');
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                btn.button('reset');
                alert("修改失败，错误代码：" + data.error + ",错误信息：" + data.msg);
            }
        });
    });

    $("#updatePasswordForm").validate({

        submitHandler: function (form) {
            $("#btn-password-save").button('loading');
            $(form).ajaxSubmit({
                dataType: "json",
                success: function (data) {
                    // 解析数据时报错，原因是返回的数据已经是object格式，无需再使用var result = JSON.parse(data); 进行转换，
                    // 直接使用var result =data.data；
                    if (data.error == 0) {
                        alert("修改密码成功");
                        $("#updatePassword").modal('toggle');
                    } else if (data.error == 10) {
                        window.open("../index.html", "_self");
                    } else if (data.error == 16) {
                        alert(data.msg);
                        $("#btn-password-save").button('reset');
                    } else {
                        alert("修改失败，错误代码：" + data.error + ",错误信息：" + data.msg);
                        $("#btn-password-save").button('reset');
                    }
                }
            });
        },
        errorClass: "text-danger",
        errorElement: "span",
        rules: {
            oldPassword: {
                required: true,
                minlength: 6,
                maxlength: 18
            },
            newPassword: {
                required: true,
                minlength: 6,
                maxlength: 18
            },
            password_confirm: {
                required: true,
                equalTo: "#newPassword"
            }
        },
        messages: {
            oldPassword: {
                required: "请输入密码",
                minlength:
                    "密码长度不能小于 6 个字符",
                maxlength:
                    "密码长度不能超过 18 个字符"
            },
            newPassword: {
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
            }
        }
    });

    $("#btn-password-save").click(function () {

        $("#updatePasswordForm").submit();
    });

})