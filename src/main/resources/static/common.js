// Общий код для всех страниц. Подключается в HTML первым.

const API_URL = "http://localhost:8080";
const CURRENCY = "₽";
const CART_KEY = "cart";
const SESSION_KEY = "session";

// --- Корзина ---
function loadCart() {
  try {
    return JSON.parse(localStorage.getItem(CART_KEY)) || [];
  } catch (error) {
    return [];
  }
}

function saveCart(cart) {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
}

function formatPrice(value) {
  return Number(value).toLocaleString("ru-RU") + " " + CURRENCY;
}

// --- Сессия: токен и данные пользователя после входа ---
function getSession() {
  try {
    const session = JSON.parse(localStorage.getItem(SESSION_KEY));
    return session && session.token && session.user ? session : null;
  } catch (error) {
    return null;
  }
}

function saveSession(token, user) {
  localStorage.setItem(SESSION_KEY, JSON.stringify({ token, user }));
}

function clearSession() {
  localStorage.removeItem(SESSION_KEY);
}

// --- Блок «Войти» / «Имя · Выйти» в шапке ---
function renderAuthNav() {
  const box = document.getElementById("auth-nav");
  if (!box) return;
  box.replaceChildren();

  const session = getSession();
  if (!session) {
    const link = document.createElement("a");
    link.href = "/login.html";
    link.textContent = "Войти";
    box.appendChild(link);
    return;
  }

  const hello = document.createElement("span");
  hello.className = "hello";
  hello.textContent = session.user.name;

  const logout = document.createElement("button");
  logout.className = "link-button";
  logout.type = "button";
  logout.textContent = "Выйти";
  logout.addEventListener("click", () => {
    clearSession();
    renderAuthNav();
  });

  box.append(hello, logout);
}

renderAuthNav();
