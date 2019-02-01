var currentUser;
var examList;
var page = 1;
var pageCount;

function restChose() {
    $("#paperId").val("");
    $("#paperTitle").val("");
}

function pagePrevious() {

    if (page > 1) {
        page--;
        getExamList();
    }

}

function pageNext() {

    if (page < pageCount) {
        page++;
        getExamList();
    }
}


//获取用户信息
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

//获取考试列表
function getExamList() {

    var paper = {
        "id": $("#paperId").val(),
        "title": $("#paperTitle").val()
    }

    $.ajax({
        type: "post",
        dataType: "json",
        url: "../paper/search.do?page=" + page,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(paper),
        success: function (data) {

            if (data.error == 0) {
                examList = data.data;
                getPaperCount();
                setExamList();
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + "" + data.msg);
            }
        }
    });

}

//显示试题列表
function setExamList() {

    $("#examList tbody").html("");

    $.each(examList, function (index, item) {

        var html = "<tr id=tr-" + item.id + "><td>" + item.id + "</td><td>" + item.title + "</td><td>" + new Date(item.startTime).toLocaleString() + "</td><td>" + new Date(item.endTime).toLocaleString() + "</td><td><a href='javascript:scoreList(" + item.id + ");'>成绩列表</a></td></tr>";

        $("#examList tbody").append(html);
    });
    $("#page").html(page);
}

//获取试题页数
function getPaperCount() {

    var paper = {
        "id": $("#paperId").val(),
        "title": $("#paperTitle").val()
    }

    $.ajax({
        type: "post",
        dataType: "json",
        url: "../paper/count.do",
        data: JSON.stringify(paper),
        contentType: "application/json;charset=UTF-8",
        success: function (data) {

            if (data.error == 0) {
                questionCount = data.data;
                pageCount = (questionCount % 10 == 0 ? questionCount / 10 : parseInt(questionCount / 10) + 1);
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

function scoreList(id) {
    window.open("scoreList.html#" + id, "_self");
}


$(function () {
    getUserInfo();
    getExamList();

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