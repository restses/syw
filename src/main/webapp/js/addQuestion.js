var optionIndex = "ABCDEFGHIJKRMNOPQRSTUVWXYZ";
var optionSize = 0;
var question = {};
var questionType = 1;
var currentUser;

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

function switchType() {

    $("#question-type").change(function () {

        questionType = $(this).val();

        if (questionType == 2) {

            $(":radio").each(function () {
                $(this).attr("type", "checkbox");
            });

        } else if (questionType == 1) {

            $(":checkbox").each(function () {
                $(this).attr("type", "radio");
            });
        }

    });


}

function formCheck() {

    var ask = $("#ask").val();

    var answers = $(":radio:checked,:checkbox:checked");
    var options = $("textarea[name='content']");

    if (ask.length <= 0) {
        alert("试题描述不能为空");
        return false;
    }

    if (answers.length <= 0) {
        alert("试题答案不能为空");
        return false;
    }

    options.each(function (index, item) {
        if ($(item).val().length <= 0) {
            alert("选项内容不能为空");
            return false;
        }
    });

    return true;
}

function questionReset() {
    $("#question-form").resetForm();
    $("#analysis").val("");

}

function option2json() {

    var type = $("select[name='type']").val();
    var ask = $("#ask").val();
    var options = $("textarea[name='content']");
    var answers = $(":radio:checked,:checkbox:checked");
    var list = [];
    var analysis = $("textarea[name='analysis']").val();

    options.each(function (index, item) {

        var object = {
            "content": $(item).val(),
            "answer": 0
        }

        list.push(object)
    });

    answers.each(function (index, item) {
        list[$(item).val()].answer = 1;
    });

    var json = {
        "type": type,
        "ask": ask,
        "options": list,
        "analysis": analysis
    }

    return json;
}

function addQuestion() {

    if (!formCheck()) {
        return;
    }

    var question = option2json();


    $.ajax({
        type: "post",
        dataType: "json",
        url: "add.do",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(question),
        success: function (data) {
            if (data.error == 0) {
                alert("添加试题成功");
                questionReset();
            } else {
                alert(data.error + " " + data.msg);
            }
        }
    });
}

function removeOption() {
    if (optionSize <= 1) {
        alert("至少保留一个选项");
        return;
    }

    $("#" + (--optionSize)).remove();
}

function addOption(index) {
    if (index > optionIndex.length - 1) {
        alert("超出支持的最大长度");
        return;
    }


    var option = " <div class=\"form-group\" id=" + index + ">\n" +
        "                        <label><span>" + optionIndex.charAt(index) + "</span>.是否为答案？<input type=\"radio\" name='answer' value='" + index + "'/></label>\n" +
        "                        <span class=\"glyphicon glyphicon-remove text-danger\" style=\"cursor:pointer\" onclick='javascript:removeOption()'></span>\n" +
        "                        <textarea name='content' class=\"form-control\" rows=\"2\"></textarea>\n" +
        "                    </div>";

    $("#question-form").append(option);

    if (questionType == 2) {

        $(":radio").each(function () {
            $(this).attr("type", "checkbox");
        });

    } else if (questionType == 1) {

        $(":checkbox").each(function () {
            $(this).attr("type", "radio");
        });
    }
}

$(function () {

    getUserInfo();

    //默认添加四个选项
    for (var i = 0; i < 4; i++) {
        addOption(i);
        optionSize++;
    }

    $("#addOption").click(function () {
        addOption(optionSize++);
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


    switchType();
});