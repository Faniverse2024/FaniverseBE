package fantastic.faniverse.chat.presentation;

import fantastic.faniverse.chat.application.ChatService;
import fantastic.faniverse.chat.presentation.request.MessageRequest;
import fantastic.faniverse.chat.presentation.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/send/{roomId}")
    public ResponseEntity<Void> send(@RequestBody MessageRequest messageRequest, @PathVariable Long roomId) {
        Long userId = messageRequest.getUserId(); // MessageRequest에 사용자 ID가 있다고 가정
        chatService.createMessage(userId, roomId, messageRequest); // 변경된 부분
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{roomId}")
    public ResponseEntity<List<MessageResponse>> list(@PathVariable Long roomId, @RequestParam Long lastMessageId) {
        return ResponseEntity.ok(chatService.findLastMessages(roomId, lastMessageId).stream().map(MessageResponse::of).toList());
    }


}
