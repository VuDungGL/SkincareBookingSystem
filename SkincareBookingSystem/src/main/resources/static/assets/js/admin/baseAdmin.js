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
    formatDate2:function(isoString) {
        if (!isoString) return '';
        const date = new Date(isoString);
        return date.toISOString().split('T')[0];
    },
     showLoading:function() {
         $('.loading-overlay').addClass("loading-active").fadeIn();
         $('body').append('<div class="loading-overlay"><div class="loader"></div></div>');
    },
     hideLoading:function() {
         $('.loading-overlay').removeClass("loading-active").fadeOut();
    },
    onFormatOffSetDateTime: function () {
        const inputDate = document.getElementById("birthDate").value; // e.g., "2004-12-28"

        if (inputDate) {
            const date = new Date(inputDate);

            const timezoneOffset = -date.getTimezoneOffset(); // phút
            const offsetHours = Math.floor(timezoneOffset / 60);
            const offsetMinutes = timezoneOffset % 60;

            const offsetStr = `${offsetHours >= 0 ? '+' : '-'}${String(Math.abs(offsetHours)).padStart(2, '0')}:${String(Math.abs(offsetMinutes)).padStart(2, '0')}`;

            const isoStringWithOffset = `${inputDate}T00:00:00${offsetStr}`;

            return isoStringWithOffset;
        }

        return null;
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