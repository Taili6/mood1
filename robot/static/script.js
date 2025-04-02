document.addEventListener("DOMContentLoaded", function () {
    const chatInput = document.getElementById("chatInput");
    const sendButton = document.getElementById("sendButton");
    const chatBox = document.getElementById("chatBox");

    sendButton.addEventListener("click", function () {
        const userMessage = chatInput.value;
        if (!userMessage) return;
    
        // 顯示使用者訊息（保留舊的訊息）
        chatBox.innerHTML += `<p><strong>你:</strong> ${userMessage}</p>`;
        chatInput.value = "";
    
        // 使用GET方法發送到 Flask API
        fetch(`http://127.0.0.1:5000/chat?message=${encodeURIComponent(userMessage)}`, {
            method: "GET",  // 只使用 GET 請求
            headers: { "Content-Type": "application/json" }
            // 注意：GET 請求不應該有 body
        })
        .then(response => response.json())
        .then(data => {
            chatBox.innerHTML += `<p><strong>機器人:</strong> ${data.response}</p>`;
            speakResponse(data.response); // 語音回應
            handleRobotAnimation("Talking"); // 開始講話，觸發講話動畫
        })
        .catch(error => console.error("錯誤:", error));
    });
    setTimeout(() => {
        console.log(window.speechSynthesis.getVoices());
    }, 1000);
    

});
