var currentUser;
var questionCount;
var page = 1;
var pageCount;
var questionList;

//选中的试题列表
var examQuestionList = [];
//存放选中的试题的全部信息
var viewList = [];

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

function submitExam(json) {
    var $saving = $("#saving");
    $saving.show();
    $.ajax({
        type: "post",
        dataType: "json",
        url: "../paper/add.do",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(json),
        success: function (data) {

            if (data.error == 0) {
                alert("添加考试成功，请转自[考试管理]页添加考试人员");
                window.open("examManager.html", "_self");
            } else if (data.error == 10) {
                window.open("../index.html", "_self");
            } else {
                alert(data.error + "" + data.msg);
                $saving.hide();
            }
        }
    });
}

function saveExam() {

    var title = $("#title").val();
    var time = $("#time").val();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();

    var json = {};

    if (title.length > 0) {
        if (time.length > 0) {
            if (startTime.length > 0) {
                if (endTime.length > 0) {
                    if (examQuestionList.length > 0) {

                        var dateStart = new Date(startTime).getTime();
                        var dateEnd = new Date(endTime).getTime();

                        if (dateEnd - dateStart < 0) {
                            alert("结束时间不能早已开始时间");
                        }

                        json = {
                            "paper": {
                                "title": title,
                                "time": time,
                                "startTime": dateStart,
                                "endTime": dateEnd
                            },
                            "questions": examQuestionList
                        }

                        console.info(JSON.stringify(json));
                        submitExam(json);

                    } else {
                        alert("试题列表不能为空！");
                    }

                } else {
                    alert("结束时间不能为空");
                }
            } else {
                alert("开始时间不能为空");
            }
        } else {
            alert("答题时间不能为空");
        }
    } else {
        alert("考试名称不能为空");
    }
}

//保存试题，将试题从试题列表中选中并添加至预览表格中
function questionSave() {

    var $choseQuestion = $("#choseQuestion tbody");
    $choseQuestion.html("");

    $.each(viewList, function (index, item) {
        var html = "<tr id='tr-" + item.id + "'>" +
            "<td>" + item.id + "</td><td>" + item.type + "</td><td><xmp>" + item.ask + "</xmp></td><td><input type='text' name='score' value='" + item.score + "' class='form-control input-sm' questionId='" + item.id + "'></td><td><a href='javascript:removeFromList(" + item.id + ")'>删除</a></td>"
        "</tr>"

        $choseQuestion.append(html);
    });

    $("#questionList").modal('hide');

    $("input[name='score']").change(function () {

        var attr = $(this).attr("questionId");
        var score = $(this).val();

        $.each(examQuestionList, function (index, item) {
            if (item.questionId == attr) {
                item.score = score;
            }
        })

        console.info("score:" + attr);
        console.info(JSON.stringify(examQuestionList));
    });

}

function questionIsChecked() {
    $.each(questionList, function (i, question) {
        $.each(examQuestionList, function (j, item) {
            if (question.id == item.questionId) {
                $("#ql-" + question.id).prop("checked", true);
            }
        });
    });
}

function checkedListener() {

    $("input[name='question']").on('click', function () {

        if ($(this).prop('checked')) {
            addQuestionToList($(this).val());
        } else {
            removeFromList($(this).val())
        }

    });

}

function removeFromList(id) {

    var grep = $.grep(examQuestionList, function (item, index) {
        return item.questionId != id;
    });

    var newViewList = $.grep(viewList, function (item, index) {
        return item.id != id;
    });

    examQuestionList = grep;
    viewList = newViewList;

    $("#tr-" + id).remove();

}

function addQuestionToList(id) {

    var isContains = false;

    var question = {
        "questionId": id,
        "score": 1
    };

    var view = {
        "id": id,
        "type": $("#type-" + id).html(),
        "ask": $("#ask-" + id).html(),
        "score": 1
    }

    $.each(examQuestionList, function (index, item) {
        if (item.questionId == id) {
            isContains = true;
        }
    });

    if (!isContains) {
        examQuestionList.push(question);
        viewList.push(view);
    }

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

function selectAll() {

    $("#checkedAll").click(function () {

        var isChecked = $(this).prop("checked");

        if (isChecked) {

            $(":checkbox").prop("checked", true);

            var $checkbox = $("input[name='question']:checked");

            $.each($checkbox, function (index, item) {
                addQuestionToList($(item).val());
            });

            console.info(JSON.stringify(examQuestionList));

        } else {
            $(":checkbox").prop("checked", false);
            examQuestionList = [];
            viewList = [];
        }
    });

}

function searchQuestion() {
    page = 1;
    getQuestionList();
    getQuestionCount();

}

function setQuestionList() {

    $("#questionList tbody").html("");
    $("#checkedAll").prop("checked", false);

    $.each(questionList, function (index, item) {


        var td = " <tr id='tr" + item.id + "'>\n" +
            "                <td><input type=\"checkbox\" name='question' value='" + item.id + "' id='ql-" + item.id + "'></td>\n" +
            "                <td>" + item.id + "</td>\n" +
            "                <td id='type-" + item.id + "'>" + (item.type == 1 ? "单选题" : "多选题") + "</td>\n" +
            "                <td><xmp id='ask-" + item.id + "'>" + item.ask + "</xmp></td>\n" +
            "            </tr>";
        $("#questionList tbody").append(td);
    });


    $("#page").html(page);

    questionIsChecked();

}


function questionListReset() {
    $("#questionForm").resetForm();
    $("#questionList tbody").html("");
    $("#checkedAll").prop("checked", false);
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
        url: "../question/search.do?page=" + page,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(question),
        success: function (data) {

            if (data.error == 0) {
                questionList = data.data;
                $("#loading").hide();
                setQuestionList();
                checkedListener();
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
        url: "../question/count.do",
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
                alert(data.error + " " + data.msg);
            }
        }
    });

}


function addQuestion() {
    selectAll();
    questionListReset();
    $("#questionList").modal('show');
    getQuestionList();
    getQuestionCount();
}

$(function () {

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

    getUserInfo();
});