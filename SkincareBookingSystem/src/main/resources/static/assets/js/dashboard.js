$(document).ready(function(){
    renderData.renderInit();
});

const renderData= {
    renderInit: function(){
        this.onLoadTotalMember();
        this.onLoadTotalBookingSuccess();
        this.onLoadFeedback();
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
            method: 'GET',
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
    }
};