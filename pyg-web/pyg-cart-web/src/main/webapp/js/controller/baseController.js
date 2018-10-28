/** 基础控制器 */
app.controller('baseController', function ($scope, $http) {
    /** 获取登录用户名 */
    $scope.loadUserName = function () {
        // 定义重定向
        $scope.redirectUrl = window.encodeURIComponent(location.href);
        // 获取登录用户名
        $http.get("/user/showName").then(function (response) {
            if (response.data) {
                $scope.loginName = response.data.loginName;
            }
        });
    }
});