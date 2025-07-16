
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const wrapper = document.getElementById('mainWrapper');

    if (sidebar && wrapper) {
        sidebar.classList.toggle('active');

        // Chỉ thêm shifted nếu đang là desktop
        if (window.innerWidth > 768) {
            wrapper.classList.toggle('shifted');
        }
    }
}

document.addEventListener('click', function (event) {
    const sidebar = document.getElementById('sidebar');
    const toggleButton = document.querySelector('.sidebar-toggle');
    if (
        window.innerWidth <= 768 &&
        sidebar &&
        !sidebar.contains(event.target) &&
        !toggleButton.contains(event.target)
    ) {
        sidebar.classList.remove('active');
    }
});