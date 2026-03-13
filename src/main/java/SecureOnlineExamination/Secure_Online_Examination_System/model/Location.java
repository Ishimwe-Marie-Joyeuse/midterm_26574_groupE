package SecureOnlineExamination.Secure_Online_Examination_System.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Single entity for Rwanda admin hierarchy using LocationType enum.
 * Self-referential: Province (parent=null) → District → Sector → Cell → Village.
 * User saves location ONLY through a Location of type VILLAGE.
 */
@Entity
@Table(name = "locations", uniqueConstraints = {
        @UniqueConstraint(name = "uk_location_code", columnNames = "code")
}, indexes = {
        @Index(name = "idx_location_type", columnList = "location_type"),
        @Index(name = "idx_location_parent", columnList = "parent_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    @Column(unique = true, nullable = false, length = 30)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = false, length = 20)
    private LocationType locationType;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Location parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Location> subLocations = new ArrayList<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Location(String code, String name, LocationType locationType, String description, Location parent) {
        this.code = code;
        this.name = name;
        this.locationType = locationType;
        this.description = description;
        this.parent = parent;
    }

    @JsonIgnore
    public Location getProvince() {
        Location current = this;
        while (current.getParent() != null) {
            current = current.getParent();
        }
        return current;
    }
}
