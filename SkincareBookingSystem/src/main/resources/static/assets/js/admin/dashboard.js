$(document).ready(function(){
    renderData.renderInit();
});

const renderData= {
    renderInit: function(){
        this.onLoadTotalMember();
        this.onLoadTotalBookingSuccess();
        this.onLoadFeedback();
        this.onLoadTotalBookingCancel();
        this.onLoadBestSaler();
        this.onLoadRevenue();
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
};