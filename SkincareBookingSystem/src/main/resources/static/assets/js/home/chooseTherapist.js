$(document).ready(function(){
    onChooseTherapist.onInit();
});

const onChooseTherapist = {
    onInit: function () {
        localStorage.setItem("therapist_chosen", "false");

        $(window).on("beforeunload", function () {
            const isChosen = localStorage.getItem("therapist_chosen") === "true";
            if (!isChosen && localStorage.getItem("bookingDetailID")) {
                localStorage.removeItem("bookingDetailID");
                localStorage.removeItem("serviceID");
                localStorage.removeItem("startTime");
                localStorage.removeItem("workDate");
                localStorage.removeItem("therapist_chosen");
            }
        });
    },

    onChooseTherapist: function (therapistID) {
        const bookingDetailID = localStorage.getItem("bookingDetailID");

        if (!bookingDetailID) {
            Swal.fire("Lỗi!", "Không tìm thấy thông tin đặt lịch!", "error");
            return;
        }

        const data = {
            bookingDetailID: parseInt(bookingDetailID),
            therapistID: therapistID
        };

        $.ajax({
            url: "/booking/onChooseTherapist",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (res) {
                // Đánh dấu là đã chọn
                localStorage.setItem("therapist_chosen", "true");

                Swal.fire("Thành công!", "Đã chọn chuyên gia thành công!", "success").then(() => {
                    localStorage.removeItem("bookingDetailID");
                    localStorage.removeItem("therapist_chosen");
                    localStorage.removeItem("serviceID");
                    localStorage.removeItem("startTime");
                    localStorage.removeItem("workDate");
                    window.location.href = "/home";
                });
            },
            error: function () {
                Swal.fire("Lỗi!", "Không thể lưu chuyên gia!", "error");
            }
        });
    }
};
