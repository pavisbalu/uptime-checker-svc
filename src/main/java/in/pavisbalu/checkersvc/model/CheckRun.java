package in.pavisbalu.checkersvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@JsonInclude(Include.NON_NULL)
public class CheckRun {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "check_id", nullable = false, updatable = false)
    private Check check;

    private ResultType result;
    private String errorContext;
    private long responseTimeInMillis;
    private Long createdAt;
}
