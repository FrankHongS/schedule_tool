window.employee = function () {

    const employee = {};

    window.originData = {};

    let curEmployeeArray;

    employee.bindClick = function () {
        $('.employee-container ul').on('click','li',function (e) {
            $(e.target).addClass('active').removeClass('unactive')
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

            const curEmployee=curEmployeeArray[$current.index()];

            originData = {
                type: 1,
                id:curEmployee.id,
                name: curEmployee.name,
                alias: curEmployee.alias
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

            const curEmployee=curEmployeeArray[$current.index()];

            if (confirm('确认删除')) {
                const name = curEmployee.name;
                $.ajax({
                    url:'/schedule/station_employee/delete?id='+curEmployee.id,
                    success:result=>{
                        if(result.code===0){
                            alert('删除' + name + '成功');
                            window.queryEmployees();
                        }else{
                            alert('删除失败...'+result.message);
                        }
                    },
                    error:(xhr,e)=>{
                        alert('删除失败...');
                    }
                });

            }

        });
    };

    window.queryEmployees = function () {
        $.ajax({
            url: '/schedule/station_employee',
            success: result => {
                if (result.code == 0) {
                    curEmployeeArray=result.data.employees;
                    employee.buildEmployees(curEmployeeArray);
                }else{
                    console.log(result);
                }
            }
        });
    };

    employee.buildEmployees = function (employeeArray) {
        const employeeItems = employeeArray.map(
            employee => {
                return $('<li>')
                    .text(employee.name+'('+employee.alias+')');
            }
        );

        $('.employee-container ul')
            .html('')
            .append(employeeItems);

    };


    employee.bindClick();
    window.queryEmployees();
};
