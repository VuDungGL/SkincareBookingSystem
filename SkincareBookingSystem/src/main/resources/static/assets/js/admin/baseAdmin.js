$(document).ready(function(){
    $.ajaxSetup({
        beforeSend: function (xhr) {
            baseCore.showLoading()
        },
        complete: function () {
            baseCore.hideLoading()
        }
    });
    onAddLeftBarActive.onInit();
    baseCore.baseInit();
});

const baseCore={
    baseInit: function(){
        this.onLoadAvt();
        this.onLoadDropdownLeftBar();
    },
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
         $('body').append('<div class="loading-overlay" style="z-index: 9999;"><div class="loader"></div></div>');
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
        if (status === 1) {
            return "Khách hàng mới";
        } else if (status === 2) {
            return "Khách hàng quen thuộc";
        }else if(status === 3){
            return "Khách hàng VIP";
        }else if(status === 4){
            return "Khách hàng cũ";
        }else if(status === 0){
            return "Chưa kích hoạt OTP";
        }
    },
    onLoadDropdown: function () {
        $(document).off('click', '.dropdown-toggle-content').on('click', '.dropdown-toggle-content', function (event) {
            event.stopPropagation();
            let menu = $(this).siblings('.dropdown-menu');

            if (menu.length) {
                $('.dropdown-menu').not(menu).fadeOut(200).css({ opacity: 0, transform: 'translateY(-10px)' }); // Ẩn các dropdown khác
                if (menu.is(':visible')) {
                    menu.fadeOut(200).css({ opacity: 0, transform: 'translateY(-10px)' });
                } else {
                    menu.css({ display: 'block' }).animate({ opacity: 1, transform: 'translateY(0px)' }, 200);
                }
            } else {
                console.warn("Dropdown menu không tồn tại.");
            }
        });

        $(document).off('click', 'body').on('click', 'body', function () {
            $('.dropdown-menu').fadeOut(200).css({ opacity: 0, transform: 'translateY(-10px)' });
        });
    },
    onLoadAvt: function(){
        const token = localStorage.getItem("access_token");
        const decoded = AuthCore.decodeToken(token);
        if (!decoded) return;

        const avatarUrl = decoded.avt || "/assets/images/base/admin/base_user.png"; // fallback ảnh mặc định
        const imgTag = `<img src="/${avatarUrl}" class="rounded-circle" style="border: 2px solid #2a4969;margin-left: 12px;" alt="User Image" width="44" height="44">`;
        const container = document.getElementById("img-box");
        if (container) {
            container.innerHTML = imgTag;
        }
    },
    toggleDropdown: function(id) {
        const submenu = document.getElementById(id);
        const isShown = submenu.classList.contains('show');

        // Đóng tất cả các menu khác (nếu cần)
        document.querySelectorAll('.nav-service-submenu').forEach(menu => {
            menu.classList.remove('show');
        });

        if (!isShown) {
            submenu.classList.add('show');
            localStorage.setItem('activeDropdown', id); // lưu lại dropdown đang mở
        } else {
            submenu.classList.remove('show');
            localStorage.removeItem('activeDropdown');
        }
    },
    onLoadDropdownLeftBar: function(){
        const activeId = localStorage.getItem('activeDropdown');
        if (activeId) {
            const submenu = document.getElementById(activeId);
            if (submenu) {
                submenu.classList.add('show');
            }
        }
    }

}

const onAddLeftBarActive = {
    onInit: function() {
        this.onLoadLeftBar();
    },

    onGetURL: function () {
        const pathname = window.location.pathname;
        const pathSegments = pathname.split('/').filter(segment => segment.length > 0);
        return pathSegments[pathSegments.length - 1];
    },

    onLoadLeftBar: function () {
        var key = onAddLeftBarActive.onGetURL();
        if (key === 'admin') {
            document.getElementById(`nav-users`).classList.add("active");
        }
        else {
            var element = document.getElementById(`nav-${key}`);
            if (element) {
                element.classList.add("active");
            }
            if(key==='service'){
                document.getElementById(`dropdown-service`).classList.add("active-dropdown");
            }else if(key === 'addService'){
                document.getElementById(`dropdown-addService`).classList.add("active-dropdown");
            }
        }
    },
}