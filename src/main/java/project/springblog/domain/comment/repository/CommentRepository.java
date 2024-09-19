package project.springblog.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springblog.domain.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
