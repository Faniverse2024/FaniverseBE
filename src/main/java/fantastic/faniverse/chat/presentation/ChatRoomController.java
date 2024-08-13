package fantastic.faniverse.chat.presentation;

import fantastic.faniverse.chat.application.ChatRoomService;
import fantastic.faniverse.chat.presentation.response.CharRoomResponse;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<CharRoomResponse>> chat(@PathVariable Long userId) {
        User currentUser = userService.findUserById(userId);
        return ResponseEntity.ok(chatRoomService.findChatRoomByUser(currentUser)
                .stream()
                .map(CharRoomResponse::of)
                .toList());
    }

    @DeleteMapping("/{roomId}/{userId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        User currentUser = userService.findUserById(userId);
        chatRoomService.deleteChatRoom(roomId, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block/{roomId}/{userId}")
    public ResponseEntity<Void> blockChatRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        User currentUser = userService.findUserById(userId);
        chatRoomService.blockChatRoom(roomId, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create/{sellerId}/{userId}/{productId}")
    public ResponseEntity<Void> createChatRoom(@PathVariable Long sellerId, @PathVariable Long userId, @PathVariable Long productId) {
        User currentUser = userService.findUserById(userId);
        chatRoomService.createChatRoom(currentUser, sellerId, productId);
        return ResponseEntity.ok().build();
    }

}
