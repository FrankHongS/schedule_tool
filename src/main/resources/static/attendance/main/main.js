$(
    function () {

        const main = {};

        main.buildUI = function () {
            const dataList = [
                [
                    'Joey',
                    15,
                    20,
                    15,
                    'edit'
                ],
                [
                    'Chandler',
                    15,
                    20,
                    15,
                    'edit'
                ],
                [
                    'Reachel',
                    15,
                    20,
                    15,
                    'edit'
                ]
            ];

            const rowGroup = ['name', 'leave', 'late', 'homebase', 'edit']
            const cellsArray = dataList.map(
                rowValues => {
                    return rowValues.map(
                        (cells, index) => {
                            return $('<td>')
                                .addClass(rowGroup[index % rowGroup.length])
                                .text(cells);
                        }
                    );
                }
            );

            const rowsArray = cellsArray.map(
                row => {
                    return $('<tr>')
                        .append(row);
                }
            );

            $('tbody').append(rowsArray);

            $('.name').bind('click', function (e) {
                // $(location).attr('href','../detail/detail.html?a='+e.target.innerHTML);//重定向跳转，在当前窗口打开新页面
                window.open('../detail/detail.html?a=' + e.target.innerHTML);//跳转时打开新窗口
            });

            $('.edit').bind('click', function (e) {
                layer.open({
                    type: 2,
                    title: 'hello world !',
                    area: ['800px', '560px'],
                    fix: false, //不固定
                    maxmin: true,
                    scrollbar: false,//屏蔽父窗口滚动条
                    content: '../edit/edit.html'
                });
            });

            $('.add-btn').bind('click', function (e) {
                layer.open({
                    type: 2,
                    title: '添加新成员',
                    area: ['450px', '400px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: '../add/add.html'
                });

            });

            laydate.render({
                elem: '.from',
                theme: '#393D49',
                btns: ['confirm'],
            });

            laydate.render({
                elem: '.to',
                theme: '#393D49',
                btns: ['confirm'],
            });

        };

        main.buildUI();
    }
);