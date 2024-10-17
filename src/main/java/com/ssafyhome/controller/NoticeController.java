package com.ssafyhome.controller;

import com.ssafyhome.model.dto.NoticeDto;
import com.ssafyhome.model.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
		name = "Notice Controller",
		description = "관리 운영에 필요한 공지사항"
)
@RestController
@RequestMapping("/notice")
public class NoticeController {

	private final NoticeService noticeService;

	public NoticeController(NoticeService noticeService) {

		this.noticeService = noticeService;
	}

	@Operation(
			summary = "공지사항 등록",
			description = "작성한 NoticeDto 객체 저장"
	)
	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> registerNotice(
			@RequestBody
			NoticeDto noticeDto
	) {

		noticeService.registerNotice(noticeDto);
		return new ResponseEntity<>("register notice success", HttpStatus.CREATED);
	}

	@Operation(
			summary = "공지사항 상세조회",
			description = "noticeSeq 와 일치하는 NoticeDto 객체 반환"
	)
	@GetMapping("/detail")
	public ResponseEntity<NoticeDto> getNotice(
			@RequestParam
			long noticeSeq
	) {

		NoticeDto noticeDto = noticeService.getNotice(noticeSeq);
		return new ResponseEntity<>(noticeDto, HttpStatus.OK);
	}

	@Operation(
			summary = "공지사항 전체조회",
			description = "모든 NoticeDto 를 List<NoticeDto>로 반환"
	)
	@GetMapping("/{page}")
	public ResponseEntity<List<NoticeDto>> getNoticeList(
			@PathVariable
			int page
	) {

		List<NoticeDto> noticeDtoList = noticeService.getNotices(page);
		return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
	}

	@Operation(
			summary = "공지사항 수정",
			description = "noticeSeq 와 일치하는 NoticeDto 를 새롭게 저장"
	)
	@PutMapping("/{noticeSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateNotice(
			@PathVariable
			long noticeSeq,

			@RequestBody
			NoticeDto noticeDto
	) {

		noticeService.updateNotice(noticeSeq, noticeDto);
		return new ResponseEntity<>("update notice success", HttpStatus.OK);
	}

	@Operation(
			summary = "공지사항 삭제",
			description = "noticeSeq 와 일치하는 NoticeDto 객체 삭제"
	)
	@DeleteMapping("/{noticeSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteNotice(
			@PathVariable
			long noticeSeq
	) {

		noticeService.deleteNotice(noticeSeq);
		return new ResponseEntity<>("delete notice success", HttpStatus.OK);
	}
}
