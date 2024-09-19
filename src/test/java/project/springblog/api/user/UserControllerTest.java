package project.springblog.api.user;

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
import project.springblog.api.user.request.UserJoinRequest;
import project.springblog.api.user.request.UserDeleteRequest;
import project.springblog.application.user.UserService;
import project.springblog.application.user.response.UserJoinResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  @DisplayName("회원가입 요청 성공 시 UserJoinResponse와 201 status 반환한다.")
  void 회원가입_성공_테스트() throws Exception {
    UserJoinRequest request = UserJoinRequest.builder()
                                             .email("junseoplee@outlook.com")
                                             .password("junseoplee")
                                             .username("junseoplee")
                                             .build();

    UserJoinResponse response = new UserJoinResponse("junseoplee@outlook.com", "junseoplee");

    when(userService.joinUser(any())).thenReturn(response);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/join")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(request)))
           .andExpect(MockMvcResultMatchers.status().isCreated())
           .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("junseoplee@outlook.com"))
           .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("junseoplee"))
           .andDo(MockMvcResultHandlers.print());
  }

  @Test
  @DisplayName("회원탈퇴 요청 성공 시 204 status 반환한다.")
  void 회원탈퇴_성공_테스트() throws Exception {
    UserDeleteRequest request = UserDeleteRequest.builder()
                                                 .email("junseoplee@outlook.com")
                                                 .password("junseoplee")
                                                 .build();

    doNothing().when(userService).deleteUser(any());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(request)))
           .andExpect(MockMvcResultMatchers.status().isNoContent())
           .andDo(MockMvcResultHandlers.print());
  }
}
