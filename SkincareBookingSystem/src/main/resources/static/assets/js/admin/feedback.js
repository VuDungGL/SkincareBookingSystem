document.addEventListener("DOMContentLoaded", () => {
    const track = document.querySelector('.feedback-track');
    const carousel = document.querySelector('.feedback-carousel');
    const leftBtn = document.querySelector('.carousel-button.left');
    const rightBtn = document.querySelector('.carousel-button.right');

    let cards = [];
    let cardWidth = 0, visibleCards = 0, index = 0;
    let totalOriginalCards = 0;

    const createFeedbackCard = (feedback) => {
        const card = document.createElement('div');
        card.className = 'feedback-card';

        card.innerHTML = `
            <div class="feedback-content">
                <p class="feedback-message">"${feedback.content}"</p>
                <div class="feedback-author">
                    <img src="${feedback.avatar || '/default-avatar.png'}" alt="Avatar" class="feedback-avatar">
                    <div class="feedback-name">${feedback.fullName}</div>
                </div>
            </div>
        `;
        return card;
    };

    const cloneCards = () => {
        const clones = track.querySelectorAll('.cloned');
        clones.forEach(clone => clone.remove());

        const startClones = cards.slice(-visibleCards).map(card => {
            const clone = card.cloneNode(true);
            clone.classList.add('cloned');
            return clone;
        });

        const endClones = cards.slice(0, visibleCards).map(card => {
            const clone = card.cloneNode(true);
            clone.classList.add('cloned');
            return clone;
        });

        startClones.forEach(clone => track.insertBefore(clone, track.firstChild));
        endClones.forEach(clone => track.appendChild(clone));
    };

    const updateDimensions = () => {
        cards = Array.from(track.querySelectorAll('.feedback-card:not(.cloned)'));
        totalOriginalCards = cards.length;

        if (cards.length === 0) return;

        cardWidth = cards[0].offsetWidth + 40;
        visibleCards = Math.floor(carousel.offsetWidth / cardWidth) || 1;
        index = visibleCards;

        cloneCards();

        const totalCards = totalOriginalCards + visibleCards * 2;
        track.style.width = `${totalCards * cardWidth}px`;
        track.style.transition = 'none';
        track.style.transform = `translateX(-${index * cardWidth}px)`;
    };

    const moveToIndex = (newIndex) => {
        if (cardWidth === 0) return; // tránh lỗi khi chưa load xong
        track.style.transition = 'transform 0.5s ease-in-out';
        track.style.transform = `translateX(-${newIndex * cardWidth}px)`;
        index = newIndex;
    };

    const resetPosition = () => {
        track.style.transition = 'none';
        if (index >= totalOriginalCards + visibleCards) {
            index = visibleCards;
            track.style.transform = `translateX(-${index * cardWidth}px)`;
        } else if (index < visibleCards) {
            index = totalOriginalCards;
            track.style.transform = `translateX(-${index * cardWidth}px)`;
        }
    };

    const fetchFeedbacks = () => {
        $.ajax({
            url: '/feedback/getAllFeedback',
            method: 'GET',
            success: function (feedbackList) {
                track.innerHTML = ''; // clear old cards
                feedbackList.forEach(feedback => {
                    const card = createFeedbackCard(feedback);
                    track.appendChild(card);
                });
                updateDimensions();
            },
            error: function (xhr, status, error) {
                console.error('Lỗi khi tải feedback:', error);
            }
        });
    };

    rightBtn.addEventListener('click', () => moveToIndex(index + 1));
    leftBtn.addEventListener('click', () => moveToIndex(index - 1));
    track.addEventListener('transitionend', resetPosition);
    window.addEventListener('resize', updateDimensions);

    fetchFeedbacks(); // Load khi DOM sẵn sàng
});
