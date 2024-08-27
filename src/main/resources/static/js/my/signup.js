let usernameReg = /^[a-zA-Z0-9-]{2,30}$/;
let nameReg = /^[a-zA-Zㄱ-ㅣ가-힣\d\s]{2,50}$/;
let emailReg = /^\w+([\\.-]?\w+)*@\w+([\\.-]?\w+)*(\.\w{2,3})+$/;
let pwdReg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+=?<>.,/|~`])[A-Za-z\d!@#$%^&*()_+=?<>.,/|~`]{8,}$/
let isEmailVerified = false;

function emailVerificationFormOpen () {
    const email = document.getElementById("email-input").value;
    const sendButton = document.getElementById("send-button");
    let sendEmail = document.getElementById("send-email");
    let cantSendEmail = document.getElementById("cant-send-email");
    let dupEmail = document.getElementById("dup-email");
    sendButton.disabled = true;

    axios
        .get("/api/mail/verification?email=" + email)
        .then((result) => {
            dupEmail.classList.add("d-none");
            cantSendEmail.classList.add("d-none");
            sendEmail.classList.remove("d-none");
            document.getElementById("email-verify-div").className = "form-group mb-4";
        })
        .catch((error) => {
            if (error.response.status === 409) {
                dupEmail.classList.remove("d-none");
            } else {
                cantSendEmail.classList.remove("d-none");
            }
            sendButton.disabled = false;
        })
}

function emailVerification() {
    const verificationCode = document.getElementById("email-verify").value;
    const nowEmail = document.getElementById("email-input").value;

    axios.post("/api/mail/verification", {
        "email" : nowEmail,
        "verificationCode" : verificationCode
    })
        .then((result) => {
            document.getElementById("email-input").readOnly = true;
            document.getElementById("email-verify-div").className = "form-group mb-4 d-none";
            isEmailVerified = true;
        })
        .catch(() => {
        })
}

async function usernameValidate() {
    let isDuplicated = true;
    let usernameInput = document.getElementById("username-input");
    let cantUsername = document.getElementById("cant-username");

    await axios.get("/api/users/username/verification?username=" + usernameInput.value)
        .then((result) => {
            isDuplicated = result.data;

            if (usernameReg.test(usernameInput.value) && !isDuplicated) {
                usernameInput.className = "form-control is-valid";
                cantUsername.classList.add("d-none");
                return true;
            } else {
                usernameInput.className = "form-control is-invalid";
                cantUsername.classList.remove("d-none");
                return false;
            }
        })
        .catch(() => {
            usernameInput.className = "form-control is-invalid";
            cantUsername.classList.remove("d-none");
        })
}

function emailValidate(element) {
    if (!emailReg.test(element.value)) {
        element.className = "form-control is-invalid";
        return false;
    } else {
        element.className = "form-control is-valid";
        return true;
    }
}

function nameValidate(element) {
    let cantName = document.getElementById("cant-name");

    if (!nameReg.test(element.value)) {
        element.className = "form-control is-invalid";
        cantName.classList.remove("d-none");
        return false;
    } else {
        element.className = "form-control is-valid";
        cantName.classList.add("d-none");
        return true;
    }
}

function pwdValidate(element) {
    let cantPassword = document.getElementById("cant-pwd");

    if (!pwdReg.test(element.value)) {
        element.className = "form-control is-invalid";
        cantPassword.classList.remove("d-none");
        return false;
    } else {
        element.className = "form-control is-valid";
        cantPassword.classList.add("d-none");
        return true;
    }
}

function pwdCheckValidate(element) {
    const origin = document.getElementById("pwd-input").value;
    let cantPwdCheck = document.getElementById("cant-pwd-check");

    if (origin !== element.value) {
        element.className = "form-control is-invalid";
        cantPwdCheck.classList.remove("d-none");
        return false;
    } else if (!pwdReg.test(element.value)) {
        element.className = "form-control is-invalid";
        cantPwdCheck.classList.remove("d-none");
        return false;
    } else {
        element.className = "form-control is-valid";
        cantPwdCheck.classList.add("d-none");
        return true;
    }
}

function memberSignup(element) {
    element.disabled = true;

    let usernameInput = document.getElementById("username-input");
    let nameInput = document.getElementById("name-input");
    let emailInput = document.getElementById("email-input");
    let pwdInput = document.getElementById("pwd-input");
    let pwdCheckInput = document.getElementById("pwd-check-input");

    if (usernameValidate(usernameInput)
        && nameValidate(nameInput)
        && emailValidate(emailInput)
        && pwdValidate(pwdInput)
        && pwdCheckValidate(pwdCheckInput)
        && isEmailVerified) {
        let signupForm = document.getElementById("signupForm");
        signupForm.submit();

    } else {
        element.disabled = false;
    }
}

function debounce(func, timeout = 500) {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => {
            func.apply(this, args);
        }, timeout);
    };
}

const usernameCheck = debounce(() => usernameValidate());