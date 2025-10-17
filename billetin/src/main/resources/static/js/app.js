(() => {
    const STORAGE_KEY = "billetin.token";
    const isBrowser = typeof window !== "undefined";

    const base64UrlDecode = (value) => {
        const normalized = value.replace(/-/g, "+").replace(/_/g, "/");
        const padded = normalized.padEnd(normalized.length + (4 - (normalized.length % 4)) % 4, "=");
        const decoded = atob(padded);
        try {
            return decodeURIComponent(
                decoded
                    .split("")
                    .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
                    .join("")
            );
        } catch {
            return decoded;
        }
    };

    const decodeToken = (token) => {
        try {
            const [, payload] = token.split(".");
            if (!payload) return null;
            return JSON.parse(base64UrlDecode(payload));
        } catch {
            return null;
        }
    };

    const getToken = () => (isBrowser ? localStorage.getItem(STORAGE_KEY) : null);
    const setToken = (token) => {
        if (isBrowser) localStorage.setItem(STORAGE_KEY, token);
    };
    const clearToken = () => {
        if (isBrowser) localStorage.removeItem(STORAGE_KEY);
    };

    const ensureTokenIsValid = (token) => {
        if (!token) return null;
        const decoded = decodeToken(token);
        if (!decoded) {
            clearToken();
            return null;
        }
        const now = Math.floor(Date.now() / 1000);
        if (decoded.exp && decoded.exp < now) {
            clearToken();
            return null;
        }
        return decoded;
    };

    const getSession = () => ensureTokenIsValid(getToken());

    const redirectToLogin = () => {
        if (!isBrowser) return;
        const current = window.location.pathname.split("/").pop();
        if (current !== "login.html") {
            window.location.href = "login.html";
        }
    };

    const logout = () => {
        clearToken();
        redirectToLogin();
    };

    const requireAuth = ({ role } = {}) => {
        const payload = getSession();
        if (!payload) {
            redirectToLogin();
            throw new Error("Sesion expirada o inexistente");
        }
        if (role) {
            const expected = role.startsWith("ROLE_") ? role : `ROLE_${role}`;
            const roles = payload.roles || [];
            const match = roles.some((r) => r === expected || r.endsWith(role));
            if (!match) {
                throw new Error("No tienes permisos para acceder a este modulo");
            }
        }
        return payload;
    };

    const apiFetch = async (url, options = {}) => {
        const token = getToken();
        const headers = new Headers(options.headers || {});

        if (options.body && !(options.body instanceof FormData)) {
            headers.set("Content-Type", "application/json");
        }

        if (token) {
            headers.set("Authorization", `Bearer ${token}`);
        }

        const response = await fetch(url, {
            ...options,
            headers,
        });

        if (response.status === 401) {
            clearToken();
            redirectToLogin();
            throw new Error("Sesion expirada. Ingresa nuevamente.");
        }

        let parsedBody = null;
        const contentType = response.headers.get("content-type") || "";
        if (contentType.includes("application/json")) {
            parsedBody = await response.json().catch(() => null);
        } else if (contentType.includes("text")) {
            parsedBody = await response.text().catch(() => null);
        }

        if (!response.ok) {
            const detail =
                (parsedBody && (parsedBody.detail || parsedBody.message)) ||
                response.statusText ||
                "Error en la solicitud";
            const error = new Error(detail);
            error.status = response.status;
            error.body = parsedBody;
            throw error;
        }

        return parsedBody;
    };

    const formatCurrency = (value, currency = "ARS") => {
        if (value == null || Number.isNaN(Number(value))) return "-";
        return new Intl.NumberFormat("es-AR", {
            style: "currency",
            currency,
            minimumFractionDigits: 2,
        }).format(Number(value));
    };

    const formatDate = (value) => {
        if (!value) return "-";
        const date = typeof value === "string" ? new Date(value) : value;
        if (Number.isNaN(date.getTime())) return "-";
        return date.toLocaleDateString("es-AR");
    };

    const readForm = (form) => {
        const data = new FormData(form);
        return Object.fromEntries(data.entries());
    };

    const handleLogoutClick = (event) => {
        event.preventDefault();
        logout();
    };

    const updateAuthLink = () => {
        if (!isBrowser) return;
        const authLink = document.querySelector("[data-auth-link]");
        if (!authLink) return;

        const session = getSession();
        if (session) {
            authLink.textContent = "Cerrar sesion";
            authLink.href = "#";
            authLink.dataset.state = "authenticated";
            if (!authLink.dataset.logoutBound) {
                authLink.addEventListener("click", handleLogoutClick);
                authLink.dataset.logoutBound = "true";
            }
        } else {
            if (authLink.dataset.logoutBound) {
                authLink.removeEventListener("click", handleLogoutClick);
                delete authLink.dataset.logoutBound;
            }
            authLink.textContent = "Iniciar sesion";
            authLink.href = "login.html";
            authLink.dataset.state = "guest";
        }
    };

    if (isBrowser) {
        document.addEventListener("DOMContentLoaded", () => {
            updateAuthLink();
            const logoutButtons = document.querySelectorAll("[data-action=\"logout\"]");
            logoutButtons.forEach((btn) => {
                if (btn.dataset.enhanced) return;
                btn.addEventListener("click", (event) => {
                    event.preventDefault();
                    logout();
                });
                btn.dataset.enhanced = "true";
            });
        });

        window.addEventListener("storage", (event) => {
            if (event.key === STORAGE_KEY) {
                updateAuthLink();
            }
        });
    }

    window.Billetin = {
        getToken,
        setToken,
        clearToken,
        decodeToken,
        getSession,
        requireAuth,
        apiFetch,
        logout,
        formatCurrency,
        formatDate,
        readForm,
    };
})();
