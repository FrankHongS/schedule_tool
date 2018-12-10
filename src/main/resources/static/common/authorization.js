$(
    function(){
        const username=Cookies.get('username');
        const password=Cookies.get('password');

        if(username==='admin'&&password==='123456'){
            console.log('log in succssfully');
        }else{
            alert('log in please');
        }
    }
);