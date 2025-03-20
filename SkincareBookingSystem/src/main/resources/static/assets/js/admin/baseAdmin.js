$(document).ready(function(){
    onAddLeftBarActive.onInit();
    baseCore.baseInit();
});

const baseCore={
    baseInit: function(){

    },

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