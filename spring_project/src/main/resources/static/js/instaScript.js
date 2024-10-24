function uploadFile(width, height) {

	// 화면 크기 가져오기
	const screenWidth = window.screen.width;
	const screenHeight = window.screen.height;

	// 새 창의 크기
	const left = (screenWidth - width) / 2;
	const top = (screenHeight - height) / 2;
	
	const options = `width=${width},height=${height},left=${left},top=${top},scrollbars=yes,resizable=no`;

	window.open("/writeBoardForm", "uploadFile", options)

}