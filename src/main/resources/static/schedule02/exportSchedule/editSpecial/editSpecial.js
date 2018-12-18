$(
    function () {
        const editSpecial = {};

        let curEmployeeArray;

        let isRequestSuccess = false;

        const originData = window.parent.originData;

        editSpecial.bindOriginData = function () {
            switch (originData.type) {
                case 0:
                    break;
                case 1:
                curEmployeeArray.map(
                    (employee,index)=>{
                        originData.curSpecial.employees.map(
                            sp=>{
                                if(sp.id===employee.id){
                                    $('input').get(index).checked=true;
                                }
                            }
                        );
                    }
                );
                    break;
                default:
                    break;
            }
        };

        editSpecial.bindClick = function () {

            $('.save-btn').click(function () {
                const $checked = $('input:checked');

                if ($checked.length === 0) {
                    $('.message-container .message').text('请选择人员');
                    return;
                }

                const idArray = [];
                for (let i = 0; i < $checked.length; i++) {
                    const index = $($checked.get(i)).parent().index();
                    idArray.push(curEmployeeArray[index].id);
                }

                let idStr = '';

                idArray.map(
                    (id, index) => {
                        if (index == idArray.length - 1) {
                            idStr += id;
                        } else {
                            idStr += id + ','
                        }
                    }
                )

                if(originData.type===0){
                    editSpecial.saveSpecial(idStr);
                }else if(originData.type===1){
                    editSpecial.updateSpecial(originData.curSpecial.id,idStr);
                }
            });

            $('.cancel-btn').click(function () {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);

                if (isRequestSuccess) {
                    parent.querySpecialGroup();
                }
            });
        };

        editSpecial.saveSpecial=function(idStr){
            $.ajax({
                url: '/schedule/mutex_employee/add',
                type: 'POST',
                data: {
                    ids: idStr
                },
                success: result => {
                    if (result.code === 0) {
                        $('.message-container .message').text('添加组成功');
                        isRequestSuccess = true;
                    } else {
                        $('.message-container .message').text('添加组失败...' + result.message);
                    }
                }
            });
        };

        editSpecial.updateSpecial=function(id,idStr){
            $.ajax({
                url: '/schedule/mutex_employee/update',
                type: 'POST',
                data: {
                    id:id,
                    ids: idStr
                },
                success: result => {
                    if (result.code === 0) {
                        $('.message-container .message').text('修改组成功');
                        isRequestSuccess = true;
                    } else {
                        $('.message-container .message').text('修改组失败...' + result.message);
                    }
                }
            });
        };

        editSpecial.queryEmployees = function () {
            $.ajax({
                url: '/schedule/station_employee',
                success: result => {
                    if (result.code == 0) {
                        curEmployeeArray = result.data.employees;
                        this.buildEmployee(curEmployeeArray);
                        this.bindOriginData();
                    } else {
                        console.log(result);
                    }
                }
            });
        };

        editSpecial.buildEmployee = function (employeeArray) {
            const employeeItems = employeeArray.map(
                employee => {
                    return $('<li>')
                        .append('<input type="checkbox">' + employee.name + '(' + employee.alias + ')');
                }
            );

            $('.employee-container ul')
                .html('')
                .append(employeeItems);
        };

        editSpecial.queryEmployees();
        editSpecial.bindClick();

    }
);