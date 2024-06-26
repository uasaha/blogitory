function login() {
    const email = document.getElementById('login-id-input').value;
    const pwd = document.getElementById('login-pwd-input').value;
    const failedDiv = document.getElementById('failed-reason');

    axios.post('/api/login',
        {
            "email": email,
            "password": pwd
        },
        {
            headers: {
                "Content-Type": "application/json; charset=UTF-8",
                [_csrf_header]: _csrf
            }
        }).then((response) => {
        document.location.reload();
        }).catch((error) => {
            console.log(error);
        failedDiv.className = "mb-4";
        })
}

function logout() {
    axios.post('/api/logout',
        {
            headers: {
                "Content-Type": "application/json; charset=UTF-8",
                [_csrf_header]: _csrf
            }
        })
        .catch((error) => {
            console.log(error)
        })
        .finally((response) => {
            document.location.reload();
        });
}

function isEnterPress() {
    const email = document.getElementById('login-id-input').value;
    const pwd = document.getElementById('login-pwd-input').value;

    if (window.event.keyCode == 13) {
        this.login();
    }
}