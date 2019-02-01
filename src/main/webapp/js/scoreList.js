var currentUser;
var scoreList = [];
var paperId;

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


function setScoreList() {

    var totalScore = 0;
    var max = 0;
    var min = 100;

    $.each(scoreList, function (index, item) {

        totalScore += item.score;

        if (max < item.score) {
            max = item.score;
        }

        if (min > item.score) {
            min = item.score;
        }

        var html = "<tr><td>" + item.id + "</td><td>" + item.name + "</td><td>" + item.department + "</td><td>" + item.score + "</td><td><a href='score.html#" + paperId + "-" + item.id + "' >详情</a></td></tr>"
        $("#scoreList tbody").append(html);
    });

    $("#max").html(max);
    $("#min").html(min);
    $("#avg").html(parseFloat(totalScore) / scoreList.length);

}

function getScoreList() {

    $.get("scoreList.do?paperId=" + paperId, function (data) {
        if (data.error == 0) {
            scoreList = data.data;
            setScoreList();
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
    getScoreList();

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