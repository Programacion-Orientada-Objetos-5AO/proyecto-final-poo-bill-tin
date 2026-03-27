console.log("✅ JS cargado correctamente en Mac");

// URL base del backend
const API_URL = "http://localhost:8080/api/objetivos";

// Esperar a que el DOM esté cargado
document.addEventListener("DOMContentLoaded", async () => {
    const btnGuardar = document.getElementById("guardar-btn");
    const form = document.getElementById("objetivo-form");
    const mensaje = document.getElementById("mensaje");
    const tablaBody = document.getElementById("objetivos-table-body");

    // ✅ Verificar que los elementos existan
    if (!btnGuardar || !form) {
        console.error("❌ No se encontró el formulario o el botón en el DOM");
        return;
    }

    // ✅ Verificar token en localStorage
    const token = localStorage.getItem("token");
    if (!token) {
        alert("⚠️ No hay sesión activa. Por favor, iniciá sesión nuevamente.");
        window.location.href = "login.html";
        return;
    }

    // ✅ Cargar objetivos existentes
    try {
        const resp = await fetch(API_URL, {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (resp.status === 401) {
            localStorage.removeItem("token");
            alert("⚠️ Tu sesión expiró. Iniciá sesión otra vez.");
            window.location.href = "login.html";
            return;
        }

        const objetivos = await resp.json();
        if (tablaBody) {
            tablaBody.innerHTML = "";
            objetivos.forEach(o => {
                const fila = `
                    <tr>
                        <td>${o.nombre}</td>
                        <td>$${o.montoMeta.toLocaleString()}</td>
                        <td>${o.fechaFin}</td>
                        <td>${o.estado || "Activo"}</td>
                    </tr>
                `;
                tablaBody.innerHTML += fila;
            });
        }

    } catch (err) {
        console.error("Error cargando objetivos:", err);
        if (mensaje) mensaje.textContent = "No se pudieron cargar los objetivos.";
    }

    // ✅ Guardar nuevo objetivo
    btnGuardar.addEventListener("click", async () => {
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        const nuevoObjetivo = {
            nombre: document.getElementById("nombre").value,
            montoMeta: parseFloat(document.getElementById("monto").value),
            fechaFin: document.getElementById("fechaLimite").value
        };

        try {
            const resp = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify(nuevoObjetivo)
            });

            if (resp.status === 401) {
                localStorage.removeItem("token");
                alert("⚠️ Sesión expirada. Iniciá sesión otra vez.");
                window.location.href = "login.html";
                return;
            }

            if (!resp.ok) {
                throw new Error(`Error ${resp.status}`);
            }

            alert("🎯 Objetivo guardado correctamente");
            window.location.href = "objetivoCreado.html";

        } catch (error) {
            console.error("Error al guardar:", error);
            if (mensaje) mensaje.textContent = "No se pudo guardar el objetivo.";
        }
    });
});
