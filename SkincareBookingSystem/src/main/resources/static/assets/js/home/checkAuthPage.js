$(document).ready(function(){
    onCheckAuth.onInit();
})

const onCheckAuth = {
    onInit: function(){
        this.onCheckAuth();
    },
    onCheckAuth: function(){
        var info = null;
        const token = localStorage.getItem("access_token");
        if(token){
            info = AuthCore.decodeToken(token)
        }

        if (info == null || info.typ !== 2) {
            Swal.fire({
                title: 'Bạn cần đăng nhập để tiếp tục',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Đăng nhập',
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "/login";
                } else {
                    window.location.href = "/";
                }
            });
        }
    },
}