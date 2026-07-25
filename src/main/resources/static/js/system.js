function toggleStatus(id) {
    fetch(`/toggle/${id}`, { method: 'POST' })
        .then(response => {
            if (response.ok) return response.text();
            throw new Error('네트워크 응답 실패');
        })
        .then(response => {
            window.location.reload();
        })
        .catch(error => console.error('Error:', error));
}

function timeLaps(id, status, success) {
    fetch(`/solve/timelaps/${id}/${status}/${success}`, { method: 'POST' })
        .then(response => {
            if (response.ok) return response.text();
            throw new Error('네트워크 응답 실패');
        })
        .then(response => {
            window.location.reload();
        })
        .catch(error => console.error('Error:', error));
}

function solveAdd(currentUserName, pid) {
    fetch(`/solve/solveadd/${currentUserName}/${pid}`, { method: 'POST' })
        .then(response => {
            if (response.ok) return response.text();
            throw new Error('네트워크 응답 실패');
        })
        .then(response => {
            window.location.reload();
        })
        .catch(error => console.error('Error:', error));
}