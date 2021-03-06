$(
    function () {

        const main = {};

        const size = 6;// the count of one page to show data
        let count;
        let isFirstTime=true;//防止分页跳转处第一页加载两次以及是否重新搜索的标志位

        const titleArray = [];
        window.title = {};

        // create sum table
        main.buildSumTable = function (dataList,isPage) {

            if(isPage==true&&dataList.length<size){
                const placeholderArray=['','','','','',''];//行数不够6行时，用空格占位

                for(let i=dataList.length;i<size;i++){
                    dataList.push(placeholderArray);
                }
            }
            const rowGroup = ['name', 'alias', 'annual', 'leave', 'late', 'edit'];

            
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
                window.open('../detail/detail.html?profile=' + $(this).parent('tr').attr('name')
                    + '&from=' + window.title.from + '&to=' + window.title.to);//跳转时打开新窗口
            });

            $('.edit').bind('click', function (e) {

                const tempArray = $(this).parent('tr').attr('name').split(' , ');
                title.name = tempArray[0];
                title.alias = tempArray[1];
                title.employeeId = tempArray[3];

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

            $('.year-link').bind('click',function(e){
                const year=$('.year').val();
                if(year==''){
                    alert('年份不能为空！');
                    return;
                }

                $('.year-link').attr('href','/schedule/excel/export_year?year='+year);
            });

            $('.month-link').bind('click',function(e){
                const month=$('.month').val();
                if(month==''){
                    alert('月份不能为空！');
                    return;
                }

                $('.month-link').attr('href','/schedule/excel/export_month?month='+month);
            });

            $('.search-btn').bind('click', () => {

                const alias = $('.alias').val();
                const name=$('.search-name').val();
                const from = $('.from').val();
                const to = $('.to').val();

                isFirstTime=true;

                title.from = 0;
                title.to = 0;

                let url;

                if(name&&!from&&!to){
                    url = '/schedule/sum/name?name=' + name;
                    this.getRequest(url);
                    return;
                }else if(name&&from&&to){
                    url = '/schedule/sum/range_and_name?from=' + from + '&to=' + to + '&name=' + name;

                    title.from = from;
                    title.to = to;
                    this.getRequest(url);
                    return;
                }

                if (alias && !from && !to) {//only alias
                    url = '/schedule/sum/alias?alias=' + alias;
                } else if (!alias &&!name&& !from && !to) {//neither alias nor range
                    url = '/schedule/sum?page=0&size=' + size;
                    this.getRequest(url,false);
                    return;
                } else if (!alias && from && to) {//only range
                    url = '/schedule/sum/range?page=0&size=' + size + '&from=' + from + '&to=' + to;

                    title.from = from;
                    title.to = to;
                    this.getRequest(url,true);
                    return;
                } else if (alias && from && to) {//both alias and range
                    url = '/schedule/sum/range_and_alias?from=' + from + '&to=' + to + '&alias=' + alias;

                    title.from = from;
                    title.to = to;
                } else {
                    alert('时间范围需同为空或同不为空... :)');
                    return;
                }

                this.getRequest(url);
            });
        };

        main.getRequest = function (url,isRange) {
            $('.main-form-item .message').text('正在查找...');
            $.ajax({
                url: url,
                success: result => {
                    $('.main-form-item .message').text('查找成功');
                    $('tbody').html('');

                    if (result.sum.dataList) {
                        this.buildSumTable(this.parseData(result.sum.dataList),true);

                        count=result.sum.count;//将总条数赋值给count

                        if(isFirstTime==true||$('#pager-container').is(':hidden')){
                            this.buildPager(isRange);
                        }
                    } else {
                        this.buildSumTable(this.parseData(result.sum),false);
                        $('#pager-container').hide();
                    }
                },
                error: (xhr, e) => {
                    $('.main-form-item .message').text('查找失败...' + e);
                }
            });
        };

        main.parseData = function (dataArray) {
            const list = [];
            for (let i = 0; i < dataArray.length; i++) {
                let item = dataArray[i];
                let listItem = [];


                listItem[0] = item.name;
                listItem[1] = item.alias;
                listItem[2] = item.annualCount;
                listItem[3] = item.leaveSum;
                listItem[4] = item.lateSum;
                listItem[5] = 'edit';

                titleArray[i] = item.name + ' , ' + item.alias +' , ' +item.annualCount+' , ' + item.employeeId;

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

            // bind year selector
            laydate.render({
                elem: '.year',
                theme: '#393D49',
                type: 'year',
                btns: ['confirm']
            });

            // bind month selector
            laydate.render({
                elem: '.month',
                theme: '#393D49',
                type: 'month',
                btns: ['confirm']
            });

        };

        /**
         * count: 总条数
         * limit: 每一页显示条数
         * range: 搜索时是否填写日期范围
         */
        main.buildPager = function (range) {
            $('#pager-container').show();

            layui.use(['laypage'], () =>{
                const laypage = layui.laypage;
                laypage.render({
                    elem: 'pager-container',
                    count: count,
                    limit: size,
                    groups: 10,
                    layout: ['count', 'prev', 'next', 'page'],
                    jump: obj => {

                        if(isFirstTime==true){
                            isFirstTime=false;
                            return;
                        }

                        const curr = obj.curr;
                        let url;
                        if (range == true) {
                            url = '/schedule/sum/range?page=' + (curr-1) + '&size=' + size + '&from=' + title.from + '&to=' + title.to;
                        } else {
                            url = '/schedule/sum?page=' + (curr-1) + '&size=' + size;
                        }

                        this.getRequest(url);
                    }
                });

            });
        };

        main.buildUI=function(){
            $('#pager-container').hide();

            // $('.export-year-btn').after($('<a>').addClass('export-year-link').attr('href','hahlfh'));
        };

        main.bindClick();
        main.bindLaydate();
        main.buildUI();
    }
);