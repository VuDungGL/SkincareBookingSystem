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
                                            <button class="btn-primary">Chỉnh sửa</button>
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
                    type: "POST",
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
                        'filter': 'brightness(1)' // hiện ảnh rõ lên khi upload
                    });
                };
                reader.readAsDataURL(file);
            }
        });
    }
}