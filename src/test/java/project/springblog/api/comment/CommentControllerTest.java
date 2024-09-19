package project.springblog.api.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.springblog.api.comment.request.CommentCreateRequest;
import project.springblog.application.comment.CommentService;
import project.springblog.application.comment.response.CommentCreateResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CommentControllerTest {

  @Mock
  private CommentService commentService;

  @InjectMocks
  private CommentController commentController;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
  }

  @Test
  @DisplayName("댓글 작성 요청 성공 시 CommentCreateResponse와 201 status 반환한다.")
  void 댓글작성_성공_테스트() throws Exception {
    CommentCreateRequest request = CommentCreateRequest.builder()
                                                       .email("test@test.com")
                                                       .password("test-pw")
                                                       .content("test")
                                                       .build();

    CommentCreateResponse response = new CommentCreateResponse(1L, "test@test.com", "test");

    when(commentService.createComment(any(), any())).thenReturn(response);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments/1")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(request)))
           .andExpect(MockMvcResultMatchers.status().isCreated())
           .andExpect(MockMvcResultMatchers.jsonPath("$.commentId").value(1L))
           .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@test.com"))
           .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("test"))
           .andDo(MockMvcResultHandlers.print());
  }
}
