package com.karoman0584.nfebackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "nfe")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Nfe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @XmlElement(name = "numero")
    private Integer number;

    @XmlElement(name = "dhRegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerAt;

    @XmlElement(name = "nomeEmitente")
    private String emitterName;

    @XmlElement(name = "nomeDestinatario")
    private String receiverName;

    @XmlElement(name = "valorNota")
    private BigDecimal total;

    private ProcessStatus status;

    @XmlElementWrapper(name = "duplicatas")
    @XmlElement(name = "duplicata")
    @OneToMany(mappedBy = "nfe", cascade = CascadeType.ALL)
    private List<Duplicata> duplicatas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Nfe nfe = (Nfe) o;
        return id != null && Objects.equals(id, nfe.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
