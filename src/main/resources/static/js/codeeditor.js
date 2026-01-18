var editor = CodeMirror.fromTextArea(document.getElementById("code-editor"), {
    lineNumbers: true,      // 줄 번호 표시
    mode: "text/x-java",     // 언어 설정 (SQL, HTML, JS 등)
    theme: "dracula",       // 테마 설정
    indentUnit: 4,       // 들여쓰기 칸수
    viewportMargin: Infinity
});