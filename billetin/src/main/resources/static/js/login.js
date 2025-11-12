// Configuración de la API
const API_URL = 'http://localhost:8080';

// Esperar a que el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const submitBtn = document.getElementById('submitBtn');
    const errorMessage = document.getElementById('errorMessage');
    
    // Verificar si ya hay una sesión activa
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    
    if (token && username) {
        mostrarSesionActiva(username);
        return; // No cargar el formulario
    }

    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        // Deshabilitar botón mientras se procesa
        submitBtn.disabled = true;
        submitBtn.textContent = 'Ingresando...';
        errorMessage.style.display = 'none';

        // Obtener datos del formulario
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            // Hacer petición al backend
            const response = await fetch(`${API_URL}/api/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            const data = await response.json();

            if (response.ok) {
                // Login exitoso - guardar token
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', username);
                
                // Mostrar mensaje de éxito
                alert('¡Inicio de sesión exitoso!');
                
                // Redirigir al dashboard o página principal
                window.location.href = 'index.html';
            } else {
                // Error en las credenciales
                mostrarError(data.detail || 'Credenciales inválidas. Verifica tu usuario y contraseña.');
            }

        } catch (error) {
            console.error('Error:', error);
            mostrarError('No se pudo conectar con el servidor. Verifica que la API esté corriendo en http://localhost:8080');
        } finally {
            // Rehabilitar botón
            submitBtn.disabled = false;
            submitBtn.textContent = 'Ingresar';
        }
    });

    function mostrarError(mensaje) {
        errorMessage.textContent = mensaje;
        errorMessage.style.display = 'block';
    }

    function mostrarSesionActiva(username) {
        // Ocultar el formulario
        loginForm.style.display = 'none';
        
        // Crear el mensaje de sesión activa
        const sesionDiv = document.createElement('div');
        sesionDiv.style.cssText = 'text-align:center; padding:3rem 2rem; background:linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius:16px; box-shadow:0 10px 30px rgba(0,0,0,0.2);';
        sesionDiv.innerHTML = `
            <div style="font-size:4rem; margin-bottom:1rem; animation:bounce 0.6s;">✓</div>
            <h2 style="color:#fff; margin-bottom:0.5rem; font-size:1.8rem; font-weight:600;">¡Sesión activa!</h2>
            <p style="color:rgba(255,255,255,0.9); margin-bottom:2rem; font-size:1rem;">
                Has iniciado sesión como<br>
                <strong style="font-size:1.1rem; color:#fff;">${username}</strong>
            </p>
            <div style="display:flex; gap:1rem; justify-content:center; flex-wrap:wrap;">
                <button onclick="window.location.href='index.html'" 
                    style="padding:0.75rem 2rem; background:#fff; color:#667eea; border:none; border-radius:8px; font-weight:600; cursor:pointer; font-size:1rem; transition:all 0.3s; box-shadow:0 4px 15px rgba(0,0,0,0.2);"
                    onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 6px 20px rgba(0,0,0,0.3)';"
                    onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='0 4px 15px rgba(0,0,0,0.2)';">
                    🏠 Ir al inicio
                </button>
                <button onclick="cerrarSesion()" 
                    style="padding:0.75rem 2rem; background:rgba(255,255,255,0.2); color:#fff; border:2px solid #fff; border-radius:8px; font-weight:600; cursor:pointer; font-size:1rem; transition:all 0.3s; backdrop-filter:blur(10px);"
                    onmouseover="this.style.background='rgba(255,255,255,0.3)'; this.style.transform='translateY(-2px)';"
                    onmouseout="this.style.background='rgba(255,255,255,0.2)'; this.style.transform='translateY(0)';">
                    🚪 Cerrar sesión
                </button>
            </div>
        `;
        
        // Insertar después del header del formulario
        const formLayout = document.querySelector('.form-layout');
        const header = formLayout.querySelector('header');
        header.insertAdjacentElement('afterend', sesionDiv);
        
        // Ocultar también el form-footer
        const footer = document.querySelector('.form-footer');
        if (footer) footer.style.display = 'none';
        
        // Agregar animación bounce
        const style = document.createElement('style');
        style.textContent = `
            @keyframes bounce {
                0%, 100% { transform: translateY(0); }
                50% { transform: translateY(-20px); }
            }
        `;
        document.head.appendChild(style);
    }
});

// Función global para cerrar sesión
function cerrarSesion() {
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        alert('Sesión cerrada exitosamente');
        location.reload(); // Recargar la página
    }
}