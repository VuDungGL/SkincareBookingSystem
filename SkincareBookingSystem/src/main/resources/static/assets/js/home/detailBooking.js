$(document).ready(function() {
    onRenderData.onInit();
})

const onRenderData = {
    onInit: function(){
        this.onRenderButton();
        this.onFormatDateDetailBooking();
        this.onRenderActiveStart();
    },
    onFormatDateDetailBooking: function(){
        const dateSpan = document.getElementById('appointment-date');
        if (!dateSpan) return;

        const rawDate = dateSpan.getAttribute('value') || dateSpan.textContent.trim();
        if (!rawDate) return;

        const formattedDate = coreConst.formatDate(rawDate);
        dateSpan.textContent = formattedDate;
    },
    onRenderButton: function () {
        const paymentButton = document.getElementById('button-payment');
        const cancelButton = document.getElementById('cancel-button');
        const editButton = document.getElementById('edit-button');
        const feedbackBox = document.getElementById('feedback-box');

        const appointmentStatus = parseInt(document.getElementById('appointment-status').dataset.status);
        const appointmentPaymentStatus = document.getElementById('appointment-payment-status').dataset.paid === 'true';

        if (appointmentStatus === 1) {
            feedbackBox.style.display = "block";
            cancelButton.style.display = "none";
            editButton.style.display = "none";
            if (!appointmentPaymentStatus) {
                paymentButton.style.display = "block";
            } else {
                paymentButton.style.display = "none";
            }
        } else if (appointmentStatus === 0) {
            feedbackBox.style.display = "none";
            cancelButton.style.display = "block";
            editButton.style.display = "block";
            if (!appointmentPaymentStatus) {
                paymentButton.style.display = "block";
            } else {
                paymentButton.style.display = "none";
                cancelButton.style.display = "none";
                editButton.style.display = "none";
                feedbackBox.style.display = "block";
            }
        } else {
            feedbackBox.style.display = "none";
            cancelButton.style.display = "none";
            editButton.style.display = "none";
            paymentButton.style.display = "none";
        }
    },

    onCreatePayment: function(bookingID, amount,bookingDetailID){
        swal.fire({title: 'Xác nhận thanh toán?',
            text: 'Bạn sẽ không thể hủy hoặc chỉnh sửa đặt lịch sau khi đã thanh toán! Bạn sẽ được chuyển đến cổng thanh toán.',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#ff4d94',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Thanh Toán',
            cancelButtonText: 'Thoát'
        }).then((result) => {
            if (result.isConfirmed) {
               $.ajax({
                   url: "payment/create-payment-url",
                   method: 'POST',
                   contentType: "application/json",
                   data: JSON.stringify({
                       amount: amount.toString(),
                       bankCode: "",
                       language: "vn",
                       ipAddress: "127.0.0.1",
                       bookingId: bookingID.toString(),
                       bookingDetailID: bookingDetailID.toString()
                   }),
                   success: function (response) {
                       var responseObj = JSON.parse(response);
                       if (responseObj.code === "00") {
                           var url = responseObj.data;
                           window.location.href = url;
                       } else {
                           Swal.fire('Lỗi!', responseObj.message, 'error');
                       }
                   },
                   error: function(xhr, status, error) {
                       console.error('AJAX error:', error);
                       Swal.fire('Lỗi!', 'Đã có lỗi xảy ra khi tạo URL thanh toán.', 'error');
                   }
               })
            }
        });
    },

    onCancelBooking: function(bookingDetailID){
        Swal.fire({
            title: 'Bạn có chắc chắn muốn hủy đơn hàng?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#ff4d94',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Hủy Đơn',
            cancelButtonText: 'Thoát'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "booking/onCancelBooking",
                    method: 'POST',
                    contentType: "application/json",
                    data: JSON.stringify({
                        bookingDetailID:bookingDetailID
                    }),
                    success: function (response) {
                        Swal.fire({
                            title: 'Thành công!',
                            icon: 'success',
                            text: response.message
                        }).then(() => {
                            location.reload();
                        });
                    }
                })
            }
        });
    },
    setRating: function(rating) {
        $(".star-rating i").each(function(index) {
            if (index < rating) {
                $(this).addClass("star-active");
            } else {
                $(this).removeClass("star-active");
            }
        });
    },

    onRenderActiveStart: function(){
        $(".star-rating i").on("click", function() {
            var rating = $(this).data("value");
            onRenderData.setRating(rating);
        });
    },

    onCreateFeedback: function(bookingDetailID){
        var rating = $(".star-rating i.star-active").length;
        var feedbackText = $(".feedback-textarea").val();
        var userID = AuthCore.decodeToken(localStorage.getItem("access_token")).sub;

        if (!feedbackText || feedbackText.length < 36 || feedbackText.length > 450) {
            Swal.fire({
                icon: 'warning',
                title: 'Lỗi',
                text: 'Vui lòng nhập feedback có ít nhất 36 ký tự và tối đa 450 ký tự.'
            });
            return;
        }

        $.ajax({
            url: "/users/createFeedback",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                rating: rating,
                comment: feedbackText,
                userID: userID,
                bookingDetailID: bookingDetailID
            }),
            success: function(response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Cảm ơn bạn!',
                    text: 'Feedback của bạn đã được gửi thành công.'
                }).then(() => {
                    window.location.reload();
                });
            },
            error: function(error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Đã có lỗi xảy ra khi gửi feedback. Vui lòng thử lại.'
                });
            }
        });
    }


}

