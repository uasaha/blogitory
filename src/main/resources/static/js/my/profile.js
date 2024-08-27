const profileName = document.querySelector('#profile-name');
const profileBio = document.querySelector('#profile-bio');
const profileFollow = document.querySelector('#profile-follow');
const profileButton = document.querySelector('#profile-button');
const profileEmail = document.querySelector('#profile-email');
const profilePfp = document.querySelector('#profile-pfp');
const profilePfpUpdatable = document.querySelector('#profile-pfp-updatable');
const profileUpdatable = document.querySelector('#profile-updatable');
const profileButtonUpdatable = document.querySelector('#profile-button-updatable');
let nameReg = /^[a-zA-Zㄱ-ㅣ가-힣\d\s]{2,30}$/;
let bioReg = /^.{0,300}$/;

function isNullOrEmpty(value) {
    return value === null || value === '';
}

function nameValidate(element) {
    let cantName = document.getElementById("cant-name");
    let cantNameMobile = document.getElementById("cant-name-mobile");
    if (!nameReg.test(element.value)) {
        cantName.classList.remove("d-none");
        cantNameMobile.classList.remove("d-none");
        element.className = "form-control is-invalid";
        return false;
    } else {
        cantName.classList.add("d-none");
        cantNameMobile.classList.add("d-none");
        element.className = "form-control is-valid";
        return true;
    }
}

function bioValidate(element) {
    let cantBio = document.getElementById("cantBio");
    let cantBioMobile = document.getElementById("cant-bio-mobile");

    if (!bioReg.test(element.value)) {
        cantBio.classList.remove("d-none");
        cantBioMobile.classList.remove("d-none");
        return false;
    }
    cantBio.classList.add("d-none");
    cantBioMobile.classList.add("d-none");
    return true;
}

function disappearProfiles() {
    profileName.className = "profile-name d-none";

    if (!isNullOrEmpty(profileBio)) {
        profileBio.className = "row justify-content-center d-none";
    }
    profileFollow.className = "col-12 d-none";
    profileButton.className = "row justify-content-center d-none";

    if (!isNullOrEmpty(profileEmail)) {
        profileEmail.className = "row justify-content-center d-none";
    }
    profilePfp.className = "avatar avatar-md d-none rounded-circle";
    profilePfpUpdatable.className = "avatar avatar-md cursor-pointer rounded-circle";
    profileUpdatable.className = "row justify-content-center";
    profileButtonUpdatable.className = "row justify-content-end";
}

function showProfiles() {
    profileName.className = "profile-name";

    if (!isNullOrEmpty(profileBio)) {
        profileBio.className = "row justify-content-center";
    }
    profileFollow.className = "col-12";
    profileButton.className = "row justify-content-center";

    if (!isNullOrEmpty(profileEmail)) {
        profileEmail.className = "row justify-content-center";
    }
    profilePfp.className = "avatar avatar-md rounded-circle";
    profilePfpUpdatable.className = "avatar avatar-md cursor-pointer d-none rounded-circle";
    profileUpdatable.className = "row justify-content-center d-none";
    profileButtonUpdatable.className = "row justify-content-end d-none";
}

function saveProfiles() {
    const profileNameUpdatable = document.querySelector('#profile-name-updatable');
    const profileBioUpdatable = document.querySelector('#profile-bio-updatable');
    const profileEmailUpdatable = document.querySelector('#profile-email-updatable');
    const $$profileLinksUpdatable = document.querySelectorAll('.profile-links-updatable');
    let links = [];

    if (!nameValidate(profileNameUpdatable)) {
        alert("이름을 확인해주세요.")
    }

    pushLink(links, $$profileLinksUpdatable[0].value);
    pushLink(links, $$profileLinksUpdatable[1].value);
    pushLink(links, $$profileLinksUpdatable[2].value);
    pushLink(links, $$profileLinksUpdatable[3].value);
    pushLink(links, $$profileLinksUpdatable[4].value);

    axios.put("/api/users/profiles", {
        "name" : profileNameUpdatable.value,
        "bio" : profileBioUpdatable.value,
        "email" : profileEmailUpdatable.value,
        "linkList" : links
    }).then(() => {
        showProfiles();
        document.location.reload();
    }).catch(() => {
        openFailedAlerts("저장에 실패하였습니다.")
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

    if (!isNullOrEmpty(profileBioMobile)) {
        profileBioMobile.className = "row justify-content-center d-none";
    }
    profileFollowMobile.className = "col-12 d-none";
    profileButtonMobile.className = "row justify-content-center d-none";
    profilePfpMobile.className = "avatar avatar-md d-none rounded-circle";
    profilePfpUpdatableMobile.className = "avatar avatar-md cursor-pointer rounded-circle";
    profileUpdatableMobile.className = "row justify-content-center";
    profileButtonUpdatableMobile.className = "row justify-content-end";
}

function showProfilesMobile() {
    profileNameMobile.className = "col-8";
    if (!isNullOrEmpty(profileBioMobile)) {
        profileBioMobile.className = "row justify-content-center";
    }
    profileFollowMobile.className = "col-12";
    profileButtonMobile.className = "row justify-content-center";
    profilePfpMobile.className = "avatar avatar-md rounded-circle";
    profilePfpUpdatableMobile.className = "avatar avatar-md cursor-pointer d-none rounded-circle";
    profileUpdatableMobile.className = "row justify-content-center d-none";
    profileButtonUpdatableMobile.className = "row justify-content-end d-none";
}

function saveProfilesMobile() {
    const profileNameUpdatable = document.querySelector('#profile-name-updatable-mobile');
    const profileBioUpdatable = document.querySelector('#profile-bio-updatable-mobile');
    const profileEmailUpdatable = document.querySelector('#profile-email-updatable-mobile');
    const $$profileLinksUpdatable = document.querySelectorAll('.profile-links-updatable-mobile');
    let links = [];

    if (!nameValidate(profileNameUpdatable)) {
        openWarnAlerts("이름을 확인해주세요. 이름은 한글과 영어만 가능합니다.")
    }

    pushLink(links, $$profileLinksUpdatable[0].value);
    pushLink(links, $$profileLinksUpdatable[1].value);
    pushLink(links, $$profileLinksUpdatable[2].value);
    pushLink(links, $$profileLinksUpdatable[3].value);
    pushLink(links, $$profileLinksUpdatable[4].value);

    console.log(links);

    axios.put("/api/users/profiles", {
        "name" : profileNameUpdatable.value,
        "bio" : profileBioUpdatable.value,
        "email" : profileEmailUpdatable.value,
        "linkList" : links
    }).then(() => {
        showProfiles();
        document.location.reload();
    }).catch(() => {
        openFailedAlerts("저장에 실패하였습니다.")
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

    axios.post("/api/images/thumbnail", formData, {
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
            openSuccessAlerts("저장되었습니다.")
        })
        .catch(() => {
            openFailedAlerts("저장에 실패하였습니다.")
        })
}

Date.prototype.yyyymmdd = function() {
    var mm = this.getMonth() + 1;
    var dd = this.getDate();

    return [this.getFullYear(),
        (mm>9 ? '' : '0') + mm,
        (dd>9 ? '' : '0') + dd
    ].join('-');
};

document.addEventListener("DOMContentLoaded", () => {
    //demo code start.
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    let monData = [];
    let tueData = [];
    let wedData = [];
    let thuData = [];
    let friData = [];
    let satData = [];
    let sunData = [];

    let daysInYear = 366;
    let now = new Date();

    let currentDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate());

    if (currentDate.getDay() === 1) {
        currentDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate() -1);
        daysInYear = daysInYear + 1;
    }
    if (currentDate.getDay() === 2) {
        currentDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate() -2);
        daysInYear = daysInYear + 2;
    }
    if (currentDate.getDay() === 3) {
        currentDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate() -3);
        daysInYear = daysInYear + 3;
    }
    if (currentDate.getDay() === 4) {
        currentDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate() -4);
        daysInYear = daysInYear + 4;
    }
    if (currentDate.getDay() === 5) {
        currentDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate() -5);
        daysInYear = daysInYear + 5;
    }
    if (currentDate.getDay() === 6) {
        currentDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate() -6);
        daysInYear = daysInYear + 6;
    }

    let allDayData = [];

    for (let i = 0; i <= daysInYear; i++) {
        allDayData.push(
            {
                name: currentDate.getDay(),
                date: currentDate.yyyymmdd(),
                value: getRandomInt(0, 5)
            }
        )

        currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
    }

    for (let i = 0; i <= daysInYear; i++) {
        if (allDayData[i].name === 0) {
            sunData.push({
                x: allDayData[i].date,
                y: allDayData[i].value,
            })
        }
        if (allDayData[i].name === 1) {
            monData.push({
                x: allDayData[i].date,
                y: allDayData[i].value,
            })
        }
        if (allDayData[i].name === 2) {
            tueData.push({
                x: allDayData[i].date,
                y: allDayData[i].value,
            })
        }
        if (allDayData[i].name === 3) {
            wedData.push({
                x: allDayData[i].date,
                y: allDayData[i].value,
            })
        }
        if (allDayData[i].name === 4) {
            thuData.push({
                x: allDayData[i].date,
                y: allDayData[i].value,
            })
        }
        if (allDayData[i].name === 5) {
            friData.push({
                x: allDayData[i].date,
                y: allDayData[i].value,
            })
        }
        if (allDayData[i].name === 6) {
            satData.push({
                x: allDayData[i].date,
                y: allDayData[i].value,
            })
        }
    }

    let data = [
        {
            name: 'Sat',
            data: satData
        },
        {
            name: 'Fri',
            data: friData
        },
        {
            name: 'Thu',
            data: thuData
        },
        {
            name: 'Wed',
            data: wedData
        },
        {
            name: 'Tue',
            data: tueData
        },
        {
            name: 'Mon',
            data: monData
        },
        {
            name: 'Sun',
            data: sunData
        },
    ];

    let options = {
        chart: {
            height: 175,
            type: 'heatmap',
            toolbar: {
                show: false,
            },
        },
        plotOptions: {
            heatmap: {
                radius: 0,
                distributed: true,
                colorScale: {
                    ranges: [{
                        from: 0,
                        to: 0,
                        name: '0',
                        color: '#BDBDBD'
                    }, {
                        from: 1,
                        to: 1,
                        name: '1',
                        color: '#8fb0c6'
                    }, {
                        from: 2,
                        to: 2,
                        name: '2',
                        color: '#4F7FB4'
                    }, {
                        from: 3,
                        to: 3,
                        name: '3',
                        color: '#335582'
                    }, {
                        from: 4,
                        to: 4,
                        name: '4',
                        color: '#11264f'
                    }, {
                        from: 5,
                        to: 5,
                        name: '5++',
                        color: '#0C1152'
                    },
                    ],
                    inverse: true,
                }
            }
        },
        dataLabels: {
            enabled: false,
        },
        stroke: {
            show: false,
        },
        series: data,
        xaxis: {
            type: "datetime",
            labels: {
                format: 'MM/dd'
            },
            axisBorder: { show: false },
            tickAmount: 12,
            tickPlacement: 'between',
            axisTicks: { show: true },
        },
        tooltip: {
            intersect: false,
            x: {
                show: true,
                format: "yyyy/MM/dd"
            },
        },
        grid: {
            show: false,
        },
    };

    let chart = new ApexCharts(document.querySelector("#heatmap-chart"), options)
    let chartMobile = new ApexCharts(document.querySelector("#heatmap-chart-mobile"), options);
    chart.render();
    chartMobile.render();
});

function follow(username) {
    axios.post("/api/follow/@" + username)
        .then(res => {
            location.reload();
        })
        .catch(() => {
            openFailedAlerts("실패하였습니다.");
        });
}

function unFollow(username) {
    axios.delete("/api/follow/@" + username)
        .then(res => {
            location.reload();
        })
        .catch(() => {
            openFailedAlerts("실패하였습니다.");
        });
}
