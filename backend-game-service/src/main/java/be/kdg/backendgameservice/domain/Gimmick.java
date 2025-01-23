package be.kdg.backendgameservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Gimmick {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private int xpCost;
    private int balanceCost;
    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gimmick gimmick = (Gimmick) o;
        return Objects.equals(id, gimmick.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
