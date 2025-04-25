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

    loadUserList: function(){
        this.messagesRootRef.once("value", (snapshot) => {
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

            sessions.forEach(({ sessionId, isUnanswered }) => {
                const userEl = document.createElement("div");
                userEl.innerText = sessionId;
                userEl.style.cursor = "pointer";
                userEl.style.padding = "8px";
                userEl.style.borderBottom = "1px solid #eee";

                if (isUnanswered) {
                    userEl.style.fontWeight = "bold";
                }

                userEl.addEventListener("click", () => {
                    this.loadMessagesBySession(sessionId);
                });

                userListEl.appendChild(userEl);
            });
        });
    },

    loadMessagesBySession: function(sessionId){
        this.currentSession = sessionId;
        document.getElementById("chat-header").innerText = `Đoạn chat với user: ${sessionId}`;
        const messagesList = document.getElementById("messages-list");
        messagesList.innerHTML = "";

        const sessionRef = this.messagesRootRef.child(sessionId);
        sessionRef.once("value", (snapshot) => {
            const messages = snapshot.val();

            for (let msgId in messages) {
                const msg = messages[msgId];
                const msgEl = document.createElement("div");
                msgEl.innerHTML = `<b>${msg.sender === "admin" ? "👩‍💼 Nhân viên" : "🧑 User"}:</b> ${msg.content}`;
                msgEl.style.margin = "4px 0";
                messagesList.appendChild(msgEl);
            }

            messagesList.scrollTop = messagesList.scrollHeight;
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
    }

};