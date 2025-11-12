
// categorias.js - compatible con window.Billetin (sin imports)
document.addEventListener("DOMContentLoaded", () => {
  console.log("✅ categorias.js cargado correctamente");

  const { apiFetch, readForm, getToken } = window.Billetin || {};
  const API_BASE_URL = "http://localhost:8080/api";

  const tableBody = document.getElementById("categoriasTableBody");
  const form = document.getElementById("categoriaForm");
  const feedback = document.getElementById("categoriaFeedback");

  if (!apiFetch || !getToken) {
    console.error("❌ Billetin no está inicializado. Asegurate de cargar app.js antes de categorias.js");
    if (tableBody) tableBody.innerHTML = "<tr><td colspan='3' style='text-align:center;color:red;'>Error inicializando cliente</td></tr>";
    return;
  }

  // Helper para mostrar mensajes
  const showFeedback = (msg = "", isError = false, timeout = 4000) => {
    if (!feedback) return;
    feedback.style.display = msg ? "block" : "none";
    feedback.style.color = isError ? "#b71c1c" : "#0b6623";
    feedback.style.background = isError ? "#ffebee" : "#eef7ee";
    feedback.textContent = msg;
    if (timeout && msg) setTimeout(() => { feedback.style.display = "none"; }, timeout);
  };

  // Cargar categorias y pintar tabla
  const cargarCategorias = async () => {
    if (!tableBody) return;
    tableBody.innerHTML = `<tr><td colspan="3" style="text-align:center;">Cargando categorías...</td></tr>`;

    try {
      const categorias = await apiFetch(`${API_BASE_URL}/categorias`);
      console.log("📥 Categorías:", categorias);

      if (!Array.isArray(categorias) || categorias.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="3" style="text-align:center;">No hay categorías</td></tr>`;
        return;
      }

      tableBody.innerHTML = categorias.map(c => `
        <tr>
          <td>${c.id ?? c.idCategoria ?? "-"}</td>
          <td>${c.nombre ?? "-"}</td>
          <td>${c.tipo ?? "-"}</td>
        </tr>
      `).join("");
    } catch (err) {
      console.error("❌ Error cargando categorías:", err);
      tableBody.innerHTML = `<tr><td colspan="3" style="text-align:center;color:red;">Error al cargar categorías</td></tr>`;
      showFeedback("Error al cargar categorías. Revisa la consola.", true, 6000);
    }
  };

  // Crear nueva categoria
  if (form) {
    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      showFeedback(""); // limpiar

      const data = readForm(form);
      // Normalizar payload según DTO backend
      const payload = {
        nombre: data.nombre?.trim(),
        tipo: data.tipo
      };

      if (!payload.nombre || !payload.tipo) {
        showFeedback("Completa nombre y tipo antes de guardar.", true);
        return;
      }

      try {
        // POST
        await apiFetch(`${API_BASE_URL}/categorias`, {
          method: "POST",
          body: JSON.stringify(payload)
        });

        showFeedback("✅ Categoría creada correctamente");
        form.reset();
        await cargarCategorias();
      } catch (err) {
        console.error("❌ Error creando categoría:", err);
        const msg = (err && (err.message || (err.body && (err.body.detail || err.body.message)))) || "Error creando categoría";
        showFeedback(`❌ ${msg}`, true, 6000);
      }
    });
  }

  // Inicial
  cargarCategorias();
});
