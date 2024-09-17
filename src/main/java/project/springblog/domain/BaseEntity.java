package project.springblog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createDateTime;

  @UpdateTimestamp
  @Column(name = "update_at", insertable = false)
  private LocalDateTime modifiedDateTime;
}
