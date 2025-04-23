$(document).ready(function(){
    onRenderDataHistories.onInit();
});

const onRenderDataHistories = {
    onInit: function(){
        this.onRenderListHistory();
    },

    onRenderListHistory: function(pageSize = 6 , pageIndex = 0){
        var search = $('#search-input')[0].value;
        const user = AuthCore.decodeToken(localStorage.getItem("access_token"));
        $.ajax({
            url: "/booking/getHistoryBooking",
            method: 'POST',
            data: JSON.stringify({
                pageSize: pageSize,
                pageIndex: pageIndex,
                searchText: search,
                userID: user.sub
            }),
            contentType: 'application/json',
            success: function(response){
                if (response){
                    let container = $("#table-content");

                    container.empty();

                    response.content.forEach((booking, index) => {
                        let createDate = coreConst.formatDate(booking.createDate);
                        let statusText = "";
                        let statusClass = "";
                        if (booking.status == 0) {
                            statusText = "Mới";
                            statusClass = "pending";
                        } else if (booking.status == 1) {
                            statusText = "Đã Hoàn Thành";
                            statusClass = "completed";
                        } else if (booking.status == 2) {
                            statusText = "Đã Hủy";
                            statusClass = "cancel";
                        }

                        let isPaidText = "";
                        let isPaidClass = "";
                        if (booking.isPaid == 1) {
                            isPaidText = "Đã thanh toán";
                            isPaidClass = "completed";
                        } else {
                            isPaidText = "Chưa thanh toán";
                            isPaidClass = "cancel";
                        }

                        var memberCard = `
                            <tr>
                                <td>${createDate}</td>
                                <td>${booking.serviceName}</td>
                                <td>${booking.therapistFirstName} ${booking.therapistLastName}</td>
                                <td><span class="status ${statusClass}">${statusText}</span></td>
                                <td><span class="status ${isPaidClass}">${isPaidText}</span></td>
                                <td>
                                    <a href="" class="detail-booking">Xem chi tiết</a>
                                </td>
                            </tr>
                        `;

                        container.append(memberCard);
                    });

                    $('#page-staff-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    coreConst.updatePagination(response.totalPages, response.number, "onRenderDataHistories.onRenderListHistory");
                }
            }
        })
    }
}