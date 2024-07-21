document.addEventListener('DOMContentLoaded', (event) => {
    const typingSound = document.getElementById('typingSound');
    const moveCountElement = document.getElementById('moveCount');
    let moveCount = 0;

    document.addEventListener('keydown', (e) => {
        typingSound.currentTime = 0; // Rewind to the start
        typingSound.play();
    });

    // Functionality of mouse
    const cursor = document.createElement('div');
    cursor.classList.add('cursor');
    document.body.appendChild(cursor);

    let mouseX = 0, mouseY = 0;
    let cursorX = 0, cursorY = 0;
    const speed = 0.1; // The smaller the number, the smoother the transition

    function animate() {
        const distX = mouseX - cursorX;
        const distY = mouseY - cursorY;
        cursorX += distX * speed;
        cursorY += distY * speed;
        cursor.style.left = cursorX + 'px';
        cursor.style.top = cursorY + 'px';
        requestAnimationFrame(animate);
    }

    animate();

    document.addEventListener('mousemove', (e) => {
        mouseX = e.clientX;
        mouseY = e.clientY;
    });

    document.addEventListener('mouseenter', (e) => {
        cursor.style.left = e.clientX + 'px';
        cursor.style.top = e.clientY + 'px';
    });

    document.addEventListener('mouseleave', (e) => {
        cursor.style.left = '-50px';
        cursor.style.top = '-50px';
    });

    // Magic Square Functionality
    const cells = document.querySelectorAll('td');
    let selectedCell = null;

    cells.forEach(cell => {
        cell.addEventListener('click', function() {
            const [_, row, col] = this.id.split('-').map(Number);
            if (selectedCell) {
                // If an adjacent cell is clicked, perform the swap
                if (this.classList.contains('adjacent')) {
                    swapCells(selectedCell, this);
                    moveCount++;
                    moveCountElement.textContent = moveCount;
                    selectedCell = null;
                } else {
                    // If a non-adjacent cell is clicked, reset selection
                    clearSelection();
                    selectCell(this, row, col);
                }
            } else {
                // No cell is selected, select the current cell
                selectCell(this, row, col);
            }
        });
    });

    function selectCell(cell, row, col) {
        clearSelection();
        selectedCell = cell;
        cell.classList.add('selected');

        const directions = [
            [0, -1], // left
            [0, 1],  // right
            [-1, 0], // up
            [1, 0]   // down
        ];

        directions.forEach(([dRow, dCol]) => {
            const newRow = row + dRow;
            const newCol = col + dCol;
            const adjacentCell = document.getElementById(`cell-${newRow}-${newCol}`);
            if (adjacentCell) {
                adjacentCell.classList.add('adjacent');
            }
        });
    }

    function clearSelection() {
        const cells = document.querySelectorAll('td');
        cells.forEach(c => {
            c.classList.remove('selected', 'adjacent');
        });
        selectedCell = null;
    }

    function swapCells(cell1, cell2) {
        // Perform client-side swap for immediate feedback
        const temp = cell1.textContent;
        cell1.textContent = cell2.textContent;
        cell2.textContent = temp;

        // Clear selection
        clearSelection();

        validateMagicSquare();
    }

    // Validate Magic Square to see if the correct one has been reconstructed by the player
    function validateMagicSquare() {
        fetch('/validate', {
            method: 'POST'
        })
            .then(response => response.text())
            .then(html => {
                document.getElementById('validationResult').innerHTML = html;
            });
    }

    // Page transition logic
    document.querySelectorAll('a').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const targetUrl = this.href;
            document.body.classList.add('slide-up');
            setTimeout(() => {
                window.location.href = targetUrl;
            }, 500); // Match the duration of the CSS transition
        });
    });

    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function (e) {
            document.body.classList.add('slide-up');
        });
    });

    // Ensure the body does not have the slide-up class on page load and apply fade-in effect
    setTimeout(() => {
        document.body.classList.add('fade-in');
    }, 0);
});