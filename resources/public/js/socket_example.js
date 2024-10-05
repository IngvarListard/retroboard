const socket = new WebSocket('ws://localhost:4000');

// Функция для отправки сообщения
function sendMessage(message) {
    socket.send(message);
}

// Устанавливаем интервал для проверки состояния соединения
const intervalId = setInterval(() => {
  sendMessage('Hello, server!');
}, 1000); // Отправляем сообщение каждую секунду

// Очищаем интервал, когда соединение открыто
socket.onopen = function() {
  console.log('WebSocket connection established');
  // Можно также отправить сообщение сразу после открытия соединения
  sendMessage('Initial message to server');
};

// Очищаем интервал, когда соединение закрыто
socket.onclose = function() {
  console.log('WebSocket connection closed');
  clearInterval(intervalId);
};