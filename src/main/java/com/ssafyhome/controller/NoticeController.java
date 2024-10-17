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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
