$(
    function () {
        const editProgramEmployee = {};
        const originData = window.parent.originData;

        let isRequsetSuccess;

        editProgramEmployee.bindOriginData = function () {

            $('.program-name').val(originData.programName);

            switch (originData.type) {
                case 0:
                    $('#program-role')
                        .html('')
                        .append(
                            originData.roleNameArray.map(
                                (roleName, index) => {
                                    return $('<option>')
                                        .attr('value', originData.roleIdArray[index])
                                        .append(roleName)
                                }
                            )
                        );
                    this.buildEmployeeNames();
                    break;

                case 1:
                    $('.employee-weight').val(originData.ratio);
                    $('#employee-name')
                        .html('')
                        .append(
                            $('<option>')
                                .append(originData.name)
                        );
                    $('#program-role')
                        .html('')
                        .append(
                            originData.roleNameArray.map(
                                (roleName, index) => {
                                    if(originData.roleIdArray[index]==originData.curRoleId){
                                        return $('<option>')
                                            .attr('value', originData.roleIdArray[index])
                                            .append(roleName)
                                    }
                                }
                            )
                        );
                    break;

                default:
                    break;
            }
        };

        editProgramEmployee.buildEmployeeNames = function () {

            $.ajax({
                url: '/schedule/station_employee',
                success: result => {
                    if (result.code == 0) {

                        const employeeArray = result.data.employees;

                        const employeeItems = employeeArray.map(
                            employee => {
                                return $('<option>')
                                    .attr('value', employee.id)
                                    .append(employee.name);
                            }
                        );

                        $('#employee-name')
                            .html('')
                            .append(employeeItems);

                    }
                }
            });

        };

        editProgramEmployee.bindClick = function () {
            $('.save-btn').click(
                function () {
                    switch (originData.type) {
                        case 0:
                            {
                                const data = {
                                    employeeId: $('#employee-name').children('option:selected').val(),
                                    roleId: $('#program-role').children('option:selected').val(),
                                    ratio: $('.employee-weight').val()
                                }

                                editProgramEmployee.saveRequest(data);
                            }
                            break;
                        case 1:
                            {
                                const data = {
                                    employeeId: originData.employeeId,
                                    roleId: originData.curRoleId,
                                    ratio: $('.employee-weight').val()
                                }

                                editProgramEmployee.modifyRequest(data);
                            }
                            break;
                        default:
                            break;
                    }


                }
            );

            $('.cancel-btn').click(
                function () {
                    const index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);

                    if (isRequsetSuccess) {
                        window.parent.queryProgramEmployees(originData.curRoleId);
                    }
                }
            );
        };

        editProgramEmployee.saveRequest = function (data) {
            $('.message-container .message').text('正在保存...');
            $.ajax({
                url: '/schedule/role_employee/add',
                type: 'POST',
                data: data,
                success: result => {
                    if (result.code === 0) {
                        $('.message-container .message').text('保存成功');
                        isRequsetSuccess = true;
                    } else {
                        $('.message-container .message').text('保存失败...' + result.message);
                        isRequsetSuccess = false;
                    }
                }
            });
        };

        editProgramEmployee.modifyRequest = function (data) {
            $('.message-container .message').text('正在修改...');
            $.ajax({
                url: '/schedule/role_employee/update',
                type: 'POST',
                data: data,
                success: result => {
                    if (result.code === 0) {
                        $('.message-container .message').text('修改成功');
                        isRequsetSuccess = true;
                    } else {
                        $('.message-container .message').text('修改失败...' + result.message);
                        isRequsetSuccess = false;
                    }
                }
            });
        }

        editProgramEmployee.bindOriginData();
        editProgramEmployee.bindClick();
    }
);