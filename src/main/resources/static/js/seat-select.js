document.addEventListener('DOMContentLoaded', function () {
    var seatMap = document.getElementById('seatMap');
    if (!seatMap) {
        return;
    }

    var price = parseFloat(seatMap.getAttribute('data-price')) || 0;
    var selected = new Map(); // seatId -> seatLabel

    var totalEl = document.getElementById('totalAmount');
    var countEl = document.getElementById('selectedCount');
    var seatsListEl = document.getElementById('selectedSeatsList');
    var submitBtn = document.getElementById('bookBtn');
    var hiddenInputsContainer = document.getElementById('seatIdsContainer');

    function formatCurrency(value) {
        return value.toLocaleString('vi-VN') + 'đ';
    }

    function refresh() {
        var count = selected.size;
        if (totalEl) {
            totalEl.textContent = formatCurrency(count * price);
        }
        if (countEl) {
            countEl.textContent = count;
        }
        if (submitBtn) {
            submitBtn.disabled = count === 0;
        }

        if (seatsListEl) {
            if (count === 0) {
                seatsListEl.innerHTML = '<span class="text-muted small fst-italic">Chưa chọn ghế nào</span>';
            } else {
                var badges = [];
                selected.forEach(function (label) {
                    badges.push('<span class="seat-badge-pill">' + label + '</span>');
                });
                seatsListEl.innerHTML = badges.join(' ');
            }
        }

        if (hiddenInputsContainer) {
            hiddenInputsContainer.innerHTML = '';
            selected.forEach(function (_, seatId) {
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'seatIds';
                input.value = seatId;
                hiddenInputsContainer.appendChild(input);
            });
        }
    }

    seatMap.querySelectorAll('.seat').forEach(function (seatEl) {
        if (seatEl.classList.contains('booked')) {
            return;
        }
        seatEl.addEventListener('click', function () {
            var seatId = seatEl.getAttribute('data-seat-id');
            var seatLabel = seatEl.getAttribute('data-seat-label') || seatEl.textContent.trim();
            if (selected.has(seatId)) {
                selected.delete(seatId);
                seatEl.classList.remove('selected');
            } else {
                selected.set(seatId, seatLabel);
                seatEl.classList.add('selected');
            }
            refresh();
        });
    });

    refresh();
});
