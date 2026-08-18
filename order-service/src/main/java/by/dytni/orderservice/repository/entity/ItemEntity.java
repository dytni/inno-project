package by.dytni.orderservice.repository.entity;

import java.math.BigDecimal;

import by.dytni.commonhibernate.repository.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = "item_entity")
public class ItemEntity  extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    //unique?
    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_price", nullable = false)
    private BigDecimal price;
}
