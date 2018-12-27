$(
    function () {
        const editSpecialProgram = {};

        const curProgramArray = [];

        let isRequestSuccess = false;

        const originData = window.parent.originData;

        editSpecialProgram.bindOriginData = function () {
            switch (originData.type) {
                case 0:
                    break;
                case 1:
                    curProgramArray.map(
                        (role, index) => {
                            originData.curSpecialProgram.rolesList.map(
                                sp => {
                                    if (sp.id === role.id) {
                                        $('input').get(index).checked = true;
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

        editSpecialProgram.bindClick = function () {

            $('.save-btn').click(function () {
                const $checked = $('input:checked');

                if ($checked.length === 0) {
                    $('.message-container .message').text('请选择节目');
                    return;
                }

                const idArray = [];
                for (let i = 0; i < $checked.length; i++) {
                    const index = $($checked.get(i)).parent().index();
                    idArray.push(curProgramArray[index].id);
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

                if (originData.type === 0) {
                    editSpecialProgram.saveSpecial(idStr);
                } else if (originData.type === 1) {
                    editSpecialProgram.updateSpecial(originData.curSpecialProgram.id, idStr);
                }
            });

            $('.cancel-btn').click(function () {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);

                if (isRequestSuccess) {
                    parent.querySpecialProgramGroup();
                }
            });
        };

        editSpecialProgram.saveSpecial = function (idStr) {
            $('.message-container .message').text('正在添加组...');
            $.ajax({
                url: '/schedule/equal_role/add',
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

        editSpecialProgram.updateSpecial = function (id, idStr) {
            $('.message-container .message').text('正在修改组...');
            $.ajax({
                url: '/schedule/equal_role/update',
                type: 'POST',
                data: {
                    id: id,
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

        editSpecialProgram.queryPrograms = function () {
            $.ajax({
                url: '/schedule/sprogram/programs?stationId=' + 1,
                success: result => {
                    if (result.code == 0) {
                        this.buildPrograms(result.data.programs);
                        this.bindOriginData();
                    } else {
                        console.log(result);
                    }
                }
            });
        };

        editSpecialProgram.buildPrograms = function (progarmArray) {

            const roleArray = [];

            progarmArray.map(
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

            const programItems = roleArray.map(
                role => {
                    return $('<li>')
                        .append('<input type="checkbox">' + role.programName + '(' + role.roleName + ')');
                }
            );

            console.log(curProgramArray);

            $('.program-container ul')
                .html('')
                .append(programItems);
        };

        editSpecialProgram.queryPrograms();
        editSpecialProgram.bindClick();

    }
);