$(
    function () {
        const main = {};

        let programArray;
        let employeeArray;

        let activeProgramItem;
        let activeEmployeeItem;

        window.inputLabel = {};

        main.checkIfActive = function (container) {
            let activeItem;

            $(container)
                .on('click', 'li', function (e) {
                    const currItem = $(e.target);

                    if (!currItem.hasClass('active')) {
                        currItem.addClass('active');
                        if (currItem.hasClass('selected')) {
                            currItem.removeClass('selected');
                        }
                        if (activeItem) {
                            activeItem.removeClass('active');
                        }
                        activeItem = currItem;
                        if ($(this).parents().hasClass('program-container')) {
                            activeProgramItem = activeItem;
                        } else if ($(this).parents().hasClass('employee-container')) {
                            activeEmployeeItem = activeItem;
                        }
                    }
                })
                .on('mouseenter', 'li', function (e) {
                    const currItem = $(e.target);
                    if (!currItem.hasClass('active') && !currItem.hasClass('selected')) {
                        currItem.addClass('selected');
                    }
                });
        };

        main.bindClick = function () {
            // 根据点击节目的item加载对应的人员
            $('.program-container ul').on('click', 'li', e => {
                queryEmployees();
            });

            //添加节目
            $('.add-program').on('click', function (e) {
                inputLabel = {
                    name: '节目名称',
                    type: 0
                };
                layer.open({
                    type: 2,
                    title: '添加节目',
                    area: ['350px', '230px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: '../add/add.html'
                });

            });

            $('.delete-program').on('click', function (e) {
                if (!activeProgramItem) {
                    alert('请选中节目之后再删除');
                    return;
                }

                if(confirm('确认删除？')){
                    const id=programArray[activeProgramItem.index()].id;
                    console.log(id);
                    $.ajax({
                        url:'/schedule/program/delete',
                        type:'POST',
                        data:{
                            id:id
                        },
                        success:result=>{
                            if(result.code==0){
                                alert('删除'+activeProgramItem.text()+'成功');
                                queryPrograms();
                            }
                        }
                    });
                }
            });

            $('.edit-program').on('click', function () {
                if (!activeProgramItem) {
                    alert('请选中节目之后再编辑');
                    return;
                }
                const id=programArray[activeProgramItem.index()].id;
                inputLabel = {
                    name: '节目名称',
                    id:id,
                    value:activeProgramItem.text(),
                    type: 1
                };
                layer.open({
                    type: 2,
                    title: '编辑节目',
                    area: ['350px', '230px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: '../add/add.html'
                });

            });

            //添加人员
            $('.add-employee').on('click', function (e) {
                const id=programArray[activeProgramItem.index()].id;
                inputLabel = {
                    name: '人员名字',
                    programId: id,
                    type: 2
                };
                layer.open({
                    type: 2,
                    title: '添加人员',
                    area: ['350px', '230px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: '../add/add.html'
                });

            });
            //删除人员
            $('.delete-employee').on('click', function (e) {
                if (!activeEmployeeItem) {
                    alert('请选中人员之后再删除');
                    return;
                }

                if(confirm('确认删除？')){
                    const id=employeeArray[activeEmployeeItem.index()].id;
                    console.log(id);
                    $.ajax({
                        url:'/schedule/program_employee/delete',
                        type:'POST',
                        data:{
                            id:id
                        },
                        success:result=>{
                            if(result.code==0){
                                alert('删除'+activeEmployeeItem.text()+'成功');
                                queryEmployees();
                            }
                        }
                    });
                }
            });
            //编辑人员
            $('.edit-employee').on('click', function (e) {

                if (!activeEmployeeItem) {
                    alert('请选中人员之后再编辑');
                    return;
                }

                const id=employeeArray[activeEmployeeItem.index()].id;
                inputLabel = {
                    name: '人员名字',
                    id:id,
                    value:activeEmployeeItem.text(),
                    type: 3
                };
                layer.open({
                    type: 2,
                    title: '编辑人员',
                    area: ['350px', '230px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: '../add/add.html'
                });

            });

        };

        main.bindLayer = function () {
            laydate.render({
                elem: '.sub-date',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
            laydate.render({
                elem: '.export-from',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
            laydate.render({
                elem: '.export-to',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
        };

        window.queryPrograms = function () {
            $.ajax({
                url: '/schedule/program',
                success: result => {
                    if (result.code == 0) {
                        main.buildPrograms(result.data.program);
                        programArray=result.data.program;
                        console.log(result);
                        activeProgramItem=undefined;
                    }
                }
            });
        };

        window.queryEmployees=function(){
            const id=programArray[activeProgramItem.index()].id;
            $.ajax({
                url:'/schedule/program_employee?programId='+id,
                success:result=>{
                    if(result.code==0){
                        console.log(result);
                        main.buildEmployees(result.data.program_employee);
                        employeeArray=result.data.program_employee;
                        activeEmployeeItem=undefined;
                    }
                }
            });
        };

        main.checkIfActive('.program-container ul');
        main.checkIfActive('.employee-container ul');
        main.checkIfActive('.sub-container ul');
        main.checkIfActive('.spec-employees-container ul');
        main.bindClick();
        main.bindLayer();
        window.queryPrograms();



        main.buildPrograms = function (programsArray) {
            const programItems = programsArray.map(
                item => {
                    return $('<li>')
                        .text(item.name);
                }
            );

            $('.program-container ul').html('');
            $('.program-container ul').append(programItems);
        };

        main.buildEmployees = function (employeeArray) {
            const employeeItems = employeeArray.map(
                employee => {
                    return $('<li>')
                        .text(employee.name);
                }
            );

            $('.employee-container ul')
                .html('')
                .append(employeeItems);
            
        };

    }
);