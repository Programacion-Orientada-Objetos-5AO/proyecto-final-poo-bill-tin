    document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const codigoInput = document.getElementById("codigo");
    const nombreInput = document.getElementById("nombre");

    if (!form) return; // seguridad

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const codigo = codigoInput.value.trim();
        const nombre = nombreInput.value.trim();

        if (!codigo || !nombre) {
            alert("Por favor, completá todos los campos.");
            return;
        }

        const monedaData = {
            codigo: codigo.toUpperCase(),
            nombre: nombre
        };

        try {
            const response = await fetch("/api/monedas", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": localStorage.getItem("token") 
                        ? `Bearer ${localStorage.getItem("token")}` 
                        : ""
                },
                body: JSON.stringify(monedaData)
            });

            if (!response.ok) {
                const text = await response.text();
                console.error("Error del servidor:", text);
                alert("Error al guardar la moneda. Verificá los datos o tu sesión.");
                return;
            }

            console.log("Moneda creada correctamente.");
            window.location.href = "moneda-registrada.html";

        } catch (error) {
            console.error("Error de red o backend:", error);
            alert("No se pudo conectar con el servidor.");
        }
    });
});
