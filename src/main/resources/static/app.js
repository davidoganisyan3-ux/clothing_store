// API_URL, CURRENCY, loadCart, saveCart, formatPrice лежат в common.js
const SIZE_ORDER = ["XS", "S", "M", "L", "XL", "XXL"];

const catalog = document.getElementById("catalog");
const filters = document.getElementById("filters");
const statusBox = document.getElementById("status");
const cartBadge = document.getElementById("cart-badge");

const dialog = document.getElementById("size-dialog");
const dialogTitle = document.getElementById("dialog-title");
const dialogPrice = document.getElementById("dialog-price");
const sizeOptions = document.getElementById("size-options");
const sizeHint = document.getElementById("size-hint");
const addButton = document.getElementById("add-button");
const cancelButton = document.getElementById("cancel-button");

let allProducts = [];            // все товары с сервера
let sizesByProduct = new Map();  // id товара -> список его размеров
let activeCategory = null;       // id выбранной категории, null = показать все
let selectedProduct = null;      // товар, для которого открыто окно
let selectedSize = null;         // выбранный в окне размер

// --- 1. Загружаем товары и размеры ---
async function loadData() {
  try {
    // Два запроса идут одновременно, ждём оба сразу
    const [productsResponse, sizesResponse] = await Promise.all([
      fetch(`${API_URL}/products`),
      fetch(`${API_URL}/product_sizes`)
    ]);
    if (!productsResponse.ok || !sizesResponse.ok) {
      throw new Error("Сервер вернул ошибку");
    }
    allProducts = await productsResponse.json();
    sizesByProduct = groupSizes(await sizesResponse.json());
    renderFilters();
    renderProducts();
  } catch (error) {
    console.error(error);
    showStatus("Не удалось загрузить товары. Проверь, что бэкенд запущен.");
  }
}

// Бэкенд отдаёт все размеры одним списком. Раскладываем их по товарам:
// у каждого товара своя «полка» с его размерами
function groupSizes(sizes) {
  const map = new Map();
  sizes.forEach(item => {
    const productId = item.product.id;
    if (!map.has(productId)) {
      map.set(productId, []);
    }
    map.get(productId).push(item);
  });
  // Сортируем по привычному порядку: S, M, L, XL...
  map.forEach(list => list.sort((a, b) => sizeRank(a.size) - sizeRank(b.size)));
  return map;
}

function sizeRank(size) {
  const index = SIZE_ORDER.indexOf(size);
  return index === -1 ? SIZE_ORDER.length : index;
}

// --- 2. Кнопки категорий ---
function renderFilters() {
  const categories = new Map();
  allProducts.forEach(p => categories.set(p.category.id, p.category.name));

  filters.replaceChildren();
  addFilterButton("Все", null);
  categories.forEach((name, id) => addFilterButton(name, id));
  updateFilterState();
}

function addFilterButton(label, categoryId) {
  const button = document.createElement("button");
  button.className = "chip";
  button.textContent = label;
  button.dataset.category = categoryId === null ? "all" : String(categoryId);
  button.addEventListener("click", () => {
    activeCategory = categoryId;
    updateFilterState();
    renderProducts();
  });
  filters.appendChild(button);
}

function updateFilterState() {
  const active = activeCategory === null ? "all" : String(activeCategory);
  filters.querySelectorAll(".chip").forEach(button => {
    button.setAttribute("aria-pressed", String(button.dataset.category === active));
  });
}

// --- 3. Карточки товаров ---
function renderProducts() {
  const list = activeCategory === null
    ? allProducts
    : allProducts.filter(p => p.category.id === activeCategory);

  catalog.replaceChildren();

  if (list.length === 0) {
    showStatus("В этой категории пока ничего нет.");
    return;
  }
  hideStatus();
  list.forEach(product => catalog.appendChild(createCard(product)));
}

function createCard(product) {
  const card = document.createElement("article");
  card.className = "card";

  const tile = document.createElement("div");
  tile.className = "tile";
  tile.dataset.tone = String(product.category.id % 4);
  const tileLabel = document.createElement("span");
  tileLabel.className = "tile-label";
  tileLabel.textContent = product.category.name;
  tile.appendChild(tileLabel);

  const body = document.createElement("div");
  body.className = "card-body";

  const name = document.createElement("h2");
  name.className = "card-name";
  name.textContent = product.name;

  const desc = document.createElement("p");
  desc.className = "card-desc";
  desc.textContent = product.description;

  const footer = document.createElement("div");
  footer.className = "card-footer";

  const price = document.createElement("span");
  price.className = "price";
  price.textContent = formatPrice(product.price);

  const buy = document.createElement("button");
  buy.className = "buy";
  buy.textContent = "Купить";
  buy.addEventListener("click", () => openSizeDialog(product));

  footer.append(price, buy);
  body.append(name, desc, footer);
  card.append(tile, body);
  return card;
}

// --- 4. Окно выбора размера ---
function openSizeDialog(product) {
  selectedProduct = product;
  selectedSize = null;
  addButton.disabled = true;

  dialogTitle.textContent = product.name;
  dialogPrice.textContent = formatPrice(product.price);
  sizeOptions.replaceChildren();

  const sizes = sizesByProduct.get(product.id) || [];
  if (sizes.length === 0) {
    sizeHint.textContent = "Этого товара пока нет в наличии.";
  } else {
    sizeHint.textContent = "Выбери размер";
    sizes.forEach(item => sizeOptions.appendChild(createSizeButton(item)));
  }

  dialog.showModal();
}

function createSizeButton(item) {
  const button = document.createElement("button");
  button.className = "chip";
  button.type = "button";
  button.textContent = item.size;
  button.setAttribute("aria-pressed", "false");
  // Размера нет на складе, кнопка неактивна
  button.disabled = item.quantity <= 0;

  button.addEventListener("click", () => {
    selectedSize = item;
    sizeOptions.querySelectorAll(".chip").forEach(b => {
      b.setAttribute("aria-pressed", String(b === button));
    });
    sizeHint.textContent = `В наличии: ${item.quantity} шт.`;
    addButton.disabled = false;
  });
  return button;
}

cancelButton.addEventListener("click", () => dialog.close());

addButton.addEventListener("click", () => {
  if (!selectedSize) return;

  const cart = loadCart();
  const existing = cart.find(line => line.productSizeId === selectedSize.id);

  if (existing) {
    if (existing.quantity >= selectedSize.quantity) {
      sizeHint.textContent = "Больше этого размера на складе нет.";
      return;
    }
    existing.quantity++;
    existing.stock = selectedSize.quantity; // обновляем остаток на случай, если он изменился
  } else {
    cart.push({
      productSizeId: selectedSize.id,
      productId: selectedProduct.id,
      name: selectedProduct.name,
      size: selectedSize.size,
      price: selectedProduct.price,
      quantity: 1,
      stock: selectedSize.quantity // сколько всего на складе, нужно корзине для лимита
    });
  }

  saveCart(cart);
  updateCartBadge();
  dialog.close();
});

// --- 5. Счётчик корзины в шапке ---
function updateCartBadge() {
  const total = loadCart().reduce((sum, line) => sum + line.quantity, 0);
  cartBadge.textContent = `Корзина: ${total}`;
}

// --- Вспомогательное ---
function showStatus(text) {
  statusBox.textContent = text;
  statusBox.hidden = false;
}

function hideStatus() {
  statusBox.hidden = true;
}

updateCartBadge();
loadData();
