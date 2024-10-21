document.addEventListener("DOMContentLoaded", function() {
    let granted = false;

    if (Notification.permission === 'granted') {
        granted = true;
    } else if (Notification.permission !== 'denied') {
        let permission = Notification.requestPermission();
        granted = permission === 'granted';
    }

    const eventSource = new EventSource(`/api/notifications/subscribe`);
    eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        const lastEventId = event.lastEventId;
        let message = data.message;

        if (data.content) {
            message += "\n" + data.content;
        }

        if (granted) {
            const notification = new window.Notification(
                'Blogitory',
                {
                    body: message,
                    badge: '/static/logo/logo-375582.png',
                    icon: '/static/logo/logo-375582.png',
                },
            );

            notification.onclick = (event) => {
                event.preventDefault();
                axios.get(`/api/notifications/` + lastEventId);
                window.open(data.url, "_blank");

            };
        } else {
            console.log("알림이 허용되어있지 않습니다. 알림을 받으시려면 허용해주세요.");
        }
    }
});

function getRelativeDate(date) {
    const now = new Date();
    const diff = (now - date) / 1000;

    const seconds = Math.floor(diff);
    const minutes = Math.floor(diff / 60);
    const hours = Math.floor(diff / 3600);

    const nowDate = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const inputDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
    const dayDiff = Math.floor((nowDate - inputDate) / 86400000); // 1일 = 86400000ms

    const weeks = Math.floor(dayDiff / 7);
    const months = Math.floor(dayDiff / 30);
    const years = Math.floor(dayDiff / 365);

    if (seconds < 0) {
        return `${seconds}초 후`;
    }

    if (seconds < 60) {
        return `${seconds}초 전`;
    }

    if (minutes < 60) {
        return `${minutes}분 전`;
    }

    if (hours < 24) {
        return `${hours}시간 전`;
    }

    if (dayDiff < 7) {
        return `${dayDiff}일 전`;
    }

    if (weeks < 5) {
        return `${weeks}주일 전`;
    }

    if (months < 12) {
        const remainingWeeks = Math.floor((dayDiff % 30) / 7);
        return remainingWeeks > 0
            ? `${months}개월 ${remainingWeeks}주 전`
            : `${months}개월 전`;
    }

    const remainingMonths = months % 12;
    return remainingMonths > 0
        ? `${years}년 ${remainingMonths}개월 전`
        : `${years}년 전`;
}


function getNoticeComponent(notice) {
    let itemDiv = document.createElement("div");
    itemDiv.className = "list-group-item";

    let rowDiv = document.createElement("div");
    rowDiv.className = "row align-items-center";

    let messageColDiv = document.createElement("div");
    messageColDiv.className = "col text-truncate";

    let messageDiv = document.createElement("div");
    messageDiv.textContent = notice.response.message;
    if (notice.read) {
        messageDiv.className = "text-secondary text-truncate cursor-pointer";
    } else {
        messageDiv.className = "text-truncate cursor-pointer";
    }

    messageDiv.addEventListener("click", () => {
        axios.get(`/api/notifications/` + notice.noticeNo);
        window.location.href = notice.response.url;
    })

    messageColDiv.appendChild(messageDiv);

    if (notice.response.content) {
        let messageContentDiv = document.createElement("div");
        messageContentDiv.textContent = notice.response.content;
        messageColDiv.appendChild(messageContentDiv);

        if (notice.read) {
            messageContentDiv.className = "text-secondary text-truncate";
        } else {
            messageContentDiv.className = "text-truncate";
        }
    }

    let messageTimeDiv = document.createElement("div");
    messageTimeDiv.classList = "text-secondary";
    messageTimeDiv.style.fontSize = "11px";
    messageTimeDiv.textContent = getRelativeDate(new Date(notice.createdAt));
    messageColDiv.appendChild(messageTimeDiv);

    rowDiv.appendChild(messageColDiv);

    let deleteBtnColDiv = document.createElement("div");
    deleteBtnColDiv.className = "col-auto";

    let deleteBtn = document.createElement("a");
    deleteBtn.className = "text-secondary cursor-pointer";
    deleteBtn.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-x"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M18 6l-12 12" /><path d="M6 6l12 12" /></svg>`;

    deleteBtn.addEventListener("click", () => {
        axios.delete(`/api/notifications/` + notice.noticeNo)
            .then(res => {
                itemDiv.classList.add("d-none");
            })
            .catch(err => {
                console.log("notice delete failed");
            });
    })

    deleteBtnColDiv.appendChild(deleteBtn);
    rowDiv.appendChild(deleteBtnColDiv);

    itemDiv.appendChild(rowDiv);

    return itemDiv;
}