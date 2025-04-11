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


    // Danh sách câu hỏi trắc nghiệm
    const questions = [
        {
            question: "1. Bạn cảm thấy làn da của mình như thế nào sau khi rửa mặt?",
            options: [
                { text: "A. Khô, căng", score: 3 },
                { text: "B. Bình thường, mềm mại", score: 2 },
                { text: "C. Nhờn, bóng dầu", score: 1 },
                { text: "D. Chỉ nhờn ở vùng chữ T (trán, mũi, cằm)", score: 2 }
            ]
        },
        {
            question: "2. Lỗ chân lông của bạn trông như thế nào?",
            options: [
                { text: "A. To và dễ thấy", score: 1 },
                { text: "B. Nhỏ và khó nhận ra", score: 3 },
                { text: "C. Trung bình, không quá lớn hoặc quá nhỏ", score: 2 }
            ]
        },
        {
            question: "3. Bạn có thường xuyên bị mụn không?",
            options: [
                { text: "A. Thường xuyên", score: 1 },
                { text: "B. Thỉnh thoảng", score: 2 },
                { text: "C. Hiếm khi hoặc không bao giờ", score: 3 }
            ]
        },
        {
            question: "4. Bạn rửa mặt bao nhiêu lần mỗi ngày?",
            options: [
                { text: "A. Một lần hoặc ít hơn", score: 1 },
                { text: "B. Hai lần", score: 3 },
                { text: "C. Hơn hai lần", score: 2 }
            ]
        },
        {
            question: "5. Bạn có thường xuyên sử dụng kem chống nắng không?",
            options: [
                { text: "A. Hàng ngày", score: 3 },
                { text: "B. Chỉ khi ra ngoài trời nắng", score: 2 },
                { text: "C. Hiếm khi hoặc không bao giờ", score: 1 }
            ]
        },
        {
            question: "6. Bạn có sử dụng kem dưỡng ẩm không?",
            options: [
                { text: "A. Mỗi ngày", score: 3 },
                { text: "B. Đôi khi", score: 2 },
                { text: "C. Không sử dụng", score: 1 }
            ]
        },
        {
            question: "7. Bạn có gặp vấn đề về da nào sau đây không?",
            options: [
                { text: "A. Da khô, bong tróc", score: 3 },
                { text: "B. Da dầu, bóng nhờn", score: 1 },
                { text: "C. Nám, tàn nhang", score: 2 },
                { text: "D. Mụn trứng cá", score: 1 }
            ]
        },
        {
            question: "8. Bạn có thường xuyên cảm thấy da bị kích ứng, mẩn đỏ không?",
            options: [
                { text: "A. Có, rất thường xuyên", score: 1 },
                { text: "B. Đôi khi", score: 2 },
                { text: "C. Hiếm khi hoặc không bao giờ", score: 3 }
            ]
        }
    ];

    let score = 0;

// Tải câu hỏi trắc nghiệm
    function loadQuiz() {
        const quizDiv = document.getElementById('quiz');
        quizDiv.innerHTML = ''; // Xóa nội dung cũ
        questions.forEach((q, index) => {
            const questionDiv = document.createElement('div');
            questionDiv.classList.add('question');
            questionDiv.innerHTML = `<p>${q.question}</p>`;
            q.options.forEach((option, i) => {
                const optionDiv = document.createElement('div');
                optionDiv.classList.add('option');
                optionDiv.innerHTML = `<input type="radio" name="question${index}" value="${option.score}"> ${option.text}`;
                questionDiv.appendChild(optionDiv);
            });
            quizDiv.appendChild(questionDiv);
        });
    }

// Tính điểm và hiển thị kết quả
    function submitQuiz() {
        score = 0;
        questions.forEach((q, index) => {
            const selectedOption = document.querySelector(`input[name="question${index}"]:checked`);
            if (selectedOption) {
                score += parseInt(selectedOption.value);
            }
        });

        displayResult();
    }

// Hiển thị kết quả
    function displayResult() {
        const resultDiv = document.getElementById('result');
        const skinType = document.getElementById('skin-type');
        const recommendation = document.getElementById('recommendation');

        let skinTypeText = '';
        let recommendationText = '';

        if (score >= 8 && score <= 12) {
            skinTypeText = 'Da dầu';
            recommendationText = 'Đề xuất dịch vụ: Làm sạch da, trị mụn, kiểm soát dầu';
        } else if (score >= 13 && score <= 17) {
            skinTypeText = 'Da hỗn hợp';
            recommendationText = 'Đề xuất dịch vụ: Dưỡng ẩm, cân bằng dầu & nước';
        } else if (score >= 18 && score <= 24) {
            skinTypeText = 'Da khô';
            recommendationText = 'Đề xuất dịch vụ: Dưỡng ẩm chuyên sâu, phục hồi da';
        }

        skinType.textContent = `Loại da: ${skinTypeText}`;
        recommendation.textContent = recommendationText;

        resultDiv.style.display = 'block';
        document.querySelector('.quiz-section').style.display = 'none';
    }

// Mở modal khi nhấn nút "Nhấn vào đây để bắt đầu"
    document.addEventListener('DOMContentLoaded', function () {
        const startQuizBtn = document.getElementById('start-quiz-btn');
        if (startQuizBtn) {
            startQuizBtn.addEventListener('click', function () {
                loadQuiz(); // Tải câu hỏi
                const quizModal = new bootstrap.Modal(document.getElementById('quiz-modal'));
                quizModal.show();
            });
        } else {
            console.warn('Không tìm thấy nút #start-quiz-btn trong DOM');
        }
    });
// Gắn sự kiện cho nút "Hoàn thành"
    document.addEventListener('DOMContentLoaded', function () {
        const submitBtn = document.getElementById('submit-btn');
        if (submitBtn) {
            submitBtn.addEventListener('click', submitQuiz);
        } else {
            console.warn('Không tìm thấy phần tử #submit-btn');
        }
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

