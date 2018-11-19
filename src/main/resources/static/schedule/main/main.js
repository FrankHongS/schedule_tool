$(
    function () {
        const main={};

        let activeProgramItem;
        let activeEmployeeItem;

        window.inputLabel={};

        main.checkIfActive=function(container){
            let activeItem;

            $(container)
                .on('click', 'li', function(e) {
                    const currItem = $(e.target);
    
                    if (!currItem.hasClass('active')) {
                        currItem.addClass('active');
                        if(currItem.hasClass('selected')){
                            currItem.removeClass('selected');
                        }
                        if (activeItem) {
                            activeItem.removeClass('active');
                        }
                        activeItem = currItem;
                        if($(this).parents().hasClass('program-container')){
                            activeProgramItem=activeItem;
                        }else if($(this).parents().hasClass('employee-container')){
                            activeEmployeeItem=activeItem;
                        }
                    }
                })
                .on('mouseenter','li',function(e){
                    const currItem=$(e.target);
                    if(!currItem.hasClass('active')&&!currItem.hasClass('selected')){
                        currItem.addClass('selected');
                    }
                });
        };

        main.bindClick=function(){
            $('.program-container ul').on('click','li',e=>{
                const currItem=$(e.target);
                const index=currItem.index();
                this.buildEmployees(index);
            });

            $('.add-program').on('click', function (e) {
                inputLabel={
                    name:'节目名称',
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
                if(!activeProgramItem){
                    alert('请选中节目之后再删除');
                    return;
                }

                alert('确认删除？');
            });

            $('.edit-program').on('click', function () {
                if(!activeProgramItem){
                    alert('请选中节目之后再编辑');
                    return;
                }
                inputLabel={
                    name:'节目名称',
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

            $('.add-employee').on('click', function (e) {
                inputLabel={
                    name:'人员名字',
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

            $('.delete-employee').on('click', function (e) {
                if(!activeEmployeeItem){
                    alert('请选中人员之后再删除');
                    return;
                }

                alert('确认删除？');
            });

            $('.edit-employee').on('click', function (e) {

                if(!activeEmployeeItem){
                    alert('请选中节目之后再编辑');
                    return;
                }

                inputLabel={
                    name:'人员名字',
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

        main.buildEmployees=function(index){
            const employeeItems=window.employees[index].map(
                employee=>{
                    return $('<li>')
                            .text(employee);
                }
            );
            
            $('.employee-container ul')
                .html('')
                .append(employeeItems);
        };

        main.bindLayer=function(){
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

        main.checkIfActive('.program-container ul');
        main.checkIfActive('.employee-container ul');
        main.checkIfActive('.sub-container ul');
        main.checkIfActive('.spec-employees-container ul');
        main.bindClick();
        main.bindLayer();

        const programItems=window.programs.map(
            item=>{
                return $('<li>')
                        .text(item);
            }
        );

        $('.program-container ul').append(programItems);
    }
);