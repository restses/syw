var paperId;
var existedList = [];
var page = 1;
var pageCount;
var currentUser;
var paperInfo;


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

function viewDetail(id) {
    window.open("score.html#" + paperId + "-" + id,"_self");
}

function setExamUserList() {

    var $examUserList = $("#examUserList tbody");

    $examUserList.html("");

    if(examUserList.length==0){
        $examUserList.append("<label>考生列表为空</label>");
    }

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

        var html;

        if (item.status == 3) {
            html = "<tr id='examTr-" + item.id + "'><td>" + item.id + "</td><td>" + item.email + "</td><td>" + item.name + "</td><td>" + item.department + "</td><td>" + status + "</td><td><button class='btn btn-default btn-primary btn-sm' onclick='viewDetail(" + item.id + ");'>查看</button></td></tr>";
        } else {
            html = "<tr id='examTr-" + item.id + "'><td>" + item.id + "</td><td>" + item.email + "</td><td>" + item.name + "</td><td>" + item.department + "</td><td>" + status + "</td><td><button class='btn btn-default btn-primary btn-sm' disabled>查看</button></td></tr>";
        }

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