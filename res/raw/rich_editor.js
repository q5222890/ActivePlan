

var RE = {};

RE.currentSelection;

RE.editor = document.getElementById('editor');

// Initializations
RE.callback = function() {
    window.location.href = "re-callback://" + encodeURI(RE.getHtml());
}
RE.editor.addEventListener("input", RE.callback);

RE.setHtml = function(contents) {
    RE.editor.innerHTML = contents;
}

RE.getHtml = function() {
    return RE.editor.innerHTML;
}

RE.getText = function() {
    return RE.editor.innerText;
}

RE.setFontSize = function(size) {
    RE.editor.style.fontSize = size;
}

RE.setBackgroundColor = function(color) {
    RE.editor.style.backgroundColor = color;
}

RE.setWidth = function(size) {
    RE.editor.style.minWidth = size;
}

RE.setHeight = function(size) {
    RE.editor.style.minHeight = size;
}

RE.undo = function() {
    document.execCommand('undo', false, null);
}

RE.redo = function() {
    document.execCommand('redo', false, null);
}

RE.setBold = function() {
    document.execCommand('bold', false, null);
}

RE.setItalic = function() {
    document.execCommand('italic', false, null);
}

RE.setSubscript = function() {
    document.execCommand('subscript', false, null);
}

RE.setSuperscript = function() {
    document.execCommand('superscript', false, null);
}

RE.setStrikeThrough = function() {
    document.execCommand('strikeThrough', false, null);
}

RE.setUnderline = function() {
    document.execCommand('underline', false, null);
}

RE.setTextColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('foreColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}
RE.setTextSize =function(size) {
    document.execCommand('fontSize', false, size)
}

RE.setTextBackgroundColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('hiliteColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setHeading = function(heading) {
    document.execCommand('formatBlock', false, '<h'+heading+'>');
}

RE.setIndent = function() {
    document.execCommand('indent', false, null);
}

RE.setOutdent = function() {
    document.execCommand('outdent', false, null);
}

RE.setJustifyLeft = function() {
    document.execCommand('justifyLeft', false, null);
}

RE.setJustifyCenter = function() {
    document.execCommand('justifyCenter', false, null);
}

RE.setJustifyRight = function() {
    document.execCommand('justifyRight', false, null);
}

RE.insertImage = function(url, alt) {
    var html = '<img src="' + url + '" alt="' + alt + '" class="h5img"  width=100%/><br/><br/><br/>'
    RE.insertHTML(html);
}

//RE.insertVideo = function(html) {
////    RE.insertHTML('<div class=h5video style=width:100%>' + href + '</div>');
////    RE.insertHTML('<div class="h5video" width=100% >'+href+'</div>');
//    RE.insertHTML(html);
////      RE.insertHTML('<div style="width:80%; height:auto;"> ' +href+ '</div>');
//}

RE.insertVideo = function(href) {
    RE.insertHTML('<iframe class=h5video style=width:100%;height:100% href=' + href + '</iframe><br/><br/><br/>');
}
//function escapeHTML(a) {
//    return a.replace(/</g, "<").replace(/>/g, ">").replace(/&/g, "&").replace(/ /g, " ").replace(/"/g, """).replace(/'/g, "'")
//}

RE.escapeHTML =function (before , after) {
//    RE.escapeHTML(a).replace(b);
    before  = after;
    RE.insertHTML(after);
}
//RE.insertVideo = function(iframe) {
//     RE.insertHTML('<div class=insertiframe style=width:100% > '+ iframe +' </div>');
//}


//RE.insertVideo = function(href) {
//    RE.insertHTML('<video class=h5video style=width:80%;height:auto src=' + href + '></video>');
//}
//
//RE.insertVideo = function(href) {
//    RE.insertHTML('<video class=h5video style=width:80% href=' + href + '></video>');
//}

//RE.insertVideo = function(href) {
//    RE.insertHTML('<video width=80% controls=controls><source src='+href+' type=video/mp4></source><source src='+href+' type=video/ogg></source><source src='+href+' type=video/swf></source><source src='+href+' type=video/flv></source> your browser does not support the video tag </video>');
//}

RE.setBlockquote = function() {
    document.execCommand('formatBlock', false, '<blockquote>');
}

RE.insertHTML = function(html) {
    RE.restorerange();
    document.execCommand('insertHTML', false, html);
}

RE.insertLink = function(url, title) {
    RE.restorerange();
    var sel = document.getSelection();
    if (sel.toString().length != 0) {
        if (sel.rangeCount) {

            var el = document.createElement("a");
            el.setAttribute("href", url);
            el.setAttribute("title", title);

            var range = sel.getRangeAt(0).cloneRange();
            range.surroundContents(el);
            sel.removeAllRanges();
            sel.addRange(range);
        }
    }
    RE.callback();
}

RE.prepareInsert = function() {
    RE.backuprange();
}


RE.backuprange = function(){
    var selection = window.getSelection();
    var range = selection.getRangeAt(0);
    RE.currentSelection = {
        "startContainer": range.startContainer,
        "startOffset": range.startOffset,
        "endContainer": range.endContainer,
        "endOffset": range.endOffset};
}

RE.restorerange = function(){
    var selection = window.getSelection();
    selection.removeAllRanges();
    var range = document.createRange();
    range.setStart(RE.currentSelection.startContainer, RE.currentSelection.startOffset);
    range.setEnd(RE.currentSelection.endContainer, RE.currentSelection.endOffset);
    selection.addRange(range);
}

RE.focus = function() {
    var range = document.createRange();
    range.selectNodeContents(RE.editor);
    range.collapse(false);
    var selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
    RE.editor.focus();
}

RE.blurFocus = function() {
    RE.editor.blur();
}