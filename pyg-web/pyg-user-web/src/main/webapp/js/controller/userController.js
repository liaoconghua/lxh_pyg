/** 定义控制器层 */
app.controller('userController', function($scope, baseService){

    // 定义用户对象
    $scope.user = {};

    // 定义保存用户方法
    $scope.save = function () {
        //  判断两次密码是否一致
        if ($scope.user.password != $scope.password){
            alert("两次密码不一致,请重新输入!");
            return;
        }
        baseService.sendPost("/user/save?smsCode=" + $scope.smsCode, $scope.user).then(function (response) {
            if (response.data){
                alert("注册成功!");
                $scope.user = {};
                $scope.password = "";
                $scope.smsCode = "";
            }else {
                alert("注册失败!");
            }
        });
    };

    $scope.sendCode = function () {
        if ($scope.user.phone && /^1[3|4|5|6|7|8|9]\d{9}$/.test($scope.user.phone)){
            baseService.sendGet("/user/sendCode?phone=" + $scope.user.phone).then(function (response) {
                alert(response.data ? "验证码已发送！" : "验证码发送失败！");
            });
        }else {
            alert("请输入手机号码!");
        }
    }
});