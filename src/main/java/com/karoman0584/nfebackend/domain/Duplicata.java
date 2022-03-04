package com.karoman0584.nfebackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "duplicata")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Duplicata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @XmlElement(name = "parcela")
    private Integer installment;

    @XmlElement(name = "valor")
    private BigDecimal total;

    @XmlElement(name = "dataVencimento")
    @Temporal(TemporalType.DATE)
    private Date expireDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "nfe_id", nullable = false)
    private Nfe nfe;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Duplicata that = (Duplicata) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
