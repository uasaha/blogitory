function login() {
    const email = document.getElementById('login-id-input').value;
    const pwd = document.getElementById('login-pwd-input').value;
    const failedDiv = document.getElementById('failed-reason');

    axios.post('/api/login', {
        "email" : email,
        "password" : pwd
    }).then((response) => {
        document.location.reload();
    }).catch((error) => {
        failedDiv.className = "mb-4";
    })
}

function logout() {
    axios.get('/logout')
        .finally((response) => {
        document.location.reload();
    });
}