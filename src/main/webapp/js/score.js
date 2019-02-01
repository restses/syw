//当前用户对象
var currentUser;
//当前试卷id
var paperId;
//试题列表
var questionList;
//用户答题的选项
var userAnswers;
//试题选项列表
var options;
//用户分数
var score;
//选项索引
var optionIndex = "ABCDEFGHIJKRMNOPQRSTUVWXYZ";

//显示用户的选项
function showUserAnswer() {

    for (var i = 0; i < userAnswers.length; i++) {
        $("#" + userAnswers[i]).prop('checked', true);
    }
}

//显示试卷
function setPaperContent(list) {
    $("#score").html(score);

    var $form = $("#paper-form");

    for (var i = 0; i < list.length; i++) {

        var option = list[i].options;
        var code = "<div class=\"panel panel-default\"><div class=\"panel-body\"><label><xmp>" + (i + 1) + "、" + list[i].ask + "</xmp></label> <span class='text-primary'>(" + list[i].score + " 分)</span>";
        var html = "";

        for (var j = 0; j < option.length; j++) {
            var op;


            if (list[i].type == 1) {
                if (option[j].answer == 1) {
                    op = "<div class='radio'><span class='glyphicon glyphicon-ok text-success'></span> <label><input type='radio' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";
                } else {
                    op = "<div class='radio'><span class='glyphicon glyphicon-remove text-danger'></span> <label><input type='radio' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";
                }
            } else if (list[i].type == 2) {
                if (option[j].answer == 1) {
                    op = "<div class='checkbox'><span class='glyphicon glyphicon-ok text-success'></span> <label><input type='checkbox' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";
                } else {
                    op = "<div class='checkbox'><span class='glyphicon glyphicon-remove text-danger'></span> <label><input type='checkbox' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";

                }
            }

            html = html + op;

        }
        var analysis = "<span class='glyphicon glyphicon-pushpin text-info'>解析：</span> <xmp style='color: red'>" + list[i].analysis + "</xmp>";
        var sur = "</div></div>";
        $form.append(code + html + analysis + sur);
    }

    showUserAnswer();
}

//获取答卷信息和成绩
function getPaperMark() {
    paperId = window.location.hash.substring(1);

    if (paperId != null) {

        $.get("../exam/score.do?paperId=" + paperId, function (data) {

            if (data.error == 0) {
                questionList = data.data.paperQuestionVos;
                userAnswers = data.data.checkedId;
                score = data.data.score;

                setPaperContent(questionList);

            } else {
                alert("错误代码：" + data.error + ",错误信息：" + data.msg);
            }
        });

    } else {
        alert("请求参数错误");
    }


}


//获取用户信息
function getCurrentUserInfo() {
    $.get("info.do", function (data) {
        if (data.error == 0) {
            currentUser = data.data;
            $("#currentUser").html(currentUser.name);

            getPaperMark();


        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }
    });
}


$(function () {

    getCurrentUserInfo();

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

});