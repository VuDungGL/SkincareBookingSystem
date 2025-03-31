$(document).ready(function(){
    onAddLeftBarActive.onInit();
    baseCore.baseInit();
});

const baseCore={
    baseInit: function(){

    },
    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString("en-GB", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        });
    },
     showLoading:function() {
         $('.loading-overlay').addClass("loading-active").fadeIn();
         $('body').append('<div class="loading-overlay"><div class="loader"></div></div>');
    },
     hideLoading:function() {
         $('.loading-overlay').removeClass("loading-active").fadeOut();
    }
}

const onAddLeftBarActive = {
    onInit: function() {
        this.onLoadLeftBar();
    },

    onGetURL: function () {
        const pathname = window.location.pathname;
        const pathSegments = pathname.split('/').filter(segment => segment.length > 0);
        return pathSegments[pathSegments.length - 1];
    },

    onLoadLeftBar: function () {
        var key = onAddLeftBarActive.onGetURL();
        if (key === 'admin') {
            document.getElementById(`nav-users`).classList.add("active");
        } else {
            var element = document.getElementById(`nav-${key}`);
            if (element) {
                element.classList.add("active");
            }
        }
    },
}