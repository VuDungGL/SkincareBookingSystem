$(document).ready(function(){
    renderDataStaff.renderInit();
});
let modalInstance = null;
const renderDataStaff = {
    renderInit : function(){
        this.renderAllStaffTable();
        this.renderDepartmentInfo();
        this.onLoadDropdown();
    },

    renderAllStaffTable : function(pageSize=6, pageIndex=0){
        var search = $('#search-input')[0].value;
        $.ajax({
            url: "/therapist/getListSkinTherapist",
            method: 'POST',
            data: JSON.stringify({
                pageSize: pageSize,
                pageIndex: pageIndex,
                searchText: search
            }),
            contentType: "application/json",
            success: function(response){
                if(response != null){
                    let container = $("#table-content");

                    container.empty();

                    response.content.forEach((user, index) => {
                        let birth = baseCore.formatDate(user.birthDate);
                        let genderText = "Khác";

                        if (user.gender === 1) {
                            genderText = "Nam";
                        } else if (user.gender === 2) {
                            genderText = "Nữ";
                        }
                        var memberCard = `
                            <tr>
                                <td class="data column-id">#SBS0${user.skinTherapistID}</td>
                                <td class="data column-name"><img src="/${user.avt}" alt="User Image">${user.firstName} ${user.lastName}</td>
                                <td class="data column-birth">${birth}</td>
                                <td class="data column-email">${user.email}</td>
                                <td class="data column-phone">${user.phone}</td>
                                <td class="data column-gender">${genderText}</td>
                                <td class="data column-department">${user.expertise}</td>
                                <td class="data column-position">${user.salary}</td>
                                <td class="data column-option">
                                    <div class="dropdown">
                                        <i class="fa-solid fa-ellipsis-vertical dropdown-toggle-content"></i>
                                        <ul class="dropdown-menu" style="max-width: 120px">
                                            <li class="dropdown-item d-flex"><i class="fa-solid fa-pen-to-square" style="color: black !important;"></i>Edit</li>
                                            <li class="dropdown-item text-danger d-flex" onclick="renderDataStaff.onDeleteStaff(${user.skinTherapistID})"><i class="fa-solid fa-trash text-danger"></i>Remove</li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        `;
                        container.append(memberCard);
                    })
                    $('#page-staff-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    renderDataStaff.updatePagination(response.totalPages, response.number);
                    renderDataStaff.onLoadDropdown();
                }
            }
        });
    },

    updatePagination: function(totalPages, currentPage) {
        let paginationContainer = $("#pagination-page");
        paginationContainer.empty();
        let paginationHtml = '';

        let displayPage = currentPage + 1;
        let lastPage = totalPages;

        paginationHtml += currentPage === 0
            ? `<i class="fa-solid fa-angle-left opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-left" style="cursor: pointer" onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage - 1})"></i>`;

        if (totalPages <= 5) {
            for (let i = 0; i < totalPages; i++) {
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${i})" 
                                class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
            }
        } else {
            if (currentPage <= 1) {
                for (let i = 0; i <= 2; i++) {
                    paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${totalPages - 1})">${totalPages}</span>`;
            } else if (currentPage >= 2 && currentPage <= totalPages - 4) {
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage - 1})">${displayPage - 1}</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage})" class="page-active">${displayPage}</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage + 1})">${displayPage + 1}</span>`;
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${totalPages - 1})">${totalPages}</span>`;
            } else {
                paginationHtml += `<span>...</span>`;
                for (let i = totalPages - 4; i < totalPages; i++) {
                    paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
            }
        }

        paginationHtml += currentPage === totalPages - 1
            ? `<i class="fa-solid fa-angle-right opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-right" style="cursor: pointer" onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage + 1})"></i>`;

        paginationContainer.html(paginationHtml);
    },

    renderDepartmentInfo: function(){
        $.ajax({
            url: "/therapist/getAllSkinTherapist",
            method: 'GET',
            contentType: "application/json",
            success: function (response){
                if(response){
                    let departmentContent = $(".swiper-wrapper");
                    departmentContent.empty();

                    let therapists = response;
                    let slides = "";

                    // Chia thành từng nhóm 4 therapist một slide
                    for (let i = 0; i < therapists.length; i += 4) {
                        let slideContent = `<div class="swiper-slide"><div class="row g-4">`;

                        for (let j = i; j < i + 4 && j < therapists.length; j++) {
                            let therapist = therapists[j];
                            slideContent += `
                            <div class="col-md-6 col-lg-6 col-xl-3">
                                <div class="team-item">
                                    <div class="team-img rounded-top">
                                        <img src="/${therapist.avt}" class="img-fluid w-100 rounded-top bg-light" style="max-height: 250px" alt="">
                                    </div>
                                    <div class="team-text text-center p-4">
                                        <h3 class="text-dark">${therapist.firstName} ${therapist.lastName}</h3>
                                        <p class="mb-0">${therapist.expertise}</p>
                                    </div>
                                    <div class="team-social">
                                        <a class="btn btn-light btn-square rounded-circle mb-2" href="#" title="${therapist.email}"><i class="fa-solid fa-envelope"></i></a>
                                        <a class="btn btn-light btn-square rounded-circle mb-2" href="#" title="${therapist.phone}"><i class="fa-solid fa-phone"></i></a>
                                        <a class="btn btn-light btn-square rounded-circle mb-2" href="#" title="Kinh nghiệm ${therapist.experience} năm"><i class="fa-solid fa-flask"></i></a>
                                    </div>
                                </div>
                            </div>
                        `;
                        }

                        slideContent += `</div></div>`; // Kết thúc slide
                        slides += slideContent;
                    }

                    departmentContent.append(slides);

                    // Khởi tạo Swiper sau khi cập nhật danh sách
                    new Swiper(".mySwiper", {
                        slidesPerView: "auto",
                        spaceBetween: 10,
                        loop: true,
                        autoplay: {
                            delay: 3000,
                            disableOnInteraction: false,
                        },
                        navigation: {
                            nextEl: ".swiper-button-next",
                            prevEl: ".swiper-button-prev",
                        },
                        pagination: {
                            el: ".swiper-pagination",
                            clickable: true,
                        },
                    });

                }
            }
        })
    },

    onLoadSlideDepartment: function(){
        var swiper = new Swiper(".mySwiper", {
            slidesPerView: "auto",
            spaceBetween: 12,
            navigation: {
                nextEl: ".swiper-button-next",
                prevEl: ".swiper-button-prev",
            },
        });
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
    onDeleteStaff: function(staffID){
        Swal.fire({
            title: "Xác nhận xóa?",
            text: "Bạn có chắc chắn muốn xóa nhân viên này không?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Xóa",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/deleteStaff",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({ staffID: staffID }),
                    success: function (response) {
                        Swal.fire("Đã xóa!", response, "success").then(() => {
                            location.reload();
                        });
                    },
                    error: function (xhr) {
                        Swal.fire("Lỗi!", xhr.responseText, "error");
                    }
                });
            }
        });
    },
    openPopup: function () {
        var modalElement = document.getElementById('registerModal');
        if (modalElement) {
            modalInstance = new bootstrap.Modal(modalElement);
            modalInstance.show();
        } else {
            console.error("Modal element not found!");
        }
    },
    closePopup: function () {
        if (modalInstance) {
            modalInstance.hide();
        }
    }
}