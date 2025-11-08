document.addEventListener("DOMContentLoaded", () => {
  if (!window.Billetin) {
    return;
  }

  const tableBody = document.getElementById("transaccionesTableBody");
  const feedback = document.getElementById("transaccionesFeedback");

  const setFeedback = (message = "", type = "info") => {
    if (!feedback) return;
    if (!message) {
      feedback.hidden = true;
      feedback.textContent = "";
      feedback.classList.remove("error");
      return;
    }

    feedback.textContent = message;
    feedback.hidden = false;
    feedback.classList.toggle("error", type === "error");
  };

  const formatAmount = (value, currencyName) => {
    const numericValue = Number(value);
    if (!Number.isFinite(numericValue)) {
      return "—";
    }

    const possibleCode =
      typeof currencyName === "string" && /^[A-Za-z]{3}$/.test(currencyName.trim())
        ? currencyName.trim().toUpperCase()
        : null;

    if (possibleCode && window.Billetin.formatCurrency) {
      try {
        return window.Billetin.formatCurrency(numericValue, possibleCode);
      } catch (error) {
        // Fallback below
      }
    }

    return numericValue.toLocaleString("es-AR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
  };

  const renderTransacciones = (transacciones) => {
    if (!tableBody) return;

    tableBody.innerHTML = "";

    if (!Array.isArray(transacciones) || transacciones.length === 0) {
      const row = document.createElement("tr");
      const cell = document.createElement("td");
      cell.colSpan = 6;
      cell.style.textAlign = "center";
      cell.style.color = "var(--text-muted)";
      cell.style.padding = "1.5rem";
      cell.textContent = "No hay datos disponibles.";
      row.appendChild(cell);
      tableBody.appendChild(row);
      return;
    }

    transacciones.forEach((transaccion) => {
      const row = document.createElement("tr");

      const fechaCell = document.createElement("td");
      fechaCell.textContent = window.Billetin.formatDate(transaccion.fecha);
      row.appendChild(fechaCell);

      const usuarioCell = document.createElement("td");
      usuarioCell.textContent = transaccion.usuarioNombre ?? "—";
      row.appendChild(usuarioCell);

      const categoriaCell = document.createElement("td");
      const categoriaWrapper = document.createElement("span");
      categoriaWrapper.className = "status-pill";
      categoriaWrapper.textContent = transaccion.categoriaNombre ?? "—";
      categoriaCell.appendChild(categoriaWrapper);
      row.appendChild(categoriaCell);

      const monedaCell = document.createElement("td");
      monedaCell.textContent = transaccion.monedaNombre ?? "—";
      row.appendChild(monedaCell);

      const montoCell = document.createElement("td");
      montoCell.textContent = formatAmount(transaccion.monto, transaccion.monedaNombre);
      row.appendChild(montoCell);

      const accionesCell = document.createElement("td");
      accionesCell.style.color = "var(--text-muted)";
      accionesCell.textContent = "—";
      row.appendChild(accionesCell);

      tableBody.appendChild(row);
    });
  };

  const loadTransacciones = async () => {
    if (!tableBody) return;

    tableBody.innerHTML = `
      <tr>
        <td colspan="6" style="text-align:center;color:var(--text-muted);padding:1.5rem;">
          Cargando transacciones...
        </td>
      </tr>
    `;
    setFeedback("", "info");

    try {
      const transacciones = await window.Billetin.apiFetch("/api/transacciones");
      renderTransacciones(transacciones);
    } catch (error) {
      renderTransacciones([]);
      const message =
        (error &&
          (error.message ||
            (error.body && (error.body.detail || error.body.message)))) ||
        "No se pudieron obtener las transacciones.";
      setFeedback(message, "error");
    }
  };

  try {
    window.Billetin.requireAuth();
  } catch (error) {
    renderTransacciones([]);
    setFeedback(error.message || "Necesitas iniciar sesion para ver las transacciones.", "error");
    return;
  }

  loadTransacciones();
});
