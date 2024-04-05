let nameReg = /^[a-zA-Zㄱ-ㅣ가-힣\d]{2,20}$/;
let emailReg = /^\w+([\\.-]?\w+)*@\w+([\\.-]?\w+)*(\.\w{2,3})+$/;
let pwdReg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+=?<>.,/|~`])[A-Za-z\d!@#$%^&*()_+=?<>.,/|~`]{8,}$/
let isEmailVerified = false;

function emailVerificationFormOpen () {
    const email = document.getElementById("email-input").value;
    const sendButton = document.getElementById("send-button");
    sendButton.disabled = true;

    axios
        .get("/api/v1/mail/verification?email=" + email)
        .then((result) => {
            alert("인증번호는 10분간 유효합니다.");
            document.getElementById("email-verify-div").className = "form-group mb-4";
        })
        .catch(() => {
            alert("발송에 실패하였습니다. 다시 시도해주세요.");
            sendButton.disabled = false;
        })
}

function emailVerification() {
    const verificationCode = document.getElementById("email-verify").value;
    const nowEmail = document.getElementById("email-input").value;
    axios.post("/api/v1/mail/verification", {
        "email" : nowEmail,
        "verificationCode" : verificationCode
    })
        .then((result) => {
            alert("인증에 성공하였습니다.");
            document.getElementById("email-input").readOnly = true;
            document.getElementById("email-verify-div").className = "form-group mb-4 d-none";
            isEmailVerified = true;
        })
        .catch(() => {
            alert("인증에 실패하였습니다. 다시 시도해주세요.");
        })
}

function nameValidate(element) {
    if (!nameReg.test(element.value)) {
        element.className = "form-control is-invalid";
        return false;
    } else {
        element.className = "form-control is-valid";
        return true;
    }
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

function pwdValidate(element) {
    if (!pwdReg.test(element.value)) {
        element.className = "form-control is-invalid";
        return false;
    } else {
        element.className = "form-control is-valid";
        return true;
    }
}

function pwdCheckValidate(element) {
    const origin = document.getElementById("pwd-input").value;
    if (origin !== element.value) {
        element.className = "form-control is-invalid";
        return false;
    } else if (!pwdReg.test(element.value)) {
        element.className = "form-control is-invalid";
        return false;
    } else {
        element.className = "form-control is-valid";
        return true;
    }
}

function memberSignup(element) {
    element.disabled = true;

    let nameInput = document.getElementById("name-input");
    let emailInput = document.getElementById("email-input");
    let pwdInput = document.getElementById("pwd-input");
    let pwdCheckInput = document.getElementById("pwd-check-input");

    if (nameValidate(nameInput)
        && emailValidate(emailInput)
        && pwdValidate(pwdInput)
        && pwdCheckValidate(pwdCheckInput)
        && isEmailVerified) {
        alert("성공!")
        let signupForm = document.getElementById("signupForm");
        signupForm.submit();

    } else {
        element.disabled = false;
        alert("실패!");
    }
}