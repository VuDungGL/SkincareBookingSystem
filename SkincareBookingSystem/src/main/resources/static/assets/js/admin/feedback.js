document.addEventListener("DOMContentLoaded", () => {
    const track = document.querySelector('.feedback-track');
    const carousel = document.querySelector('.feedback-carousel');
    const leftBtn = document.querySelector('.carousel-button.left');
    const rightBtn = document.querySelector('.carousel-button.right');
    const cards = Array.from(document.querySelectorAll('.feedback-card'));

    const cardWidth = cards[0].offsetWidth + 40; // 40 = gap giữa các card
    const visibleCards = Math.floor(carousel.offsetWidth / cardWidth);
    let index = visibleCards; // Bắt đầu từ phần tử đầu sau khi clone

    // Clone đầu & cuối để tạo hiệu ứng tuần hoàn
    const clonesStart = cards.slice(-visibleCards).map(card => card.cloneNode(true));
    const clonesEnd = cards.slice(0, visibleCards).map(card => card.cloneNode(true));

    clonesStart.forEach(clone => track.insertBefore(clone, track.firstChild));
    clonesEnd.forEach(clone => track.appendChild(clone));

    // Cập nhật độ rộng của track
    track.style.transform = `translateX(-${index * cardWidth}px)`;

    const moveToIndex = (newIndex) => {
        track.style.transition = 'transform 0.5s ease-in-out';
        track.style.transform = `translateX(-${newIndex * cardWidth}px)`;
        index = newIndex;
    };

    const resetPosition = () => {
        track.style.transition = 'none';
        if (index >= cards.length + visibleCards) {
            // Nếu đến clone cuối, quay lại đầu thật
            index = visibleCards;
            track.style.transform = `translateX(-${index * cardWidth}px)`;
        } else if (index < visibleCards) {
            // Nếu đến clone đầu, quay lại cuối thật
            index = cards.length;
            track.style.transform = `translateX(-${index * cardWidth}px)`;
        }
    };

    rightBtn.addEventListener('click', () => {
        moveToIndex(index + 1);
    });

    leftBtn.addEventListener('click', () => {
        moveToIndex(index - 1);
    });

    track.addEventListener('transitionend', resetPosition);
});
