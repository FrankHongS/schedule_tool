$(
    function(){
        $('.test').click(function(){
            Cookies.set('name', 'value', { expires: 7 });
        });
    }
);