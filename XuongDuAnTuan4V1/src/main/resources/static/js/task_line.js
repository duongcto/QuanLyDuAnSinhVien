setTimeout(() => {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        alert.style.transition = 'opacity 0.5s ease-out';
        alert.style.opacity = '0';
        setTimeout(() => alert.remove(), 500);
    });
}, 4000);




const draggables = document.querySelectorAll('.card');
const columns = document.querySelectorAll('.column');
const boardContainer = document.querySelector('.board-container');

let draggedItem = null; // Biến toàn cục để lưu trữ phần tử đang được kéo
let currentColumnId = null; // Biến để lưu ID cột ban đầu của thẻ khi kéo

// Global Modal Overlay
const globalModalOverlay = document.getElementById('globalModalOverlay');

// Task Card Modal Elements (New main card detail modal)
const taskCardModal = document.getElementById('taskCardModal');
const closeTaskCardModalBtn = document.getElementById('closeTaskCardModalBtn');
const taskTitleInput = document.getElementById('taskTitleInput');
const taskTieuDe = document.getElementById('taskTieuDe');
const taskDescriptionInput = document.getElementById('taskDescriptionInput');
const submitDescriptionButton = document.getElementById('submitDescriptionButton');////////////////////////////

// Buttons inside taskCardModal
const tagBtn = document.getElementById('tagBtn');
const dateBtn = document.getElementById('dateBtn');
const memberBtn = document.getElementById('memberBtn');


// Label Modal Elements
const labelPopupModal = document.getElementById('labelPopupModal');
const closeLabelModalButton = document.getElementById('closeLabelModal');

// Date Modal Elements
const datePopupModal = document.getElementById('datePopupModal');
const closeDateModalButton = document.getElementById('closeDateModal');
const startDateCheckbox = document.getElementById('startDateCheckbox');
const startDateInput = document.getElementById('startDateInput');
const dueDateCheckbox = document.getElementById('dueDateCheckbox');
const dueDateInput = document.getElementById('dueDateInput');


// Member Modal Elements
const memberPopupModal = document.getElementById('memberPopupModal');
const closeMemberModalButton = document.getElementById('closeMemberModal');


// State for selected dates (for the currently opened card)
let currentSelectedStartDate = null;
let currentSelectedDueDate = null;
let currentActiveCard = null; // To store the card element currently being edited

console.log('Script loaded. Initializing drag and drop listeners.');

// Helper to format date as YYYY-MM-DD for input type="date"
function formatDateForInput(date) {
    if (!date) return '';
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}



// Function to update the date and time input fields based on currentSelected dates
function updateDateInputFields() {
    startDateInput.value = formatDateForInput(currentSelectedStartDate);
    dueDateInput.value = formatDateForInput(currentSelectedDueDate);

    // Handle checkbox states
    startDateInput.disabled = !startDateCheckbox.checked;
    dueDateInput.disabled = !dueDateCheckbox.checked;

}
const priorityPopupModal = document.getElementById('priorityPopupModal');
const closePriorityModalButton = document.getElementById('closePriorityModal'); // Updated ID
const priorityItems = document.querySelectorAll('.priority-item'); // Updated class
const priorityBtn = document.getElementById('priorityBtn'); // Nút mở modal ưu tiên

// Event listener for opening priority modal

priorityBtn.addEventListener('click', () => {
    console.log('Priority clicked');
    if (taskCardModal) taskCardModal.classList.add('show');
    if (priorityPopupModal) priorityPopupModal.classList.add('show');
});


// Event listener for closing priority modal
if (closePriorityModalButton) {
    closePriorityModalButton.addEventListener('click', () => {
        if (priorityPopupModal) priorityPopupModal.classList.remove('show');
        if (taskCardModal) taskCardModal.classList.add('show');
    });
}

// Ngăn chặn sự kiện click lan truyền từ priority modal content ra overlay
if (priorityPopupModal) priorityPopupModal.addEventListener('click', (e) => e.stopPropagation());

// Logic để đặt trạng thái "selected" khi mở modal
// (Phần này nằm trong hàm openTaskCardModal trong code gốc, tôi trích ra đây để bạn dễ hình dung)
// Khi openTaskCardModal được gọi:
// const currentPriority = cardElement.dataset.cardPriority;
// priorityItems.forEach(item => {
//     if (item.dataset.priority === currentPriority) {
//         item.classList.add('selected');
//     } else {
//         item.classList.remove('selected');
//     }
// });


// Priority Item Click Listeners
priorityItems.forEach(item => {
    item.addEventListener('click', () => {
        const priorityValue = item.dataset.priority;
        const displayText = item.dataset.displayText;
        const cardId = currentActiveCard?.dataset.cardId;
        const UuTien = document.getElementById("uuTien");

        // Remove 'selected' from all items first
        priorityItems.forEach(pItem => pItem.classList.remove('selected'));
        // Add 'selected' to the clicked item
        item.classList.add('selected');
        UuTien.innerHTML = ""; // Xoá cũ
        UuTien.innerHTML = priorityValue;
        item.classList.add('selected');
        if (priorityValue.endsWith("Trung bình")) {
            UuTien.style.color = "orange";
        } else if (priorityValue.endsWith("Thấp")) {
            UuTien.style.color = "green";
        } else {
            UuTien.style.color = "red";
        }



        if (cardId) {
            fetch('/api/updateCardPriority', { // Bạn cần triển khai API này
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    cardId: cardId,
                    priority: priorityValue
                })
            })
                .then(response => {
                    if (!response.ok) throw new Error("Cập nhật độ ưu tiên thất bại");
                    return response.json();
                })
                .then(data => {
                    showThymeleafStyleSuccess("Cập nhật độ ưu tiên thành công!");
                    if (currentActiveCard) {
                        currentActiveCard.dataset.cardPriority = priorityValue;
                        const modalCardPriorityDisplay = document.getElementById('modalCardPriority');
                        if (modalCardPriorityDisplay) modalCardPriorityDisplay.textContent = displayText;

                        // Update the priority display on the card itself
                        let prioritySpan = currentActiveCard.querySelector('.card-meta .fas.fa-flag + span');
                        let flagIcon = currentActiveCard.querySelector('.card-meta .fas.fa-flag');

                        if (priorityValue && priorityValue !== 'null' && priorityValue !== '') {
                            if (!prioritySpan) {
                                const metaDiv = currentActiveCard.querySelector('.card-meta');
                                const priorityItem = document.createElement('div');
                                priorityItem.classList.add('card-meta-item');
                                priorityItem.innerHTML = `<i class="fas fa-flag"></i> <span></span>`;
                                // Prepend if date exists, otherwise append
                                const dateItem = metaDiv.querySelector('.card-meta-item .far.fa-clock')?.closest('.card-meta-item');
                                if (dateItem) {
                                    metaDiv.insertBefore(priorityItem, dateItem.nextSibling);
                                } else {
                                    metaDiv.appendChild(priorityItem);
                                }
                                prioritySpan = priorityItem.querySelector('span');
                            }
                            prioritySpan.textContent = displayText;
                            // Remove existing priority classes
                            prioritySpan.classList.remove('priority-high', 'priority-very-high', 'priority-medium', 'priority-low');
                            // Add new priority class
                            if (displayText.toLowerCase().includes('cao')) prioritySpan.classList.add('priority-high');
                            else if (displayText.toLowerCase().includes('rất cao')) prioritySpan.classList.add('priority-very-high');
                            else if (displayText.toLowerCase().includes('trung bình')) prioritySpan.classList.add('priority-medium');
                            else if (displayText.toLowerCase().includes('thấp')) prioritySpan.classList.add('priority-low');

                        } else {
                            // If priority is removed, remove the elements
                            if (flagIcon) {
                                flagIcon.closest('.card-meta-item').remove();
                            }
                        }
                    }
                    closePriorityModalButton.click(); // Close priority modal and return to task modal
                })
                .catch(err => {
                    console.error("Lỗi khi cập nhật độ ưu tiên:", err);
                });
        }
    });
});

// Function to close all modals and popups
function closeAllModals() {
    globalModalOverlay.classList.remove('active');
    taskCardModal.classList.remove('show'); // Hide the main task card modal
    labelPopupModal.classList.remove('show');
    datePopupModal.classList.remove('show');
    memberPopupModal.classList.remove('show');
    // Reset description input state when closing the main modal
    taskDescriptionInput.classList.remove('expanded');
    submitDescriptionButton.classList.remove('show');
    window.location.reload()
}

// ------------------------- Xử lý kéo thả THẺ (Card) -------------------------
function makeCardDraggable(card) {
    card.addEventListener('dragstart', (e) => {
        draggedItem = card;
        currentColumnId = card.closest('.column').dataset.columnId;
        setTimeout(() => {
            card.classList.add('dragging');
            card.style.visibility = 'hidden';
        }, 0);
        e.stopPropagation();
        //isDragging = true; // This flag is not really needed if we use setTimeout for click vs drag
    });

    card.addEventListener('dragend', () => {
        if (draggedItem) {
            draggedItem.classList.remove('dragging');
            draggedItem.style.visibility = 'visible';
        }
        draggedItem = null;
        currentColumnId = null;
        // setTimeout(() => { isDragging = false; }, 50);
    });

    card.addEventListener('click', (e) => {
        // Lấy tham chiếu đến phần tử cột cha của thẻ
        const columnElement = card.closest('.column');
        openTaskCardModal(card, columnElement);
    });
}

draggables.forEach(makeCardDraggable); // Apply to existing cards

columns.forEach(column => {
    const cardsContainer = column.querySelector('.cards-container');
    if (!cardsContainer) {
        console.warn('No .cards-container found in column:', column);
        return;
    }

    cardsContainer.addEventListener('dragover', e => {
        e.preventDefault();
        if (draggedItem && draggedItem.classList.contains('card')) {
            const afterElement = getDragAfterElement(cardsContainer, e.clientY, '.card');
            const draggableCard = document.querySelector('.card.dragging');

            if (draggableCard) {
                if (afterElement == null) {
                    cardsContainer.appendChild(draggableCard);
                } else {
                    cardsContainer.insertBefore(draggableCard, afterElement);
                }
            }
        }
    });

    cardsContainer.addEventListener('drop', (e) => {
        if (draggedItem && draggedItem.classList.contains('card')) {
            const cardId = draggedItem.dataset.cardId;
            const newColumnId = column.dataset.columnId;
            const newPosition = Array.from(cardsContainer.children).indexOf(draggedItem);

            fetch('/api/moveCard', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    cardId: cardId,
                    newColumnId: newColumnId,
                    oldColumnId: currentColumnId,
                    newPosition: newPosition
                })
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(err => { throw new Error(err.message || 'Server error'); });
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Success:', data);
                })
                .catch((error) => {
                    console.error('Fetch Error:', error);
                });
        }
    });
});

// ------------------------- Xử lý kéo thả CỘT (Column) -------------------------
columns.forEach(column => {
    column.addEventListener('dragstart', (e) => {
        // Only allow column drag if no card is currently being dragged
        if (!draggedItem || !draggedItem.classList.contains('card')) {
            draggedItem = column;
            setTimeout(() => {
                column.classList.add('dragging');
            }, 0);
            e.dataTransfer.effectAllowed = 'move';
            e.dataTransfer.setData('text/plain', column.dataset.columnId);
            e.stopPropagation();
        } else {
            e.preventDefault(); // Prevent column drag if a card is being dragged
        }
    });

    column.addEventListener('dragend', () => {
        if (draggedItem) {
            draggedItem.classList.remove('dragging');
        }
        draggedItem = null;
    });
});

boardContainer.addEventListener('dragover', (e) => {
    e.preventDefault();
    if (draggedItem && draggedItem.classList.contains('column')) {
        const afterElement = getDragAfterElement(boardContainer, e.clientX, '.column');
        const draggableColumn = document.querySelector('.column.dragging');

        if (draggableColumn) {
            if (afterElement == null) {
                boardContainer.appendChild(draggableColumn);
            } else {
                boardContainer.insertBefore(draggableColumn, afterElement);
            }
        }
    }
});

boardContainer.addEventListener('drop', (e) => {
    if (draggedItem && draggedItem.classList.contains('column')) {
        const columnId = draggedItem.dataset.columnId;
        const newOrder = Array.from(boardContainer.children).map(col => col.dataset.columnId);

        fetch('/api/updateColumnOrder', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newOrder)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message || 'Server error'); });
                }
                return response.json();
            })
            .then(data => {
                console.log('Success:', data);
            })
            .catch((error) => {
                console.error('Fetch Error:', error);
            });
    }
});

// ------------------------- Hàm hỗ trợ chung -------------------------
function getDragAfterElement(container, clientCoordinate, selector) {
    const draggableElements = [...container.querySelectorAll(`${selector}:not(.dragging)`)];

    return draggableElements.reduce((closest, child) => {
        const box = child.getBoundingClientRect();
        const coordinate = (selector === '.card') ? clientCoordinate : clientCoordinate;
        const boxStart = (selector === '.card') ? box.top : selector === '.column' ? box.left : 0;
        const boxSize = (selector === '.card') ? box.height : selector === '.column' ? box.width : 0;

        const offset = coordinate - boxStart - boxSize / 2;

        if (offset < 0 && offset > closest.offset) {
            return { offset: offset, element: child };
        } else {
            return closest;
        }
    }, { offset: -Infinity }).element;
}

// ------------------------- Task Card Modal Functions -------------------------

// Hàm mở modal thẻ công việc và điền dữ liệu
function openTaskCardModal(cardElement, columnElement) {
    currentActiveCard = cardElement;
    const taskId = cardElement.dataset.cardId;
    hienThiThanhVien(taskId);
    hienThiHan(taskId);
    openDoUuTienModal(cardElement);

    taskTieuDe.innerText = columnElement.querySelector('.column-header h2').textContent;

    taskTitleInput.value = cardElement.dataset.cardTitle || '';
    taskDescriptionInput.value = cardElement.dataset.cardDescription || '';

    const startDateStr = cardElement.dataset.cardStartDate;
    if (startDateStr) {
        currentSelectedStartDate = new Date(startDateStr);
        startDateCheckbox.checked = true; // ✅ thêm dòng này
    } else {
        currentSelectedStartDate = null;
        startDateCheckbox.checked = false; // ✅ thêm dòng này
    }


    const dueDateStr = cardElement.dataset.cardDueDate;

    if (dueDateStr) {
        currentSelectedDueDate = new Date(dueDateStr);
        if (!currentSelectedDueDate.getHours() && !currentSelectedDueDate.getMinutes()) {
            currentSelectedDueDate.setHours(18, 43, 0, 0);
        }
    } else {
        currentSelectedDueDate = null;
    }




    updateDateInputFields();


    globalModalOverlay.classList.add('active'); // Show the main overlay
    taskCardModal.classList.add('show'); // Show the task card modal
}

// Event listener cho nút đóng modal thẻ công việc
closeTaskCardModalBtn.addEventListener('click', closeAllModals);

// Xử lý sự kiện focus/click trên ô nhập liệu mô tả trong taskCardModal
taskDescriptionInput.addEventListener('focus', () => {
    taskDescriptionInput.classList.add('expanded');
    submitDescriptionButton.classList.add('show');

});

// Xử lý sự kiện click trên nút gửi mô tả
submitDescriptionButton.addEventListener('click', () => {
    // Here you would save the description

    taskDescriptionInput.classList.remove('expanded');
    submitDescriptionButton.classList.remove('show');
    fetch('/api/updateDescription', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            cardId: currentActiveCard.dataset.cardId,
            description: taskDescriptionInput.value.trim()
        })
    })
        .then(response => {
            if (!response.ok) throw new Error("Cập nhật thất bại");
            return response.json();
        })
        .then(data => {
            // 👉 Lưu message vào localStorage
            showThymeleafStyleSuccess("Cập nhật thành công!");
        })
        .catch(err => {
            localStorage.setItem('errorMessage', err.message || "Lỗi khi cập nhật");

        });


});

// Event listeners for buttons inside taskCardModal to open sub-modals
tagBtn.addEventListener('click', () => {
    taskCardModal.classList.remove('show'); // Hide main card modal
    labelPopupModal.classList.add('show'); // Show label modal
});

/////////////////////////////////////////////////////////////////Thành Viên ////////////////////////////////

memberBtn.addEventListener('click', () => {
    memberPopupModal.classList.add('show');

    const cardId = currentActiveCard.dataset.cardId;

    fetch(`thanh_vien_da_phan_cong/${cardId}`)
        .then(res => res.json())
        .then(userIdList => {
            const checkboxes = document.querySelectorAll('.member-checkbox');

            checkboxes.forEach(cb => {
                const userId = parseInt(cb.value);

                // Đặt trạng thái checked theo danh sách từ server
                cb.checked = userIdList.includes(userId);

                // Gỡ sự kiện cũ trước (nếu có) để tránh trùng
                cb.replaceWith(cb.cloneNode(true));
            });

            // Re-select lại sau khi clone
            document.querySelectorAll('.member-checkbox').forEach(cb => {
                const userId = parseInt(cb.value);

                cb.addEventListener('change', () => {
                    const isChecked = cb.checked;
                    const cardId = currentActiveCard.dataset.cardId;

                    fetch('/them_thanh_vien', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            userId: userId,
                            action: isChecked ? 'add' : 'remove',
                            idCv: cardId
                        })
                    })
                        .then(response => response.json())
                        .then(data => {
                            console.log("✅ Thành viên cập nhật:", data);
                            hienThiThanhVien(cardId);
                        })
                        .catch(err => {
                            console.error("❌ Lỗi:", err);
                        });
                });
            });
        });
});

function hienThiThanhVien(taskId) {
    fetch(`/du_an/thanh_vien_da_phan_cong/${taskId}`)
        .then(res => res.json())
        .then(users => {
            const membersListContainer = document.getElementById("membersListContainer");
            membersListContainer.innerHTML = ""; // Xóa cũ

            users.forEach(user => {
                const avatarDiv = document.createElement('div');
                avatarDiv.className = 'member-avatar initial-avatar';

                const initial = user.hoTen ? user.hoTen.charAt(0).toUpperCase() : '?';

                // 👉 Thêm title vào span để tooltip hoạt động
                avatarDiv.innerHTML = `<span title="${user.emailPt || 'Không có email'}">${initial}</span>`;

                membersListContainer.appendChild(avatarDiv);
            });

        })
        .catch(error => console.error("Lỗi khi lấy danh sách thành viên:", error));
}

function fetchCardDoUuTien(cardId) {
    return fetch(`/api/getCardPriority/${cardId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Không thể lấy dữ liệu độ ưu tiên");
            }
            return response.json();
        })
        .then(data => {
            console.log("Độ ưu tiên hiện tại:", data);
            return data.priority;
        })
        .catch(err => {
            console.error("Lỗi khi lấy độ ưu tiên:", err);
            return null;
        });
}
function openDoUuTienModal(cardElement) {
    currentActiveCard = cardElement;
    const cardId = cardElement?.dataset.cardId;

    const UuTien = document.getElementById("uuTien");



    // Reset trạng thái
    document.querySelectorAll('.priority-item').forEach(item => {
        item.classList.remove('selected');
    });

    if (cardId) {
        fetchCardDoUuTien(cardId).then(priority => {
            if (priority) {
                document.querySelectorAll('.priority-item').forEach(item => {
                    const priorityText = item.dataset.priority;

                    if (item.dataset.priority === priority) {
                        UuTien.innerHTML = ""; // Xoá cũ
                        UuTien.innerHTML = priorityText;
                        item.classList.add('selected');
                        if (priorityText.endsWith("Trung bình")) {
                            UuTien.style.color = "orange";
                        } else if (priorityText.endsWith("Thấp")) {
                            UuTien.style.color = "green";
                        } else {
                            UuTien.style.color = "red";
                        }
                    }

                });
            }
        });
    }
}







////////////////////////////////////////////////////////////////////////////////////////////////////////////
function hienThiHan(taskId) {
    fetch(`/du_an/han_cua_cong_viec/${taskId}`)
        .then(res => res.text())
        .then(han => {
            const endDateDiv = document.getElementById("enddate");
            endDateDiv.innerHTML = ""; // Xoá cũ



            if (han && han.trim() !== "") {
                const hanDiv = document.createElement('div');
                hanDiv.style.fontSize = '15px';
                hanDiv.textContent ='Ngày hết hạn:'+  han;
                hanDiv.style.color = "#100f0f"; // đỏ nhẹ
                hanDiv.style.fontWeight = "bold";
                endDateDiv.appendChild(hanDiv);
            } else {
                endDateDiv.innerHTML = "<em>Không có hạn</em>";
            }
        })
        .catch(error => {
            console.error("Lỗi khi lấy hạn công việc:", error);
        });
}
///////////////////////////////////////////////////////////////// Thông báo ////////////////////////////////
////////////////////////////////////Ngay/////////////////////////////////////////////////////
document.getElementById('saveDateButton').addEventListener('click', () => {
    const startDateCheckbox = document.getElementById('startDateCheckbox');
    const dueDateCheckbox = document.getElementById('dueDateCheckbox');
    const startDateInput = document.getElementById('startDateInput');
    const dueDateInput = document.getElementById('dueDateInput');


    const cardId = currentActiveCard?.dataset.cardId;

    if (!cardId) {
        console.error("Không có ID công việc hiện tại");
        return;
    }

    const payload = {
        cardId: cardId,
        startDate: startDateCheckbox.checked ? startDateInput.value : null,
        dueDate: dueDateCheckbox.checked ? dueDateInput.value : null,
    };


    fetch('/api/save-date', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) throw new Error("Lưu thất bại");
            return res.json();
        })
        .then(data => {
            showThymeleafStyleSuccess("Lưu ngày thành công!");
            datePopupModal.classList.remove('show');
            taskCardModal.classList.add('show');
            hienThiHan(cardId)
        })
        .catch(err => {
            console.error("❌ Lỗi khi lưu:", err);
            showThymeleafStyleSuccess("Lỗi không xác định");
        });
});

////////////////////////////////////////////////////////////////////////////////////////////////

function showThymeleafStyleSuccess(message) {
    const container = document.querySelector('.custom-alert-container');

    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-success slide-in-left';
    alertDiv.setAttribute('role', 'alert');
    alertDiv.innerHTML = `
        <i class="bi bi-check-circle-fill me-2"></i>
        <span>${message}</span>
    `;

    container.appendChild(alertDiv);

    // Tự động ẩn sau 3s
    setTimeout(() => {
        alertDiv.style.opacity = '0';
        setTimeout(() => alertDiv.remove(), 500);
    }, 3000);
}
//////////////////////////////////////////////////////////////////////////////////////////////////////
dateBtn.addEventListener('click', () => {
    taskCardModal.classList.remove('show'); // Hide main card modal
    datePopupModal.classList.add('show'); // Show date modal
    updateDateInputFields(); // Ensure date inputs are updated
});

memberBtn.addEventListener('click', () => {
    // Hide main card modal
    memberPopupModal.classList.add('show'); // Show member modal
});


// Event listener cho nút đóng modal nhãn
closeLabelModalButton.addEventListener('click', () => {
    labelPopupModal.classList.remove('show'); // Ẩn modal nhãn
    taskCardModal.classList.add('show'); // Hiển thị lại modal thẻ công việc
});

// Event listener for closing date modal
closeDateModalButton.addEventListener('click', () => {
    datePopupModal.classList.remove('show'); // Hide date modal
    taskCardModal.classList.add('show'); // Show task card modal again
});

// Event listener for closing member modal
closeMemberModalButton.addEventListener('click', () => {
    memberPopupModal.classList.remove('show'); // Hide member modal
    taskCardModal.classList.add('show'); // Show task card modal again
});


//     // Đóng modal khi nhấp ra ngoài lớp phủ
//     globalModalOverlay.addEventListener('click', (e) => {
//     // Ensure click is directly on the overlay, not on any modal content
//     if (e.target === globalModalOverlay) {
//     closeAllModals();
// }
// });

// Ngăn chặn sự kiện click lan truyền từ modal content ra overlay
taskCardModal.addEventListener('click', (e) => e.stopPropagation());
labelPopupModal.addEventListener('click', (e) => e.stopPropagation());
datePopupModal.addEventListener('click', (e) => e.stopPropagation());
memberPopupModal.addEventListener('click', (e) => e.stopPropagation());


// Date input change listeners to sync with selected dates
startDateInput.addEventListener('change', (event) => {
    currentSelectedStartDate = new Date(event.target.value);
});

dueDateInput.addEventListener('change', (event) => {
    currentSelectedDueDate = new Date(event.target.value);
});



// Checkbox change listeners
startDateCheckbox.addEventListener('change', () => {
    startDateInput.disabled = !startDateCheckbox.checked;
    if (!startDateCheckbox.checked) {
        currentSelectedStartDate = null;
        startDateInput.value = '';
    } else if (!currentSelectedStartDate) {
        currentSelectedStartDate = new Date();
        updateDateInputFields();
    }
});

dueDateCheckbox.addEventListener('change', () => {
    dueDateInput.disabled = !dueDateCheckbox.checked;
    if (!dueDateCheckbox.checked) {
        currentSelectedDueDate = null;
        dueDateInput.value = '';
    } else if (!currentSelectedDueDate) {
        currentSelectedDueDate = new Date();
        currentSelectedDueDate.setHours(18, 43, 0, 0);
        updateDateInputFields();
    }
});

// Thêm chức năng cho nút "Thêm danh sách khác"
const addListButton = document.getElementById('addListButton');
const addListInputContainer = document.getElementById('addListInputContainer');
const listNameInput = document.getElementById('listNameInput'); // Lấy tham chiếu đến input
const addListConfirmButton = document.getElementById('addListConfirmButton'); // Lấy tham chiếu đến nút
const closeAddListInput = document.getElementById('closeAddListInput');

// Hàm kiểm tra và vô hiệu hóa/kích hoạt nút "Thêm danh sách" và thay đổi màu nút "Thêm danh sách khác"
function toggleAddListButton() {
    // Nút thêm danh sách (trong form) sẽ bị vô hiệu hóa nếu giá trị của input là rỗng
    const isInputEmpty = listNameInput.value.trim() === '';
    addListConfirmButton.disabled = isInputEmpty;

    // Thay đổi màu nút "Thêm danh sách khác" dựa trên trạng thái của input
    if (isInputEmpty) {
        addListButton.classList.remove('active-input');
    } else {
        addListButton.classList.add('active-input');
    }
}

addListButton.addEventListener('click', () => {
    addListButton.classList.add('hidden');
    addListInputContainer.classList.remove('hidden');
    listNameInput.focus();
    toggleAddListButton(); // Vô hiệu hóa nút ngay khi hiển thị input nếu nó trống
});

closeAddListInput.addEventListener('click', () => {
    addListButton.classList.remove('hidden');
    addListInputContainer.classList.add('hidden');
    window.location.reload();
    listNameInput.value = ''; // Xóa nội dung input
    toggleAddListButton(); // Đảm bảo nút bị vô hiệu hóa và màu reset sau khi đóng
});

// Lắng nghe sự kiện 'input' trên trường nhập liệu để cập nhật trạng thái nút theo thời gian thực
listNameInput.addEventListener('input', toggleAddListButton);

// Vô hiệu hóa nút và đặt màu ban đầu khi tải trang lần đầu (nếu input trống)
document.addEventListener('DOMContentLoaded', toggleAddListButton);


// ------------------------- Add Card Functionality -------------------------
document.querySelectorAll('.column').forEach(column => {
    const addCardButton = column.querySelector('.add-card-button');
    const addCardInputContainer = column.querySelector('.add-card-input-container');
    const addCardInput = column.querySelector('.add-card-input');
    const addCardConfirmButton = column.querySelector('.add-card-confirm-button');
    const closeCardInputIcon = column.querySelector('.close-card-input-icon');
    const cardsContainer = column.querySelector('.cards-container');

    // Function to toggle the state of the "Add Card" button
    const toggleAddCardConfirmButton = () => {
        addCardConfirmButton.disabled = addCardInput.value.trim() === '';
    };

    // Show input container when "Add Card" button is clicked
    addCardButton.addEventListener('click', () => {
        addCardButton.classList.add('hidden');
        addCardInputContainer.classList.remove('hidden');
        addCardInput.focus();
        toggleAddCardConfirmButton(); // Set initial state of confirm button
    });

    // Hide input container when "X" is clicked
    closeCardInputIcon.addEventListener('click', () => {
        addCardButton.classList.remove('hidden');
        addCardInputContainer.classList.add('hidden');
        addCardInput.value = ''; // Clear input
        toggleAddCardConfirmButton(); // Reset button state
    });

    // Enable/disable "Add Card" confirm button based on input
    addCardInput.addEventListener('input', toggleAddCardConfirmButton);

    // Handle "Add Card" submission
    addCardConfirmButton.addEventListener('click', () => {
        const cardTitle = addCardInput.value.trim();
        const columnId = column.dataset.columnId;

        if (cardTitle) {
            // Simulate API call to add card
            // In a real application, you would send this to your backend
            console.log(`Adding card "${cardTitle}" to column ${columnId}`);

            // For demonstration, create a new card element immediately
            const newCard = document.createElement('div');
            newCard.classList.add('card');
            // Assign a temporary ID for demonstration. In a real app, this would come from the backend.
            newCard.dataset.cardId = `new-card-${Date.now()}`;
            newCard.dataset.cardTitle = cardTitle;
            newCard.dataset.cardDescription = ''; // Default empty description
            newCard.dataset.cardPriority = '';
            newCard.dataset.cardDueDate = '';
            newCard.dataset.cardStartDate = ''; // thiếu dòng này
            newCard.dataset.cardAssignedTo = '';
            newCard.dataset.cardLabels = '';
            newCard.dataset.cardType = '';
            newCard.dataset.cardAttachment = '';
            newCard.draggable = true;

            newCard.innerHTML = `
                    <p class="card-title">${cardTitle}</p>
                    <div class="card-meta"></div>
                `;
            cardsContainer.appendChild(newCard);
            makeCardDraggable(newCard); // Make the newly added card draggable

            // Reset and hide input
            addCardInput.value = '';
            addCardButton.classList.remove('hidden');
            addCardInputContainer.classList.add('hidden');
            toggleAddCardConfirmButton(); // Reset button state
        }
    });
});



