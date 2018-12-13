$(
    function () {

        const originData = window.parent.originData;

        const editProgram = {};

        let isRequestSuccess=false;

        editProgram.bindOriginData = function () {
            switch (originData.type) {
                case 0:// add program
                    break;
                case 1:// modify program
                    $('.program-name-wrapper .name').val(originData.name);
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
        }

        editProgram.bindClick = function () {

            $('.add-role-wrapper .add').click(() => {
                this.buildProgramRole('.role-container');
                //未生成DOM节点，绑定不了点击事件?
                $('.discard-wrapper .discard').click(function (e) {
                    $(e.target).parents('.role-wrapper').remove();
                });
            });

            $('.save-btn').click(function () {
                const name = $('.program-name-wrapper .name').val();

                if (!name) {
                    $('.message-container .message').text('节目名不能为空！');
                    return;
                }

                let msg = {
                    success: '保存成功',
                    failure: '保存失败...'
                };

                new Promise(
                    resolve => {
                        $.ajax({
                            url: '/schedule/sprogram/add',
                            type: 'POST',
                            data: {
                                stationId: 1,
                                name: name
                            },
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

                        $.ajax({
                            url: '/schedule/role/addSome',
                            type: 'POST',
                            data: {
                                roles: JSON.stringify(roleArray)
                            },
                            success: result => {
                                console.log(result);
                                if (result.code === 0) {
                                    isRequestSuccess=true;
                                    $('.message-container .message').text(msg.success);
                                } else {
                                    $('.message-container .message').text(msg.failure + result.message);
                                }
                            }
                        });
                    }
                );
            });

            $('.cancel-btn').click(function () {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);

                if(isRequestSuccess){//只有请求成功，才刷新列表
                    window.parent.queryEmployees();
                }
            });

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

        editProgram.bindOriginData();
        editProgram.bindClick();

    }
);