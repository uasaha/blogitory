const nameReg = /^[a-zA-Zㄱ-ㅣ가-힣\d]{2,20}$/;
const socialLength = 50;

function pfpInputClick() {
    document.querySelector("#pfpInput").click();
}

function pfpChanged() {
    let pfp = document.querySelector("#pfp");
    let pfpInput = document.querySelector("#pfpInput");

    let formData = new FormData();
    formData.append("file", pfpInput.files[0]);

    axios.post("/api/images/thumbnail", formData, {
        headers: {
            'Content-Type': `multipart/form-data;`,
        }
    })
        .then((result) => {
            pfp.src = result.data.url;
            pfp.alt = result.data.originName;
        })
        .catch(() => {
            console.log("실패하였습니다.");
        })
}

function pfpRemove() {
    axios.put("/api/images/thumbnail", {
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then((result) => {
            pfp.src = "/static/icons/person.svg";
            pfp.alt = "no image";
        })
        .catch(() => {
            console.log("실패하였습니다.")
        })
}

function profileUpdate() {
    let name = document.querySelector("#name").value;
    let introEmail = document.querySelector("#introEmail").value;
    let bio = document.querySelector("#bio").value;
    let $$profileLinks = document.querySelectorAll('.profile-links');
    let saveFailed = document.querySelector("#save-failed");
    let links = [];

    if (!nameValidate(name)) {
        alert("이름을 확인해주세요.")
    }

    pushLink(links, $$profileLinks[0].value);
    pushLink(links, $$profileLinks[1].value);
    pushLink(links, $$profileLinks[2].value);
    pushLink(links, $$profileLinks[3].value);
    pushLink(links, $$profileLinks[4].value);

    axios.put("/api/users/profiles", {
        "name" : name,
        "bio" : bio,
        "email" : introEmail,
        "linkList" : links
    }).then(() => {
        document.location.reload();
    }).catch(() => {
        saveFailed.classList.remove("d-none")
    })
}

function pushLink(linkArray, val) {
    if (val !== '') {
        linkArray.push(val);
    }
}

function nameValidate(element) {
    return nameReg.test(element.value);
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

function updateAlert(type, checkbox) {
    let url = `/api/users/alerts?type=${type}&isOn=${checkbox.checked}`;

    axios.put(url, {})
        .catch(() => {
        alert("저장에 실패하였습니다.")
    })
}

let categoryDiv = document.querySelector("#categoryDiv");

function blogSwap() {
    let selectBox = document.querySelector('#blogSelect').selectedIndex;
    let selectedBlog = blogs[selectBox];
    let blogPfp = document.querySelector("#blogPfp");
    let blogName = document.querySelector("#blogName");
    let blogUrl = document.querySelector("#blogUrl");
    let blogOpen = document.querySelector("#blogOpen");
    let blogBio = document.querySelector("#blogBio");

    if (selectedBlog.thumbUrl === '' || selectedBlog.thumbUrl === null) {
        selectedBlog.thumbUrl = '/static/icons/reorder.svg';
    }

    blogPfp.src = selectedBlog.thumbUrl;
    blogName.value = selectedBlog.blogName;
    blogUrl.value = selectedBlog.blogUrl;
    blogOpen.value = selectedBlog.createdAt.split('T')[0];
    blogBio.value = selectedBlog.blogBio;
    let categories = selectedBlog.categories;

    categoryDiv.innerHTML = "<h3 class=\"card-title mt-4\">카테고리</h3>" +
        "<div class=\"row g-3 mb-1\">\n" +
        "    <div class=\"col-auto\">\n" +
        "        <input id=\"newCategory\" type=\"text\" class=\"form-control\" value=\"\">\n" +
        "    </div>\n" +
        "    <div class=\"col-auto\"><button class=\"btn\" onclick='addCategory()'>\n" +
        "        추가\n" +
        "    </button></div>\n" +
        "</div>";

    for (let i = 0; i < categories.length; i++) {
        if (categories[i].name === '' || categories[i].name === null) {
            return;
        }

        if (!categories[i].deleted) {
            drawCategory(categories[i]);
        }
    }
}

function drawCategory(category) {
    let categoryRow = document.createElement("div");
    categoryRow.className = "row g-3 mb-1";

    let categoryCol = document.createElement("div");
    categoryCol.className = "col-auto";
    let categoryUpdateCol = document.createElement("div");
    categoryUpdateCol.className = "col-auto";
    let categoryUpdateBtn = document.createElement("button");
    categoryUpdateBtn.className = "btn"

    let categoryDeleteCol = document.createElement("div");
    categoryDeleteCol.className = "col-auto";
    let categoryDeleteBtn = document.createElement("button");
    categoryDeleteBtn.className = "btn btn-ghost-danger"


    let categoryInput = document.createElement("input");
    categoryInput.type = "text";
    categoryInput.className = "form-control";
    categoryInput.id = "category-input-" + category.categoryNo;
    categoryInput.value = category.name;

    categoryUpdateBtn.appendChild(document.createTextNode("수정"));

    categoryUpdateBtn.addEventListener("click", function() {
        updateCategory(category.categoryNo)
    });

    categoryDeleteBtn.addEventListener("click", function() {
        deleteCategory(category.categoryNo)
    });

    categoryDeleteBtn.appendChild(document.createTextNode("삭제"));
    categoryDeleteCol.appendChild(categoryDeleteBtn);
    categoryUpdateCol.appendChild(categoryUpdateBtn);
    categoryRow.appendChild(categoryCol);
    categoryCol.appendChild(categoryInput);
    categoryRow.appendChild(categoryUpdateCol);
    categoryRow.appendChild(categoryDeleteCol);
    categoryDiv.appendChild(categoryRow);
}

function addCategory() {
    let newCategory = document.querySelector("#newCategory");
    let blogUrl = document.querySelector("#blogUrl").value;

    if (newCategory.value === "" || newCategory.value === null) {
        alert("카테고리를 입력하지 않았습니다.")
        return;
    }

    let url = `/api/categories?name=${newCategory.value}&blogUrl=${blogUrl}`;

    axios.post(url)
        .then((result) => {
            console.log(result);

        let savedCategory = result.data;

        drawCategory(savedCategory);

        newCategory.value = "";
    }).catch(() => {
        alert("저장에 실패하였습니다.");
    })
}

function updateCategory(categoryNo) {
    let categoryName = document.querySelector("#category-input-" + categoryNo);

    if (categoryName.value === "" || categoryName.value === null) {
        alert("카테고리를 입력하지 않았습니다.")
        return;
    }

    let url = `/api/categories/${categoryNo}?name=${categoryName.value}`;

    axios.put(url)
        .catch(() => {
            alert("저장에 실패하였습니다.")
        })
}

function deleteCategory(categoryNo) {
    let categoryName = document.querySelector("#category-input-" + categoryNo);
    let url = `/api/categories/${categoryNo}`;

    axios.delete(url)
        .then((result) => {
            categoryName.parentElement.parentElement.remove();
        })
        .catch(() => {
            alert("삭제에 실패하였습니다.")
        })
}

function blogNew() {
    if (blogs !== null && blogs.length >= 3) {
        alert("더 이상 개설할 수 없습니다.");
        return;
    }

    let blogPfp = document.querySelector("#blogPfp");
    let blogName = document.querySelector("#blogName");
    let blogUrl = document.querySelector("#blogUrl");
    let blogOpen = document.querySelector("#blogOpen");
    let blogBio = document.querySelector("#blogBio");
    let blogSaveBtn = document.querySelector("#blogSave");
    let blogCreateBtn = document.querySelector("#blogCreate");
    let blogSelectDiv = document.querySelector("#blogSelectDiv");
    let blogOpenDiv = document.querySelector("#blogOpenDiv");
    let categoryDiv = document.querySelector("#categoryDiv");
    let blogCancelBtn = document.querySelector("#blogCancel");
    let blogPfpDiv = document.querySelector("#blogPfpDiv");
    let urlPrefix = document.querySelector("#urlPrefix");

    blogOpen.readOnly = false;
    blogUrl.readOnly = false;
    blogSelectDiv.classList.add("d-none");
    blogOpenDiv.classList.add("d-none");
    categoryDiv.classList.add("d-none");
    blogSaveBtn.classList.add("d-none");
    blogPfpDiv.classList.add("d-none");
    blogCreateBtn.classList.remove("d-none");
    blogCancelBtn.classList.remove("d-none");
    urlPrefix.classList.remove("d-none");

    blogPfp.src = '';
    blogName.value = '';
    blogUrl.value = '';
    blogOpen.value = '';
    blogBio.value = '';
}

function blogCreateCancel() {
    let blogUrl = document.querySelector("#blogUrl");
    let blogOpen = document.querySelector("#blogOpen");
    let blogSaveBtn = document.querySelector("#blogSave");
    let blogCreateBtn = document.querySelector("#blogCreate");
    let blogSelectDiv = document.querySelector("#blogSelectDiv");
    let blogOpenDiv = document.querySelector("#blogOpenDiv");
    let categoryDiv = document.querySelector("#categoryDiv");
    let blogCancelBtn = document.querySelector("#blogCancel");
    let blogPfpDiv = document.querySelector("#blogPfpDiv");
    let urlPrefix = document.querySelector("#urlPrefix");

    blogOpen.readOnly = true;
    blogUrl.readOnly = true;
    blogSelectDiv.classList.remove("d-none");
    blogOpenDiv.classList.remove("d-none");
    categoryDiv.classList.remove("d-none");
    blogSaveBtn.classList.remove("d-none");
    blogPfpDiv.classList.remove("d-none");
    blogCreateBtn.classList.add("d-none");
    blogCancelBtn.classList.add("d-none");
    urlPrefix.classList.add("d-none");

    blogSwap();
}

function blogCreate() {
    let blogName = document.querySelector("#blogName");
    let blogUrl = document.querySelector("#blogUrl");
    let blogBio = document.querySelector("#blogBio");

    axios.post("/api/blogs", {
        "name": blogName.value,
        "url": blogUrl.value,
        "bio": blogBio.value
    }).then(() => {
        window.location.reload();
    }).catch(() => {
        alert("개설에 실패하였습니다.")
    })
}

function updateBlog() {
    let blogName = document.querySelector("#blogName");
    let blogUrl = document.querySelector("#blogUrl");
    let blogBio = document.querySelector("#blogBio");
    let saveSuccess = document.querySelector("#save-success");
    let saveFailed = document.querySelector("#save-failed");
    let url = `/api/blogs?url=${blogUrl.value}`;

    saveSuccess.className="d-none";
    saveFailed.className="d-none";

    axios.put(url, {
        "name": blogName.value,
        "bio": blogBio.value
    }).then((result) => {
        console.log(result);
        saveSuccess.classList.remove("d-none");
    }).catch(() => {
        saveFailed.classList.remove("d-none");
    });
}

function updateBlogThumbOpen() {
    let blogThumbInput = document.querySelector("#blogThumbInput");
    blogThumbInput.click();
}

function updateBlogThumb() {
    let blogThumbInput = document.querySelector("#blogThumbInput");
    let blogPfp = document.querySelector("#blogPfp");
    let blogUrl = document.querySelector("#blogUrl");

    let formData = new FormData();
    formData.append("file", blogThumbInput.files[0]);

    let url = `/api/blogs/thumbs?url=${blogUrl.value}`;

    axios.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then((result) => {
        blogPfp.src = result.data.url;
        blogPfp.alt = result.data.originName;
    }).catch(() => {
        alert("저장에 실패하였습니다.")
    });
}

function deleteBlogThumb() {
    let blogPfp = document.querySelector("#blogPfp");
    let blogUrl = document.querySelector("#blogUrl");
    let url = `/api/blogs/thumbs?url=${blogUrl.value}`;
    axios.delete(url).then(() => {
        document.location.reload();
    })
}

function changePasswordClick() {
    let email = document.querySelector("#memberEmail").value;
    let url = `/api/users/password?email=${email}`;
    console.log(url);

    axios.get(url)
        .then(() => {
            let sent = document.querySelector("#pwdSent");
            sent.classList.remove("d-none");
        })
        .catch(() => {
            let sentFail = document.querySelector("#pwdSentFail");
            sentFail.classList.remove("d-none");
        })
}

function deleteAccount() {
    let pwd = document.querySelector("#left-pwd").value;

    axios.post('/api/users', {
        "password": pwd
    })
        .then(() => {
            document.location.reload();
        })
        .catch((error) => {
            console.log(error);
            alert("계정 삭제 실패하였습니다.");
        })
}

function deleteBlog() {
    let blogUrl = document.querySelector("#blogUrl");
    let url = `/api/blogs/quit?url=${blogUrl.value}`;
    let pwd = document.querySelector("#left-pwd").value;

    axios.post(url, {
        "password": pwd,
    }).then(() => {
        document.location.reload();
    }).catch(() => {
        alert("폐쇄에 실패하였습니다.");
    })
}