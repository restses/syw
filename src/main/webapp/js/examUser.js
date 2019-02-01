var paperId;
var examUserList;
var page = 1;
var userList;
var userListCount;
var pageCount;
var checkedUserList = [];
var existedList = [];
var currentUser;
var paperInfo;

function deleteExamUser(id) {

    var flag = confirm("确认删除？");

    if (flag) {

        $("#delete-ing").show();

        $.get("deleteExamUser.do?paperId=" + paperId + "&userId=" + id, function (data) {
            if (data.error == 0) {

                $("#delete-ing").hide();
                $("#examTr-" + id).remove();

                existedList = $.grep(existedList, function (item, index) {
                    return item != id;
                });

                $("#delete-success").show().delay(2000).hide(0);

            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + "" + data.msg);
                $("#delete-ing").hide();
                $("#delete-fail").show().delay(2000).hide(0);
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

function userSave() {

    var users = [];

    $.each(checkedUserList, function (index, item) {

        var user = {
            "paperId": paperId,
            "userId": item
        }

        users.push(user);
    });


    $("#saving").show();

    $.ajax({
        type: "post",
        dataType: "json",
        url: "addExamUser.do",
        data: JSON.stringify(users),
        contentType: "application/json;charset=UTF-8",
        success: function (data) {

            if (data.error == 0) {
                alert("添加考生成功");
                $('#saving').hide();
                $('#userList').modal('hide');
                getExamUserList();
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + "" + data.msg);
            }

        }
    });
}

function isExisted() {

    $.each(existedList, function (index, item) {
        $("#cb-" + item).attr("disabled", true);
        $("#cb-" + item).prop('checked', true);
    });
}


function showCheckedUser() {

    $.each(checkedUserList, function (i, m) {
        $.each(userList, function (j, n) {
            if (m == n.id) {
                $("#cb-" + n.id).prop("checked", true);
            }
        });
    });
}

function addUser(id) {

    if (checkedUserList.indexOf(id) == -1 && existedList.indexOf(id) == -1) {
        checkedUserList.push(id);
    }

    console.info(JSON.stringify(checkedUserList));
}

function removeUser(id) {

    checkedUserList = $.grep(checkedUserList, function (item, index) {
        return item != id;
    });

    console.info(JSON.stringify(checkedUserList));

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

function setExamUserList() {

    var $examUserList = $("#examUserList tbody");

    $examUserList.html("");

    $.each(examUserList, function (index, item) {

        existedList.push(item.id);
        var status;

        if (item.status == 0) {
            status = "未参加";
        } else if (item.status == 1) {
            status = "考试中";
        } else if (item.status == 2) {
            status = "已交卷";
        } else if (item.status == 3) {
            status = '已批阅';
        }

        var html = "<tr id='examTr-" + item.id + "'><td>" + item.id + "</td><td>" + item.email + "</td><td>" + item.name + "</td><td>" + item.department + "</td><td>" + status + "</td><td><a href='javascript:deleteExamUser(" + item.id + ")'>删除</a></td></tr>";
        $examUserList.append(html);
    });
}

function getExamUserList() {
    $.get("examUser.do?paperId=" + paperId, function (data) {
        if (data.error == 0) {
            examUserList = data.data;
            $("#examUserLoading").hide();
            setExamUserList();
        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }
    });
}

function setUserList() {

    var $userList = $("#userList tbody");
    $("#checkedAll").prop('checked', false);

    $userList.html("");

    $.each(userList, function (index, item) {
        var html = "<tr id='tr-" + item.id + "'><td><input type='checkbox' id='cb-" + item.id + "' name='user' value='" + item.id + "'/> </td><td>" + item.id + "</td><td>" + item.email + "</td><td>" + item.name + "</td><td>" + item.department + "</td></tr>";
        $userList.append(html);
    });

    $("#page").html(page);

    isExisted();
    showCheckedUser();
    checkListener();

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

function addExamUser() {
    $("#userList").modal('show');
    checkedUserList = [];
    getUserList();
}

function checkAll() {

    $("#checkedAll").click(
        function () {

            var isChecked = $("#checkedAll").prop("checked");

            if (isChecked) {

                $(":checkbox[name='user']").not(':disabled').prop("checked", true);

                var $checkbox = $(":checkbox[name='user']:checked").not(':disabled');

                $.each($checkbox, function (index, item) {
                    addUser($(item).val());
                });

            } else {
                $(":checkbox[name='user']").not(':disabled').prop("checked", false);
                checkedUserList = [];
            }

            console.info(JSON.stringify(checkedUserList));
        }
    );
}


function checkListener() {

    $(":checkbox[name='user']").not('disabled').on('click', function () {

        var isCheck = $(this).prop('checked');

        if (isCheck) {
            addUser($(this).val());
        } else {
            removeUser($(this).val());
        }

    });
}

function getExamInfo() {

    $.get("../paper/detail.do?paperId=" + paperId, function (data) {
        if (data.error == 0) {
            paperInfo = data.data;

            console.info(JSON.stringify(paperInfo));

            $("#title").html(paperInfo.title);
            $("#time").html(paperInfo.time);
            $("#startTime").html(new Date(paperInfo.startTime).toLocaleString());
            $("#endTime").html(new Date(paperInfo.endTime).toLocaleString());

        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }
    });

}

$(function () {
    paperId = window.location.hash.substring(1);
    getUserInfo();
    getExamInfo();
    getExamUserList();
    checkAll();

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