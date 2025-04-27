$(document).ready(function(){
    onRenderBill.onInit();
})

const onRenderBill = {
    onInit: function(){
        this.onLoadBill();
    },

    onLoadBill: function(){
        const params = new URLSearchParams(window.location.search);

        const amount = params.get('vnp_Amount');
        const bankCode = params.get('vnp_BankCode');
        const vnpayCard = params.get('vnp_CardType');
        const orderInfo = params.get('vnp_OrderInfo');
        let bookingDetailID = null;

        if (orderInfo && orderInfo.includes("BookingDetailID:")) {
            bookingDetailID = orderInfo.split("BookingDetailID:")[1];
        }
        const orderId = params.get('vnp_TxnRef');
        const transactionNo = params.get('vnp_TransactionNo');
        const payDate = params.get('vnp_PayDate');
        const responseCode = params.get('vnp_ResponseCode');

        if (responseCode === "00") {
            $(".payment-success").show();
            $(".error-content").hide();

            var details = $(".payment-details");
            details.find("span").eq(0).text(vnpayCard || "");
            details.find("span").eq(1).text(bankCode || "");
            details.find("p").eq(2).append(orderInfo || "");
            details.find("p").eq(3).append(orderId || "");
            details.find("p").eq(4).append(transactionNo || "");
            details.find("p").eq(5).append(onRenderBill.formatCurrency(amount));
            details.find("p").eq(6).append(onRenderBill.formatDate(payDate));
            $.ajax({
                url: "payment/isPaid",
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify({
                    bookingDetailID: parseInt(bookingDetailID)
                })
            })

            const transactionDateIso = onRenderBill.convertVnpDateToIso(payDate);

            $.ajax({
                url: "/payment/saveTransaction",
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify({
                    bookingDetailID: parseInt(bookingDetailID),
                    amount: parseFloat(amount),
                    paymentMethod: vnpayCard || "VNPAY",
                    transactionDate: transactionDateIso,
                    transactionCode: orderId,
                    transactionStatus: "Paid",
                    paymentStatus: "Success"
                }),

            });
        } else {
            $(".payment-success").hide();
            $(".error-content").show();
        }
    },
    convertVnpDateToIso: function(vnpDateStr) {
        const year = vnpDateStr.substring(0, 4);
        const month = vnpDateStr.substring(4, 6);
        const day = vnpDateStr.substring(6, 8);
        const hour = vnpDateStr.substring(8, 10);
        const minute = vnpDateStr.substring(10, 12);
        const second = vnpDateStr.substring(12, 14);

        return `${year}-${month}-${day}T${hour}:${minute}:${second}Z`;
    },
    formatCurrency: function(number) {
        if (!number) return "";
        return (parseInt(number) / 100).toLocaleString('vi-VN') + " ₫";
    },

    formatDate: function(dateString) {
        if (!dateString) return "";
        return dateString.substring(6,8) + '/' + dateString.substring(4,6) + '/' + dateString.substring(0,4) + ' ' + dateString.substring(8,10) + ':' + dateString.substring(10,12) + ':' + dateString.substring(12,14);
    }
}