$(
    function () {

        const originData = window.parent.originData;

        const editProgram = {};

        const discardIdArray = [];

        let modifyObject;

        let isRequestSuccess = false;

        editProgram.bindOriginData = function () {
            switch (originData.type) {
                case 0:// add program
                    break;
                case 1:// modify program
                    $('.program-name-wrapper .name').val(originData.name);

                    const roleArray = originData.roleArray;

                    for (let i = 0; i < roleArray.length; i++) {
                        this.buildProgramRole('.role-container');
                    }

                    setTimeout(() => {
                        roleArray.map(
                            (role, roleIndex) => {
                                const cur = $('.role-wrapper').get(roleIndex);
                                $(cur).find('.role-name').get(0).value = role.name;
                                $(cur).attr('item-id', role.id);

                                splitStrUtil(role.cycle).map(
                                    (value, index) => {
                                        if (value == '0') {
                                            $(cur).find('ul li input').get(index).checked = true;
                                        }
                                    }
                                );
                            }
                        );
                    }, 300)

                    break;
                default:
                    break;
            }
        };

        editProgram.buildProgramRole = function (container) {
            $(container).append("<div class='role-wrapper'>" +
                "<div class='role-name-wrapper'>" +
                "<label class='role-name-label'>节目角色</label>" +
                "<input type='text' class='form-input role-name' placeholder='角色名' name='role-name'>" +
                "</div>" +
                "<div class='day-wrapper'>" +
                "<label for='name' class='name-label'>休息时间</label>" +
                "<div class='day-container'>" +
                "<ul>" +
                "<li><input type='checkbox'>Mon</li>" +
                "<li><input type='checkbox'>Tue</li>" +
                "<li><input type='checkbox'>Wed</li>" +
                "<li><input type='checkbox'>Thu</li>" +
                "<li><input type='checkbox'>Fri</li>" +
                "<li><input type='checkbox'>Sat</li>" +
                "<li><input type='checkbox'>Sun</li>" +
                "</ul>" +
                "</div>" +
                "</div>" +
                "<div class='discard-wrapper'>" +
                "<img class='discard' src='../../../img/discard.png' alt='discard'>" +
                "</div>" +
                "</div>");

            //未生成DOM节点，绑定不了点击事件?
            $('.discard-wrapper .discard').click(function (e) {
                const $parent = $(e.target).parents('.role-wrapper');
                $parent.remove();
                discardIdArray.push($parent.attr('item-id'));

                console.log(discardIdArray);
            });
        }

        editProgram.bindClick = function () {

            $('.add-role-wrapper .add').click(() => {
                this.buildProgramRole('.role-container');
            });

            $('.save-btn').click(() => {
                const name = $('.program-name-wrapper .name').val();

                if (!name) {
                    $('.message-container .message').text('保存失败...节目名不能为空！');
                    return;
                }

                const $roles = $('.role-wrapper');

                if($roles.length===0){
                    $('.message-container .message').text('保存失败...至少添加一个角色！');
                    return;
                }

                let msg;
                let data;
                switch (originData.type) {
                    case 0:
                        msg = {
                            success: '保存成功',
                            failure: '保存失败...'
                        };

                        data = {
                            stationId: 1,
                            name: name
                        }

                        this.saveProgram(msg, data);
                        break;
                    case 1:
                        msg = {
                            success: '修改成功',
                            failure: '修改失败...'
                        };

                        data = {
                            programId: originData.id,
                            name: name
                        }

                        this.updateProgram(msg, data);
                        break;
                    default:
                        break;
                }

            });

            $('.cancel-btn').click(function () {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);

                if (isRequestSuccess) {//只有请求成功，才刷新列表
                    window.parent.queryPrograms();
                }
            });

        };

        editProgram.saveProgram = function (msg, data) {
            
            new Promise(
                resolve => {
                    $.ajax({
                        url: '/schedule/sprogram/add',
                        type: 'POST',
                        data: data,
                        success: result => {
                            console.log(result);
                            if (result.code === 0) {
                                resolve(result.data.id);
                            } else {
                                $('.message-container .message').text(msg.failure + result.message);
                            }
                        }
                    });
                }
            ).then(
                value => {

                    const roleArray = editProgram.generateRoleArray(value);

                    if (roleArray.length === 0) {
                        isRequestSuccess = true;
                        $('.message-container .message').text(msg.success);
                    } else {
                        $.ajax({
                            url: '/schedule/role/addSome',
                            type: 'POST',
                            data: {
                                roles: JSON.stringify(roleArray)
                            },
                            success: result => {
                                if (result.code === 0) {
                                    isRequestSuccess = true;
                                    $('.message-container .message').text(msg.success);
                                } else {
                                    $('.message-container .message').text(msg.failure + result.message);
                                }
                            }
                        });
                    }
                }
            );
        };

        editProgram.updateProgram = function (msg, data) {
            new Promise(
                resolve => {
                    $.ajax({
                        url: '/schedule/sprogram/update',
                        type: 'POST',
                        data: data,
                        success: result => {
                            if (result.code === 0) {
                                resolve(data.programId);
                                isRequestSuccess = true;
                                $('.message-container .message').text(msg.success);
                            } else {
                                isRequestSuccess = false;
                                $('.message-container .message').text(msg.failure + result.message);
                            }
                        },
                        error: (xhr, e) => {
                            isRequestSuccess = false;
                            $('.message-container .message').text(msg.failure);
                        }
                    });
                }
            ).then(
                value => {

                    return new Promise(resolve => {
                        modifyObject = editProgram.generateUpdateAndSaveRoleArray(value);

                        if (modifyObject.saveRoleArray.length === 0) {
                            resolve();
                        } else {
                            $.ajax({
                                url: '/schedule/role/addSome',
                                type: 'POST',
                                data: {
                                    roles: JSON.stringify(modifyObject.saveRoleArray)
                                },
                                success: result => {
                                    if (result.code === 0) {
                                        resolve();
                                    } else {
                                        $('.message-container .message').text(msg.failure + result.message);
                                    }
                                }
                            });
                        }
                    });

                }
            ).then(
                () => {
                    for (let i = 0; i < discardIdArray.length; i++) {
                        $.ajax({
                            url: '/schedule/role/delete?id=' + discardIdArray[i],
                            success: result => {
                                if (result.code === 0) {
                                    isRequestSuccess = true;
                                    $('.message-container .message').text(msg.success);
                                } else {
                                    isRequestSuccess = false;
                                    $('.message-container .message').text(msg.failure + result.message);
                                }
                            }
                        });
                    }

                    for (let j = 0; j < modifyObject.updateRoleArray.length; j++) {
                        $.ajax({
                            url: '/schedule/role/update',
                            type: 'POST',
                            data: modifyObject.updateRoleArray[j],
                            success: result => {
                                if (result.code === 0) {
                                    isRequestSuccess = true;
                                    $('.message-container .message').text(msg.success);
                                } else {
                                    isRequestSuccess = false;
                                    $('.message-container .message').text(msg.failure + result.message);
                                }
                            }
                        });
                    }
                }
            );
        };

        editProgram.generateRoleArray = function (id) {
            const target = [];

            const $roles = $('.role-wrapper');

            for (let i = 0; i < $roles.length; i++) {
                const item = {};

                item.programId = id;
                item.name = $('.role-wrapper .role-name').get(i).value;
                item.cycle = '1111100';
                item.workDays = 5;

                target.push(item);
            }

            return target;
        };

        // when modifying
        editProgram.generateUpdateAndSaveRoleArray = function (id) {
            const updateRoleArray = [];
            const saveRoleArray = [];

            const $roles = $('.role-wrapper');

            for (let i = 0; i < $roles.length; i++) {
                const cur = $roles.get(i);
                if ($(cur).attr('item-id')) {
                    const item = {};
                    item.id = $(cur).attr('item-id');
                    item.roleName = $('.role-wrapper .role-name').get(i).value;
                    item.cycle = '1111100';
                    item.workDays = 5;

                    updateRoleArray.push(item);
                } else {
                    const item = {};
                    item.programId = id;
                    item.name = $('.role-wrapper .role-name').get(i).value;
                    item.cycle = '1111100';
                    item.workDays = 5;

                    saveRoleArray.push(item);
                }
            }

            return {
                updateRoleArray: updateRoleArray,
                saveRoleArray: saveRoleArray
            };
        }

        function splitStrUtil(str) {
            return str.split('');
        }

        editProgram.bindOriginData();
        editProgram.bindClick();
    }
);