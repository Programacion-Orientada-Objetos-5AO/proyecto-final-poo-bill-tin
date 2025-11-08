// Configuración de la API
const API_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', function() {
    cargarCategorias();
    
    const categoriaForm = document.getElementById('categoriaForm');
    const submitBtn = document.getElementById('categoriaSubmit');
    const feedback = document.getElementById('categoriaFeedback');
    
    categoriaForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        // Deshabilitar botón
        submitBtn.disabled = true;
        submitBtn.textContent = 'Guardando...';
        feedback.style.display = 'none';
        
        // Obtener datos del formulario
        const nombre = document.getElementById('nombre').value.trim();
        const tipoSelect = document.getElementById('tipo').value;
        
        // Convertir el valor del select al enum de Java
        const tipoMap = {
            'ingreso_fijo': 'INGRESO_FIJO',
            'ingreso_variable': 'INGRESO_VARIABLE',
            'egreso_fijo': 'EGRESO_FIJO',
            'egreso_variable': 'EGRESO_VARIABLE'
        };
        
        const tipo = tipoMap[tipoSelect];
        
        try {
            const response = await fetch(`${API_URL}/api/categorias`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    nombre: nombre,
                    tipo: tipo
                })
            });
            
            if (response.ok) {
                mostrarFeedback('✅ Categoría creada exitosamente', 'success');
                categoriaForm.reset();
                cargarCategorias(); // Recargar la tabla
            } else {
                const error = await response.json();
                mostrarFeedback('❌ Error: ' + (error.message || 'No se pudo crear la categoría'), 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            mostrarFeedback('❌ Error de conexión con el servidor', 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Guardar categoria';
        }
    });
});

async function cargarCategorias() {
    const tbody = document.getElementById('categoriasTableBody');
    
    try {
        const response = await fetch(`${API_URL}/api/categorias`);
        const categorias = await response.json();
        
        if (categorias.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="3" style="text-align:center;color:var(--text-muted);padding:1.5rem;">
                        No hay categorías registradas. Crea la primera usando el formulario.
                    </td>
                </tr>
            `;
            return;
        }
        
        // Mapear los tipos de vuelta al español
        const tipoTexto = {
            'INGRESO_FIJO': 'Ingreso fijo',
            'INGRESO_VARIABLE': 'Ingreso variable',
            'EGRESO_FIJO': 'Gasto fijo',
            'EGRESO_VARIABLE': 'Gasto variable'
        };
        
        tbody.innerHTML = categorias.map(cat => `
            <tr>
                <td>${cat.idCategoria}</td>
                <td><strong>${cat.nombre}</strong></td>
                <td>${tipoTexto[cat.tipo] || cat.tipo}</td>
            </tr>
        `).join('');
        
    } catch (error) {
        console.error('Error al cargar categorías:', error);
        tbody.innerHTML = `
            <tr>
                <td colspan="3" style="text-align:center;color:#d32f2f;padding:1.5rem;">
                    ❌ Error al cargar las categorías. Verifica que el servidor esté corriendo.
                </td>
            </tr>
        `;
    }
}

function mostrarFeedback(mensaje, tipo) {
    const feedback = document.getElementById('categoriaFeedback');
    feedback.style.display = 'block';
    feedback.textContent = mensaje;
    
    if (tipo === 'success') {
        feedback.style.background = '#d4edda';
        feedback.style.color = '#155724';
        feedback.style.border = '1px solid #c3e6cb';
    } else {
        feedback.style.background = '#f8d7da';
        feedback.style.color = '#721c24';
        feedback.style.border = '1px solid #f5c6cb';
    }
    
    setTimeout(() => {
        feedback.style.display = 'none';
    }, 5000);
}