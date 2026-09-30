const slides = document.querySelectorAll(".hero-slide");
const prevBtn = document.getElementById("prevSlide");
const nextBtn = document.getElementById("nextSlide");

let currentSlide = 0;
let autoSlideTimer = null;

function showSlide(index) {
    slides.forEach((slide, i) => {
        const isActive = i === index;

        slide.classList.toggle("active", isActive);

        const video = slide.querySelector("video");

        if (video) {
            if (isActive) {
                video.currentTime = 0;
                video.play().catch(() => {});
            } else {
                video.pause();
            }
        }
    });
}

function nextSlide() {
    currentSlide = (currentSlide + 1) % slides.length;
    showSlide(currentSlide);
}

function prevSlide() {
    currentSlide = (currentSlide - 1 + slides.length) % slides.length;
    showSlide(currentSlide);
}

function startAutoSlide() {
    stopAutoSlide();
    autoSlideTimer = setInterval(nextSlide, 5000);
}

function stopAutoSlide() {
    if (autoSlideTimer) {
        clearInterval(autoSlideTimer);
        autoSlideTimer = null;
    }
}

if (slides.length > 0) {
    nextBtn?.addEventListener("click", () => {
        nextSlide();
        startAutoSlide();
    });

    prevBtn?.addEventListener("click", () => {
        prevSlide();
        startAutoSlide();
    });

    showSlide(currentSlide);
    startAutoSlide();
}

const shopBtn = document.getElementById("shopBtn");
const supportBtn = document.getElementById("supportBtn");
const accountBtn = document.getElementById("accountBtn");

const megaMenuShop = document.getElementById("megaMenuShop");
const megaMenuSupport = document.getElementById("megaMenuSupport");
const accountModal = document.getElementById("accountModal");

function closeAllMenus() {
    megaMenuShop?.classList.remove("show");
    megaMenuSupport?.classList.remove("show");
    accountModal?.classList.remove("show");

    shopBtn?.classList.remove("active");
    supportBtn?.classList.remove("active");
    accountBtn?.classList.remove("active");
}

if (shopBtn && megaMenuShop) {
    shopBtn.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();

        const isOpen = megaMenuShop.classList.contains("show");

        closeAllMenus();

        if (!isOpen) {
            megaMenuShop.classList.add("show");
            shopBtn.classList.add("active");
        }
    });
}

if (supportBtn && megaMenuSupport) {
    supportBtn.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();

        const isOpen = megaMenuSupport.classList.contains("show");

        closeAllMenus();

        if (!isOpen) {
            megaMenuSupport.classList.add("show");
            supportBtn.classList.add("active");
        }
    });
}

if (accountBtn && accountModal) {
    accountBtn.addEventListener("click", (e) => {
        e.stopPropagation();

        const isOpen = accountModal.classList.contains("show");

        closeAllMenus();

        if (!isOpen) {
            accountModal.classList.add("show");
            accountBtn.classList.add("active");
        }
    });
}

document.addEventListener("click", (e) => {
    if (!e.target.closest(".menu")) {
        closeAllMenus();
    }
});

megaMenuShop?.addEventListener("click", (e) => e.stopPropagation());
megaMenuSupport?.addEventListener("click", (e) => e.stopPropagation());
accountModal?.addEventListener("click", (e) => e.stopPropagation());