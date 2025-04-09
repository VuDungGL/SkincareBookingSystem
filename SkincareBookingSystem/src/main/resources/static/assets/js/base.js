$(document).ready(function () {
    AuthCore.onInit();
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
    formatDate2:function(isoString) {
        if (!isoString) return '';
        const date = new Date(isoString);
        return date.toISOString().split('T')[0];
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
}
const AuthCore = {
    onInit: function () {
        this.onLogin();
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
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi đăng nhập',
                        text: xhr.responseText
                    });
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
    }
};
