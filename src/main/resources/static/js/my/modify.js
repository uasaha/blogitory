document.addEventListener('DOMContentLoaded', function(){
    console.log(posts);
});

const {Editor} = toastui;
const {codeSyntaxHighlight} = Editor.plugin;
const {colorSyntax} = Editor.plugin;

const tagInput = document.querySelector("#tag-input");
const tagRegex = /^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/;
const titleRegex = /^[ㄱ-ㅎ가-힣a-zA-Z0-9!@#$%^&*()_+=~₩\s]+$/;
const urlRegex = /^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]+$/;

const tagify = new Tagify(tagInput, {
    pattern: tagRegex,
});

const editor = new Editor({
    el: document.querySelector('#editor-div'),
    height: '600px',
    previewStyle: 'tab',
    previewHighlight: false,
    usageStatistics: false,
    initialEditType: 'wysiwyg',
    plugins: [codeSyntaxHighlight, colorSyntax],
    language: "ko-KR",
});

editor.addHook("addImageBlobHook", function (blob, callback) {
    let formData = new FormData();
    formData.append('file', blob);

    axios.post("/api/images/posts", formData, {
        headers: {
            'content-type': 'multipart/form-data'
        }
    }).then(response => {
        callback(response.data.url, response.data.originName);
    }).catch(error => {
        callback('image_upload_fail', 'Image');
    })
});

document.addEventListener("DOMContentLoaded", () => {
    tagify.addTags(posts.tags);

    if (posts.detail) {
        editor.setMarkdown(posts.detail);
    }
});

function validateTitle(el) {
    if(titleRegex.test(el.value)) {
        return true;
    }
    el.classList.add("is-invalid");
    return false;
}

function uploadPostsThumb(el) {
    let postsThumbUrlInput = document.querySelector("#posts-thumb-url");
    let postsThumbImg = document.querySelector("#posts-thumb-img");

    let formData = new FormData();
    formData.append("file", el.files[0]);

    axios.post("/api/images/posts", formData, {
        headers: {
            'content-type': 'multipart/form-data'
        }
    }).then(response => {
        postsThumbUrlInput.value = response.data.url;
        postsThumbImg.src = response.data.url;
        postsThumbImg.style.display = "";
    });
}

function savePosts(el) {
    el.disabled = true;
    let postsThumbUrl = document.querySelector("#posts-thumb-url").value;
    let postsTitle = document.querySelector("#posts-title").value;
    let postsTags = tagify.value.map(tag => tag.value);
    let postsSummary = document.querySelector("#posts-summary").value;
    let postsContents = editor.getMarkdown();

    if (!validateTitle(postsTitle)) {
        openWarnAlerts("제목의 형식이 잘못되었습니다.");
        el.disabled = false;
        return;
    }

    if (!postsContents || postsContents === "") {
        openWarnAlerts("내용을 입력하세요.");
        el.disabled = false;
        return;
    }

    let url = window.location.pathname.replace("/mod", "");

    axios.post("/api" + url, {
        title: postsTitle,
        summary: postsSummary,
        content: postsContents,
        thumb: postsThumbUrl,
        tags: postsTags
    }).then(() => {
        location.href = url;
    }).catch(() => {
        openFailedAlerts("문제가 발생하였습니다.");
    })
}

function cancelModifyPosts() {
    history.back();
}