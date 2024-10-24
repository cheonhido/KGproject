
// 모든 a 요소를 가져옵니다.
const menuItems = document.querySelectorAll('.left-section a');
// 모든 슬라이드 아웃 섹션을 가져옵니다.
const slideOuts = document.querySelectorAll('.slide-out');
// 왼쪽 탐색 섹션
const leftSection = document.querySelector('.left-section');

// 새로고침
const logoButton = document.getElementById('logoButton');
const homeButton = document.getElementById('homeButton');

// 페이지 별로 window 이벤트 사용하기 위한 변수 설정
const pageType = document.body.dataset.page;

// 로고를 클릭했을 때 페이지를 새로고침
logoButton.addEventListener('click', function(event) {
	event.preventDefault();
	location.reload();
});
// 홈 버튼 클릭 시 페이지 새로고침
homeButton.addEventListener('click', function(event) {
	event.preventDefault(); // 기본 동작 방지
	location.reload(); // 페이지 새로고침
});

// 각 a 요소에 클릭 이벤트를 추가합니다.
menuItems.forEach(item => {
	item.addEventListener('click', function(event) {
		event.preventDefault(); // 링크 기본 동작 방지

		// 모든 슬라이드 아웃 섹션에서 'active' 클래스를 제거하고 display를 none으로 설정
		slideOuts.forEach(slide => {
			slide.classList.remove('active');
		});

		// 왼쪽 탐색 섹션을 줄이고, 클릭된 항목의 이미지만 보이도록 설정합니다.
		// leftSection.classList.add('collapsed');

		// 클릭된 항목의 슬라이드 아웃 섹션을 활성화합니다.
		const slideId = this.getAttribute('data-slide');
		const slideOut = document.getElementById(slideId);
		slideOut.classList.add('active');
	});
});

// 클릭된 항목이 외부를 클릭하면 원래 상태로 되돌립니다.
document.addEventListener('click', function(event) {
	if (!event.target.closest('.left-section')) {
		slideOuts.forEach(slide => {
			slide.classList.remove('active');
		});

		// 왼쪽 탐색 섹션을 원래 상태로 되돌립니다.
		leftSection.classList.remove('collapsed');
	}
});

document.querySelectorAll(".followBtn_1").forEach(function(button) {
	button.addEventListener("click", function() {
		var toUserValue = this.closest("tr").querySelector("[data-userid]").getAttribute("data-userid");
		document.getElementById("hidden_to_user").value = toUserValue;

		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/follow", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		xhr.onreadystatechange = function() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				if (xhr.status === 200) {
					button.value = "팔로잉";
					button.disabled = true;
					button.style.color = "#212121";
				} else {
					alert("팔로우 요청 중 오류가 발생했습니다.");
				}
			}
		};

		xhr.send("to_user=" + encodeURIComponent(toUserValue));
	});
});


// 모든 "followBtn" 클래스를 가진 버튼에 대해 클릭 이벤트 리스너를 추가
document.querySelectorAll(".followBtn_2").forEach(function(button) {
	button.addEventListener("click", function() {
		// 클릭된 버튼과 연결된 form 요소를 찾음
		var form = button.closest("form");
		// form 내에 있는 fromUser 값을 가져옴
		var fromUserValue = form.querySelector("#fromUser").value;

		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/followResponse", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		xhr.onreadystatechange = function() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				if (xhr.status === 200) {
					// 서버 응답이 성공적일 때 버튼의 value를 "팔로우 수락함"으로 변경
					button.value = "팔로잉";
					button.disabled = true; // 버튼을 비활성화 (선택 사항)
					button.style.color = "#212121";
				} else {
					// 서버에서 오류가 발생한 경우 경고 메시지 표시
					alert("팔로우 요청 중 오류가 발생했습니다.");
				}
			}
		};

		// 폼 데이터를 URL 인코딩하여 전송
		xhr.send("from_user=" + encodeURIComponent(fromUserValue));
	});
});
// JavaScript로 슬라이드 패널 토글
document.querySelectorAll('.left-section a').forEach(link => {
	link.addEventListener('click', function(e) {
		e.preventDefault();

		// 모든 슬라이드 아웃 패널을 비활성화
		document.querySelectorAll('.slide-out').forEach(panel => {
			panel.classList.remove('active');
		});

		const slideIdForMiddle = this.getAttribute('data-slide');

		if (slideIdForMiddle === 'slide-4') {

			const feedSection = document.querySelector('.feed-section');
			// AJAX 요청으로 MyPage.html 가져오기
			fetch('/instaReels')
				.then(response => response.text())
				.then(html => {
					feedSection.innerHTML = html;

					// body 태그의 data-page 속성을 "instaReels"로 변경
					document.body.setAttribute('data-page', 'instaReels');

					reelsPageEventHandler();

				})

				.catch(error => console.error('Error loading the message form:', error));
		}

		// 클릭한 링크의 데이터 속성에 따라 슬라이드 아웃 패널을 활성화
		const slideId = this.getAttribute('data-slide');
		const slidePanel = document.getElementById(slideId);
		if (slidePanel) {
			slidePanel.classList.add('active');

			// 만약 메시지 섹션(slide-5)이라면 중앙패널에 sendMessageForm.html을 로드
			if (slideId === 'slide-5') {
				const feedSection = document.querySelector('.feed-section');

				// AJAX 요청으로 sendMessageForm.html 가져오기
				fetch('/sendMessageForm')
					.then(response => response.text())
					.then(data => {
						feedSection.innerHTML = data; // 중앙 패널에 HTML 삽입

						// 친구 리스트 데이터를 추가로 가져오기
						return fetch('/getFriendList');
					})
					.then(response => response.json())
					.then(friendList => {

						// sendMessageForm 로드 후 이벤트 핸들러 바인딩
						initializeSendMessageForm(friendList);
					})
					.catch(error => console.error('Error loading the message form:', error));
			}
			// 만약 메시지 섹션(slide-8)이라면 중앙패널에 MyPage.html로 이동
			if (slideId === 'slide-8') {
				const feedSection = document.querySelector('.feed-section');
				const pageidElement = document.getElementById('toUser');
				if (pageidElement) {
					const pageid = pageidElement.value;
					const encodedPageid = encodeURIComponent(pageid);

					// MyPage로 이동하는 부분
					window.location.href = `/MyPage?pageid=${encodedPageid}`;
				}
			}
		}

		// 더 보기 클릭 시 로그아웃 패널 토글
		if (slideId === 'slide-more') {
			const logoutPanel = document.getElementById('logout-panel');
			if (logoutPanel.classList.contains('active')) {
				logoutPanel.classList.remove('active');
			} else {
				logoutPanel.classList.add('active');
			}
		}

		// 탐색 탭 클릭 시
		if (slideId === 'slide-3') {
			const feedSection = document.querySelector('.feed-section');

			fetch(`/discoveryPage`)
				.then(response => response.text())  // HTML 데이터를 텍스트로 받음
				.then(html => {
					feedSection.innerHTML = html;  // discoveryPage의 HTML을 중앙 패널에 로드
				})
		}
	});
});

// SendMessageForm 이벤트 핸들러 설정 함수
function initializeSendMessageForm(friendList) {
	// sendMessageForm의 버튼 클릭 시 sendMessage.html을 로드            
	const toUserInput = document.getElementById('toUserInput');
	const suggestionList = document.getElementById('suggestionList');

	if (!toUserInput || !suggestionList) {
		console.error('toUserInput or suggestionList not found');
		return;
	}

	toUserInput.addEventListener('input', function() {
		const inputValue = toUserInput.value.toLowerCase();
		suggestionList.innerHTML = ''; // 기존 리스트 초기화

		if (inputValue) {
			const filteredFriends = friendList.filter(friend =>
				friend.username.toLowerCase().includes(inputValue) ||
				friend.userid.toLowerCase().includes(inputValue) ||
				friend.pfimage.toLowerCase().includes(inputValue)
			);

			filteredFriends.forEach(friend => {
				const listItem = document.createElement('li');
				// 프로필 이미지, 이름, 아이디를 포함한 HTML 구조
				listItem.innerHTML = `
                          <div style="display: flex; align-items: center;">
                              <img src="/storage/${friend.pfimage}" alt="${friend.username}" style="width: 40px; height: 40px; border-radius: 50%; margin-right: 10px;">
                              <div>
                                  <div style="font-weight: bold;">${friend.username}</div>
                                  <div style="color: #888;">${friend.userid}</div>
                              </div>
                          </div>
                      `;

				listItem.addEventListener('click', () => {
					toUserInput.value = friend.userid; // 선택된 이름을 입력 필드에 채우기
					suggestionList.innerHTML = ''; // 리스트 초기화
				});
				suggestionList.appendChild(listItem);
			});
		}
	});

	if (sendButton && toUserInput) {
		sendButton.addEventListener('click', function(e) {
			e.preventDefault();  // 기본 폼 제출 방지

			const toUser = toUserInput.value; // 입력된 사용자 가져오기
			const encodedToUser = encodeURIComponent(toUser);  // 사용자를 URL 인코딩
			const feedSection = document.querySelector('.feed-section');

			// GET 요청으로 sendMessage.html 가져오기, 인코딩된 사용자명을 쿼리 파라미터로 전달
			fetch(`/sendMessage?to_user=${encodedToUser}`)
				.then(response => response.text())
				.then(data => {
					feedSection.innerHTML = data; // 중앙 패널에 HTML 삽입
					// 필요하다면 여기에 새로운 HTML에 대한 이벤트 핸들러를 초기화   
					initializeSendMessage();
				})
				.catch(error => console.error('Error loading the message page:', error));
		});
	} else {
		console.error('sendButton or toUserInput not found in the DOM');
	}
}

// 로그아웃 패널 외부 클릭 시 패널 숨기기
document.addEventListener('click', function(e) {
	const logoutPanel = document.getElementById('logout-panel');
	const moreItem = document.querySelector('.more-item a');

	// 로그아웃 패널이 열려 있고, 클릭한 위치가 패널 외부인 경우
	if (logoutPanel.classList.contains('active') && !logoutPanel.contains(e.target) && !moreItem.contains(e.target)) {
		logoutPanel.classList.remove('active');
	}
});

// 만들기 동작 (파일 업로드)
function uploadFile(width, height) {
	document.body.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
	const screenWidth = window.screen.width;
	const screenHeight = window.screen.height;
	const left = (screenWidth - width) / 2;
	const top = (screenHeight - height) / 2;

	const newWindow = window.open('/writeBoardForm', 'uploadFile', `width=${width},height=${height},left=${left},top=${top},scrollbars=yes,resizable=no`);

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

	document.getElementById('modalBackground').style.display = 'block';
	document.getElementById('modalBackground').onclick = function() {
		document.body.style.backgroundColor = '#fafafa';
		newWindow.close();
		this.style.display = 'none';
	};
}

// 상세보기 함수
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

// 좋아요 toggle 함수 호출
document.querySelectorAll('.heart-button').forEach((heartButton) => {
	if (heartButton) { // heartButton이 null이 아닐 때만 이벤트 리스너 등록
		heartButton.addEventListener('click', () => {
			const postSeq = heartButton.dataset.postSeq;
			const isLiked = heartButton.dataset.isLiked === 'true'; // 데이터 속성에서 isLiked 값을 가져옴

			likePost(postSeq, isLiked);
		});
	}
});

function likePost(postSeq, isLiked) {
	const url = isLiked ? '/unlikePost' : '/likePost';

	fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded'
		},
		body: new URLSearchParams({
			post_seq: postSeq
		})
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === 'success') {
				// 좋아요 개수 업데이트
				//console.log(document.getElementById('likesCount-' + postSeq));
				document.getElementById('likesCount-' + postSeq).innerText = data.likeCount;

				// 좋아요 버튼 이미지 업데이트
				const heartImage = document.getElementById('heart1Image-' + postSeq);
				heartImage.src = isLiked ? 'img/heart1.png' : 'img/heart2.png';

				// isLiked 값 토글 (새로운 상태를 버튼의 dataset에 반영)
				document.querySelector(`#heart1Button-${postSeq}`).dataset.isLiked = !isLiked;
			}
		})
		.catch(error => {
			console.error('Error:', error);
		});
}

// 게시글 저장하기 버튼 이벤트 처리
document.querySelectorAll('.save-button').forEach((saveButton) => {
	saveButton.addEventListener('click', function() {
		const postSeq = this.dataset.postSeq;  // 게시글 번호
		const isSaved = this.dataset.isSaved === 'true';  // 현재 저장 상태 확인

		// 서버에 저장 또는 저장 해제를 요청
		toggleSave(postSeq, isSaved, this);  // 버튼 자체를 넘김
	});
});

function toggleSave(postSeq, isSaved, button) {
	// 저장과 저장 해제를 모두 처리하는 API 호출
	fetch(`/toggleSavePost/${postSeq}`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded'
		},
		body: new URLSearchParams({
			post_seq: postSeq
		})
	})
		.then(response => response.json())
		.then(data => {
			//console.log(data === '1');
			if (data.saved) {
				// 서버에서 저장된 상태로 돌아오면 UI 업데이트
				const saveImage = document.getElementById('saveImage-' + postSeq);
				saveImage.src = isSaved ? 'img/save.png' : 'img/save5.png';
				document.querySelector(`#save1Button-${postSeq}`).dataset.isSaved = !isSaved;
			}
		})
		.catch(error => {
			console.error('Error:', error);
			// 에러 발생 시 사용자에게 알림 (선택 사항)
			alert('저장 상태를 변경하는 중 오류가 발생했습니다.');
		});
}

// 페이지 로드시 게시글 저장 상태 적용
if (pageType === 'mainForm') {
	window.addEventListener('load', function() {
		fetch('/savedPosts')
			.then(response => response.json())
			.then(savedPostIds => {
				savedPostIds.forEach(postSeq => {
					const saveButton = document.querySelector(`.save-button[data-post-seq="${postSeq}"]`);
					if (saveButton && saveButton.dataset.isSaved !== 'true') {
						const saveImage = document.getElementById('saveImage-' + postSeq);
						saveImage.src = 'img/save5.png';  // 저장된 상태로 이미지 변경
						saveButton.dataset.isSaved = 'true';  // 저장 상태 업데이트
					}
				});
			})
			.catch(error => console.error('Error:', error));
	});
}

// sendMessage.html 입력이벤트  
function initializeSendMessage() {
	const input = document.getElementById('message-input');
	const micImage = document.getElementById('micImage');
	const uploadImage = document.getElementById('uploadImage');
	const heartImage = document.getElementById('heart-image');
	const sendButton = document.getElementById('send-button');
	const toUserInput = document.getElementById('toUserInput');
	const messageList = document.getElementById('messageList');

	// messageList.scrollTop = messageList.scrollHeight;

	// 각 요소가 존재하는지 확인 후 스타일 속성 접근
	if (sendButton && heartImage) {
		// 초기 상태 설정
		sendButton.style.display = 'none';
		heartImage.style.display = 'block';

		if (input) {
			input.addEventListener('input', function() {
				if (input.value.trim() !== '') {
					sendButton.style.display = 'block'; // 입력이 있으면 버튼 표시
					if (micImage) micImage.style.display = 'none'; // 입력이 있으면 마이크 이미지 숨김
					if (uploadImage) uploadImage.style.display = 'none'; // 입력이 있으면 업로드 이미지 숨김
					heartImage.style.display = 'none'; // 입력이 있으면 하트 이미지 숨김
				} else {
					sendButton.style.display = 'none'; // 입력이 없으면 버튼 숨김
					if (micImage) micImage.style.display = 'block'; // 입력이 없으면 마이크 이미지 표시
					if (uploadImage) uploadImage.style.display = 'block'; // 입력이 없으면 업로드 이미지 표시
					heartImage.style.display = 'block'; // 입력이 없으면 하트 이미지 표시
				}
			});
		} else {
			console.error('Input element with id="message-input" not found');
		}
	} else {
		console.error('Send button or heart image element not found');
	}

	// 메세지 보내기 버튼 이벤트
	if (sendButton && input && toUserInput) {
		sendButton.addEventListener('click', function(e) {
			e.preventDefault();

			const message = input.value.trim();  // 입력된 메시지 가져오기
			const toUser = toUserInput.textContent.trim();  // 수신자 정보 가져오기

			if (!toUser) {
				alert('수신자가 설정되지 않았습니다.');
				return;
			}

			if (!message) {
				alert('메시지를 입력해주세요.');
				return;
			}

			// AJAX 요청으로 메시지 전송 (실제로 서버로 메시지 전송)
			fetch('/saveMessage', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ message: message, getuser: toUser }),  // 메시지와 수신자 정보 전송
			})
				.then(response => response.text())
				.then(data => {
					if (data.includes("성공적으로")) {
						// 서버 응답이 성공했을 때만 메시지를 화면에 추가
						const messageList = document.getElementById('messageList');
						const newMessage = document.createElement('div');

						// 보낸 메시지 HTML 구조 추가
						newMessage.classList.add('message', 'sent', 'message-text');  // 메시지 스타일 설정
						newMessage.textContent = message;  // 메시지 내용 설정

						// 메시지 리스트에 새로운 메시지 추가
						messageList.appendChild(newMessage);

						// 강제로 레이아웃 다시 계산
						newMessage.offsetHeight;  // 이 줄을 추가하여 레이아웃을 강제 계산

						// 입력 필드 초기화
						input.value = '';

						// 스크롤을 최신 메시지로 이동 (즉시 화면 아래로 스크롤)
						setTimeout(() => {
							messageList.scrollTop = messageList.scrollHeight;
						}, 0);
					} else {
						// 메시지 전송 실패 시 오류 메시지 표시
						alert('메시지 전송에 실패했습니다.');
					}
				})
				.catch(error => {
					console.error('Error:', error);
					alert('메시지 전송 중 오류가 발생했습니다.');
				});
		});
	}
}

// 다른 사람 게시물에서 메뉴 버튼 클릭했을 때 오버레이로 menu창 활성화
const more1Button = document.getElementById('list1button');
const plus1Menu = document.getElementById('plus1menu');
const list1overlay = document.getElementById('list1overlay');

// 다른사람 게시물에서 더보기 클릭시 : overlay와 menu창 활성화
if (more1Button && plus1Menu && list1overlay) {
	more1Button.addEventListener('click', () => {
		plus1Menu.classList.toggle('show');
		list1overlay.classList.toggle('show');
	});

	// menu창 이외의 화면(overlay)클릭시 : overlay와 menu창 비활성화
	list1overlay.addEventListener('click', () => {
		plus1Menu.classList.remove('show');
		list1overlay.classList.remove('show');
	});

	// 취소버튼 클릭시 : overlay와 menu창 비활성화
	plus1Menu.addEventListener('click', (event) => {
		const option = event.target.textContent;
		if (option === '취소') {
			plus1Menu.classList.remove('show');
			list1overlay.classList.remove('show');
		}
		//console.log(`Selected option: ${option}`);
	});
}

// 자신의 게시물에서 메뉴 버튼 클릭 했을 때 오버레이로 menu창 활성화
document.addEventListener('DOMContentLoaded', () => {
	document.querySelectorAll('.list2button').forEach(button => {
		button.addEventListener('click', (event) => {
			const postSeq = button.getAttribute('data-post-seq');  // 게시글 seq 가져오기
			const boardContent = button.getAttribute('data-board-content');
			//console.log('list2button clicked for postSeq:', postSeq);  // 디버깅 메시지

			const plus2Menu = document.getElementById(`plus2menu-${postSeq}`);
			const list2overlay = document.getElementById(`list2overlay-${postSeq}`);

			// 메뉴와 오버레이가 제대로 선택되었는지 확인
			if (!plus2Menu || !list2overlay) {
				console.error(`Menu or overlay not found for postSeq: ${postSeq}`);
				return;  // 오류 발생 시 처리 중단
			}

			// 메뉴 및 오버레이 표시
			plus2Menu.classList.toggle('show');
			list2overlay.classList.toggle('show');
			//console.log('Menu and overlay toggled for postSeq:', postSeq);

			// 오버레이 클릭 시 메뉴 닫기
			list2overlay.addEventListener('click', () => {
				plus2Menu.classList.remove('show');
				list2overlay.classList.remove('show');
				//console.log('Overlay clicked, hiding menu and overlay for postSeq:', postSeq);
			});

			// 메뉴 내 옵션 선택 시 동작 처리
			plus2Menu.addEventListener('click', (event) => {
				const option = event.target.textContent.trim();
				//console.log('Menu option selected for postSeq:', postSeq, 'Option:', option);

				if (option === '삭제') {
					// 삭제 동작 처리
					//console.log('Deleting post with postSeq:', postSeq);
					location.href = `/instaboardDelete?seq=${postSeq}`;
				} else if (option === '취소') {
					// 메뉴 및 오버레이 숨기기
					plus2Menu.classList.remove('show');
					list2overlay.classList.remove('show');
					//console.log('Cancel selected, hiding menu and overlay for postSeq:', postSeq);
				} else if (option === '수정') {
					location.href = `/instaModifyForm?seq=${postSeq}&content=${boardContent}`;
				}
			}, { once: true });  // 이벤트가 중복해서 바인딩되지 않도록 설정
		});
	});
});

// 댓글의 버튼 클릭 시 자신 아이디일 경우 메뉴 창 오버레이
document.addEventListener('DOMContentLoaded', () => {

	document.querySelectorAll('.comment').forEach(commentElement => {
		const button = commentElement.querySelector('.list3button');

		if (button) {
			button.addEventListener('click', (event) => {
				//console.log("list3button was clicked");

				const commentId = commentElement.getAttribute('data-comment-id'); // 댓글 작성자 ID
				const postSeq = commentElement.getAttribute('data-post-seq'); // 게시글 ID
				const getId = commentElement.getAttribute('data-get-id'); // 댓글 고유 ID

				//console.log(`Comment ID: ${commentId}, Post Seq: ${postSeq}, Get ID: ${getId}`);

				const plus3Menu = commentElement.querySelector(`#plus3menu-${getId}`);
				const list3overlay = commentElement.querySelector(`#list3overlay-${getId}`);

				//console.log(`Attempted to find #plus3menu-${getId} and #list3overlay-${getId}`);

				if (!plus3Menu || !list3overlay) {
					console.error('Menu or overlay not found');
					return;
				}

				plus3Menu.classList.toggle('show');
				list3overlay.classList.toggle('show');

				list3overlay.addEventListener('click', () => {
					//console.log('Overlay clicked, hiding menu and overlay');
					plus3Menu.classList.remove('show');
					list3overlay.classList.remove('show');
				});

				plus3Menu.addEventListener('click', (event) => {
					const option = event.target.textContent.trim();
					//console.log(`Menu option selected: ${option}`);

					if (option === '취소') {
						//console.log('Cancel selected, hiding menu and overlay');
						plus3Menu.classList.remove('show');
						list3overlay.classList.remove('show');
					} else if (option === '삭제') {
						//console.log(`Deleting comment with ID: ${commentId}`);
						location.href = `/deleteComment?commentId=${commentId}&post_seq=${postSeq}&getId=${getId}`;
					}
				}, { once: true });
			});
		}
	});
});

// 댓글의 버튼 클릭시 다른 아이디일 경우 메뉴창 오버레이
document.addEventListener('DOMContentLoaded', () => {

	document.querySelectorAll('.comment').forEach(commentElement => {
		const button = commentElement.querySelector('.list4button');

		if (button) {
			button.addEventListener('click', (event) => {
				//console.log("list4button was clicked");

				const commentId = commentElement.getAttribute('data-comment-id'); // 댓글 작성자 ID
				const postSeq = commentElement.getAttribute('data-post-seq'); // 게시글 ID
				const getId = commentElement.getAttribute('data-get-id'); // 댓글 고유 ID

				//console.log(`Comment ID: ${commentId}, Post Seq: ${postSeq}, Get ID: ${getId}`);

				const plus4Menu = commentElement.querySelector(`#plus4menu-${getId}`);
				const list4overlay = commentElement.querySelector(`#list4overlay-${getId}`);

				//console.log(`Attempted to find #plus4menu-${getId} and #list4overlay-${getId}`);

				if (!plus4Menu || !list4overlay) {
					console.error('Menu or overlay not found');
					return;
				}

				plus4Menu.classList.toggle('show');
				list4overlay.classList.toggle('show');

				list4overlay.addEventListener('click', () => {
					//console.log('Overlay clicked, hiding menu and overlay');
					plus4Menu.classList.remove('show');
					list4overlay.classList.remove('show');
				});

				plus4Menu.addEventListener('click', (event) => {
					const option = event.target.textContent.trim();
					//console.log(`Menu option selected: ${option}`);

					if (option === '취소') {
						//console.log('Cancel selected, hiding menu and overlay');
						plus4Menu.classList.remove('show');
						list4overlay.classList.remove('show');
					} else if (option === '신고') {
						location.href = "";
					}
				}, { once: true });
			});
		}
	});
});

// 게시글 공유하기 버튼 클릭이벤트
let selectedUsers = [];  // 선택된 유저를 저장하는 배열

// 팝업창 띄우기
const msgButton = document.getElementById('msgButton');

if (msgButton) {
	msgButton.addEventListener('click', function() {
		resetPopup();  // 팝업 열 때 선택 상태 초기화
		document.getElementById('post_popup').style.display = 'block';
		renderUserList(JSON.parse(document.getElementById('followMListRandom_hidden').value));  // 추천 리스트 렌더링
	});
}

function openPopup() {
	document.getElementById("popupBackground").style.display = "block";
	document.getElementById("post_popup").style.display = "block";
}

// 팝업 닫기 함수
function closePopup() {
	resetPopup();
	document.getElementById('post_popup').style.display = 'none';
	document.getElementById("popupBackground").style.display = "none";
	document.getElementById("post_popup").style.display = "none";
}

// 팝업창 초기화 함수
function resetPopup() {
	selectedUsers = [];  // 선택된 유저 초기화
	const selectedUsersContainer = document.getElementById('selected_users');
	selectedUsersContainer.innerHTML = '';  // 선택된 유저 목록 초기화

	// 모든 체크박스 해제
	document.querySelectorAll('.post_share_checkbox').forEach(function(checkbox) {
		checkbox.checked = false;
	});

	// 보내기 버튼 비활성화
	const sendButton = document.querySelector('#share_submit_button_ok');
	sendButton.disabled = true;
	sendButton.textContent = "보내기";

	// 검색창 초기화
	const searchInput = document.getElementById('search_all_user');
	searchInput.value = '';  // 검색창 값 초기화
}

// 팝업창 내 검색 이벤트	
const searchInput = document.getElementById('search_all_user');

if (searchInput) {
	searchInput.addEventListener('input', function() {
		const searchValue = this.value.trim().toLowerCase();
		const titleElement = document.getElementById('list_title');  // 제목을 변경할 요소

		if (searchValue === '') {
			titleElement.textContent = '추천';  // 제목을 "추천"으로 유지
			renderUserList(JSON.parse(document.getElementById('followMListRandom_hidden').value));  // 추천 리스트 다시 렌더링
			return;
		}

		const allUserList = JSON.parse(document.getElementById('all_user_list_hidden').value);  // 전체 유저 리스트 가져오기
		const filteredUsers = allUserList.filter(user =>
			user.userid.toLowerCase().includes(searchValue) ||
			user.username.toLowerCase().includes(searchValue)
		);

		// 검색 결과 렌더링
		titleElement.textContent = '검색 결과';  // 제목을 "검색 결과"로 변경
		renderUserList(filteredUsers);  // 필터링된 목록을 출력
	});
}


// 검색 결과 출력 함수 수정
function renderUserList(users) {
	const followerList = document.getElementById('print_followerList');
	followerList.innerHTML = '';  // 기존 리스트 초기화

	if (users.length === 0) {
		followerList.innerHTML = '<li>검색 결과가 없습니다.</li>';  // 결과가 없을 때 메시지
		return;
	}

	users.forEach(user => {
		const listItem = document.createElement('li');
		listItem.style.display = 'flex';
		listItem.style.alignItems = 'center';
		listItem.style.marginBottom = '10px';

		// 프로필 이미지
		const img = document.createElement('img');
		img.src = `/storage/${user.pfimage}`;
		img.alt = user.username;
		img.style.width = '40px';
		img.style.height = '40px';
		img.style.borderRadius = '50%';
		img.style.marginRight = '10px';

		// 이름과 유저명
		const userInfo = document.createElement('div');
		const username = document.createElement('strong');
		username.textContent = user.username;
		const userid = document.createElement('small');
		userid.textContent = user.userid;

		userInfo.appendChild(username);
		userInfo.appendChild(document.createElement('br'));
		userInfo.appendChild(userid);

		// 체크박스
		const checkbox = document.createElement('input');
		checkbox.type = 'checkbox';
		checkbox.value = user.userid;
		checkbox.className = 'post_share_checkbox';
		checkbox.style.marginLeft = 'auto';

		// 이미 선택된 유저라면 체크된 상태 유지
		if (selectedUsers.some(u => u.userid === user.userid)) {
			checkbox.checked = true;
		}

		// 체크박스 선택/해제 이벤트 처리
		checkbox.addEventListener('change', function() {
			if (checkbox.checked) {
				selectedUsers.push({ userid: user.userid, username: user.username });
			} else {
				selectedUsers = selectedUsers.filter(u => u.userid !== user.userid);
			}

			updateSelectedUsers();  // 선택된 유저 목록 업데이트
			updateSendButton();  // 버튼 상태 업데이트
		});

		listItem.appendChild(img);
		listItem.appendChild(userInfo);
		listItem.appendChild(checkbox);
		followerList.appendChild(listItem);
	});

	// **리스트 렌더링 후 상단의 선택된 유저 목록을 업데이트**
	updateSelectedUsers();
}
// 보내기 버튼 클릭 이벤트(선택된 체크박스 값 controller에 전송)
function sendSelectedFollowers() {
	// 선택된 유저 목록을 selectedUsers 배열에서 가져옴
	let selectedFollowers = selectedUsers.map(user => user.userid);  // selectedUsers 배열에서 userid 추출
	const messageText = document.getElementById('message_textarea').value;  // 메시지 입력란에서 값 가져오기
	const sharepostseq = document.getElementById('share_post_seq').value;

	// 선택된 팔로워가 없으면 경고
	if (selectedFollowers.length === 0) {
		alert("선택 된 사람이 없습니다.");
		return;
	}

	// AJAX를 통해 선택된 데이터를 서버로 전송
	fetch('/sendSelectedFollowers', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			followers: selectedFollowers,
			share_message: messageText,
			share_post: sharepostseq
		})  // 배열 데이터를 JSON 형태로 전송
	})
		.then(response => response.json())
		.then(data => {
			if (data.success) {
				alert("전송 완료");
				closePopup();  // 팝업 닫기
			} else {
				alert("전송 중 오류가 발생했습니다.");
			}
		})
		.catch(error => {
			console.error('Error:', error);
			alert("서버와의 통신 중 오류가 발생했습니다.");
		});
}

// 보내기 버튼 활성화 / 비활성화 및 텍스트 변경, 선택된 유저 업데이트
document.querySelectorAll('.post_share_checkbox').forEach(function(checkbox) {
	checkbox.addEventListener('change', function() {
		updateSelectedUsers();  // 선택된 유저 목록 업데이트
		updateSendButton();  // 버튼 상태 업데이트
	});
});

// 보내기 버튼 상태 업데이트 함수
function updateSendButton() {
	const sendButton = document.querySelector('#share_submit_button_ok');
	const checkedCount = selectedUsers.length;

	if (checkedCount === 0) {
		sendButton.disabled = true;  // 선택된 체크박스가 없으면 버튼 비활성화
		sendButton.textContent = "보내기";  // 기본 텍스트
	} else if (checkedCount === 1) {
		sendButton.disabled = false;  // 한 명 선택 시 버튼 활성화
		sendButton.textContent = "보내기";  // 기본 텍스트 유지
	} else if (checkedCount > 1) {
		sendButton.disabled = false;  // 두 명 이상 선택 시 버튼 활성화
		sendButton.textContent = "따로 보내기";  // 텍스트를 '따로 보내기'로 변경
	}
}

// 선택된 유저 목록을 업데이트하는 함수
function updateSelectedUsers() {
	const selectedUsersContainer = document.getElementById('selected_users');
	const messageBox = document.getElementById('share_message_box'); // 메시지 창 가져오기
	selectedUsersContainer.innerHTML = ''; // 기존의 선택된 유저 목록을 초기화

	if (selectedUsers.length > 0) {
		messageBox.style.display = 'block';  // 유저가 선택되었을 때 메시지 창 표시
	} else {
		messageBox.style.display = 'none';  // 선택된 유저가 없으면 메시지 창 숨김
	}

	// **selectedUsers 배열에 있는 유저를 기준으로 항상 업데이트**
	selectedUsers.forEach(user => {
		// 선택된 유저 태그 생성
		const selectedUserTag = document.createElement('span');
		selectedUserTag.style.marginRight = '8px';
		selectedUserTag.style.padding = '2px 5px';
		selectedUserTag.style.border = '1px solid #dbdbdb';
		selectedUserTag.style.borderRadius = '4px';
		selectedUserTag.style.backgroundColor = '#f0f0f0';
		selectedUserTag.style.display = 'inline-flex';
		selectedUserTag.style.alignItems = 'center';

		// 유저 이름 텍스트 추가
		const userText = document.createElement('span');
		userText.textContent = user.username;
		userText.style.fontSize = '12px';
		selectedUserTag.appendChild(userText);

		// X 버튼 생성
		const removeButton = document.createElement('span');
		removeButton.textContent = ' X';
		removeButton.style.marginLeft = '6px';  // X 버튼과 텍스트 사이 간격 축소
		removeButton.style.cursor = 'pointer';
		removeButton.style.color = 'red';
		removeButton.style.fontSize = '12px';  // X 버튼 글씨 크기 축소

		// X 버튼 클릭 시 선택 해제 및 체크박스 해제
		removeButton.addEventListener('click', function() {
			// selectedUsers 배열에서 해당 유저 제거
			selectedUsers = selectedUsers.filter(u => u.userid !== user.userid);

			// 해당 유저의 체크박스 해제
			const correspondingCheckbox = document.querySelector(`input[value="${user.userid}"]`);
			if (correspondingCheckbox) {
				correspondingCheckbox.checked = false;
			}

			// 선택된 유저 목록 업데이트
			updateSelectedUsers();
			updateSendButton();  // 버튼 상태 업데이트
		});

		// X 버튼 추가
		selectedUserTag.appendChild(removeButton);

		// 선택된 유저 태그 추가
		selectedUsersContainer.appendChild(selectedUserTag);
	});
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
// 왼쪽 슬라이드 패널 검색기능 
document.addEventListener('DOMContentLoaded', function() {
	if (typeof loginedId !== 'undefined' && loginedId !== null) {
		// console.log("Logged in user:", loginedId);
		const searchResultDiv = document.getElementById('print_search_result');
		const searchTextField = document.getElementById('search_textField');
		const clearSearchButton = document.getElementById('clear_search');

		// Spring에서 넘겨받은 데이터를 JSON으로 파싱
		allUserList = JSON.parse(allUserList); // 문자열을 배열로 변환

		let currentXHR = null;  // 현재 진행 중인 AJAX 요청을 저장

		// X 버튼을 눌렀을 때 검색창과 검색 결과 초기화
		clearSearchButton.addEventListener('click', function() {
			searchTextField.value = '';  // 검색 입력란 초기화
			searchResultDiv.innerHTML = '';  // 검색 결과 초기화
			searchTextField.focus();  // 검색창에 다시 포커스
		});

		if (searchTextField) {
			searchTextField.addEventListener('input', function() {
				const searchValue = this.value.trim().toLowerCase();

				// 검색어가 빈 문자열일 때는 검색 결과를 초기화하고 종료
				if (searchValue === '') {
					searchResultDiv.innerHTML = '';
					return;
				}

				searchResultDiv.innerHTML = ''; // 기존 결과 초기화

				if (currentXHR) {
					currentXHR.abort();  // 이전에 진행 중이던 AJAX 요청 취소
				}

				// 만약 검색어가 '#'으로 시작한다면 서버로 AJAX 요청
				if (searchValue.startsWith('#') && searchValue.length > 1) {
					const hashtag = searchValue.substring(1); // '#' 제거

					currentXHR = new XMLHttpRequest();
					currentXHR.open('GET', `/searchHashtagInfo?hashtagName=${encodeURIComponent(hashtag)}`, true);
					currentXHR.onreadystatechange = function() {
						if (currentXHR.readyState === XMLHttpRequest.DONE && currentXHR.status === 200) {
							const postsGroupedByHashtag = JSON.parse(currentXHR.responseText);

							// 검색 결과 초기화
							searchResultDiv.innerHTML = '';

							// 해시태그별로 게시글을 렌더링
							for (const [hashtag, posts] of Object.entries(postsGroupedByHashtag)) {
								const hashtagHeader = document.createElement('h4');
								hashtagHeader.textContent = `#${hashtag}`;
								searchResultDiv.appendChild(hashtagHeader);

								// 해시태그 클릭 시 해당 해시태그의 posts를 서버로 전송하는 이벤트 리스너 추가
								hashtagHeader.addEventListener('click', function() {
									sendHashtagDataToServer(hashtag, posts); // 서버로 해시태그 및 posts 전송
									closeSlide('slide-2');            // 슬라이드 닫기									
									searchResultDiv.innerHTML = '';   // 검색 결과 초기화
									searchTextField.value = '';       // 검색창 초기화
								});

								if (posts.length > 0) {
									const postElement = document.createElement('div');
									postElement.innerHTML = `<p>게시물 ${posts.length}개</p>`;
									searchResultDiv.appendChild(postElement);
								}
							}
							currentXHR = null;  // 요청 완료 후 초기화
						}
					};
					currentXHR.send();
				} else {
					searchResultDiv.innerHTML = '';
				}

				// userid나 username에 검색어가 포함된 유저 필터링
				const filteredUsers = allUserList.filter(user =>
					(user.userid.toLowerCase().includes(searchValue) ||
						user.username.toLowerCase().includes(searchValue)) &&
					user.userid !== loginedId
				);

				// 필터링된 유저 출력
				if (filteredUsers.length > 0) {
					filteredUsers.forEach(user => {
						const userElement = document.createElement('div');
						userElement.style.display = 'flex';
						userElement.style.alignItems = 'center';
						userElement.style.marginBottom = '10px';

						// 프로필 이미지
						const imgElement = document.createElement('img');
						imgElement.src = `/storage/${user.pfimage}`;
						imgElement.alt = `${user.username}'s profile image`;
						imgElement.style.width = '40px';
						imgElement.style.height = '40px';
						imgElement.style.borderRadius = '50%';
						imgElement.style.marginRight = '10px'; // 이미지와 텍스트 사이 여백

						// 유저 정보 (ID와 이름)
						const userInfo = document.createElement('h4');
						userInfo.innerHTML = `${user.userid} <br> ${user.username}`;

						// 클릭 시 해당 유저의 mypage로 이동
						userElement.addEventListener('click', function() {
							window.location.href = `/MyPage?pageid=${user.userid}`; // 해당 유저의 mypage로 이동
						});

						// 프로필 이미지와 유저 정보를 같이 출력
						userElement.appendChild(imgElement);
						userElement.appendChild(userInfo);

						searchResultDiv.appendChild(userElement); // 검색 결과를 출력 영역에 추가
					});
				} else {
					// searchResultDiv.innerHTML = '<h4>검색 결과가 없습니다.</h4>';
				}

			});
		}
	} else {
		//console.log("from_user is not defined or null");
	}
});

// 해시태그 및 posts 데이터를 서버로 전송하는 함수
function sendHashtagDataToServer(hashtag, posts) {
	const xhr = new XMLHttpRequest();
	xhr.open('POST', '/hashtagSearchResultForm', true);  // 서버에 보낼 엔드포인트 설정
	xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

	const data = JSON.stringify({ hashtag: hashtag, posts: posts });

	xhr.onreadystatechange = function() {
		if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
			// 서버에서 처리 후 응답받은 HTML을 feed-section에 로드
			document.querySelector('.feed-section').innerHTML = xhr.responseText;

			// openView 함수 호출
			document.querySelectorAll('.openViewButton_hash').forEach(button => {
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
		}

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
	};

	xhr.send(data);  // 데이터 전송
}

// 슬라이드 창 닫기 
function closeSlide(slideId) {
	const slideElement = document.getElementById(slideId);
	if (slideElement) {
		slideElement.classList.remove('active');  // 슬라이드 닫기
	}
}

// 릴스 버튼 이벤트
function reelsPageEventHandler() {
	const videoElements = document.querySelectorAll('video'); // 모든 비디오 요소 선택

	// 최초 비디오 자동 재생 시도
	if (videoElements.length > 0) {
		videoElements[0].muted = true;  // 첫 번째 비디오 음소거
		videoElements[0].play()         // 첫 번째 비디오 자동 재생
			.then(() => {
				//console.log("첫 번째 비디오 자동 재생 성공");
			})
			.catch(error => {
				//console.error("첫 번째 비디오 자동 재생 중 오류 발생:", error);
			});
	}

	videoElements.forEach(video => {
		// 비디오가 재생될 때 다른 비디오 멈추기
		video.addEventListener('play', function() {
			videoElements.forEach(v => {
				if (v !== video) {
					v.pause(); // 현재 재생 중인 비디오가 아닌 다른 비디오를 일시 정지
				}
			});
		});

		// 비디오를 보이게 할 때 자동 재생 (옵션, 스크롤에 의해 자동 재생)
		const observer = new IntersectionObserver(entries => {
			entries.forEach(entry => {
				if (entry.isIntersecting) {
					entry.target.muted = true;
					entry.target.play().catch(error => {
						console.error("비디오 자동 재생 중 오류 발생:", error);
					});
				} else {
					entry.target.pause(); // 화면에서 벗어날 때 일시 정지
				}
			});
		}, { threshold: 0.5 }); // 비디오의 50% 이상이 보일 때 재생

		observer.observe(video); // 각 비디오에 대해 관찰 시작
	});
	// 좋아요 버튼 클릭 시 이미지 변경 및 카운트 증가
	document.querySelectorAll('.reels-like-button').forEach((likeButton, index) => {
		if (likeButton) {
			likeButton.addEventListener('click', () => {
				const postSeq = likeButton.dataset.postSeq;
				const isLiked = likeButton.dataset.isLiked === 'true';
				likePost(postSeq, isLiked);
			});
		}
	});

	function likePost(postSeq, isLiked) {
		const url = isLiked ? '/unlikePost' : '/likePost';

		fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: new URLSearchParams({
				post_seq: postSeq
			})
		})
			.then(response => response.json())
			.then(data => {
				if (data.status === 'success') {
					const heartImage = document.getElementById('reels_like-img-' + postSeq);
					// 좋아요 개수 업데이트
					document.getElementById('reels_like-count-' + postSeq).innerText = data.likeCount;
					// 좋아요 버튼 이미지 업데이트
					heartImage.src = isLiked ? 'img/heart1.png' : 'img/heart2.png';
					// isLiked 값 토글
					document.getElementById('reels_like-btn-' + postSeq).dataset.isLiked = !isLiked;
				}
			})
			.catch(error => {
				console.error('Error:', error);
			});
	}

	// 릴스에서 게시글 저장 버튼 이벤트
	document.querySelectorAll('.save-button-reels').forEach((saveButton) => {
		saveButton.addEventListener('click', function() {
			const postSeq = saveButton.dataset.postSeq;  // 게시글 번호
			const isSaved = saveButton.dataset.isSaved === 'true';  // 현재 저장 상태 확인

			// 서버에 저장 또는 저장 해제를 요청
			toggleSave(postSeq, isSaved);  // 버튼 자체를 넘김
		});
	});

	function toggleSave(postSeq, isSaved) {
		// 저장과 저장 해제를 모두 처리하는 API 호출
		fetch(`/toggleSavePost/${postSeq}`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: new URLSearchParams({
				post_seq: postSeq
			})
		})
			.then(response => response.json())
			.then(data => {
				//console.log(data);
				if (data.saved) {
					// 서버에서 저장된 상태로 돌아오면 UI 업데이트
					const saveImage = document.getElementById('saveImage-' + postSeq);
					saveImage.src = isSaved ? 'img/save.png' : 'img/save5.png';
					document.querySelector(`#save1Button-${postSeq}`).dataset.isSaved = !isSaved;
				}
			})
			.catch(error => {
				console.error('Error:', error);
				// 에러 발생 시 사용자에게 알림 (선택 사항)
				alert('저장 상태를 변경하는 중 오류가 발생했습니다.');
			});
	}

	// 페이지 로드시 게시글 저장 상태 적용
	// 요소가 존재하는지 확인 후, 저장된 게시글 상태 적용
	const reelsSaveButtons = document.querySelectorAll('.save-button-reels');
	if (reelsSaveButtons.length > 0) {
		fetch('/savedPosts')
			.then(response => response.json())
			.then(savedPostIds => {
				savedPostIds.forEach(postSeq => {
					const saveButton = document.querySelector(`.save-button-reels[data-post-seq="${postSeq}"]`);
					if (saveButton && saveButton.dataset.isSaved !== 'true') {
						const saveImage = document.getElementById('saveImage-' + postSeq);
						saveImage.src = 'img/save5.png';  // 저장된 상태로 이미지 변경
						saveButton.dataset.isSaved = 'true';  // 저장 상태 업데이트
					}
				});
			})
			.catch(error => console.error('Error:', error));
	} else {
		//console.log("Reels 페이지의 save-button-reels 요소가 로드되지 않았습니다.");
	}


	// 설명 글 '더보기...' 클릭 시 전체 내용 보기 기능 구현
	document.querySelectorAll('.reels_read-more').forEach(readMoreBtn => {
		readMoreBtn.addEventListener('click', () => {
			const description = readMoreBtn.parentElement;
			const reelInfo = description.closest('.reels_info');
			if (description.classList.contains('expanded')) {
				description.classList.remove('expanded');
				readMoreBtn.textContent = '더보기...';
				reelInfo.classList.remove('expanded');
			} else {
				description.classList.add('expanded');
				readMoreBtn.textContent = '접기';
				reelInfo.classList.add('expanded');
			}
		});
	});
	// 댓글 팝업 열기
	document.querySelectorAll('.commentButton').forEach((commentButton) => {
		commentButton.addEventListener('click', function() {
			const postSeq = commentButton.dataset.postSeq;
			openCommentPopup(postSeq);
		});
	});

	function openCommentPopup(postSeq) {
		// 팝업 창 열기
		const popup = document.getElementById('comment-popup');
		const overlay = document.getElementById('comment-popup-overlay');
		popup.style.display = 'block';
		overlay.style.display = 'block';

		// 서버에서 댓글을 받아오는 로직 (Ajax 처리)
		fetch(`/${postSeq}/comments/all`)
			.then(response => response.json())
			.then(data => {
				const commentList = document.getElementById('comment-list');
				commentList.innerHTML = ''; // 기존 댓글 제거

				if (data && data.length > 0) {
					// 서버에서 받아온 댓글을 추가
					data.forEach(comment => {
						const commentDiv = document.createElement('div');
						commentDiv.classList.add('comment');
						commentDiv.id = `comment-${comment.id}`;
						commentDiv.innerHTML = `<strong>${comment.userid}</strong>: ${comment.content}`;
						commentList.appendChild(commentDiv);
					});
				}
			})
			.catch(error => {
				console.error('Error fetching comments:', error);
				const errorDiv = document.createElement('div');
				errorDiv.classList.add('error');
				errorDiv.innerText = '댓글을 불러오는 중 오류가 발생했습니다.';
				commentList.appendChild(errorDiv);
			});
	}

	function closeCommentPopup() {
		const popup = document.getElementById('comment-popup');
		const overlay = document.getElementById('comment-popup-overlay');
		const popupBtn = document.getElementById('close-popup-btn');
		popup.style.display = 'none';
		overlay.style.display = 'none';
		popupBtn.style.display = 'none';
	}

	// 닫기 버튼과 배경 클릭 시 팝업 닫기
	document.getElementById('comment-popup-overlay').addEventListener('click', closeCommentPopup);
	document.getElementById('close-popup-btn').addEventListener('click', closeCommentPopup);
};

// dm item 이벤트 (메세지)
document.addEventListener('DOMContentLoaded', function() {
	// 모든 dm-item을 선택하여 클릭 이벤트 리스너를 추가	
	const dmItems = document.querySelectorAll('.dm-item');

	dmItems.forEach(function(item) {
		item.addEventListener('click', function() {
			// data-userid 속성에서 해당 사용자의 userid 가져오기
			const toUser = this.getAttribute('data-userid');
			const encodedToUser = encodeURIComponent(toUser);
			const feedSection = document.querySelector('.feed-section');

			// GET 요청으로 sendMessage.html 가져오기, 인코딩된 사용자명을 쿼리 파라미터로 전달
			fetch(`/sendMessage?to_user=${encodedToUser}`)
				.then(response => response.text())
				.then(data => {
					feedSection.innerHTML = data; // 중앙 패널에 HTML 삽입
					// 필요하다면 여기에 새로운 HTML에 대한 이벤트 핸들러를 초기화   
					initializeSendMessage();
				})
				.catch(error => console.error('Error loading the message page:', error));
		});
	});
});

