$(document).ready(function(){
    renderDataStaff.renderInit();
});

const renderDataStaff = {
    renderInit : function(){
        this.renderAllStaffTable();
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

        let paginationHtml = currentPage === 0
            ? `<i class="fa-solid fa-angle-left opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-left" style="cursor: pointer" onclick="renderDataStaff.renderAllStaffTable(6, ${Math.max(0, currentPage - 1)})"></i>`;


        if (totalPages <= 5) {
            // Nếu totalPages <= 5, hiển thị tất cả các trang
            for (let i = 0; i < totalPages; i++) {
                paginationHtml += `<span class="${currentPage === i ? 'page-active' : ''}" 
                            onclick="renderDataStaff.renderAllStaffTable(6, ${i})">${i + 1}</span>`;
            }
        } else {
            // Nếu currentPage gần đầu (0,1,2)
            if (currentPage <= 2) {
                for (let i = 0; i < 4; i++) {
                    paginationHtml += `<span class="${currentPage === i ? 'page-active' : ''}" 
                                onclick="renderDataStaff.renderAllStaffTable(6, ${i})">${i + 1}</span>`;
                }
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${totalPages - 1})">${totalPages}</span>`;
            }
            // Nếu currentPage gần cuối
            else if (currentPage >= totalPages - 3) {
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, 0)">1</span>`;
                paginationHtml += `<span>...</span>`;
                for (let i = totalPages - 4; i < totalPages; i++) {
                    paginationHtml += `<span class="${currentPage === i ? 'page-active' : ''}" 
                                onclick="renderDataStaff.renderAllStaffTable(6, ${i})">${i + 1}</span>`;
                }
            }
            // Nếu currentPage ở giữa
            else {
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, 0)">1</span>`;
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage - 1})">${currentPage}</span>`;
                paginationHtml += `<span class="page-active">${currentPage + 1}</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${currentPage + 1})">${currentPage + 2}</span>`;
                paginationHtml += `<span>...</span>`;
                paginationHtml += `<span onclick="renderDataStaff.renderAllStaffTable(6, ${totalPages - 1})">${totalPages}</span>`;
            }
        }

        paginationHtml += currentPage === totalPages - 1
            ? `<i class="fa-solid fa-angle-right opacity-50" style="cursor: not-allowed; pointer-events: none;"></i>`
            : `<i class="fa-solid fa-angle-right" style="cursor: pointer" onclick="renderDataStaff.renderAllStaffTable(6, ${Math.min(totalPages - 1, currentPage + 1)})"></i>`;


        paginationContainer.html(paginationHtml);
    }
}