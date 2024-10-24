document.addEventListener('DOMContentLoaded', function() {

	// 댓글 모두보기 버튼을 찾아 이벤트 리스너를 추가
	document.querySelectorAll('[id^=show-more-comments]').forEach(function(showMoreBtn) {
		// console.log('showMoreBtn found:', showMoreBtn); // 디버깅: 각 버튼을 제대로 찾는지 확인

		const postSeq = showMoreBtn.getAttribute('data-post-seq');
		const stat_index = showMoreBtn.getAttribute('data-stat-index');
		const commentsSection = document.getElementById('commentsSection-' + postSeq + '-' + stat_index);

		// console.log(`Post Seq: ${postSeq}, Stat Index: ${stat_index}, Comments Section: ${commentsSection}`);

		if (!commentsSection) {
			console.error(`commentsSection element not found for postSeq: ${postSeq}`);
			return;
		}

		// 댓글 모두보기 버튼 클릭 시 숨겨진 댓글을 보여줌
		showMoreBtn.addEventListener('click', function() {
			// console.log('Show more comments button clicked for postSeq:', postSeq); // 디버깅

			// hidden 클래스를 가진 모든 댓글을 찾아서 표시
			const hiddenComments = commentsSection.querySelectorAll('.hidden');
			hiddenComments.forEach(function(comment) {
				comment.classList.remove('hidden'); // hidden 클래스를 제거하여 댓글 표시
				// console.log('Showing comment:', comment); // 각 댓글이 표시되는지 확인
			});

			// "댓글 모두보기" 버튼 숨기기
			showMoreBtn.style.display = 'none';
		});
	});
});



function showSlide(index, slideIndex) {
	const slides = document.getElementById(`carousel-slides-${index}`).children;
	if (slideIndex >= slides.length) {
		slideIndex = 0;
	} else if (slideIndex < 0) {
		slideIndex = slides.length - 1;
	}
	const newTransformValue = -slideIndex * 100;
	document.getElementById(`carousel-slides-${index}`).style.transform = `translateX(${newTransformValue}%)`;
	return slideIndex;
}

function prevSlide(index) {
	let currentSlideIndex = window[`currentSlideIndex_${index}`] || 0;
	currentSlideIndex = showSlide(index, currentSlideIndex - 1);
	window[`currentSlideIndex_${index}`] = currentSlideIndex;
}

function nextSlide(index) {
	let currentSlideIndex = window[`currentSlideIndex_${index}`] || 0;
	currentSlideIndex = showSlide(index, currentSlideIndex + 1);
	window[`currentSlideIndex_${index}`] = currentSlideIndex;
}

document.addEventListener('DOMContentLoaded', function() {
	document.querySelectorAll('.carousel-container').forEach(function(carouselContainer, index) {
		const slideCount = carouselContainer.querySelector('.carousel-slides').children.length;
		if (slideCount <= 1) {
			carouselContainer.querySelector('.left-arrow').style.display = 'none';
			carouselContainer.querySelector('.right-arrow').style.display = 'none';
		}
	});
});