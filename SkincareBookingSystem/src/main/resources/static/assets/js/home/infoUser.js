$(document).ready(function(){
    onLoadInfo.onInit();
});

const onLoadInfo ={
    onInit: function(){
        this.onHideUserCard();
        this.onLoadUserBox();
        this.onInputName();
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
    }



}