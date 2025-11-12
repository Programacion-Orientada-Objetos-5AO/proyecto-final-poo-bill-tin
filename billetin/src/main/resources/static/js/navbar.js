// navbar.js — versión segura sin redirección automática
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    const ctaButton = document.querySelector('.cta');

    // ✅ Si no hay token, simplemente mostramos el botón normal
    if (!token || !username) {
        console.log("⚠️ No hay sesión activa. Mostrando botón de inicio de sesión.");
        return;
    }

    // Si existe sesión, reemplazamos el botón por el icono de usuario
    if (ctaButton) {
        ctaButton.href = '#';
        ctaButton.innerHTML = '👤';
        ctaButton.style.cssText = `
            font-size: 1.5rem;
            background: linear-gradient(135deg, #358d52ff 0%, #399240ff 100%);
            width: 45px;
            height: 45px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        `;

        // Hover
        ctaButton.addEventListener('mouseenter', () => {
            ctaButton.style.transform = 'scale(1.1)';
            ctaButton.style.boxShadow = '0 6px 20px rgba(11, 129, 50, 0.5)';
        });

        ctaButton.addEventListener('mouseleave', () => {
            ctaButton.style.transform = 'scale(1)';
            ctaButton.style.boxShadow = '0 4px 15px rgba(33, 136, 33, 0.3)';
        });

        // Menú desplegable
        const menu = document.createElement('div');
        menu.id = 'userMenu';
        menu.style.cssText = `
            position: absolute;
            top: 70px;
            right: 20px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            padding: 1rem;
            min-width: 250px;
            display: none;
            z-index: 1000;
            animation: slideDown 0.3s ease;
        `;

        menu.innerHTML = `
            <div style="padding: 0.75rem; border-bottom: 2px solid #f0f0f0; margin-bottom: 0.75rem;">
                <div style="font-size: 0.85rem; color: #888; margin-bottom: 0.25rem;">Sesión activa</div>
                <div style="font-weight: 600; color: #333; font-size: 0.95rem; word-break: break-all;">${username}</div>
            </div>
            <button onclick="window.location.href='index.html'" style="
                width: 100%;
                padding: 0.75rem;
                background: #f5f5f5;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 0.95rem;
                font-weight: 500;
                color: #333;
                margin-bottom: 0.5rem;
                transition: all 0.2s;
                text-align: left;
            " onmouseover="this.style.background='#e0e0e0'" onmouseout="this.style.background='#f5f5f5'">
                🏠 Ir al inicio
            </button>
            <button id="logoutButton" style="
                width: 100%;
                padding: 0.75rem;
                background: #fee;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 0.95rem;
                font-weight: 500;
                color: #d32f2f;
                transition: all 0.2s;
                text-align: left;
            " onmouseover="this.style.background='#fdd'" onmouseout="this.style.background='#fee'">
                🚪 Cerrar sesión
            </button>
        `;

        document.body.appendChild(menu);

        // Animación
        const style = document.createElement('style');
        style.textContent = `
            @keyframes slideDown {
                from { opacity: 0; transform: translateY(-10px); }
                to { opacity: 1; transform: translateY(0); }
            }
        `;
        document.head.appendChild(style);

        // Toggle menú
        ctaButton.addEventListener('click', (e) => {
            e.preventDefault();
            menu.style.display = menu.style.display === 'none' ? 'block' : 'none';
        });

        // Cerrar al hacer clic fuera
        document.addEventListener('click', (e) => {
            if (!ctaButton.contains(e.target) && !menu.contains(e.target)) {
                menu.style.display = 'none';
            }
        });

        // Cerrar sesión
        document.getElementById('logoutButton').addEventListener('click', () => {
            if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                alert('Sesión cerrada exitosamente');
                window.location.href = 'login.html';
            }
        });
    }
});
