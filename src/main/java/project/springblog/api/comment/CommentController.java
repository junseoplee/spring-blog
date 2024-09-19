package project.springblog.api.comment;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.springblog.api.comment.request.CommentCreateRequest;
import project.springblog.api.comment.request.CommentUpdateRequest;
import project.springblog.application.comment.CommentService;
import project.springblog.application.comment.response.CommentCreateResponse;
import project.springblog.application.comment.response.CommentUpdateResponse;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  private static final String COMMENT_URI = "/api/comments/";

  @PostMapping("/{articleId}")
  public ResponseEntity<CommentCreateResponse> createComment(@PathVariable Long articleId,
      @Valid @RequestBody CommentCreateRequest request) {
    CommentCreateResponse response = commentService.createComment(articleId, request.toServiceRequest());
    URI location = URI.create(COMMENT_URI + response.getCommentId());
    return ResponseEntity.created(location).body(response);
  }

  @PatchMapping("/{articleId}/{commentId}")
  public ResponseEntity<CommentUpdateResponse> updateComment(
      @PathVariable Long articleId,
      @PathVariable Long commentId,
      @Valid @RequestBody CommentUpdateRequest request
  ) {
    CommentUpdateResponse response = commentService.updateComment(articleId, commentId, request.toServiceRequest());
    return ResponseEntity.ok(response);
  }
}
