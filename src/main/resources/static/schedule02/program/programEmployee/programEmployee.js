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
        let roleIdArray;

        let curProgramEmployeeArray;

        programEmployee.getParameter=function(){
            programName=decodeURIComponent($.Request('program'));

            roleIdArray=decodeURIComponent($.Request('roleIds')).split(',');
            roleIdArray.splice(roleIdArray.length-1,1);

            window.originData={
                programName:programName,
                roleIdArray:roleIdArray
            };
        };

        programEmployee.bindClick=function(){
            $('.add').click(function(){

                // save type
                window.originData.type=0;

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

        };

        window.queryProgramEmployees=function(){
            $.ajax({
                url:'/schedule/role_employee?id='+roleIdArray[0],
                success:result=>{
                    if(result.code===0){
                        curProgramEmployeeArray=result.data.employees;
                        programEmployee.buildEmployeeTable('.employee-body',programEmployee.parseData(curProgramEmployeeArray));
                    }
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

            $(container).html('')
                        .append(rowsArray);

            $('.modify').click(function(){

                const curEmployee=curProgramEmployeeArray[$(this).parent('tr').index()];

                // modify type
                window.originData.type=1;
                window.originData.employeeId=curEmployee.id;
                window.originData.name=curEmployee.name;
                window.originData.ratio=curEmployee.ratio;

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
                
                const curEmployee=curProgramEmployeeArray[$(this).parent('tr').index()];

                if(confirm('确认删除')){

                    roleIdArray.map(
                        (roleId,index)=>{
                            setTimeout(
                                ()=>{
                                    $.ajax({
                                        url:'/schedule/role_employee/delete?employeeId='+curEmployee.id+'&roleId='+roleId,
                                        success:result=>{
                                            if(result.code===0){
                                                if(index===roleIdArray.length-1){
                                                    alert('删除成功');
                                                    window.queryProgramEmployees();
                                                }
                                            }else{
                                                if(index===roleIdArray.length-1){
                                                    alert('删除失败...'+result.message);
                                                }
                                                console.log('删除失败...'+result.message);
                                            }
                                        }
                                    })
                                },100
                            );
                        }
                    );
                    
                    
                }
            });
        };

        programEmployee.parseData=function(employees){
            const dataList=[];

            for(let i=0;i<employees.length;i++){
                const item=[];
                item[0]=i+1;
                item[1]=programName;
                item[2]=employees[i].name;
                item[3]=employees[i].ratio;
                item[4]='delete';
                item[5]='modify';

                dataList.push(item);
            }

            return dataList;
        };

        programEmployee.getParameter();
        // programEmployee.buildEmployeeTable('.employee-body',dataList);
        window.queryProgramEmployees();
        programEmployee.bindClick();
    }
);