$(document).ready(function(){
    onLoadInfo.onInit();
});

const onLoadInfo ={
    onInit: function(){
        this.onHideUserCard();
        this.onLoadUserBox();
        this.onInputName();
        this.onLoadInfoOnInfoPage();
        this.onUpdateAvt();
    },
    onHideUserCard :function () {

        var userInfoBox = document.getElementById('user-info-box');
        var userProfileCard = document.getElementById('user-profile-card');

        if (userInfoBox && userProfileCard) {
            userInfoBox.addEventListener('mouseover', function () {
                userProfileCard.style.display = 'block';
            });

            userInfoBox.addEventListener('mouseout', function () {
                userProfileCard.style.display = 'none';
            });

            userProfileCard.addEventListener('mouseover', function () {
                userProfileCard.style.display = 'block';
            });

            userProfileCard.addEventListener('mouseout', function () {
                userProfileCard.style.display = 'none';
            });
        }
    },
    onLoadUserBox:function(){
        const userData = AuthCore.decodeToken(localStorage.getItem("access_token")); // giả sử bạn lưu user info trong key "userData"

        const loginSection = document.getElementById("loginSection");
        const userInfoBox = document.getElementById("user-info-box");
        const userProfileCard = document.getElementById("user-profile-card");

        if (userData === null) {
            if (loginSection) loginSection.style.display = "flex";
            if (userInfoBox) userInfoBox.style.display = "none";
            if (userProfileCard) userProfileCard.style.display = "none";
        } else {
            if (loginSection) loginSection.style.display = "none";
            if (userInfoBox) userInfoBox.style.display = "block";

            // Gán thông tin người dùng
            document.getElementById("user-info-name").textContent = userData.firstName + " " + userData.lastName || "Tên người dùng";
            document.getElementById("user-img").src = userData.avt || "/assets/images/base/admin/default-users/male-user-wearing.png";

            document.getElementById("user-name-card").textContent = userData.firstName + " " + userData.lastName  || "Tên người dùng";
            document.getElementById("user-email-card").innerHTML = `<i class="bi bi-envelope"></i> ${userData.email || "email@example.com"}`;
            document.getElementById("user-image-card").src = userData.avt || "/assets/images/base/admin/default-users/male-user-wearing.png";
        }
    },
    onInputName: function () {
        const userData = AuthCore.decodeToken(localStorage.getItem("access_token"));

        if (!userData.firstName || !userData.lastName) {
            Swal.fire({
                title: 'Thêm thông tin',
                width: '700px',
                padding: '48px 80px',
                borderRadius: '24px',
                background: '#accffe',
                html: `
                    <div class="modal-content" style="min-height: 350px; padding: 60px 32px">
                        <div class="form-group">
                            <label>Họ:</label>
                            <input id="firstName" value="">
                        </div>                      
                        <div class="form-group">
                            <label>Tên:</label>
                            <input id="lastName" value="">
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại:</label>
                            <input id="phone" value="">
                        </div>
                        <div class="form-group">
                            <label>Ngày sinh:</label>
                            <input id="birthDate" type="date" value="">
                        </div>
                        <div class="form-group">
                            <label>Giới tính:</label>
                            <select id="gender">
                                <option value="1" selected>Nam</option>
                                <option value="2" >Nữ</option>
                                <option value="0" >Khác</option>
                            </select>
                        </div>
                    </div>
                    <img src="/assets/images/base/admin/create-icon-2.png" class="image-2" style="height:200px; right:12px;z-index: -1">
                    <img src="/assets/images/base/admin/image-1.png" class="image-1" style="height:123px;">
                    `,
                focusConfirm: false,
                showCancelButton: true,
                confirmButtonText: 'Cập nhật',
                preConfirm: () => {
                    const firstName = $('#firstName').val().trim();
                    const lastName = $('#lastName').val().trim();
                    const phone = $('#phone').val().trim();
                    const birthDay = $('#birthDate').val();
                    const birthDateISO = new Date(birthDay).toISOString();
                    const gender = $('#gender').val();

                    const nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/u;
                    const phoneRegex = /^\d{10,11}$/;

                    if (!firstName || !lastName || !phone || !birthDay || gender === '') {
                        Swal.showValidationMessage('Tất cả các trường là bắt buộc');
                        return false;
                    }

                    if (!nameRegex.test(firstName) || !nameRegex.test(lastName)) {
                        Swal.showValidationMessage('Họ và tên không hợp lệ (chỉ gồm chữ cái và không có ký tự đặc biệt)');
                        return false;
                    }

                    if (!phoneRegex.test(phone)) {
                        Swal.showValidationMessage('Số điện thoại phải có 10 hoặc 11 chữ số');
                        return false;
                    }

                    const today = new Date();
                    const birthDate = new Date(birthDay);
                    let age = today.getFullYear() - birthDate.getFullYear();
                    const m = today.getMonth() - birthDate.getMonth();
                    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                        age--;
                    }

                    if (age < 18) {
                        Swal.showValidationMessage('Bạn phải đủ 18 tuổi');
                        return false;
                    }

                    return { firstName, lastName, phone, birthDay: birthDateISO, gender };
                }
            }).then((result) => {
                if (result.isConfirmed && result.value) {
                    const formValues = result.value;

                    $.ajax({
                        url: '/users/updateInfo',
                        method: 'POST',
                        contentType: 'application/json',
                        headers: {
                            'Authorization': 'Bearer ' + localStorage.getItem("access_token")
                        },
                        data: JSON.stringify(formValues),
                        success: function (res) {
                            if (res.status === 200) {
                                localStorage.removeItem('access_token');
                                localStorage.setItem('access_token', res.data.token);
                                Swal.fire({
                                    icon: "success",
                                    title: "Thông báo",
                                    text: res.message || "Cập nhật thành công"
                                }).then(() => {
                                    window.location.reload();
                                });
                            }
                        },
                        error: function (xhr) {
                            const msg = xhr.responseJSON?.message || 'Không thể cập nhật. Vui lòng thử lại sau.';
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi',
                                text: msg
                            });
                        }
                    });
                }
            });
        }
    },

    onLoadInfoOnInfoPage: function (){
        const user = AuthCore.decodeToken(localStorage.getItem("access_token"));

        document.getElementById("fullName-display-1").textContent = user.firstName + " " + user.lastName;
        document.getElementById("fullName-display-2").textContent = user.firstName + " " + user.lastName;
        document.getElementById("firstName-input").value = user.firstName;
        document.getElementById("lastName-input").value = user.lastName;

        document.getElementById("birthDate-display").textContent = coreConst.formatDate(user.birthdate);
        document.getElementById("birthDate-input").value = coreConst.formatDate2(user.birthdate);

        document.getElementById("email-display").textContent = user.email;
        document.getElementById("email-input").value = user.email;

        document.getElementById("phone-display").textContent = user.phone;
        document.getElementById("phone-input").value = user.phone;

        document.getElementById("gender-display").textContent = coreConst.formatGenderText(user.gender);
        document.getElementById("gender-input").value = user.gender;
    },

    onUpdateName: function(){
        var firstName = $(`#firstName-input`).val().trim();
        var lastName = $(`#lastName-input`).val().trim();
        var user = AuthCore.decodeToken(localStorage.getItem("access_token"));

        const nameRegex = /^[a-zA-ZÀ-Ỹà-ỹ\s]+$/;

        if (!firstName || !lastName) {
            swal.fire({
                title: "Lỗi nhập liệu",
                text: "Họ và tên không được để trống!",
                icon: "error"
            });
            return;
        }

        if (!nameRegex.test(firstName) || !nameRegex.test(lastName)) {
            swal.fire({
                title: "Lỗi định dạng",
                text: "Họ và tên chỉ được chứa chữ cái và khoảng trắng!",
                icon: "error"
            });
            return;
        }

        swal.fire({
            title: "Xác nhận",
            text: `Bạn có chắc muốn thay đổi họ tên thành "${firstName} ${lastName}" không?`,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/updateInfo",
                    method: "POST",
                    data: JSON.stringify({
                        firstName: firstName,
                        lastName: lastName,
                        userID: user.sub
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function(response){
                        if(response){
                            localStorage.removeItem("access_token");
                            localStorage.setItem("access_token", response.data.token);
                            swal.fire({
                                title: "Thành công",
                                text: response.message,
                                icon: "success"
                            }).then(() => {
                                location.reload();
                            });
                        }
                    },
                    error: function(xhr) {
                        swal.fire({
                            title: "Lỗi hệ thống",
                            text: "Không thể cập nhật họ tên, vui lòng thử lại.",
                            icon: "error"
                        });
                    }
                });
            }
        });
    },
    onUpdatePhone: function(){
        var phone = $(`#phone-input`).val().trim();
        var user = AuthCore.decodeToken(localStorage.getItem("access_token"));

        const phoneRegex = /^\d{10,12}$/;

        if (!phone) {
            swal.fire({
                title: "Lỗi nhập liệu",
                text: "Số điện thoại không được để trống!",
                icon: "error"
            });
            return;
        }

        if (!phoneRegex.test(phone)) {
            swal.fire({
                title: "Lỗi định dạng",
                text: "Số điện thoại phải có từ 10 đến 12 chữ số và không chứa ký tự khác!",
                icon: "error"
            });
            return;
        }

        swal.fire({
            title: "Xác nhận",
            text: `Bạn có chắc muốn thay đổi số điện thoại thành ${phone} không?`,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/updateInfo",
                    method: "POST",
                    data: JSON.stringify({
                        phone: phone,
                        userID: user.sub
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function(response){
                        if(response){
                            localStorage.removeItem("access_token");
                            localStorage.setItem("access_token", response.data.token);
                            swal.fire({
                                title: "Thành công",
                                text: response.message,
                                icon: "success"
                            }).then(() => {
                                location.reload();
                            });
                        }
                    },
                    error: function() {
                        swal.fire({
                            title: "Lỗi hệ thống",
                            text: "Không thể cập nhật số điện thoại. Vui lòng thử lại.",
                            icon: "error"
                        });
                    }
                });
            }
        });
    },
    onUpdateEmail: function(){
        var email = $(`#email-input`).val().trim();
        var user = AuthCore.decodeToken(localStorage.getItem("access_token"));

        const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;

        // Validate
        if (!email) {
            swal.fire({
                title: "Lỗi nhập liệu",
                text: "Email không được để trống!",
                icon: "error"
            });
            return;
        }

        if (email.length > 50) {
            swal.fire({
                title: "Lỗi độ dài",
                text: "Email không được vượt quá 50 ký tự!",
                icon: "error"
            });
            return;
        }

        if (!emailRegex.test(email)) {
            swal.fire({
                title: "Lỗi định dạng",
                text: "Email phải có dạng example@gmail.com!",
                icon: "error"
            });
            return;
        }

        swal.fire({
            title: "Xác nhận",
            text: `Bạn có chắc muốn thay đổi email thành ${email} không?`,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/updateInfo",
                    method: "POST",
                    data: JSON.stringify({
                        email: email,
                        userID: user.sub
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function(response){
                        if(response){
                            localStorage.removeItem("access_token");
                            localStorage.setItem("access_token", response.data.token);
                            swal.fire({
                                title: "Thành công",
                                text: response.message,
                                icon: "success"
                            }).then(() => {
                                location.reload();
                            });
                        }
                    },
                    error: function() {
                        swal.fire({
                            title: "Lỗi hệ thống",
                            text: "Không thể cập nhật email. Vui lòng thử lại.",
                            icon: "error"
                        });
                    }
                });
            }
        });
    },
    onUpdateBirthDate: function () {
        var birthDate = $("#birthDate-input").val();
        var user = AuthCore.decodeToken(localStorage.getItem("access_token"));

        if (!birthDate) {
            swal.fire({
                title: "Lỗi nhập liệu",
                text: "Vui lòng chọn ngày sinh!",
                icon: "error"
            });
            return;
        }

        const selectedDate = new Date(birthDate);
        const today = new Date();
        const age = today.getFullYear() - selectedDate.getFullYear();
        const monthDiff = today.getMonth() - selectedDate.getMonth();
        const dayDiff = today.getDate() - selectedDate.getDate();

        const isUnder18 = age < 18 || (age === 18 && (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)));

        if (isUnder18) {
            swal.fire({
                title: "Không hợp lệ",
                text: "Bạn phải đủ 18 tuổi trở lên!",
                icon: "warning"
            });
            return;
        }

        const offsetInMs = 7 * 60 * 60 * 1000;
        const localDate = new Date(selectedDate.getTime() + offsetInMs);
        const isoString = localDate.toISOString().split('T')[0] + "T00:00:00+07:00";

        swal.fire({
            title: "Xác nhận",
            text: `Bạn có chắc muốn cập nhật ngày sinh thành ${birthDate}?`,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/updateInfo",
                    method: "POST",
                    data: JSON.stringify({
                        birthDay: isoString,
                        userID: user.sub
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function (response) {
                        if (response) {
                            localStorage.removeItem("access_token");
                            localStorage.setItem("access_token", response.data.token);
                            swal.fire({
                                title: "Thành công",
                                text: response.message,
                                icon: "success"
                            }).then(() => {
                                location.reload();
                            });
                        }
                    },
                    error: function () {
                        swal.fire({
                            title: "Lỗi hệ thống",
                            text: "Không thể cập nhật ngày sinh. Vui lòng thử lại.",
                            icon: "error"
                        });
                    }
                });
            }
        });
    },
    onUpdateGender: function () {
        var gender = parseInt($("#gender-input").val());
        var user = AuthCore.decodeToken(localStorage.getItem("access_token"));

        if (!gender || isNaN(gender)) {
            swal.fire({
                title: "Lỗi nhập liệu",
                text: "Vui lòng chọn giới tính hợp lệ!",
                icon: "error"
            });
            return;
        }

        let genderText = "";
        switch (gender) {
            case 1: genderText = "Nam"; break;
            case 2: genderText = "Nữ"; break;
            case 3: genderText = "Khác"; break;
            default: genderText = "Không xác định"; break;
        }

        swal.fire({
            title: "Xác nhận",
            text: `Bạn có chắc muốn cập nhật giới tính thành ${genderText}?`,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/updateInfo",
                    method: "POST",
                    data: JSON.stringify({
                        gender: gender,
                        userID: user.sub
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function (response) {
                        if (response) {
                            localStorage.removeItem("access_token");
                            localStorage.setItem("access_token", response.data.token);
                            swal.fire({
                                title: "Thành công",
                                text: response.message,
                                icon: "success"
                            }).then(() => {
                                location.reload();
                            });
                        }
                    },
                    error: function () {
                        swal.fire({
                            title: "Lỗi hệ thống",
                            text: "Không thể cập nhật giới tính. Vui lòng thử lại.",
                            icon: "error"
                        });
                    }
                });
            }
        });
    },
    onUpdateAvt: function () {
        const user = AuthCore.decodeToken(localStorage.getItem("access_token"));
        $("#avatar-img").attr("src", user.avt || "assets/images/default-avatar.png");

        $(".edit_avt-btn").on("click", function () {
            swal.fire({
                title: "Tải lên ảnh mới",
                width: 700,
                html: `
            <style>
                .custom-upload {
                    padding: 15px;
                    background: #f8f9fa;
                    border-radius: 12px;
                    text-align: center;
                    font-family: 'Segoe UI', sans-serif;
                }

                .upload-area {
                    border: 2px dashed #4285f4;
                    border-radius: 10px;
                    padding: 30px;
                    cursor: pointer;
                    color: #333;
                    transition: background 0.2s;
                    display: block;
                    margin-bottom: 10px;
                }

                .upload-area:hover {
                    background: #e8f0fe;
                }

                .upload-icon {
                    margin: auto;
                    font-size: 36px;
                    display: block;
                    margin-bottom: 10px;
                    max-height: 450px;
                }

                .file-info {
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    padding: 5px 10px;
                    border-radius: 8px;
                    background-color: #e0e0e0;
                }

                .file-name {
                    font-size: 14px;
                    flex: 1;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    text-align: left;
                }

                .remove-btn {
                    background: transparent;
                    border: none;
                    font-size: 18px;
                    cursor: pointer;
                    color: #ff3b30;
                }

                #preview-img {
                    margin-top: 10px;
                    max-width: 100%;
                    max-height: 450px;
                    display: none;
                    border-radius: 10px;
                }
            </style>

            <div class="custom-upload">
                <label class="upload-area" for="avatar-input">
                    <svg class="upload-icon" viewBox="0 0 24 24" fill="none">
                        <path d="M7 10V9C7 6.23858 9.23858 4 12 4C14.7614 4 17 6.23858 17 9V10C19.2091 10 21 11.7909 21 14C21 15.4806 20.1956 16.8084 19 17.5M7 10C4.79086 10 3 11.7909 3 14C3 15.4806 3.8044 16.8084 5 17.5M7 10C7.43285 10 7.84965 10.0688 8.24006 10.1959M12 12V21M12 12L15 15M12 12L9 15" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <p id="upload-text">Browse File to upload!</p>
                    <img id="preview-img" />
                    <input type="file" id="avatar-input" accept="image/*" hidden>
                </label>
            </div>
            `,
                showCancelButton: true,
                confirmButtonText: 'Cập nhật',
                cancelButtonText: 'Hủy',
                preConfirm: () => {
                    const fileInput = document.getElementById('avatar-input');
                    if (!fileInput.files || fileInput.files.length === 0) {
                        Swal.showValidationMessage('Vui lòng chọn một ảnh');
                        return false;
                    }
                    return fileInput.files[0];
                },
                didOpen: () => {
                    $("#avatar-input").on("change", function () {
                        const file = this.files[0];
                        if (file) {
                            $("#file-name").text(file.name);
                            const reader = new FileReader();
                            reader.onload = function (e) {
                                $("#preview-img").attr("src", e.target.result).show();
                                $(".upload-icon, #upload-text").hide();
                            };
                            reader.readAsDataURL(file);
                        }
                    });

                    $("#remove-file").on("click", function () {
                        $("#avatar-input").val("");
                        $("#file-name").text("Not selected file");
                        $("#preview-img").hide();
                        $(".upload-icon, #upload-text").show();
                    });
                }
            }).then((result) => {
                if (result.isConfirmed && result.value) {
                    const file = result.value;
                    const formData = new FormData();
                    formData.append("userID", user.sub);
                    formData.append("avatar", file);

                    $.ajax({
                        url: "/users/updateInfoAvt",
                        method: "POST",
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (response) {
                            if (response) {
                                localStorage.removeItem("access_token");
                                localStorage.setItem("access_token", response.data.token);
                                swal.fire({
                                    title: "Thành công",
                                    text: response.message,
                                    icon: "success"
                                }).then(() => {
                                    location.reload();
                                });
                            }
                        },
                        error: function () {
                            swal.fire({
                                title: "Lỗi",
                                text: "Không thể cập nhật ảnh đại diện.",
                                icon: "error"
                            });
                        }
                    });
                }
            });
        });
    },
    onChangePassword: function(){
        const oldPassword = $("#current-password").val().trim();
        const newPassword = $("#new-password").val().trim();
        const confirmPassword = $("#confirm-password").val().trim();

        if (!oldPassword || !newPassword || !confirmPassword) {
            Swal.fire("Lỗi", "Vui lòng nhập đầy đủ thông tin.", "error");
            return;
        }

        if (newPassword.length < 8 || newPassword.length > 28) {
            Swal.fire("Lỗi", "Mật khẩu mới phải từ 8 đến 28 ký tự.", "error");
            return;
        }

        if (newPassword !== confirmPassword) {
            Swal.fire("Lỗi", "Xác nhận mật khẩu không khớp.", "error");
            return;
        }

        // Lấy userID từ JWT (tuỳ hệ thống bạn đang dùng)
        const user = AuthCore.decodeToken(localStorage.getItem("access_token"));
        const userID = user?.sub;

        if (!userID) {
            Swal.fire("Lỗi", "Không tìm thấy thông tin người dùng.", "error");
            return;
        }
        swal.fire({
            title: "Xác nhận",
            text: `Bạn có chắc muốn thay đổi mặt khẩu không?`,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Hủy',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/users/changePassword",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({
                        userID: userID,
                        oldPassword: oldPassword,
                        newPassword: newPassword
                    }),
                    success: function (res) {
                        if (res.status === 200) {
                            Swal.fire("Thành công", res.message, "success").then(() => {
                                $("#current-password, #new-password, #confirm-password").val("");
                            });
                        }else {
                            Swal.fire("Lỗi", res.message, "error");
                        }
                    },
                    error: function (xhr) {
                        Swal.fire("Lỗi", xhr.responseText.message, "error");
                    }
                });
            }
        });
    }

}