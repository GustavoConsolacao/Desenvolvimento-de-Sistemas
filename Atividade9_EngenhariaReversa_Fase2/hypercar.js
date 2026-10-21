const heroSection = document.querySelector('.hero-section');
const heroCar = document.getElementById('heroCar');
const previousButton = document.getElementById('previousButton');
const nextButton = document.getElementById('nextButton');
const progressItems = document.querySelectorAll('.progress-item');
const modelLinks = document.querySelectorAll('.models-nav a');
const modelName = document.getElementById('modelName');
const modelDescription = document.getElementById('modelDescription');
const menuButton = document.getElementById('menuButton');
const menuOverlay = document.getElementById('menuOverlay');
const menuClose = document.getElementById('menuClose');

let current = 0;
let busy = false;

const slides = [
    {
        name: 'W1',
        description: 'Uma combinação extraordinária de potência, leveza e tecnologia. O W1 representa uma nova geração de desempenho McLaren.'
    },
    {
        name: 'W1',
        description: 'Aerodinâmica avançada e engenharia ultraleve trabalham juntas para entregar uma experiência de condução excepcional.'
    },
    {
        name: 'W1',
        description: 'Tecnologia inspirada nas pistas encontra uma obsessão por desempenho, precisão e controle.'
    },
    {
        name: 'W1',
        description: 'A busca pela perfeição transforma cada detalhe em parte essencial da experiência McLaren.'
    }
];

function updateProgress() {
    progressItems.forEach((item, index) => {
        item.classList.toggle('active', index === current);
    });
}

function renderSlide() {
    modelName.textContent = slides[current].name;
    modelDescription.textContent = slides[current].description;
    updateProgress();
}

function changeSlide(direction) {
    if (busy) return;

    busy = true;

    heroSection.classList.add('transitioning');

    setTimeout(() => {

        current = direction === 'next'
            ? (current + 1) % slides.length
            : (current - 1 + slides.length) % slides.length;

        renderSlide();

        heroCar.style.transition = 'none';

        heroCar.style.transform = direction === 'next'
            ? 'scale(.96) translateX(18px)'
            : 'scale(.96) translateX(-18px)';

        requestAnimationFrame(() => {

            requestAnimationFrame(() => {

                heroCar.style.transition =
                    'opacity 900ms ease-out, transform 900ms ease-out';

                heroCar.style.opacity = '1';

                heroCar.style.transform =
                    'scale(1) translateX(0)';

                heroSection.classList.remove('transitioning');

                setTimeout(() => {
                    busy = false;
                }, 650);

            });

        });

    }, 420);
}

nextButton.addEventListener('click', () => {
    changeSlide('next');
});

previousButton.addEventListener('click', () => {
    changeSlide('previous');
});

progressItems.forEach((item, index) => {

    item.addEventListener('click', () => {

        if (busy || index === current) return;

        changeSlide(
            index > current
                ? 'next'
                : 'previous'
        );

    });

});

modelLinks.forEach(link => {

    link.addEventListener('click', event => {

        event.preventDefault();

        modelLinks.forEach(item => {
            item.classList.remove('active');
        });

        link.classList.add('active');

    });

});

menuButton.addEventListener('click', () => {

    menuOverlay.classList.add('open');

    document.body.style.overflow = 'hidden';

});

function closeMenu() {

    menuOverlay.classList.remove('open');

    document.body.style.overflow = '';

}

menuClose.addEventListener('click', closeMenu);

menuOverlay.addEventListener('click', event => {

    if (event.target === menuOverlay) {
        closeMenu();
    }

});

document.addEventListener('keydown', event => {

    if (event.key === 'Escape') {
        closeMenu();
    }

    if (event.key === 'ArrowRight') {
        changeSlide('next');
    }

    if (event.key === 'ArrowLeft') {
        changeSlide('previous');
    }

});

heroCar.addEventListener('load', () => {

    requestAnimationFrame(() => {
        heroCar.classList.add('loaded');
    });

});

if (heroCar.complete) {
    heroCar.classList.add('loaded');
}

updateProgress();