/** 定义控制器层 */
app.controller('goodsController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/goods/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/goods/" + url, $scope.entity)
            .then(function (response) {
                if (response.data) {
                    /** 重新加载数据 */
                    $scope.reload();
                } else {
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        /** 重新加载数据 */
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        } else {
            alert("请选择要删除的记录！");
        }
    };

    /** 定义商品状态数组 */
    $scope.status = ['未审核', '已审核', '审核未通过', '已驳回'];


    // 定义数组封装用户选择的id
    $scope.ids = [];
    // 为checkbox绑定点击事件
    $scope.updateSelection = function ($event, id) {
        // 如果是选中
        if ($event.target.checked) {
            // 就像ids中添加id
            $scope.ids.push(id);
        } else {
            // 得到该元素在数组中的索引号
            var idx = $scope.ids.indexOf(id);

            $scope.ids.splice(idx, 1);
        }

    };

    /** 审核商品,修改状态 */
    $scope.updateStatus = function (status) {
        // 判断是否有选择
        if ($scope.ids.length != 0) {
            // 调用服务层
            baseService.sendGet("/goods/updateStatus?status=" + status + "&ids=" + $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        alert("修改成功!");
                        // 重新加载数据
                        $scope.reload();
                        // 请求ids数据
                        $scope.ids = [];
                    } else {
                        alert("操作失败!");
                    }
                });

        } else {
            if (status == "1"){
                alert("请选择要审核的商品!");
            }else {
                alert("请选择要驳回的商品!");
            }

        }
    };

    /** 通过id删除该商品 */
    $scope.deleteById = function (goodId) {

        if ($scope.ids.length != 0){
            baseService.sendGet("/goods/deleteById?ids=" + $scope.ids)
                .then(function (response) {
                    if (response.data){
                        alert("删除成功!");
                        // 重新加载页面
                        $scope.reload();
                        // 请求ids里面的数据
                        $scope.ids = [];
                    }else {
                        alert("操作失败!");
                    }
                })
        }else {
            alert("请选择要删除的商品!");

        }

    }

});