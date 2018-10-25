/** 商品详细页（控制器）*/
app.controller('itemController', function ($scope) {
        /** 定义购买数量操作方法 */
        $scope.addNum = function (k) {
            $scope.num += k;
            if ($scope.num < 1) {
                $scope.num = 1;
            }
        };
        // 监控num变量发生改变
        $scope.$watch('num', function (newVal, oldVal) {
            if (newVal < 1) {
                $scope.num = 1;
            }
        });

        // 记录用户选择的规格选项
        $scope.specItems = {};
        // 用户选择规格的点击事件
        $scope.selectSpec = function (key, value) {
            $scope.specItems[key] = value;
            /** 查询对应的SKU商品 */
            searchSku();
        };

        // 判断某个规格选项是否被选中
        $scope.isSelected = function (name, value) {
            return $scope.specItems[name] == value;
        };

        $scope.loadSku = function () {
            // 取第一个SKU
            $scope.sku = itemList[0];
            // 获取SKU商品选择的选项规格
            $scope.specItems = JSON.parse($scope.sku.spec); // 需要将{"网络":"联通4G","机身内存":"64G"}转成json数据
        };

        /** 根据用户选中的规格选项,查询对应的SKU商品 */
        var searchSku = function () {
            for (var i = 0; i < itemList.length; i++){
                /** 判断规格选项是不是当前用户选中的 */
                if (itemList[i].spec == JSON.stringify($scope.specItems)){
                    $scope.sku = itemList[i];
                    return;
                }
            }
        };

        /** 加入购物车事件绑定 */
        $scope.addToCart = function () {
            alert('skuId: ' + $scope.sku.id);
        }
    });