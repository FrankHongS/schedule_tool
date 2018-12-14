$(
    function(){
        const programEmployee={};

        const dataList=[
            [
                1,
                '今日十万加',
                '王洛一',
                2,
                'delete',
                'modify'
            ],
            [
                2,
                '今日十万加',
                '王洛一',
                2,
                'delete',
                'modify'
            ],
            [
                3,
                '今日十万加',
                '王洛一',
                2,
                'delete',
                'modify'
            ],
            [
                4,
                '今日十万加',
                '王洛一',
                2,
                'delete',
                'modify'
            ],
            [
                5,
                '今日十万加',
                '王洛一',
                2,
                'delete',
                'modify'
            ]
        ];

        let programName;

        programEmployee.getParameter=function(){
            programName=decodeURIComponent($.Request('program'));
        };

        programEmployee.bindClick=function(){
            $('.add').click(function(){
                layer.open({
                    type: 2,
                    title: '添加节目人员',
                    area: ['400px', '290px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: './edit/edit.html'
                });
            });

            $('.modify').click(function(){
                layer.open({
                    type: 2,
                    title: '修改节目人员',
                    area: ['400px', '290px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: './edit/edit.html'
                });
            });

            $('.delete').click(function(){
                if(confirm('确认删除')){

                    alert('删除成功');
                }
            });
        };

        programEmployee.buildEmployeeTable=function(container,dataList){
            const rowGroup = ['index', 'program', 'employee', 'weight', 'delete', 'modify'];

            
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
                        .append(row);
                }
            );

            $(container).append(rowsArray);
        };

        programEmployee.getParameter();
        programEmployee.buildEmployeeTable('.employee-body',dataList);
        programEmployee.bindClick();
    }
);