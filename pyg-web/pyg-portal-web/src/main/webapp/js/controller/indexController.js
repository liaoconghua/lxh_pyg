/** 定义首页控制器层 */
app.controller("indexController", function($scope, baseService){

    $scope.findContentByCategoryId = function (categoryId) {
        baseService.sendGet("/content/findContentByCategoryId?categoryId=" + categoryId)
            .then(function (response) {
                if (response.data) {
                    $scope.contentList = response.data;
                } else {
                    alert("网络异常...加载失败!");
                }
            });

    }
});