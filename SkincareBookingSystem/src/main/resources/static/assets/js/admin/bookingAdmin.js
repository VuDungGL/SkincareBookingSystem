$(document).ready(function(){
    onLoadDataBooking.onInit();
});

const onLoadDataBooking = {
    onInit : function(){
        this.onLoadUnPaidBooking();
        this.onLoadPaidBooking();
    },
     onLoadUnPaidBooking: function( pageSize = 6, pageIndex = 0){
         var search = $('#search-input-unpaid')[0].value;
        $.ajax({
            url: "/admin/booking/getAll",
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                pageIndex: pageIndex,
                pageSize: pageSize,
                searchText: search,
                isPaid: false
            }),
            success: function(response){
                if (response){
                    let container = $("#table-unpaid-content");

                    container.empty();

                    response.content.forEach((booking, index) => {
                        let createDate = baseCore.formatDate(booking.createDate);
                        let statusText = "";
                        if(booking.status == 0){
                            statusText = "Mới";
                        }else if(booking.status == 1){
                            statusText = "Đã Hoàn Thành";
                        }else if(booking.status == 2){
                            statusText = "Đã Hủy";
                        }
                        let isPaidText = ""
                        if(booking.isPaid == 1){
                            isPaidText = "Đã thanh toán";
                        }else{
                            isPaidText = "chưa thanh toán";
                        }

                        let therapistOptions = '';
                        if (booking.skinTherapistID === 0) {
                            therapistOptions = `
                                <li class="dropdown-item d-flex" onclick="onLoadDataBooking.onLoadTherapistFree('${booking.workDate}', '${booking.startTime}', ${booking.serviceID}, ${booking.bookingDetailID})">
                                    <i class="fa-solid fa-pen-to-square" style="color: black !important;"></i>Choose Therapist
                                </li>
                            `;
                                                }
                        let checkoutOption = '';
                        if (booking.status === 0) {
                            checkoutOption = `
                                <li class="dropdown-item d-flex" onclick="onLoadDataBooking.onCheckOutBooking('${booking.bookingDetailID}')">
                                    <i class="fa-solid fa-right-from-bracket" style="color: black !important;"></i>Check Out
                                </li>
                            `;
                        }

                        var memberCard = `
                            <tr>
                                <td class="data column-id">#SBS0${booking.bookingDetailID}</td>                             
                                <td class="data column-name">${booking.fullName}</td>
                                <td class="data column-department">${booking.email}</td>
                                <td class="data column-phone">${booking.phone}</td>
                                <td class="data column-phone">${booking.skinTherapistID === 0 ? "Chưa chỉ định" : booking.skinTherapistID}</td>
                                <td class="data column-birth">${booking.workDate}</td>
                                <td class="data column-birth">${booking.startTime}</td>
                                <td class="data column-birth">${booking.endTime}</td>
                                <td class="data column-position column-data-status-${booking.status}">${statusText}</td>
                                <td class="data column-email column-data-paid-${booking.isPaid}">${isPaidText}</td>
                                <td class="data column-option">
                                    <div class="dropdown">
                                        <i class="fa-solid fa-ellipsis-vertical dropdown-toggle-content"></i>
                                        <ul class="dropdown-menu" style="max-width: 120px">
                                            ${checkoutOption}
                                            ${therapistOptions}                        
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        `;

                        container.append(memberCard);
                    })
                    $('#page-staff-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    baseCore.updatePagination(response.totalPages, response.number, "onLoadDataBooking.onLoadUnPaidBooking");
                    baseCore.onLoadDropdown();
                }
            }
        })
     },
    onLoadPaidBooking: function( pageSize = 6, pageIndex = 0){
        var search = $('#search-input-paid')[0].value;
        $.ajax({
            url: "/admin/booking/getAll",
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                pageIndex: pageIndex,
                pageSize: pageSize,
                searchText: search,
                isPaid: true
            }),
            success: function(response){
                if (response){
                    let container = $("#table-paid-content");

                    container.empty();

                    response.content.forEach((booking, index) => {
                        let createDate = baseCore.formatDate(booking.createDate);
                        let statusText = "";
                        if(booking.status == 0){
                            statusText = "Mới";
                        }else if(booking.status == 1){
                            statusText = "Đã Hoàn Thành";
                        }else if(booking.status == 2){
                            statusText = "Đã Hủy";
                        }
                        let isPaidText = ""
                        if(booking.isPaid == 1){
                            isPaidText = "Đã thanh toán";
                        }else{
                            isPaidText = "chưa thanh toán";
                        }

                        let therapistOptions = '';
                        if (booking.skinTherapistID === 0) {
                            therapistOptions = `
                                <li class="dropdown-item d-flex" onclick="onLoadDataBooking.onLoadTherapistFree('${booking.workDate}', '${booking.startTime}', ${booking.serviceID}, ${booking.bookingDetailID})">
                                    <i class="fa-solid fa-pen-to-square" style="color: black !important;"></i>Choose Therapist
                                </li>
                            `;
                        }
                        let checkoutOption = '';
                        if (booking.status === 0) {
                            checkoutOption = `
                                <li class="dropdown-item d-flex" onclick="onLoadDataBooking.onCheckOutBooking('${booking.bookingDetailID}')">
                                    <i class="fa-solid fa-right-from-bracket" style="color: black !important;"></i>Check Out
                                </li>
                            `;
                        }

                        var memberCard = `
                            <tr>
                                <td class="data column-id">#SBS0${booking.bookingDetailID}</td>                             
                                <td class="data column-name">${booking.fullName}</td>
                                <td class="data column-department">${booking.email}</td>
                                <td class="data column-phone">${booking.phone}</td>
                                <td class="data column-phone">${booking.skinTherapistID === 0 ? "Chưa chỉ định" : booking.skinTherapistID}</td>
                                <td class="data column-birth">${booking.workDate}</td>
                                <td class="data column-birth">${booking.startTime}</td>
                                <td class="data column-birth">${booking.endTime}</td>
                                <td class="data column-position column-data-status-${booking.status}">${statusText}</td>
                                <td class="data column-email column-data-paid-${booking.isPaid}">${isPaidText}</td>
                                <td class="data column-option">
                                    <div class="dropdown">
                                        <i class="fa-solid fa-ellipsis-vertical dropdown-toggle-content"></i>
                                        <ul class="dropdown-menu" style="max-width: 120px">
                                            ${checkoutOption}
                                            ${therapistOptions}                        
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        `;
                        container.append(memberCard);
                    })
                    $('#page-paid-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    onLoadDataBooking.updatePaginationPaid(response.totalPages, response.number, "onLoadDataBooking.onLoadUnPaidBooking");
                    baseCore.onLoadDropdown();
                }
            }
        })
    },
    updatePaginationPaid: function(totalPages, currentPage, functionActive) {
        let paginationContainer = $("#pagination-page-paid");
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

    onGetTherapistFree: function(workDate, startTime, serviceID){
        const data = {
            workDate: workDate,
            startTime: startTime,
            serviceID: serviceID
        };

        return $.ajax({
            url: "/admin/booking/getListTherapistFree",
            method: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json"
        });
    },

    onChooseTherapist: function(therapistID, bookingDetailID) {
        Swal.fire({
            title: 'Bạn có chắc?',
            text: 'Bạn muốn chọn nhân viên này cho lịch hẹn?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/booking/onChooseTherapist",
                    method: "POST",
                    data: JSON.stringify({
                        therapistID:therapistID,
                        bookingDetailID:bookingDetailID
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function(response){
                        if(response){
                            swal.fire({
                                title: "Thành công",
                                text: response.message,
                                icon: "success"
                            }).then(() => {
                                location.reload();
                            });
                        }
                    }
                });
            }
        });
    },
    onLoadTherapistFree: function(workDate, startTime, serviceID, bookingDetailID){
        onLoadDataBooking.onGetTherapistFree(workDate, startTime, serviceID).done(function (therapists) {
            if (!therapists || therapists.length === 0) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Không có nhân viên!',
                    text: 'Hiện không có nhân viên trong thời gian này. Liên hệ với khách hàng để thay đổi lịch.',
                    confirmButtonText: 'Đóng',
                    confirmButtonColor: '#3085d6'
                });
                return;
            }

            let slides = "";

            for (let i = 0; i < therapists.length; i += 4) {
                let slideContent = `<div class="swiper-slide"><div class="row g-4">`;

                for (let j = i; j < i + 4 && j < therapists.length; j++) {
                    let t = therapists[j];
                    slideContent += `
                <div class="col-md-6 col-lg-6 col-xl-3">
                    <div class="team-item" onclick="onLoadDataBooking.onChooseTherapist('${t.id}', '${bookingDetailID}')" style="cursor: pointer;">
                        <div class="team-img rounded-top">
                            <img src="/${t.avt}" class="img-fluid w-100 rounded-top bg-light" style="max-height: 300px; min-height: 300px;" alt="Ảnh chuyên gia">
                        </div>
                        <div class="team-text text-center p-4">
                            <h3 class="text-dark">${t.firstName} ${t.lastName}</h3>
                            <p class="mb-0">${t.expertise}</p>
                        </div>
                        <div class="team-social text-center mt-2">
                            <a class="btn btn-light btn-square rounded-circle mx-1" title="${t.email}">
                                <i class="fa-solid fa-envelope"></i>
                            </a>
                            <a class="btn btn-light btn-square rounded-circle mx-1" title="${t.phone}">
                                <i class="fa-solid fa-phone"></i>
                            </a>
                            <span class="btn btn-light btn-square rounded-circle mx-1" title="Kinh nghiệm ${t.experience} năm">
                                <i class="fa-solid fa-flask"></i>
                            </span>
                        </div>
                    </div>
                </div>
            `;
                }

                slideContent += `</div></div>`;
                slides += slideContent;
            }

            Swal.fire({
                width: '90%',
                confirmButtonText: 'Đóng',
                html: `
                    <div class="container-fluid team mb-5">
                        <div class="container py-5" style="background: white; padding: 24px; border-radius: 24px;">
                            <div class="text-center mx-auto mb-5" style="max-width: 800px;">
                                <p class="fs-4 text-uppercase" style="font-weight: 600; color: var(--third-primary-color)">Chuyên Gia Làm Đẹp</p>
                                <h1 class="display-4 mb-4">Chuyên gia Spa & Làm đẹp</h1>
                            </div>
                            <div class="swiper mySwiper">
                                <div class="swiper-wrapper">
                                    ${slides}
                                </div>
                                <div class="swiper-button-next" id="swiper-button-next" style="z-index: 999"></div>
                                <div class="swiper-button-prev" id="swiper-button-prev" style="z-index: 999"></div>
                                <div class="swiper-pagination"></div>
                            </div>
                        </div>
                    </div>
                    `,
                didOpen: () => {
                    new Swiper(".mySwiper", {
                        slidesPerView: 1,
                        spaceBetween: 30,
                        loop: true,
                        navigation: {
                            nextEl: ".swiper-button-next",
                            prevEl: ".swiper-button-prev",
                        },
                        autoplay: {
                            disableOnInteraction: false,
                        },
                        pagination: {
                            el: ".swiper-pagination",
                            clickable: true,
                        },
                    });
                }
            });
        });
    },
    onCheckOutBooking: function(bookingDetailID){
        swal.fire({
            title: "Xác nhận hoàn thành dịch vụ",
            text: "Bạn có chắc muốn xác nhận đã hoàn thành dịch vụ?",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/admin/booking/checkOutBooking",
                    method: "POST",
                    data: JSON.stringify({
                        bookingDetailID: bookingDetailID
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function(response){
                        if(response){
                            swal.fire({
                                title: "Thành công",
                                text: response.message,
                                icon: "success"
                            }).then(() => {
                                location.reload();
                            });
                        }
                    }
                });
            }
        });
    }
}