/** 定义首页控制器层 */
app.controller("indexController", function ($scope, $controller, baseService) {
    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    $scope.findContentByCategoryId = function (categoryId) {
        baseService.sendGet("/content/findContentByCategoryId?categoryId=" + categoryId)
            .then(function (response) {
                if (response.data) {
                    $scope.contentList = response.data;
                } else {
                    alert("网络异常...加载失败!");
                }
            });
    };

    $scope.search = function () {
        var keyword = $scope.keywords ? $scope.keywords : '';
        location.href = "http://search.pinyougou.com?keywords=" + keyword;
    };



});