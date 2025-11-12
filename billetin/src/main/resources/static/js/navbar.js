// navbar.js — versión segura sin redirección automática
const NAVBAR_SELECTORS = {
    navbar: '.navbar',
    cta: '.cta'
};

const ACTIONS = {
    HOME: 'home',
    LOGIN: 'login',
    SIGNUP: 'signup',
    LOGOUT: 'logout'
};

document.addEventListener('DOMContentLoaded', () => {
    const navbar = document.querySelector(NAVBAR_SELECTORS.navbar);
    if (!navbar) {
        return;
    }

    const existingMenu = navbar.querySelector('.user-menu');
    if (existingMenu) {
        existingMenu.remove();
    }

    const legacyCta = navbar.querySelector(NAVBAR_SELECTORS.cta);
    if (legacyCta) {
        legacyCta.remove();
    }

    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');

    const userMenu = document.createElement('div');
    userMenu.className = 'user-menu';
    userMenu.innerHTML = `
        <button class="user-avatar" type="button" id="userMenuToggle" aria-haspopup="true" aria-expanded="false">
            <svg class="user-avatar__icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                <path d="M12 2.5a5.5 5.5 0 1 1-5.5 5.5A5.51 5.51 0 0 1 12 2.5Zm0 9.5a4 4 0 1 0-4-4 4 4 0 0 0 4 4Zm0 2.5c4.14 0 7.5 2.58 7.5 5.75a1.25 1.25 0 0 1-2.5 0c0-1.68-2.4-3.25-5-3.25s-5 1.57-5 3.25a1.25 1.25 0 0 1-2.5 0C4.5 17.08 7.86 14.5 12 14.5Z"/>
            </svg>
            <span class="sr-only">Abrir menú de usuario</span>
        </button>
        <div class="user-dropdown" id="userDropdown" role="menu" hidden>
            <header class="user-dropdown__header">
                <span class="user-dropdown__label"></span>
                <span class="user-dropdown__description"></span>
            </header>
            <div class="user-dropdown__actions"></div>
        </div>
    `;

    navbar.appendChild(userMenu);

    const dropdown = userMenu.querySelector('#userDropdown');
    const toggleButton = userMenu.querySelector('#userMenuToggle');
    const label = dropdown.querySelector('.user-dropdown__label');
    const description = dropdown.querySelector('.user-dropdown__description');
    const actions = dropdown.querySelector('.user-dropdown__actions');

    const closeMenu = () => {
        dropdown.hidden = true;
        toggleButton.setAttribute('aria-expanded', 'false');
    };

    const openMenu = () => {
        dropdown.hidden = false;
        toggleButton.setAttribute('aria-expanded', 'true');
    };

    const toggleMenu = () => {
        if (dropdown.hidden) {
            openMenu();
        } else {
            closeMenu();
        }
    };

    const renderMenu = () => {
        if (token && username) {
            label.textContent = 'Sesión activa';
            description.textContent = username;
            description.classList.remove('user-dropdown__description--muted');
            actions.innerHTML = `
                <button class="user-dropdown__action" data-action="${ACTIONS.HOME}">🏠 Ir al inicio</button>
                <button class="user-dropdown__action user-dropdown__action--danger" data-action="${ACTIONS.LOGOUT}">🚪 Cerrar sesión</button>
            `;
        } else {
            label.textContent = 'Modo invitado';
            description.textContent = 'Inicia sesión para personalizar tu experiencia.';
            description.classList.add('user-dropdown__description--muted');
            actions.innerHTML = `
                <button class="user-dropdown__action user-dropdown__action--primary" data-action="${ACTIONS.LOGIN}">🔐 Iniciar sesión</button>
                <button class="user-dropdown__action" data-action="${ACTIONS.SIGNUP}">🆕 Crear cuenta</button>
            `;
        }
    };

    renderMenu();

    toggleButton.addEventListener('click', (event) => {
        event.preventDefault();
        toggleMenu();
    });

    actions.addEventListener('click', (event) => {
        const button = event.target.closest('button[data-action]');
        if (!button) {
            return;
        }

        event.preventDefault();
        const action = button.dataset.action;

        switch (action) {
            case ACTIONS.HOME:
                window.location.href = 'index.html';
                break;
            case ACTIONS.LOGIN:
                window.location.href = 'login.html';
                break;
            case ACTIONS.SIGNUP:
                window.location.href = 'registro.html';
                break;
            case ACTIONS.LOGOUT:
                if (confirm('¿Querés cerrar sesión?')) {
                    localStorage.removeItem('token');
                    localStorage.removeItem('username');
                    window.location.href = 'login.html';
                }
                break;
            default:
                break;
        }

        closeMenu();
    });

    document.addEventListener('click', (event) => {
        if (!dropdown.hidden && !userMenu.contains(event.target)) {
            closeMenu();
        }
    });

    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape') {
            closeMenu();
        }
    });
});
