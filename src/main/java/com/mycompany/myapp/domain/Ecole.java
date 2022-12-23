package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ecole.
 */
@Entity
@Table(name = "ecole")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ecole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_ecole")
    private String nomEcole;

    @Column(name = "adresse_ecole")
    private String adresseEcole;

    @Column(name = "telephone_ecole")
    private Integer telephoneEcole;

    @OneToMany(mappedBy = "ecole")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiants", "ecole" }, allowSetters = true)
    private Set<Filiere> filieres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ecole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEcole() {
        return this.nomEcole;
    }

    public Ecole nomEcole(String nomEcole) {
        this.setNomEcole(nomEcole);
        return this;
    }

    public void setNomEcole(String nomEcole) {
        this.nomEcole = nomEcole;
    }

    public String getAdresseEcole() {
        return this.adresseEcole;
    }

    public Ecole adresseEcole(String adresseEcole) {
        this.setAdresseEcole(adresseEcole);
        return this;
    }

    public void setAdresseEcole(String adresseEcole) {
        this.adresseEcole = adresseEcole;
    }

    public Integer getTelephoneEcole() {
        return this.telephoneEcole;
    }

    public Ecole telephoneEcole(Integer telephoneEcole) {
        this.setTelephoneEcole(telephoneEcole);
        return this;
    }

    public void setTelephoneEcole(Integer telephoneEcole) {
        this.telephoneEcole = telephoneEcole;
    }

    public Set<Filiere> getFilieres() {
        return this.filieres;
    }

    public void setFilieres(Set<Filiere> filieres) {
        if (this.filieres != null) {
            this.filieres.forEach(i -> i.setEcole(null));
        }
        if (filieres != null) {
            filieres.forEach(i -> i.setEcole(this));
        }
        this.filieres = filieres;
    }

    public Ecole filieres(Set<Filiere> filieres) {
        this.setFilieres(filieres);
        return this;
    }

    public Ecole addFiliere(Filiere filiere) {
        this.filieres.add(filiere);
        filiere.setEcole(this);
        return this;
    }

    public Ecole removeFiliere(Filiere filiere) {
        this.filieres.remove(filiere);
        filiere.setEcole(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ecole)) {
            return false;
        }
        return id != null && id.equals(((Ecole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ecole{" +
            "id=" + getId() +
            ", nomEcole='" + getNomEcole() + "'" +
            ", adresseEcole='" + getAdresseEcole() + "'" +
            ", telephoneEcole=" + getTelephoneEcole() +
            "}";
    }
}
