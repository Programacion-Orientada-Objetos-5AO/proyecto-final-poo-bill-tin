document.addEventListener("DOMContentLoaded", async () => {
    console.log("✅ membresias.js cargado correctamente");

    const { apiFetch, readForm, getToken } = window.Billetin || {};
    const API_BASE_URL = "http://localhost:8080/api";

    const tablaBody = document.querySelector("#membresias-table-body");
    const form = document.querySelector("form[action='/api/membresias']");

    // 🧩 Verificación de inicialización
    if (!apiFetch || !getToken) {
        console.error("❌ Billetin no está inicializado. Verifica que app.js se cargue antes de membresias.js");
        return;
    }

    const token = getToken();
    if (!token) {
        console.warn("⚠️ No hay token. Redirigiendo a login...");
        window.location.href = "login.html";
        return;
    }

    // 🔁 Cargar membresías desde el backend
    const cargarMembresias = async () => {
        tablaBody.innerHTML = "<tr><td colspan='5' style='text-align:center;'>Cargando...</td></tr>";

        try {
            const membresias = await apiFetch(`${API_BASE_URL}/membresias`);
            console.log("🎟️ Membresías cargadas:", membresias);

            tablaBody.innerHTML = "";

            if (!membresias || membresias.length === 0) {
                tablaBody.innerHTML = "<tr><td colspan='5' style='text-align:center;'>No hay membresías registradas.</td></tr>";
                return;
            }

            membresias.forEach((m) => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${m.id}</td>
                    <td>${m.nombre}</td>
                    <td>${m.precio}</td>
                    <td>${m.beneficios || "-"}</td>
                    <td>${m.duracion ? new Date(m.duracion).toLocaleDateString() : "-"}</td>
                `;
                tablaBody.appendChild(row);
            });
        } catch (error) {
            console.error("❌ Error cargando membresías:", error);
            tablaBody.innerHTML = "<tr><td colspan='5' style='text-align:center;'>Error al cargar membresías.</td></tr>";
        }
    };

    // 📝 Crear membresía
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            const data = readForm(form);

            // ✅ Coinciden con el DTO del backend
            const membresiaPayload = {
                nombre: data.nombre,
                precio: parseFloat(data.precio),
                beneficios: data.beneficios,
                duracion: data.duracion
            };

            console.log("📤 Enviando membresía:", membresiaPayload);

            try {
                const response = await apiFetch(`${API_BASE_URL}/membresias`, {
                    method: "POST",
                    body: JSON.stringify(membresiaPayload),
                });

                console.log("✅ Membresía creada correctamente:", response);
                alert("✅ Membresía creada correctamente");
                form.reset();
                await cargarMembresias();
            } catch (error) {
                console.error("❌ Error creando membresía:", error);
                alert("❌ Error al crear membresía");
            }
        });
    }

    // 🚀 Cargar membresías existentes al entrar
    cargarMembresias();
});
