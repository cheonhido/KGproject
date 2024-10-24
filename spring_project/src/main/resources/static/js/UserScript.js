function IdPwdCheck() {
	var frm = document.loginForm;

	if (!frm.userid.value.trim()) {
		alert("아이디를 입력해주세요");
		frm.userid.value = "";
		frm.userid.focus();
		return false;
	}
	if (!frm.userpwd.value.trim()) {
		alert("비밀번호를 입력해주세요");
		frm.userpwd.value = "";
		frm.userpwd.focus();
		return false;
	}
	frm.submit();
}

function changePicture(id) {
	// 새 창 크기 설정
	var width = 500;  // 너비
	var height = 300;  // 높이

	// 현재 창 크기 가져오기
	var windowWidth = window.innerWidth;
	var windowHeight = window.innerHeight;

	// 창의 위치 계산
	var left = (windowWidth - width) / 2;
	var top = (windowHeight - height) / 2;

	// 새 창 열기
	var features = `width=${width},height=${height},left=${left},top=${top},scrollbars=yes,resizable=yes`;
	window.open("/changePicture?userid=" + id, "", features);
}

function changePicture2(id) {
	// 새 창 크기 설정
	var width = 500;  // 너비
	var height = 300;  // 높이

	// 현재 창 크기 가져오기
	var windowWidth = window.innerWidth;
	var windowHeight = window.innerHeight;

	// 창의 위치 계산
	var left = (windowWidth - width) / 2;
	var top = (windowHeight - height) / 2;

	// 새 창 열기
	var features = `width=${width},height=${height},left=${left},top=${top},scrollbars=yes,resizable=yes`;
	window.open("/changePicture2?userid=" + id, "", features);
}


// 게시물, 저장됨 버튼 처리
const postsBtn = document.getElementById('posts-btn');
const savedBtn = document.getElementById('saved-btn');
const postsIcon = postsBtn.querySelector('.button-icon');
const savedIcon = savedBtn.querySelector('.button-icon');

// 게시물 버튼 클릭 이벤트
postsBtn.addEventListener('click', function() {
	document.querySelector('.posts-container').classList.add('active');
	document.querySelector('.saved-container').classList
		.remove('active');
	this.classList.add('active');
	savedBtn.classList.remove('active');

	// 게시물 버튼 이미지 변경
	postsIcon.src = '/img/menu.png'; // 게시물 버튼 클릭 시 새로운 이미지로 변경
	savedIcon.src = '/img/save2.png'; // 저장됨 버튼의 기존 이미지 유지
});

// 저장됨 버튼 클릭 이벤트
savedBtn.addEventListener('click', function() {
	document.querySelector('.saved-container').classList.add('active');
	document.querySelector('.posts-container').classList
		.remove('active');
	this.classList.add('active');
	postsBtn.classList.remove('active');

	// 저장됨 버튼 이미지 변경
	savedIcon.src = '/img/save4.png'; // 저장됨 버튼 클릭 시 새로운 이미지로 변경
	postsIcon.src = '/img/menu2.png'; // 게시물 버튼의 기존 이미지 유지

	document.querySelectorAll('.openViewButton').forEach(button => {
		button.addEventListener('click', function(event) {
			event.preventDefault(); // 기본 동작 방지

			// data-* 속성으로부터 값들을 가져옴
			const userid = this.getAttribute('data-userid');
			const fileName = this.getAttribute('data-filename');
			const content = this.getAttribute('data-content');
			const seq = this.getAttribute('data-seq');
			const comments = this.getAttribute('data-comments');

			// openView 함수 호출 (팝업이나 새 창으로 열릴 수 있음)
			openView(1200, 800, userid, fileName, content, comments, seq);
		});
	});
});

// openView 함수 호출
function openView(width, height, userid, fileName, content, comments, seq) {
	const screenWidth = window.screen.width;
	const screenHeight = window.screen.height;
	const left = (screenWidth - width) / 2;
	const top = (screenHeight - height) / 2;

	// 새로운 창으로 데이터 전달
	const params = `userid=${encodeURIComponent(userid)}&fileName=${encodeURIComponent(fileName)}&content=${encodeURIComponent(content)}&comments=${encodeURIComponent(comments)}&seq=${encodeURIComponent(seq)}`;
	window.open(`/instaboardView?${params}`, 'openView', `width=${width},height=${height},left=${left},top=${top},scrollbars=yes,resizable=no`);

}

// 프로필 편집 버튼 클릭 시 이벤트 처리
document.getElementById('profileModifyBtn').addEventListener('click', function() {
	// MyPage로 이동하는 부분
	window.location.href = `/UserModifyForm`;
});

// 팔로우/팔로잉 목록 모달제어
const followerLink = document.getElementById('follower-link');
const followLink = document.getElementById('follow-link');
const followerModal = document.getElementById('follower-modal');
const followModal = document.getElementById('follow-modal');
const closeBtn1 = document.querySelector('.close-btn1');
const closeBtn2 = document.querySelector('.close-btn2');

if (followerLink && followerModal && closeBtn1) {
	// 팔로워 링크 클릭 시 모달 창 열기
	followerLink.addEventListener('click', function(event) {
		event.preventDefault();  // 기본 링크 동작 방지
		followerModal.style.display = 'block';  // 모달 창 열기
	});

	// 닫기 버튼 클릭 시 모달 창 닫기
	closeBtn1.addEventListener('click', function() {
		followerModal.style.display = 'none';  // 모달 창 닫기
	});

	// 모달 창 외부 클릭 시 모달 창 닫기
	window.addEventListener('click', function(event) {
		if (event.target === followerModal) {
			followerModal.style.display = 'none';
		}
	});
} else {
	console.error('팔로워 링크 또는 모달 요소를 찾을 수 없습니다.');
}

if (followLink && followModal && closeBtn2) {
	// 팔로워 링크 클릭 시 모달 창 열기
	followLink.addEventListener('click', function(event) {
		event.preventDefault();  // 기본 링크 동작 방지
		followModal.style.display = 'block';  // 모달 창 열기
	});

	// 닫기 버튼 클릭 시 모달 창 닫기
	closeBtn2.addEventListener('click', function() {
		followModal.style.display = 'none';  // 모달 창 닫기
	});

	// 모달 창 외부 클릭 시 모달 창 닫기
	window.addEventListener('click', function(event) {
		if (event.target === followModal) {
			followModal.style.display = 'none';
		}
	});
} else {
	console.error('팔로잉 링크 또는 모달 요소를 찾을 수 없습니다.');
}

// 팔로워 모달 검색 기능 추가
const followerSearchInput = document.getElementById('follower-search');
if (followerSearchInput) {
	followerSearchInput.addEventListener('input', function() {
		const searchText = this.value.toLowerCase(); // 입력값을 소문자로 변환
		const followerItems = document.querySelectorAll('#follower-modal .follower-item');

		followerItems.forEach(item => {
			const username = item.querySelector('.follower-username').textContent.toLowerCase();
			const name = item.querySelector('.follower-name').textContent.toLowerCase();

			// 검색어가 userid 또는 username에 포함되어 있는지 확인
			if (username.includes(searchText) || name.includes(searchText)) {
				item.style.display = 'flex'; // 일치하는 항목 표시
			} else {
				item.style.display = 'none'; // 일치하지 않는 항목 숨김
			}
		});
	});
}

// 팔로잉 모달 검색 기능 추가
const followSearchInput = document.querySelector('#follow-modal .search-container input');
if (followSearchInput) {
	followSearchInput.addEventListener('input', function() {
		const searchText = this.value.toLowerCase(); // 입력값을 소문자로 변환
		const followItems = document.querySelectorAll('#follow-modal .follower-item');

		followItems.forEach(item => {
			const username = item.querySelector('.follower-username').textContent.toLowerCase();
			const name = item.querySelector('.follower-name').textContent.toLowerCase();

			// 검색어가 userid 또는 username에 포함되어 있는지 확인
			if (username.includes(searchText) || name.includes(searchText)) {
				item.style.display = 'flex'; // 일치하는 항목 표시
			} else {
				item.style.display = 'none'; // 일치하지 않는 항목 숨김
			}
		});
	});
}

// 언팔로우 버튼에 이벤트 리스너 추가
document.querySelectorAll('.following-btn').forEach(button => {
	button.addEventListener('click', function() {
		// 팔로워의 userid 가져오기
		const toUser = this.closest('.follower-item').querySelector('.follower-username').textContent.trim();

		// 언팔로우 확인 창 띄우기
		const confirmUnfollow = confirm(`${toUser} 님을 정말 언팔로우하시겠습니까?`);

		if (confirmUnfollow) {
			// Ajax 요청으로 서버에 언팔로우 요청 보내기
			fetch('/unFollowUser', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				body: `to_user=${encodeURIComponent(toUser)}`
			})
				.then(response => {
					if (response.ok) {
						alert('언팔로우가 완료되었습니다.');
						// 언팔로우 성공 시 버튼 상태 변경
						button.textContent = '팔로우';
						button.classList.remove('following-btn');
						button.classList.add('follow-btn');
					} else {
						throw new Error('언팔로우 요청 중 오류가 발생했습니다.');
					}
				})
				.catch(error => {
					console.error('Error:', error);
					alert('언팔로우 요청 중 오류가 발생했습니다.');
				});
		}
	});
});

// openView 함수 호출
document.querySelectorAll('.openViewButton').forEach(button => {
	button.addEventListener('click', function(event) {
		event.preventDefault();

		const userid = this.getAttribute('data-userid');
		const fileName = this.getAttribute('data-filename');
		const content = this.getAttribute('data-content');
		const comments = this.getAttribute('data-comments');
		const seq = this.getAttribute('data-seq');

		openView(1200, 800, userid, fileName, content, comments, seq);
	});
});

function openView(width, height, userid, fileName, content, comments, seq) {
	const screenWidth = window.screen.width;
	const screenHeight = window.screen.height;
	const left = (screenWidth - width) / 2;
	const top = (screenHeight - height) / 2;

	// 파라미터를 URL에 포함하여 전달
	const params = `userid=${encodeURIComponent(userid)}&fileName=${encodeURIComponent(fileName)}&content=${encodeURIComponent(content)}&comments=${encodeURIComponent(comments)}
        				&seq=${encodeURIComponent(seq)}`;
	const newWindow = window.open(`/instaboardView?${params}`, 'openView', `width=${width},height=${height},left=${left},top=${top},scrollbars=yes,resizable=no`);

	newWindow.onload = function() {
		try {
			newWindow.document.body.style.margin = '0';
			newWindow.document.body.style.padding = '0';
			newWindow.document.body.style.overflow = 'hidden';
			newWindow.document.body.style.backgroundColor = '#fff';
			newWindow.document.documentElement.style.height = '100%';
		} catch (e) {
			console.error("Error in loading new window content: ", e);
		}
	};
}