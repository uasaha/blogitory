const {Editor} = toastui;
const {codeSyntaxHighlight} = Editor.plugin;
const {colorSyntax} = Editor.plugin;

const tagInput = document.querySelector("#tag-input");
const tagRegex = /^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/;
const urlRegex = /^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]+$/;

// const youtubeEl = document.createElement('span');
// youtubeEl.style = 'cursor: pointer;'
//
// const icon = document.createElement('img');
// icon.setAttribute('src', '/static/icons/youtube_social_icon_red.png');
// icon.setAttribute('width', '32');
// youtubeEl.appendChild(icon);
//
// const container = document.createElement('div');
// const description = document.createElement('p');
// description.textContent = "Youtube 영상 주소를 입력하세요.";
//
// const urlInput = document.createElement('input');
// urlInput.style.width = '100%';
//
// const youtubeBtn = document.createElement('button');
// youtubeBtn.className = 'btn btn-outline-secondary float-end mt-2 mb-2';
// youtubeBtn.textContent = "입력";
//
// container.appendChild(description);
// container.appendChild(urlInput);
// container.appendChild(youtubeBtn);

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
    toolbarItems: [
        ['heading', 'bold', 'italic', 'strike'],
        ['hr', 'quote'],
        ['ul', 'ol', 'task', 'indent', 'outdent'],
        ['table', 'image', 'link',
            // {
            //     name: 'Youtube',
            //     tooltip: 'Youtube',
            //     el: youtubeEl,
            //     popup: {
            //         body: container,
            //         style: { width: 'auto' },
            //     }
            // }
        ],
        ['code', 'codeblock'],
        ['scrollSync'],
    ],
    // customHTMLSanitizer: () => {
    //     htmlBlock: {
    //         function iframe(node) {
    //             return [
    //                 {
    //                     type: 'openTag',
    //                     tagName: 'iframe',
    //                     outerNewLine: true,
    //                     attributes: node.attrs,
    //                 },
    //                 { type: 'html', content: node.childrenHTML || '' },
    //                 { type: 'closeTag', tagName: 'iframe', outerNewLine: true },
    //             ];
    //         }
    //         function div(node) {
    //             return [
    //                 { type: 'openTag', tagName: 'div', outerNewLine: true, attributes: node.attrs },
    //                 { type: 'html', content: node.childrenHTML || '' },
    //                 { type: 'closeTag', tagName: 'div', outerNewLine: true },
    //             ];
    //         }
    //     }
    //     funhtmlInline: {
    //         function big(node, { entering }) {
    //             return entering
    //                 ? { type: 'openTag', tagName: 'big', attributes: node.attrs }
    //                 : { type: 'closeTag', tagName: 'big' };
    //         }
    //     }
    // }
});

let themeStorageKey = "tablerTheme";
let defaultTheme = "light";
let selectedTheme = localStorage.getItem(themeStorageKey);
let editorEl = document.getElementsByClassName("toastui-editor-defaultUI")[0];
let viewerEls = document.querySelectorAll("#viewer");
let viewerEl = viewerEls[0];

if (selectedTheme === "light") {
    if (editorEl) {
        if (editorEl.classList.contains("toastui-editor-dark")) {
            editorEl.classList.remove("toastui-editor-dark");
        }
    }

    if (viewerEl) {
        viewerEl.className="";
    }

} else if (selectedTheme === "dark") {
    if (editorEl) {
        editorEl.classList.add("toastui-editor-dark");
    }

    if (viewerEl) {
        viewerEl.className="toastui-editor-dark";
    }
}

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
    setInterval(tempSave, 60000);
    tagify.addTags(posts.tags);

    if (posts.details !== null) {
        editor.setMarkdown(posts.details);
    }
});

// youtubeBtn.addEventListener('click', () => {
//     if((/https:\/\/youtu.be\/.{11,}/).test(urlInput.value)
//         || (/https:\/\/www.youtube.com\/watch\?v=.{11,}/).test(urlInput.value)) {
//
//         let str = '<div><iframe width="560" height="315" src="https://www.youtube.com/embed/' + urlInput.value.slice(-11) + '" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture;" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe></div>';
//
//         editor.insertText(str);
//     }
// });

function tempSave() {
    let tp = new URL(window.location.href).searchParams.get("tp");
    let selectedBlog = document.querySelector("#blog-select").value;
    let selectedCategory = document.querySelector("#category-select").value;
    let selectedTitle = document.querySelector("#posts-title").value;
    let selectedUrl = document.querySelector("#posts-url").value;
    let selectedThumbUrl = document.querySelector("#posts-thumb-url").value;
    let selectedTag = tagify.value.map(tag => tag.value);
    let selectedSummary = document.querySelector("#posts-summary").value;
    let selectedContents = editor.getMarkdown();

    axios.post("/api/posts/" + tp, {
        "blogNo": selectedBlog,
        "title": selectedTitle,
        "categoryNo": selectedCategory,
        "url": selectedUrl,
        "summary": selectedSummary,
        "thumbnailUrl": selectedThumbUrl,
        "details": selectedContents,
        "tags": selectedTag
    }).then(response => {
        openSuccessAlerts("작성중인 내용이 임시 저장되었습니다.");
    }).catch(error => {
        openFailedAlerts("임시 저장에 실패하였습니다.");
    });
}

function setCategory(value) {
    let categories;

    for (let blog of blogs) {
        if (Number(value) === blog.blogNo) {
            categories = blog.categories;
        }
    }

    let categorySelect = document.querySelector("#category-select");

    categorySelect.innerHTML = "<option value=\"null\">---카테고리---</option>";

    for (let category of categories) {
        let option = document.createElement("option");
        option.value = category.categoryNo;
        option.text = category.name;

        categorySelect.appendChild(option);
    }
}

function savePosts(el) {
    el.disabled = true;

    let tp = new URL(window.location.href).searchParams.get("tp");
    let selectedBlog = document.querySelector("#blog-select").value;
    let selectedCategory = document.querySelector("#category-select").value;
    let selectedTitle = document.querySelector("#posts-title");
    let selectedUrl = document.querySelector("#posts-url");
    let selectedThumbUrl = document.querySelector("#posts-thumb-url").value;
    let selectedTag = tagify.value.map(tag => tag.value);
    let selectedSummary = document.querySelector("#posts-summary").value;
    let selectedContents = editor.getMarkdown();

    if (!selectedBlog || selectedBlog === "") {
        openWarnAlerts("블로그를 선택해주세요.");
        el.disabled = false;
        return;
    }

    if (!selectedCategory || selectedCategory === "") {
        openWarnAlerts("카테고리를 선택해주세요.");
        el.disabled = false;
        return;
    }

    if (!validateTitle(selectedTitle)) {
        openWarnAlerts("제목의 형식이 잘못되었습니다.");
        el.disabled = false;
        return;
    }

    if (!validateUrl(selectedUrl)) {
        openWarnAlerts("올바른 URL 형식이 아닙니다.");
        el.disabled = false;
        return;
    }

    if (!selectedContents || selectedContents === "") {
        openWarnAlerts("내용을 입력하세요.");
        el.disabled = false;
        return;
    }

    axios.post("/api/posts?tp=" + tp, {
        "blogNo": selectedBlog,
        "title": selectedTitle.value,
        "categoryNo": selectedCategory,
        "url": selectedUrl.value,
        "summary": selectedSummary,
        "thumbnailUrl": selectedThumbUrl,
        "details": selectedContents,
        "tags": selectedTag
    }).then(response => {
        console.log(response);
        window.location.href = "/" + response.data.postsUrl;
    }).catch(error => {
        el.disabled = false;
        openFailedAlerts("글 등록에 실패하였습니다.");
    });
}

function validateTitle(el) {
    if(el.value.length > 0) {
        return true;
    }
    el.classList.add("is-invalid");
    return false;
}

function validateUrl(el) {
    if (el.value == null || el.value === "") {
        return true;
    }

    if(urlRegex.test(el.value)) {
        return true;
    }
    el.classList.add("is-invalid");
    return false;
}

function saveTempPosts(el) {
    el.disabled = true;

    this.tempSave();

    window.history.back();
}

function cancelPosts(el) {
    el.disabled = true;
    let tp = new URL(window.location.href).searchParams.get("tp");

    axios.delete("/api/posts?tp=" + tp)
        .then(response => {
            window.history.back();
        }).catch(error => {

    })
}

function deleteTempPosts(tp) {
    axios.delete("/api/posts?tp=" + tp)
        .then(response => {
            window.history.back();
        }).catch(error => {

    })
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