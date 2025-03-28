$(document).ready(function(){
    renderDataStaff.renderInit();
});

const renderDataStaff = {
    renderInit : function(){
        this.renderAllStaffTable();
        this.renderDepartmentInfo();
    },
    renderAllStaffTable : function(pageSize=6, pageIndex=0){
        var search = $('#search-input')[0].value;
        $.ajax({
            url: "/users/getListStaff",
            method: 'POST',
            data: JSON.stringify({
                pageSize: pageSize,
                pageIndex: pageIndex,
                searchText: search
            }),
            contentType: "application/json",
            success: function(response){
                if(response != null){
                    let container = $("#table-content");

                    container.empty();

                    response.content.forEach((user, index) => {
                        let birth = baseCore.formatDate(user.birthDay);
                        let genderText = "Khác";

                        if (user.gender === 1) {
                            genderText = "Nam";
                        } else if (user.gender === 2) {
                            genderText = "Nữ";
                        }
                        var memberCard = `
                            <tr>
                                <td class="data column-id">#SBS0${user.userID}</td>
                                <td class="data column-name"><img src="/${user.avt}" alt="User Image">${user.firstName} ${user.lastName}</td>
                                <td class="data column-birth">${birth}</td>
                                <td class="data column-email">${user.email}</td>
                                <td class="data column-phone">${user.phone}</td>
                                <td class="data column-gender">${genderText}</td>
                                <td class="data column-department">${user.department}</td>
                                <td class="data column-position">${user.position}</td>
                                <td class="data column-option"><i class="fa-solid fa-ellipsis-vertical"></i></td>
                            </tr>
                        `;
                        container.append(memberCard);
                    })
                    $('#page-staff-total').text(`page ${response.number + 1} of ${response.totalPages}`);
                    renderDataStaff.updatePagination(response.totalPages, response.number);
                }
            }
        });
    },

    updatePagination: function(totalPages, currentPage) {
        let paginationContainer = $("#pagination-page");
        paginationContainer.empty();
        let paginationHtml = '';

        let displayPage = currentPage + 1;
        let lastPage = totalPages;

        paginationHtml += currentPage === 0
            ? `<i class="fa-solid fa-angle-left opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-left" style="cursor: pointer" onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage - 1})"></i>`;

        if (totalPages <= 5) {
            for (let i = 0; i < totalPages; i++) {
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${i})" 
                                class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
            }
        } else {
            if (currentPage <= 1) {
                for (let i = 0; i <= 2; i++) {
                    paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${totalPages - 1})">${totalPages}</span>`;
            } else if (currentPage >= 2 && currentPage <= totalPages - 4) {
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage - 1})">${displayPage - 1}</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage})" class="page-active">${displayPage}</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage + 1})">${displayPage + 1}</span>`;
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${totalPages - 1})">${totalPages}</span>`;
            } else {
                paginationHtml += `<span>...</span>`;
                for (let i = totalPages - 4; i < totalPages; i++) {
                    paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${i})" 
                                    class="${currentPage === i ? 'page-active' : ''}">${i + 1}</span>`;
                }
            }
        }

        paginationHtml += currentPage === totalPages - 1
            ? `<i class="fa-solid fa-angle-right opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-right" style="cursor: pointer" onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage + 1})"></i>`;

        paginationContainer.html(paginationHtml);
    },




    renderDepartmentInfo: function(){
        $.ajax({
            url: "/users/getListDepartment",
            method: 'GET',
            success: function (response){
                if(response){
                    let departmentContent = $("#department-content");
                    departmentContent.empty();

                    response.forEach((department, index) => {
                        var maxVisible = 4;
                        var visibleAvatars = department.memberAvatars.slice(0, maxVisible);
                        var remainingCount = department.memberAvatars.length - maxVisible;

                        var memberAvatarsHTML = visibleAvatars.map(avatar => `<img src="/${avatar}" alt="Avatar">`).join('');

                        // Nếu có nhiều hơn 3 thành viên, hiển thị +X
                        if (remainingCount > 0) {
                            memberAvatarsHTML += `<div class="avatar more">+${remainingCount}</div>`;
                        }

                        var memberCard = `
                        <li class="splide__slide">
                            <div class="card">
                                <div style="background-image: url('${department.icon}'); width: 100%; height: 200px; background-repeat: no-repeat; background-size: cover; background-position: center center;"></div>
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h5 class="card-title">${department.department}</h5>
                                            <p class="text-muted">Manager: ${department.managerFirstName} ${department.managerLastName}</p>
                                            <p class="fw-bold mb-1">Staff</p>
                                        </div>
                                        <div class="add-member d-flex align-items-center" style="padding-right: 6px">
                                            <i class="fa-solid fa-plus" title="Thêm thành viên"></i>
                                        </div>
                                    </div>
                                    <div class="progress mb-2" style="height: 6px;">
                                        <div class="progress-bar" role="progressbar" style="width: 100%;" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100"></div>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <span><i class="bi bi-clock"></i> ${department.totalMember} Member</span>
                                        <div class="avatars d-flex">
                                            ${memberAvatarsHTML}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    `;
                        departmentContent.append(memberCard);
                    })
                    renderDataStaff.onLoadSlideDepartment();
                }
            }
        })
    },

    onLoadSlideDepartment: function(){
        var splide = new Splide('#department-carousel', {
            type       : 'loop',
            perPage    : 1,
            gap        : '20px',
            autoplay   : false,
            interval   : 3000,
            pagination : false,
            breakpoints: {
                768: { perPage: 1 },
                1024: { perPage: 1 }
            },
        }).mount();

        // Điều khiển thủ công bằng nút bấm
        document.getElementById('prevBtn').addEventListener('click', function () {
            splide.go('-1');
        });

        document.getElementById('nextBtn').addEventListener('click', function () {
            splide.go('+1');
        });
    }
}