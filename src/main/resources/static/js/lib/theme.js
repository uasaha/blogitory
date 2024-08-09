(function (factory) {
	typeof define === 'function' && define.amd ? define(factory) :
	factory();
})((function () { 'use strict';

	let themeStorageKey = "tablerTheme";
	let defaultTheme = "light";
	let selectedTheme;
	let params = new Proxy(new URLSearchParams(window.location.search), {
	  get: function get(searchParams, prop) {
	    return searchParams.get(prop);
	  }
	});
	if (!!params.theme) {
	  localStorage.setItem(themeStorageKey, params.theme);
	  selectedTheme = params.theme;
	} else {
	  var storedTheme = localStorage.getItem(themeStorageKey);
	  selectedTheme = storedTheme ? storedTheme : defaultTheme;
	}

	setTheme(selectedTheme);
}));


function setTheme(theme) {
	let themeStorageKey = "tablerTheme";
	let defaultTheme = "light";
	let selectedTheme = theme;
	const editorEl = document.getElementsByClassName("toastui-editor-defaultUI")[0];
	const viewerEls = document.querySelectorAll("#viewer");
	const viewerEl = viewerEls[0];
	const viewerMobileEl = viewerEls[1];

	if (localStorage.getItem(themeStorageKey) == null) {
		localStorage.setItem(themeStorageKey, defaultTheme);
	}


	if (selectedTheme === "light") {
		localStorage.setItem(themeStorageKey, "light");

		if (editorEl) {
			if (editorEl.classList.contains("toastui-editor-dark")) {
				editorEl.classList.remove("toastui-editor-dark");
			}
		}

		if (viewerEl && viewerMobileEl) {
			viewerEl.className="";
			viewerMobileEl.className="";
		}

	} else if (selectedTheme === "dark") {
		localStorage.setItem(themeStorageKey, "dark");

		if (editorEl) {
			editorEl.classList.add("toastui-editor-dark");
		}

		if (viewerEl && viewerMobileEl) {
			viewerEl.className="toastui-editor-dark";
			viewerMobileEl.className="toastui-editor-dark";
		}
	}

	if (selectedTheme === 'dark') {
		document.body.setAttribute("data-bs-theme", selectedTheme);
	} else {
		document.body.removeAttribute("data-bs-theme");
	}


}