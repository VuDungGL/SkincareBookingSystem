$(document).ready(function(){
    renderDataStaff.renderInit();
});

const renderDataStaff = {
    renderInit : function(){
        this.renderAllStaffTable();
        this.renderDepartmentInfo();
    },

    renderAllStaffTable : function(pageSize=6, pageIndex=0){
        var search = $('#search-input')[0].value;
        $.ajax({
            url: "/admin/therapist/getListSkinTherapist",
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
                        let genderText = baseCore.formatGenderText(user.gender);

                        var memberCard = `
                            <tr>
                                <td class="data column-id">#SBS0${user.skinTherapistID}</td>
                                <td class="data column-name"><img src="/${user.avt}" alt="User Image">${user.firstName} ${user.lastName}</td>
                                <td class="data column-birth">${birth}</td>
                                <td class="data column-email">${user.email}</td>
                                <td class="data column-phone">${user.phone}</td>
                                <td class="data column-gender">${genderText}</td>
                                <td class="data column-department">${user.expertise}</td>
                                <td class="data column-position">${user.salary.toLocaleString('vi-VN')}</td>
                                <td class="data column-option">
                                    <div class="dropdown">
                                        <i class="fa-solid fa-ellipsis-vertical dropdown-toggle-content"></i>
                                        <ul class="dropdown-menu" style="max-width: 120px">
                                            <li class="dropdown-item d-flex" onclick="renderDataStaff.onEditProfileTherapist(${user.skinTherapistID})"><i class="fa-solid fa-pen-to-square" style="color: black !important;"></i>Edit</li>
                                            <li class="dropdown-item text-danger d-flex" onclick="renderDataStaff.onDeleteStaff(${user.skinTherapistID})"><i class="fa-solid fa-trash text-danger"></i>Remove</li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        `;
                        container.append(memberCard);
                    })
                    $('#page-staff-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    baseCore.updatePagination(response.totalPages, response.number, "renderDataStaff.renderAllStaffTable");
                    baseCore.onLoadDropdown();
                }
            }
        });
    },

    renderDepartmentInfo: function(){
        $.ajax({
            url: "/admin/therapist/getAllSkinTherapist",
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
                                        <img src="/${therapist.avt}" class="img-fluid w-100 rounded-top bg-light" style="max-height: 300px; min-height: 300px;" alt="">
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

    onDeleteStaff: function(skinTherapistID){
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
                    url: "/admin/therapist/deleteSkinTherapist",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({ skinTherapistID: skinTherapistID }),
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

    onGetInfoTherapistByID: function(skinTherapistID){
        return $.ajax({
            url: "/admin/therapist/getSkinTherapistById",
            method: "POST",
            data: JSON.stringify({ skinTherapistID: skinTherapistID }),
            contentType: "application/json",
            dataType: "json"
        });
    },

    updateTherapist: function(data) {
        return $.ajax({
            url: "/admin/therapist/updateTherapist",
            method: "POST",
            data: data,
            contentType: false,
            processData: false,
            dataType: "json"
        });
    },

    onEditProfileTherapist: function (skinTherapistID) {
        renderDataStaff.onGetInfoTherapistByID(skinTherapistID).done(function (therapist) {
            let date = baseCore.formatDate2(therapist.birthDate );
            Swal.fire({
                title: 'Chỉnh sửa thông tin',
                width: '700px',
                background: '#accffe',
                html: `
            <div class="modal-content">
                <div class="avatar-container">
                    <input id="avtUpload" type="file" class="hidden-input">
                    <img id="avtPreview" src="/${therapist.avt || 'default.jpg'}">
                </div>

                <div class="form-group">
                    <label>Họ:</label>
                    <input id="firstName" value="${therapist.firstName || ''}">
                </div>
                <div class="form-group">
                    <label>Tên:</label>
                    <input id="lastName" value="${therapist.lastName || ''}">
                </div>
                <div class="form-group">
                    <label>Ngày sinh:</label>
                    <input id="birthDate" type="date" value="${date || ''}">
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input id="email" value="${therapist.email || ''}">
                </div>
                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <input id="phone" value="${therapist.phone || ''}">
                </div>
                <div class="form-group">
                    <label>Giới tính:</label>
                    <select id="gender">
                        <option value="1" ${therapist.gender === 1? 'selected' : ''}>Nam</option>
                        <option value="2" ${therapist.gender === 2? 'selected' : ''}>Nữ</option>
                        <option value="0" ${therapist.gender === null || therapist.gender === 0 ? 'selected' : ''}>Khác</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Chuyên môn:</label>
                    <input id="expertise" value="${therapist.expertise || ''}">
                </div>
                <div class="form-group">
                    <label>Lương:</label>
                    <input id="salary" value="${therapist.salary || ''}">
                </div>
            </div>
            <img src="/assets/images/base/admin/create-icon-2.png" class="image-2" style="height:300px; z-index: -1">
            <img src="/assets/images/base/admin/image-1.png" class="image-1" style="height:123px;">
            `,
                showCancelButton: true,
                confirmButtonText: 'Lưu',
                preConfirm: () => {
                    const firstName = $('#firstName').val().trim();
                    const lastName = $('#lastName').val().trim();
                    const phone = $('#phone').val().trim();
                    const salary = $('#salary').val().trim();

                    const vietnameseNamePattern = /^[a-zA-ZÀ-ỹ\s]+$/;
                    const phonePattern = /^[0-9]{10,15}$/;
                    const salaryPattern = /^[0-9]+$/;

                    if (!firstName || !lastName) {
                        Swal.fire('Lỗi!', 'Họ và tên không được để trống!', 'error');
                        return false;
                    }

                    if (!vietnameseNamePattern.test(firstName) || !vietnameseNamePattern.test(lastName)) {
                        Swal.fire('Lỗi!', 'Họ và tên chỉ được chứa chữ cái và dấu tiếng Việt, không chứa số hoặc ký tự đặc biệt!', 'error');
                        return false;
                    }

                    if (!phonePattern.test(phone)) {
                        Swal.fire('Lỗi!', 'Số điện thoại chỉ được nhập số và có độ dài từ 10 đến 15 ký tự!', 'error');
                        return false;
                    }

                    if (!salaryPattern.test(salary)) {
                        Swal.fire('Lỗi!', 'Lương chỉ được nhập số!', 'error');
                        return false;
                    }
                    const formData = new FormData();
                    formData.append('firstName', firstName);
                    formData.append('lastName', lastName);
                    formData.append('gender', $('#gender').val());
                    formData.append('expertise', $('#expertise').val());
                    formData.append('salary', salary);
                    formData.append('status', 1);
                    formData.append('email', $('#email').val());
                    formData.append('skinTherapistID', skinTherapistID);
                    formData.append("phone", phone)
                    let birth = baseCore.onFormatOffSetDateTime($('#birthDate').val());
                    formData.append('birthDate', birth);
                    const file = $('#avtUpload')[0].files[0];
                    if (file) formData.append('avatar', file);
                    return renderDataStaff.updateTherapist(formData);
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire('Thành công!', 'Thông tin đã được cập nhật.', 'success').then(() => {
                        location.reload();
                    });
                }
            });

            $('#avtPreview').on('click', function () {
                $('#avtUpload').click();
            });

            $('#avtUpload').on('change', function (event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = (e) => $('#avtPreview').attr('src', e.target.result);
                    reader.readAsDataURL(file);
                }
            });
        });
    },

    createTherapist: function(data) {
        return $.ajax({
            url: "/admin/therapist/createTherapist",
            method: "POST",
            data: data,
            contentType: false,
            processData: false,
            dataType: "json"
        });
    },
    onCreateProfileTherapist: function () {
        Swal.fire({
            title: 'Thêm thông tin',
            width: '700px',
            background: '#accffe',
            html: `
        <div class="modal-content">
            <div class="avatar-container">
                <input id="avtUpload" type="file" class="hidden-input">
                <img id="avtPreview" src="">
            </div>

            <div class="form-group">
                <label>Họ:</label>
                <input id="firstName" value="">
            </div>
            <div class="form-group">
                <label>Tên:</label>
                <input id="lastName" value="">
            </div>
            <div class="form-group">
                <label>Ngày sinh:</label>
                <input id="birthDate" type="date" value="">
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input id="email" value="">
            </div>
            <div class="form-group">
                <label>Số điện thoại:</label>
                <input id="phone" value="">
            </div>
            <div class="form-group">
                <label>Giới tính:</label>
                <select id="gender">
                    <option value="1" selected>Nam</option>
                    <option value="2" >Nữ</option>
                    <option value="0" >Khác</option>
                </select>
            </div>
            <div class="form-group">
                <label>Chuyên môn:</label>
                <input id="expertise" value="">
            </div>
            <div class="form-group">
                <label>Lương:</label>
                <input id="salary" value="">
            </div>
        </div>
        <img src="/assets/images/base/admin/create-icon-2.png" class="image-2" style="height:300px; z-index: -1">
        <img src="/assets/images/base/admin/image-1.png" class="image-1" style="height:123px;">
        `,
            showCancelButton: true,
            confirmButtonText: 'Lưu',
            preConfirm: () => {
                const firstName = $('#firstName').val().trim();
                const lastName = $('#lastName').val().trim();
                const phone = $('#phone').val().trim();
                const salary = $('#salary').val().trim();

                const vietnameseNamePattern = /^[a-zA-ZÀ-ỹ\s]+$/;
                const phonePattern = /^[0-9]{10,15}$/;
                const salaryPattern = /^[0-9]+$/;

                if (!firstName || !lastName) {
                    Swal.fire('Lỗi!', 'Họ và tên không được để trống!', 'error');
                    return false;
                }

                if (!vietnameseNamePattern.test(firstName) || !vietnameseNamePattern.test(lastName)) {
                    Swal.fire('Lỗi!', 'Họ và tên chỉ được chứa chữ cái và dấu tiếng Việt, không chứa số hoặc ký tự đặc biệt!', 'error');
                    return false;
                }

                if (!phonePattern.test(phone)) {
                    Swal.fire('Lỗi!', 'Số điện thoại chỉ được nhập số và có độ dài từ 10 đến 15 ký tự!', 'error');
                    return false;
                }

                if (!salaryPattern.test(salary)) {
                    Swal.fire('Lỗi!', 'Lương chỉ được nhập số!', 'error');
                    return false;
                }
                const formData = new FormData();
                formData.append('firstName', firstName);
                formData.append('lastName', lastName);
                formData.append('gender', $('#gender').val());
                formData.append('expertise', $('#expertise').val());
                formData.append('salary', salary);
                formData.append('status', 1);
                formData.append('email', $('#email').val());
                formData.append("phone", phone);
                let birth = baseCore.onFormatOffSetDateTime($('#birthDate').val());
                formData.append('birthDate', birth);
                const file = $('#avtUpload')[0].files[0];
                if (file) formData.append('avatar', file);
                return renderDataStaff.createTherapist(formData);
            }
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire('Thành công!', 'Thông tin đã được cập nhật.', 'success').then(() => {
                    location.reload();
                });
            }
        });

        $('#avtPreview').on('click', function () {
            $('#avtUpload').click();
        });

        $('#avtUpload').on('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => $('#avtPreview').attr('src', e.target.result);
                reader.readAsDataURL(file);
            }
        });
    },

}