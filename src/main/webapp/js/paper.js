var currentUser;
var paperInfo;
var time;
var paperId;
var optionIndex = "ABCDEFGHIJKRMNOPQRSTUVWXYZ";

function handOn() {
    $("#myModal").modal('show');
    var options = $(":radio,:checkbox").serializeArray();
    var json = [];

    $.each(options, function (i, field) {

        var option = {
            "userId": currentUser.id,
            "paperId": paperId,
            "questionId": field.name,
            "optionId": field.value
        };

        json.push(option);
    });

    console.info(JSON.stringify(json));

    $.ajax({
        type: "post",
        dataType: "json",
        url: "../exam/submit.do?userId=" + currentUser.id + "&paperId=" + paperId,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(json),
        success: function (data) {
            if (data.error == 0) {
                alert("交卷成功");
                window.open("exam.html", "_self");
            } else {
                $("#myModal").modal('hide');
                alert("错误代码：" + data.error + " 错误信息：" + data.msg);
            }
        }
    });
}

function alive() {

    setInterval(function () {
        $.get("../time/now.do", function (data) {
            console.info(data.data);
        })
    }, 60000);

}

function setTime(data) {
    time = parseInt(data) * 60 * 1000;
    setInterval(function () {
        time = time - 1000;
        if (time > 0) {
            var h = Math.floor(time / 1000 / 60 / 60 % 24);
            var m = Math.floor(time / 1000 / 60 % 60);
            var s = Math.floor(time / 1000 % 60);
            $("#time").html(h + ":" + m + ":" + s);
        } else {
            handOn();
        }
    }, "1000");
}

function setPaperInfo(data) {
    $("#title").html(data.title);
    setTime(data.time);
    alive();
}

function setPaperContent(list) {

    var $form = $("#paper-form");

    for (var i = 0; i < list.length; i++) {

        var option = list[i].options;
        var pre = "<div class=\"panel panel-default\"><div class=\"panel-body\"><label><xmp>" + (i + 1) + "、" + list[i].ask + "</xmp></label><span class='text-primary'>(" + list[i].score + " 分)</span>";
        var html = "";

        for (var j = 0; j < option.length; j++) {
            var op;

            if (list[i].type == 1) {
                op = "<div class='radio'><label><input type='radio' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "'><xmp>" + optionIndex.charAt(j) + "、" + option[j].content + "</xmp></label></div>";
            } else if (list[i].type == 2) {
                op = "<div class='checkbox'><label><input type='checkbox' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "'><xmp>" + optionIndex.charAt(j) + "、" + option[j].content + "</xmp></label></div>";
            }

            html = html + op;
        }

        var sur = "</div></div>";
        $form.append(pre + html + sur);
    }
}

function getPaperInfo() {

    paperId = window.location.hash.substring(1);
    $.get("../exam/detail.do?paperId=" + paperId, function (data) {
        if (data.error == 0) {
            paperInfo = data.data.paper;
            var list = data.data.paperQuestionVos;
            setPaperInfo(paperInfo);
            setPaperContent(list);
        } else if (data.error == 36) {
            alert("未到考试时间或考试已结束");
            window.open("exam.html", "_self");
        } else if (data.error == 38) {
            alert("你已经参加过考试了，请勿重复参加");
            window.open("exam.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
            window.history.go(-1);
        }
    });
}

$(function () {
    //获取当前登陆的用户信息
    $.get("info.do", function (data) {
        if (data.error == 0) {
            currentUser = data.data;
            $("#currentUser").html(currentUser.name);
            getPaperInfo();
        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }
    });

    $("#Submit").click(function () {
        var flag = confirm("确认交卷？");
        if (flag) {
            handOn();
        }

    });

});