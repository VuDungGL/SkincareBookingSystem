$(document).ready(function(){
    renderData.renderInit();
    onSerchYear.onInit();
    onLoadDataChart.onInit();
});

const renderData= {
    renderInit: async function(){
        baseCore.showLoading() // Hiển thị hiệu ứng loading trước khi gọi API

        try {
            await Promise.all([
                this.onLoadTotalMember(),
                this.onLoadTotalBookingSuccess(),
                this.onLoadFeedback(),
                this.onLoadTotalBookingCancel(),
                this.onLoadBestSaler(),
                this.onLoadRevenue(),
                this.onLoadTopWorkingHard(),
                this.onLoadWorkingTable(4, 1)
            ]);
        } catch (error) {
            console.error("Lỗi khi tải dữ liệu:", error);
        } finally {
            baseCore.hideLoading()
        }
    },

    onLoadTotalMember: function(){
        $.ajax({
            url: "/users/TotalMember",
            method: 'GET',
            success: function(response){
                if(response != null){
                    $("#total-member").text(response);
                }
            }
        });
    },

    onLoadTotalBookingSuccess: function(){
        $.ajax({
            url: "/booking/GetTotal",
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                status: 1
            }),
            success: function(response){
                if(response != null){
                    $("#total-booking").text(response);
                }
            }
        });
    },

    onLoadFeedback: function(){
        $.ajax({
            url: "/booking/GetRatingFeedback",
            method: 'GET',
            success: function(response){
                if(response != null){
                    $("#rating-feedback").text(response.rating+ "/5");
                    $("#total-feedback").text(response.totalRating);
                }
            }
        });
    },

    onLoadTotalBookingCancel: function(){
        $.ajax({
            url: "/booking/GetTotal",
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                status: 2
            }),
            success: function(response){
                if(response != null){
                    $("#total-booking-cancel").text(response);
                }
            }
        });
    },

    onLoadBestSaler: function(){
        $.ajax({
            url: "/booking/BestSaler",
            method: 'GET',
            success: function(response){
                if(response != null){
                    $("#best-saler").text(response.serviceName);
                }
            }
        });
    },

    onLoadRevenue: function(){
        $.ajax({
            url: "/booking/getRevenue",
            method: 'GET',
            success: function(response){
                if(response != null){
                    $("#revenue-title").text("Revenue in " + response.month + " " + response.year);
                    $("#revenue-content").text(response.revenue);
                }
            }
        });
    },

    onLoadTopWorkingHard: function(){
        $.ajax({
            url: "/booking/getHardWorking",
            method: 'POST',
            data: JSON.stringify({
                pageSize: 3,
                pageIndex: 1
            }),
            contentType: "application/json",
            success: function(response){
                if(response != null){
                    let container = $("#topWorkingContainer");

                    container.empty();

                    response.forEach((user, index) => {
                        var memberCard = `
                        <div class="member-card col-12 d-flex align-items-center">
                            <img src="/${user.avt}" alt="${user.firstName}">
                            <div class="member-info">
                                <div class="member-name">${user.firstName} ${user.lastName}</div>
                                <div class="member-role">${user.email}</div>
                            </div>
                            ${index === 0 ? '<img src="/assets/images/base/admin/_1st.png" alt="logo" class="options"/>' : ''}
                        </div>`;
                        container.append(memberCard);
                    })
                }
            }
        });
    },

    onLoadWorkingTable: function(pageSize, pageIndex){
        $.ajax({
            url: "/booking/getHardWorking",
            method: 'POST',
            data: JSON.stringify({
                pageSize: pageSize,
                pageIndex: pageIndex
            }),
            contentType: "application/json",
            success: function(response){
                if(response != null){
                    let container = $("#table-content");

                    container.empty();

                    response.forEach((user, index) => {
                        var memberCard = `
                        <tr>
                            <td class="column-id">#SBS0${user.skinTherapistID}</td>
                            <td class="column-name">${user.firstName} ${user.lastName}</td>
                            <td class="column-birth">${user.email}</td>
                            <td class="column-phone">${user.phone}</td>
                            <td class="column-total-task">${user.totalTask}</td>
                            <td class="column-total-earned">
                                <div class="progress-bar" title=""><div style="width: ${user.percentTask}%;"></div></div>${user.percentTask}%
                            </td>
                            <td class="column-total-earned">$${user.total}</td>
                        </tr>`;
                        container.append(memberCard);
                    })
                }
            }
        });
    },

    onLoadPageIndex: function(pageSize, pageIndex){
        renderData.onLoadWorkingTable(pageSize, pageIndex);
        $("span[id^='page-']").removeClass("page-active");
        $("#page-" + pageIndex).addClass("page-active");
    }

};

const onLoadDataChart = {
    onInit: function(){
        this.onGetDataSaleYear();
        this.onLoadColumnChart();
    },

    onGetDataSaleYear: function() {
        var year = $('#dateInput').val();
        $.ajax({
            url: "/booking/annualSale",
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ year: year }),
            success: function (response) {
                if (response && Array.isArray(response)) {
                    onLoadDataChart.onGetTotalNewMemberYear(response);
                    var totalSale =0;
                    response.forEach(item => {
                        if (item.total) {
                            totalSale += item.total;
                        }
                    });
                    $('#total-sale').text('$' + totalSale + '.0');
                }
            }
        });
    },

    onGetTotalNewMemberYear: function(salesData) {
        var year = $('#dateInput').val();
        $.ajax({
            url: "/users/annualNewMember",
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ year: year }),
            success: function (response) {
                if (response && Array.isArray(response)) {
                    onLoadDataChart.onCreateChart(salesData, response);
                }
            }
        });
    },

    onCreateChart: function(salesData, newMemberData) {
        // Kiểm tra dữ liệu đầu vào có phải mảng không
        if (!Array.isArray(salesData) || salesData.length !== 12) {
            console.error("salesData không hợp lệ", salesData);
            salesData = Array(12).fill({ total: 0 });
        }

        if (!Array.isArray(newMemberData) || newMemberData.length !== 12) {
            console.error("newMemberData không hợp lệ", newMemberData);
            newMemberData = Array(12).fill({ total: 0 });
        }

        google.charts.load('current', { 'packages': ['corechart'] });
        google.charts.setOnLoadCallback(function() {
            drawChart(salesData, newMemberData);
        });

        function drawChart(salesData, newMemberData) {
            var chartData = [['Month', 'Sales', 'NewMember']];

            for (var i = 0; i < 12; i++) {
                var salesTotal = salesData[i] ? salesData[i].total : 0;
                var newMemberTotal = newMemberData[i] ? newMemberData[i].total : 0;
                chartData.push([(i + 1).toString(), salesTotal, newMemberTotal]);
            }

            var data = google.visualization.arrayToDataTable(chartData);

            var options = {
                curveType: 'function',
                height: 250,
                width: 400,
                chartArea: {
                    left: 0,
                    top: 0,
                    width: '100%',
                    height: '85%'
                },
                legend: {
                    position: 'bottom',
                    alignment: 'center'
                },
                hAxis: {
                    textStyle: {
                        fontSize: 12,
                        bold: true
                    },
                    gridlines: { color: 'transparent' }
                },
                vAxis: {
                    textStyle: {
                        fontSize: 12,
                        bold: true
                    },
                    gridlines: { color: 'transparent' },
                    baselineColor: 'transparent',
                    textPosition: 'none'
                },
                series: {
                    0: {
                        lineWidth: 4,
                        interpolateNulls: true,
                        color: '#4400ff',
                        strokeOpacity: 0.8
                    },
                    1: {
                        lineWidth: 4,
                        interpolateNulls: true,
                        color: 'red',
                        strokeOpacity: 0.8
                    }
                }
            };

            var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
            chart.draw(data, options);

        }
    },

    onLoadColumnChart:function (){
        google.charts.load("current", { packages: ["corechart"] });
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            let fetchPaid = fetch('/booking/revenueLast7day', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ isPaid: 1 })
            }).then(response => response.json());

            let fetchUnpaid = fetch('/booking/revenueLast7day', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ isPaid: 0 })
            }).then(response => response.json());

            Promise.all([fetchPaid, fetchUnpaid])
                .then(([paidData, unpaidData]) => {
                    let formattedData = [['Day', 'Paid', { role: 'style' }, 'UnPaid', { role: 'style' }]];

                    paidData.forEach((paid, index) => {
                        let unpaid = unpaidData[index] || { total: 0, day: paid.month };
                        formattedData.push([
                            paid.month.toString(),
                            unpaid.total, 'color: #6A0DAD; stroke-width: 2;',
                            paid.total, 'color: #40C4FF; opacity: 0.6'
                        ]);
                    });

                    var data = google.visualization.arrayToDataTable(formattedData);
                var options = {
                    title: 'Last Revenue',
                    titleTextStyle: {
                        color: '#4400ff',
                        fontSize: 18,
                        bold: true
                    },
                    legend: { position: 'none' },
                    isStacked: true,
                    chartArea: { width: '80%', height: '70%' },
                    bar: { groupWidth: "40%" },
                    hAxis: {
                        textStyle: {
                            fontSize: 12,
                            bold: true
                        }
                    },
                    vAxis: {
                        textStyle: {
                            fontSize: 12,
                        },
                        gridlines: { color: 'transparent' },
                    }
                };

                var chart = new google.visualization.ColumnChart(document.getElementById("chart_div"));
                chart.draw(data, options);
            }
        )}
    }
}

const onSerchYear =  {
    onInit: function(){
        this.onSetDefaultYear();
        this.onClickItem();
    },

    onSetDefaultYear: function(){
        const dateInput = document.getElementById("dateInput");
        if (dateInput) {
            dateInput.value = new Date().getFullYear();
        }
    },

    onClickItem: function() {
        const dateInput = document.getElementById("dateInput");
        const calendarIcon = document.getElementById("calendarIcon");
        const yearPicker = document.getElementById("yearPicker");
        const yearContainer = document.getElementById("yearContainer");

        if (!dateInput || !calendarIcon || !yearPicker || !yearContainer) return;

        yearContainer.innerHTML = "";

        for (let year = 2000; year <= 2099; year++) {
            const yearSpan = document.createElement("span");
            yearSpan.textContent = year;
            yearSpan.style.cursor = "pointer";
            yearSpan.style.margin = "5px";
            yearSpan.addEventListener("click", function () {
                dateInput.value = year;
                yearPicker.style.display = "none";

                onLoadDataChart.onGetDataSaleYear();
            });
            yearContainer.appendChild(yearSpan);
        }


        calendarIcon.addEventListener("click", function (event) {
            yearPicker.style.display = (yearPicker.style.display === "block") ? "none" : "block";
            event.stopPropagation();
        });

        document.addEventListener("click", function (event) {
            if (!calendarIcon.contains(event.target) && !yearPicker.contains(event.target)) {
                yearPicker.style.display = "none";
            }
        });
    }
}

