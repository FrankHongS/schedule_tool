$(
    function () {

        const main = {};
        main.buildSumTable = function (dataList) {

            const rowGroup = ['name', 'leave', 'late', 'homebase', 'edit'];
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
                window.open('../detail/detail.html?title=' + e.target.innerHTML);//跳转时打开新窗口
            });

            $('.edit').bind('click', function (e) {
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

        main.bindClick=function(){

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

            $('.search-btn').bind('click',function(e){
                $.ajax({
                    url:'/schedule/sum',
                    success:result=>{
                        $('tbody').html('');//clear old table
                        main.buildSumTable(main.parseData(result.sum))
                    }
                });
            });
        };

        main.bindLaydate=function(){
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

        main.parseData=function(dataArray){
            const list=[];
            for(let i=0;i<dataArray.length;i++){
                let item=dataArray[i];
                let listItem=[];

                listItem[0]=item.name+' '+item.alias;
                listItem[1]=item.leaveSum;
                listItem[2]=item.lateSum;
                listItem[3]=item.homebaseSum;
                listItem[4]='edit';

                list[i]=listItem;
            }

            console.table(list);

            return list;
        };

        main.bindClick();
        main.bindLaydate();
    }
);