# API 명세서

## 회원 관련 API

### 회원 가입

| Method | URI             | Description  | Request Body Fields                                    | Response Body Fields                    | Status Code  |
|--------|-----------------|--------------|-------------------------------------------------------|-----------------------------------------|--------------|
| POST   | /api/users/join | 회원 가입     | `email`, `password`, `username`                        | `email`, `username`                     | 201 Created  |

---

### 회원 탈퇴

| Method | URI               | Description  | Request Body Fields                             | Response Body Fields | Status Code  |
|--------|-------------------|--------------|-------------------------------------------------|----------------------|--------------|
| DELETE | /api/users/delete | 회원 탈퇴     | `email`, `password`                             | 없음                 | 204 No Content |

---

## 게시글 관련 API

### 게시글 작성하기

| Method | URI               | Description  | Request Body Fields                                  | Response Body Fields                              | Status Code  |
|--------|-------------------|--------------|-----------------------------------------------------|--------------------------------------------------|--------------|
| POST   | /api/articles      | 게시글 작성   | `email`, `password`, `title`, `content`              | `articleId`, `email`, `title`, `content`          | 201 Created  |

---

### 게시글 수정하기

| Method | URI                        | Description  | Request Body Fields                                  | Response Body Fields                              | Status Code  |
|--------|----------------------------|--------------|-----------------------------------------------------|--------------------------------------------------|--------------|
| PATCH  | /api/articles/{articleId}   | 게시글 수정   | `email`, `password`, `title`, `content`              | `articleId`, `email`, `title`, `content`          | 200 OK       |

---

### 게시글 삭제하기

| Method | URI                        | Description  | Request Body Fields                                  | Response Body Fields                              | Status Code  |
|--------|----------------------------|--------------|-----------------------------------------------------|--------------------------------------------------|--------------|
| DELETE | /api/articles/{articleId}   | 게시글 삭제   | `email`, `password`                                  | 없음                                               | 204 No Content |

---

## 댓글 관련 API

### 댓글 작성하기

| Method | URI                          | Description  | Request Body Fields                                   | Response Body Fields                                 | Status Code  |
|--------|------------------------------|--------------|------------------------------------------------------|-----------------------------------------------------|--------------|
| POST   | /api/comments/{articleId}     | 댓글 작성     | `email`, `password`, `content`                        | `commentId`, `email`, `content`                     | 201 Created  |

---

### 댓글 수정하기

| Method | URI                                          | Description  | Request Body Fields                                   | Response Body Fields                                 | Status Code  |
|--------|----------------------------------------------|--------------|------------------------------------------------------|-----------------------------------------------------|--------------|
| PATCH  | /api/comments/{articleId}/{commentId}         | 댓글 수정     | `email`, `password`, `content`                        | `commentId`, `email`, `content`                     | 200 OK       |

---

### 댓글 삭제하기

| Method | URI                                          | Description  | Request Body Fields                                   | Response Body Fields                                 | Status Code  |
|--------|----------------------------------------------|--------------|------------------------------------------------------|-----------------------------------------------------|--------------|
| DELETE | /api/comments/{articleId}/{commentId}         | 댓글 삭제     | `email`, `password`                                   | 없음                                                | 204 No Content |
