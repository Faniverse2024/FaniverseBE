package fantastic.faniverse.community.controller;

import fantastic.faniverse.community.controller.dto.CommentCreateRequest;
import fantastic.faniverse.community.controller.dto.CommentResponse;
import fantastic.faniverse.community.controller.dto.CommentUpdateForm;
import fantastic.faniverse.community.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create/{postId}")
    public ResponseEntity<Object> creatComment(
            @PathVariable("postId") Long postId,
            @RequestBody @Validated CommentCreateRequest request,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        commentService.createComment(request.toEntity(), postId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{postId}")
    public ResponseEntity<Object> getCommentList(
            @PathVariable("postId") Long postId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<CommentResponse> commentList = commentService
                .getCommentList(postId, page, pageSize).stream()
                .map(CommentResponse::convertCommentResponse).collect(Collectors.toList());
        return ResponseEntity.ok(commentList);
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<Object> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody @Validated CommentUpdateForm updateForm,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        commentService.updateComment(updateForm.toEntity(), commentId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable("commentId") Long commentId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        commentService.delete(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
