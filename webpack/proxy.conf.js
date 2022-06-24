function setupProxy({ tls }) {
  const conf = [
    {
      context: ['/inventory', '/catalog/books', '/order'],
      target: `http${tls ? 's' : ''}://localhost:8080/book-store`,
      secure: false,
      changeOrigin: tls,
    },
  ];
  return conf;
}

module.exports = setupProxy;
