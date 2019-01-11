$(
    function () {
        const programEmployee = {};

        const dataList = [
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

        programEmployee.getParameter = function () {
            programName = decodeURIComponent($.Request('program'));

            roleIdArray = decodeURIComponent($.Request('roleIds')).split(',');

            roleNameArray = decodeURIComponent($.Request('roleNames')).split(' , ');

            window.originData = {
                programName: programName,
                roleIdArray: roleIdArray,
                roleNameArray: roleNameArray,
            };

            // build progran-role select
            $('#program-role')
                .html('')
                .append(
                    roleNameArray.map(
                        (roleName, index) => {
                            return $('<option>')
                                .attr('value', roleIdArray[index])
                                .append(roleName)
                        }
                    )
                );
        };

        programEmployee.bindClick = function () {
            $('.add').click(function () {

                // save type
                window.originData.type = 0;
                window.originData.curRoleId=$('#program-role').children('option:selected').val();

                layer.open({
                    type: 2,
                    title: '添加节目人员',
                    area: ['450px', '360px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: './edit/edit.html'
                });
            });

            $('#program-role').bind('change', function () {
                const curRoleId = $('#program-role').children('option:selected').val();
                window.queryProgramEmployees(curRoleId);
            });

        };

        window.queryProgramEmployees = function (roleId) {

            if (!roleId) {
                roleId = roleIdArray[0];
            }

            $.ajax({
                url: '/arrange/role_employee?id=' + roleId,
                success: result => {
                    if (result.code === 0) {
                        curProgramEmployeeArray = result.data.employees;
                        programEmployee.buildEmployeeTable('.employee-body', programEmployee.parseData(curProgramEmployeeArray));
                    }
                }
            });
        };

        programEmployee.buildEmployeeTable = function (container, dataList) {
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

            $('.modify').click(function () {

                const curEmployee = curProgramEmployeeArray[$(this).parent('tr').index()];

                // modify type
                window.originData.type = 1;
                window.originData.employeeId = curEmployee.id;
                window.originData.name = curEmployee.name;
                window.originData.ratio = curEmployee.ratio;

                window.originData.curRoleId=$('#program-role').children('option:selected').val();

                layer.open({
                    type: 2,
                    title: '修改节目人员',
                    area: ['450px', '360px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: './edit/edit.html'
                });
            });

            $('.delete').click(function () {

                const curEmployee = curProgramEmployeeArray[$(this).parent('tr').index()];
                const curRoleId = $('#program-role').children('option:selected').val();

                if (confirm('确认删除')) {

                    $.ajax({
                        url: '/arrange/role_employee/delete?employeeId=' + curEmployee.id + '&roleId=' + curRoleId,
                        success: result => {
                            if (result.code === 0) {
                                alert('删除成功');
                                window.queryProgramEmployees(curRoleId);
                            } else {
                                alert('删除失败...' + result.message);
                            }
                        }
                    })


                }
            });
        };

        programEmployee.parseData = function (employees) {
            const dataList = [];

            for (let i = 0; i < employees.length; i++) {
                const item = [];
                item[0] = i + 1;
                item[1] = programName;
                item[2] = employees[i].name;
                item[3] = employees[i].ratio;
                item[4] = 'delete';
                item[5] = 'modify';

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