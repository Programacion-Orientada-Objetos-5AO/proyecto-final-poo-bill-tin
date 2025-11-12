document.addEventListener("DOMContentLoaded", async () => {
    console.log("✅ monedas.js cargado correctamente");

    const { apiFetch, readForm, getToken } = window.Billetin || {};
    const API_BASE_URL = "http://localhost:8080/api";

    const tablaBody = document.querySelector("#monedas-table-body");
    const form = document.querySelector("form[action='/api/monedas']");

    if (!apiFetch || !getToken) {
        console.error("❌ Billetin no está inicializado. Verifica que app.js se cargue antes de monedas.js");
        return;
    }

    const token = getToken();
    if (!token) {
        console.warn("⚠️ No hay token. Redirigiendo a login...");
        window.location.href = "login.html";
        return;
    }

    // 🔁 Cargar monedas desde el backend
    const cargarMonedas = async () => {
        tablaBody.innerHTML = "<tr><td colspan='3' style='text-align:center;'>Cargando...</td></tr>";

        try {
            const monedas = await apiFetch(`${API_BASE_URL}/monedas`);
            console.log("💰 Monedas cargadas:", monedas);

            tablaBody.innerHTML = "";

            if (!monedas || monedas.length === 0) {
                tablaBody.innerHTML = "<tr><td colspan='3' style='text-align:center;'>No hay monedas registradas.</td></tr>";
                return;
            }

            monedas.forEach((moneda) => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${moneda.id}</td>
                    <td>${moneda.codigo}</td>
                    <td>${moneda.nombre}</td>
                `;
                tablaBody.appendChild(row);
            });
        } catch (error) {
            console.error("❌ Error cargando monedas:", error);
            tablaBody.innerHTML = "<tr><td colspan='3' style='text-align:center;'>Error al cargar monedas.</td></tr>";
        }
    };

    // 📝 Crear moneda
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            const data = readForm(form);
            console.log("📤 Enviando moneda:", data);

            try {
                const response = await apiFetch(`${API_BASE_URL}/monedas`, {
                    method: "POST",
                    body: JSON.stringify(data),
                });

                console.log("✅ Moneda creada correctamente:", response);
                alert("✅ Moneda creada correctamente");

                form.reset();
                await cargarMonedas(); // recarga desde el backend
            } catch (error) {
                console.error("❌ Error creando moneda:", error);
                alert("❌ Error al crear moneda");
            }   
        });
    }

    // 🚀 Cargar monedas existentes al entrar
    cargarMonedas();
});
