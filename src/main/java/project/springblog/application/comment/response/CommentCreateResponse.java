package project.springblog.application.comment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateResponse {

  private final Long commentId;
  private final String email;
  private final String content;
}
