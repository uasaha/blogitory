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

function openSuccessAlerts(msg) {
    let successAlerts = document.querySelector("#success-alerts");
    let successMsg = document.querySelector("#success-alerts-msg");
    successMsg.textContent = msg;
    successAlerts.classList.remove("d-none");

    setTimeout(function () {
        successAlerts.classList.add("d-none");
    }, 5000)
}

function openFailedAlerts(msg) {
    let failedAlerts = document.querySelector("#failed-alerts");
    let failedMsg = document.querySelector("#failed-alerts-msg");
    failedMsg.textContent = msg;
    failedAlerts.classList.remove("d-none");

    setTimeout(function () {
        failedAlerts.classList.add("d-none");
    }, 5000)
}

function openWarnAlerts(msg) {
    let warnAlerts = document.querySelector("#warn-alerts");
    let warnMsg = document.querySelector("#warn-alerts-msg");
    warnMsg.textContent = msg;
    warnAlerts.classList.remove("d-none");

    setTimeout(function () {
        warnAlerts.classList.add("d-none");
    }, 5000)
}