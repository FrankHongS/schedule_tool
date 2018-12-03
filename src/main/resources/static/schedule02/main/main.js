$(
    function(){
        $('.nav-container li').click(function(){
            $(this).addClass('active').removeClass('unactive')
                   .siblings().removeClass('active').addClass('unactive');
                   return false;
        });
    }
);