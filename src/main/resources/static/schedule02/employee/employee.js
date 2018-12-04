window.employee = function () {

    const employee = {};

    window.originData = {};

    employee.bindClick = function () {
        $('.employee-container li').click(function () {
            $(this).addClass('active').removeClass('unactive')
                .siblings().removeClass('active').addClass('unactive');

            return false;
        });

        $('.employee-main-container .add').click(function () {

            originData = {
                type: 0
            };

            layer.open({
                type: 2,
                title: '添加人员',
                area: ['400px', '230px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/schedule/schedule02/employee/edit/edit.html'
            });
        });

        $('.employee-main-container .modify').click(function () {

            const $current = $('.employee-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请选中人员再修改');
                return;
            }

            originData = {
                type: 1,
                name: $current.text()
            }

            layer.open({
                type: 2,
                title: '修改人员',
                area: ['400px', '230px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/schedule/schedule02/employee/edit/edit.html'
            });
        });

        $('.employee-main-container .delete').click(function () {

            const $current = $('.employee-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请选中人员再删除');
                return;
            }

            if (confirm('确认删除')) {
                const name = $current.text();


                alert('删除' + name + '成功');
            }

        });
    };

    employee.bindClick();

};
