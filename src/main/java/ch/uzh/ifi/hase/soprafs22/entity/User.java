package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "user")
@DynamicUpdate
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  public User(){

  }

  public User(long id, String name, String username, String token) {
      this.setId(id);
      this.setName(name);
      this.setUsername(username);
      this.setToken(token);
  }

  public User(long id, String name, String username, String token, String password) {
        this.setId(id);
        this.setName(name);
        this.setUsername(username);
        this.setToken(token);
        this.setPassword(password);
    }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = true)
  private String name;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = true)
  private UserStatus status;

  @Column(columnDefinition = "boolean default false")
  private Boolean loggedIn;

  @Column(nullable = true, unique = false)
  private Date birthDate;

  @Column(nullable = true, unique = false)
  private LocalDate creationDate;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public Boolean getLoggedIn() {
        return this.loggedIn;
    }

    public void setLoggedIn (Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

  public String getPassword() { return password; }

  public void setPassword(String password) { this.password = password; }

    public LocalDate getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    public Date getBirthDate() { return birthDate; }

    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
}
