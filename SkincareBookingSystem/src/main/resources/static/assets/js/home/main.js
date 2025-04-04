(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner(0);
    
    
    // Initiate the wowjs
    new WOW().init();


    // Fixed Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.sticky-top').addClass('shadow-sm').css('top', '0px');
        } else {
            $('.sticky-top').removeClass('shadow-sm').css('top', '-200px');
        }
    });
    
    
   // Back to top button
   $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
        $('.back-to-top').fadeIn('slow');
    } else {
        $('.back-to-top').fadeOut('slow');
    }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Pricing-carousel
    $(".pricing-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:2
            },
            768:{
                items:2
            },
            992:{
                items:3
            },
            1200:{
                items:4
            }
        }
    });

    // Testimonial-carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:1
            },
            992:{
                items:2
            },
            1200:{
                items:2
            }
        }
    });

    $(document).ready(function() {
        // Lấy path hiện tại (bỏ query string và hash nếu có)
        const currentPath = window.location.pathname.split('?')[0].split('#')[0];

        // Xử lý active cho main menu items
        $('.navbar-nav a.nav-link').each(function() {
            const $this = $(this);
            const linkHref = $this.attr('href');

            // Kiểm tra nếu là dropdown toggle thì bỏ qua
            if ($this.hasClass('dropdown-toggle')) {
                return true; // continue loop
            }

            if (linkHref && currentPath.includes(linkHref)) {
                $this.addClass('active');

                // Nếu item này thuộc dropdown, active luôn dropdown cha
                const $dropdown = $this.closest('.dropdown-menu');
                if ($dropdown.length) {
                    $dropdown.prev('.dropdown-toggle').addClass('active');
                    $dropdown.closest('.dropdown').addClass('active');
                }
            }
        });

        // Xử lý riêng cho dropdown items
        $('.dropdown-menu a.dropdown-item').each(function() {
            const $this = $(this);
            const linkHref = $this.attr('href');

            if (linkHref && currentPath.includes(linkHref)) {
                $this.addClass('active');

                // Active dropdown cha
                $this.closest('.dropdown-menu')
                    .prev('.dropdown-toggle')
                    .addClass('active');

                $this.closest('.dropdown')
                    .addClass('active');
            }
        });
    });


    // Modal Video
    $(document).ready(function () {
        var $videoSrc;
        $('.btn-play').click(function () {
            $videoSrc = $(this).data("src");
        });
        console.log($videoSrc);

        $('#videoModal').on('shown.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })

        $('#videoModal').on('hide.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc);
        })
    });


    // Facts counter
    $('[data-toggle="counter-up"]').counterUp({
        delay: 5,
        time: 2000
    });


})(jQuery);

