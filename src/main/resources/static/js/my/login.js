function login() {
    const email = document.getElementById('login-id-input').value;
    const pwd = document.getElementById('login-pwd-input').value;
    const failedDiv = document.getElementById('failed-reason');

    axios.post('/api/login',
        {
            "email": email,
            "password": pwd
        }).then((response) => {
        document.location.reload();
        }).catch((error) => {
            console.log(error);
        failedDiv.className = "mb-4";
        })
}

function logout() {
    axios.post('/api/logout')
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

function pwdChangeEvent() {
    let loginBody = document.querySelector('#login-body');
    let pwdBody = document.querySelector('#pwd-body');

    loginBody.classList.add('d-none');
    pwdBody.classList.remove('d-none');
}

function pwdEnterPress() {
    if (window.event.keyCode == 13) {
        pwdMailPush();
    }
}

function pwdMailPush() {
    const email = document.getElementById('pwd-id-input').value;

    axios.get('/api/users/password?email=' + email)
        .then(() => {
            let sent = document.querySelector("#pwdSent");
            sent.classList.remove("d-none");
        })
        .catch(() => {
            let sentFail = document.querySelector("#pwdSentFail");
            sentFail.classList.remove("d-none");
        });
}

function pwdCancel() {
    let loginBody = document.querySelector('#login-body');
    let pwdBody = document.querySelector('#pwd-body');

    loginBody.classList.remove('d-none');
    pwdBody.classList.add('d-none');
}