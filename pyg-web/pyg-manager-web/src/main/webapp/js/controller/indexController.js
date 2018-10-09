// 后台首页的控制器
app.controller("indexController", function ($scope, baseService) {

    // 获取登录用户名
    $scope.showLoginName = function () {
        baseService.sendGet("/showLoginName").then(function (response) {
            // 获取响应的名字
            $scope.loginName = response.data.loginName;
        })
    }
});