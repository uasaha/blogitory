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
    imageDiv.className = "ratio ratio-4x3 card-img-top cursor-pointer";

    let img = document.createElement('img');
    img.src = article.thumb;
    img.style.objectFit = "cover";

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

function getPostBodyNoWriterComponent(article) {
    let postBodyDiv = document.createElement('div');
    postBodyDiv.className = "card-body";

    postBodyDiv.appendChild(getTitleComponent(article));
    postBodyDiv.appendChild(getSummaryComponent(article));
    postBodyDiv.appendChild(getFooterComponent(article));

    return postBodyDiv;
}

function getPostComponent(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12 col-md-6 col-lg-4 col-xl-3 mt-3";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card zoom posts-min cursor-pointer";
    postCardDiv.addEventListener("click", () => {
        location.href = "/" + article.postUrl;
    });

    if (article.thumb) {
        postCardDiv.appendChild(getThumbnailComponent(article));
    }

    postCardDiv.appendChild(getPostBodyComponent(article));

    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function getPostComponentForProfile(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12 col-lg-6";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card zoom posts-min cursor-pointer";
    postCardDiv.addEventListener("click", () => {
        location.href = "/" + article.postUrl;
    });

    if (article.thumb) {
        postCardDiv.appendChild(getThumbnailComponent(article));
    }

    postCardDiv.appendChild(getPostBodyComponent(article));

    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function getPostsComponentForBlog(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12 col-md-6";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card zoom posts-min cursor-pointer";
    postCardDiv.addEventListener("click", () => {
        location.href = "/" + article.postUrl;
    })

    if (article.thumb) {
        postCardDiv.appendChild(getThumbnailComponent(article));
    }

    postCardDiv.appendChild(getPostBodyNoWriterComponent(article));

    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function getListStylePostsComponentForBlog(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12";
    postDiv.style = "min-height: 9rem";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card cursor-pointer zoom cursor-pointer";

    postCardDiv.addEventListener("click", () => {
        location.href = "/" + article.postUrl;
    })

    let postBodyDiv = document.createElement('div');
    postBodyDiv.className = "row g-0";

    if (article.thumb) {
        let postThumbCol = document.createElement('div');
        postThumbCol.className = "col-4";

        let postThumbDiv = document.createElement('div');
        postThumbDiv.className = "ratio ratio-4x3 card-img-start";

        let postThumbImg = document.createElement('img');
        postThumbImg.src = article.thumb;
        postThumbImg.alt = "thumb";
        postThumbImg.style.objectFit = "cover";

        postThumbImg.addEventListener('error', () => {
            postThumbCol.classList.add("d-none");
        });

        postThumbDiv.appendChild(postThumbImg);
        postThumbCol.appendChild(postThumbDiv);
        postBodyDiv.appendChild(postThumbCol);
    }

    let postInfoDiv = document.createElement('div');
    postInfoDiv.className = "col";

    let postInfoBodyDiv = document.createElement('div');
    postInfoBodyDiv.className = "card-body";

    let postTitleDiv = document.createElement('div');
    postTitleDiv.className = "col-12";

    let postTitle = document.createElement('span');
    postTitle.className = "card-title";
    postTitle.innerText = article.title;
    postTitleDiv.appendChild(postTitle);

    let postSummaryDiv = document.createElement('div');
    postSummaryDiv.className = "col-12";

    let postSummary = document.createElement('span');
    postSummary.className = "text-over-summary-1";
    postSummary.innerText = article.summary;
    postSummaryDiv.appendChild(postSummary);

    postInfoBodyDiv.appendChild(postTitleDiv);
    postInfoBodyDiv.appendChild(postSummaryDiv);
    postInfoBodyDiv.appendChild(getFooterComponent(article));

    postInfoDiv.appendChild(postInfoBodyDiv);
    postBodyDiv.appendChild(postInfoDiv)

    postCardDiv.appendChild(postBodyDiv);
    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function getListStylePostsComponent(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-12";
    postDiv.style = "min-height: 11rem";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card cursor-pointer zoom cursor-pointer";

    postCardDiv.addEventListener("click", () => {
        location.href = "/" + article.postUrl;
    })

    let postBodyDiv = document.createElement('div');
    postBodyDiv.className = "row g-0";

    if (article.thumb) {
        let postThumbCol = document.createElement('div');
        postThumbCol.className = "col-4";

        let postThumbDiv = document.createElement('div');
        postThumbDiv.className = "ratio ratio-4x3 card-img-start";

        let postThumbImg = document.createElement('img');
        postThumbImg.src = article.thumb;
        postThumbImg.alt = "thumb";
        postThumbImg.style.objectFit = "cover";

        postThumbImg.addEventListener('error', () => {
            postThumbCol.classList.add("d-none");
        });

        postThumbDiv.appendChild(postThumbImg);
        postThumbCol.appendChild(postThumbDiv);
        postBodyDiv.appendChild(postThumbCol);
    }

    let postInfoDiv = document.createElement('div');
    postInfoDiv.className = "col";

    let postInfoBodyDiv = document.createElement('div');
    postInfoBodyDiv.className = "card-body";


    let postTitleDiv = document.createElement('div');
    postTitleDiv.className = "col-12";

    let postTitle = document.createElement('span');
    postTitle.className = "card-title";
    postTitle.innerText = article.title;
    postTitleDiv.appendChild(postTitle);

    let postSummaryDiv = document.createElement('div');
    postSummaryDiv.className = "col-12";

    let postSummary = document.createElement('span');
    postSummary.className = "text-over-summary-1";
    postSummary.innerText = article.summary;
    postSummaryDiv.appendChild(postSummary);

    let writerDiv = getWriterComponent(article);
    writerDiv.classList.add("mb-3");

    postInfoBodyDiv.appendChild(writerDiv);
    postInfoBodyDiv.appendChild(postTitleDiv);
    postInfoBodyDiv.appendChild(postSummaryDiv);
    postInfoBodyDiv.appendChild(getFooterComponent(article));

    postInfoDiv.appendChild(postInfoBodyDiv);
    postBodyDiv.appendChild(postInfoDiv)

    postCardDiv.appendChild(postBodyDiv);
    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function getGridStylePostsComponent(article) {
    let postDiv = document.createElement('div');
    postDiv.className = "col-3";

    let postCardDiv = document.createElement('div');
    postCardDiv.className = "card cursor-pointer zoom cursor-pointer";

    postCardDiv.addEventListener("click", () => {
        location.href = "/" + article.postUrl;
    })

    let postThumbDiv = document.createElement('div');
    postThumbDiv.className = "ratio ratio-1x1";

    if (article.thumb) {
        let postThumbImg = document.createElement('img');
        postThumbImg.src = article.thumb;
        postThumbImg.alt = "thumb";
        postThumbImg.style.objectFit = "cover";

        postThumbDiv.appendChild(postThumbImg);
    } else {
        let postTitleDiv = document.createElement('div');
        postTitleDiv.className = "d-flex justify-content-center align-items-center";

        let postTitle = document.createElement('span');
        postTitle.className = "font-weight-bold text-truncate";
        postTitle.style = "font-size: 1.5em;";
        postTitle.innerText = article.title;
        postTitleDiv.appendChild(postTitle);

        postThumbDiv.appendChild(postTitleDiv);
    }

    postCardDiv.appendChild(postThumbDiv);
    postDiv.appendChild(postCardDiv);

    return postDiv;
}

function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function getPostsByStyleForBlog(article) {
    let style = getCookie('view-style');

    if (!style) {
        setCookie('view-style', 'album', 365);
    }

    style = getCookie('view-style');

    document.getElementById("view-" + style + "-btn").classList.add("text-primary");

    if (style === 'album') {
        return getPostsComponentForBlog(article);
    }

    if (style === 'grid') {
        return getGridStylePostsComponent(article);
    }

    return getListStylePostsComponentForBlog(article);
}

function getPostsByStyle(article) {
    let style = getCookie('view-style');

    if (!style) {
        setCookie('view-style', 'album', 365);
    }

    style = getCookie('view-style');

    document.getElementById("view-" + style + "-btn").classList.add("text-primary");

    if (style === 'album') {
        return getPostComponent(article);
    }

    if (style === 'grid') {
        return getGridStylePostsComponent(article);
    }

    return getListStylePostsComponent(article);
}

/*
article = {
    username: "",
    title: "",
    createdAt: "",
    updatedAt: "",
    details: "",
    postsUrl: "",
    blogUrl: "",
    blogName: "",
    blogThumb: "",
*/
function getFeedPostsComponent(article) {
    let cardDiv = document.createElement('div');
    cardDiv.className = "card mb-4";

    let cardHeaderDiv = document.createElement('div');
    cardHeaderDiv.className = "card-header";

    let cardHeaderColDiv = document.createElement('div');
    cardHeaderColDiv.className = "col";

    let cardHeaderH1 = document.createElement('h1');
    cardHeaderH1.className = "card-title text-center";
    cardHeaderH1.style = "font-size: 25px;";
    cardHeaderH1.innerText = article.title;

    let cardHeaderDateP = document.createElement('p');
    cardHeaderDateP.className = "card-subtitle text-center";
    cardHeaderDateP.style = "font-size: 10px;";
    let date = getRelativeDate(new Date(article.createdAt));

    if (article.updatedAt) {
        date = date + " (수정 됨)";
    }

    cardHeaderDateP.innerText = date;

    cardHeaderColDiv.appendChild(cardHeaderH1);
    cardHeaderColDiv.appendChild(cardHeaderDateP);
    cardHeaderDiv.appendChild(cardHeaderColDiv);
    cardDiv.appendChild(cardHeaderDiv);

    let cardBodyDiv = document.createElement('div');
    cardBodyDiv.className = "card-body content p-5";

    let cardBodyRowDiv = document.createElement('div');
    cardBodyRowDiv.className = "row";

    let cardBodyViwer = document.createElement('div');
    cardBodyViwer.className = "viewer";
    cardBodyViwer.id = article.postsUrl;

    let viewer = new Editor.factory({
        el: cardBodyViwer,
        viewer: true,
    });

    viewer.setMarkdown(article.details);

    cardBodyRowDiv.appendChild(cardBodyViwer);
    cardBodyDiv.appendChild(cardBodyRowDiv);
    cardDiv.appendChild(cardBodyDiv);

    let cardFooterDiv = document.createElement('div');
    cardFooterDiv.className = "card-footer";

    let cardFooterRowDiv = document.createElement('div');
    cardFooterRowDiv.className = "row";

    let cardFooterColDiv = document.createElement('div');
    cardFooterColDiv.className = "col";

    let cardFooterPostsA = document.createElement('a');
    cardFooterPostsA.className = "float-start";
    cardFooterPostsA.href = article.postsUrl;

    let cardFooterPostsDiv = document.createElement('div');
    cardFooterPostsDiv.className = "text-secondary";
    cardFooterPostsDiv.innerText = "자세히 보기";

    cardFooterPostsA.appendChild(cardFooterPostsDiv);
    cardFooterColDiv.appendChild(cardFooterPostsA);

    let cardFooterUserA = document.createElement('a');
    cardFooterUserA.className = "d-flex lh-1 text-body p-0 float-end";
    cardFooterUserA.href = "/" + article.blogUrl;

    let cardFooterBlogThumb = document.createElement('img');
    cardFooterBlogThumb.className = "avatar avatar-sm";
    cardFooterBlogThumb.src = article.blogThumb;
    cardFooterBlogThumb.alt = "pfp";

    cardFooterUserA.appendChild(cardFooterBlogThumb);

    let cardFooterUserDiv = document.createElement('div');
    cardFooterUserDiv.className = "ps-2";

    let cardFooterBlogName = document.createElement('div');
    cardFooterBlogName.innerText = article.blogName;

    let cardFooterUsername = document.createElement('div');
    cardFooterUsername.className = "mt-1 small text-secondary";
    cardFooterUsername.innerText = "@" + article.username;

    cardFooterUserDiv.appendChild(cardFooterBlogName);
    cardFooterUserDiv.appendChild(cardFooterUsername);
    cardFooterUserA.appendChild(cardFooterUserDiv);
    cardFooterColDiv.appendChild(cardFooterUserA);
    cardFooterRowDiv.appendChild(cardFooterColDiv);
    cardFooterDiv.appendChild(cardFooterRowDiv);
    cardDiv.appendChild(cardFooterDiv);

    return cardDiv;
}