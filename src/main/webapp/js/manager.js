var currentUser;


function getUserInfo() {

    $.get("../user/info.do", function (data) {
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

    $("#logout").click(function () {

        var flag = confirm("确认退出？");

        if (flag) {
            $.get("../user/logout.do", function (data) {
                if (data.error == 0) {
                    window.open("index.html", "_self");
                } else {
                    alert(data.msg);
                }
            });
        }
    });

});