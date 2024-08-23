let usernameReg = /^[a-z0-9-]{2,30}$/;
let nameReg = /^[a-zA-Zㄱ-ㅣ가-힣\d]{2,50}$/;

async function usernameValidate() {
    let isDuplicated = true;
    let usernameInput = document.getElementById("username-input");

    await axios.get("/api/users/username/verification?username=" + usernameInput.value)
        .then((result) => {
            isDuplicated = result.data;

            if (usernameReg.test(usernameInput.value) && !isDuplicated) {
                usernameInput.className = "form-control is-valid";
                return true;
            } else {
                usernameInput.className = "form-control is-invalid";
                return false;
            }
        })
        .catch(() => {
            usernameInput.className = "form-control is-invalid";
        });
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

function debounce(func, timeout = 200) {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => {
            func.apply(this, args);
        }, timeout);
    };
}

const usernameCheck = debounce(() => usernameValidate());

function memberSignup(element) {
    element.disabled = true;

    let usernameInput = document.getElementById("username-input");
    let nameInput = document.getElementById("name-input");
    let idInput = document.getElementById("id-input");

    if (usernameValidate(usernameInput)
        && nameValidate(nameInput)
        && idInput.value) {
        let signupForm = document.getElementById("signupForm");
        signupForm.submit();

    } else {
        element.disabled = false;
        openFailedAlerts("실패하였습니다. 잠시 후 다시 시도해주세요.");
    }
}