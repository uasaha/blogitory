const profileName = document.querySelector('#profile-name');
const profileBio = document.querySelector('#profile-bio');
const profileFollow = document.querySelector('#profile-follow');
const profileButton = document.querySelector('#profile-button');
const profileEmail = document.querySelector('#profile-email');
const profilePfp = document.querySelector('#profile-pfp');
const profilePfpUpdatable = document.querySelector('#profile-pfp-updatable');
const profileUpdatable = document.querySelector('#profile-updatable');
const profileButtonUpdatable = document.querySelector('#profile-button-updatable');

function disappearProfiles() {
    profileName.className = "profile-name d-none";
    profileBio.className = "row justify-content-center d-none";
    profileFollow.className = "col-12 d-none";
    profileButton.className = "row justify-content-center d-none";
    profileEmail.className = "row justify-content-center d-none";
    profilePfp.className = "avatar avatar-md d-none";
    profilePfpUpdatable.className = "avatar avatar-md cursor-pointer";
    profileUpdatable.className = "row justify-content-center";
    profileButtonUpdatable.className = "row justify-content-end";
}

function showProfiles() {
    profileName.className = "profile-name";
    profileBio.className = "row justify-content-center";
    profileFollow.className = "col-12";
    profileButton.className = "row justify-content-center";
    profileEmail.className = "row justify-content-center";
    profilePfp.className = "avatar avatar-md";
    profilePfpUpdatable.className = "avatar avatar-md cursor-pointer d-none";
    profileUpdatable.className = "row justify-content-center d-none";
    profileButtonUpdatable.className = "row justify-content-end d-none";
}

function saveProfiles() {
    const profileNameUpdatable = document.querySelector('#profile-name-updatable');
    const profileBioUpdatable = document.querySelector('#profile-bio-updatable');
    const profileEmailUpdatable = document.querySelector('#profile-email-updatable');
    const $$profileLinksUpdatable = document.querySelectorAll('.profile-links-updatable');
    let links = [];

    pushLink(links, $$profileLinksUpdatable[0].value);
    pushLink(links, $$profileLinksUpdatable[1].value);
    pushLink(links, $$profileLinksUpdatable[2].value);
    pushLink(links, $$profileLinksUpdatable[3].value);
    pushLink(links, $$profileLinksUpdatable[4].value);

    console.log(links);

    axios.put("/api/v1/users/profiles", {
        "name" : profileNameUpdatable.value,
        "bio" : profileBioUpdatable.value,
        "email" : profileEmailUpdatable.value,
        "linkList" : links
    }).then(() => {
        showProfiles();
        document.location.reload();
    })
}

function pushLink(linkArray, val) {
    if (val !== '') {
        linkArray.push(val);
    }
}

const profileNameMobile = document.querySelector('#profile-name-mobile');
const profileBioMobile = document.querySelector('#profile-bio-mobile');
const profileFollowMobile = document.querySelector('#profile-follow-mobile');
const profileButtonMobile = document.querySelector('#profile-button-mobile');
const profilePfpMobile = document.querySelector('#profile-pfp-mobile');
const profilePfpUpdatableMobile = document.querySelector('#profile-pfp-updatable-mobile');
const profileUpdatableMobile = document.querySelector('#profile-updatable-mobile');
const profileButtonUpdatableMobile = document.querySelector('#profile-button-updatable-mobile');


function disappearProfilesMobile() {
    profileNameMobile.className = "col-8 d-none";
    profileBioMobile.className = "row justify-content-center d-none";
    profileFollowMobile.className = "col-12 d-none";
    profileButtonMobile.className = "row justify-content-center d-none";
    profilePfpMobile.className = "avatar avatar-md d-none";
    profilePfpUpdatableMobile.className = "avatar avatar-md cursor-pointer";
    profileUpdatableMobile.className = "row justify-content-center";
    profileButtonUpdatableMobile.className = "row justify-content-end";
}

function showProfilesMobile() {
    profileNameMobile.className = "col-8";
    profileBioMobile.className = "row justify-content-center";
    profileFollowMobile.className = "col-12";
    profileButtonMobile.className = "row justify-content-center";
    profilePfpMobile.className = "avatar avatar-md";
    profilePfpUpdatableMobile.className = "avatar avatar-md cursor-pointer d-none";
    profileUpdatableMobile.className = "row justify-content-center d-none";
    profileButtonUpdatableMobile.className = "row justify-content-end d-none";
}

function saveProfilesMobile() {
    const profileNameUpdatable = document.querySelector('#profile-name-updatable-mobile');
    const profileBioUpdatable = document.querySelector('#profile-bio-updatable-mobile');
    const profileEmailUpdatable = document.querySelector('#profile-email-updatable-mobile');
    const $$profileLinksUpdatable = document.querySelectorAll('.profile-links-updatable-mobile');
    let links = [];

    pushLink(links, $$profileLinksUpdatable[0].value);
    pushLink(links, $$profileLinksUpdatable[1].value);
    pushLink(links, $$profileLinksUpdatable[2].value);
    pushLink(links, $$profileLinksUpdatable[3].value);
    pushLink(links, $$profileLinksUpdatable[4].value);

    console.log(links);

    axios.put("/api/v1/users/profiles", {
        "name" : profileNameUpdatable.value,
        "bio" : profileBioUpdatable.value,
        "email" : profileEmailUpdatable.value,
        "linkList" : links
    }).then(() => {
        showProfiles();
        document.location.reload();
    })
}

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