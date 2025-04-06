$(document).ready(function(){
    onRenderData.onInit();
});

const onRenderData = {
    onInit: function(){
        this.renderVipCustomer();
        this.onLoadAllUsers();
    },

    renderVipCustomer: function(){
        $.ajax({
            url: "/users/getListCustomer",
            method: 'POST',
            data: JSON.stringify({
                status: 2
            }),
            contentType: "application/json",
            success: function (response){
                if(response){
                    let vipCustomerContent = $(".swiper-wrapper");
                    vipCustomerContent.empty();

                    let vipCustomerData = response;
                    let slides = "";

                    // Chia thành từng nhóm 4 therapist một slide
                    for (let i = 0; i < vipCustomerData.length; i += 4) {
                        let slideContent = `<div class="swiper-slide"><div class="row g-4">`;

                        for (let j = i; j < i + 4 && j < vipCustomerData.length; j++) {
                            let vipCustomer = vipCustomerData[j];
                            slideContent += `
                            <div class="col-md-6 col-lg-6 col-xl-3">
                                <div class="team-item">
                                    <div class="team-img rounded-top">
                                        <img src="/${vipCustomer.avt}" class="img-fluid w-100 rounded-top bg-light" style="max-height: 300px; min-height: 300px;" alt="">
                                    </div>
                                    <div class="team-text text-center p-4">
                                        <h3 class="text-dark">${vipCustomer.firstName} ${vipCustomer.lastName}</h3>
                                        <p class="mb-0">${vipCustomer.userName}</p>
                                    </div>
                                    <div class="team-social">
                                        <a class="btn btn-light btn-square rounded-circle mb-2" href="#" title="${vipCustomer.email}"><i class="fa-solid fa-envelope"></i></a>
                                        <a class="btn btn-light btn-square rounded-circle mb-2" href="#" title="${vipCustomer.phone}"><i class="fa-solid fa-phone"></i></a>
                                    </div>
                                </div>
                            </div>
                        `;
                        }

                        slideContent += `</div></div>`; // Kết thúc slide
                        slides += slideContent;
                    }

                    vipCustomerContent.append(slides);

                    // Khởi tạo Swiper sau khi cập nhật danh sách
                    new Swiper(".mySwiper", {
                        slidesPerView: "auto",
                        spaceBetween: 10,
                        loop: true,
                        autoplay: {
                            delay: 5000,
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
    onLoadAllUsers: function(pageSize = 6, pageIndex = 0){
        var search = $('#search-input')[0].value;
        $.ajax({
            url: "/users/getListCustomerByUserRole",
            method: 'POST',
            data: JSON.stringify({
                pageSize: pageSize,
                pageIndex: pageIndex,
                roleID: 2,
                searchText: search
            }),
            contentType: "application/json",
            success: function(response){
                if(response != null){
                    let container = $("#table-content");

                    container.empty();

                    response.content.forEach((user, index) => {
                        let birth = baseCore.formatDate(user.birthDate);
                        let genderText = baseCore.formatGenderText(user.gender);
                        let statusText = baseCore.formatStatusUserText(user.status);


                        var memberCard = `
                            <tr>
                                <td class="data column-id">#SBS0${user.userID}</td>                             
                                <td class="data column-name"><img src="/${user.avt}" alt="User Image">${user.userName}</td>
                                <td class="data column-department">${user.firstName} ${user.lastName}</td>
                                <td class="data column-birth">${birth}</td>
                                <td class="data column-email">${user.email}</td>
                                <td class="data column-phone">${user.phone}</td>
                                <td class="data column-gender">${genderText}</td>
                                <td class="data column-position">${statusText}</td>
                                <td class="data column-option">
                                    <div class="dropdown">
                                        <i class="fa-solid fa-ellipsis-vertical dropdown-toggle-content"></i>
                                        <ul class="dropdown-menu" style="max-width: 120px">
                                            <li class="dropdown-item d-flex" onclick=""><i class="fa-solid fa-pen-to-square" style="color: black !important;"></i>Edit</li>
                                            <li class="dropdown-item text-danger d-flex" onclick="onRenderData.onDeleteUser(${user.userID}, '${user.userName}')"><i class="fa-solid fa-trash text-danger"></i>Remove</li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        `;
                        container.append(memberCard);
                    })
                    $('#page-staff-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    baseCore.updatePagination(response.totalPages, response.number, "onRenderData.onLoadAllUsers");
                    baseCore.onLoadDropdown();
                }
            }
        });
    },

    onDeleteUser: function(userID, userName){
        Swal.fire({
            title: "Xác nhận xóa?",
            text: `Bạn có chắc chắn muốn xóa ${userName} này không?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Xóa",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/deleteUser",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({ userID: userID }),
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
    }
}