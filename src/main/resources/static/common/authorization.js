$(
    function(){

        const DESKey='12345678';

        try {
            const username=decryptByAES(Cookies.get('username'),DESKey);
            const password=decryptByAES(Cookies.get('password'),DESKey);
    
            if(username==='Frank'&&password==='123456'){
                console.log('log in succssfully');
            }else{
                alert('log in please');
                $(location).attr('href', '/schedule/common/login/login.html');
            }
        } catch (error) {
            alert('log in please');
            $(location).attr('href', '/schedule/common/login/login.html');
        }

        function decryptByAES(message, key) {
            var bytes = CryptoJS.AES.decrypt(message, key);
            return bytes.toString(CryptoJS.enc.Utf8);
        } 
    }
);