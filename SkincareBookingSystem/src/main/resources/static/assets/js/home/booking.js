$(document).ready(function(){
    onLoadBookingPage.onInit();
});

const onLoadBookingPage = {
    onInit: function(){
        this.onCheckAuth();
        this.onLoadUserInfo();
        this.onLoadTotalPrice();
        this.onLoadTableService();
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

    onLoadUserInfo: function () {
        const token = AuthCore.decodeToken(localStorage.getItem("access_token"));
        if (token) {
            document.getElementById('fullName-of-user').value = token.firstName + " " + token.lastName;
            document.getElementById('email-of-user').value = token.email;
            document.getElementById('phone-of-user').value = token.phone;
        }
    },
    onLoadTotalPrice: function(){
        $('#serviceSelect').on('change', function () {
            const selectedOption = $(this).find('option:selected');
            const price = selectedOption.data('price');

            if (price) {
                $('#total-price').text("Tổng tiền: " + price.toLocaleString('vi-VN') + " VND");
            } else {
                $('#total-price').text('');
            }
        });
    },

    onLoadTableService: function(){
        const accessToken = localStorage.getItem("access_token");

        if (accessToken) {
            $.ajax({
                url: '/appointment/getAll',
                method: 'GET',
                success: function (services) {
                    if (Array.isArray(services.data) && services.data.length > 0) {
                        let html = ``;

                        services.data.forEach((service, index) => {
                            html += `
                            <div class="service-item">
                                <div class="service-name d-flex flex-row"><img src="/${service.illustration}" width="60px"/> 
                                    <div class="d-flex flex-column" style="margin-left: 12px">
                                        <span style="font-weight: 600">${service.serviceName}</span>
                                        <span>Thời gian: ${service.duration}'</span>
                                    </div> 
                                </div>
                              <div class="service-price">${Number(service.price).toLocaleString('vi-VN')} VND</div>
                            </div>
                          `;
                            });

                            $('#service-price-list').html(html);

                            // Show modal
                            const modal = new bootstrap.Modal(document.getElementById('servicePriceModal'));
                            modal.show();
                        } else {
                            console.warn("Không có liệu trình nào.");
                        }
                    },
                    error: function (err) {
                        console.error("Lỗi khi gọi API:", err);
                    }
            });
        }
    },

    onCreateBooking: function() {
        const fullName = $("#fullName-of-user").val().trim();
        const email = $("#email-of-user").val().trim();
        const phone = $("#phone-of-user").val().trim();
        const serviceID = parseInt($("#serviceSelect").val());
        const note = $("#area-text").val().trim();
        const workDate = $("input[type='date']").val();
        const startTime = $("input[type='time']").val();
        const selectedOption = $("#serviceSelect option:selected");
        const price = selectedOption.data("price");

        var userID = null;
        if(localStorage.getItem("access_token")){
            var info = AuthCore.decodeToken(localStorage.getItem("access_token"));
            userID = info.sub;
        }
        const therapistID = null;

        const nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/u;

        if (!fullName || !nameRegex.test(fullName)) {
            Swal.fire("Lỗi!", "Họ và tên không hợp lệ! Chỉ bao gồm chữ cái và khoảng trắng.", "warning");
            return;
        }

        if (!email || !email.includes("@")) {
            Swal.fire("Lỗi!", "Email không hợp lệ!", "warning");
            return;
        }

        if (!phone || phone.length < 9) {
            Swal.fire("Lỗi!", "Số điện thoại không hợp lệ!", "warning");
            return;
        }

        if (!serviceID || serviceID <= 0) {
            Swal.fire("Lỗi!", "Vui lòng chọn liệu trình hợp lệ!", "warning");
            return;
        }

        if (!workDate) {
            Swal.fire("Lỗi!", "Vui lòng chọn ngày làm việc!", "warning");
            return;
        }

        const selectedDate = new Date(workDate);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const tomorrow = new Date(today);
        tomorrow.setDate(today.getDate() + 1);

        if (selectedDate < tomorrow) {
            Swal.fire("Lỗi!", "Ngày làm việc phải sau ngày hiện tại ít nhất 1 ngày!", "warning");
            return;
        }

        if (!startTime) {
            Swal.fire("Lỗi!", "Vui lòng chọn thời gian bắt đầu!", "warning");
            return;
        }

        // Validate giờ từ 08:00 đến 20:00
        const [hour, minute] = startTime.split(":").map(Number);
        if (hour < 8 || hour >= 20) {
            Swal.fire("Lỗi!", "Thời gian chỉ cho phép trong khoảng từ 08:00 đến 20:00!", "warning");
            return;
        }


        const requestData = {
            bookingID: 0,
            createDate: new Date().toISOString(),
            fullName: fullName,
            phone: phone,
            email: email,
            note: note,
            workDate: workDate,
            startTime: startTime,
            endTime: null,
            serviceID: serviceID,
            userID: userID,
            therapistID: therapistID,
            price: price
        };

        $.ajax({
            url: "/booking/createBooking",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData),
            success: function (response) {
                const bookingDetailID = response.data.id;
                localStorage.setItem("bookingDetailID", bookingDetailID);
                localStorage.setItem("workDate", workDate);
                localStorage.setItem("startTime", startTime);
                localStorage.setItem("serviceID", serviceID);
                Swal.fire("Thành công!", response.message, "success").then(()=>{
                    Swal.fire({
                        title: "Thông báo",
                        text: "Bạn chưa chọn chuyên gia, chọn ngay?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Chọn ngay",
                        cancelButtonText: "Để sau",
                        allowOutsideClick: true,
                        allowEscapeKey: true,
                        willClose: () => {
                            location.reload();
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = `/chooseTherapist?workDate=${localStorage.getItem("workDate")}&startTime=${localStorage.getItem("startTime")}&serviceID=${localStorage.getItem("serviceID")}`;
                        }else{
                            localStorage.removeItem("bookingDetailID");
                            localStorage.removeItem("workDate");
                            localStorage.removeItem("startTime");
                            localStorage.removeItem("serviceID");
                            location.reload();
                        }
                    });
                });
            },
            error: function (xhr) {
                Swal.fire("Lỗi!", "Đặt lịch không thành công!", "error");
            }
        });
    },
}