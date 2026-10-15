<footer class="container py-4 text-center">Employee Performance Management System · Secure, simple performance reviews</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
  (function () {
    const root = document.documentElement;
    const saved = localStorage.getItem('epms-theme');
    if (saved) root.dataset.theme = saved;
    document.querySelectorAll('[data-theme-toggle]').forEach(function (button) {
      button.addEventListener('click', function () {
        const next = root.dataset.theme === 'dark' ? 'light' : 'dark';
        root.dataset.theme = next;
        localStorage.setItem('epms-theme', next);
      });
    });
    document.querySelectorAll('[data-confirm]').forEach(function (form) {
      form.addEventListener('submit', function (event) {
        if (!window.confirm(form.dataset.confirm)) event.preventDefault();
      });
    });
    document.querySelectorAll('[data-table-filter]').forEach(function (input) {
      input.addEventListener('input', function () {
        const query = input.value.toLowerCase().trim();
        document.querySelectorAll(input.dataset.tableFilter + ' tbody tr').forEach(function (row) {
          row.hidden = query && !row.textContent.toLowerCase().includes(query);
        });
      });
    });
  }());
</script>
