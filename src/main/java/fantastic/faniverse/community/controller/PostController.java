package fantastic.faniverse.community.controller;

import fantastic.faniverse.community.controller.dto.PostCreateRequest;
import fantastic.faniverse.community.controller.dto.PostResponse;
import fantastic.faniverse.community.controller.dto.PostUpdateForm;
import fantastic.faniverse.community.domain.Post;
import fantastic.faniverse.community.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/search")
    public ResponseEntity<Object> searchPostsByTitle(
            @RequestParam(value = "boardId") Long boardId,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<PostResponse> list = postService
                .searchPostsByTitle(boardId, title, page, pageSize).stream()
                .map(PostResponse::convertPostResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPost(
            @PathVariable("postId") Long postId
    ) {
        Post post = postService.getPost(postId);
        PostResponse postResponse = PostResponse.convertPostResponse(post);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getBoardPostList(
            @RequestParam(value = "boardId") Integer boardId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<PostResponse> list = postService
                .getBoardPostList(boardId, page, pageSize).stream()
                .map(PostResponse::convertPostResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @RequestBody @Validated PostCreateRequest request,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("user");
        postService.createPost(request.toEntity(), userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<Object> updatePost(
            @RequestBody @Validated PostUpdateForm updateForm,
            @PathVariable("postId") Long postId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("user");
        postService.updatePost(updateForm.toEntity(), postId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Object> deletePost(
            @PathVariable("postId") Long postId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("user");
        postService.deletePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/likes/{postId}")
    public ResponseEntity<Object> likesPost(
            @PathVariable("postId") Long postId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("user");
        postService.likesPost(postId, userId);
        return ResponseEntity.ok().build();
    }
}
