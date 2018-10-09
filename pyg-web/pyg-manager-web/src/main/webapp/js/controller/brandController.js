/** 品牌控制器层 */
app.controller("brandController", function($scope, $controller, baseService){
    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});
    /** 读取品牌数据绑定到表格中 */
    $scope.findAll = function(){
        /** 调用服务层查询所有品牌数据 */
        baseService.sendGet("/brand/findAll").then(function(response){
            $scope.dataList = response.data;
        });
    };
    /** 定义搜索对象 */
    $scope.searchEntity = {};
    /** 分页查询品牌信息 */
    $scope.search = function(page, rows){
        /** 调用服务层分页查询品牌数据 */
        baseService.findByPage("/brand/findByPage",page,
            rows,$scope.searchEntity)
            .then(function(response){
                $scope.dataList = response.data.rows;
                /** 更新总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };
    /** 添加与修改品牌 */
    $scope.saveOrUpdate = function(){
        /** 定义请求URL */
        var url = "save"; // 添加品牌
        if ($scope.entity.id){
            url = "update"; // 修改品牌
        }
        /** 调用服务层 */
        baseService.sendPost("/brand/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载品牌数据 */
                    $scope.reload();
                }else{
                    alert("操作失败");
                }
            });
    };
    /** 显示修改，为修改表单绑定当行数据 */
    $scope.show = function(entity){
        // 把entity的json对象转化成一个新的json对象
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };
    /** 批量删除品牌 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            /** 调用服务层 */
            baseService.deleteById("/brand/delete", $scope.ids).then(
                function(response){
                    if(response.data){
                        /** 重新加载品牌数据 */
                        $scope.reload();
                    }
                }
            );
        }else{
            alert("请选择要删除的品牌！");
        }
    };
});