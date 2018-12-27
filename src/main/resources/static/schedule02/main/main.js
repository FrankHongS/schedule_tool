window.schedule_main = function () {

    const main = {};

    main.bindClick = function () {
        $('.nav-container li').click(function () {
            $(this).addClass('active').removeClass('unactive')
                .siblings().removeClass('active').addClass('unactive');

            const index = $(this).index();

            switch (index) {
                case 0:
                    if (!$('#employee').hasClass('loaded')) {
                        $('#employee').load('/schedule/schedule02/employee/employee.html ', function () {
                            window.employee();
                        });
                        $('#employee').addClass('loaded');
                    }

                    $('#employee').show()
                        .siblings().hide();

                    break;
                case 1:
                    if (!$('#program').hasClass('loaded')) {
                        $('#program').load('/schedule/schedule02/program/program.html ', function () {
                            window.program();
                        });
                        $('#program').addClass('loaded');
                    }

                    $('#program').show()
                        .siblings().hide();
                    break;
                case 2:
                    if (!$('#substitute').hasClass('loaded')) {
                        $('#substitute').load('/schedule/schedule02/substitute/substitute.html ', function () {
                            window.substitute();
                        });
                        $('#substitute').addClass('loaded');
                    }

                    $('#substitute').show()
                        .siblings().hide();
                    break;
                case 3:
                    if (!$('#export').hasClass('loaded')) {
                        $('#export').load('/schedule/schedule02/exportSchedule/exportSchedule.html ', function () {
                            window.exportSchedule();
                        });
                        $('#export').addClass('loaded');
                    }

                    $('#export').show()
                        .siblings().hide();
                    break;
                default:
                    break;
            }
            return false;
        });

    };

    main.bindClick();
    $('.nav-container li:first-child').trigger('click');
};