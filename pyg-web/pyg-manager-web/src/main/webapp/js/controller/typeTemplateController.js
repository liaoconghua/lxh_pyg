/** 定义控制器层 */
app.controller('typeTemplateController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 定义搜索对象 */
    $scope.searchEntity = {};
    /** 分页查询类型模版信息 */
    $scope.search = function(page, rows){
        baseService.findByPage("/typeTemplate/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        var hine = "保存成功!";
        if ($scope.entity.id){
            url = "update";
            hine = "修改成功!";
        }
        /** 发送post请求 */
        baseService.sendPost("/typeTemplate/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载数据 */
                    alert(hine);
                    $scope.reload();
                }else{
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/typeTemplate/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };

    /** 品牌列表 */
    $scope.brandList = {};

    /** 品牌列表 */
    $scope.findBrandList = function () {
        baseService.sendGet("/brand/findBrandList").then(function (response) {
            $scope.brandList = {data:response.data};
        })

    };

    /** 规格列表 */
    $scope.specificontion = {};

    $scope.findSpecificationList = function () {
        baseService.sendGet("/specification/findSpecificationList").then(function (response) {
            $scope.specificontion = {data:response.data};
        })

    };

    /** 新增扩展属性 */
    $scope.addButton = function () {
        $scope.entity.customAttributeItems.push({});
    };

    /** 删除扩展属性 */
    $scope.delButton = function (index) {
        $scope.entity.customAttributeItems.splice(index,1);
    }

    /** 显示修改 */
    $scope.show = function (entity) {
        $scope.entity = JSON.parse(JSON.stringify(entity));
        /** 转换品牌列表 */
        $scope.entity.brandIds = JSON.parse(entity.brandIds);
        /** 转换规格列表 */
        $scope.entity.specIds = JSON.parse(entity.specIds);
        /** 转换扩展属性 */
        $scope.entity.customAttributeItems = JSON.parse(entity.customAttributeItems);
    }

    /** 根据模版id删除 */
    $scope.deleteSpec = function () {

    }

});