// transacciones.js
document.addEventListener("DOMContentLoaded", () => {
  console.log("✅ transacciones.js cargado correctamente");

  const API_BASE_URL = "http://localhost:8080/api";
  const token = localStorage.getItem("token"); // usa la clave que venías usando

  // token requerido
  if (!token) {
    console.warn("⚠️ No hay token. Redirigiendo a login...");
    window.location.href = "login.html";
    return;
  }

  // Helpers
  const headersAuth = () => ({
    "Authorization": `Bearer ${token}`,
    "Content-Type": "application/json"
  });

  const formatCurrency = (v) => {
    if (v == null || Number.isNaN(Number(v))) return "-";
    return new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", minimumFractionDigits: 2 }).format(Number(v));
  };

  const formatDate = (d) => {
    if (!d) return "-";
    // acepta strings ISO o Date
    try {
      const date = (typeof d === "string") ? new Date(d) : d;
      if (Number.isNaN(date.getTime())) return d;
      return date.toLocaleDateString("es-AR");
    } catch {
      return d;
    }
  };

  // Si el HTML no tiene elementos para resultados de consultas, los creamos
  const ensureResultContainers = () => {
    // gasto entre fechas
    let contFechas = document.getElementById("resultadoFechas");
    if (!contFechas) {
      const form = document.querySelector('form[action="/api/transacciones/gasto-entre-fechas"]');
      if (form) {
        contFechas = document.createElement("div");
        contFechas.id = "resultadoFechas";
        contFechas.style.marginTop = "0.75rem";
        form.insertAdjacentElement("afterend", contFechas);
      }
    }
    // gasto por categoria
    let contCat = document.getElementById("resultadoCategoria");
    if (!contCat) {
      const form2 = document.querySelector('form[action^="/api/transacciones/gastos-por-categoria"]');
      if (form2) {
        contCat = document.createElement("div");
        contCat.id = "resultadoCategoria";
        contCat.style.marginTop = "0.75rem";
        form2.insertAdjacentElement("afterend", contCat);
      }
    }
  };

  ensureResultContainers();

  // Tabla donde pintamos las transacciones
  const tablaBody = document.getElementById("transaccionesTableBody");

  // Extrae primer número que encuentre dentro de un objeto (por si el backend devuelve { total: 50000 } u otra forma)
  const extractNumberFrom = (value) => {
    if (value == null) return null;
    if (typeof value === "number") return value;
    if (typeof value === "string" && !Number.isNaN(Number(value))) return Number(value);
    if (typeof value === "object") {
      for (const k of Object.keys(value)) {
        const v = value[k];
        const found = extractNumberFrom(v);
        if (found != null) return found;
      }
    }
    return null;
  };

  // Cargar transacciones y pintar la tabla
  async function cargarTransacciones() {
    if (!tablaBody) return;
    tablaBody.innerHTML = `<tr><td colspan="8" style="text-align:center;padding:1rem">Cargando transacciones...</td></tr>`;

    try {
      const res = await fetch(`${API_BASE_URL}/transacciones`, {
        headers: headersAuth()
      });
      if (res.status === 401) {
        console.warn("401 desde backend -> limpiar token");
        localStorage.removeItem("token");
        window.location.href = "login.html";
        return;
      }
      if (!res.ok) throw new Error(`HTTP ${res.status}`);

      const transacciones = await res.json();

      if (!Array.isArray(transacciones) || transacciones.length === 0) {
        tablaBody.innerHTML = `<tr><td colspan="8" style="text-align:center;padding:1rem">No hay transacciones registradas.</td></tr>`;
        return;
      }

      // Mapear cada transacción intentando soportar varias formas (DTOs distintos)
      tablaBody.innerHTML = transacciones.map(t => {
        // posibles campos:
        // t.id, t.nombreUsuario, t.monto, t.descripcion, t.fecha,
        // t.usuarioNombre OR t.usuario?.nombre
        // t.categoriaNombre OR t.categoria?.nombre
        // t.monedaNombre OR t.moneda?.nombre OR t.moneda?.codigo
        const id = t.id ?? "-";
        const nombreUsuario = t.nombreUsuario ?? t.usuarioNombre ?? (t.usuario && (t.usuario.nombre ?? t.usuario.username)) ?? "-";
        const monto = t.monto ?? t.montoMeta ?? "-";
        const descripcion = t.descripcion ?? t.descripcionTransaccion ?? "-";
        const fecha = t.fecha ?? t.fechaOperacion ?? "-";
        const usuarioNombre = t.usuarioNombre ?? (t.usuario && (t.usuario.nombre ?? t.usuario.username)) ?? "-";
        const categoriaNombre = t.categoriaNombre ?? (t.categoria && (t.categoria.nombre)) ?? "-";
        const monedaNombre = t.monedaNombre ?? (t.moneda && (t.moneda.nombre ?? t.moneda.codigo)) ?? "-";

        return `
          <tr>
            <td>${id}</td>
            <td>${escapeHtml(nombreUsuario)}</td>
            <td>${formatCurrency(monto)}</td>
            <td style="max-width:280px;white-space:pre-wrap;">${escapeHtml(descripcion)}</td>
            <td>${formatDate(fecha)}</td>
            <td>${escapeHtml(usuarioNombre)}</td>
            <td>${escapeHtml(categoriaNombre)}</td>
            <td>${escapeHtml(monedaNombre)}</td>
          </tr>
        `;
      }).join("");

    } catch (err) {
      console.error("❌ Error cargando transacciones:", err);
      tablaBody.innerHTML = `<tr><td colspan="8" style="text-align:center;color:red;padding:1rem">Error al cargar transacciones. Ver consola.</td></tr>`;
    }
  }

  // Crear transacción (POST)
  async function crearTransaccion() {
    const nombreUsuario = document.getElementById("nombreUsuario")?.value?.trim();
    const montoRaw = document.getElementById("monto")?.value;
    const descripcion = document.getElementById("descripcion")?.value?.trim();
    const fecha = document.getElementById("fecha")?.value;
    const idUsuario = document.getElementById("idUsuario")?.value;
    const idCategoria = document.getElementById("idCategoria")?.value;
    const idMoneda = document.getElementById("idMoneda")?.value;

    if (!nombreUsuario || !montoRaw || !fecha || !idUsuario || !idCategoria) {
      alert("Completa los campos obligatorios antes de guardar.");
      return;
    }

    const payload = {
      nombreUsuario,
      monto: parseFloat(montoRaw),
      descripcion: descripcion || null,
      fecha,
      idUsuario: idUsuario ? Number(idUsuario) : null,
      idCategoria: idCategoria ? Number(idCategoria) : null,
      idMoneda: idMoneda ? Number(idMoneda) : null
    };

    try {
      const res = await fetch(`${API_BASE_URL}/transacciones`, {
        method: "POST",
        headers: headersAuth(),
        body: JSON.stringify(payload)
      });

      // leer respuesta (texto o json)
      const text = await res.text();
      let body;
      try { body = JSON.parse(text); } catch { body = text; }

      if (res.status === 401) {
        alert("Sesión inválida. Volvé a iniciar sesión.");
        localStorage.removeItem("token");
        window.location.href = "login.html";
        return;
      }
      if (!res.ok) {
        console.error("❌ Error backend crear transacción:", body);
        alert("Error al crear transacción (ver consola)");
        return;
      }

      // éxito
      alert("✅ Transacción creada correctamente");
      const form = document.getElementById("formTransaccion");
      if (form) form.reset();
      await cargarTransacciones();

    } catch (err) {
      console.error("❌ Error en crearTransaccion:", err);
      alert("No se pudo conectar con el servidor.");
    }
  }

  // Calcular gasto entre fechas: soporta respuesta numérica o { total: X } u objeto
  async function calcularGastoEntreFechas(inicio, fin) {
    const cont = document.getElementById("resultadoFechas");
    if (cont) cont.textContent = "Calculando...";

    try {
      const res = await fetch(`${API_BASE_URL}/transacciones/gasto-entre-fechas?inicio=${encodeURIComponent(inicio)}&fin=${encodeURIComponent(fin)}`, {
        headers: headersAuth()
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(`HTTP ${res.status} - ${text}`);
      }

      const data = await res.json(); // backend puede devolver Double o objeto
      let total = extractNumberFrom(data);
      if (total == null) {
        // fallback si devolvió algo raro
        total = (typeof data === "number") ? data : null;
      }

      if (total == null) {
        if (cont) cont.textContent = "No se pudo obtener el total.";
        else alert("No se pudo obtener el total gastado.");
        return;
      }

      const pretty = formatCurrency(total);
      if (cont) {
        cont.innerHTML = `<div style="background:#eef7ff;padding:10px;border-radius:8px;margin-top:8px;">
          <strong>💸 Total gastado:</strong> <span style="font-weight:700">${pretty}</span>
        </div>`;
      } else {
        alert(`Total gastado: ${pretty}`);
      }
    } catch (err) {
      console.error("❌ Error calcularGastoEntreFechas:", err);
      const cont2 = document.getElementById("resultadoFechas");
      if (cont2) cont2.textContent = "Error al calcular el gasto.";
      else alert("Error al calcular el gasto entre fechas.");
    }
  }

  // Gastos por categoría para un usuario (muestra una lista bonita)
  async function gastoPorCategoria(usuarioId) {
    const cont = document.getElementById("resultadoCategoria");
    if (cont) cont.textContent = "Consultando...";

    if (!usuarioId) {
      if (cont) cont.textContent = "Ingresá un ID de usuario.";
      return;
    }

    try {
      const res = await fetch(`${API_BASE_URL}/transacciones/gastos-por-categoria/${encodeURIComponent(usuarioId)}`, {
        headers: headersAuth()
      });
      if (!res.ok) {
        const txt = await res.text();
        throw new Error(`HTTP ${res.status} - ${txt}`);
      }

      const data = await res.json(); // se espera Map<String, Double>
      if (!data || Object.keys(data).length === 0) {
        if (cont) cont.textContent = "No hay gastos para este usuario.";
        return;
      }

      // construir HTML
      const rows = Object.entries(data).map(([cat, tot]) => {
        return `<div style="display:flex;justify-content:space-between;padding:6px 0;border-bottom:1px solid rgba(0,0,0,0.04)">
                  <div>${escapeHtml(cat)}</div>
                  <div style="font-weight:700">${formatCurrency(tot)}</div>
                </div>`;
      }).join("");

      if (cont) cont.innerHTML = `<div style="background:#fff7ea;padding:10px;border-radius:8px;margin-top:8px;">${rows}</div>`;
      else alert("Consulta completada. Revisá la consola.");

    } catch (err) {
      console.error("❌ Error gastoPorCategoria:", err);
      if (cont) cont.textContent = "Error al consultar gastos por categoría.";
      else alert("Error al consultar gastos por categoría.");
    }
  }

  // Small helper to avoid XSS when inyectamos strings
  function escapeHtml(unsafe) {
    if (unsafe == null) return "-";
    return String(unsafe)
      .replaceAll("&", "&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll('"', "&quot;")
      .replaceAll("'", "&#039;");
  }

  // Asignar eventos de formularios
  // crear transacción
  const formCrear = document.getElementById("formTransaccion") || document.getElementById("form-transaccion");
  if (formCrear) formCrear.addEventListener("submit", async (e) => { e.preventDefault(); await crearTransaccion(); });

  // gasto entre fechas (el form que en tu HTML usa action="/api/transacciones/gasto-entre-fechas")
  const formFechas = document.querySelector('form[action="/api/transacciones/gasto-entre-fechas"]') || document.querySelector('form[action="/api/transacciones/gasto-entre-fechas/"]');
  if (formFechas) {
    formFechas.addEventListener("submit", async (e) => {
      e.preventDefault();
      const inicio = document.getElementById("inicio")?.value;
      const fin = document.getElementById("fin")?.value;
      if (!inicio || !fin) { alert("Elegí ambas fechas."); return; }
      await calcularGastoEntreFechas(inicio, fin);
    });
  }

  // gasto por categoria (form con action que empieza con /api/transacciones/gastos-por-categoria)
  const formCat = document.querySelector('form[action^="/api/transacciones/gastos-por-categoria"]');
  if (formCat) {
    formCat.addEventListener("submit", async (e) => {
      e.preventDefault();
      const uid = document.getElementById("usuarioId")?.value;
      await gastoPorCategoria(uid);
    });
  }

  // inicial
  cargarTransacciones();
});
