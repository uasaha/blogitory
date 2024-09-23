const COMMENT_WRITE_FORM_ID = "comment-write-form";
const COMMENT_WRITE_AREA_ID = "comment-write-area";
const COMMENT_PREFIX = "comment-";
const COMMENT_WRITE = "comment-div";
const COMMENT_CONTENT_AREA_PREFIX = "comment-content-area-";
const COMMENT_MODIFY_AREA_PREFIX = "comment-modify-area-";
const COMMENT_MODIFY_BTN_PREFIX = "comment-modify-btn-";
const OPEN_CHILD_COMMENT_BTN_PREFIX = "open-child-comment-btn-";
const CHILD_COMMENT_WRITE_FORM_PREFIX = "child-comment-write-form-";
const CHILD_COMMENT_WRITE_BTN_PREFIX = "child-comment-write-btn-";
const CHILD_COMMENT_WRITE_INPUT_PREFIX = "child-comment-write-input-";
let CURRENT_COMMENT_PAGE = 0;

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

function openWriteForm() {
    let writeForms = document.querySelector('#' + COMMENT_WRITE_FORM_ID);
    let writeAreas = document.querySelector('#' + COMMENT_WRITE_AREA_ID);

    if (writeForms.classList.contains("d-none")) {
        writeForms.classList.remove("d-none");
        writeAreas.focus();
    } else {
        writeForms.classList.add("d-none");
        writeAreas.value = "";
    }
}

function openChildComment(el) {
    let commentNo = el.id.split("-")[2];
    let parentComment = document.querySelector("#" + COMMENT_PREFIX + commentNo);

    parentComment.after();
}

function clip() {
    let clipUrl = '';
    let clipTextarea = document.createElement("textarea");
    document.body.appendChild(clipTextarea);
    clipUrl = window.document.location.href;
    clipTextarea.value = clipUrl;
    clipTextarea.select();
    document.execCommand("copy");
    document.body.removeChild(clipTextarea);
    openSuccessAlerts("URL이 복사되었습니다.")
}

function registerComment() {
    let contents = document.querySelector("#" + COMMENT_WRITE_AREA_ID);

    if (contents.value == null || contents.value == "") {
        openWarnAlerts("댓글을 입력해주세요.");
        return
    }

    axios.post("/api/comments", {
        "postsUrl": posts.postUrl,
        "content": contents.value,
    }).then(() => {
        openSuccessAlerts("등록되었습니다.");
        location.reload();
    }).catch(() => {
        openFailedAlerts("잠시 후 다시 시도해주세요.");
    });
}

function registerChildComment(commentNo, childWriteInput) {
    let contents = document.querySelector("#" + CHILD_COMMENT_WRITE_INPUT_PREFIX + commentNo);

    if (contents.value == null || contents.value == "") {
        openWarnAlerts("댓글을 입력해주세요.");
        return;
    }

    let currentMember;

    if (loginMember != null) {
        currentMember = {
            username: loginMember.username,
            name: loginMember.name,
            thumb: loginMember.thumb,
        }
    }

    let writer = {
        username: currentMember.username,
        name: currentMember.name,
        pfp: currentMember.thumb
    }

    let commentContent = {
        commentNo: null,
        content: contents.value,
        createdAt: new Date(),
        updatedAt: null,
        isDeleted: false,
    }

    axios.post("/api/comments?pn=" + commentNo, {
        "postsUrl": posts.postUrl,
        "content": contents.value,
    }).then(() => {
        openSuccessAlerts("등록되었습니다.");
        let parentComment = document.getElementById(COMMENT_PREFIX + commentNo);
        parentComment.after(getChildComment(0, writer, currentMember, commentContent));
        childWriteInput.value = "";
        document.getElementById(CHILD_COMMENT_WRITE_FORM_PREFIX+commentNo).classList.add("d-none");
    }).catch(() => {
        openFailedAlerts("잠시 후 다시 시도해주세요.");
    })
}

function getComment(commentNo, writer, currentMember, commentContent, childCnt) {
    if (commentContent.isDeleted) {
        return getDeletedComment(commentNo, childCnt, "parent");
    }

    let comment = document.createElement("div");
    comment.id = COMMENT_PREFIX + commentNo;
    comment.className = "card-footer";

    let commentInner = document.createElement("div");
    commentInner.className = "row";

    commentInner.appendChild(getCommentHeader(commentNo, writer, currentMember, childCnt));
    commentInner.appendChild(getCommentBody(commentContent, currentMember));

    if (childCnt > 0) {
        commentInner.appendChild(getAnswerComment(commentNo, commentNo, childCnt, "개의 답글 보기", 0));
    }

    comment.appendChild(commentInner);

    return comment;
}

function getChildComment(commentNo, writer, currentMember, commentContent) {
    if (commentContent.isDeleted) {
        return getDeletedComment(commentNo, 0, "child");
    }

    let comment = document.createElement("div");
    comment.className = "card-footer ms-3";

    let commentInner = document.createElement("div");
    commentInner.className = "row";

    commentInner.appendChild(getCommentHeader(commentNo, writer, currentMember));
    commentInner.appendChild(getChildCommentBody(commentContent, currentMember));

    comment.appendChild(commentInner);

    return comment;
}

function getDeletedComment(commentNo, childCnt, type) {
    let deletedComment = document.createElement("div");
    deletedComment.id = COMMENT_PREFIX + commentNo;
    deletedComment.className = "card-footer";

    if (type === "child") {
        deletedComment.classList.add("ms-3");
    }

    let deletedCommentInner = document.createElement("div");
    deletedCommentInner.className = "row";

    let deletedCommentBody = document.createElement("div");
    deletedCommentBody.className = "row mt-2";

    let deletedCommentText = document.createElement("p");
    deletedCommentText.classList = "col-12 m-0";
    deletedCommentText.innerText = "삭제된 댓글입니다.";

    deletedCommentBody.appendChild(deletedCommentText);
    deletedCommentInner.appendChild(deletedCommentBody);
    deletedComment.appendChild(deletedCommentInner);

    if (childCnt > 0) {
        deletedCommentInner.appendChild(getAnswerComment(commentNo, commentNo, childCnt, "개의 답글 보기", 0));
    }

    return deletedComment;
}

function getCommentHeader(commentNo, writer, currentMember, childCnt) {
    let row = document.createElement("div");
    row.className = "row";

    row.appendChild(getCommentWriter(writer));

    if (currentMember != null && currentMember.username === writer.username) {
        row.appendChild(getCommentDropdown(commentNo, childCnt));
    }

    return row;
}

function getCommentWriter(writer) {
    let col = document.createElement("div");
    col.className = "col";

    col.innerHTML =
        "<div class=\"d-flex lh-1 text-body p-0\">\n" +
        "  <img class=\"avatar avatar-sm rounded-circle\"\n" +
        "    src=\"" + writer.pfp + "\"\n" +
        "    alt=\"Profile Image\">\n" +
        "  <div class=\"ps-2 overflow-hidden\">\n" +
        "  <div onclick=\"window.location.href='/@" + writer.username + "'\">" + writer.name + "</div>\n" +
        "  <div onclick=\"window.location.href='/@" + writer.username + "'\" class=\"mt-1 small text-secondary\">@" + writer.username + "</div>\n" +
        "  </div>\n" +
        "</div>";

    return col;
}

function getCommentDropdown(targetNo, childCnt) {
    let col = document.createElement("div");
    col.className = "col-1 d-flex justify-content-end";

    col.innerHTML =
        "<div class=\"dropdown\">\n" +
        "    <button type=\"button\" style=\"background: none; border: none\" data-bs-toggle=\"dropdown\">\n" +
        "      <svg\n" +
        "        xmlns=\"http://www.w3.org/2000/svg\"\n" +
        "        width=\"24\"\n" +
        "        height=\"24\"\n" +
        "        viewBox=\"0 0 24 24\"\n" +
        "        fill=\"none\"\n" +
        "        stroke=\"currentColor\"\n" +
        "        stroke-width=\"2\"\n" +
        "        stroke-linecap=\"round\"\n" +
        "        stroke-linejoin=\"round\"\n" +
        "        class=\"text-body icon icon-tabler icons-tabler-outline icon-tabler-dots\"\n" +
        "        >\n" +
        "        <path stroke=\"none\" d=\"M0 0h24v24H0z\" fill=\"none\"/>\n" +
        "        <path d=\"M5 12m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0\" />\n" +
        "        <path d=\"M12 12m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0\" />\n" +
        "        <path d=\"M19 12m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0\" />\n" +
        "      </svg>\n" +
        "    </button>\n" +
        "    <div class=\"dropdown-menu\">\n" +
        "      <a class=\"dropdown-item\" onclick=\"openModifyCommentForm(" + targetNo + ")\">\n" +
        "        수정\n" +
        "      </a>\n" +
        "      <a class=\"dropdown-item\" onclick=\"deleteComment(" + targetNo + ", " + childCnt +")\">\n" +
        "      삭제\n" +
        "      </a>\n" +
        "    </div>\n" +
        "</div>"

    return col;
}

function openModifyCommentForm(commentNo) {
    let commentContentArea = document.getElementById(COMMENT_CONTENT_AREA_PREFIX+commentNo);
    let commentModifyArea = document.createElement("textarea");
    commentModifyArea.className = "form-control";
    commentModifyArea.value = commentContentArea.innerText;
    commentModifyArea.rows = 4;
    commentModifyArea.id = COMMENT_MODIFY_AREA_PREFIX + commentNo;

    let commentModifyButton = document.createElement("button");
    commentModifyButton.textContent = "저장";
    commentModifyButton.className = "btn btn-outlined-secondary ms-auto mt-1";
    commentModifyButton.style = "width: 4rem";
    commentModifyButton.id = COMMENT_MODIFY_BTN_PREFIX + commentNo;
    commentModifyButton.addEventListener("click", () => {
        modifyComment(commentNo, commentContentArea, commentModifyArea, commentModifyButton);
    });

    commentContentArea.classList.add("d-none");
    commentContentArea.before(commentModifyArea);
    commentModifyArea.after(commentModifyButton);
}

function modifyComment(commentNo, commentContentArea, commentModifyArea, commentModifyButton) {
    axios.put("/api/comments/" + commentNo, {
        "contents": commentModifyArea.value,
    }).then(() => {
        openSuccessAlerts("댓글이 수정되었습니다.");
        commentContentArea.innerText = commentModifyArea.value;
        commentModifyArea.classList.add("d-none");
        commentModifyButton.classList.add("d-none");
        commentContentArea.classList.remove("d-none");
    }).catch(() => {
        openFailedAlerts("댓글 수정에 실패하였습니다.");
    })
}

function deleteComment(commentNo, childCnt) {
    axios.delete("/api/comments/" + commentNo)
        .then(() => {
            openSuccessAlerts("삭제되었습니다.");

            let comment = document.getElementById(COMMENT_PREFIX + commentNo);
            comment.classList.add("d-none");
            let deletedComment = getDeletedComment(commentNo, childCnt, "parent");
            comment.before(deletedComment);
        }).catch(() => {
            openFailedAlerts("삭제에 실패하였습니다.");
        });
}

function getCommentBody(commentContent, currentMember) {
    let row = document.createElement("div");
    row.className = "row mt-2";

    let content = document.createElement("p");
    content.className = "col-12 m-0";
    content.innerText = commentContent.content;
    content.id = COMMENT_CONTENT_AREA_PREFIX + commentContent.commentNo;
    row.appendChild(content);

    let info = document.createElement("p");
    info.className = "col-12 text-secondary m-0";
    info.style = "font-size: 10px";

    if (currentMember) {
        let openChildCommentBtn = document.createElement("a");
        openChildCommentBtn.id = "open-child-comment-btn-" + commentContent.commentNo;
        openChildCommentBtn.className = "text-secondary me-1";
        openChildCommentBtn.addEventListener("click", () => {

            let childWriteForm = document.querySelector('#' + CHILD_COMMENT_WRITE_FORM_PREFIX + commentContent.commentNo);

            if (!childWriteForm) {
                let target = document.getElementById("comment-" + commentContent.commentNo);

                target.after(drawChildCommentForm(commentContent.commentNo, currentMember));

                let childWriteBtn = document.querySelector('#' + CHILD_COMMENT_WRITE_BTN_PREFIX + commentContent.commentNo);
                let childWriteInput = document.querySelector('#' + CHILD_COMMENT_WRITE_INPUT_PREFIX + commentContent.commentNo);

                childWriteBtn.addEventListener("click", () => {
                    registerChildComment(commentContent.commentNo, childWriteInput);
                });
            } else {
                if (childWriteForm.classList.contains("d-none")) {
                    childWriteForm.classList.remove("d-none");
                } else {
                    childWriteForm.classList.add("d-none");
                }
            }
        });
        openChildCommentBtn.text = "답글 달기";
        info.appendChild(openChildCommentBtn);
    }


    let writeDateTime = document.createElement("span");
    writeDateTime.textContent = convertDateTime(commentContent.createdAt);

    let updateDateTime = document.createElement("span");

    if (commentContent.updatedAt) {
        writeDateTime.textContent = convertDateTime(commentContent.updatedAt);
        updateDateTime.classList.add("ms-1");
        updateDateTime.textContent = "(수정 됨)";
    }

    info.appendChild(writeDateTime);
    info.appendChild(updateDateTime);

    row.appendChild(info);

    return row;
}

function getChildCommentBody(commentContent) {
    let row = document.createElement("div");
    row.className = "row mt-2";

    let content = document.createElement("p");
    content.className = "col-12 m-0";
    content.innerText = commentContent.content;
    content.id = COMMENT_CONTENT_AREA_PREFIX + commentContent.commentNo;
    row.appendChild(content);

    let info = document.createElement("p");
    info.className = "col-12 text-secondary m-0";
    info.style = "font-size: 10px";

    let writeDateTime = document.createElement("span");
    writeDateTime.textContent = convertDateTime(commentContent.createdAt);

    let updateDateTime = document.createElement("span");

    if (commentContent.updatedAt) {
        writeDateTime.textContent = convertDateTime(commentContent.updatedAt);
        updateDateTime.classList.add("ms-1");
        updateDateTime.textContent = "(수정 됨)";
    }

    info.appendChild(writeDateTime);
    info.appendChild(updateDateTime);

    row.appendChild(info);

    return row;
}

function convertDateTime(datetime) {
    let date = new Date(datetime);
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    month = month < 10 ? '0' + month : month;
    day = day < 10 ? '0' + day : day;
    hour = hour < 10 ? '0' + hour : hour;
    minute = minute < 10 ? '0' + minute : minute;

    return `${year}. ${month}. ${day}. ${hour}:${minute}`;
}

function getAnswerComment(parentNo, targetNo, childCnt, msg, page) {
    let answerComment = document.createElement("div");
    answerComment.className = "row justify-content-center";

    let childBtn = document.createElement("div");
    childBtn.className = "m-1";
    childBtn.style = "cursor: pointer;";

    let icon = document.createElement("img");
    icon.style = "height: 16px;";
    icon.src = "/static/icons/chevron-down.svg";
    icon.alt = "comment";

    let text = document.createElement("span");
    text.classList.add("ms-1")
    text.style = "font-size: 12px;";
    text.textContent = childCnt + msg;

    childBtn.appendChild(icon);
    childBtn.appendChild(text);

    childBtn.addEventListener("click", () => {
        loadChildComments(parentNo, targetNo, page, answerComment);
    })

    answerComment.appendChild(childBtn);

    return answerComment;
}

function loadChildComments(parentNo, targetNo, page, btn) {
    btn.classList.add("d-none");

    let target = document.getElementById(COMMENT_PREFIX + targetNo);

    let path = window.location.pathname;
    let url = "/api" + path + "/comments/" + parentNo + "/child";

    if (page) {
        url += "?page=" + page;
    }

    axios.get(url)
        .then(response => {
            let comments = response.data.body;

            for (let i = comments.length - 1; i >= 0; i--) {
                let comment = comments[i];

                let writer = {
                    username: comment.username,
                    name: comment.name,
                    pfp: comment.userPfp
                };

                let commentContent = {
                    commentNo: comment.commentNo,
                    content: comment.content,
                    createdAt: comment.createdAt,
                    updatedAt: comment.updatedAt,
                    isDeleted: comment.deleted,
                }

                let currentMember;

                if (loginMember != null) {
                    currentMember = {
                        username: loginMember.username,
                        name: loginMember.name,
                        thumb: loginMember.thumb,
                    }
                }

                let commentEl = getChildComment(comment.commentNo, writer, currentMember, commentContent);
                commentEl.id = COMMENT_PREFIX + comment.commentNo;

                if (i == comments.length - 1 && response.data.hasNext) {
                    let remain = response.data.total - (page + 1) * 10;
                    commentEl.appendChild(getAnswerComment(parentNo, comment.commentNo, remain, "개의 답글 더 보기", (Number(page)+1)));
                }
                target.after(commentEl);
            }
        })
        .catch(error => {
            openFailedAlerts("답글을 불러오는 데 실패하였습니다. 잠시 후 다시 시도해주세요.");
            btn.classList.remove("d-none");
        });
}

function drawChildCommentForm(commentNo, currentMember) {
    let el = document.createElement("div");
    let formId = CHILD_COMMENT_WRITE_FORM_PREFIX + commentNo;
    el.innerHTML =
        "<div id='" + formId + "' class=\"card-footer ms-4\">\n" +
        "  <div class=\"row\">\n" +
        "    <div class=\"row\">\n" +
        "      <a class=\"d-flex lh-1 text-body p-0\">\n" +
        "      <img class=\"avatar avatar-sm rounded-circle\"\n" +
        "        src=\"" + currentMember.thumb + "\"\n" +
        "        alt=\"pfp\">\n" +
        "        <div class=\"ps-2 overflow-hidden\">\n" +
        "          <div>" + currentMember.name + "</div>\n" +
        "          <div class=\"mt-1 small text-secondary\">@" + currentMember.username + "</div>\n" +
        "        </div>\n" +
        "      </a>\n" +
        "    </div>\n" +
        "    <div class=\"row mt-2\">\n" +
        "      <textarea id=\"" + CHILD_COMMENT_WRITE_INPUT_PREFIX + commentNo + "\" class=\"form-control\" rows=\"4\"></textarea>\n" +
        "    </div>\n" +
        "    <div class=\"row d-flex justify-content-end\">\n" +
        "      <button id='child-comment-write-btn-" + commentNo + "' class=\"btn mt-2\" style=\"font-size: 10px; height: 30px; width: 50px\">\n" +
        "        등록\n" +
        "      </button>\n" +
        "    </div>\n" +
        "  </div>\n" +
        "</div>"

    return el;
}

function drawComments(targetEl, comments, pages) {
    for (let i = comments.length - 1; i >= 0; i--) {
        let comment = comments[i];
        let writer = {
            username: comment.username,
            name: comment.name,
            pfp: comment.userPfp
        };

        let commentContent = {
            commentNo: comment.commentNo,
            content: comment.content,
            createdAt: comment.createdAt,
            updatedAt: comment.updatedAt,
            isDeleted: comment.deleted,
        }

        let currentMember;

        if (loginMember != null) {
            currentMember = {
                username: loginMember.username,
                name: loginMember.name,
                thumb: loginMember.thumb,
            }
        }

        let commentEl = getComment(comment.commentNo, writer, currentMember, commentContent, comment.childCnt);

        if (pages.hasNext && i == comments.length - 1) {
            let moreComment = document.createElement("div");
            moreComment.className = "hr-text my-5";
            moreComment.innerText = "댓글 더 보기";
            moreComment.style.cursor = "pointer";
            moreComment.addEventListener("click", function () {
                moreComment.classList.add("d-none");

                getCommentsByRest((Number(pages.page) + 1), commentEl);
            })

            targetEl.after(moreComment);
        }

        targetEl.after(commentEl);
    }
}

function getCommentsByRest(page, commentWriteForm) {
    let path = window.location.pathname;
    let url = "/api" + path + "/comments";

    if (page > 0) {
        url += "?page=" + page;
    }

    axios.get(url)
        .then(res => {
            let comments = res.data.body;

            let pages = {
                "page": res.data.currentPage,
                "total": res.data.total,
                "hasNext": res.data.hasNext,
            }

            drawComments(commentWriteForm, comments, pages);
        })
        .catch(() => {
            openFailedAlerts("댓글을 불러오는 데 실패하였습니다. 잠시 후 다시 시도해주세요.");
        })

}

function hrefModifyPage() {
    let path = window.location.pathname + "/mod";

    location.href = path;
}

document.addEventListener('DOMContentLoaded', function () {
    let path = window.location.pathname;
    let noHeart = document.getElementById("heart-icon-a");
    let fillHeart = document.getElementById("heart-icon-b");
    let heartCnt = document.getElementById("heart-cnt");
    let commentCnt = document.getElementById("comment-cnt");

    axios.get("/api" + path + "/heart")
        .then((res) => {
            if (res.data === true) {
                fillHeart.classList.remove("d-none");
            } else {
                noHeart.classList.remove("d-none");
            }
        })
        .catch((error) => {
        });

    axios.get("/api" + path + "/comments/count")
        .then((res) => {
            commentCnt.innerText = res.data;
        });

    axios.get("/api" + path + "/hearts/count")
        .then((res) => {
            heartCnt.innerText = res.data;
        })

    let commentWriteForm = document.getElementById(COMMENT_WRITE);
    getCommentsByRest(0, commentWriteForm);
});

function deletePosts() {
    let path = window.location.pathname;

    axios.delete("/api" + path)
        .then(() => {
            let blogPath = path.split("/");

            location.href = "/" + blogPath[1] + "/" + blogPath[2];
        })
}

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

function heart() {
    let path = window.location.pathname;
    let noHeart = document.getElementById("heart-icon-a");
    let fillHeart = document.getElementById("heart-icon-b");
    let heartCnt = document.getElementById("heart-cnt");

    axios.post("/api" + path + "/heart")
        .then(() => {
            noHeart.classList.add("d-none");
            fillHeart.classList.remove("d-none");

            let nowCnt = Number(heartCnt.innerText);
            heartCnt.innerText = nowCnt + 1;
        })
        .catch(() => {
            openFailedAlerts("잠시 후 다시 시도해주세요.");
            noHeart.classList.remove("d-none");
            fillHeart.classList.add("d-none");
        });
}

function cancelHeart() {
    let path = window.location.pathname;
    let noHeart = document.getElementById("heart-icon-a");
    let fillHeart = document.getElementById("heart-icon-b");
    let heartCnt = document.getElementById("heart-cnt");

    axios.delete("/api" + path + "/heart")
        .then(() => {
            noHeart.classList.remove("d-none");
            fillHeart.classList.add("d-none");

            let nowCnt = Number(heartCnt.innerText);
            heartCnt.innerText = nowCnt - 1;
        })
        .catch(() => {
            openFailedAlerts("잠시 후 다시 시도해주세요.");
            noHeart.classList.add("d-none");
            fillHeart.classList.remove("d-none");
        });
}

function closePosts() {
    let path = window.location.pathname;

    axios.delete("/api" + path + "/visible")
        .then(() => {
            let paths = path.split("/");
            location.href = "/" + paths[1] + "/" + paths[2];
        })
        .catch(() => {
            openFailedAlerts("잠시 후 다시 시도해주세요.");
        });
}