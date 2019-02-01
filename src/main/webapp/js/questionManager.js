var currentUser;
var questionCount;
var page = 1;
var pageCount;
var questionList;

var optionIndex = "ABCDEFGHIJKRMNOPQRSTUVWXYZ";


function setQuestionDetail(question) {

    var $modal = $("div[class='modal-body']");

    $modal.html("");

    var option = question.options;
    var pre = "<div class=\"panel panel-default\"><div class=\"panel-body\"><label><xmp>" + question.id + "、" + question.ask + "</xmp></label>";
    var html = "";

    for (var j = 0; j < option.length; j++) {
        var op;

        if (question.type == 1) {

            if (option[j].answer == 1) {
                op = "<div class='radio'><label><input type='radio' name='" + question.id + "' value='" + option[j].id + "' disabled checked><xmp>" + optionIndex.charAt(j) + "、" + option[j].content + "</xmp></label></div>";
            } else {
                op = "<div class='radio'><label><input type='radio' name='" + question.id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、" + option[j].content + "</xmp></label></div>";

            }
        } else if (question.type == 2) {
            if (option[j].answer == 1) {
                op = "<div class='checkbox'><label><input type='checkbox' name='" + question.id + "' value='" + option[j].id + "' disabled checked><xmp>" + optionIndex.charAt(j) + "、" + option[j].content + "</xmp></label></div>";
            } else {
                op = "<div class='checkbox'><label><input type='checkbox' name='" + question.id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、" + option[j].content + "</xmp></label></div>";
            }
        }

        html = html + op;
    }
    var analysis = "<span class='glyphicon glyphicon-pushpin text-info'>解析：</span> <xmp>" + question.analysis + "</xmp>";
    var sur = "</div></div>";

    $modal.append(pre + html + analysis + sur);
    $("#questionDetail").modal('show');
}


function getQuestionDetail(id) {

    var question = null;

    $.get("detail.do?id=" + id, function (data) {

        if (data.error == 0) {
            question = data.data;
            setQuestionDetail(question);
        } else if (data.error == 10) {
            window.open("../index.html", "_self");
        } else {
            alert(data.error + "" + data.msg);
        }

    });
}

function deleteQuestion(id) {

    var b = confirm("确认删除？");

    if (b) {
        $("#delete-ing").show();
        $.get("delete.do?questionId=" + id, function (data) {
            if (data.error == 0) {
                $("#delete-ing").hide();
                $("#tr" + id).remove();
                $("#delete-success").show().delay(2000).hide(0);
                console.info("delete success");
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                $("#delete-ing").hide();
                $("#delete-fail").show().delay(2000).hide(0);
                alert(data.error + "" + data.msg);
            }
        })
    }
}

function searchQuestion() {
    page = 1;
    getQuestionList();
    getQuestionCount();

}

function pagePrevious() {

    if (page > 1) {
        page--;
        getQuestionList();
    }

}

function pageNext() {

    if (page < pageCount) {
        page++;
        getQuestionList();
    }
}

function setQuestionList() {

    $("tbody").html("");

    $.each(questionList, function (index, item) {

        var createTime = new Date(item.createTime);
        var updateTime = new Date(item.updateTime);

        var td = " <tr id='tr" + item.id + "'>\n" +
            "                <td><input type=\"checkbox\"></td>\n" +
            "                <td>" + item.id + "</td>\n" +
            "                <td>" + (item.type == 1 ? "单选题" : "多选题") + "</td>\n" +
            "                <td><xmp>" + item.ask + "</xmp></td>\n" +
            "                <td>" + createTime.toLocaleDateString() + "</td>\n" +
            "                <td>" + updateTime.toLocaleDateString() + "</td>\n" +
            "                <td><a href=\"javascript:getQuestionDetail(" + item.id + ");\">查看</a>" +
            "                    <a href=\"javascript:deleteQuestion(" + item.id + ");\">删除</a></td>\n" +
            "            </tr>";
        $("tbody").append(td)
    });


    $("#page").html(page);
}

function getQuestionList() {

    var question = {
        "id": $("#questionId").val(),
        "type": $("#questionType").val(),
        "ask": $("#questionContent").val()
    }

    $.ajax({
        type: "post",
        dataType: "json",
        url: "search.do?page=" + page,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(question),
        success: function (data) {

            if (data.error == 0) {
                questionList = data.data;
                setQuestionList();
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + "" + data.msg);
            }
        }
    });


}

function getQuestionCount() {

    var question = {
        "id": $("#questionId").val(),
        "type": $("#questionType").val(),
        "ask": $("#questionContent").val()
    }

    $.ajax({
        type: "post",
        dataType: "json",
        url: "count.do",
        data: JSON.stringify(question),
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
                alert(data.error + "" + data.msg);
            }
        }
    });

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

$(function () {

    getUserInfo();
    getQuestionCount();
    getQuestionList();

    $("#checkedAll").click(function () {
        var flag = $(this).prop("checked");
        if (flag) {
            $(":checkbox").prop("checked", true);
        } else {
            $(":checkbox").prop("checked", false);
        }
    });

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