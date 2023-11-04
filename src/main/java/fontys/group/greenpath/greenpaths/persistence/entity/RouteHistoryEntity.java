package fontys.group.greenpath.greenpaths.persistence.entity;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "route_history")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column(name = "origin")
    private String origin;
    @Column(name = "destination")
    private String destination;
    @Column(name = "length")
    private double length;
    @Column(name = "duration")
    private int duration;
    @Column(name = "date")
    private LocalDate date;
}
