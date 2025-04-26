$(document).ready(function(){
    adminBoxChat.onInit();
})

const adminBoxChat = {
    db: null,
    messagesRootRef: null,
    currentSession: null,

    onInit: function(){
        this.db = this.onConfigFireBase();
        this.messagesRootRef = this.db.ref("messages");
        this.loadUserList();
    },

    onConfigFireBase: function(){
        const firebaseConfig = {
            apiKey : "AIzaSyDLuKjsVO9Pg1pdcTkWjJSnxeFEr_bgJ_Q" ,
            authDomain : "chatbox-demo-52e7c.firebaseapp.com" ,
            databaseURL : "https://chatbox-demo-52e7c-default-rtdb.asia-southeast1.firebasedatabase.app" ,
            projectId : "chatbox-demo-52e7c" ,
            storageBucket : "chatbox-demo-52e7c.firebasestorage.app" ,
            messagingSenderId : "1031693527615" ,
            appId : "1:1031693527615:web:0fd757986506b17532268a" ,
            measurementId : "G-JE8ZTVW3XQ"
        };

        const app = firebase.initializeApp(firebaseConfig);
        return firebase.database(app);
    },

    loadUserList: function() {
        this.messagesRootRef.on("value", (snapshot) => {
            const allMessages = snapshot.val();
            const userListEl = document.getElementById("user-list");
            userListEl.innerHTML = ""; // Clear cũ

            const sessions = [];

            for (let sessionId in allMessages) {
                const messages = allMessages[sessionId];
                const messageList = Object.values(messages);

                const lastMessage = messageList[messageList.length - 1];

                sessions.push({
                    sessionId,
                    lastMessage,
                    timestamp: lastMessage?.timestamp || 0,
                    isUnanswered: lastMessage?.sender === "user"
                });
            }

            sessions.sort((a, b) => b.timestamp - a.timestamp);

            sessions.forEach(({ sessionId, lastMessage, isUnanswered }) => {
                adminBoxChat.onLoadUserInfo(sessionId)
                    .then((response) => {
                        const userInfo = response.data;

                        const userEl = document.createElement("div");
                        userEl.style.display = "flex";
                        userEl.style.alignItems = "center";
                        userEl.style.cursor = "pointer";
                        userEl.style.padding = "8px";
                        userEl.style.borderBottom = "1px solid #eee";

                        userEl.style.fontWeight = isUnanswered ? "bold" : "normal";

                        const avatarEl = document.createElement("img");
                        avatarEl.src = "/" + (userInfo.avt || "default-avatar.png");
                        avatarEl.style.width = "40px";
                        avatarEl.style.height = "40px";
                        avatarEl.style.borderRadius = "50%";
                        avatarEl.style.marginRight = "8px";

                        const textEl = document.createElement("div");

                        const nameEl = document.createElement("div");
                        nameEl.innerText = (userInfo.firstName + " " + userInfo.lastName) || sessionId;

                        const messagePreviewEl = document.createElement("div");
                        messagePreviewEl.innerText = lastMessage?.content || "";
                        messagePreviewEl.style.fontSize = "12px";
                        messagePreviewEl.style.color = "#666";
                        messagePreviewEl.classList.add("message-preview");

                        textEl.appendChild(nameEl);
                        textEl.appendChild(messagePreviewEl);

                        userEl.appendChild(avatarEl);
                        userEl.appendChild(textEl);

                        userEl.addEventListener("click", () => {
                            this.loadMessagesBySession(sessionId);
                        });

                        userListEl.appendChild(userEl);
                    })
                    .catch((error) => {
                        console.error("Failed to load user info", error);
                    });
            });
        });
    },




    loadMessagesBySession: function(sessionId){
        this.currentSession = sessionId;
        adminBoxChat.onLoadUserInfo(sessionId)
            .then((response) => {
                document.getElementById("chat-header").innerText = `Đoạn chat với: ${response.data.firstName + " "+ response.data.lastName}`;
                const messagesList = document.getElementById("messages-list");
                messagesList.innerHTML = "";

                const sessionRef = this.messagesRootRef.child(sessionId);
                sessionRef.once("value", (snapshot) => {
                    const messages = snapshot.val();

                    for (let msgId in messages) {
                        const msg = messages[msgId];
                        const msgEl = document.createElement("div");
                        msgEl.className = `message-item ${msg.sender}`;

                        const senderName = msg.sender === "admin" ? "👩‍💼 Nhân viên" : `${response.data.firstName + " "+ response.data.lastName}`;
                        const timeString = new Date(msg.timestamp).toLocaleTimeString([], {
                            hour: '2-digit',
                            minute: '2-digit'
                        });

                        msgEl.innerHTML = `
                            <div class="message-sender">${senderName}</div>
                            <div class="message-content">${msg.content}</div>
                            <div class="message-time">${timeString}</div>
                        `;

                        messagesList.appendChild(msgEl);
                    }

                    messagesList.scrollTop = messagesList.scrollHeight;
                });
            });
    },
    sendMessage: function(){
        const input = document.getElementById("adminMessageInput");
        const content = input.value.trim();
        if (!content || !this.currentSession) return;

        const messageData = {
            sender: "admin",
            content: content,
            timestamp: Date.now()
        };
        this.messagesRootRef.child(this.currentSession).push(messageData);
        input.value = "";

        this.loadMessagesBySession(this.currentSession);
    },

    onLoadUserInfo: function(userID){
        return $.ajax({
            url: "/admin/users/getUserInfo",
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                userID: userID
            })
        })
    }
};

