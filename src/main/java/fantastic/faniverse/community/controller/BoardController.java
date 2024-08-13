package fantastic.faniverse.community.controller;

import fantastic.faniverse.community.controller.dto.BoardCreateRequest;
import fantastic.faniverse.community.controller.dto.BoardResponse;
import fantastic.faniverse.community.controller.dto.BoardUpdateForm;
import fantastic.faniverse.community.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<Object> createBoard(
            @RequestBody @Validated BoardCreateRequest request
    ) {
        boardService.createBoard(request.toEntity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchBoardsByName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<BoardResponse> list = boardService
                .searchBoardsByName(name, page, pageSize).stream()
                .map(BoardResponse::convertBoardResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getBoardList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<BoardResponse> boardList = boardService
                .getBoardList(page, pageSize).stream()
                .map(BoardResponse::convertBoardResponse).collect(Collectors.toList());
        return ResponseEntity.ok(boardList);
    }

    @PatchMapping("/update/{boardId}")
    public ResponseEntity<Object> updateBoard(
            @PathVariable("boardId") Long boardId,
            @RequestBody @Validated BoardUpdateForm updateForm
    ) {
        boardService.updateBoard(updateForm.toEntity(), boardId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Object> deleteBoard(
            @PathVariable("boardId") Long boardId
    ) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/likes/{boardId}")
    public ResponseEntity<Object> likesBoard(
            @PathVariable("boardId") Long boardId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("user");
        boardService.likesBoard(boardId, userId);
        return ResponseEntity.ok().build();
    }
}
