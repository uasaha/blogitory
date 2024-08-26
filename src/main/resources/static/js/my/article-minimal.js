function getRelativeDate(date) {
    const now = new Date();
    const diff = (now - date) / 1000;

    const seconds = Math.floor(diff);
    const minutes = Math.floor(diff / 60);
    const hours = Math.floor(diff / 3600);
    const days = Math.floor(diff / 86400);
    const weeks = Math.floor(days / 7);
    const months = Math.floor(days / 30);
    const years = Math.floor(days / 365);

    if (seconds < 60) {
        return `${seconds}초 전`;
    }

    if (minutes < 60) {
        return `${minutes}분 전`;
    }

    if (hours < 24) {
        return `${hours}시간 전`;
    }

    if (days === 1) {
        return '어제';
    }

    if (days === 2) {
        return '그저께';
    }

    if (days < 7) {
        return `${days}일 전`;
    }

    if (weeks < 5) {
        return `${weeks}주일 전`;
    }

    if (months < 12) {
        const remainingWeeks = Math.floor((days % 30) / 7);
        return remainingWeeks > 0
            ? `${months}개월 ${remainingWeeks}주 전`
            : `${months}개월 전`;
    }

    const remainingMonths = months % 12;
    return remainingMonths > 0
        ? `${years}년 ${remainingMonths}개월 전`
        : `${years}년 전`;
}

function getFormattedNumber(num) {
    if (num < 1000) return num.toString();
    const units = ["k", "M", "B", "T"];
    let unitIndex = -1;
    let formattedNum = num;

    while (formattedNum >= 1000 && unitIndex < units.length - 1) {
        formattedNum /= 1000;
        unitIndex++;
    }

    return `${Math.floor(formattedNum)}${units[unitIndex]}`;
}

function getThumbnailComponent(article) {
    let imageDiv = document.createElement('div');
    imageDiv.className = "ratio ratio-16x9 card-img-top cursor-pointer";
    imageDiv.addEventListener("click", function (event) {
        location.href = "/" + article.postUrl;
    });

    let img = document.createElement('img');
    img.src = article.thumb;
    img.style.objectFit = "cover";
    img.addEventListener('error', () => {
        imageDiv.classList.add('d-none');
    });

    imageDiv.appendChild(img);

    return imageDiv;
}

function getWriterComponent(article) {
    let writerDiv = document.createElement('div');
    writerDiv.className = "col-12";

    let blogLinkA = document.createElement('a');
    blogLinkA.href = "/" + article.blogUrl;
    blogLinkA.className = "nav-link d-flex lh-1 text-reset p-0";

    let blogPfpImg = document.createElement('img');
    blogPfpImg.className = "avatar avatar-sm";

    if (article.blogPfp != null) {
        blogPfpImg.src = article.blogPfp;
        blogPfpImg.addEventListener('error', () => {
            blogPfpImg.classList.add('d-none');
        });
        blogLinkA.appendChild(blogPfpImg);
    }

    let blogInfoDiv = document.createElement('div');
    blogInfoDiv.className = "ps-2";

    let blogNameDiv = document.createElement('div');
    let blogNameSpan = document.createElement('span');
    blogNameSpan.innerText = article.blogName;
    blogNameDiv.appendChild(blogNameSpan);

    let userNameDiv = document.createElement('div');
    userNameDiv.className = "mt-1 small text-secondary";
    let userNameSpan = document.createElement('span');
    userNameSpan.innerText = "@" + article.username;
    userNameDiv.appendChild(userNameSpan);

    blogInfoDiv.appendChild(blogNameDiv);
    blogInfoDiv.appendChild(userNameDiv);

    blogLinkA.appendChild(blogInfoDiv);

    writerDiv.appendChild(blogLinkA);

    return writerDiv;
}

function getTitleComponent(article) {
    let titleDiv = document.createElement('div');
    titleDiv.className = "col-12 mt-2 mb-1 cursor-pointer";
    titleDiv.addEventListener("click", function (event) {
        location.href = "/" + article.postUrl;
    });

    let titleSpan = document.createElement('span');
    titleSpan.className = "post-title";
    titleSpan.innerText = article.title;

    titleDiv.appendChild(titleSpan);

    return titleDiv;
}

function getSummaryComponent(article) {
    let summaryDiv = document.createElement('div');
    summaryDiv.className = "col-12 post-summary";

    let summaryP = document.createElement('p');
    summaryP.className = "text-over-summary-2 mb-1";

    if (article.thumb == null || article.thumb == "") {
        summaryP.className = "text-over-summary-8 mb-1";
        summaryDiv.className = "col-12";
    }

    summaryP.innerText = article.summary;

    summaryDiv.appendChild(summaryP);

    return summaryDiv;
}

function getFooterComponent(article) {
    let footerDiv = document.createElement('div');
    footerDiv.className = "col-12 mt-2";

    let dateDiv = document.createElement('div');
    dateDiv.className = "float-start";
    let dateSpan = document.createElement('span');
    dateSpan.className = "text-secondary";
    dateSpan.innerText = getRelativeDate(new Date(article.createdAt));
    dateDiv.appendChild(dateSpan);

    footerDiv.appendChild(dateDiv);

    let infoDiv = document.createElement('div');
    infoDiv.className = "float-end";

    let heartSvg = document.createElement('span');
    heartSvg.innerHTML =
        '<svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24"\n' +
        '    viewBox="0 0 24 24"\n' +
        '    stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"\n' +
        '    stroke-linejoin="round">\n' +
        '    <path stroke="none" d="M0 0h24v24H0z" fill="none"/>\n' +
        '    <path d="M19.5 12.572l-7.5 7.428l-7.5 -7.428a5 5 0 1 1 7.5 -6.566a5 5 0 1 1 7.5 6.572"/>\n' +
        '</svg>';
    infoDiv.appendChild(heartSvg);

    let heartCntSpan = document.createElement('span');
    heartCntSpan.className = "ms-1";
    heartCntSpan.innerText = getFormattedNumber(article.heart);
    infoDiv.appendChild(heartCntSpan);

    let commentSvg = document.createElement('span');
    commentSvg.innerHTML =
        '<svg xmlns="http://www.w3.org/2000/svg" class="icon ms-2" width="24" height="24"\n' +
        '    viewBox="0 0 24 24"\n' +
        '    stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"\n' +
        '    stroke-linejoin="round">\n' +
        '    <path stroke="none" d="M0 0h24v24H0z" fill="none"/>\n' +
        '    <path d="M3 20l1.3 -3.9c-2.324 -3.437 -1.426 -7.872 2.1 -10.374c3.526 -2.501 8.59 -2.296 11.845 .48c3.255 2.777 3.695 7.266 1.029 10.501c-2.666 3.235 -7.615 4.215 -11.574 2.293l-4.7 1"/>\n' +
        '</svg>';

    infoDiv.appendChild(commentSvg);

    let commentCntSpan = document.createElement('span');
    commentCntSpan.className = "ms-1";
    commentCntSpan.innerText = getFormattedNumber(article.comment);
    infoDiv.appendChild(commentCntSpan);


    footerDiv.appendChild(infoDiv);

    return footerDiv;
}

function getPostBodyComponent(article) {
    let postBodyDiv = document.createElement('div');
    postBodyDiv.className = "card-body";

    postBodyDiv.appendChild(getWriterComponent(article));
    postBodyDiv.appendChild(getTitleComponent(article));
    postBodyDiv.appendChild(getSummaryComponent(article));
    postBodyDiv.appendChild(getFooterComponent(article));

    return postBodyDiv;
}

function getPostComponent(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12 col-md-6 col-lg-4 col-xl-3 mt-3";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card zoom";

    postCardDiv.appendChild(getThumbnailComponent(article));
    postCardDiv.appendChild(getPostBodyComponent(article));

    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function getPostComponentForProfile(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12 col-lg-6";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card zoom";

    postCardDiv.appendChild(getThumbnailComponent(article));
    postCardDiv.appendChild(getPostBodyComponent(article));

    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function getPostsComponentForBlog(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12 col-md-6";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card zoom";

    postCardDiv.appendChild(getThumbnailComponent(article));
    postCardDiv.appendChild(getPostBodyComponent(article));

    postDiv.appendChild(postCardDiv);

    return postDiv;
}