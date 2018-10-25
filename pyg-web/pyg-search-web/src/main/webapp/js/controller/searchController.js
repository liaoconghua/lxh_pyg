/** 定义搜索控制器 */
app.controller("searchController", function ($scope, $sce, baseService) {

    $scope.searchParam = {
        keywords: '', category: '', brand: '',
        price: '', spec: {},
        page : 1, rows : 20
    };

    $scope.search = function (key) {
        baseService.sendPost("/Search", $scope.searchParam)
            .then(function (response) {
                if (response.data) {
                    $scope.resultMap = response.data;

                    /** 调用初始化页码方法 */
                    initPageNum();
                }
            });
    };

    // 将文本转化成html
    $scope.trustHtml = function (html) {
        return $sce.trustAsHtml(html);
    };

    // 定义装页面条件数据
    $scope.addSearchItem = function (key, value) {
        // 判断如果是商品分类,品牌,价格
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchParam[key] = value;
        } else { // 规格
            $scope.searchParam.spec[key] = value;
        }
        $scope.search();
    };

    // 删除搜索选项
    $scope.removeSearchParam = function (key) {
        // 判断如果是商品分类,品牌,价格
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchParam[key] = "";
        } else { // 规格
            delete $scope.searchParam.spec[key];
        }
        $scope.search();
    }

    /** 定义初始化页码方法 */
    var initPageNum = function () {
        // 定义页码数组
        $scope.pageNums = [];
        // 获取总页码
        var totalPages = $scope.resultMap.totalPages;
        // 开始页码
        var firstPage = 1;
        // 结束页码
        var lastPage = totalPages;

        /** 前面有点 */
        $scope.firstDot = true;
        /** 后面有点 */
        $scope.lastDot = true;

        // 如果总页数大于5 , 显示部分页码
        if (totalPages > 5){
            // 如果当页码处于前面位置
            if ($scope.searchParam.page <= 3){
                lastPage = 5; // 生成前五页页码
                $scope.firstDot = false; // 前面没点
            }else if ($scope.searchParam.page >= totalPages - 3){
                firstPage = totalPages - 4; // 生成后5页页码
                $scope.lastDot  = false; // 后面没点
            }else { // 当前页码处于中间位置
                firstPage = $scope.searchParam.page - 2;
                lastPage = $scope.searchParam.page + 2;
            }
        }else {
            $scope.firstDot = false; // 前面没点
            $scope.lastDot = false; // 后面没点
        }

        /** 循环产生页码 */
        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }

        /** 根据分页搜索方法 */
        $scope.pageSearch = function (page) {
            page = parseInt(page);
            /** 页码验证 */
            // 判断是当前页要大于1 且 需要小于等于 最大页 且不能等于 当前页
            if (page >= 1 && page <= $scope.resultMap.totalPages
                && page != $scope.searchParam.page){
                $scope.searchParam.page = page;
                $scope.search();
            }
        }

    }
});
