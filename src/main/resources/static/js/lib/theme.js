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
	let charts = document.querySelectorAll('.apexcharts-heatmap-rect');

	if (localStorage.getItem(themeStorageKey) == null) {
		localStorage.setItem(themeStorageKey, defaultTheme);
	}


	if (selectedTheme === "light") {
		localStorage.setItem(themeStorageKey, "light");
	} else if (selectedTheme === "dark") {
		localStorage.setItem(themeStorageKey, "dark");
	}

	if (selectedTheme === 'dark') {
		document.body.setAttribute("data-bs-theme", selectedTheme);
		if (charts && charts.length > 0) {
			charts.forEach(function (chart) {
				chart.setAttribute('stroke', '#181717');
			});
		}
	} else {
		document.body.removeAttribute("data-bs-theme");
		if (charts && charts.length > 0) {
			charts.forEach(function (chart) {
				chart.setAttribute('stroke', '#FFFFFF');
			});
		}
	}
}