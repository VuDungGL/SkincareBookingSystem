$(document).ready(function(){
    onRenderData.onInit();
});

const onRenderData = {
    onInit: function(){
        this.onLoadData();
        this.onLoadInputImage();
    },
    onLoadData: function (){
        $.ajax({
            url: "/admin/services/getAll",
            method: "GET",
            success: function (response){
                if (response){
                    var service = response.data;
                    let vipCustomerContent = $(".swiper-wrapper");
                    vipCustomerContent.empty();
                    service.forEach((data, index )=>{
                        var content = `
                        <div class="swiper-slide">
                            <div class="w-100">
                                <div class="card" style="flex-direction: row; width: 100%; box-shadow: none;">
                                    <div class="card-content">
                                        <h2 style="font-weight: 600">${data.serviceName}</h2>
                                        <p>${data.description}</p>
                                        <div class="buttons">
                                            <button class="btn-primary" onclick="onRenderData.onEditService(${data.id})">Chỉnh sửa</button>
                                            <button class="btn-secondary" onclick="onRenderData.onDeleteService(${data.id})">Xóa</button>
                                        </div>
                                        <div class="logos d-flex flex-column align-items-md-start">
                                            <span>Giá: ${data.price.toLocaleString('vi-VN')}VNĐ</span>
                                            <span>Thời gian: ${data.duration}'</span>
                                        </div>
                                    </div>
                                    <div class="card-image" style="background-image: url('/${data.illustration}');">
                                    </div>
                                </div>
                            </div>
                        </div>
                        `;
                        vipCustomerContent.append(content);
                    })

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
    onDeleteService: function(serviceID){
        Swal.fire({
            title: "Xác nhận xóa?",
            text: "Bạn có chắc chắn muốn xóa dịch vụ này không?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Xóa",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/admin/services/deleteService",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({ serviceID: serviceID }),
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
    onLoadInputImage: function(){
        $('#imageInput').on('change', function () {
            const file = this.files[0];
            if (file && file.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    $('#imagePreview').css({
                        'background-image': `url('${e.target.result}')`,
                        'filter': 'brightness(1)'
                    });
                };
                reader.readAsDataURL(file);
            }
        });

    },
    onCreateService: function () {
        const serviceName = $("#serviceName").val().trim();
        const price = parseFloat($("#price").val());
        const duration = parseInt($("#duration").val());
        const description = $("#description").val().trim();
        const imageFile = $('#imageInput')[0].files[0];

        // Validate
        const vietnameseNamePattern = /^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸỳỵỷỹ\s]+$/; // Kiểm tra ký tự đặc biệt
        if (!serviceName) {
            Swal.fire("Lỗi", "Tên dịch vụ không được để trống", "error");
            return;
        }
        if (vietnameseNamePattern.test(serviceName)) {
            Swal.fire("Lỗi", "Tên dịch vụ không được chứa ký tự đặc biệt", "error");
            return;
        }
        if (isNaN(price) || price <= 0) {
            Swal.fire("Lỗi", "Giá phải là số và lớn hơn 0", "error");
            return;
        }
        if (isNaN(duration) || duration <= 0) {
            Swal.fire("Lỗi", "Thời gian thực hiện phải là số và lớn hơn 0", "error");
            return;
        }

        // Validate description (đảm bảo có độ dài từ 35 đến 450 ký tự)
        if (!description || description.length < 35 || description.length > 450) {
            Swal.fire("Lỗi", "Mô tả phải có độ dài từ 35 đến 450 ký tự", "error");
            return;
        }
        if (/^\s|\s$/.test(description)) {
            Swal.fire("Lỗi", "Mô tả không được bắt đầu hoặc kết thúc bằng khoảng trắng", "error");
            return;
        }

        // Validate image file (kiểm tra nếu có file và kiểu là ảnh)
        if (!imageFile) {
            Swal.fire("Lỗi", "Vui lòng chọn một hình ảnh", "error");
            return;
        }
        if (!imageFile.type.startsWith("image/")) {
            Swal.fire("Lỗi", "Vui lòng chọn một hình ảnh hợp lệ", "error");
            return;
        }

        Swal.fire({
            title: "Xác nhận tạo mới?",
            text: "Bạn có chắc muốn tạo mới dịch vụ này không?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Tạo",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                const formData = new FormData();
                formData.append("serviceName", serviceName);
                formData.append("price", price);
                formData.append("duration", duration);
                formData.append("description", description);
                formData.append("image", imageFile);

                $.ajax({
                    url: "/admin/services/createOrUpdateService",
                    method: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    dataType: "json",
                    success: function (response) {
                        Swal.fire("Thành công", "Dịch vụ đã được tạo", "success").then(() => {
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
    onGetServiceByID: function(serviceID){
        return $.ajax({
            url: "/admin/services/getByID",
            method: "POST",
            data: JSON.stringify({ serviceID: serviceID }),
            contentType: "application/json",
            dataType: "json"
        });
    },

    updateService: function(data) {
        return $.ajax({
            url: "/admin/services/createOrUpdateService",
            method: "POST",
            data: data,
            contentType: false,
            processData: false,
            dataType: "json"
        });
    },
    onEditService: function (serviceID) {
        onRenderData.onGetServiceByID(serviceID).done(function (service) {
            Swal.fire({
                title: 'Chỉnh sửa dịch vụ',
                width: '900px',
                background: '#e0f7fa',
                html: `
                <div class="modal-content" style="max-width: 600px !important;">
                    <div class="avatar-container">
                        <input id="imgUpload" type="file" class="hidden-input">
                        <img id="imgPreview" src="/${service.data.illustration || 'default.jpg'}">
                    </div>

                    <div class="form-group">
                        <label>Tên dịch vụ:</label>
                        <input id="serviceName" value="${service.data.serviceName || ''}">
                    </div>
                    <div class="form-group">
                        <label>Giá:</label>
                        <input id="price" value="${service.data.price.toLocaleString('vi-VN')|| ''}">
                    </div>
                    <div class="form-group">
                        <label>Thời lượng (phút):</label>
                        <input id="duration" value="${service.data.duration || ''}">
                    </div>
                    <div class="form-group">
                        <label>Mô tả:</label>
                        <textarea id="description">${service.data.description || ''}</textarea>
                    </div>              
                </div>
                <img src="/assets/images/base/admin/create-icon-2.png" class="image-2" style="height:300px; z-index: -1">
                <img src="/assets/images/base/admin/image-1.png" class="image-1" style="height:123px;">
            `,
                showCancelButton: true,
                confirmButtonText: 'Lưu',
                preConfirm: () => {
                    const serviceName = $('#serviceName').val().trim();
                    const priceRaw = $('#price').val().trim();
                    const price = priceRaw.replace(/\./g, '');
                    const duration = $('#duration').val().trim();
                    const description = $('#description').val().trim();

                    const vietnameseNamePattern = /^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸỳỵỷỹ\s]+$/;
                    const numberPattern = /^[0-9]+$/;

                    if (!serviceName || !namePattern.test(serviceName)) {
                        Swal.fire('Lỗi!', 'Tên dịch vụ không được để trống và chỉ chứa chữ cái!', 'error');
                        return false;
                    }

                    if (!numberPattern.test(price)) {
                        Swal.fire('Lỗi!', 'Giá chỉ được nhập số!', 'error');
                        return false;
                    }

                    if (!numberPattern.test(duration)) {
                        Swal.fire('Lỗi!', 'Thời lượng chỉ được nhập số!', 'error');
                        return false;
                    }

                    if (!description || description.length < 30 || description.length > 450) {
                        Swal.fire('Lỗi!', 'Mô tả phải có ít nhất 10 ký tự và tối đa 450 kí tự!', 'error');
                        return false;
                    }

                    const formData = new FormData();
                    formData.append("serviceName", serviceName);
                    formData.append("price", price);
                    formData.append("duration", duration);
                    formData.append("description", description);
                    formData.append("id", serviceID); // cần để update
                    const file = $('#imgUpload')[0].files[0];
                    if (file) formData.append("image", file);

                    return onRenderData.updateService(formData);
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire('Thành công!', 'Dịch vụ đã được cập nhật.', 'success').then(() => {
                        location.reload();
                    });
                }
            });

            $('#imgPreview').on('click', function () {
                $('#imgUpload').click();
            });

            $('#imgUpload').on('change', function (event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = (e) => $('#imgPreview').attr('src', e.target.result);
                    reader.readAsDataURL(file);
                }
            });
        });
    }


}