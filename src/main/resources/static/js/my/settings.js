const profileThumb = document.getElementById("profile-thumb");
const settingNameReq = /^[a-zA-Zㄱ-ㅣ가-힣\d]{2,20}$/;
const nameAria = document.getElementById("nameAria");
const nameAriaUpdate = document.getElementById("nameAriaUpdate");
const nameAriaWrite = document.getElementById("nameAriaWrite");
const introEmailAria = document.getElementById("introEmailAria");
const introEmailAriaUpdate = document.getElementById("introEmailAriaUpdate");
const introEmailAriaWrite = document.getElementById("introEmailAriaWrite");
const githubAria = document.getElementById("githubAria");
const githubAriaUpdate = document.getElementById("githubAriaUpdate");
const githubAriaWrite = document.getElementById("githubAriaWrite");
const facebookAria = document.getElementById("facebookAria");
const facebookAriaUpdate = document.getElementById("facebookAriaUpdate");
const facebookAriaWrite = document.getElementById("facebookAriaWrite");
const xAria = document.getElementById("xAria");
const xAriaUpdate = document.getElementById("xAriaUpdate");
const xAriaWrite = document.getElementById("xAriaWrite");
const homepageAria = document.getElementById("homepageAria");
const homepageAriaUpdate = document.getElementById("homepageAriaUpdate");
const homepageAriaWrite = document.getElementById("homepageAriaWrite");
const thumbInput = document.getElementById("thumbInput");
const socialLength = 50;

function updateThumbOpen() {
    thumbInput.click();
}

function updateThumb() {
    let settingProfileThumb = document.getElementById("profile-pfp");
    let settingProfileThumbUpdatable = document.getElementById("profile-pfp-updatable");
    let profilePfpUpdatableMobile = document.getElementById("profile-pfp-updatable-mobile");
    let profilePfpMobile = document.getElementById("profile-pfp-updatable-mobile");

    let formData = new FormData();
    formData.append("file", thumbInput.files[0]);

    axios.post("/api/v1/image/thumbnail", formData, {
        headers: {
            'Content-Type': `multipart/form-data;`,
        }
    })
        .then((result) => {
            settingProfileThumb.src = result.data.url;
            settingProfileThumb.alt = result.data.originName;
            settingProfileThumbUpdatable.src = result.data.url;
            settingProfileThumbUpdatable.alt = result.data.originName;
            profilePfpUpdatableMobile.src = result.data.url;
            profilePfpUpdatableMobile.alt = result.data.originName;
            profilePfpMobile.src = result.data.url;
            profilePfpMobile.alt = result.data.originName;
        })
        .catch(() => {
            console.log("실패하였습니다.");
        })
}

function nameAriaInvisible() {
    nameAria.className = "col-7 d-none";
    nameAriaUpdate.className = "col-2 text-center d-none";
    nameAriaWrite.className = "col-9 input-group";
}

function nameAriaVisible() {
    nameAria.className = "col-7";
    nameAriaUpdate.className = "col-2 text-center";
    nameAriaWrite.className = "col-9 input-group d-none";
}

function introEmailAriaInvisible() {
    introEmailAria.className = "col-7 d-none";
    introEmailAriaUpdate.className = "col-2 text-center d-none";
    introEmailAriaWrite.className = "col-9 input-group";
}

function introEmailAriaVisible() {
    introEmailAria.className = "col-7";
    introEmailAriaUpdate.className = "col-2 text-center";
    introEmailAriaWrite.className = "col-9 input-group d-none";
}

function githubAriaInvisible() {
    githubAria.className = "col-7 d-none";
    githubAriaUpdate.className = "col-2 text-center d-none";
    githubAriaWrite.className = "col-9 input-group";
}

function githubAriaVisible() {
    githubAria.className = "col-7";
    githubAriaUpdate.className = "col-2 text-center";
    githubAriaWrite.className = "col-9 input-group d-none";
}

function facebookAriaInvisible() {
    facebookAria.className = "col-7 d-none";
    facebookAriaUpdate.className = "col-2 text-center d-none";
    facebookAriaWrite.className = "col-9 input-group";
}

function facebookAriaVisible() {
    facebookAria.className = "col-7";
    facebookAriaUpdate.className = "col-2 text-center";
    facebookAriaWrite.className = "col-9 input-group d-none";
}

function xAriaInvisible() {
    xAria.className = "col-7 d-none";
    xAriaUpdate.className = "col-2 text-center d-none";
    xAriaWrite.className = "col-9 input-group";
}

function xAriaVisible() {
    xAria.className = "col-7";
    xAriaUpdate.className = "col-2 text-center";
    xAriaWrite.className = "col-9 input-group d-none";
}

function homepageAriaInvisible() {
    homepageAria.className = "col-7 d-none";
    homepageAriaUpdate.className = "col-2 text-center d-none";
    homepageAriaWrite.className = "col-9 input-group";
}

function homepageAriaVisible() {
    homepageAria.className = "col-7";
    homepageAriaUpdate.className = "col-2 text-center";
    homepageAriaWrite.className = "col-9 input-group d-none";
}

function saveName() {
    const settingNameInput = document.getElementById("settingNameInput");
    const nameP = document.getElementById("nameP");

    if (settingNameInput.value === nameP.innerText) {
        nameAriaVisible();
        return;
    }

    if (!settingNameValidate()) {
        alert("이름은 최소 2자, 최대 20자로 한글과 영어만 입력 가능합니다.");
        return;
    }

    axios.put("/api/v1/users/profile/name",
        {
        "name" : settingNameInput.value
        })
        .then(() => {
            nameP.innerText = settingNameInput.value;
            nameAriaVisible();
        })
        .catch(() => {
            alert("변경에 실패하였습니다. 잠시 후 다시 시도해주세요.");
        })
}

function saveIntroEmail() {
    const settingIntroEmailInput = document.getElementById("settingIntroEmailInput");
    const introEmailP = document.getElementById("introEmailP");

    if (settingIntroEmailInput.value === introEmailP.innerText) {
        introEmailAriaVisible();
        return;
    }

    if (!socialValidate(settingIntroEmailInput)) {
        alert("최대 50자입니다.");
        return;
    }

    axios.put("/api/v1/users/profile/github",
        {
            "content" : settingIntroEmailInput.value
        })
        .then(() => {
            introEmailP.innerText = settingIntroEmailInput.value;
            introEmailAriaVisible();
        })
        .catch(() => {
            alert("변경에 실패하였습니다. 잠시 후 다시 시도해주세요.");
        })
}

function saveGithub() {
    const settingGithubInput = document.getElementById("settingGithubInput");
    const githubP = document.getElementById("githubP");

    if (settingGithubInput.value === githubP.innerText) {
        githubAriaVisible();
        return;
    }

    if (!socialValidate(settingGithubInput)) {
        alert("최대 50자입니다.");
        return;
    }

    axios.put("/api/v1/users/profile/github",
        {
            "content" : settingGithubInput.value
        })
        .then(() => {
            githubP.innerText = settingGithubInput.value;
            githubAriaVisible();
        })
        .catch(() => {
            alert("변경에 실패하였습니다. 잠시 후 다시 시도해주세요.");
        })
}

function saveFacebook() {
    const settingFacebookInput = document.getElementById("settingFacebookInput");
    const facebookP = document.getElementById("facebookP");

    if (settingFacebookInput.value === facebookP.innerText) {
        facebookAriaVisible();
        return;
    }

    if (!socialValidate(settingFacebookInput)) {
        alert("최대 50자입니다.");
        return;
    }

    axios.put("/api/v1/users/profile/facebook",
        {
            "content" : settingFacebookInput.value
        })
        .then(() => {
            facebookP.innerText = settingFacebookInput.value;
            facebookAriaVisible();
        })
        .catch(() => {
            alert("변경에 실패하였습니다. 잠시 후 다시 시도해주세요.");
        })
}

function saveX() {
    const settingXInput = document.getElementById("settingXInput");
    const xP = document.getElementById("xP");

    if (settingXInput.value === xP.innerText) {
        xAriaVisible();
        return;
    }

    if (!socialValidate(settingXInput)) {
        alert("최대 50자입니다.");
        return;
    }

    axios.put("/api/v1/users/profile/x",
        {
            "content" : settingXInput.value
        })
        .then(() => {
            xP.innerText = settingXInput.value;
            xAriaVisible();
        })
        .catch(() => {
            alert("변경에 실패하였습니다. 잠시 후 다시 시도해주세요.");
        })
}

function saveHomepage() {
    const settingHomepageInput = document.getElementById("settingHomepageInput");
    const homepageP = document.getElementById("homepageP");

    if (settingHomepageInput.value === homepageP.innerText) {
        homepageAriaVisible();
        return;
    }

    if (!socialValidate(settingHomepageInput)) {
        alert("최대 50자입니다.");
        return;
    }

    axios.put("/api/v1/users/profile/homepage",
        {
            "content" : settingHomepageInput.value
        })
        .then(() => {
            homepageP.innerText = settingHomepageInput.value;
            homepageAriaVisible();
        })
        .catch(() => {
            alert("변경에 실패하였습니다. 잠시 후 다시 시도해주세요.");
        })
}

async function settingNameValidate() {
    let isDuplicated = true;
    let settingNameInput = document.getElementById("settingNameInput");
    console.log(settingNameInput.value);

    await axios.get("/api/v1/users/name/verification?name=" + settingNameInput.value,)
        .then((result) => {
            isDuplicated = result.data;
            console.log(isDuplicated);

            if (settingNameReq.test(settingNameInput.value) && !isDuplicated) {
                settingNameInput.className = "form-control is-valid";
                return true;
            } else {
                settingNameInput.className = "form-control is-invalid";
                return false;
            }
        })
}

function socialValidate(element) {
    if (element.value.length > 50) {
        element.className = "form-control is-invalid";
        return false;
    }
    element.className = "form-control is-valid";
    return true;
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

const settingNameCheck = debounce(() => settingNameValidate());