function toPostsIssuePage() {
    axios.get("/api/posts/key")
        .then(result => {
            let tp = result.data;

            if (tp !== null) {
                location.href = "/posts?tp=" + tp;
            }
        })
        .catch(error => {
            openFailedAlerts("글 작성하기에 실패하였습니다.");
        })
}

