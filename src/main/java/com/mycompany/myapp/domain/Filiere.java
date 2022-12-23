package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Filiere.
 */
@Entity
@Table(name = "filiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Filiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_filiere")
    private Integer codeFiliere;

    @Column(name = "nom_filiere")
    private String nomFiliere;

    @Column(name = "type_filiere")
    private String typeFiliere;

    @OneToMany(mappedBy = "filiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "filiere" }, allowSetters = true)
    private Set<Etudiant> etudiants = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "filieres" }, allowSetters = true)
    private Ecole ecole;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Filiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodeFiliere() {
        return this.codeFiliere;
    }

    public Filiere codeFiliere(Integer codeFiliere) {
        this.setCodeFiliere(codeFiliere);
        return this;
    }

    public void setCodeFiliere(Integer codeFiliere) {
        this.codeFiliere = codeFiliere;
    }

    public String getNomFiliere() {
        return this.nomFiliere;
    }

    public Filiere nomFiliere(String nomFiliere) {
        this.setNomFiliere(nomFiliere);
        return this;
    }

    public void setNomFiliere(String nomFiliere) {
        this.nomFiliere = nomFiliere;
    }

    public String getTypeFiliere() {
        return this.typeFiliere;
    }

    public Filiere typeFiliere(String typeFiliere) {
        this.setTypeFiliere(typeFiliere);
        return this;
    }

    public void setTypeFiliere(String typeFiliere) {
        this.typeFiliere = typeFiliere;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.setFiliere(null));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.setFiliere(this));
        }
        this.etudiants = etudiants;
    }

    public Filiere etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Filiere addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setFiliere(this);
        return this;
    }

    public Filiere removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setFiliere(null);
        return this;
    }

    public Ecole getEcole() {
        return this.ecole;
    }

    public void setEcole(Ecole ecole) {
        this.ecole = ecole;
    }

    public Filiere ecole(Ecole ecole) {
        this.setEcole(ecole);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Filiere)) {
            return false;
        }
        return id != null && id.equals(((Filiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Filiere{" +
            "id=" + getId() +
            ", codeFiliere=" + getCodeFiliere() +
            ", nomFiliere='" + getNomFiliere() + "'" +
            ", typeFiliere='" + getTypeFiliere() + "'" +
            "}";
    }
}
