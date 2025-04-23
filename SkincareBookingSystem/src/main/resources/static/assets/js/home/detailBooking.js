$(document).ready(function() {
    // Sample options for services and specialists (replace with API data if needed)
    const services = ["Spa", "Massage", "Nail Care", "Hair Styling"];
    const specialists = ["Trần Thị Mai", "Nguyễn Văn B", "Lê Thị Hồng", "Phạm Văn C"];

    // Get status from URL
    const urlParams = new URLSearchParams(window.location.search);
    const statusParam = parseInt(urlParams.get('status')); // 0: Mới, 1: Hoàn Thành, 2: Hủy

    // Sample appointment data (since no ID is provided)
    let appointment = {
        customer: "Nguyễn Văn A",
        phone: "0901234567",
        email: "nguyenvana@example.com",
        date: "2025-04-18",
        service: "Spa",
        specialist: "Trần Thị Mai",
        status: statusParam,
        time: "10:00",
        price: "450,000 VNĐ",
        paymentStatus: "Chưa Thanh Toán" // Default payment status
    };

    if (appointment.status === 1) {
        appointment.paymentStatus = "Đã Thanh Toán";
    }

    // Validate status
    if (![0, 1, 2].includes(statusParam)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: 'Trạng thái không hợp lệ!'
        });
        appointment.status = 0; // Fallback to Mới
    }

    // Function to update UI based on status
    function updateUI() {
        // Populate appointment details
        $('#appointment-customer').text(appointment.customer);
        $('#appointment-phone').text(appointment.phone);
        $('#appointment-email').text(appointment.email);
        $('#appointment-date').text(appointment.date);
        $('#appointment-service').text(appointment.service);
        $('#appointment-specialist').text(appointment.specialist);
        $('#appointment-time').text(appointment.time);
        $('#appointment-price').text(appointment.price);
        $('#appointment-payment-status').text(appointment.paymentStatus);

        // Update status display
        let statusText = '';
        let statusClass = '';
        if (appointment.status === 0) {
            statusText = 'Mới';
            statusClass = 'pending';
        } else if (appointment.status === 1) {
            statusText = 'Đã Hoàn Thành';
            statusClass = 'completed';
        } else if (appointment.status === 2) {
            statusText = 'Đã Hủy';
            statusClass = 'cancel';
        }
        $('#appointment-status').text(statusText).removeClass('pending completed cancel').addClass(statusClass);

        // Update payment status display
        let paymentStatusClass = appointment.paymentStatus === "Đã Thanh Toán" ? 'paid' : 'unpaid';
        $('#appointment-payment-status').removeClass('paid unpaid').addClass(paymentStatusClass);

        // Show/hide buttons and feedback based on status and payment status
        if (appointment.status === 1) { // Đã Hoàn Thành
            $('.pay-btn').addClass('hidden');
            $('.edit-btn').addClass('hidden');
            $('.cancel-btn').addClass('hidden');
            $('.feedback-section').removeClass('hidden');
        } else if (appointment.status === 2) { // Đã Hủy
            $('.pay-btn').removeClass('hidden').addClass('disabled');
            $('.edit-btn').removeClass('hidden').addClass('disabled');
            $('.cancel-btn').removeClass('hidden').addClass('disabled');
            $('.feedback-section').addClass('hidden');
        } else { // Mới (status=0)
            $('.edit-btn').removeClass('hidden').removeClass('disabled');
            $('.cancel-btn').removeClass('hidden').removeClass('disabled');
            $('.feedback-section').addClass('hidden');
            // Show pay button only if not yet paid
            if (appointment.paymentStatus === "Chưa Thanh Toán") {
                $('.pay-btn').removeClass('hidden').removeClass('disabled');
            } else {
                $('.pay-btn').addClass('hidden');
            }
        }
    }

    // Star rating logic
    $('.star-rating i').click(function() {
        const rating = $(this).data('value');
        $('.star-rating i').each(function() {
            if ($(this).data('value') <= rating) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        });
    });

    // Pay button logic
    $('.pay-btn').click(function(e) {
        e.preventDefault();
        if (appointment.status !== 0) {
            Swal.fire({
                icon: 'error',
                title: 'Không khả dụng',
                text: 'Thanh toán chỉ khả dụng cho đơn hàng mới!'
            });
            return;
        }
        if (appointment.paymentStatus === "Đã Thanh Toán") {
            Swal.fire({
                icon: 'error',
                title: 'Không khả dụng',
                text: 'Đơn hàng đã được thanh toán!'
            });
            return;
        }
        Swal.fire({
            title: 'Xác nhận thanh toán?',
            text: 'Bạn sẽ được chuyển đến cổng thanh toán.',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#ff4d94',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Thanh Toán',
            cancelButtonText: 'Thoát'
        }).then((result) => {
            if (result.isConfirmed) {
                appointment.paymentStatus = "Đã Thanh Toán"; // Update payment status
                updateUI();
                Swal.fire(
                    'Thành Công!',
                    'Thanh toán đã được xử lý.',
                    'success'
                );
            }
        });
    });

    // Edit button logic
    $('.edit-btn').click(function(e) {
        e.preventDefault();
        if (appointment.status !== 0) {
            Swal.fire({
                icon: 'error',
                title: 'Không khả dụng',
                text: 'Chỉnh sửa chỉ khả dụng cho đơn hàng mới!'
            });
            return;
        }
        Swal.fire({
            title: 'Chỉnh Sửa Đơn Hàng',
            html: `
                <div class="edit-form">
                    <label>Người Đặt:</label>
                    <input type="text" id="edit-customer" value="${appointment.customer}">
                    <label>Số Điện Thoại:</label>
                    <input type="text" id="edit-phone" value="${appointment.phone}">
                    <label>Email:</label>
                    <input type="email" id="edit-email" value="${appointment.email}">
                    <label>Ngày Đặt:</label>
                    <input type="date" id="edit-date" value="${appointment.date}">
                    <label>Dịch Vụ:</label>
                    <select id="edit-service">
                        ${services.map(s => `<option value="${s}" ${s === appointment.service ? 'selected' : ''}>${s}</option>`).join('')}
                    </select>
                    <label>Chuyên Gia:</label>
                    <select id="edit-specialist">
                        ${specialists.map(s => `<option value="${s}" ${s === appointment.specialist ? 'selected' : ''}>${s}</option>`).join('')}
                    </select>
                    <label>Thời Gian:</label>
                    <input type="time" id="edit-time" value="${appointment.time}">
                </div>
            `,
            showCancelButton: true,
            confirmButtonColor: '#ff4d94',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Lưu',
            cancelButtonText: 'Thoát',
            preConfirm: () => {
                const customer = $('#edit-customer').val();
                const phone = $('#edit-phone').val();
                const email = $('#edit-email').val();
                const date = $('#edit-date').val();
                const service = $('#edit-service').val();
                const specialist = $('#edit-specialist').val();
                const time = $('#edit-time').val();

                if (!customer || !phone || !email || !date || !service || !specialist || !time) {
                    Swal.showValidationMessage('Vui lòng điền đầy đủ thông tin!');
                    return false;
                }
                return { customer, phone, email, date, service, specialist, time };
            }
        }).then((result) => {
            if (result.isConfirmed) {
                appointment.customer = result.value.customer;
                appointment.phone = result.value.phone;
                appointment.email = result.value.email;
                appointment.date = result.value.date;
                appointment.service = result.value.service;
                appointment.specialist = result.value.specialist;
                appointment.time = result.value.time;
                updateUI();
                Swal.fire(
                    'Thành Công!',
                    'Thông tin đơn hàng đã được cập nhật.',
                    'success'
                );
            }
        });
    });

    // Cancel button logic
    $('.cancel-btn').click(function(e) {
        e.preventDefault();
        if (appointment.status !== 0) {
            Swal.fire({
                icon: 'error',
                title: 'Không khả dụng',
                text: 'Hủy đơn chỉ khả dụng cho đơn hàng mới!'
            });
            return;
        }
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
                appointment.status = 2; // Update to Đã Hủy
                updateUI();
                Swal.fire(
                    'Đã Hủy!',
                    'Đơn hàng của bạn đã được hủy.',
                    'success'
                );
            }
        });
    });

    // Feedback submission logic
    $('.submit-feedback').click(function(e) {
        e.preventDefault();
        if (appointment.status !== 1) {
            Swal.fire({
                icon: 'error',
                title: 'Không khả dụng',
                text: 'Feedback chỉ có thể gửi sau khi đơn hàng hoàn thành!'
            });
            return;
        }

        const feedback = $('.feedback-textarea').val();
        const rating = $('.star-rating i.active').length;

        if (rating === 0 || !feedback) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Vui lòng đánh giá số sao và nhập feedback!'
            });
            return;
        }

        Swal.fire(
            'Thành Công!',
            'Feedback của bạn đã được gửi.',
            'success'
        );
        $('.feedback-textarea').val(''); // Clear textarea
        $('.star-rating i').removeClass('active'); // Reset stars
    });

    // Initial UI update
    updateUI();
});