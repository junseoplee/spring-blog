package project.springblog.api.article;

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
import project.springblog.api.article.request.ArticleCreateRequest;
import project.springblog.application.article.ArticleService;
import project.springblog.application.article.response.ArticleCreateResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ArticleControllerTest {

  @Mock
  private ArticleService articleService;

  @InjectMocks
  private ArticleController articleController;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
  }

  @Test
  @DisplayName("글 작성 요청 성공 시 ArticleCreateResponse와 201 status 반환한다.")
  void 글작성_성공_테스트() throws Exception {
    ArticleCreateRequest request = ArticleCreateRequest.builder()
                                                       .email("junseoplee@outlook.com")
                                                       .password("junseoplee")
                                                       .title("junseoplee")
                                                       .content("junseoplee")
                                                       .build();

    ArticleCreateResponse response = new ArticleCreateResponse(1L, "junseoplee@outlook.com", "junseoplee", "junseoplee");

    when(articleService.createArticle(any())).thenReturn(response);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/articles")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(request)))
           .andExpect(MockMvcResultMatchers.status().isCreated())
           .andExpect(MockMvcResultMatchers.jsonPath("$.articleId").value(1L))
           .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("junseoplee@outlook.com"))
           .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("junseoplee"))
           .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("junseoplee"))
           .andDo(MockMvcResultHandlers.print());
  }
}
