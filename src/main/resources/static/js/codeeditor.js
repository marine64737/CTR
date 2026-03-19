var editor = CodeMirror.fromTextArea(document.getElementById("code-editor"), {
    lineNumbers: true,      // 줄 번호 표시
    mode: "text/x-java",     // 언어 설정 (SQL, HTML, JS 등)
    theme: "dracula",       // 테마 설정
    indentUnit: 4,       // 들여쓰기 칸수
    viewportMargin: Infinity
    //height: auto !important,      /* 고정 높이 해제 */
    //min-height: 200px
    //line-height: 1.5,      /* 줄 간격을 명확히 지정 */
    //vertical-align: top   /* 텍스트가 위에서부터 시작하도록 고정 */
});

// 에디터 객체 이름이 editor라고 가정 (예: var editor = CodeMirror.fromTextArea(...))
setTimeout(function() {
    editor.refresh();
}, 1); // 0.1초 뒤에 강제로 다시 그려라!
