define( ["jquery"], function ($) {
    var countdown = 0;

    $(document).ready(function () {
        mobileOpenNav();
        openSubMenu();
        closeMenus();
        hideError();
        toggleTable();
    });

    function tryError() {
        $.ajax({
            type: "get",
            url: "test/error",
            data: {},
            success: function () {

            },
            error: function (jqXHR, textStatus, errorThrown) {
                environmentError(jqXHR.status, jqXHR.responseText);
            }
        })
    }

    function environmentError(code, text) {
        if (countdown == 0) {
            $("#err_text").text(code + " " + parseError(text, "error") + ': "' + parseError(text, "message") + '".');
            $("#err_field").removeClass("hidden");
            parseError(text, "error");

            countdown = 30;
            var interval = setInterval(function () {
                $("#err_timer").text("( " + countdown + " )");
                countdown--;

                if (countdown == 0) {
                    clearInterval(interval);
                    $("#err_field").addClass("hidden");
                }
            }, 1000);
        } else {
            var inter = setInterval(function () {
                environmentError(code, text);
            }, countdown * 1000);
        }
    }

    function parseError(errorText, part) {
        var substr = part + "\"\:\"";
        var output = "";
        if (typeof errorText !== 'undefined' && errorText.includes(substr)) {
            var i = errorText.indexOf(substr) + substr.length;
            var currentChar = "";

            while (!currentChar.match("\"")) {
                currentChar = errorText.charAt(i);
                if (!currentChar.match("\"")) {
                    output += currentChar;
                }
                i++;
            }
        } else {
            output = "not specified";
        }

        return output;
    }

    function hideError() {
        $("#errClose").on("click", function () {
            countdown = 0;
            $("#err_field").addClass("hidden");
        });
    }

    function switchLocale() {

        $.ajax({
            type: "GET",
            url: "checklocale",
            data: {},
            success: function (response) {
                var pathname = window.location.pathname;
                pathname += response == "ru" ? "?lang=en" : "?lang=ru";
                window.location.replace(pathname);
            },
            error: function () {
                alert("error");
            }
        });
    }

    function mobileOpenNav() {
        $("#nav_toggle").on("click", function () {
            toggleNav();
        });
    }

    function toggleNav() {
        var hidden = $("#mobile_nav").hasClass("hidden");

        if (hidden) {
            $("#mobile_nav").removeClass("hidden");
            $("#disabler").removeClass("hidden");
        } else {
            $("#mobile_nav").addClass("hidden");
            $(".submenu_element").addClass("hidden");
            $("#disabler").addClass("hidden");
            $(".submenu_toggle").removeClass("activated");
        }
    }

    function openSubMenu() {
        $(".submenu_toggle").on("click", function () {
            var id_val = $(this).attr("val");
            var id = "#" + id_val + "_btn";
            var selector = "#" + id_val;
            var hidden = $(selector).hasClass("hidden");

            if (hidden) {
                $(selector).removeClass("hidden");
                $(id).addClass("activated");
            } else {
                $(selector).addClass("hidden");
                $(id).removeClass("activated");
            }
        });
    }

    function closeMenus() {
        $("#disabler").on('click', function () {
            var hidden = $("#mobile_nav").hasClass("hidden");
            if (!hidden) {
                toggleNav();
            }
        })
    }

    function toggleTable() {
        $(".table-header").on("click", function () {
            var target_selector = "#" + $(this).attr("val") + "_tbody";
            var hidden = $(target_selector).hasClass("hidden");
            var caret_down_selector = "#" + $(this).attr("val") + "_caret_down";
            var caret_up_selector = "#" + $(this).attr("val") + "_caret_up";
            if (hidden) {
                $(target_selector).removeClass("hidden");
                $(caret_down_selector).addClass("hidden");
                $(caret_up_selector).removeClass("hidden");
            } else {
                $(target_selector).addClass("hidden");
                $(caret_up_selector).addClass("hidden");
                $(caret_down_selector).removeClass("hidden");
            }
        })
    }
});