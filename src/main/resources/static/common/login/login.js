$(
    function () {

        const DESKey = '12345678';
        const login={};

        login.checkAuthorization=function(){
            try {
                const username=decryptByAES(Cookies.get('username'),DESKey);
                const password=decryptByAES(Cookies.get('password'),DESKey);
                if(username==='Frank'&&password==='123456'){
                    $(location).attr('href', '../../schedule/main/main.html');
                }
            } catch (error) {
                // do nothing
            }
        };

        login.bindClick=function(){
            $('.login').click(function () {

                const username = $('.username').val();
                const password = $('.password').val();
    
                if (!username || !password) {
                    alert('用户名或密码不能为空！')
                    return;
                }
    
                $.ajax({
                    url: '/schedule/login',
                    type: 'POST',
                    data: {
                        username: username,
                        password: password
                    },
                    success: result => {
                        if (result.code === 0) {
                            Cookies.set('username', encryptByAES(username, DESKey), { expires: 1 });
                            Cookies.set('password', encryptByAES(password, DESKey), { expires: 1 });
    
                            setTimeout(function () {
                                $(location).attr('href', '../../schedule/main/main.html');
                            }, 300);
                        } else {
                            alert(result.message);
                        }
                    },
                    error: (xhr, e) => {
                        alert('登录失败')
                    }
                });
            });
        };

        function encryptByAES(message, key) {
            return CryptoJS.AES.encrypt(message, key).toString();
        }

        function decryptByAES(message, key) {
            var bytes = CryptoJS.AES.decrypt(message, key);
            return bytes.toString(CryptoJS.enc.Utf8);
        } 

        login.checkAuthorization()
        login.bindClick();
    }
);