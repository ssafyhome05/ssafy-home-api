package com.ssafyhome.controller;

import com.ssafyhome.model.entity.mysql.NoticeEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

	@Operation(
			summary = "",
			description = ""
	)
	@PostMapping("/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> registerNotice(
			@RequestBody
			NoticeEntity noticeEntity
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/detail")
	public ResponseEntity<NoticeEntity> getNotice(
			@RequestParam
			String noticeSeq
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/{page}")
	public ResponseEntity<List<NoticeEntity>> getNoticeList(
			HttpServletRequest request,
			@PathVariable
			Long page
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PutMapping("/{noticeSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateNotice(
			@PathVariable
			String noticeSeq,

			@RequestBody
			NoticeEntity noticeEntity
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@DeleteMapping("/{noticeSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteNotice(
			@PathVariable
			String noticeSeq
	) {

		return null;
	}
}
