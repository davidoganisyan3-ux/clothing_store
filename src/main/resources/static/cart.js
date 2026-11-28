// API_URL, loadCart, saveCart, formatPrice, getSession, clearSession лежат в common.js

const list = document.getElementById("cart-list");
const emptyBox = document.getElementById("cart-empty");
const summary = document.getElementById("cart-summary");
const totalBox = document.getElementById("cart-total");
const checkoutButton = document.getElementById("checkout-button");
const checkoutError = document.getElementById("checkout-error");
const doneBox = document.getElementById("order-done");

// --- Рисуем страницу заново по текущему состоянию корзины ---
function render() {
  const cart = loadCart();
  const isEmpty = cart.length === 0;

  list.replaceChildren();
  emptyBox.hidden = !isEmpty;
  summary.hidden = isEmpty;
  if (isEmpty) return;

  cart.forEach(line => list.appendChild(createLine(line)));

  const total = cart.reduce((sum, line) => sum + Number(line.price) * line.quantity, 0);
  totalBox.textContent = formatPrice(total);
}

function createLine(line) {
  const item = document.createElement("li");
  item.className = "cart-line";

  const info = document.createElement("div");
  info.className = "cart-info";
  const name = document.createElement("strong");
  name.textContent = line.name;
  const size = document.createElement("span");
  size.className = "cart-size";
  size.textContent = `Размер ${line.size}`;
  info.append(name, size);

  const qty = document.createElement("div");
  qty.className = "qty";
  const minus = createQtyButton("−", "Уменьшить количество", () => changeQuantity(line.productSizeId, -1));
  const count = document.createElement("span");
  count.className = "qty-count";
  count.textContent = String(line.quantity);
  const plus = createQtyButton("+", "Увеличить количество", () => changeQuantity(line.productSizeId, +1));
  // Больше, чем на складе, добавить нельзя: кнопка «+» отключается
  if (isAtStockLimit(line)) {
    plus.disabled = true;
    plus.title = "Больше нет на складе";
  }
  qty.append(minus, count, plus);

  const sum = document.createElement("span");
  sum.className = "price";
  sum.textContent = formatPrice(Number(line.price) * line.quantity);

  const remove = document.createElement("button");
  remove.className = "ghost";
  remove.type = "button";
  remove.textContent = "Удалить";
  remove.addEventListener("click", () => removeLine(line.productSizeId));

  item.append(info, qty, sum, remove);
  return item;
}

function createQtyButton(label, ariaLabel, onClick) {
  const button = document.createElement("button");
  button.className = "qty-button";
  button.type = "button";
  button.textContent = label;
  button.setAttribute("aria-label", ariaLabel);
  button.addEventListener("click", onClick);
  return button;
}

// --- Действия: меняем данные, сохраняем, перерисовываем ---
function changeQuantity(productSizeId, delta) {
  const cart = loadCart();
  const line = cart.find(l => l.productSizeId === productSizeId);
  if (!line) return;
  if (delta > 0 && isAtStockLimit(line)) return;

  line.quantity += delta;
  // Если стало 0 и меньше, убираем строку совсем
  saveCart(cart.filter(l => l.quantity > 0));
  render();
}

// У старых записей корзины поля stock может не быть, тогда лимит не применяем
function isAtStockLimit(line) {
  return line.stock !== undefined && line.quantity >= line.stock;
}

function removeLine(productSizeId) {
  saveCart(loadCart().filter(l => l.productSizeId !== productSizeId));
  render();
}

// --- Оформление заказа ---
async function checkout() {
  const session = getSession();
  if (!session) {
    // Не вошёл: отправляем на страницу входа, после входа вернёмся сюда
    location.href = "/login.html?next=/cart.html";
    return;
  }

  const cart = loadCart();
  if (cart.length === 0) return;

  checkoutButton.disabled = true;
  checkoutError.hidden = true;

  try {
    const response = await fetch(`${API_URL}/orders`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + session.token
      },
      body: JSON.stringify({
        userId: session.user.id,
        items: cart.map(line => ({
          productSizeId: line.productSizeId,
          quantity: line.quantity
        }))
      })
    });

    if (response.status === 401 || response.status === 403) {
      // Токен не подошёл (например, истёк): входим заново
      clearSession();
      location.href = "/login.html?next=/cart.html";
      return;
    }
    if (!response.ok) {
      showCheckoutError(await readError(response));
      return;
    }

    saveCart([]);
    showOrderDone();
  } catch (error) {
    console.error(error);
    showCheckoutError("Нет связи с сервером. Проверь, что бэкенд запущен.");
  } finally {
    checkoutButton.disabled = false;
  }
}

// Пробуем достать сообщение об ошибке из ответа бэкенда
async function readError(response) {
  try {
    const data = await response.json();
    if (data.message) return data.message;
  } catch (error) {
    // ответ без JSON, ничего страшного
  }
  return `Не удалось оформить заказ (код ${response.status}).`;
}

function showCheckoutError(text) {
  checkoutError.textContent = text;
  checkoutError.hidden = false;
}

function showOrderDone() {
  list.replaceChildren();
  summary.hidden = true;
  emptyBox.hidden = true;
  doneBox.hidden = false;
}

checkoutButton.addEventListener("click", checkout);

render();
