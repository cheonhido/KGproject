function checkUserId() {
    const userIdInput = document.querySelector('input[name="userid"]');
    const checkIcon = document.getElementById('checkIcon');
    
    // 입력값이 비어있으면 아이콘을 숨깁니다
    if (userIdInput.value.trim() === '') {
        checkIcon.style.display = 'none';
        return;
    }
    
    // AJAX 요청을 통해 중복 검사를 수행합니다
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/checkUserId?userid=${encodeURIComponent(userIdInput.value)}`, true);
	
    xhr.onload = function() {
        if (xhr.status ===  200) {
            // 서버에서의 응답에 따라 아이콘을 변경합니다
            if (xhr.responseText === 'true') {
                checkIcon.src = '/image/icons8-x-30.png'; // 사용 불가능 아이콘
                checkIcon.style.display = 'block'; // 아이콘 표시
            } else {
                checkIcon.src = '/image/icons8-check-30.png'; // 사용 가능 아이콘
                checkIcon.style.display = 'block'; // 아이콘 표시
            }
        } else {
            console.error('중복 검사 요청 실패');
        }
    };
    xhr.send();
}

// 페이지가 로드된 후 아이디 입력란에 이벤트 리스너를 추가합니다
document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('input[name="userid"]').addEventListener('input', checkUserId);
});

function WriteCheck() {
	var frm = document.UserWriteForm;

	if (!frm.userid.value.trim()) {
		alert("아이디를 입력해주세요");
		frm.userid.value = "";
		frm.userid.focus();
		return false;
	}
	if (!frm.username.value.trim()) {
		alert("이름을 입력해주세요");
		frm.username.value = "";
		frm.username.focus();
		return false;
	}
	if (!frm.userpwd.value.trim()) {
		alert("비밀번호를 입력해주세요");
		frm.userpwd.value = "";
		frm.userpwd.focus();
		return false;
	}
	if (!frm.email1.value.trim()) {
		alert("이메일을 입력해주세요");
		frm.email1.value = "";
		frm.email1.focus();
		return false;
	}
	frm.submit();
}



