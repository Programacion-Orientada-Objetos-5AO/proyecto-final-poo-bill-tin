document.addEventListener("DOMContentLoaded", () => {
  if (!window.Billetin) {
    // Fallback if core helper is missing
    return;
  }

  const form = document.getElementById("categoriaForm");
  const submitBtn = document.getElementById("categoriaSubmit");
  const feedback = document.getElementById("categoriaFeedback");
  const tableBody = document.getElementById("categoriasTableBody");

  const showFeedback = (message, type = "info") => {
    if (!feedback) return;
    feedback.textContent = message;
    feedback.style.display = "block";
    feedback.style.background =
      type === "success" ? "#e8f5e9" : type === "error" ? "#ffebee" : "#e3f2fd";
    feedback.style.color =
      type === "success" ? "#1b5e20" : type === "error" ? "#c62828" : "#0d47a1";
  };

  const clearFeedback = () => {
    if (!feedback) return;
    feedback.style.display = "none";
    feedback.textContent = "";
  };

  const renderCategorias = (categorias) => {
    if (!tableBody) {
      return;
    }

    tableBody.innerHTML = "";

    if (!Array.isArray(categorias) || categorias.length === 0) {
      const row = document.createElement("tr");
      const cell = document.createElement("td");
      cell.colSpan = 3;
      cell.style.textAlign = "center";
      cell.style.color = "var(--text-muted)";
      cell.style.padding = "1.5rem";
      cell.textContent = "No hay categorias registradas todavia.";
      row.appendChild(cell);
      tableBody.appendChild(row);
      return;
    }

    categorias.forEach((categoria) => {
      const row = document.createElement("tr");

      const idCell = document.createElement("td");
      idCell.textContent = categoria.idCategoria ?? "—";
      row.appendChild(idCell);

      const nombreCell = document.createElement("td");
      nombreCell.textContent = categoria.nombre ?? "—";
      row.appendChild(nombreCell);

      const tipoCell = document.createElement("td");
      tipoCell.textContent = categoria.tipo ?? "—";
      row.appendChild(tipoCell);

      tableBody.appendChild(row);
    });
  };

  const loadCategorias = async () => {
    if (!tableBody) return;
    tableBody.innerHTML = `
      <tr>
        <td colspan="3" style="text-align:center;color:var(--text-muted);padding:1.5rem;">
          Cargando categorias...
        </td>
      </tr>
    `;

    try {
      const categorias = await window.Billetin.apiFetch("/api/categorias");
      renderCategorias(categorias);
    } catch (error) {
      renderCategorias([]);
      showFeedback(error.message || "No se pudieron obtener las categorias.", "error");
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!form || !submitBtn) return;

    const data = window.Billetin.readForm(form);
    const payload = {
      nombre: (data.nombre || "").trim(),
      tipo: data.tipo || "",
    };

    if (!payload.nombre || !payload.tipo) {
      showFeedback("Completá el nombre y el tipo antes de guardar.", "error");
      return;
    }

    submitBtn.disabled = true;
    submitBtn.textContent = "Guardando...";
    clearFeedback();

    try {
      await window.Billetin.apiFetch("/api/categorias", {
        method: "POST",
        body: JSON.stringify(payload),
      });

      form.reset();
      showFeedback("Categoria creada correctamente.", "success");
      await loadCategorias();
    } catch (error) {
      const message =
        (error.body && error.body.detail) ||
        error.message ||
        "Ocurrió un error al crear la categoria.";
      showFeedback(message, "error");
    } finally {
      submitBtn.disabled = false;
      submitBtn.textContent = "Guardar categoria";
    }
  };

  try {
    window.Billetin.requireAuth();
  } catch (error) {
    showFeedback(error.message || "Necesitas iniciar sesión para ver las categorias.", "error");
    return;
  }

  if (form) {
    form.addEventListener("submit", handleSubmit);
  }

  loadCategorias();
});
