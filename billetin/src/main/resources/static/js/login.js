document.addEventListener("DOMContentLoaded", () => {
    if (!window.Billetin) return;

    const form = document.querySelector("#loginForm");
    if (!form) return;

    const feedback = form.querySelector(".form-feedback");
    const submitButton = form.querySelector("button[type=\"submit\"]");

    const setFeedback = (message, type = "info") => {
        if (!feedback) return;
        feedback.textContent = message;
        feedback.dataset.type = type;
    };

    const resetFeedback = () => {
        if (!feedback) return;
        feedback.textContent = "";
        delete feedback.dataset.type;
    };

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        resetFeedback();

        const formData = Billetin.readForm(form);
        const payload = {
            username: formData.username?.trim(),
            password: formData.password,
        };

        if (!payload.username || !payload.password) {
            setFeedback("Completa usuario y contrasena.", "error");
            return;
        }

        submitButton.disabled = true;
        submitButton.dataset.loading = "true";

        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            const body = await response.json().catch(() => null);

            if (!response.ok || !body?.token) {
                const detail = body?.detail || body?.message || "Credenciales invalidas.";
                throw new Error(detail);
            }

            const token = body.token;
            const decoded = Billetin.decodeToken(token);

            if (!decoded?.roles?.some((role) => role === "ROLE_ADMIN" || role.endsWith("ADMIN"))) {
                throw new Error("Solo el usuario administrador puede iniciar sesion por el momento.");
            }

            Billetin.setToken(token);
            window.location.href = "transacciones.html";
        } catch (error) {
            Billetin.clearToken();
            setFeedback(error.message || "No se pudo iniciar sesion.", "error");
        } finally {
            submitButton.disabled = false;
            delete submitButton.dataset.loading;
        }
    });
});
