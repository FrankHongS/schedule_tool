window.program = function () {
    const program = {};

    window.originData = {};

    let programArray;

    program.bindClick = function () {
        $('.program-container ul').on('click', 'li', function (e) {
            $(e.target).addClass('active').removeClass('unactive')
                .siblings().removeClass('active').addClass('unactive');

            return false;
        });

        $('.program-main-container .add').click(function () {

            originData = {
                type: 0
            }

            layer.open({
                type: 2,
                title: '添加节目',
                area: ['900px', '460px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/schedule/schedule02/program/edit/edit.html'
                // content:'./edit/edit.html'
            });
        });

        $('.program-main-container .modify').click(function () {

            const $current = $('.program-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请选中节目再修改');
                return;
            }

            const curProgram = programArray[$current.index()];

            originData = {
                type: 1,
                id: curProgram.id,
                name: curProgram.name,
                roleArray: curProgram.programRoles
            };

            layer.open({
                type: 2,
                title: '修改节目',
                area: ['900px', '460px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/schedule/schedule02/program/edit/edit.html'
                // './edit/edit.html'
            });
        });

        $('.program-main-container .delete').click(function () {
            const $current = $('.program-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请选中节目再删除');
                return;
            }

            const curProgram = programArray[$current.index()];

            if (confirm('确认删除')) {

                $.ajax({
                    url: '/schedule/sprogram/delete?programId=' + curProgram.id,
                    success: result => {
                        if (result.code === 0) {
                            alert('删除' + curProgram.name + '成功');
                            window.queryPrograms();
                        } else {
                            alert('删除' + curProgram.name + '失败...' + result.message);
                        }
                    },
                    error: (xhr, e) => {
                        alert('删除失败...');
                    }
                });

            }
        });

        $('.manage-employees').click(function () {
            const $current = $('.program-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请先选中节目');
                return;
            }

            const curProgram = programArray[$current.index()];
            let roleIds = '';
            let roleNames = '';
            curProgram.programRoles.map(
                (role, index) => {
                    if (index == curProgram.programRoles.length - 1) {
                        roleIds += role.id;
                        roleNames += role.name;
                    } else {
                        roleIds += role.id + ',';
                        roleNames += role.name + ' , ';
                    }
                }
            );

            // $(location).attr('href', '/schedule/schedule02/program/programEmployee/programEmployee.html?program=' + $current.text());
            window.open('/schedule/schedule02/program/programEmployee/programEmployee.html?program=' + curProgram.name + '&roleIds=' + roleIds + '&roleNames=' + roleNames);
        });
    };

    window.queryPrograms = function () {
        $.ajax({
            url: '/schedule/sprogram/programs?stationId=' + 1,
            success: result => {
                if (result.code == 0) {
                    programArray = result.data.programs;
                    program.buildPrograms(programArray);
                } else {
                    console.log(result);
                }
            }
        });
    };

    program.buildPrograms = function (programsArray) {
        const programItems = programsArray.map(
            item => {
                return $('<li>')
                    .text(item.name);
            }
        );

        $('.program-container ul')
            .html('')
            .append(programItems);
    };

    program.bindClick();
    window.queryPrograms();
};
