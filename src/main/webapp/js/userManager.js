var page = 1;
var userList;
var userListCount;
var pageCount;
var currentUser;

function resetPassword(id) {

    var flag = confirm("重置密码？");

    if (flag) {

        var email;

        $.each(userList, function (index, item) {
            if (item.id == id) {
                email = item.email;
            }
        });

        $.post("resetPassword.do", {
            email: email,
            newPassword: "123456"
        }, function (data) {
            if (data.error == 0) {
                alert("已将密码重置为[123456]");
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + "" + data.msg);
            }
        });
    }


}

function updateInfo(id) {

    $("#updateInfo").modal('show');

    $.each(userList, function (index, item) {
        if (item.id == id) {
            $("#updateId").val(item.id);
            $("#updateName").val(item.name);
            $("#updateDepartment").val(item.department);
        }
    });

    $("#btn-info-save").click(function () {
        var btn = $(this);
        btn.button('loading');
        $.post("update.do", {
            id: $("#updateId").val(),
            name: $("#updateName").val(),
            department: $("#updateDepartment").val()
        }, function (data) {
            if (data.error == 0) {
                alert("修改成功");
                $("#updateInfo").modal('hide');
                btn.button('reset');
                getUserList();
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                btn.button('reset');
                alert("修改失败，错误代码：" + data.error + ",错误信息：" + data.msg);
            }
        });
    });

}

function updateRole(id, role) {
    role = (role - 1 == 0 ? 2 : 1);
    $.get("role.do?id=" + id + "&role=" + role, function (data) {
        if (data.error == 0) {
            getUserList();
        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }
    });
}

function deleteUser(id) {

    var flag = confirm("确认删除？");

    if (flag) {
        $("#delete-ing").show();
        $.get("delete.do?userId=" + id, function (data) {
            if (data.error == 0) {
                $("#delete-ing").hide();
                $("#tr-" + id).remove();
                $("#delete-success").show().delay(2000).hide(0);

            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                $("#delete-ing").hide();
                $("#delete-fail").show().delay(2000).hide(0);
                alert(data.error + "" + data.msg);
            }
        });
    }

}

function getUserInfo() {

    //获取当前登陆的用户信息
    $.get("../../user/info.do", function (data) {
        if (data.error == 0) {
            currentUser = data.data;
            $("#currentUser").html(currentUser.name);
        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }
    });
}

function pagePrevious() {

    if (page > 1) {
        page--;
        getUserList();
    }

}

function pageNext() {

    if (page < pageCount) {
        page++;
        getUserList();
    }
}

function setUserList() {

    var $userList = $("#userList tbody");
    $("#checkedAll").prop('checked', false);

    $userList.html("");

    $.each(userList, function (index, item) {
        var html = "<tr id='tr-" + item.id + "'><td>" + item.id + "</td><td>" + item.email + "</td><td>" + item.name + "</td>" +
            "<td>" + item.department + "</td><td>" + (item.role == 1 ? "普通用户" : "管理员") + "</td>" +
            "<td><a href='javascript:updateInfo(" + item.id + ")'>修改信息</a> <a href='javascript:resetPassword(" + item.id + ");'>重置密码</a> <a href='javascript:updateRole(" + item.id + "," + item.role + ")'>" + (item.role == 1 ? "设为管理员" : "设为普通用户") + "</a> <a href='javascript:deleteUser(" + item.id + ")'>删除</a></td></tr>";
        $userList.append(html);
    });

    $("#page").html(page);

}

function getUserListCount() {

    var user = {
        "id": $("#id").val(),
        "name": $("#name").val(),
        "department": $("#department").val()
    }

    $.ajax({
        type: "post",
        dataType: "json",
        url: "../user/count.do",
        data: JSON.stringify(user),
        contentType: "application/json;charset=UTF-8",
        success: function (data) {

            if (data.error == 0) {
                userListCount = data.data;
                pageCount = (userListCount % 10 == 0 ? userListCount / 10 : parseInt(userListCount / 10) + 1);
                $("#totalPage").html(pageCount);
                console.info("pageCount:" + pageCount);
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + " " + data.msg);
            }
        }
    });
}

function getUserList() {

    var user = {
        "id": $("#id").val(),
        "name": $("#name").val(),
        "department": $("#department").val()
    }

    $.ajax({
        type: "post",
        dataType: "json",
        url: "../user/list.do?page=" + page,
        data: JSON.stringify(user),
        contentType: "application/json;charset=UTF-8",
        success: function (data) {

            if (data.error == 0) {
                userList = data.data;
                setUserList();
                getUserListCount();
                $("#loading").hide();
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + "" + data.msg);
            }
        }
    });
}

$(function () {
    getUserInfo();
    getUserList();

    $("#logout").click(function () {

        var flag = confirm("确认退出？");

        if (flag) {
            $.get("../../user/logout.do", function (data) {
                if (data.error == 0) {
                    window.open("../index.html", "_self");
                } else {
                    alert(data.msg);
                }
            });
        }
    });

});