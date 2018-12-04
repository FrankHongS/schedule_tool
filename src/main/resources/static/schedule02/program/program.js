window.program = function () {
    const program = {};

    window.originData = {};

    program.bindClick = function () {
        $('.program-container li').click(function () {
            $(this).addClass('active').removeClass('unactive')
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
                area: ['400px', '260px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/schedule/schedule02/program/edit/edit.html'
            });
        });

        $('.program-main-container .modify').click(function () {

            const $current = $('.program-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请选中节目再修改');
                return;
            }

            originData = {
                type: 1,
                name: $current.text()
            };

            layer.open({
                type: 2,
                title: '修改节目',
                area: ['400px', '260px'],
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

            if (confirm('确认删除')) {

                alert('删除' + $current.text() + '成功');
            }
        });

        $('.manage-employees').click(function () {
            const $current = $('.program-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请先选中节目');
                return;
            }
            $(location).attr('href', '/schedule/schedule02/program/programEmployee/programEmployee.html?program=' + $current.text());
        });
    };

    program.bindClick();
};
