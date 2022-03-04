package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personne.
 */
@Entity
@Table(name = "personne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "data_naissance")
    private LocalDate dataNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "numero_doc")
    private String numeroDoc;

    @Lob
    @Column(name = "profil")
    private byte[] profil;

    @Column(name = "profil_content_type")
    private String profilContentType;

    @Column(name = "telephone")
    private String telephone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "livraison", "fournisseur", "commandeMedicaments", "operateur" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operateur" }, allowSetters = true)
    private Set<Assurance> assurances = new HashSet<>();

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venteMedicaments", "operateur", "paiement" }, allowSetters = true)
    private Set<Vente> ventes = new HashSet<>();

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operateur" }, allowSetters = true)
    private Set<Fournisseur> fournisseurs = new HashSet<>();

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assurance", "operateur" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "commande", "operateur" }, allowSetters = true)
    private Set<Livraison> livraisons = new HashSet<>();

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inventaireMedicaments", "operateur" }, allowSetters = true)
    private Set<Inventaire> inventaires = new HashSet<>();

    @OneToMany(mappedBy = "from")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "from" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operateur" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "packs", "medicaments", "personnes" }, allowSetters = true)
    private Structure structure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Personne nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Personne prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDataNaissance() {
        return this.dataNaissance;
    }

    public Personne dataNaissance(LocalDate dataNaissance) {
        this.setDataNaissance(dataNaissance);
        return this;
    }

    public void setDataNaissance(LocalDate dataNaissance) {
        this.dataNaissance = dataNaissance;
    }

    public String getLieuNaissance() {
        return this.lieuNaissance;
    }

    public Personne lieuNaissance(String lieuNaissance) {
        this.setLieuNaissance(lieuNaissance);
        return this;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNumeroDoc() {
        return this.numeroDoc;
    }

    public Personne numeroDoc(String numeroDoc) {
        this.setNumeroDoc(numeroDoc);
        return this;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public byte[] getProfil() {
        return this.profil;
    }

    public Personne profil(byte[] profil) {
        this.setProfil(profil);
        return this;
    }

    public void setProfil(byte[] profil) {
        this.profil = profil;
    }

    public String getProfilContentType() {
        return this.profilContentType;
    }

    public Personne profilContentType(String profilContentType) {
        this.profilContentType = profilContentType;
        return this;
    }

    public void setProfilContentType(String profilContentType) {
        this.profilContentType = profilContentType;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Personne telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Personne user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setOperateur(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setOperateur(this));
        }
        this.commandes = commandes;
    }

    public Personne commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Personne addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setOperateur(this);
        return this;
    }

    public Personne removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setOperateur(null);
        return this;
    }

    public Set<Assurance> getAssurances() {
        return this.assurances;
    }

    public void setAssurances(Set<Assurance> assurances) {
        if (this.assurances != null) {
            this.assurances.forEach(i -> i.setOperateur(null));
        }
        if (assurances != null) {
            assurances.forEach(i -> i.setOperateur(this));
        }
        this.assurances = assurances;
    }

    public Personne assurances(Set<Assurance> assurances) {
        this.setAssurances(assurances);
        return this;
    }

    public Personne addAssurance(Assurance assurance) {
        this.assurances.add(assurance);
        assurance.setOperateur(this);
        return this;
    }

    public Personne removeAssurance(Assurance assurance) {
        this.assurances.remove(assurance);
        assurance.setOperateur(null);
        return this;
    }

    public Set<Vente> getVentes() {
        return this.ventes;
    }

    public void setVentes(Set<Vente> ventes) {
        if (this.ventes != null) {
            this.ventes.forEach(i -> i.setOperateur(null));
        }
        if (ventes != null) {
            ventes.forEach(i -> i.setOperateur(this));
        }
        this.ventes = ventes;
    }

    public Personne ventes(Set<Vente> ventes) {
        this.setVentes(ventes);
        return this;
    }

    public Personne addVente(Vente vente) {
        this.ventes.add(vente);
        vente.setOperateur(this);
        return this;
    }

    public Personne removeVente(Vente vente) {
        this.ventes.remove(vente);
        vente.setOperateur(null);
        return this;
    }

    public Set<Fournisseur> getFournisseurs() {
        return this.fournisseurs;
    }

    public void setFournisseurs(Set<Fournisseur> fournisseurs) {
        if (this.fournisseurs != null) {
            this.fournisseurs.forEach(i -> i.setOperateur(null));
        }
        if (fournisseurs != null) {
            fournisseurs.forEach(i -> i.setOperateur(this));
        }
        this.fournisseurs = fournisseurs;
    }

    public Personne fournisseurs(Set<Fournisseur> fournisseurs) {
        this.setFournisseurs(fournisseurs);
        return this;
    }

    public Personne addFournisseur(Fournisseur fournisseur) {
        this.fournisseurs.add(fournisseur);
        fournisseur.setOperateur(this);
        return this;
    }

    public Personne removeFournisseur(Fournisseur fournisseur) {
        this.fournisseurs.remove(fournisseur);
        fournisseur.setOperateur(null);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setOperateur(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setOperateur(this));
        }
        this.clients = clients;
    }

    public Personne clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Personne addClient(Client client) {
        this.clients.add(client);
        client.setOperateur(this);
        return this;
    }

    public Personne removeClient(Client client) {
        this.clients.remove(client);
        client.setOperateur(null);
        return this;
    }

    public Set<Livraison> getLivraisons() {
        return this.livraisons;
    }

    public void setLivraisons(Set<Livraison> livraisons) {
        if (this.livraisons != null) {
            this.livraisons.forEach(i -> i.setOperateur(null));
        }
        if (livraisons != null) {
            livraisons.forEach(i -> i.setOperateur(this));
        }
        this.livraisons = livraisons;
    }

    public Personne livraisons(Set<Livraison> livraisons) {
        this.setLivraisons(livraisons);
        return this;
    }

    public Personne addLivraison(Livraison livraison) {
        this.livraisons.add(livraison);
        livraison.setOperateur(this);
        return this;
    }

    public Personne removeLivraison(Livraison livraison) {
        this.livraisons.remove(livraison);
        livraison.setOperateur(null);
        return this;
    }

    public Set<Inventaire> getInventaires() {
        return this.inventaires;
    }

    public void setInventaires(Set<Inventaire> inventaires) {
        if (this.inventaires != null) {
            this.inventaires.forEach(i -> i.setOperateur(null));
        }
        if (inventaires != null) {
            inventaires.forEach(i -> i.setOperateur(this));
        }
        this.inventaires = inventaires;
    }

    public Personne inventaires(Set<Inventaire> inventaires) {
        this.setInventaires(inventaires);
        return this;
    }

    public Personne addInventaire(Inventaire inventaire) {
        this.inventaires.add(inventaire);
        inventaire.setOperateur(this);
        return this;
    }

    public Personne removeInventaire(Inventaire inventaire) {
        this.inventaires.remove(inventaire);
        inventaire.setOperateur(null);
        return this;
    }

    public Set<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<Message> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setFrom(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setFrom(this));
        }
        this.messages = messages;
    }

    public Personne messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public Personne addMessage(Message message) {
        this.messages.add(message);
        message.setFrom(this);
        return this;
    }

    public Personne removeMessage(Message message) {
        this.messages.remove(message);
        message.setFrom(null);
        return this;
    }

    public Set<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setOperateur(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setOperateur(this));
        }
        this.notifications = notifications;
    }

    public Personne notifications(Set<Notification> notifications) {
        this.setNotifications(notifications);
        return this;
    }

    public Personne addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setOperateur(this);
        return this;
    }

    public Personne removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setOperateur(null);
        return this;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Personne structure(Structure structure) {
        this.setStructure(structure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personne)) {
            return false;
        }
        return id != null && id.equals(((Personne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personne{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dataNaissance='" + getDataNaissance() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", numeroDoc='" + getNumeroDoc() + "'" +
            ", profil='" + getProfil() + "'" +
            ", profilContentType='" + getProfilContentType() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
