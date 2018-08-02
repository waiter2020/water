/*
    @auther randall Hu
    @time 2018-7-14 10:33
    密码修改页面专业 js
    1.原密码与新密码的否同
    2.新密码1 与 新密码2 的否同
    3.修改密码后传出信息的提醒

 */

    //3.
function sereAlert(msg) {
    if(msg!=null){
    alert(msg);
    }
}
    //2
function pwdMeet1() {
    // var op = document.getElementById("oldPwd").value;
    // var np1 = document.getElementById("newPdw1").value;
    if(document.getElementById("oldPwd").value==document.getElementById("newPdw1").value){
        document.getElementById("meet1").innerText="新密码与旧密码不能相同";
    }
}
function pwdMeet2() {
    // var np1 = document.getElementById("newPdw1").value;
    // var np2 = document.getElementById("newPdw2").value;
    if (document.getElementById("newPdw1").value!=document.getElementById("newPdw2").value){
        document.getElementById("meet2").innerText="两次新密码输入必须一致";
    }
    else{
        document.getElementById("meet2").innerText="";
    }
}
