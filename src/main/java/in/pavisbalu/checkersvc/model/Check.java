package in.pavisbalu.checkersvc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Getter
@Setter
@Entity
@Table(name = "checks")
@Validated
@ToString
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @Column(nullable = false)
    @NotBlank
    private String url;
    @Column(nullable = false)
    @NotNull
    private Duration intervalDuration;

    // Notification Settings
    private String notificationType = "log"; // email, slack, teams, etc.
    private String notificationSettingsEncodedAsJson;

    // Connection Params
    @Column(nullable = false)
    private Integer connectionTimeout = 5000;
    @Column(nullable = false)
    private Long responseTimeout = 5000L;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column
    private Long lastRan;
    @Column
    private ResultType status;
    @Column
    private Long statusSince;
    @Column
    private Long avgResponseTime;

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
