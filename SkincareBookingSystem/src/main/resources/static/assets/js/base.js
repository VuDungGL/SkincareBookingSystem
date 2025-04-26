$(document).ready(function () {
    AuthCore.onInit();
    AuthCore.checkTokenExpiry();
    customBoxChat.onInit();
});
const coreConst ={
    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString("en-GB", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        });
    },
    formatDate2: function(isoString) {
        if (!isoString) return '';
        const date = new Date(isoString);

        // Chuyển sang UTC+7
        const offsetInMs = 7 * 60 * 60 * 1000;
        const localDate = new Date(date.getTime() + offsetInMs);

        return localDate.toISOString().split('T')[0];
    },

    showLoading:function() {
        $('.loading-overlay').addClass("loading-active").fadeIn();
        $('body').append('<div class="loading-overlay"><div class="loader"></div></div>');
    },
    hideLoading:function() {
        $('.loading-overlay').removeClass("loading-active").fadeOut();
    },
    onFormatOffSetDateTime: function () {
        const inputDate = document.getElementById("birthDate").value; // e.g., "2004-12-28"

        if (inputDate) {
            const date = new Date(inputDate);

            const timezoneOffset = -date.getTimezoneOffset(); // phút
            const offsetHours = Math.floor(timezoneOffset / 60);
            const offsetMinutes = timezoneOffset % 60;

            const offsetStr = `${offsetHours >= 0 ? '+' : '-'}${String(Math.abs(offsetHours)).padStart(2, '0')}:${String(Math.abs(offsetMinutes)).padStart(2, '0')}`;

            const isoStringWithOffset = `${inputDate}T00:00:00${offsetStr}`;

            return isoStringWithOffset;
        }

        return null;
    },
    formatGenderText: function(gender){
        if (gender === 1) {
            return "Nam";
        } else if (gender === 2) {
            return "Nữ";
        }else{
            return "Khác";
        }
    },
    formatStatusUserText: function(status){
        if (status === 0) {
            return "Khách hàng mới";
        } else if (status === 1) {
            return "Khách hàng quen thuộc";
        }else if(status === 2){
            return "Khách hàng VIP";
        }else if(status === 3){
            return "Khách hàng cũ";
        }
    },
    formatInputNumber: function (element) {
        element.on('input', function () {
            let value = $(this).val().replace(/\D/g, '');
            value = new Intl.NumberFormat('vi-VN').format(value);
            $(this).val(value);
        });
    },
    updatePagination: function(totalPages, currentPage, functionActive) {
        let paginationContainer = $("#pagination-page");
        paginationContainer.empty();
        let paginationHtml = '';

        let displayPage = currentPage + 1;
        let lastPage = totalPages;

        paginationHtml += currentPage === 0
            ? `<i class="fa-solid fa-angle-left opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-left" style="cursor: pointer" onclick="(${functionActive})(6, ${currentPage - 1})"></i>`;

        if (totalPages <= 5) {
            for (let i = 0; i < totalPages; i++) {
                paginationHtml += `<span onclick="(${functionActive})(6, ${i})" 
                                class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
            }
        } else {
            if (currentPage <= 1) {
                for (let i = 0; i <= 2; i++) {
                    paginationHtml += `<span onclick="(${functionActive})(6, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="(${functionActive})(6, ${totalPages - 1})">${totalPages}</span>`;
            } else if (currentPage >= 2 && currentPage <= totalPages - 4) {
                paginationHtml += `<span onclick="(${functionActive})(6, ${currentPage - 1})">${displayPage - 1}</span>`;
                paginationHtml += `<span onclick="(${functionActive})(6, ${currentPage})" class="page-active">${displayPage}</span>`;
                paginationHtml += `<span onclick="(${functionActive})(6, ${currentPage + 1})">${displayPage + 1}</span>`;
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="(${functionActive})(6, ${totalPages - 1})">${totalPages}</span>`;
            } else {
                paginationHtml += `<span>...</span>`;
                for (let i = totalPages - 4; i < totalPages; i++) {
                    paginationHtml += `<span onclick="(${functionActive})(6, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
            }
        }

        paginationHtml += currentPage === totalPages - 1
            ? `<i class="fa-solid fa-angle-right opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-right" style="cursor: pointer" onclick="(${functionActive})(6, ${currentPage + 1})"></i>`;

        paginationContainer.html(paginationHtml);
    },
    onFormatStartRating: function(rating){
        switch (rating){
            case 1:{
                return "★☆☆☆☆";
            }
            case 2:{
                return "★★☆☆☆";
            }
            case 3:{
                return "★★★☆☆";
            }
            case 4:{
                return "★★★★☆";
            }
            case 5:{
                return "★★★★★";
            }
            case 0:{
                return "☆☆☆☆☆";
            }
        }
    }
}

const AuthCore = {
    onInit: function () {
        this.onLogin();
        this.onRegister();
    },
    decodeToken: function (token) {
        if (!token) {
            throw new Error("Token is undefined or null");
        }
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    },
    redirectByRole: function (role) {
        switch (role) {
            case 2: // USER
                window.location.href = "/";
                break;
            case 1: // ADMIN
            case 0: // MASTER_ADMIN
                window.location.href = "/admin/dashboard";
                break;
            default:
                console.warn("Unknown role: " + role);
                window.location.href = "/";
        }
    },
    onLogin: function () {
        $('#loginForm').submit(function (e) {
            e.preventDefault();

            const userName = $('#username').val();
            const password = $('#password').val();

            $.ajax({
                url: '/login/onLogin',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ userName, password }),
                success: function (response) {
                    if (response){
                        localStorage.setItem('access_token', response.tokenInfo.token);
                        var userInfo = AuthCore.decodeToken(response.tokenInfo.token);
                        if(userInfo){
                            AuthCore.redirectByRole(userInfo.typ);
                        }
                    }
                },
                error: function (xhr) {
                    if (xhr.responseJSON && xhr.responseJSON.message){
                        const errors = xhr.responseJSON.message;
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi đăng nhập',
                            text: errors
                        });
                    }
                    else{
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi hệ thống',
                            text: 'Không thể đăng nhập, thử lại sau!'
                        });
                    }
                }
            });
        });
    },
    onLogout: function () {
        $.ajax({
            url: '/login/logout',
            method: 'POST',
            success: function () {
                localStorage.removeItem('access_token');
                window.location.reload();
            },
            error: function () {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi đăng xuất',
                    text: 'Không thể đăng xuất. Vui lòng thử lại.'
                });
            }
        });
    },
    onRegister: function(){
        $('#register-form').on('submit', function (e) {
            e.preventDefault();

            // Lấy dữ liệu
            const userName = $('#username').val().trim();
            const email = $('#email').val().trim();
            const password = $('#password').val();
            const confirmPassword = $('#comfirmpass').val();

            // Xóa lỗi cũ nếu có
            $('.error-message').remove();

            // Biến chứa lỗi
            const errors = [];

            // ✅ VALIDATE
            const usernameRegex = /^[a-zA-Z0-9_.]+$/;
            const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;

            if (!userName) {
                errors.push({ field: 'username', message: 'Tên đăng nhập không được để trống' });
            } else {
                if (userName.length > 20) {
                    errors.push({ field: 'username', message: 'Tên đăng nhập không được quá 20 ký tự' });
                }
                if (!usernameRegex.test(userName)) {
                    errors.push({ field: 'username', message: 'Tên đăng nhập không được chứa ký tự đặc biệt' });
                }
            }

            if (!email) {
                errors.push({ field: 'email', message: 'Email không được để trống' });
            } else {
                if (email.length > 50) {
                    errors.push({ field: 'email', message: 'Email không được quá 50 ký tự' });
                }
                if (!emailRegex.test(email)) {
                    errors.push({ field: 'email', message: 'Email không đúng định dạng' });
                }
            }

            if (!password) {
                errors.push({ field: 'password', message: 'Mật khẩu không được để trống' });
            }else {
                if (password.length > 26 || password.length < 8) {
                    errors.push({ field: 'email', message: 'Mật khẩu có tối thiểu 8 kí tự và tối đa 26 kí tự!' });
                }
            }

            if (password !== confirmPassword) {
                errors.push({ field: 'comfirmpass', message: 'Mật khẩu xác nhận không khớp' });
            }

            // ❌ Nếu có lỗi
            if (errors.length > 0) {
                const msg = errors.map(e => `• ${e.message}`).join('\n');
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi nhập liệu',
                    text: msg
                });

                return; // Ngăn submit
            }

            // ✅ Nếu không có lỗi thì gọi API như cũ
            $.ajax({
                url: '/register/onRegister',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    userName: userName,
                    email: email,
                    password: password
                }),
                success: function (response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thông báo',
                        text: response.message
                    }).then(() => {
                        window.location.href = '/login';
                    });
                },
                error: function (xhr) {
                    if (xhr.status === 400 && xhr.responseJSON && xhr.responseJSON.message) {
                        const errors = xhr.responseJSON.message;


                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi nhập liệu',
                            text: errors
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi hệ thống',
                            text: 'Không thể đăng ký. Vui lòng thử lại sau.'
                        });
                    }
                }
            });
        });
    },
    checkTokenExpiry : function () {
        const token = localStorage.getItem('access_token');
        if (!token) return;

        try {
            const decoded = this.decodeToken(token);
            const now = Math.floor(Date.now() / 1000);

            if (decoded.exp && decoded.exp < now) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Phiên đăng nhập đã hết hạn',
                    text: 'Vui lòng đăng nhập lại.',
                    confirmButtonText: 'Đăng nhập lại',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then(() => {
                    localStorage.removeItem('access_token');
                    window.location.href = '/login';
                });
            }
        } catch (e) {
            console.error("Không thể kiểm tra token:", e);
            localStorage.removeItem('access_token');
            window.location.href = '/login';
        }
    }
}
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("chat-box").style.display = "none";
});
const customBoxChat = {
    db: null,
    messagesRef: null,
    onInit: function(){
        this.db = this.onConfigFireBase();
        this.messagesRef = this.onGetSessionID();
        this.onListenNewMessage();
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

        const db = firebase.database(app);

        return db;
    },

    onGetSessionID: function(){
        if(localStorage.getItem("access_token")){
            const userInfo = AuthCore.decodeToken(localStorage.getItem("access_token"))
            const sessionId = userInfo.sub;
            if(sessionId !== "MASTER_ADMIN"){
                localStorage.setItem("sessionId", sessionId);
                return messagesRef = this.db.ref("messages/" + sessionId);
            }
        }
        return null;
    },

    toggleChat: function() {
        if(!localStorage.getItem("access_token"))
        {
            swal.fire({
                title: "Thông báo!",
                text: "Bạn phải đăng nhập mới có thể chat với admin.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Đăng nhập",
                cancelButtonText: "Hủy"
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "/login"
                }
            });
        }else{
            const box = document.getElementById("chat-box");
            box.style.display = (box.style.display === "none" || box.style.display === "") ? "flex" : "none";
            this.sendWelcomeIfFirstTime();
        }
    },
    sendMessage: function() {
        const input = document.getElementById("messageInput");
        messagesRef.push({
            sender: "user",
            content: input.value,
            timestamp: Date.now()
        });
        input.value = "";
    },
    sendWelcomeIfFirstTime: function(){
        if (!this.messagesRef) return;

        this.messagesRef.once("value", (snapshot) => {
            const messages = snapshot.val();

            if (!messages) {
                this.messagesRef.push({
                    sender: "admin",
                    content: "Xin chào! Tôi có thể giúp gì cho bạn?",
                    timestamp: Date.now()
                });
            }
        });
    },

    onListenNewMessage: function() {
        var userInfo = AuthCore.decodeToken(localStorage.getItem("access_token"));
        if(userInfo.typ === 0 || userInfo.typ === 1 || userInfo.sub === "MASTER_ADMIN"){
            return;
        }
        messagesRef.on("child_added", (snapshot) => {
            const msg = snapshot.val();
            const messagesContainer = document.getElementById("messages");

            const messageDiv = document.createElement("div");
            messageDiv.className = `message ${msg.sender === "admin" ? "staff" : "user"}`;

            const sender = document.createElement("span");
            sender.className = "sender";
            sender.textContent = msg.sender === "admin" ? "🧑‍💼 Nhân viên" : `🧑 ${userInfo.firstName + ' ' + userInfo.lastName}`;
            messageDiv.appendChild(sender);

            const content = document.createElement("div");
            content.textContent = msg.content;
            messageDiv.appendChild(content);

            const timestamp = document.createElement("div");
            timestamp.className = "timestamp";
            const date = new Date(msg.timestamp);
            timestamp.textContent = date.getHours().toString().padStart(2, '0') + ":" + date.getMinutes().toString().padStart(2, '0');
            messageDiv.appendChild(timestamp);

            messagesContainer.appendChild(messageDiv);
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        });
    }}


