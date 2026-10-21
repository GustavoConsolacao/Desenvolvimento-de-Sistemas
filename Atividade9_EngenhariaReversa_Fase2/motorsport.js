document.addEventListener('DOMContentLoaded', () => {
    const cars = [
        {
            title: "GT3 EVO",
            desc: "Modelled on the race-winning 720S GT3. Proven and refined across thousands of kilometres on the track.",
            image: "./imagens/w3.png"
        },
        {
            title: "ARTURA GT4",
            desc: "An evolution of the Championship winning 570S GT4. Lightweight carbon fibre architecture. Compact V6 powertrain. Driven by winners.",
            image: "./imagens/w2.png"
        },
        {
            title: "ARTURA TROPHY EVO",
            desc: "Finely tuned suspension. An extra 620PS on demand. Lap times that come close to our GT3 models. Made exclusively for McLaren Trophy Championships.",
            image: "./imagens/w4.png"
        },
        {
            title: "MCL-HY GTR",
            desc: "McLaren's client track variant of the Hypercar that's set to compete in the 2027 FIA World Endurance Championship (WEC)",
            image: "./imagens/w5.png"
        }
    ];

    let currentIndex = 0;

    const sliderImage = document.getElementById('slider-image');
    const carTitle = document.getElementById('car-title');
    const carDesc = document.getElementById('car-desc');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const bars = document.querySelectorAll('.mclaren-bar');

    function updateSlider(index) {
        currentIndex = index;
        if (currentIndex < 0) currentIndex = cars.length - 1;
        if (currentIndex >= cars.length) currentIndex = 0;

        if (sliderImage) {
            sliderImage.style.opacity = '0';

            setTimeout(() => {
                sliderImage.src = cars[currentIndex].image;
                if (carTitle) carTitle.textContent = cars[currentIndex].title;
                if (carDesc) carDesc.textContent = cars[currentIndex].desc;

                bars.forEach((bar, i) => {
                    bar.classList.toggle('active', i === currentIndex);
                });

                sliderImage.style.opacity = '1';
            }, 200);
        }
    }

    if (prevBtn) prevBtn.addEventListener('click', () => updateSlider(currentIndex - 1));
    if (nextBtn) nextBtn.addEventListener('click', () => updateSlider(currentIndex + 1));

    bars.forEach((bar, index) => {
        bar.addEventListener('click', () => updateSlider(index));
    });
});