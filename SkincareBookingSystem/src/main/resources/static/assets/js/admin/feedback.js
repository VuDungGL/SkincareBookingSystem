$(document).ready(function(){
    onRenderDataFeedback.onInit();
})

const onRenderDataFeedback = {
    onInit: function(){
        this.onRenderGoodFeedback();
        this.onLoadFeedbackTable();
    },
    onRenderGoodFeedback: function () {
        $.ajax({
            url: "/admin/feedback/getListGoodFeedback",
            method: 'GET',
            success: function (response) {
                if (response.data && response.data.length > 0) {
                    const wrapper = document.getElementById("good-feedback-wrapper");
                    if (!wrapper) return;
                    wrapper.innerHTML = "";

                    for (let i = 0; i < response.data.length; i += 2) {
                        const fb1 = response.data[i];
                        const fb2 = response.data[i + 1];

                        const card1 = fb1 ? `
                        <div class="feedback-card">
                            <div class="feedback-header">
                                <img src="/${fb1.avt}" alt="User">
                                <div class="feedback-info">
                                    <div class="feedback-name">${fb1.fullName}</div>
                                    <div class="feedback-stars">${coreConst.onFormatStartRating(fb1.rating)}</div>
                                    <div class="feedback-type">Dịch vụ: ${fb1.serviceName}</div>
                                </div>
                            </div>
                            <div class="feedback-message">${fb1.comment}</div>
                        </div>
                    ` : "";

                        const card2 = fb2 ? `
                        <div class="feedback-card">
                            <div class="feedback-header">
                                <img src="/${fb2.avt}" alt="User">
                                <div class="feedback-info">
                                    <div class="feedback-name">${fb2.fullName}</div>
                                    <div class="feedback-stars">${coreConst.onFormatStartRating(fb2.rating)}</div>
                                    <div class="feedback-type">Dịch vụ: ${fb2.serviceName}</div>
                                </div>
                            </div>
                            <div class="feedback-message">${fb2.comment}</div>
                        </div>
                    ` : "";

                        const slide = document.createElement("div");
                        slide.className = "swiper-slide";
                        slide.innerHTML = `
                        <div class="feedback-slide-container d-flex">
                            ${card1}
                            ${card2}
                        </div>
                    `;
                        wrapper.appendChild(slide);
                    }

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
            },

        });
    },
     onLoadFeedbackTable: function(pageSize = 4, pageIndex = 0){
        var search = $(`#search-input`).val();
        $.ajax({
            url: "/admin/feedback/getAllPageFeedback",
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                pageIndex: pageIndex,
                pageSize: pageSize,
                searchText: search
            }),
            success: function(response){
                if(response){
                    let container = $("#feedback-list");

                    container.empty();

                    response.content.forEach((feedback, index) => {
                        var ratingStart = coreConst.onFormatStartRating(feedback.rating);
                        var createDate = coreConst.formatDate2(feedback.createDate);
                        var memberCard = `
                            <li class="feedback-item">
                                <div class="feedback-header">
                                    <strong>👤 ${feedback.fullName}</strong>
                                    <span class="feedback-stars">${ratingStart}</span>
                                    <em>${createDate}</em>
                                </div>
                                <div class="feedback-content">
                                    ${feedback.comment}
                                </div>
                                <div class="feedback-meta">
                                   Dịch vụ chăm sóc da: ${feedback.serviceName}
                                </div>
                            </li>
                        `;
                        container.append(memberCard);
                    })
                    $('#page-staff-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    onRenderDataFeedback.updatePagination(response.totalPages, response.number, "onRenderDataFeedback.onLoadFeedbackTable");
                }
            }
        })
     },
    updatePagination: function(totalPages, currentPage, functionActive) {
        let paginationContainer = $("#pagination-page");
        paginationContainer.empty();
        let paginationHtml = '';

        let displayPage = currentPage + 1;
        let lastPage = totalPages;

        paginationHtml += currentPage === 0
            ? `<i class="fa-solid fa-angle-left opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-left" style="cursor: pointer" onclick="(${functionActive})(4, ${currentPage - 1})"></i>`;

        if (totalPages <= 5) {
            for (let i = 0; i < totalPages; i++) {
                paginationHtml += `<span onclick="(${functionActive})(4, ${i})" 
                                class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
            }
        } else {
            if (currentPage <= 1) {
                for (let i = 0; i <= 2; i++) {
                    paginationHtml += `<span onclick="(${functionActive})(6, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="(${functionActive})(4, ${totalPages - 1})">${totalPages}</span>`;
            } else if (currentPage >= 2 && currentPage <= totalPages - 4) {
                paginationHtml += `<span onclick="(${functionActive})(4, ${currentPage - 1})">${displayPage - 1}</span>`;
                paginationHtml += `<span onclick="(${functionActive})(4, ${currentPage})" class="page-active">${displayPage}</span>`;
                paginationHtml += `<span onclick="(${functionActive})(4, ${currentPage + 1})">${displayPage + 1}</span>`;
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="(${functionActive})(4, ${totalPages - 1})">${totalPages}</span>`;
            } else {
                paginationHtml += `<span>...</span>`;
                for (let i = totalPages - 4; i < totalPages; i++) {
                    paginationHtml += `<span onclick="(${functionActive})(4, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
            }
        }

        paginationHtml += currentPage === totalPages - 1
            ? `<i class="fa-solid fa-angle-right opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-right" style="cursor: pointer" onclick="(${functionActive})(4, ${currentPage + 1})"></i>`;

        paginationContainer.html(paginationHtml);
    },

}