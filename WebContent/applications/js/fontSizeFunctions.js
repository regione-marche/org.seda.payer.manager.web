var GL_Style = "normalStyle";

$(window).on("load", function() {
    //rendo visibile i pulsanti A- e A+
    var divJS = document.getElementById('divFontSize');
    if (divJS != null)
        divJS.className = "seda-ui-div divFontSize";
    
    //inizializzo lo stile
    var styleCookie = getCookie("fontSizePayerManager");
    setStyle(styleCookie);
    setCookie("fontSizePayerManager", styleCookie, getExpDate(365, 0, 0));
});

// --------------------------------------------------------------------------------
function decreaseFontSize(cssFontSizeObj) {
    var styleCookie = getCookie("fontSizePayerManager");
    var styleID = "";
    switch (styleCookie) {
        case "x-largeStyle":
            styleID = "largeStyle";
            break;
        case "largeStyle":
            styleID = "normalStyle";
            break;
        case "normalStyle":
            styleID = "smallStyle";
            break;
        case "smallStyle":
            styleID = "x-smallStyle";
            break;
        case "x-smallStyle":
            styleID = "x-smallStyle";
            break;
        default:
            styleID = "normalStyle";
            break;
    }
    setStyle(styleID);
    setCookie("fontSizePayerManager", styleID, getExpDate(365, 0, 0));
    
}


function increaseFontSize() {
    var styleCookie = getCookie("fontSizePayerManager");
    var styleID = "";
    switch (styleCookie) {
        case "x-smallStyle":
            styleID = "smallStyle";
            break;
        case "smallStyle":
            styleID = "normalStyle";
            break;
        case "normalStyle":
            styleID = "largeStyle";
            break;
        case "largeStyle":
            styleID = "x-largeStyle";
            break;
        case "x-largeStyle":
            styleID = "x-largeStyle";
            break;
        default:
            styleID = "normalStyle";
            break;
    }
    setStyle(styleID);
    setCookie("fontSizePayerManager", styleID, getExpDate(365, 0, 0));
}

function setStyle(styleName) {
    var cssFontSizeObj = document.getElementById('fontsize_id');
    
    if (cssFontSizeObj != null) {
        switch (styleName) {
            case "x-largeStyle":
                cssFontSizeObj.href = "../applications/templates/shared/css/fontXLargeSize.css";
                break;
            case "largeStyle":
                cssFontSizeObj.href = "../applications/templates/shared/css/fontLargeSize.css";
                break;
            case "normalStyle":
                cssFontSizeObj.href = "../applications/templates/shared/css/fontNormalSize.css";
                break;
            case "smallStyle":
                cssFontSizeObj.href = "../applications/templates/shared/css/fontSmallSize.css";
                break;
            case "x-smallStyle":
                cssFontSizeObj.href = "../applications/templates/shared/css/fontXSmallSize.css";
                break;
            default:
                cssFontSizeObj.href = "../applications/templates/shared/css/fontNormalSize.css";
                break;
        }
    }
}


// Input values:
// name - name of the cookie
// [path] - path of the cookie (must be same as path used to create cookie)
// [domain] - domain of the cookie (must be same as domain used to create cookie)
// * path and domain default if assigned null or omitted if no explicit argument proceeds
function deleteCookie(name, path, domain) {
    if (getCookie(name)) {
        document.cookie = name + "=" +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") +
    "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}

// Input value:
// name - name of the desired cookie
// * return string containing value of specified cookie or null if cookie does not exist
function getCookie(name) {
    var dc = document.cookie;
    if (dc == "" || dc == null)
        return (GL_Style);
    else {
        var prefix = name + "=";
        var begin = dc.indexOf("; " + prefix);
        if (begin == -1) {
            begin = dc.indexOf(prefix);
            if (begin != 0) 
            	return GL_Style;
        } else
            begin += 2;
        var end = document.cookie.indexOf(";", begin);
        if (end == -1)
            end = dc.length;
        var ret = unescape(dc.substring(begin + prefix.length, end));
        return ret;
    }
}

function getExpDate(days, hours, minutes) {
    var expDate = new Date();
    if (typeof days == "number" && typeof hours == "number" && typeof minutes == "number") {
        expDate.setDate(expDate.getDate() + parseInt(days));
        expDate.setHours(expDate.getHours() + parseInt(hours));
        expDate.setMinutes(expDate.getMinutes() + parseInt(minutes));
        return expDate.toGMTString();
    }
}

// Input values:
// name - name of the cookie
// value - value of the cookie
// [expires] - expiration date of the cookie (defaults to end of current session)
// [path] - path for which the cookie is valid (defaults to path of calling document)
// [domain] - domain for which the cookie is valid (defaults to domain of calling document)
// [secure] - Boolean value indicating if the cookie transmission requires a secure transmission
// * an argument defaults when it is assigned null as a placeholder
// * a null placeholder is not required for trailing omitted arguments
function setCookie(name, value, expires, path, domain, secure) {
    var curCookie = name + "=" + escape(value) +
        ((expires) ? "; expires=" + expires : "") +
        "; path=/" +
        ((domain) ? "; domain=" + domain : "") +
        ((secure) ? "; secure" : "");
    document.cookie = curCookie;
    GL_Style = value;
    return (true);
}
