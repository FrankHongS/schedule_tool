$(
    function () {
        const editProgramEmployee = {};
        const originData = window.parent.originData;

        let isRequsetSuccess;

        editProgramEmployee.bindOriginData = function () {

            $('.program-name').val(originData.programName);

            switch (originData.type) {
                case 0:

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
                            originData.roleIdArray.map(
                                roleId => {
                                    const data = {
                                        employeeId: $('#employee-name').children('option:selected').val(),
                                        roleId: roleId,
                                        ratio: $('.employee-weight').val()
                                    }

                                    editProgramEmployee.saveRequest(data);
                                }
                            );
                            break;
                        case 1:
                            originData.roleIdArray.map(
                                roleId => {
                                    const data = {
                                        employeeId: originData.employeeId,
                                        roleId: roleId,
                                        ratio: $('.employee-weight').val()
                                    }

                                    editProgramEmployee.modifyRequest(data);
                                }
                            );
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

                    if(isRequsetSuccess){
                        window.parent.queryProgramEmployees();
                    }
                }
            );
        };

        editProgramEmployee.saveRequest = function (data) {
            $.ajax({
                url: '/schedule/role_employee/add',
                type: 'POST',
                data: data,
                success: result => {
                    if (result.code === 0) {
                        $('.message-container .message').text('保存成功');
                        isRequsetSuccess=true;
                    } else {
                        $('.message-container .message').text('保存失败...' + result.message);
                        isRequsetSuccess=false;
                    }
                }
            });
        };

        editProgramEmployee.modifyRequest = function (data) {
            $.ajax({
                url: '/schedule/role_employee/update',
                type: 'POST',
                data: data,
                success: result => {
                    if (result.code === 0) {
                        $('.message-container .message').text('修改成功');
                        isRequsetSuccess=true;
                    } else {
                        $('.message-container .message').text('修改失败...' + result.message);
                        isRequsetSuccess=false;
                    }
                }
            });
        }

        editProgramEmployee.bindOriginData();
        editProgramEmployee.bindClick();
    }
);