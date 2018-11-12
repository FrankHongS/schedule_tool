$(
    function () {

        const main = {};

        const titleArray = [];
        window.title = {};

        const dataList=[
            [
                '张三',
                'v-zhangsan',
                '5',
                '15',
                '8',
                'edit'
            ],
            [
                '张三',
                'v-zhangsan',
                '5',
                '15',
                '8',
                'edit'
            ],
            [
                '张三',
                'v-zhangsan',
                '5',
                '15',
                '8',
                'edit'
            ],
            [
                '张三',
                'v-zhangsan',
                '5',
                '15',
                '8',
                'edit'
            ],
            [
                '张三',
                'v-zhangsan',
                '5',
                '15',
                '8',
                'edit'
            ],
            [
                '张三',
                'v-zhangsan',
                '5',
                '15',
                '8',
                'edit'
            ],
        ];
        // create sum table
        main.buildSumTable = function (dataList) {

            const rowGroup = ['name', 'alias','annual','leave', 'late', 'edit'];
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
                (row, index) => {
                    return $('<tr>')
                        .attr('name', titleArray[index])
                        .append(row);
                }
            );

            $('tbody').append(rowsArray);

            $('.name').bind('click', function (e) {
                // $(location).attr('href','../detail/detail.html?a='+e.target.innerHTML);//重定向跳转，在当前窗口打开新页面
                window.open('../detail/detail.html?title=' + $(this).parent('tr').attr('name')
                +'&from='+window.title.from+'&to='+window.title.to);//跳转时打开新窗口
            });

            $('.edit').bind('click', function (e) {

                const tempArray = $(this).parent('tr').attr('name').split(' , ');
                title.name = tempArray[0];
                title.alias = tempArray[1];
                title.employeeId = tempArray[2];

                layer.open({
                    type: 2,
                    title: '编辑考勤信息',
                    area: ['800px', '560px'],
                    fix: true, //不固定
                    maxmin: true,
                    scrollbar: false,//屏蔽父窗口滚动条
                    content: '../edit/edit.html'
                });
            });

        };

        main.bindClick = function () {

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

            $('.search-btn').bind('click', () => {

                const alias = $('.alias').val();
                const from = $('.from').val();
                const to = $('.to').val();

                title.from=0;
                title.to=0;

                let url;

                if (alias && !from && !to) {//only alias
                    url = '/schedule/sum/alias?alias=' + alias;
                } else if (!alias && !from && !to) {//neither alias nor range
                    url = '/schedule/sum';
                } else if (!alias && from && to) {//only range
                    url = '/schedule/sum/range?from=' + from + '&to=' + to;

                    title.from=from;
                    title.to=to;
                } else if (alias && from && to) {//both alias and range
                    url = '/schedule/sum/range_and_alias?from=' + from + '&to=' + to + '&alias=' + alias;

                    title.from=from;
                    title.to=to;
                }else{
                    $('.main-form-item .message').text('查找失败，时间范围需同为空或同不为空... :)');
                    return;
                }

                this.getRequest(url);
            });
        };

        main.getRequest = function (url) {
            $('.main-form-item .message').text('正在查找...');
            $.ajax({
                url: url,
                success: result => {
                    $('.main-form-item .message').text('查找成功');
                    $('tbody').html('');
                    this.buildSumTable(this.parseData(result.sum))
                },
                error: (xhr,e)=>{
                    $('.main-form-item .message').text('查找失败...'+e);
                }
            });
        };

        main.parseData = function (dataArray) {
            const list = [];
            for (let i = 0; i < dataArray.length; i++) {
                let item = dataArray[i];
                let listItem = [];


                listItem[0] = item.name + ' ' + item.alias;
                listItem[1] = item.leaveSum;
                listItem[2] = item.lateSum;
                listItem[3] = 'edit';

                titleArray[i] = item.name + ' , ' + item.alias + ' , ' + item.employeeId;

                list[i] = listItem;
            }

            return list;
        };

        main.bindLaydate = function () {
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

        main.buildPager=function(){
            layui.use(['laypage'],function(){
                const laypage=layui.laypage;
                laypage.render({
                    elem:'pager-container',
                    count: 50,
                    limit:6,
                    groups:10,
                    layout: ['count','prev', 'next', 'page'],
                    jump:obj=>{
                        console.log(obj);
                        const curr=obj.curr;
                    }
                });
            });
        };

        main.bindClick();
        main.bindLaydate();
        main.buildPager();
        main.buildSumTable(dataList);
    }
);