//当前用户对象
var currentUser;
//当前试卷id
var paperId;
//试题列表
var questionList;
//试题选项列表
var options;

//选项索引
var optionIndex = "ABCDEFGHIJKRMNOPQRSTUVWXYZ";


//显示试卷
function setPaperContent(list) {

    var $form = $("#paper-form");

    for (var i = 0; i < list.length; i++) {

        var option = list[i].options;
        var code = "<div class=\"panel panel-default\"><div class=\"panel-body\"><label><xmp>" + (i + 1) + "、" + list[i].ask + "</xmp></label> <span class='text-primary'>(" + list[i].score + " 分)</span>";
        var html = "";

        for (var j = 0; j < option.length; j++) {
            var op;


            if (list[i].type == 1) {
                if (option[j].answer == 1) {
                    op = "<div class='radio'><label><input type='radio' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled checked><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";
                } else {
                    op = "<div class='radio'><label><input type='radio' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";
                }
            } else if (list[i].type == 2) {
                if (option[j].answer == 1) {
                    op = "<div class='checkbox'><label><input type='checkbox' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled checked><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";
                } else {
                    op = "<div class='checkbox'><label><input type='checkbox' id='" + option[j].id + "' name='" + list[i].id + "' value='" + option[j].id + "' disabled><xmp>" + optionIndex.charAt(j) + "、"  + option[j].content + "</xmp></label></div>";

                }
            }

            html = html + op;

        }
        var analysis = "<span class='glyphicon glyphicon-pushpin text-info'>解析：</span> <xmp>" + list[i].analysis + "</xmp>";
        var sur = "</div></div>";
        $form.append(code + html + analysis + sur);
    }

}

//获取答卷详情
function getPaperMark() {
    paperId = window.location.hash.substring(1);

    if (paperId != null) {

        $.get("../paper/questionList.do?paperId=" + paperId, function (data) {

            if (data.error == 0) {
                questionList = data.data;
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
    $.get("../../user/info.do", function (data) {
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