/* Visual interactions only. No fetch, storage, credentials or API changes. */
(() => {
  if (window.self !== window.top) document.body.classList.add('is-embedded');
  const toggle = document.querySelector('.menu-toggle');
  const nav = document.querySelector('#site-nav');
  if (toggle && nav) {
    toggle.addEventListener('click', () => {
      const open = toggle.getAttribute('aria-expanded') !== 'true';
      toggle.setAttribute('aria-expanded', String(open));
      toggle.setAttribute('aria-label', open ? 'Cerrar menú' : 'Abrir menú');
      nav.classList.toggle('is-open', open);
    });
    nav.addEventListener('keydown', event => {
      if (event.key === 'Escape') {
        toggle.setAttribute('aria-expanded', 'false');
        toggle.setAttribute('aria-label', 'Abrir menú');
        nav.classList.remove('is-open');
        toggle.focus();
      }
    });
  }
  const carousel = document.querySelector('.feature-carousel');
  if (!carousel) return;
  const slides = [...carousel.querySelectorAll('[data-slide]')];
  const dots = [...carousel.querySelectorAll('[data-go]')];
  let active = 0;
  function show(index) {
    active = (index + slides.length) % slides.length;
    slides.forEach((slide, i) => { slide.hidden = i !== active; });
    dots.forEach((dot, i) => dot.setAttribute('aria-pressed', String(i === active)));
    document.getElementById('slide-status').textContent = `Diapositiva ${active + 1} de ${slides.length}`;
  }
  dots.forEach(dot => dot.addEventListener('click', () => show(Number(dot.dataset.go))));
  carousel.querySelector('[data-prev]').addEventListener('click', () => show(active - 1));
  carousel.querySelector('[data-next]').addEventListener('click', () => show(active + 1));
})();
