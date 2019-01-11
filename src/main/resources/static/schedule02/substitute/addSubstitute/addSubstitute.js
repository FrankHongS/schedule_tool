$(
    function () {

        const addSubstitute = {};

        const curProgramArray=[];
        let curEmployeeArray;

        addSubstitute.buildProgramList = function () {
            $.ajax({
                url: '/arrange/sprogram/programs?stationId=' + 1,
                success: result => {
                    if (result.code == 0) {

                        const roleArray = [];

                        result.data.programs.map(
                            program => {
                                program.programRoles.map(
                                    role => {
                                        curProgramArray.push(role);
                                        roleArray.push({
                                            programName: program.name,
                                            roleName: role.name
                                        });
                                    }
                                );
                            }
                        );

                        const rowsArray = roleArray.map(
                            (role, index) => {
                                return $('<option>')
                                    .val(index)
                                    .append(role.programName + '(' + role.roleName + ')');
                            }
                        )

                        $('select#sub-program').append(rowsArray);
                    } else {
                        console.log(result);
                    }
                }
            });
        };

        addSubstitute.bindEmployeeList = function () {
            $.ajax({
                url: '/arrange/station_employee',
                success: result => {
                    if (result.code == 0) {
                        curEmployeeArray=result.data.employees;
                        const rowsArray = curEmployeeArray.map(
                            (employee, index) => {
                                return $('<option>')
                                    .val(index)
                                    .append(employee.name + '(' + employee.alias + ')');
                            }
                        )

                        $('select#sub-name').append(rowsArray);
                    } else {
                        console.log(result);
                    }
                }
            });
        };

        addSubstitute.bindClick = function () {
            $('.save-btn').on('click', function () {

                const selectedProgram=$('#sub-program').children('option:selected').val();
                const selectedEmployee=$('#sub-name').children('option:selected').val();
                const curProgram=curProgramArray[selectedProgram];
                const curEmployee=curEmployeeArray[selectedEmployee];

                if($('#sub-hol:checked').length == 1){
                    $.ajax({
                        url: '/arrange/schedule/add_holiday',
                        type: 'POST',
                        data: {
                            roleId:curProgram.id,
                            date: $('.sub-date').val(),
                            employeeId:curEmployee.id
                        },
                        success: result => {
                            if (result.code == 0) {
                                $('.message').text('保存成功');
                            } else {
                                $('.message').text('保存失败...' + result.message);
                                console.log(result)
                            }
                        }
                    });
                }else{
                    $.ajax({
                        url: '/arrange/replace/add',
                        type: 'POST',
                        data: {
                            roleId:curProgram.id,
                            date: $('.sub-date').val(),
                            employeeId:curEmployee.id
                        },
                        success: result => {
                            if (result.code == 0) {
                                $('.message').text('保存成功');
                            } else {
                                $('.message').text('保存失败...' + result.message);
                                console.log(result)
                            }
                        }
                    });
                }
            });

            $('.cancel-btn').bind('click', function () {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            });
        };

        addSubstitute.bindLayer = function () {
            laydate.render({
                elem: '.sub-date',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
        };

        addSubstitute.buildProgramList();
        addSubstitute.bindEmployeeList();
        addSubstitute.bindClick();
        addSubstitute.bindLayer();

    }
);