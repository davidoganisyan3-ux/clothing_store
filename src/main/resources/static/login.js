// API_URL, saveSession лежат в common.js

const tabLogin = document.getElementById("tab-login");
const tabRegister = document.getElementById("tab-register");
const loginForm = document.getElementById("login-form");
const registerForm = document.getElementById("register-form");
const errorBox = document.getElementById("form-error");

// --- Переключение вкладок «Вход» / «Регистрация» ---
function showTab(name) {
  const isLogin = name === "login";
  loginForm.hidden = !isLogin;
  registerForm.hidden = isLogin;
  tabLogin.setAttribute("aria-pressed", String(isLogin));
  tabRegister.setAttribute("aria-pressed", String(!isLogin));
  errorBox.hidden = true;
}

tabLogin.addEventListener("click", () => showTab("login"));
tabRegister.addEventListener("click", () => showTab("register"));

// --- Куда вернуться после входа ---
function getNextUrl() {
  const next = new URLSearchParams(location.search).get("next");
  // Принимаем только пути внутри нашего сайта
  return next && next.startsWith("/") && !next.startsWith("//") ? next : "/index.html";
}

// --- Запросы к бэкенду ---
async function sendJson(path, body) {
  return fetch(`${API_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });
}

// Возвращает true, если вошли и сессия сохранена
async function login(email, password) {
  const response = await sendJson("/login", { email, password });
  if (!response.ok) return false;

  const data = await response.json(); // { token, user: { id, name, email, role } }
  saveSession(data.token, data.user);
  return true;
}

// --- Отправка форм ---
loginForm.addEventListener("submit", async event => {
  event.preventDefault(); // не даём браузеру перезагрузить страницу
  const form = new FormData(loginForm);
  const button = loginForm.querySelector("button[type=submit]");

  button.disabled = true;
  errorBox.hidden = true;
  try {
    const ok = await login(form.get("email"), form.get("password"));
    if (ok) {
      location.href = getNextUrl();
    } else {
      showError("Неверный email или пароль.");
    }
  } catch (error) {
    console.error(error);
    showError("Нет связи с сервером. Проверь, что бэкенд запущен.");
  } finally {
    button.disabled = false;
  }
});

registerForm.addEventListener("submit", async event => {
  event.preventDefault();
  const form = new FormData(registerForm);
  const button = registerForm.querySelector("button[type=submit]");
  const email = form.get("email");
  const password = form.get("password");

  button.disabled = true;
  errorBox.hidden = true;
  try {
    const response = await sendJson("/users/register", {
      email,
      name: form.get("name"),
      password
    });
    if (!response.ok) {
      showError("Не удалось зарегистрироваться. Возможно, такой email уже занят.");
      return;
    }
    // Регистрация прошла, сразу входим теми же данными
    const ok = await login(email, password);
    if (ok) {
      location.href = getNextUrl();
    } else {
      showTab("login");
      showError("Аккаунт создан. Теперь войди с этим email и паролем.");
    }
  } catch (error) {
    console.error(error);
    showError("Нет связи с сервером. Проверь, что бэкенд запущен.");
  } finally {
    button.disabled = false;
  }
});

function showError(text) {
  errorBox.textContent = text;
  errorBox.hidden = false;
}
